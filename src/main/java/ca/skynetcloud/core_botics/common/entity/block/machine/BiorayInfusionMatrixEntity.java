package ca.skynetcloud.core_botics.common.entity.block.machine;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;

public class BiorayInfusionMatrixEntity extends BlockEntity implements GeoBlockEntity {

    private int storedBioray = 0;
    private final int maxStoredBioray = 10000;


    public BiorayInfusionMatrixEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }



    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return null;
    }


    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        view.putInt("bioray", storedBioray);
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
        view.getInt("bioray", storedBioray);
    }
}
