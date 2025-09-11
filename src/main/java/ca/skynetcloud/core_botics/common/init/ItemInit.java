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


public class ItemInit {

    public static Item ConversionCard;
    public static Item SpeedCard;

    public static void initialize() {
        ConversionCard = register("conversion_card", UpgradeCardItem::new, new Item.Settings());
        SpeedCard = register("speed_card", UpgradeCardItem::new, new Item.Settings());
    }


    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MODID, name));
        Item item = itemFactory.apply(settings.registryKey(itemKey));
        Registry.register(Registries.ITEM, itemKey, item);
        return item;
    }
}