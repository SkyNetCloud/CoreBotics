package ca.skynetcloud.core_botics.common.init;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static ca.skynetcloud.core_botics.CoreBoticsMain.MODID;
import static net.minecraft.registry.RegistryKeys.ITEM_GROUP;

public class ItemGroupInit {

    public static final RegistryKey<ItemGroup> MOD_JAM_ITEM_GROUP_KEY = RegistryKey.of(ITEM_GROUP, Identifier.of(MODID, "item_group"));
    public static final ItemGroup MODJAM_ITEM_GROUP = FabricItemGroup.builder().icon(() -> new ItemStack(BlockInit.BIORAY_COLLECTOR_BLOCK.asItem())).displayName(Text.translatable("itemGroup.core_botics")).build();

}
