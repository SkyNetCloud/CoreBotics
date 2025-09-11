package ca.skynetcloud.core_botics;

import ca.skynetcloud.core_botics.common.init.BlockEntityInit;
import ca.skynetcloud.core_botics.common.init.BlockInit;
import ca.skynetcloud.core_botics.common.init.ItemGroupInit;
import ca.skynetcloud.core_botics.common.init.ItemInit;
import ca.skynetcloud.core_botics.common.recipes.EntropyRecipeManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static ca.skynetcloud.core_botics.common.init.ItemGroupInit.MOD_JAM_ITEM_GROUP_KEY;

public class CoreBoticsMain implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("core_botics");
    public static final String MODID = "core_botics";

    @Override
    public void onInitialize() {

        BlockInit.initialize();
        BlockEntityInit.initialize();
        ItemInit.initialize();


        EntropyRecipeManager.loadRecipesFromResources();
        Registry.register(Registries.ITEM_GROUP, MOD_JAM_ITEM_GROUP_KEY, ItemGroupInit.MODJAM_ITEM_GROUP);

        ItemGroupEvents.modifyEntriesEvent(MOD_JAM_ITEM_GROUP_KEY).register(fabricItemGroupEntries -> {
            fabricItemGroupEntries.add(BlockInit.ENTROPY_COLLECTOR_BLOCK.asItem());
            fabricItemGroupEntries.add(ItemInit.ConversionCard);
            fabricItemGroupEntries.add(ItemInit.SpeedCard);
        });
    }

    public static Identifier id(String path) {
        return Identifier.of(MODID, path);
    }
}
