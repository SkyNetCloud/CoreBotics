package ca.skynetcloud.core_botics.client.datagen.provider;


import ca.skynetcloud.core_botics.common.recipes.BiorayCollectorRecipeManager;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

import static ca.skynetcloud.core_botics.CoreBoticsMain.MODID;

@SuppressWarnings("unused")
public class RecipeProvider extends FabricRecipeProvider {

    public RecipeProvider(FabricDataOutput output, CompletableFuture<net.minecraft.registry.RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
        BiorayCollectorRecipeManager.add(Items.IRON_INGOT, Items.DIAMOND, 10, "diamond_from_iron");
        BiorayCollectorRecipeManager.add(Items.COPPER_INGOT, Items.GOLD_INGOT, 15, "gold_from_copper");
        BiorayCollectorRecipeManager.exportAllRecipes(output);
        return null;
    }

    @Override
    public String getName() {
        return MODID + "Entropy Recipes";
    }
}
