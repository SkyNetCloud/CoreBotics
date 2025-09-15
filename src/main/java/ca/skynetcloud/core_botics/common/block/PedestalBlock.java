package ca.skynetcloud.core_botics.common.block;

import ca.skynetcloud.core_botics.common.entity.PedestalBlockEntity;
import ca.skynetcloud.core_botics.common.entity.block.machine.BiorayInfusionMatrixEntity;
import ca.skynetcloud.core_botics.common.init.BlockEntityInit;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;


import static ca.skynetcloud.core_botics.utils.StackHelper.shrink;
import static ca.skynetcloud.core_botics.utils.StackHelper.withSize;
import static net.minecraft.sound.SoundCategory.BLOCKS;

public class PedestalBlock extends BlockWithEntity {

    private static final VoxelShape PEDESTAL_SHAPE = VoxelShapes.cuboid(3.0 / 16.0, 0.0, 3.0 / 16.0, 13.0 / 16.0, 5.0 / 16.0, 13.0 / 16.0);

    public PedestalBlock(Settings settings) {
        super(settings);
    }
    public static final MapCodec<PedestalBlock> CODEC = createCodec(PedestalBlock::new);
    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
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
        var block = world.getBlockEntity(pos);

        if (block instanceof PedestalBlockEntity pedestal) {
            var inventory = pedestal.getInventory();
            var input = inventory.getStack(0);
            var held = player.getMainHandStack();

            if (input.isEmpty() && !held.isEmpty()) {
                inventory.setStack(0, withSize(held, 1, false));
                player.setStackInHand(hand, shrink(held, 1, false));
                world.playSound(null, pos, SoundEvents.ENTITY_ITEM_PICKUP, BLOCKS, 1.0F, 1.0F);
            } else if (!input.isEmpty()) {
                inventory.setStack(0, ItemStack.EMPTY);

                var item = new ItemEntity(world, player.getX(), player.getY(), player.getZ(), input);
                item.setToDefaultPickupDelay();
                world.spawnEntity(item);
            }
        }

        return ActionResult.SUCCESS;
    }


    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return PEDESTAL_SHAPE;
    }




}
