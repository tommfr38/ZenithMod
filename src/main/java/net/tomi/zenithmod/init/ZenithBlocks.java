package net.tomi.zenithmod.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.tomi.zenithmod.ZenithMod;

public class ZenithBlocks {

    public static final ResourceKey<Block> ZENITH_BLOCK_KEY = key("zenith_block");

    /** Full light level: the texture pulses, so the block should read as a light source. */
    public static final Block ZENITH_BLOCK = Registry.register(
            BuiltInRegistries.BLOCK,
            ZENITH_BLOCK_KEY,
            new Block(BlockBehaviour.Properties.of()
                    .mapColor(DyeColor.MAGENTA)
                    .strength(5.0F, 6.0F)
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> 15)
                    .sound(SoundType.METAL)
                    .setId(ZENITH_BLOCK_KEY)));

    private static ResourceKey<Block> key(String name) {
        return ResourceKey.create(Registries.BLOCK, ZenithMod.id(name));
    }

    public static void init() {
    }
}
