package ca.skynetcloud.core_botics.common.block.machine;

import ca.skynetcloud.core_botics.client.screen.handler.BiorayCollectorScreenHandler;
import ca.skynetcloud.core_botics.common.block.DeactivatedRobotBlock;
import ca.skynetcloud.core_botics.common.entity.block.machine.BiorayCollectorEntity;
import ca.skynetcloud.core_botics.common.init.BlockEntityInit;
import ca.skynetcloud.core_botics.common.item.UpgradeCardItem;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;


public class BiorayCollectorBlock extends BlockWithEntity {


    public static final MapCodec<BiorayCollectorBlock> CODEC = createCodec(BiorayCollectorBlock::new);
    public static final Text TITLE = Text.translatable("container.core_botics.entropy");


    public BiorayCollectorBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BiorayCollectorEntity(pos, state);
    }


    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    protected float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return super.getAmbientOcclusionLightLevel(state, world, pos);
    }

    @Override
    protected @Nullable NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof BiorayCollectorEntity be) {
            return new SimpleNamedScreenHandlerFactory(
                    (syncId, inv, player) -> new BiorayCollectorScreenHandler(
                            syncId,
                            inv,
                            be.getPropertyDelegate(),
                            ScreenHandlerContext.create(world, pos)
                    ),
                    BiorayCollectorBlock.TITLE
            );
        }
        return null;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        ItemStack heldItem = player.getMainHandStack();

        if (!(heldItem.getItem() instanceof UpgradeCardItem)) {
            if (world.getBlockEntity(pos) instanceof BiorayCollectorEntity collector) {
                if (!collector.isOpen()) {
                    collector.setOpen(true);
                    player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
                }
            }
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }





    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return type == BlockEntityInit.BIORAY_COLLECTOR_ENTITY
                ? (world1, pos, state1, be) -> {
            if (!world1.isClient && be instanceof BiorayCollectorEntity entropyCollector) {
                entropyCollector.tick(world1, pos, state1, entropyCollector);

            }
        }
                : null;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        double f = pos.getX() + 0.5;
        double t = pos.getY() + 0.9;
        double p = pos.getZ() + 0.5;
        if (random.nextInt(6) == 0){
            world.addParticleClient(ParticleTypes.ELECTRIC_SPARK, f,t,p, 0.0, 0.09, 0.0);
        }
        super.randomDisplayTick(state, world, pos, random);
    }

    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);

        for (Direction dir : Direction.values()){
            BlockPos neighborPos = pos.offset(dir);
            BlockState neighborState = world.getBlockState(neighborPos);
            if (neighborState.getBlock() instanceof DeactivatedRobotBlock){
                world.updateNeighbor(neighborState, neighborPos, this, null, true);
            }
        }
    }

}
