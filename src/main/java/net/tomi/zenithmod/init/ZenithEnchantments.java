package net.tomi.zenithmod.init;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.tomi.zenithmod.ZenithMod;

public class ZenithEnchantments {

    /**
     * Defined in data/zenithmod/enchantment/zeniths_power.json — enchantments are datapack
     * entries, so this is only the key used to look the holder up at runtime.
     */
    public static final ResourceKey<Enchantment> ZENITHS_POWER =
            ResourceKey.create(Registries.ENCHANTMENT, ZenithMod.id("zeniths_power"));

    /** The Looting level Zenith's Power stands in for. */
    public static final int LOOTING_LEVEL = 3;

    public static boolean hasZenithsPower(ItemStack stack) {
        if (!stack.is(ZenithItems.ZENITH_SWORD)) {
            return false;
        }
        ItemEnchantments enchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        for (Holder<Enchantment> held : enchantments.keySet()) {
            if (held.is(ZENITHS_POWER)) {
                return true;
            }
        }
        return false;
    }
}
