package ca.skynetcloud.core_botics.common.recipes.serializers;

import ca.skynetcloud.core_botics.common.recipes.BiorayInfusionRecipe;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.dynamic.Codecs;


public class BiorayInfusionSerializer implements RecipeSerializer<BiorayInfusionRecipe> {
    public static final MapCodec<BiorayInfusionRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codecs.POSITIVE_INT.fieldOf("bioray").forGetter(BiorayInfusionRecipe::bioray),
            Ingredient.CODEC.fieldOf("ingredient").forGetter(BiorayInfusionRecipe::ingredient),
            ItemStack.CODEC.fieldOf("result").forGetter(BiorayInfusionRecipe::output)
    ).apply(instance, (bioray, ingredient, result) -> new BiorayInfusionRecipe(ingredient, result, bioray)));


    @Override
    public MapCodec<BiorayInfusionRecipe> codec() {
        return CODEC;
    }

    @Override
    public PacketCodec<RegistryByteBuf, BiorayInfusionRecipe> packetCodec() {
        return null;
    }
}
