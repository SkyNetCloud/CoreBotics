package ca.skynetcloud.core_botics.common.block.machine;

import ca.skynetcloud.core_botics.client.screen.handler.BiorayCollectorScreenHandler;
import ca.skynetcloud.core_botics.common.entity.block.machine.BiorayCollectorEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BiorayInfusionMatrixBlock extends BlockWithEntity {

    private static final Text TITLE = Text.translatable("container.bioray_forge");
    public static final MapCodec<BiorayCollectorBlock> CODEC = createCodec(BiorayCollectorBlock::new);

    protected BiorayInfusionMatrixBlock() {
        super(Settings.create().nonOpaque().sounds(BlockSoundGroup.IRON));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    protected @Nullable NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof BiorayCollectorEntity be) {
            return new SimpleNamedScreenHandlerFactory(
                    (syncId, inv, player) -> new BiorayCollectorScreenHandler(syncId, inv, be.getPropertyDelegate(), ScreenHandlerContext.create(world, pos)),
                    TITLE
            );
        }
        return null;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }
}
