package ca.skynetcloud.core_botics.common.item;

import ca.skynetcloud.core_botics.common.entity.block.machine.BiorayCollectorEntity;
import ca.skynetcloud.core_botics.common.init.ItemInit;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static ca.skynetcloud.core_botics.common.init.ItemInit.SpeedCard;
import static ca.skynetcloud.core_botics.common.init.ItemInit.tickUpgradeCard;

public class UpgradeCardItem extends Item {

    public UpgradeCardItem(Settings settings) {
        super(settings);
    }



    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockEntity be = world.getBlockEntity(pos);
        ItemStack stack = context.getStack();

        if (stack.isOf(tickUpgradeCard)) {
            if (be instanceof BiorayCollectorEntity collector) {
                if (collector.tickCardCount < 10) {
                    collector.tickCardCount++;
                    collector.markDirty();

                    if (!world.isClient) {
                        PlayerEntity player = context.getPlayer();
                        player.sendMessage(Text.literal("Tick Card Applied: " + collector.tickCardCount), true);
                    }

                    PlayerEntity player = context.getPlayer();
                    if (player != null && !player.isCreative()) {
                        stack.decrement(1);
                    }

                    return ActionResult.SUCCESS;
                } else {
                    if (!world.isClient) {
                        PlayerEntity player = context.getPlayer();
                        player.sendMessage(Text.literal("Maximum of 10 tick cards applied!"), true);
                    }
                    return ActionResult.SUCCESS;
                }
            }
        }

        if (stack.isOf(SpeedCard)) {
            if (be instanceof BiorayCollectorEntity collector) {
                if (collector.speedCard < 10) {
                    collector.speedCard++;
                    collector.markDirty();

                    if (!world.isClient) {
                        PlayerEntity player = context.getPlayer();
                        player.sendMessage(Text.literal("Speed Applied: " + collector.speedCard), true);
                    }

                    PlayerEntity player = context.getPlayer();
                    if (player != null && !player.isCreative()) {
                        stack.decrement(1);
                    }

                    return ActionResult.SUCCESS;
                } else {
                    if (!world.isClient) {
                        PlayerEntity player = context.getPlayer();
                        player.sendMessage(Text.literal("Maximum of 10 speed cards applied!"), true);
                    }
                    return ActionResult.SUCCESS;
                }
            }
        }


        if (stack.isOf(ItemInit.ConversionCard)) {
            if (be instanceof BiorayCollectorEntity collector) {
                collector.convetCard = !collector.convetCard;
                collector.markDirty();

                if (!world.isClient) {
                    PlayerEntity player = context.getPlayer();
                    player.sendMessage(Text.literal("Entropy Collector block conversion ").append( Text.literal(collector.convetCard ? "Disabled" : "Enabled")
                            .formatted(collector.convetCard ? Formatting.RED : Formatting.GREEN)), true);
                }
                return ActionResult.SUCCESS;
            }
        }

        return super.useOnBlock(context);
    }
}
