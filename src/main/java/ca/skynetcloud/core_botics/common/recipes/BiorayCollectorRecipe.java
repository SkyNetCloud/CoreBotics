package ca.skynetcloud.core_botics.common.recipes;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


public record BiorayCollectorRecipe(Item input, Item output, int bioraycost, String name) {

    public boolean matches(ItemStack stack) {
        return stack.getItem() == input;
    }

    public ItemStack craft() {
        return new ItemStack(output);
    }
}
