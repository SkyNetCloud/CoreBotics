package ca.skynetcloud.core_botics.client.datagen.helper;

import ca.skynetcloud.core_botics.common.recipes.BiorayInfusionRecipe;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import java.util.ArrayList;
import java.util.List;

import static ca.skynetcloud.core_botics.CoreBoticsMain.id;

public class BiorayRecipeHelper {

    public static void offerBiorayRecipe(RecipeExporter exporter, String recipeName, Item matrix, Item pedestalItem, Item output, int bioray) {
        List<Item> pedestals = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            pedestals.add(pedestalItem);
        }

        offerBiorayRecipe(exporter, recipeName, matrix, pedestals, output, bioray);
    }

    public static void offerBiorayRecipe(RecipeExporter exporter, String recipeName, Item matrix, List<Item> pedestals, Item output, int bioray) {
        Ingredient matrixIngredient = Ingredient.ofItems(matrix);

        List<Ingredient> pedestalIngredients = new ArrayList<>();
        for (Item item : pedestals) {
            pedestalIngredients.add(Ingredient.ofItems(item));
        }

        BiorayInfusionRecipe recipe = new BiorayInfusionRecipe(matrixIngredient, pedestalIngredients, new ItemStack(output), bioray);

        exporter.accept(RegistryKey.of(RegistryKeys.RECIPE, id(recipeName)), recipe, null);
    }
}
