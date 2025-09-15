package ca.skynetcloud.core_botics.common.recipes.serializers;

import ca.skynetcloud.core_botics.common.recipes.BiorayInfusionRecipe;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.dynamic.Codecs;

import java.util.ArrayList;


public class BiorayInfusionSerializer implements RecipeSerializer<BiorayInfusionRecipe> {
    public static final MapCodec<BiorayInfusionRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("matrix").forGetter(BiorayInfusionRecipe::matrix),
            Ingredient.CODEC.listOf().fieldOf("pedestals").forGetter(BiorayInfusionRecipe::pedestals),
            ItemStack.CODEC.fieldOf("result").forGetter(BiorayInfusionRecipe::output),
            Codecs.POSITIVE_INT.fieldOf("bioray").forGetter(BiorayInfusionRecipe::bioray)
    ).apply(instance, BiorayInfusionRecipe::new));

    public static final PacketCodec<RegistryByteBuf, BiorayInfusionRecipe> STREAM_CODEC =
            PacketCodec.tuple(
                    Ingredient.PACKET_CODEC, BiorayInfusionRecipe::matrix,
                    PacketCodecs.collection(ArrayList::new, Ingredient.PACKET_CODEC), BiorayInfusionRecipe::pedestals,
                    ItemStack.PACKET_CODEC, BiorayInfusionRecipe::output,
                    PacketCodecs.VAR_INT, BiorayInfusionRecipe::bioray,
                    BiorayInfusionRecipe::new
            );
    @Override
    public MapCodec<BiorayInfusionRecipe> codec() {
        return CODEC;
    }

    @Override
    public PacketCodec<RegistryByteBuf, BiorayInfusionRecipe> packetCodec() {
        return STREAM_CODEC;
    }
}
