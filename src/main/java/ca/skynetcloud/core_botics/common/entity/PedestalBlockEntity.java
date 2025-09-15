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

    private final SimpleInventory simpleInventory;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);


    public PedestalBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.INFUSION_PEDESTAL_ENTITY, pos, state);
        this.simpleInventory = new SimpleInventory(1);
    }


    public SimpleInventory getSimpleInventory() {
        return simpleInventory;
    }

    @Override
    public void registerControllers(software.bernie.geckolib.animatable.manager.AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}
