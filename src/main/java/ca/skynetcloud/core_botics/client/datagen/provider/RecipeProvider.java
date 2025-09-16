package ca.skynetcloud.core_botics.client.datagen.provider;



import ca.skynetcloud.core_botics.common.init.ItemInit;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static ca.skynetcloud.core_botics.CoreBoticsMain.MODID;
import static ca.skynetcloud.core_botics.client.datagen.helper.BiorayRecipeHelper.offerBiorayRecipe;

@SuppressWarnings("unused")
public class RecipeProvider extends FabricRecipeProvider {

    public RecipeProvider(FabricDataOutput output, CompletableFuture<net.minecraft.registry.RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
        return new RecipeGenerator(registryLookup,exporter) {
            @Override
            public void generate() {
                offerBiorayRecipe(exporter, "speed_card_upgrade", Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, List.of(Items.IRON_INGOT, Items.GOLD_INGOT, Items.DIAMOND), ItemInit.SpeedCard, 12);
                offerBiorayRecipe(exporter, "conversion_card_upgrade", Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, List.of(Items.IRON_INGOT, Items.GOLD_INGOT, Items.DIAMOND), ItemInit.ConversionCard, 12);
            }
        };
    }




    @Override
    public String getName() {
        return MODID + " Recipe";
    }
}
