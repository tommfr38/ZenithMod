package net.tomi.zenithmod.item;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.tomi.zenithmod.init.ZenithEnchantments;

/**
 * The Zenith Sword.
 *
 * <p>Hold right-click to draw the sword back, release to be thrown in the direction you
 * are looking, the way a firework does under an elytra. Left-click still swings normally.
 *
 * <p>Zenith's Power is also the only enchantment this sword may carry — see
 * {@link #stripForeignEnchantments}.
 */
public class ZenithSwordItem extends Item {

    /** 3 seconds. */
    private static final int COOLDOWN_TICKS = 60;
    /** Boost speed. Riptide III is about 3.0. */
    private static final double BOOST_POWER = 3.0;
    /** Lift off the ground first, or the boost is swallowed by the block underfoot. */
    private static final double GROUND_CLEARANCE = 1.2;
    /** Quarter of a second: long enough to see the wind-up, short enough to feel instant. */
    private static final int WINDUP_TICKS = 5;
    private static final int USE_DURATION = 72000;

    public ZenithSwordItem(Properties properties) {
        super(properties);
    }

    /** Drives the arm pose. The drawn-back sword itself is a model swap, in items/zenith_sword.json. */
    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.SPEAR;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return USE_DURATION;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        // Without the enchantment this is an ordinary sword.
        if (!ZenithEnchantments.hasZenithsPower(stack) || player.getCooldowns().isOnCooldown(stack)) {
            return InteractionResult.PASS;
        }
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) {
            return false;
        }
        if (USE_DURATION - timeLeft < WINDUP_TICKS) {
            return false;
        }
        if (!ZenithEnchantments.hasZenithsPower(stack) || player.getCooldowns().isOnCooldown(stack)) {
            return false;
        }

        // Deliberately unguarded by side. A player's own movement is driven by their
        // client, so a boost applied only on the server is overwritten immediately;
        // vanilla's riptide launch runs on both sides for the same reason.
        if (player.onGround()) {
            player.move(MoverType.SELF, new Vec3(0.0, GROUND_CLEARANCE, 0.0));
        }
        Vec3 look = player.getLookAngle();
        player.push(look.x * BOOST_POWER, look.y * BOOST_POWER, look.z * BOOST_POWER);

        // Same durability path as a swing, so Zenith's Power's Unbreaking roll applies.
        stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
        player.getCooldowns().addCooldown(stack, COOLDOWN_TICKS);
        player.awardStat(net.minecraft.stats.Stats.ITEM_USED.get(this));

        level.playSound(player, player.getX(), player.getY(), player.getZ(),
                SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 1.0F, 1.0F);
        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot) {
        super.inventoryTick(stack, level, entity, slot);
        stripForeignEnchantments(stack);
    }

    /**
     * Zenith's Power is the only enchantment this sword may carry.
     *
     * <p>Keeping the sword out of #minecraft:swords already stops the enchanting table and
     * a survival anvil, but two routes get past that: a creative-mode anvil skips the
     * {@code canEnchant} check outright, and a sword enchanted under an older version of
     * the mod keeps whatever it was given. Stripping on tick closes both, and anything
     * else that manages to write an enchantment onto the stack.
     */
    private static void stripForeignEnchantments(ItemStack stack) {
        ItemEnchantments enchantments = stack.get(DataComponents.ENCHANTMENTS);
        if (enchantments == null || enchantments.isEmpty()) {
            return;
        }
        boolean foreign = false;
        for (Holder<Enchantment> held : enchantments.keySet()) {
            if (!held.is(ZenithEnchantments.ZENITHS_POWER)) {
                foreign = true;
                break;
            }
        }
        if (!foreign) {
            return;
        }

        ItemEnchantments.Mutable kept = new ItemEnchantments.Mutable(enchantments);
        kept.removeIf(held -> !held.is(ZenithEnchantments.ZENITHS_POWER));
        ItemEnchantments result = kept.toImmutable();
        if (result.isEmpty()) {
            stack.remove(DataComponents.ENCHANTMENTS);
        } else {
            stack.set(DataComponents.ENCHANTMENTS, result);
        }
    }
}
