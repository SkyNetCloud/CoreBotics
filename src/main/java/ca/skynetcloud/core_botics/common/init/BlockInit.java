package ca.skynetcloud.core_botics.common.init;

import ca.skynetcloud.core_botics.common.block.DeactivatedRobotBlock;
import ca.skynetcloud.core_botics.common.block.PedestalBlock;
import ca.skynetcloud.core_botics.common.block.machine.BiorayCollectorBlock;
import ca.skynetcloud.core_botics.common.block.machine.BiorayInfusionMatrixBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static ca.skynetcloud.core_botics.CoreBoticsMain.MODID;
import static ca.skynetcloud.core_botics.CoreBoticsMain.id;

public class BlockInit {


    public static void initialize() {}

    public static final Block BIORAY_COLLECTOR_BLOCK = Registry.register(Registries.BLOCK, id("bioray_collector"), new BiorayCollectorBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.IRON).nonOpaque().registryKey(RegistryKey.of(RegistryKeys.BLOCK, id("bioray_collector")))));
    public static final Block DEACTIVATED_ROBOT = Registry.register(Registries.BLOCK, id("deactivated_robot"), new DeactivatedRobotBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.IRON).nonOpaque().registryKey(RegistryKey.of(RegistryKeys.BLOCK, id("deactivated_robot")))));
    public static final Block INFUSION_PEDESTAL_BLOCK = Registry.register(Registries.BLOCK, id("infusion_pedestal"), new PedestalBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.IRON).nonOpaque().registryKey(RegistryKey.of(RegistryKeys.BLOCK, id("infusion_pedestal")))));
    public static final Block INFUSION_MATRIX_BLOCK = Registry.register(Registries.BLOCK, id("infusion_matrix"), new BiorayInfusionMatrixBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.IRON).nonOpaque().registryKey(RegistryKey.of(RegistryKeys.BLOCK, id("infusion_matrix")))));

    public static final BlockItem BIORAY_COLLECTOR_ITEM = Registry.register(Registries.ITEM, id("bioray_collector"), new BlockItem(BIORAY_COLLECTOR_BLOCK, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, id("bioray_collector")))));
    public static final BlockItem DEACTIVATED_ROBOT_ITEM = Registry.register(Registries.ITEM, id("deactivated_robot"), new BlockItem(DEACTIVATED_ROBOT, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, id("deactivated_robot")))));
    public static final BlockItem INFUSION_PEDESTAL_ITEM = Registry.register(Registries.ITEM, id("infusion_pedestal"), new BlockItem(INFUSION_PEDESTAL_BLOCK, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, id("infusion_pedestal")))));
    public static final BlockItem INFUSION_MATRIX_ITEM = Registry.register(Registries.ITEM, id("infusion_matrix"), new BlockItem(INFUSION_MATRIX_BLOCK, new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, id("infusion_matrix")))));

}
