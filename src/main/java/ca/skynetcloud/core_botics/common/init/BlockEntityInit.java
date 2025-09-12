package ca.skynetcloud.core_botics.common.init;

import ca.skynetcloud.core_botics.common.entity.block.BiorayCollectorEntity;
import ca.skynetcloud.core_botics.common.entity.block.DectivatedRobotEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static ca.skynetcloud.core_botics.CoreBoticsMain.MODID;

public class BlockEntityInit {

    public static void initialize() {}

    public static final BlockEntityType<BiorayCollectorEntity> ENTROPY_COLLECTOR_ENTITY = register("bioray_collector_entity", (FabricBlockEntityTypeBuilder.Factory<? extends BiorayCollectorEntity>) BiorayCollectorEntity::new, BlockInit.BIORAY_COLLECTOR_BLOCK);
    public static final BlockEntityType<DectivatedRobotEntity> DEACTIVATED_ROBOT_ENTITY = register("deactivated_robot_entity",(FabricBlockEntityTypeBuilder.Factory<? extends DectivatedRobotEntity>) DectivatedRobotEntity::new, BlockInit.DEACTIVATED_ROBOT);





    private static <T extends BlockEntity> BlockEntityType<T> register(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        Identifier id = Identifier.of(MODID, name);
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }
}
