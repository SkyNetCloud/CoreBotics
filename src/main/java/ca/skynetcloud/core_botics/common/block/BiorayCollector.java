package ca.skynetcloud.core_botics.common.block;

import ca.skynetcloud.core_botics.common.entity.block.BiorayCollectorEntity;
import ca.skynetcloud.core_botics.common.init.BlockEntityInit;
import ca.skynetcloud.core_botics.common.item.UpgradeCardItem;
import ca.skynetcloud.core_botics.common.screen.BiorayCollectorScreenHandler;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;


public class BiorayCollector extends BlockWithEntity {


    private static final VoxelShape BASE_SHAPE = BlockWithEntity.createCuboidShape(2,0,2,14,2,14);
    //private static final VoxelShape CUBE_SHAPE = BlockWithEntity.createCuboidShape(4,8,4,12,16,12);
    private static final VoxelShape FULL_SHAPE = BASE_SHAPE;
    public static final MapCodec<BiorayCollector> CODEC = createCodec(BiorayCollector::new);
    public static final Text TITLE = Text.translatable("container.core_botics.entropy");


    public BiorayCollector(AbstractBlock.Settings settings) {
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
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return FULL_SHAPE;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return FULL_SHAPE;
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
                    BiorayCollector.TITLE
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
        return type == BlockEntityInit.ENTROPY_COLLECTOR_ENTITY
                ? (world1, pos, state1, be) -> {
            if (!world1.isClient && be instanceof BiorayCollectorEntity entropyCollector) {
                entropyCollector.tick(world1, pos, state1, entropyCollector);

            }
        }
                : null;
    }
}
