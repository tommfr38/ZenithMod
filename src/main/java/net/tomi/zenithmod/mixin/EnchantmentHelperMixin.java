package net.tomi.zenithmod.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.tomi.zenithmod.init.ZenithEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Looting's drop bonus is not something an enchantment can grant on its own: every mob
 * loot table calls minecraft:enchanted_count_increase naming minecraft:looting outright.
 * Reporting a Looting level for a sword carrying Zenith's Power is what makes the
 * Looting III part of it actually drop extra loot, in modded tables as well as vanilla.
 */
@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(method = "getEnchantmentLevel", at = @At("HEAD"), cancellable = true)
    private static void zenithmod$lootingFromZenithsPower(Holder<Enchantment> enchantment,
                                                          LivingEntity entity,
                                                          CallbackInfoReturnable<Integer> cir) {
        if (enchantment.is(Enchantments.LOOTING)
                && ZenithEnchantments.hasZenithsPower(entity.getMainHandItem())) {
            cir.setReturnValue(ZenithEnchantments.LOOTING_LEVEL);
        }
    }
}
