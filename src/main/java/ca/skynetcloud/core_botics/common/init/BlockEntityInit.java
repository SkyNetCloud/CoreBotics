package ca.skynetcloud.core_botics.common.init;

import ca.skynetcloud.core_botics.common.entity.block.EntropyCollectorEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class BlockEntityInit {

    public static void initialize() {}

    public static final BlockEntityType<EntropyCollectorEntity> ENTROPY_COLLECTOR_ENTITY = register((FabricBlockEntityTypeBuilder.Factory<? extends EntropyCollectorEntity>) EntropyCollectorEntity::new, BlockInit.ENTROPY_COLLECTOR_BLOCK);





    private static <T extends BlockEntity> BlockEntityType<T> register(
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        Identifier id = Identifier.of("core_botics", "entropy_collector_entity");
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }
}
