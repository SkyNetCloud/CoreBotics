package ca.skynetcloud.core_botics.common.init;

import ca.skynetcloud.core_botics.common.recipes.BiorayInfusionRecipe;
import ca.skynetcloud.core_botics.common.recipes.serializers.BiorayInfusionSerializer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import static ca.skynetcloud.core_botics.CoreBoticsMain.id;

public class RecipeInit {

    public static void initialize() {}

    public static final RecipeSerializer<BiorayInfusionRecipe> GROWTH_CHAMBER_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER, id( "bioray_infusion"),
            new BiorayInfusionSerializer());
    public static final RecipeType<BiorayInfusionRecipe> BIORAY_INFUSION_RECIPE_TYPE = Registry.register(Registries.RECIPE_TYPE, id("bioray_infusion"), new RecipeType<BiorayInfusionRecipe>() {
            @Override
            public String toString() {
                return "bioray_infusion";
            }
        });

}
