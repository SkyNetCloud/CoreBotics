package ca.skynetcloud.core_botics.common.recipes;

import ca.skynetcloud.core_botics.common.init.RecipeInit;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public record BiorayInfusionRecipe(Ingredient ingredient, ItemStack output, int bioray) implements Recipe<BiorayInfusionRecipeInput> {


    @Override
    public Ingredient ingredient() {
        return ingredient;
    }

    @Override
    public boolean matches(BiorayInfusionRecipeInput input, World world) {
        if (world.isClient){
            return false;
        }

        return ingredient.test(input.getStackInSlot(0));
    }

    @Override
    public ItemStack craft(BiorayInfusionRecipeInput input, RegistryWrapper.WrapperLookup registries) {
        return output.copy();
    }

    @Override
    public ItemStack output() {
        return output;
    }

    @Override
    public RecipeSerializer<? extends Recipe<BiorayInfusionRecipeInput>> getSerializer() {
        return RecipeInit.GROWTH_CHAMBER_SERIALIZER;
    }

    @Override
    public RecipeType<? extends Recipe<BiorayInfusionRecipeInput>> getType() {
        return RecipeInit.BIORAY_INFUSION_RECIPE_TYPE;
    }

    @Override
    public IngredientPlacement getIngredientPlacement() {
        return null;
    }

    @Override
    public RecipeBookCategory getRecipeBookCategory() {
        return null;
    }


}
