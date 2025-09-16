package ca.skynetcloud.core_botics.common.entity.block.machine;

import ca.skynetcloud.core_botics.common.init.BlockEntityInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class PedestalBlockEntity extends BlockEntity implements GeoBlockEntity {

    private ItemStack storedItem = ItemStack.EMPTY;
    private float rotation = 0;
    private final AnimatableInstanceCache animCache = GeckoLibUtil.createInstanceCache(this);

    protected static final RawAnimation idle = RawAnimation.begin().thenLoop("idle");

    public PedestalBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.INFUSION_PEDESTAL_ENTITY, pos, state);
    }


    public float getRenderingRotation() {
        rotation += 0.5f;
        if (rotation >= 360) rotation = 0;
        return rotation;
    }


    public ItemStack getStack() {
        return storedItem;
    }

    public void setStack(ItemStack stack) {
        this.storedItem = stack;
        markDirty();
    }

    public boolean hasItem() {
        return !storedItem.isEmpty();
    }

    public ItemStack removeStack(int amount) {
        if (storedItem.isEmpty()) return ItemStack.EMPTY;

        if (storedItem.getCount() <= amount) {
            ItemStack toReturn = storedItem;
            storedItem = ItemStack.EMPTY;
            markDirty();
            return toReturn;
        } else {
            ItemStack toReturn = storedItem.split(amount);
            markDirty();
            return toReturn;
        }
    }


    @Override
    protected void readData(ReadView view) {
        this.storedItem = view.read("storedItem", ItemStack.CODEC).orElse(ItemStack.EMPTY);
    }

    @Override
    protected void writeData(WriteView view) {
        if (!storedItem.isEmpty()) {
            view.put("storedItem", ItemStack.CODEC, storedItem);
        } else {
            view.remove("storedItem");
        }
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    @SuppressWarnings("unused")
    public void triggerCraftParticles() {
        boolean showCraftParticles = true;
    }




    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("controller", 0, event -> event.setAndContinue(idle)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return animCache;
    }


    public void spawnItemParticles(ItemStack stack) {
        if (!(this.world instanceof ServerWorld serverWorld) || stack.isEmpty())
            return;

        double px = pos.getX() + 0.5;
        double py = pos.getY() + 1.0;
        double pz = pos.getZ() + 0.5;

        serverWorld.spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, stack), px, py, pz, 8, 0.2, 0.2, 0.2, 0.5
        );
    }



}
