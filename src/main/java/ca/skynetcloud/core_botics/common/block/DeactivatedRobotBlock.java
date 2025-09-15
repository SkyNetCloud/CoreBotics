package ca.skynetcloud.core_botics.common.block;


import ca.skynetcloud.core_botics.common.entity.block.machine.BiorayCollectorEntity;
import ca.skynetcloud.core_botics.common.entity.mobs.HelperBotEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
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
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

public class DeactivatedRobotBlock extends HorizontalFacingBlock {
    public static final MapCodec<DeactivatedRobotBlock> CODEC = createCodec(DeactivatedRobotBlock::new);

    public static final int MAX_CHARGED = 3;
    public static final BooleanProperty CONNECTED = BooleanProperty.of("connected");
    public static final IntProperty CHARGED = IntProperty.of("charged", 0, MAX_CHARGED);
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
        setDefaultState(getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(CHARGED, 0)
                .with(CONNECTED, false));
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        boolean connected = isConnected(ctx.getWorld(), ctx.getBlockPos());
        return getDefaultState()
                .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite())
                .with(CONNECTED, connected);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case EAST -> SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH;
        };
    }

    private boolean tryFillWithBioray(WorldAccess world, BlockPos pos, BlockState state) {
        if (world.isClient()) return false;

        for (var dir : Direction.values()) {
            BlockPos neighborPos = pos.offset(dir);
            if (world.getBlockEntity(neighborPos) instanceof BiorayCollectorEntity collector) {
                if (collector.getStoredBioray() > 0) {
                    collector.setStoredBioray(collector.getStoredBioray() - 1);
                    int charge = state.get(CHARGED);
                    if (charge < DeactivatedRobotBlock.MAX_CHARGED) {
                        world.setBlockState(pos, state.with(CHARGED, getCharged(state) + 1), Block.NOTIFY_LISTENERS);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buf = new VoxelShape[]{shape, VoxelShapes.empty()};
        int times = (to.getHorizontalQuarterTurns() - from.getHorizontalQuarterTurns() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buf[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> {
                buf[1] = VoxelShapes.union(buf[1], VoxelShapes.cuboid(
                        1 - maxZ, minY, minX,
                        1 - minZ, maxY, maxX
                ));
            });
            buf[0] = buf[1];
            buf[1] = VoxelShapes.empty();
        }
        return buf[0];
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, CHARGED, CONNECTED);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public MapCodec<DeactivatedRobotBlock> getCodec() {
        return CODEC;
    }

    public static int getCharged(BlockState state) {
        return state.get(CHARGED);
    }

    private static boolean isConnected(WorldView world, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            BlockEntity be = world.getBlockEntity(pos.offset(dir));
            if (be instanceof BiorayCollectorEntity collector && collector.getStoredBioray() > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView,
                                                   BlockPos pos, Direction direction, BlockPos neighborPos,
                                                   BlockState neighborState, Random random) {
        boolean connected = isConnected(world, pos);
        BlockState newState = state.with(CONNECTED, connected);

        if (connected) {
            tickView.scheduleBlockTick(pos, this, 1);
        }
        return newState;
    }

    private void spawnHelperRobot(World world, BlockPos pos, BlockState state) {
        world.removeBlock(pos, false);
        HelperBotEntity bot = new HelperBotEntity(world);
        Vec3d center = pos.toCenterPos();
        float yaw = Direction.getHorizontalDegreesOrThrow(state.get(FACING));
        bot.setDeployed(true);
        bot.refreshPositionAndAngles(center.getX(), center.getY(), center.getZ(), yaw, 0);
        world.spawnEntity(bot);
        world.playSound(null, pos, SoundEvents.ENTITY_IRON_GOLEM_REPAIR, SoundCategory.BLOCKS, 1f, 1f);
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        boolean filled = tryFillWithBioray(world, pos, state);
        int charge = getCharged(state);

        if (filled && charge < MAX_CHARGED) {
            charge++;
            world.setBlockState(pos, state.with(CHARGED, charge), Block.NOTIFY_LISTENERS);
            world.playSound(null, pos, SoundEvents.BLOCK_DRIED_GHAST_TRANSITION, SoundCategory.BLOCKS, 1.0f, 1.0f);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(state));
        } else if (!filled && charge > 0) {
            charge--;
            world.setBlockState(pos, state.with(CHARGED, charge), Block.NOTIFY_LISTENERS);
        }

        if (charge >= MAX_CHARGED) {
            spawnHelperRobot(world, pos, state);
            return;
        }

        world.scheduleBlockTick(pos, this, 500);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        world.scheduleBlockTick(pos, this, 1);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (!state.get(CONNECTED) && random.nextInt(6) == 0) {
            world.addParticleClient(
                    ParticleTypes.WHITE_SMOKE,
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    0.0, 0.04, 0.0
            );
        }
        super.randomDisplayTick(state, world, pos, random);
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if ((state.get(CONNECTED) || state.get(CHARGED) > 0) && !world.getBlockTickScheduler().isQueued(pos, this)) {
            world.scheduleBlockTick(pos, this, 1);
        }
    }

    @Override
    public boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }
}
