package net.tomi.zenithmod.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.tomi.zenithmod.ZenithMod;

public class ZenithTags {

    /**
     * Anvil repair material for Zenith gear. Backed by
     * data/zenithmod/tags/item/zenith_repair_materials.json.
     */
    public static final TagKey<Item> ZENITH_REPAIR_MATERIALS =
            TagKey.create(Registries.ITEM, ZenithMod.id("zenith_repair_materials"));
}
