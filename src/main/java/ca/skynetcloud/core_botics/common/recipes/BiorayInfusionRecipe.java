package ca.skynetcloud.core_botics.common.recipes;

import ca.skynetcloud.core_botics.common.init.RecipeInit;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategories;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;

public record BiorayInfusionRecipe(Ingredient matrix, List<Ingredient> pedestals, ItemStack output, int bioray) implements Recipe<BiorayInfusionRecipeInput> {


    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.of();
        list.add(matrix);
        list.addAll(pedestals);
        return list;
    }


    public boolean requires(ItemStack stack) {
        if (stack.isEmpty()) return false;

        for (Ingredient pedestalIngredient : pedestals) {
            if (pedestalIngredient.test(stack)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean matches(BiorayInfusionRecipeInput input, World world) {
        if (world.isClient) return false;

        if (!matrix.test(input.getMatrixStack())) return false;

        List<ItemStack> pedestalStacks = input.getPedestalStacks();
        if (pedestalStacks.size() < pedestals.size()) return false;

        boolean[] matched = new boolean[pedestalStacks.size()];

        for (Ingredient pedestalIngredient : pedestals) {
            boolean found = false;
            for (int i = 0; i < pedestalStacks.size(); i++) {
                if (!matched[i] && pedestalIngredient.test(pedestalStacks.get(i))) {
                    matched[i] = true;
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }

        return true;
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
        return IngredientPlacement.NONE;
    }

    @Override
    public RecipeBookCategory getRecipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }


}
