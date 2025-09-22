package ca.skynetcloud.core_botics.common.init;


import ca.skynetcloud.core_botics.common.item.CardRemoverItem;
import ca.skynetcloud.core_botics.common.item.ConvertTool;
import ca.skynetcloud.core_botics.common.item.UpgradeCardItem;
import ca.skynetcloud.core_botics.common.item.tools.BiorayPickaxe;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

import static ca.skynetcloud.core_botics.CoreBoticsMain.id;


public class ItemInit {

    public static void initialize() {}

    public static final UpgradeCardItem ConversionCard = Registry.register(Registries.ITEM, id("conversion_card"), new UpgradeCardItem(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, id("conversion_card")))));
    public static final UpgradeCardItem tickUpgradeCard = Registry.register(Registries.ITEM, id("tick_card"), new UpgradeCardItem(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, id("tick_card")))));
    public static final UpgradeCardItem SpeedCard = Registry.register(Registries.ITEM, id("speed_card"), new UpgradeCardItem(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, id("speed_card")))));
    public static final UpgradeCardItem FLYING_CARD = Registry.register(Registries.ITEM, id("flying_card"), new UpgradeCardItem(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, id("flying_card")))));
    public static final Item SPEED_CARD_REMOVER = Registry.register(Registries.ITEM, id("card_remover_speed"),  new CardRemoverItem(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, id("card_remover_speed"))), ItemInit.SpeedCard));
    public static final Item TICK_CARD_REMOVER  = Registry.register(Registries.ITEM, id("card_remover_tick"),  new CardRemoverItem(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, id("card_remover_tick"))), ItemInit.tickUpgradeCard));
    public static final Item CONVERT_TOOL  = Registry.register(Registries.ITEM, id("convert_tool"),  new ConvertTool(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, id("convert_tool")))));
    public static final Item BIORAY_PICKAXE = Registry.register(Registries.ITEM, id("bioray_pickaxe"), new BiorayPickaxe(new Item.Settings().pickaxe(ToolMaterial.NETHERITE, 10, 50).registryKey(RegistryKey.of(RegistryKeys.ITEM, id("bioray_pickaxe")))));



}