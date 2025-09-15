package ca.skynetcloud.core_botics.client.screen.handler;

import ca.skynetcloud.core_botics.common.entity.block.machine.BiorayCollectorEntity;
import ca.skynetcloud.core_botics.common.entity.block.machine.BiorayInfusionMatrixEntity;
import ca.skynetcloud.core_botics.common.init.ScreenHandlerInit;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;


public class BiorayInfusionMatrixScreenHandler extends ScreenHandler {

    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    public final BiorayInfusionMatrixEntity blockEntity;


    public BiorayInfusionMatrixScreenHandler(int syncId, PlayerInventory inventory, BlockPos pos) {
        this(syncId, inventory, inventory.player.getWorld().getBlockEntity(pos), new ArrayPropertyDelegate(4));
    }

    public BiorayInfusionMatrixScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity, PropertyDelegate arrayPropertyDelegate) {
        super(ScreenHandlerInit.BIORAY_INFUSION_MATRIX_SCREEN_HANDLER, syncId);

        this.inventory = ((Inventory) blockEntity);
        this.blockEntity = ((BiorayInfusionMatrixEntity) blockEntity);
        this.propertyDelegate = arrayPropertyDelegate;

        this.addSlot(new Slot(inventory, 0, 76, 26){
            @Override
            public int getMaxItemCount() {
                return 1;
            }
        });

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        addProperties(arrayPropertyDelegate);
    }

    public int getStoredBioray() {
        return propertyDelegate.get(2);
    }

    public boolean isCrafting() {
        return propertyDelegate.get(0) > 0;
    }

    public int getMaxEntropy() {
        return propertyDelegate.get(3);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);

        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();

            int matrixSize = this.inventory.size();
            int totalSlots = this.slots.size();

            boolean moved;

            if (invSlot < matrixSize) {
                moved = this.insertItem(originalStack, matrixSize, totalSlots, false);
            } else {
                moved = this.insertItem(originalStack, 0, matrixSize, false);
            }

            if (!moved) {
                slot.markDirty();
                sendContentUpdates(); // force sync to client
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
                slot.markDirty();
                sendContentUpdates();
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }



    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

}