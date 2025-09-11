package ca.skynetcloud.core_botics.common.block;

import ca.skynetcloud.core_botics.common.entity.block.EntropyCollectorEntity;
import ca.skynetcloud.core_botics.common.init.BlockEntityInit;
import ca.skynetcloud.core_botics.common.item.UpgradeCardItem;
import ca.skynetcloud.core_botics.common.screen.EntropyScreenHandler;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
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
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class EntropyCollector extends BlockWithEntity {

    public static final MapCodec<EntropyCollector> CODEC = createCodec(EntropyCollector::new);
    public static final Text TITLE = Text.translatable("container.core_botics.entropy");

    // ✅ constructor must take AbstractBlock.Settings
    public EntropyCollector(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new EntropyCollectorEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE; // you’re letting Geckolib render
    }

    @Override
    protected float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return super.getAmbientOcclusionLightLevel(state, world, pos);
    }

    @Override
    protected @Nullable NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof EntropyCollectorEntity be) {
            return new SimpleNamedScreenHandlerFactory(
                    (syncId, inv, player) -> new EntropyScreenHandler(
                            syncId,
                            inv,
                            be.getPropertyDelegate(),
                            ScreenHandlerContext.create(world, pos)
                    ),
                    EntropyCollector.TITLE
            );
        }
        return null;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        ItemStack heldItem = player.getMainHandStack();

        if (!(heldItem.getItem() instanceof UpgradeCardItem)) {
            if (world.getBlockEntity(pos) instanceof EntropyCollectorEntity collector) {
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
            if (!world1.isClient && be instanceof EntropyCollectorEntity entropyCollector) {
                entropyCollector.tick(world1, pos, state1, entropyCollector);

            }
        }
                : null;
    }
}
