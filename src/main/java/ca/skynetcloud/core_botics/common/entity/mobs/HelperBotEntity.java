package ca.skynetcloud.core_botics.common.entity.mobs;

import ca.skynetcloud.core_botics.common.init.EntityInit;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class HelperBotEntity extends TameableEntity implements GeoEntity {

    ;

    private boolean deployed = false;
    protected static final RawAnimation DEPLOY = RawAnimation.begin().thenPlay("deploy");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public HelperBotEntity(EntityType<HelperBotEntity> type, World world) {
        super(type, world);
    }

    public HelperBotEntity(World world) {
        this(EntityInit.HELPER_BOT_ENTITY, world);
    }

    public static DefaultAttributeContainer.Builder createHelperRobotAttributes() {
        return PassiveEntity.createLivingAttributes().add(EntityAttributes.MAX_HEALTH, 400.0)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.25)
                .add(EntityAttributes.ATTACK_DAMAGE, 25.0)
                .add(EntityAttributes.ARMOR, 10.0)
                .add(EntityAttributes.ARMOR_TOUGHNESS, 5.0)
                .add(EntityAttributes.FOLLOW_RANGE, 100.0)
                .add(EntityAttributes.KNOCKBACK_RESISTANCE, 0.8)
                .add(EntityAttributes.ATTACK_KNOCKBACK, 1.0);
    }

    public void setDeployed(boolean deployed) {
        this.deployed = deployed;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>( "robotController", 0, state -> {
            if (state.isCurrentAnimation(DEPLOY) && state.controller().hasAnimationFinished()) {
                state.setAnimation(RawAnimation.begin().thenLoop("idle"));
                return PlayState.CONTINUE;
            }

            return PlayState.CONTINUE;
        }));
    }

    @Override
    public void tick() {
        super.tick();

        // Play deploy animation on first tick after spawn
        if (!this.getWorld().isClient && deployed) {
            triggerAnim("robotController", "deploy");
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }
}
