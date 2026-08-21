package net.tomi.zenithmod.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.tomi.zenithmod.ZenithMod;

public class ZenithEnchantments {

    /**
     * Defined in data/zenithmod/enchantment/zeniths_strength.json — enchantments are
     * datapack entries, so this is only the key used to look the holder up at runtime.
     */
    public static final ResourceKey<Enchantment> ZENITHS_STRENGTH =
            ResourceKey.create(Registries.ENCHANTMENT, ZenithMod.id("zeniths_strength"));
}
