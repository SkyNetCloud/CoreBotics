package ca.skynetcloud.core_botics.common.entity.block.machine;

import ca.skynetcloud.core_botics.CoreBoticsMain;
import ca.skynetcloud.core_botics.client.screen.handler.BiorayInfusionMatrixScreenHandler;
import ca.skynetcloud.core_botics.common.entity.PedestalBlockEntity;
import ca.skynetcloud.core_botics.common.init.BlockEntityInit;
import ca.skynetcloud.core_botics.common.init.RecipeInit;
import ca.skynetcloud.core_botics.common.recipes.BiorayInfusionRecipe;
import ca.skynetcloud.core_botics.common.recipes.BiorayInfusionRecipeInput;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static net.minecraft.block.Block.NOTIFY_ALL;
import static net.minecraft.block.Block.NOTIFY_LISTENERS;

public class BiorayInfusionMatrixEntity extends BlockEntity implements Inventory, ExtendedScreenHandlerFactory<BlockPos>, GeoBlockEntity, BlockEntityTicker<BiorayInfusionMatrixEntity> {

    private static final Text TITLE = Text.translatable("container.infusion_matrix");
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private int storedBioray = 0;
    private final int maxStoredBioray = 10000;
    private int progress = 0;
    private final int maxProgress = 95;
    protected final PropertyDelegate propertyDelegate;

    protected static final RawAnimation DEPLOY_WITH_OUT_BIORAY = RawAnimation.begin().thenPlayAndHold("not_active");
    protected static final RawAnimation DEPLOY_WITH_BIORAY = RawAnimation.begin().thenPlayAndHold("active");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public BiorayInfusionMatrixEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.INFUSION_MATRIX_ENTITY, pos, state);
        this.propertyDelegate = new net.minecraft.screen.PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> progress;
                    case 1 -> maxProgress;
                    case 2 -> storedBioray;
                    case 3 -> maxStoredBioray;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> progress = value;
                    case 2 -> storedBioray = value;
                }
            }

            @Override
            public int size() {
                return 4;
            }
        };
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("controller", 0, event -> {
            if (getCollectorBelow() != null) return event.setAndContinue(DEPLOY_WITH_BIORAY);
            return event.setAndContinue(DEPLOY_WITH_OUT_BIORAY);
        }).triggerableAnim("crafting", RawAnimation.begin().thenLoop("crafting")));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public net.minecraft.screen.PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }

    @Nullable
    private BiorayCollectorEntity getCollectorBelow() {
        if (world == null) return null;
        BlockEntity beBelow = world.getBlockEntity(pos.down(2));
        if (beBelow instanceof BiorayCollectorEntity collector) return collector;
        return null;
    }

    private List<ItemStack> getPedestalStacks() {
        List<ItemStack> stacks = new ArrayList<>();
        for (BlockPos pedestalPos : getPedestalOffsets()) {
            BlockEntity be = world.getBlockEntity(pedestalPos);
            if (be instanceof PedestalBlockEntity pedestal) {
                ItemStack stack = pedestal.getSimpleInventory().getStack(0);
                if (!stack.isEmpty()) stacks.add(stack.copy());
            }
        }
        return stacks;
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, BiorayInfusionMatrixEntity be) {

        if (!(world instanceof ServerWorld serverWorld)) return;
        BiorayCollectorEntity collector = getCollectorBelow();
        if (collector == null) return;


        int canReceive = maxStoredBioray - storedBioray;
        if (canReceive > 0) storedBioray += collector.tryDrainBioray(canReceive);

        Optional<RecipeEntry<BiorayInfusionRecipe>> recipeOpt = ((ServerWorld)world).getRecipeManager().getFirstMatch(RecipeInit.BIORAY_INFUSION_RECIPE_TYPE, new BiorayInfusionRecipeInput(inventory.get(0),getPedestalStacks()), world);



        if (recipeOpt.isEmpty()) { progress = 0; return; }
        if (storedBioray <= 0) return;

        progress++;
        storedBioray--;

        BiorayInfusionRecipe recipe = recipeOpt.get().value();
        if (progress >= maxProgress) {
            consumePedestalItems(recipe);
            ItemStack input = inventory.get(0);
            if (!input.isEmpty()) input.decrement(1);
            ItemStack output = recipe.output();
            ItemStack slot = inventory.get(0);
            if (slot.isEmpty()) inventory.set(0, output.copy());
            else if (slot.isOf(output.getItem())) slot.increment(output.getCount());
            progress = 0;
        }
        markDirty();
    }

    private List<BlockPos> getPedestalOffsets() {
        List<BlockPos> offsets = new ArrayList<>();
        int minY = pos.getY() - 2;
        int maxY = pos.getY() + 1;
        int maxDistance = 5;

        for (int y = minY; y <= maxY; y++) {
            for (int x = pos.getX() - maxDistance; x <= pos.getX() + maxDistance; x++) {
                for (int z = pos.getZ() - maxDistance; z <= pos.getZ() + maxDistance; z++) {
                    BlockPos checkPos = new BlockPos(x, y, z);
                    BlockEntity be = world.getBlockEntity(checkPos);
                    if (be instanceof PedestalBlockEntity) {
                        offsets.add(checkPos);
                    }
                }
            }
        }

        return offsets;
    }


    private void consumePedestalItems(BiorayInfusionRecipe recipe) {
        for (BlockPos pedestalPos : getPedestalOffsets()) {
            BlockEntity be = world.getBlockEntity(pedestalPos);
            if (be instanceof PedestalBlockEntity pedestal) {
                ItemStack stack = pedestal.getSimpleInventory().getStack(0);
                if (!stack.isEmpty()) {
                    pedestal.getSimpleInventory().clear();
                    pedestal.markDirty();

                    if (!world.isClient()) {
                        world.updateListeners(pedestalPos, world.getBlockState(pedestalPos), world.getBlockState(pedestalPos), Block.NOTIFY_ALL);
                        ((ServerWorld) world).getChunkManager().markForUpdate(pos);
                    }

                    CoreBoticsMain.LOGGER.info("Cleared pedestal at " + pedestalPos + ", markDirty called.");
                } else {
                    CoreBoticsMain.LOGGER.info("Pedestal at " + pedestalPos + " was already empty.");
                }
            }
        }
    }


    @Override
    protected void writeData(WriteView view) {
        Inventories.writeData(view, inventory);
        view.putInt("infusion_matrix.progress", progress);
        view.putInt("infusion_matrix.bioray", storedBioray);
    }

    @Override
    protected void readData(ReadView view) {
        Inventories.readData(view, inventory);
        view.getInt("infusion_matrix.progress", progress);
        view.getInt("infusion_matrix.bioray", storedBioray);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
        return pos;
    }

    @Override
    public Text getDisplayName() {
        return TITLE;
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new BiorayInfusionMatrixScreenHandler(syncId, playerInventory, this, this.getPropertyDelegate());
    }

    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return inventory.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(inventory, slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(inventory, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.set(slot, stack);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        inventory.clear();
    }


}
