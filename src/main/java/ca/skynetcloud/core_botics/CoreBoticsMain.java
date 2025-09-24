package ca.skynetcloud.core_botics;

import ca.skynetcloud.core_botics.common.block.machine.BiorayCollectorBlock;
import ca.skynetcloud.core_botics.common.entity.mobs.QuadToBikeEntity;
import ca.skynetcloud.core_botics.common.init.*;
import ca.skynetcloud.core_botics.common.network.payloads.RideModeTogglePayload;
import ca.skynetcloud.core_botics.common.network.payloads.VehicleInputPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static ca.skynetcloud.core_botics.common.init.ItemGroupInit.MOD_JAM_ITEM_GROUP_KEY;

public class CoreBoticsMain implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("core_botics");
    public static final String MODID = "core_botics";

    @Override
    public void onInitialize() {

        BlockInit.initialize();
        BlockEntityInit.initialize();
        ItemInit.initialize();
        EntityInit.initialize();
        AttributeInit.initialize();
        RecipeInit.initialize();
        DataComponentInit.initialize();

        PayloadTypeRegistry.playC2S().register(RideModeTogglePayload.ID, RideModeTogglePayload.CODEC);
        PayloadTypeRegistry.playC2S().register(VehicleInputPayload.ID, VehicleInputPayload.CODEC);

        ServerTickEvents.START_WORLD_TICK.register(world -> {
            BiorayCollectorBlock.checkBeaconLightning(world);
        });

        ServerPlayNetworking.registerGlobalReceiver(RideModeTogglePayload.ID, (payload, context) -> {
            Entity entity = context.player().getWorld().getEntityById(payload.entityId());
            if (entity instanceof QuadToBikeEntity mount && mount.hasPassenger(context.player())) {
                mount.toggleMode();
            }
        });

        ServerPlayNetworking.registerGlobalReceiver(VehicleInputPayload.ID, (payload, context) -> {
            ServerPlayerEntity player = context.player();
            Entity entity = player.getWorld().getEntityById(payload.entityId());

            if (entity instanceof QuadToBikeEntity mount && mount.hasPassenger(player)) {
                mount.setAscendKeyPressed(payload.ascend());
                mount.setDescendKeyPressed(payload.descend());
            }
        });

        ItemTooltipCallback.EVENT.register((itemStack, tooltipContext, tooltipType, list) -> {
            if (!itemStack.isOf(ItemInit.tickUpgradeCard)) {
                return;
            }
            list.add(Text.translatable("item.core_botics.tick_card.tooltip"));
        });

        Registry.register(Registries.ITEM_GROUP, MOD_JAM_ITEM_GROUP_KEY, ItemGroupInit.MODJAM_ITEM_GROUP);

        ItemGroupEvents.modifyEntriesEvent(MOD_JAM_ITEM_GROUP_KEY).register(fabricItemGroupEntries -> {
            fabricItemGroupEntries.add(BlockInit.BIORAY_COLLECTOR_BLOCK.asItem());
            fabricItemGroupEntries.add(BlockInit.INFUSION_PEDESTAL_ITEM.asItem());
            fabricItemGroupEntries.add(BlockInit.INFUSION_MATRIX_BLOCK.asItem());
            fabricItemGroupEntries.add(BlockInit.DEACTIVATED_ROBOT.asItem());
            fabricItemGroupEntries.add(ItemInit.ConversionCard);
            fabricItemGroupEntries.add(ItemInit.SpeedCard);
            fabricItemGroupEntries.add(ItemInit.FLYING_CARD);
            fabricItemGroupEntries.add(ItemInit.CARD_BASE);
            fabricItemGroupEntries.add(ItemInit.tickUpgradeCard);
            fabricItemGroupEntries.add(ItemInit.TICK_CARD_REMOVER);
            fabricItemGroupEntries.add(ItemInit.SPEED_CARD_REMOVER);
            fabricItemGroupEntries.add(ItemInit.CONVERT_TOOL);
            fabricItemGroupEntries.add(ItemInit.BIORAY_PICKAXE);
        });
    }


    public static Identifier id(String path) {
        return Identifier.of(MODID, path);
    }
}
