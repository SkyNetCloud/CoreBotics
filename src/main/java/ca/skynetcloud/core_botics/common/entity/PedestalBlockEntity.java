package ca.skynetcloud.core_botics.common.entity;

import ca.skynetcloud.core_botics.common.init.BlockEntityInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

import static net.minecraft.block.Block.NOTIFY_ALL;
import static net.minecraft.inventory.Inventories.splitStack;

public class PedestalBlockEntity extends BlockEntity implements GeoBlockEntity {

    private final SimpleInventory simpleInventory = new SimpleInventory(1);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private boolean isCrafting = false;

    public PedestalBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.INFUSION_PEDESTAL_ENTITY, pos, state);
    }

    public SimpleInventory getInventory() {
        return simpleInventory;
    }

    public boolean isEmpty() {
        return simpleInventory.heldStacks.stream().allMatch(ItemStack::isEmpty);
    }

    public ItemStack getStack(int slot) {
        return simpleInventory.heldStacks.get(slot);
    }

    public void setStack(int slot, ItemStack stack) {
        simpleInventory.heldStacks.set(slot, stack);
        markDirtyAndSync();
    }

    public ItemStack removeStack(int slot) {
        ItemStack removed = Inventories.removeStack(simpleInventory.heldStacks, slot);
        markDirtyAndSync();
        return removed;
    }

    public ItemStack removeStack(int slot, int amount) {
        ItemStack removed = splitStack(simpleInventory.heldStacks, slot, amount);
        markDirtyAndSync();
        return removed;
    }

    public void clear() {
        simpleInventory.heldStacks.clear();
        markDirtyAndSync();
    }

    public void markDirtyAndSync() {
        markDirty();
        if (world != null && !world.isClient) {
            world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), NOTIFY_ALL);
        }
    }

    @Override
    public void registerControllers(software.bernie.geckolib.animatable.manager.AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected void writeData(WriteView view) {
        Inventories.writeData(view, simpleInventory.heldStacks);
        view.putBoolean("pedestal.isCrafting", isCrafting);
    }

    @Override
    public void readData(ReadView view) {
        Inventories.readData(view, simpleInventory.heldStacks);
        isCrafting = view.getBoolean("pedestal.isCrafting", false);
    }

    public boolean isCrafting() {
        return isCrafting;
    }

    public void setCrafting(boolean crafting) {
        this.isCrafting = crafting;
    }

    public void consumeItemIfCrafting() {

            ItemStack stack = simpleInventory.getStack(0);
            if (!stack.isEmpty()) {
                simpleInventory.setStack(0, ItemStack.EMPTY); // clear slotmarkDirtyAndSync();
            }

    }
}
