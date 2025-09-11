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
        translationBuilder.add(BlockInit.ENTROPY_COLLECTOR_BLOCK, "Entropy Collector");
        translationBuilder.add(BlockInit.ENTROPY_COLLECTOR_BLOCK.asItem(), "Entropy Collector");

        translationBuilder.add(ItemInit.SpeedCard, "Speed Card");
        translationBuilder.add(ItemInit.ConversionCard, "Conversion Card");

        translationBuilder.add("gui.core_botics.entropyscreen.label_stored_entropy", "Stored Entropy: %d/%d");
        translationBuilder.add("container.core_botics.entropy", "Entropy Collector");

        addText(translationBuilder, MODJAM_ITEM_GROUP.getDisplayName(), "Mod Jam MOD");
    }
}
