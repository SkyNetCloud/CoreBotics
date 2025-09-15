package ca.skynetcloud.core_botics.common.init;

import ca.skynetcloud.core_botics.common.entity.PedestalBlockEntity;
import ca.skynetcloud.core_botics.common.entity.block.machine.BiorayCollectorEntity;
import ca.skynetcloud.core_botics.common.entity.block.machine.BiorayInfusionMatrixEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static ca.skynetcloud.core_botics.CoreBoticsMain.MODID;
import static ca.skynetcloud.core_botics.CoreBoticsMain.id;

public class BlockEntityInit {
    public static void initialize() {}

    public static final BlockEntityType<BiorayCollectorEntity> BIORAY_COLLECTOR_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("bioray_collector_entity"), FabricBlockEntityTypeBuilder.create(BiorayCollectorEntity::new, BlockInit.BIORAY_COLLECTOR_BLOCK).build());
    public static final BlockEntityType<PedestalBlockEntity> INFUSION_PEDESTAL_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("bioray_infusion_pedestal_entity"), FabricBlockEntityTypeBuilder.create(PedestalBlockEntity::new, BlockInit.INFUSION_PEDESTAL_BLOCK).build());
    public static final BlockEntityType<BiorayInfusionMatrixEntity> INFUSION_MATRIX_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("bioray_infusion_matrix_entity"), FabricBlockEntityTypeBuilder.create(BiorayInfusionMatrixEntity::new, BlockInit.INFUSION_MATRIX_BLOCK).build());
}
