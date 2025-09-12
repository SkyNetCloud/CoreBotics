package ca.skynetcloud.core_botics.common.screen;

import ca.skynetcloud.core_botics.common.entity.block.BiorayCollectorEntity;
import ca.skynetcloud.core_botics.common.init.ScreenHandlerInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;


public class BiorayCollectorScreenHandler extends ScreenHandler {

    private final PropertyDelegate propertyDelegate;
    private final ScreenHandlerContext context;

    public BiorayCollectorScreenHandler(int syncId, PlayerInventory playerInventory, PropertyDelegate propertyDelegate, ScreenHandlerContext context) {
        super(ScreenHandlerInit.ENTROPY_SCREEN_HANDLER, syncId);
        this.propertyDelegate = propertyDelegate;
        this.context = context;
        addProperties(propertyDelegate);


        int startX = 8;
        int startY = 84;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, startX + col * 18, startY + row * 18));
            }
        }


        int hotbarY = startY + 58;
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, startX + col * 18, hotbarY));
        }

    }

    public BiorayCollectorScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new ArrayPropertyDelegate(2), ScreenHandlerContext.EMPTY);
    }

    public int getStoredBioray() {
        return propertyDelegate.get(0);
    }

    public int getMaxEntropy() {
        return propertyDelegate.get(1);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, blockPos) -> {
            if (world.getBlockEntity(blockPos) instanceof BiorayCollectorEntity entropyCollectorEntity) {
               entropyCollectorEntity.setOpen(false);
            }
        } );
    }
}