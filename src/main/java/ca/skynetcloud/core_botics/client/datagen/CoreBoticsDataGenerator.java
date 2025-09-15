package ca.skynetcloud.core_botics.client.datagen;

import ca.skynetcloud.core_botics.client.datagen.provider.LangProvider;
import ca.skynetcloud.core_botics.client.datagen.provider.RecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class CoreBoticsDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(LangProvider::new);
        pack.addProvider(RecipeProvider::new);
    }


}
