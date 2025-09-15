package ca.skynetcloud.core_botics.common.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;

import java.util.List;

public record BiorayInfusionRecipeInput(ItemStack matrixStack, List<ItemStack> pedestalStacks) implements RecipeInput {

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot == 0) {
            return matrixStack;
        }
        return ItemStack.EMPTY;
    }

    public ItemStack getMatrixStack() {
        return matrixStack;
    }

    public List<ItemStack> getPedestalStacks() {
        return pedestalStacks;
    }

    @Override
    public int size() {
        return 1 + pedestalStacks.size();
    }
}