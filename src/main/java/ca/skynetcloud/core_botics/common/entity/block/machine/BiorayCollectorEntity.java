package ca.skynetcloud.core_botics.common.entity.block.machine;

import ca.skynetcloud.core_botics.CoreBoticsMain;
import ca.skynetcloud.core_botics.common.init.BlockEntityInit;
import ca.skynetcloud.core_botics.common.init.BlockInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BeamEmitter;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animatable.processing.AnimationTest;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.*;

import static net.minecraft.block.Block.NOTIFY_ALL;
import static net.minecraft.block.Block.NOTIFY_LISTENERS;
import static net.minecraft.block.Blocks.*;

public class BiorayCollectorEntity extends BlockEntity implements GeoBlockEntity, BlockEntityTicker<BiorayCollectorEntity> {

    private int storedBioray = 0;
    private final int maxStoredBioray = 10000;
    private boolean isOpen = false;
    private int tickCooldown = 0;
    private static final int TICKS_PER_BIORAY = 100;
    private static final int TRANSFORM_TICKS = 50;
    private static final int TRANSFORM_BLOCK_NEARBY = 1000;
    public boolean disableByUpgradeCard = false;
    private int currentDepth = 1;
    private final Queue<BlockPos> spreadQueue = new LinkedList<>();
    private int spreadCount = 0;
    private int convertCooldown = 0;
    public int speedCard = 0;
    private List<BeamEmitter.BeamSegment> beamSegments = new ArrayList<>();
    private int beamMinY = -1;


