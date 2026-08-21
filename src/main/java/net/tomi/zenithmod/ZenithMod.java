package net.tomi.zenithmod;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import net.tomi.zenithmod.init.ZenithBlocks;
import net.tomi.zenithmod.init.ZenithItems;
import net.tomi.zenithmod.init.ZenithTabs;
import org.slf4j.Logger;

public class ZenithMod implements ModInitializer {

    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String ID = "zenithmod";

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(ID, path);
    }

    @Override
    public void onInitialize() {
        ZenithBlocks.init();
        ZenithItems.init();
        ZenithTabs.init();
    }
}
