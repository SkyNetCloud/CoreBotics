package ca.skynetcloud.core_botics.common.entity.mobs;

import ca.skynetcloud.core_botics.common.entity.block.machine.BiorayInfusionMatrixEntity;
import ca.skynetcloud.core_botics.common.init.EntityInit;
import ca.skynetcloud.core_botics.common.init.ItemInit;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
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

public class HelperBotEntity extends AnimalEntity implements GeoEntity {

    private static final TrackedData<Boolean> HAS_FLYING_UPGRADE =
            DataTracker.registerData(HelperBotEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public final RawAnimation Flying = RawAnimation.begin().thenLoop("moving");

    private final AnimatableInstanceCache animCache = GeckoLibUtil.createInstanceCache(this);

    public HelperBotEntity(EntityType<? extends HelperBotEntity> type, World world) {
        super(type, world);
    }

    public HelperBotEntity(World world) {
        super(EntityInit.HELPER_BOT_ENTITY, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(HAS_FLYING_UPGRADE, false);
    }

    public boolean hasFlyingUpgrade() {
        return this.dataTracker.get(HAS_FLYING_UPGRADE);
    }

    public void setFlyingUpgrade(boolean value) {
        this.dataTracker.set(HAS_FLYING_UPGRADE, value);
        this.setNoGravity(value);
    }

    @Override
    public void setNoGravity(boolean noGravity) {
        super.setNoGravity(this.hasFlyingUpgrade());
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (!this.hasFlyingUpgrade() && stack.isOf(ItemInit.FLYING_CARD)) {
            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
            }
            this.setFlyingUpgrade(true);
            this.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
            return ActionResult.SUCCESS;
        }

        if (this.hasFlyingUpgrade()) {
            player.startRiding(this);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return passenger instanceof PlayerEntity && this.hasFlyingUpgrade();
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return AnimalEntity.createLivingAttributes()
                .add(EntityAttributes.MAX_HEALTH, 56.0)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.3)
                .add(EntityAttributes.ATTACK_DAMAGE, 15.0)
                .add(EntityAttributes.FOLLOW_RANGE, 16.0)
                .add(EntityAttributes.ARMOR, 0.0)
                .add(EntityAttributes.FLYING_SPEED, 0.3);
    }

    @Override
    public boolean handleFallDamage(double fallDistance, float damagePerDistance, DamageSource damageSource) {
        return false;
    }

    @Override
    @Nullable
    public LivingEntity getControllingPassenger() {
        Entity passenger = this.getFirstPassenger();
        return passenger instanceof PlayerEntity player ? player : null;
    }

    @Override
    protected Vec3d getControlledMovementInput(PlayerEntity player, Vec3d movementInput) {
        if (!this.hasFlyingUpgrade()) {
            return Vec3d.ZERO;
        }

        float sideways = player.sidewaysSpeed;
        float forward = player.forwardSpeed;

        float yawRad = player.getYaw() * ((float) Math.PI / 180F);
        float pitchRad = player.getPitch() * ((float) Math.PI / 180F);

        float xDir = -MathHelper.sin(yawRad) * MathHelper.cos(pitchRad);
        float yDir = -MathHelper.sin(pitchRad);
        float zDir = MathHelper.cos(yawRad) * MathHelper.cos(pitchRad);

        Vec3d lookDir = new Vec3d(xDir, yDir, zDir).normalize();
        Vec3d moveVec = lookDir.multiply(forward);

        if (sideways != 0.0F) {
            float strafeX = MathHelper.cos(yawRad) * sideways;
            float strafeZ = MathHelper.sin(yawRad) * sideways;
            moveVec = moveVec.add(strafeX, 0, strafeZ);
        }

        double speed = this.getAttributeValue(EntityAttributes.FLYING_SPEED) * 3.9;
        return moveVec.multiply(speed);
    }

    @Override
    public void travel(Vec3d movementInput) {
        if (this.isLogicalSideForUpdatingMovement() && this.hasControllingPassenger()) {
            if (this.hasFlyingUpgrade()) {
                LivingEntity controller = this.getControllingPassenger();
                if (controller instanceof PlayerEntity player) {
                    Vec3d input = this.getControlledMovementInput(player, movementInput);
                    this.move(MovementType.SELF, input);
                    this.setVelocity(this.getVelocity().multiply(0.9));

                    if (input.lengthSquared() > 0.0001) {
                        float targetYaw = (float)(MathHelper.atan2(input.z, input.x) * (180F / Math.PI)) - 90.0F;
                        this.setYaw(targetYaw);
                        this.bodyYaw = targetYaw;
                        this.headYaw = targetYaw;
                    }
                    return;
                }
            }
        }
        super.travel(movementInput);
    }

    @Override
    public void writeData(WriteView view) {
        super.writeData(view);
        view.putBoolean("HasFlyingUpgrade", this.hasFlyingUpgrade());
    }

    @Override
    public void readData(ReadView view) {
        super.readData(view);
        if (view.contains("HasFlyingUpgrade")) {
            this.setFlyingUpgrade(view.getBoolean("HasFlyingUpgrade", false));
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("normal", 2, this::movingController));

    }

    public PlayState movingController(AnimationTest<BiorayInfusionMatrixEntity> state){
        if (state.isMoving()) {
            return state.setAndContinue(Flying);
        }
        return PlayState.STOP;
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
}
