package ca.skynetcloud.core_botics.common.entity.mobs;

import ca.skynetcloud.core_botics.client.init.KeyBindingInit;
import ca.skynetcloud.core_botics.common.init.EntityInit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animatable.processing.AnimationTest;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class QuadToBikeEntity extends PassiveEntity implements GeoEntity {

    private static final TrackedData<Integer> MODE =
            DataTracker.registerData(QuadToBikeEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> IS_CONVERTING =
            DataTracker.registerData(QuadToBikeEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> IS_FLYING =
            DataTracker.registerData(QuadToBikeEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    private final AnimatableInstanceCache animCache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation BIKE_ANIM = RawAnimation.begin().thenLoop("bike_mode");
    private static final RawAnimation CONVERT_ANIM = RawAnimation.begin().thenPlayAndHold("convert");
    private boolean ascendKeyPressed = false;
    private boolean descendKeyPressed = false;


    private int conversionTimer = 0;

    public enum RideMode { BIKE, QUADCOPTER }

    public QuadToBikeEntity(EntityType<? extends QuadToBikeEntity> type, World world) {
        super(type, world);
    }

    public QuadToBikeEntity(World world) {
        super(EntityInit.BIKE_ENTITY, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(MODE, RideMode.BIKE.ordinal());
        builder.add(IS_CONVERTING, false);
        builder.add(IS_FLYING, false);
    }

    public RideMode getMode() {
        return RideMode.values()[this.dataTracker.get(MODE)];
    }

    public void setMode(RideMode mode) {
        this.dataTracker.set(MODE, mode.ordinal());
    }

    public boolean isFlying() {
        return this.dataTracker.get(IS_FLYING);
    }


    public boolean isConverting() {
        return this.dataTracker.get(IS_CONVERTING);
    }

    public void startConversion() {
        if (!isConverting()) {
            this.dataTracker.set(IS_CONVERTING, true);
            this.conversionTimer = 20;
        }
    }


    public void toggleMode() {
        if (!isConverting()) {
            startConversion();
        }
    }


    @Override
    public boolean handleFallDamage(double fallDistance, float damagePerDistance, DamageSource damageSource) {
        return false;
    }


    @Override
    protected void updatePassengerPosition(Entity passenger, PositionUpdater positionUpdater) {
        if (this.hasPassenger(passenger)) {
            double offsetY = 0.5;

            Vec3d pos = new Vec3d(this.getX(), this.getY() + offsetY, this.getZ());
            passenger.setPosition(pos.x, pos.y, pos.z);

        }
    }

    public void setAscendKeyPressed(boolean pressed) {
        this.ascendKeyPressed = pressed;
    }

    public void setDescendKeyPressed(boolean pressed) {
        this.descendKeyPressed = pressed;
    }


    @Override
    public boolean canAddPassenger(Entity passenger) {
        return passenger instanceof PlayerEntity;
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        Entity passenger = this.getFirstPassenger();
        return passenger instanceof PlayerEntity player ? player : null;
    }

    @Override
    protected Vec3d getControlledMovementInput(PlayerEntity player, Vec3d movementInput) {
        float sideways = player.sidewaysSpeed;
        float forward = player.forwardSpeed;
        float yawRad = player.getYaw() * ((float) Math.PI / 180F);

        double x = -MathHelper.sin(yawRad) * forward + MathHelper.cos(yawRad) * sideways;
        double z = MathHelper.cos(yawRad) * forward + MathHelper.sin(yawRad) * sideways;

        double y = 0;
        if (isFlying()) {
            if (KeyBindingInit.ascendKey.isPressed()) y += 1;
            if (KeyBindingInit.descendKey.isPressed()) y -= 1;
        }

        Vec3d dir = new Vec3d(x, y, z).normalize();

        double speed = isFlying()
                ? this.getAttributeValue(EntityAttributes.FLYING_SPEED) * 2.0
                : this.getAttributeValue(EntityAttributes.MOVEMENT_SPEED) * 1.5;

        return dir.multiply(speed);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.getWorld().isClient) {

            if (isConverting()) {
                conversionTimer--;
                if (conversionTimer <= 0) {
                    this.dataTracker.set(IS_CONVERTING, false);

                    RideMode newMode = getMode() == RideMode.BIKE ? RideMode.QUADCOPTER : RideMode.BIKE;
                    setMode(newMode);

                    if (newMode == RideMode.BIKE) {
                        this.dataTracker.set(IS_FLYING, false);
                        this.setNoGravity(false);
                        this.setVelocity(0, 0, 0);

                        double groundY = this.getWorld().getTopY(Heightmap.Type.WORLD_SURFACE, this.getBlockPos());
                        this.setPosition(this.getX(), groundY, this.getZ());

                        for (Entity passenger : this.getPassengersDeep()) {
                            passenger.setPosition(passenger.getX(), groundY + 1.0, passenger.getZ());
                        }

                    } else {
                        this.dataTracker.set(IS_FLYING, true);
                    }
                }
            }

            if (getMode() == RideMode.BIKE) {
                this.setNoGravity(false);

                double groundY = this.getWorld().getTopY(Heightmap.Type.WORLD_SURFACE, this.getBlockPos());
                if (this.getY() > groundY) {
                    this.setPosition(this.getX(), groundY, this.getZ());
                    this.setVelocity(0, 0, 0);

                    for (Entity passenger : this.getPassengersDeep()) {
                        passenger.setPosition(passenger.getX(), groundY + 1.0, passenger.getZ());
                    }
                }
            }

            if (getMode() == RideMode.QUADCOPTER) {
                this.dataTracker.set(IS_FLYING, true);

            }
        }
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (this.hasControllingPassenger()) {
            LivingEntity controller = this.getControllingPassenger();
            if (controller instanceof PlayerEntity player) {
                Vec3d input = getControlledMovementInput(player, movementInput);

                if (getMode() == RideMode.QUADCOPTER) {
                    this.setNoGravity(true);
                } else { // BIKE mode
                    this.setNoGravity(false);

                    // Force bike to ground every tick
                    double groundY = this.getWorld().getTopY(Heightmap.Type.WORLD_SURFACE, this.getBlockPos());
                    if (this.getY() > groundY) {
                        this.setPosition(this.getX(), groundY, this.getZ());
                        this.setVelocity(0, 0, 0);

                        for (Entity passenger : this.getPassengersDeep()) {
                            passenger.setPosition(passenger.getX(), groundY + 1.0, passenger.getZ());
                        }
                    }

                    // Remove vertical movement input
                    input = new Vec3d(input.x, 0, input.z);
                }

                // Move the bike
                this.move(MovementType.SELF, input);
                this.setVelocity(this.getVelocity().multiply(0.9));

                if (input.lengthSquared() > 0.0001) {
                    float targetYaw = (float) (MathHelper.atan2(input.z, input.x) * (180F / Math.PI)) - 90.0F;
                    this.setYaw(targetYaw);
                    this.bodyYaw = targetYaw;
                    this.headYaw = targetYaw;
                }
                return;
            }
        }

        super.travel(movementInput);
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (!this.hasPassengers() && player.getVehicle() == null) {
            return player.startRiding(this) ? ActionResult.SUCCESS : ActionResult.PASS;
        }
        if (this.hasPassenger(player)) {
            player.stopRiding();
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public void writeData(WriteView view) {
        super.writeData(view);
        view.putInt("Mode", getMode().ordinal());
        view.putBoolean("IsConverting", isConverting());
        view.putInt("ConversionTimer", conversionTimer);
        view.putBoolean("IsFlying", isFlying());
    }

    @Override
    protected void readCustomData(ReadView view) {
        super.readCustomData(view);
        if (view.contains("Mode")) setMode(RideMode.values()[view.getInt("Mode", 0)]);
        if (view.contains("IsConverting")) this.dataTracker.set(IS_CONVERTING, view.getBoolean("IsConverting", false));
        if (view.contains("ConversionTimer")) this.conversionTimer = view.getInt("ConversionTimer", 0);
        if (view.contains("IsFlying")) this.dataTracker.set(IS_FLYING, view.getBoolean("IsFlying", false));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("main", 2, this::animationController));
    }

    private PlayState animationController(AnimationTest<QuadToBikeEntity> state) {
        if (getMode() == RideMode.QUADCOPTER){
            state.setAndContinue(CONVERT_ANIM);
        }
        if (getMode() == RideMode.BIKE) {
            state.setAndContinue(BIKE_ANIM);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return animCache;
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return AnimalEntity.createAnimalAttributes()
                .add(EntityAttributes.JUMP_STRENGTH, 0.7)
                .add(EntityAttributes.MAX_HEALTH, 53.0)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.225F)
                .add(EntityAttributes.STEP_HEIGHT, 1.0)
                .add(EntityAttributes.FOLLOW_RANGE, 1.0)
                .add(EntityAttributes.SAFE_FALL_DISTANCE, 6.0)
                .add(EntityAttributes.FLYING_SPEED, 0.7)
                .add(EntityAttributes.FALL_DAMAGE_MULTIPLIER, 0.5);
    }

    @Override
    public boolean isImmobile() {
        return super.isImmobile() && this.hasPassengers();
    }
}
