package ca.skynetcloud.core_botics.common.recipes;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public record BiorayInfusionRecipeInput(Inventory guiInv, World world, BlockPos pos) implements RecipeInput {

    @Override
    public ItemStack getStackInSlot(int slot) {
        return guiInv.getStack(slot);
    }

    @Override
    public int size() {
        return guiInv.size();
    }



}
