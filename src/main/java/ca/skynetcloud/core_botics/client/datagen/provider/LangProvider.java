package ca.skynetcloud.core_botics.client.datagen.provider;

import ca.skynetcloud.core_botics.CoreBoticsMain;
import ca.skynetcloud.core_botics.common.init.BlockInit;
import ca.skynetcloud.core_botics.common.init.ItemInit;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static ca.skynetcloud.core_botics.common.init.ItemGroupInit.MODJAM_ITEM_GROUP;

public class LangProvider extends FabricLanguageProvider {

    public LangProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    private static void addText(@NotNull TranslationBuilder builder, @NotNull Text text, @NotNull String value) {
        if (text.getContent() instanceof TranslatableTextContent translatableTextContent) {
            builder.add(translatableTextContent.getKey(), value);
        } else {
            CoreBoticsMain.LOGGER.warn("Failed to add translation for text: {}", text.getString());
        }
    }


    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(BlockInit.BIORAY_COLLECTOR_ITEM, "Bioray Collector");
        translationBuilder.add(BlockInit.INFUSION_PEDESTAL_ITEM, "Infusion Pedestal");
        translationBuilder.add(BlockInit.INFUSION_MATRIX_ITEM, "Infusion Matrix");
        translationBuilder.add(BlockInit.DEACTIVATED_ROBOT_ITEM, "Deactivated Robot");



        translationBuilder.add(ItemInit.SpeedCard, "Speed Upgrade Card");
        translationBuilder.add(ItemInit.tickUpgradeCard, "Tick Upgrade Card");
        translationBuilder.add(ItemInit.CONVERT_TOOL, "Convert Tool");
        translationBuilder.add(ItemInit.FLYING_CARD, "Flying Card");
        translationBuilder.add(ItemInit.BIORAY_PICKAXE, "Bioray Pickaxe");
        translationBuilder.add(ItemInit.ConversionCard, "Conversion Upgrade Card");
        translationBuilder.add(ItemInit.SPEED_CARD_REMOVER, "Speed Remover Card");
        translationBuilder.add(ItemInit.TICK_CARD_REMOVER, "Tick Remover Card");

        translationBuilder.add("gui.core_botics.label.stored_entropy", "Stored Bioray: %d/%d");
        translationBuilder.add("container.core_botics.entropy", "Bioray Collector");
        translationBuilder.add("container.infusion_matrix", "Bioray Infusion Matrix");
        translationBuilder.add("message.no_collector_block_below", "Block will not be active. Place a collector below it to activate");


        translationBuilder.add("item.core_botics.tick_card.tooltip", "Boosts tick speed of adjacent blocks. Stacks up to 10. on only Bioray Collector");

        addText(translationBuilder, MODJAM_ITEM_GROUP.getDisplayName(), "Core Botics");
    }
}
