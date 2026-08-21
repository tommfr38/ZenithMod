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
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Block;
import net.tomi.zenithmod.ZenithMod;

import java.util.Map;
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

    /** Worn-layer textures live under assets/zenithmod/equipment/zenith.json. */
    public static final ResourceKey<EquipmentAsset> ZENITH_ASSET =
            ResourceKey.create(EquipmentAssets.ROOT_ID, ZenithMod.id("zenith"));

    /**
     * Armour tier for Zenith gear.
     *
     * <p>The base durability here is what vanilla would scale per slot; every piece
     * overrides it with a flat {@link #ARMOR_DURABILITY} so all four match.
     */
    public static final ArmorMaterial ZENITH_ARMOR = new ArmorMaterial(
            32,
            Map.of(
                    ArmorType.HELMET, 4,
                    ArmorType.CHESTPLATE, 9,
                    ArmorType.LEGGINGS, 7,
                    ArmorType.BOOTS, 4,
                    ArmorType.BODY, 9),
            22,
            SoundEvents.ARMOR_EQUIP_NETHERITE,
            4.0F, // toughness, same as netherite
            0.1F, // knockback resistance, same as netherite
            ZenithTags.ZENITH_REPAIR_MATERIALS,
            ZENITH_ASSET);

    /** Every piece takes the same number of hits, as specified. */
    public static final int ARMOR_DURABILITY = 512;

    public static final Item BAR_OF_ZENITH = register("bar_of_zenith",
            props -> props.rarity(Rarity.RARE));

    /**
     * Attack damage shows as 20 in the tooltip: the player's base 1 plus this 19.
     * -2.4F attack speed matches every vanilla sword.
     */
    public static final Item ZENITH_SWORD = register("zenith_sword",
            props -> props.sword(ZENITH, 19.0F, -2.4F).rarity(Rarity.EPIC));

    public static final Item ZENITH_HELMET = registerArmor("zenith_helmet", ArmorType.HELMET);
    public static final Item ZENITH_CHESTPLATE = registerArmor("zenith_chestplate", ArmorType.CHESTPLATE);
    public static final Item ZENITH_LEGGINGS = registerArmor("zenith_leggings", ArmorType.LEGGINGS);
    public static final Item ZENITH_BOOTS = registerArmor("zenith_boots", ArmorType.BOOTS);

    public static final Item ZENITH_BLOCK = registerBlockItem("zenith_block", ZenithBlocks.ZENITH_BLOCK);

    static Item registerArmor(String name, ArmorType type) {
        // durability() comes after humanoidArmor() on purpose: it overwrites the
        // per-slot number vanilla just set.
        return register(name, props -> props.humanoidArmor(ZENITH_ARMOR, type)
                .durability(ARMOR_DURABILITY)
                .rarity(Rarity.EPIC));
    }

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
