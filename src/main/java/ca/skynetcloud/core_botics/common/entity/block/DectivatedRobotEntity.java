package ca.skynetcloud.core_botics.common.entity.block;

import ca.skynetcloud.core_botics.common.init.BlockEntityInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DectivatedRobotEntity extends BlockEntity implements GeoBlockEntity {


    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public DectivatedRobotEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityInit.DEACTIVATED_ROBOT_ENTITY ,blockPos,blockState);
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
