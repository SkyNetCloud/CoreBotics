package ca.skynetcloud.core_botics.common.block;

import ca.skynetcloud.core_botics.CoreBoticsMain;
import ca.skynetcloud.core_botics.common.entity.PedestalBlockEntity;
import ca.skynetcloud.core_botics.common.entity.block.machine.BiorayInfusionMatrixEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PedestalBlock extends BlockWithEntity {

    private static final VoxelShape SHAPE = VoxelShapes.cuboid(
            3.0/16.0, 0.0, 3.0/16.0,
            13.0/16.0, 5.0/16.0, 13.0/16.0
    );

    public PedestalBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec(PedestalBlock::new);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PedestalBlockEntity(pos, state);
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!(world.getBlockEntity(pos) instanceof PedestalBlockEntity pedestal)) return ActionResult.PASS;

        ItemStack held = player.getStackInHand(hand);

        if (!pedestal.hasItem() && !held.isEmpty()) {
            pedestal.setStack(held.copy());
            pedestal.getStack().setCount(1);
            held.decrement(1);
            world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, net.minecraft.sound.SoundCategory.BLOCKS, 1f, 1f);
            return ActionResult.SUCCESS;
        }

        if (pedestal.hasItem() && held.isEmpty()) {
            player.setStackInHand(hand, pedestal.removeStack(1));
            world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, net.minecraft.sound.SoundCategory.BLOCKS, 1f, 1f);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }


}