    private final Map<UUID, Integer> entityConvertProgress = new HashMap<>();
    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");



    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public BiorayCollectorEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.BIORAY_COLLECTOR_ENTITY, pos, state);
    }

    public int tryDrainBioray(int amount) {
        int drained = Math.min(amount, storedBioray);
        storedBioray -= drained;
        markDirty();
        return drained;
    }

    private PlayState animationPredicate(AnimationTest<BiorayCollectorEntity> test) {
        test.controller().setAnimation(IDLE);
        return PlayState.CONTINUE;
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<BiorayCollectorEntity>("idleController", 0, this::animationPredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public int getStoredBioray() {
        return storedBioray;
    }
    public int getMaxStoredBioray() {
        return maxStoredBioray;
    }

    public void setOpen(boolean open){
        this.isOpen = open;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public boolean addBioray(int amount) {
        if (storedBioray >= maxStoredBioray) return false;
        storedBioray = Math.min(storedBioray + amount, maxStoredBioray);
        markDirty();
        return true;
    }

    public void setStoredBioray(int value) {
        storedBioray = Math.max(0, Math.min(value, maxStoredBioray));
        markDirty();
    }

    private List<ItemEntity> getNearbyItems(World world, BlockPos pos, double radius) {
        return world.getEntitiesByClass(ItemEntity.class, new Box(
                pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius,
                pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius
        ), item -> !item.isRemoved());
    }

    private List<Entity> getNearbyEntities(World world, BlockPos pos, double radius) {
        return world.getEntitiesByClass(Entity.class, new Box(
                pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius,
                pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius
        ), entity -> !entity.isRemoved());
    }

    public boolean removeBioray(int amount) {
        if (storedBioray <= 0) return false;
        storedBioray = Math.max(storedBioray - amount, 0);
        markDirty();
        return true;
    }

    @Override
    protected void writeData(WriteView view) {
        super.writeData(view);
        view.putInt("bioray", storedBioray);
        view.putBoolean("disabledByCard", disableByUpgradeCard);
        view.putInt("speedCard", speedCard);
    }

    @Override
    public void readData(ReadView view) {
        super.readData(view);
        storedBioray = view.getInt("bioray", 0);
        disableByUpgradeCard = view.getBoolean("disabledByCard", false);
        speedCard = view.getInt("speedCard", 0);
    }

    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> storedBioray;
                case 1 -> maxStoredBioray;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            if (index == 0)
                storedBioray = value;
        }

        @Override
        public int size() {
            return 2;
        }
    };

    public PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }

    public int getSpeedAddon() {
        int count = Math.min(this.speedCard, 10);
        if (count <= 0) return 0;

        return 100 + (int)Math.floor((25 - 2) / 9.0 * (count - 1));
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, BiorayCollectorEntity be) {
        boolean usedEntropy = false;
        if (world.isClient) return;
        stopTriggeredAnim("idleController", "crafting");


        if (tickCooldown > 0) {
                tickCooldown--;
        }else {
            int base = 25;
            int bonus = getSpeedAddon();
            be.addBioray(base + bonus * 10);
            tickCooldown = TICKS_PER_BIORAY;
        }

        List<Entity> nearbyEntities = getNearbyEntities(world, pos, 3.0);
        for (Entity entity : nearbyEntities) {



            if (entity instanceof ZombieEntity && be.getStoredBioray() >= 500) {
                UUID id = entity.getUuid();
                int progress = entityConvertProgress.getOrDefault(id, 0) + 1;
                entityConvertProgress.put(id, progress);

                if (progress >= TRANSFORM_TICKS) {
                    entity.remove(Entity.RemovalReason.KILLED);

                    PigEntity pig = new PigEntity(EntityType.PIG, world);
                    pig.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(),
                            entity.getYaw(), entity.getPitch());
                    world.spawnEntity(pig);

                    world.addParticleClient(ParticleTypes.ELECTRIC_SPARK,
                            entity.getX(), entity.getY() + 0.5, entity.getZ(),
                            0, 0.05, 0);
                    be.removeBioray(500);
                }
            }
        }

        List<ItemEntity> nearbyItems = getNearbyItems(world, pos, 1.0);
        for (ItemEntity itemEntity : nearbyItems) {
            ItemStack stack = itemEntity.getStack();

            if (stack.isOf(Items.NETHER_STAR) && be.getStoredBioray() >= maxStoredBioray) {
                WitherEntity wither = new WitherEntity(EntityType.WITHER, world);
                wither.refreshPositionAndAngles(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, 0, 0);
                world.spawnEntity(wither);
                stack.decrement(1);
                be.removeBioray(maxStoredBioray);
            }


        }
        if (!disableByUpgradeCard) {
            if (be.getStoredBioray() == maxStoredBioray) {

                if (convertCooldown > 0) {
                    CoreBoticsMain.LOGGER.info("Convert cooldown remaining: {} ticks", convertCooldown);
                    convertCooldown--;
                }

                if (convertCooldown == 0) {

                    if (currentDepth <= 1) {
                        BlockPos below = pos.down(currentDepth);
                        CoreBoticsMain.LOGGER.info("Next vertical block to convert: {}", below);
                        BlockState blockState = world.getBlockState(below);

                        if (!blockState.isAir()
                                && !blockState.isOf(Blocks.BEDROCK)
                                && !blockState.isOf(WITHER_ROSE)
                                && !blockState.isOf(BlockInit.BIORAY_COLLECTOR_BLOCK)) {

                            world.setBlockState(below, SCULK.getDefaultState());
                            be.removeBioray(150);
                            double x = below.getX() + 0.5;
                            double y = below.getY() + 0.5;
                            double z = below.getZ() + 0.5;
                            ((ServerWorld) world).spawnParticles(ParticleTypes.ELECTRIC_SPARK, x, y, z, 4, 0, 0, 0, 0.05);
                            CoreBoticsMain.LOGGER.info("Converted vertical block below at {}", below);

                            // Only spread horizontally (X/Z)
                            spreadQueue.add(below.north());
                            spreadQueue.add(below.south());
                            spreadQueue.add(below.east());
                            spreadQueue.add(below.west());

                            currentDepth++;
                        } else {
                            CoreBoticsMain.LOGGER.debug("Cannot convert vertical block at {}", below);
                        }
                    } else if (!spreadQueue.isEmpty() && spreadCount < 250) {
                        BlockPos target = spreadQueue.peek();
                        CoreBoticsMain.LOGGER.info("Next spread block to convert: {}", target);
                        target = spreadQueue.poll();
                        BlockState blockState = world.getBlockState(target);

                        if (!blockState.isAir()
                                && !blockState.isOf(Blocks.BEDROCK)
                                && !blockState.isOf(WITHER_ROSE)
                                && !blockState.isOf(BlockInit.BIORAY_COLLECTOR_BLOCK)) {

                            world.setBlockState(target, SCULK.getDefaultState());
                            be.removeBioray(150);
                            spreadCount++;

                            double x = target.getX() + 0.5;
                            double y = target.getY() + 0.5;
                            double z = target.getZ() + 0.5;
                            ((ServerWorld) world).spawnParticles(ParticleTypes.ELECTRIC_SPARK, x, y, z, 4, 0, 0, 0, 0.05);


                            spreadQueue.add(target.north());
                            spreadQueue.add(target.south());
                            spreadQueue.add(target.east());
                            spreadQueue.add(target.west());
                        }
                    }

                    convertCooldown = TRANSFORM_BLOCK_NEARBY;
                }
            }
        }
    }


}
