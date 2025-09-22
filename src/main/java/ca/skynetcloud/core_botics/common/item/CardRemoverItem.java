package ca.skynetcloud.core_botics.common.item;


import ca.skynetcloud.core_botics.common.entity.block.machine.BiorayCollectorEntity;
import ca.skynetcloud.core_botics.common.init.ItemInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class CardRemoverItem extends Item {

    private final Item cardType;

    public CardRemoverItem(Settings settings, Item cardType) {
        super(settings);
        this.cardType = cardType;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        if (player == null) return ActionResult.PASS;

        if (world.getBlockEntity(context.getBlockPos()) instanceof BiorayCollectorEntity collector) {
            int removedCount = 0;

            if (cardType == ItemInit.SpeedCard) {
                removedCount = tryRemoveCards(player, collector, () -> collector.speedCard, v -> collector.speedCard = v, ItemInit.SpeedCard, world);
            }
            else if (cardType == ItemInit.tickUpgradeCard) {
                removedCount = tryRemoveCards(player, collector, () -> collector.tickCardCount, v -> collector.tickCardCount = v, ItemInit.tickUpgradeCard, world);
            }

            if (removedCount > 0) {
                collector.markDirty();
                return ActionResult.SUCCESS;
            } else {
                if (!world.isClient) {
                    player.sendMessage(Text.literal("No removable cards of this type found on this block.")
                            .formatted(Formatting.GRAY), true);
                }
                return ActionResult.FAIL;
            }
        }

        return super.useOnBlock(context);
    }

    @SuppressWarnings("unused")
    private int tryRemoveCards(PlayerEntity player, BiorayCollectorEntity collector, IntSupplier getter, IntConsumer setter, Item cardItem, World world) {
        int count = getter.getAsInt();
        if (count <= 0) return 0;

        if (player.isSneaking()) {
            for (int i = 0; i < count; i++) {
                player.getInventory().offerOrDrop(new ItemStack(cardItem));
            }
            setter.accept(0);
            if (!world.isClient) {
                player.sendMessage(Text.literal("All " + cardItem.getName().getString() + "s removed and returned!")
                        .formatted(Formatting.RED), true);
            }
            return count;
        } else {
            setter.accept(count - 1);
            player.getInventory().offerOrDrop(new ItemStack(cardItem));
            if (!world.isClient) {
                player.sendMessage(Text.literal("Removed one " + cardItem.getName().getString() +
                        ". Remaining: " + (count - 1)).formatted(Formatting.YELLOW), true);
            }
            return 1;
        }
    }

}
