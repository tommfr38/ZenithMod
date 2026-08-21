package net.tomi.zenithmod.mixin;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.tomi.zenithmod.init.ZenithItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * The Zenith Sword is deliberately not in #minecraft:swords — that tag feeds every
 * vanilla enchantable/* tag, and being out of it is what stops other enchantments
 * from applying. Sweep attacks are the one other thing that tag controls, so the
 * final tag test in isSweepAttack is widened to cover the sword.
 *
 * <p>Redirecting only that call leaves every movement and cooldown condition intact.
 */
@Mixin(Player.class)
public class PlayerMixin {

    @Redirect(
            method = "isSweepAttack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/tags/TagKey;)Z"))
    private boolean zenithmod$zenithSwordSweeps(ItemStack stack, TagKey<Item> tag) {
        return stack.is(tag) || stack.is(ZenithItems.ZENITH_SWORD);
    }
}
