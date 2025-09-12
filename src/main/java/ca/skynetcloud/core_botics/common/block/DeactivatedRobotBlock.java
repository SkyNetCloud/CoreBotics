package ca.skynetcloud.core_botics.common.block;

import ca.skynetcloud.core_botics.common.entity.block.BiorayCollectorEntity;
import ca.skynetcloud.core_botics.common.entity.block.DectivatedRobotEntity;
import ca.skynetcloud.core_botics.common.entity.mobs.HelperBotEntity;
import ca.skynetcloud.core_botics.common.init.BlockInit;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;


public class DeactivatedRobotBlock extends BlockWithEntity {
    public static final MapCodec<DeactivatedRobotBlock> CODEC = createCodec(DeactivatedRobotBlock::new);

    public static final int MAX_CHARGE = 3;
    public static final int CHARGE_TICK_TIME = 5000;

    public static final IntProperty CHARGE = IntProperty.of("charge", 0, MAX_CHARGE);
    public static final BooleanProperty CONNECTED = BooleanProperty.of("connected");
    public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;


    private static final VoxelShape SHAPE_NORTH = VoxelShapes.union(
            VoxelShapes.cuboid(0.1875, 0, 0.25, 0.8125, 0.5625, 0.75),
            VoxelShapes.cuboid(0.25, 0.0625, 0.228125, 0.75, 0.5, 0.5625)
    );

    private static final VoxelShape SHAPE_EAST = rotateShape(Direction.NORTH, Direction.EAST, SHAPE_NORTH);
    private static final VoxelShape SHAPE_SOUTH = rotateShape(Direction.NORTH, Direction.SOUTH, SHAPE_NORTH);
    private static final VoxelShape SHAPE_WEST = rotateShape(Direction.NORTH, Direction.WEST, SHAPE_NORTH);


    public DeactivatedRobotBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(CHARGE, 0)
                .with(CONNECTED, false));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }


    @Override
    public MapCodec<DeactivatedRobotBlock> getCodec() {
        return CODEC;
    }



    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
            BlockPos pos = ctx.getBlockPos();
            WorldView world = ctx.getWorld();
            boolean connected = false;

            for (Direction dir : Direction.values()) {
                BlockState neighbor = world.getBlockState(pos.offset(dir));
                if (neighbor.isOf(BlockInit.BIORAY_COLLECTOR_BLOCK)) {
                    connected = true;
                    break;
                }
            }
        return this.getDefaultState()
                .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                .with(CHARGE, 0)
                .with(CONNECTED, connected);
    }

    private static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{shape, VoxelShapes.empty()};

        int times = (to.getHorizontalQuarterTurns() - from.getHorizontalQuarterTurns() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> {
                buffer[1] = VoxelShapes.union(buffer[1], VoxelShapes.cuboid(
                        1 - maxZ, minY, minX,
                        1 - minZ, maxY, maxX
                ));
            });
            buffer[0] = buffer[1];
            buffer[1] = VoxelShapes.empty();
        }

        return buffer[0];
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, CHARGE, CONNECTED);
    }

    private int getCharge(BlockState state) {
        return state.get(CHARGE);
    }

    private boolean isFullyCharged(BlockState state) {
        return getCharge(state) >= MAX_CHARGE;
    }

    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (((Boolean)state.get(CONNECTED) || (Integer)state.get(CHARGE) > 0) && !world.getBlockTickScheduler().isQueued(pos, this)) {
            world.scheduleBlockTick(pos, this, 5000);
        }

    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {

        boolean connected = neighborState.isOf(BlockInit.BIORAY_COLLECTOR_BLOCK);

        if (connected != state.get(CONNECTED)){
            state = state.with(CONNECTED, connected);
        }

        if (connected){
            tickView.scheduleBlockTick(pos, this, 2);
        }

        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(CONNECTED)) {
            tickCharge(state, world, pos);
        } else {
            decayCharge(state, world, pos);
        }

        world.scheduleBlockTick(pos, this, CHARGE_TICK_TIME);
    }

    private BlockPos findCollector(World world, BlockPos pos){
        for (Direction dir : Direction.values()){
            BlockPos neighbor = pos.offset(dir);
            if (world.getBlockState(neighbor).isOf(BlockInit.BIORAY_COLLECTOR_BLOCK)){
                return neighbor;
            }
        }

        return null;
    }

    private void tickCharge(BlockState state, ServerWorld world, BlockPos pos) {
        BlockPos collectorPos = findCollector(world,pos);
        if (collectorPos == null){
            return;
        }

        BlockEntity be = world.getBlockEntity(collectorPos);
        if (be instanceof BiorayCollectorEntity collector) {
           if (collector.removeBioray(100)) {
               if (!isFullyCharged(state)) {
                   world.playSound(null, pos, SoundEvents.BLOCK_BEACON_AMBIENT, SoundCategory.BLOCKS, 0.7f, 1f);
                   world.setBlockState(pos, state.with(CHARGE, getCharge(state) + 1), Block.NOTIFY_ALL);
                   world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(state));
               } else {
                   spawnHelperBot(world, pos, state);
               }
           }
        }
    }

    private void decayCharge(BlockState state, ServerWorld world, BlockPos pos) {
        int c = getCharge(state);
        if (c > 0) {
            world.setBlockState(pos, state.with(CHARGE, c - 1), Block.NOTIFY_ALL);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(state));
        }
    }

    private void spawnHelperBot(ServerWorld world, BlockPos pos, BlockState state) {
        world.removeBlock(pos, false);

        HelperBotEntity bot = new HelperBotEntity(world);
        Vec3d center = pos.toCenterPos();
        float yaw = Direction.getHorizontalDegreesOrThrow(state.get(FACING));

        bot.refreshPositionAndAngles(center.getX(), center.getY(), center.getZ(), yaw, 0);
        world.spawnEntity(bot);

        world.playSound(null, pos, SoundEvents.ENTITY_IRON_GOLEM_REPAIR, SoundCategory.BLOCKS, 1f, 1f);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)){
            case EAST ->  SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH;
        };
    }

    @Override
    public boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DectivatedRobotEntity(pos, state);
    }
}
