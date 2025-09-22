package ca.skynetcloud.core_botics.common.block.machine;

import ca.skynetcloud.core_botics.client.screen.handler.BiorayCollectorScreenHandler;
import ca.skynetcloud.core_botics.common.block.DeactivatedRobotBlock;
import ca.skynetcloud.core_botics.common.entity.block.machine.BiorayCollectorEntity;
import ca.skynetcloud.core_botics.common.init.BlockEntityInit;
import ca.skynetcloud.core_botics.common.init.BlockInit;
import ca.skynetcloud.core_botics.common.item.CardRemoverItem;
import ca.skynetcloud.core_botics.common.item.UpgradeCardItem;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BiorayCollectorBlock extends BlockWithEntity {


    public static final MapCodec<BiorayCollectorBlock> CODEC = createCodec(BiorayCollectorBlock::new);
    public static final Text TITLE = Text.translatable("container.core_botics.bioray_collector");

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
                    (syncId, inv, player) -> new BiorayCollectorScreenHandler(syncId, inv, be.getPropertyDelegate(), ScreenHandlerContext.create(world, pos)), BiorayCollectorBlock.TITLE);
        }
        return null;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        ItemStack heldItem = player.getMainHandStack();

        if (!(heldItem.getItem() instanceof UpgradeCardItem)&& !(heldItem.getItem() instanceof CardRemoverItem)) {
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
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler) {
        if (!world.isClient && entity instanceof LightningEntity) {
            tryConvertWithLightning(world, pos);
        }
    }

    private static boolean isDiamondRing(World world, BlockPos center) {
        BlockPos[] ring = new BlockPos[]{
                center.north(), center.south(), center.east(), center.west(),
                center.north().east(), center.north().west(), center.south().east(), center.south().west()
        };
        for (BlockPos pos : ring) {
            if (!world.getBlockState(pos).isOf(Blocks.DIAMOND_BLOCK)) {
                return false;
            }
        }
        return true;
    }

    public static void checkBeaconLightning(World world) {
        if (world.isClient) return;

        List<LightningEntity> lightnings = world.getEntitiesByClass(LightningEntity.class,
                new Box(BlockPos.ORIGIN), e -> true);

        for (LightningEntity lightning : lightnings) {
            BlockPos pos = lightning.getBlockPos();
            BlockState state = world.getBlockState(pos);

            if (state.isOf(Blocks.BEACON)) {
                BiorayCollectorBlock.tryConvertWithLightning(world, pos);
            }
        }
    }

    public static void tryConvertWithLightning(World world, BlockPos center) {
        if (!isDiamondRing(world, center)) return;

        BlockPos[] ring = new BlockPos[]{
                center.north(), center.south(), center.east(), center.west(),
                center.north().east(), center.north().west(), center.south().east(), center.south().west()
        };

        for (BlockPos pos : ring) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }

        world.setBlockState(center, BlockInit.BIORAY_COLLECTOR_BLOCK.getDefaultState());

        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.ELECTRIC_SPARK,
                    center.getX() + 0.5, center.getY() + 1, center.getZ() + 0.5,
                    30, 0.5, 0.5, 0.5, 0.2);
        }

        world.playSound(null, center, SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER,
                SoundCategory.BLOCKS, 1.0f, 1.0f);
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
