package ca.skynetcloud.core_botics.utils;

import net.minecraft.item.ItemStack;

public class StackHelper {

    public static ItemStack withSize(ItemStack stack, int size, boolean container) {
        if (size <= 0) {
            if (container && stack.getItem().getRecipeRemainder() != null) {
                return new ItemStack(stack.getItem().getRecipeRemainder().getItem());
            } else {
                return ItemStack.EMPTY;
            }
        }

        ItemStack copy = stack.copy();
        copy.setCount(size);
        return copy;
    }

    public static ItemStack grow(ItemStack stack, int amount) {
        return withSize(stack, stack.getCount() + amount, false);
    }

    public static ItemStack shrink(ItemStack stack, int amount, boolean container) {
        if (stack.isEmpty()) return ItemStack.EMPTY;
        return withSize(stack, stack.getCount() - amount, container);
    }

    public static boolean areItemsEqual(ItemStack stack1, ItemStack stack2) {
        return !stack1.isEmpty() && !stack2.isEmpty() && stack1.isOf(stack2.getItem());
    }

    public static boolean areStacksEqual(ItemStack stack1, ItemStack stack2) {
        return areItemsEqual(stack1, stack2) && ItemStack.areEqual(stack1, stack2);
    }

}
