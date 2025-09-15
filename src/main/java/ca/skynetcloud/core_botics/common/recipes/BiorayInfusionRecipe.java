package ca.skynetcloud.core_botics.common.recipes;

import ca.skynetcloud.core_botics.common.init.RecipeInit;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategories;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
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
        if (world.isClient()) return false;

        if (!matrix.test(input.getStackInSlot(0))) {
            return false;
        }


        if (input.getPedestalStacks().size() < pedestals.size()) {
            return false;
        }


        List<ItemStack> pedestalStacks = new ArrayList<>(input.getPedestalStacks());
        for (Ingredient pedestalIngredient : pedestals) {
            boolean matched = false;

            for (Iterator<ItemStack> it = pedestalStacks.iterator(); it.hasNext(); ) {
                ItemStack pedestalStack = it.next();
                if (pedestalIngredient.test(pedestalStack)) {
                    it.remove();
                    matched = true;
                    break;
                }
            }

            if (!matched) return false; // one required pedestal not found
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
