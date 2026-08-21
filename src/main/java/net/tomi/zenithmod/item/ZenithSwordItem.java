package net.tomi.zenithmod.item;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.tomi.zenithmod.init.ZenithEnchantments;

/**
 * Zenith's Power is the only enchantment this sword may carry.
 *
 * <p>Keeping the sword out of #minecraft:swords already stops the enchanting table and a
 * survival anvil, but two routes get past that: a creative-mode anvil skips the
 * {@code canEnchant} check outright, and a sword enchanted under an older version of the
 * mod keeps whatever it was given. Stripping on tick closes both, and anything else that
 * manages to write an enchantment onto the stack.
 */
public class ZenithSwordItem extends Item {

    public ZenithSwordItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, EquipmentSlot slot) {
        super.inventoryTick(stack, level, entity, slot);
        stripForeignEnchantments(stack);
    }

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
