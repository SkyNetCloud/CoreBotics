package ca.skynetcloud.core_botics.common.block.machine;

import ca.skynetcloud.core_botics.client.screen.handler.BiorayCollectorScreenHandler;
import ca.skynetcloud.core_botics.client.screen.handler.BiorayInfusionMatrixScreenHandler;
import ca.skynetcloud.core_botics.common.entity.block.machine.BiorayCollectorEntity;
import ca.skynetcloud.core_botics.common.entity.block.machine.BiorayInfusionMatrixEntity;
import ca.skynetcloud.core_botics.common.init.BlockEntityInit;
import ca.skynetcloud.core_botics.common.init.BlockInit;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BiorayInfusionMatrixBlock extends BlockWithEntity {

    public static final MapCodec<BiorayCollectorBlock> CODEC = createCodec(BiorayCollectorBlock::new);

    public BiorayInfusionMatrixBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }


    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient) {

            BlockEntity be = world.getBlockEntity(pos);
            int gap = 1;
            BlockPos belowPos = pos.down(gap + 1);
            BlockState belowState = world.getBlockState(belowPos);

            if (be instanceof BiorayInfusionMatrixEntity matrixEntity) {
                if (belowState.isOf(BlockInit.BIORAY_COLLECTOR_BLOCK)) {
                    player.openHandledScreen(matrixEntity);
                } else {
                    player.sendMessage(Text.translatable("message.no_collector_block_below"), true);
                }
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BiorayInfusionMatrixEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return type == BlockEntityInit.INFUSION_MATRIX_ENTITY
                ? (world1, pos, state1, be) -> {
            if (!world1.isClient && be instanceof BiorayInfusionMatrixEntity biorayInfusionMatrixEntity) {
                biorayInfusionMatrixEntity.tick(world1, pos, state1, biorayInfusionMatrixEntity);

            }
        }
                : null;
    }
}
