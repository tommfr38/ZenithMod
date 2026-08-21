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
     * Defined in data/zenithmod/enchantment/zeniths_power.json. The definition carries no
     * effects of its own — it exists to bind to the Zenith Sword and be read here. The
     * launch itself is item code, because no enchantment effect component moves the player.
     */
    public static final ResourceKey<Enchantment> ZENITHS_POWER =
            ResourceKey.create(Registries.ENCHANTMENT, ZenithMod.id("zeniths_power"));

    public static boolean hasZenithsPower(ItemStack stack) {
        ItemEnchantments enchantments = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        for (Holder<Enchantment> held : enchantments.keySet()) {
            if (held.is(ZENITHS_POWER)) {
                return true;
            }
        }
        return false;
    }
}
