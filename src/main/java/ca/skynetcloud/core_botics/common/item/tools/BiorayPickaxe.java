package ca.skynetcloud.core_botics.common.item.tools;

import ca.skynetcloud.core_botics.common.entity.block.machine.BiorayCollectorEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static ca.skynetcloud.core_botics.common.init.DataComponentInit.BIORAY_CHARGE;
import static net.minecraft.block.entity.SculkSpreadManager.MAX_CHARGE;


public class BiorayPickaxe extends Item {


    public BiorayPickaxe(Settings settings) {
        super(settings);
    }


    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, world, entity, slot);

        if (entity instanceof PlayerEntity player) {
            if (isNearCollector(world, player.getBlockPos())) {
                addCharge(stack, 1);
            }
        }
    }

    private boolean isNearCollector(World world, BlockPos playerPos) {
        int radius = 5;
        for (BlockPos pos : BlockPos.iterate(
                playerPos.add(-radius, -radius, -radius),
                playerPos.add(radius, radius, radius))) {
            if (world.getBlockEntity(pos) instanceof BiorayCollectorEntity) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return getCharge(stack) > 0;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        int charge = getCharge(stack);
        return Math.round(((float) charge / (float) MAX_CHARGE) * 13);
    }


    public int getItemBarColor(ItemStack stack) {
        return 0x00FFAA;
    }


    public static void addCharge(ItemStack stack, int amount) {
        int charge = getCharge(stack);
        charge = Math.min(charge + amount, MAX_CHARGE);
        stack.set(BIORAY_CHARGE, charge);
    }

    public static int getCharge(ItemStack stack) {
        return stack.getOrDefault(BIORAY_CHARGE, 0);
    }
}
