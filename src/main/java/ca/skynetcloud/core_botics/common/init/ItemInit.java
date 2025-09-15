package ca.skynetcloud.core_botics.common.init;


import ca.skynetcloud.core_botics.common.item.UpgradeCardItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

import static ca.skynetcloud.core_botics.CoreBoticsMain.MODID;
import static ca.skynetcloud.core_botics.CoreBoticsMain.id;


public class ItemInit {

    public static void initialize() {}

    public static final UpgradeCardItem ConversionCard = Registry.register(
            Registries.ITEM,
            id("conversion_card"),
            new UpgradeCardItem(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, id("conversion_card"))))
    );
    public static Item SpeedCard = Registry.register(
            Registries.ITEM,
            id("speed_card"),
            new UpgradeCardItem(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, id("speed_card"))))
    );



}