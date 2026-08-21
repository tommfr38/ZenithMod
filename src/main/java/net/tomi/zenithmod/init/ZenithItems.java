package net.tomi.zenithmod.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;
import net.tomi.zenithmod.ZenithMod;

import java.util.function.UnaryOperator;

public class ZenithItems {

    /**
     * Tool tier for Zenith gear.
     *
     * <p>attackDamageBonus is 0 so the whole number lives at the {@code sword(...)}
     * call site below — one place to tune, no hidden addition.
     */
    public static final ToolMaterial ZENITH = new ToolMaterial(
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
            64,   // durability: 64 hits before breaking
            9.0F, // mining speed, same as netherite
            0.0F, // attack damage bonus
            22,   // enchantment value, gold-tier so the table offers the good stuff
            ZenithTags.ZENITH_REPAIR_MATERIALS
    );

    public static final Item BAR_OF_ZENITH = register("bar_of_zenith",
            props -> props.rarity(Rarity.RARE));

    /**
     * Attack damage shows as 20 in the tooltip: the player's base 1 plus this 19.
     * -2.4F attack speed matches every vanilla sword.
     */
    public static final Item ZENITH_SWORD = register("zenith_sword",
            props -> props.sword(ZENITH, 19.0F, -2.4F).rarity(Rarity.EPIC));

    public static final Item ZENITH_BLOCK = registerBlockItem("zenith_block", ZenithBlocks.ZENITH_BLOCK);

    static Item registerBlockItem(String name, Block block) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, ZenithMod.id(name));
        return Registry.register(BuiltInRegistries.ITEM, key,
                new BlockItem(block, new Item.Properties()
                        .useBlockDescriptionPrefix()
                        .rarity(Rarity.RARE)
                        .setId(key)));
    }

    static Item register(String name, UnaryOperator<Item.Properties> extra) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, ZenithMod.id(name));
        return Registry.register(BuiltInRegistries.ITEM, key,
                new Item(extra.apply(new Item.Properties()).setId(key)));
    }

    public static void init() {
    }
}
