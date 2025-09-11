package ca.skynetcloud.core_botics.common.recipes;

import ca.skynetcloud.core_botics.CoreBoticsMain;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EntropyRecipeManager {

    private static final List<EntropyRecipe> RECIPES = new ArrayList<>();

    public static void add(Item input, Item output, int entropyCost, String name) {
        if (input == Items.AIR || output == Items.AIR) {
            throw new IllegalArgumentException("Input or output cannot be AIR!");
        }
        RECIPES.add(new EntropyRecipe(input, output, entropyCost, name));
    }

    public static EntropyRecipe getRecipe(ItemStack stack) {
        for (EntropyRecipe recipe : RECIPES) {
            if (recipe.matches(stack)) return recipe;
        }
        return null;
    }

    public static List<EntropyRecipe> getRecipes() {
        return RECIPES;
    }

    public static void exportAllRecipes(FabricDataOutput output) {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        for (EntropyRecipe recipe : RECIPES) {
            JsonObject json = new JsonObject();
            json.addProperty("input", Registries.ITEM.getId(recipe.input()).toString());
            json.addProperty("output", Registries.ITEM.getId(recipe.output()).toString());
            json.addProperty("entropy_cost", recipe.entropyCost());

            Path path = output.getPath()
                    .resolve("data")
                    .resolve(CoreBoticsMain.MODID)
                    .resolve("data/core_botics/entropy_recipes")
                    .resolve(recipe.name() + ".json");

            try {
                Files.createDirectories(path.getParent());
                Files.writeString(path, gson.toJson(json));
                System.out.println("[CoreBoticsMain] Exported entropy recipe: " +
                        recipe.name() + " → " +
                        Registries.ITEM.getId(recipe.input()) + " → " +
                        Registries.ITEM.getId(recipe.output()));
            } catch (IOException e) {
                throw new RuntimeException("Failed to write entropy recipe JSON: " + recipe.name(), e);
            }
        }
    }

    public static void loadRecipesFromResources() {
        CoreBoticsMain.LOGGER.info("Loading entropy recipes from resources...");
        try {
            ClassLoader loader = CoreBoticsMain.class.getClassLoader();
            String folder = "data/core_botics/entropy_recipes/";
            var resources = loader.getResources(folder);

            while (resources.hasMoreElements()) {
                var url = resources.nextElement();
                Path path;
                if (url.getProtocol().equals("file")) {
                    path = Paths.get(url.toURI());
                    Files.list(path).forEach(file -> {
                        try {
                            String jsonString = Files.readString(file);
                            JsonObject json = new Gson().fromJson(jsonString, JsonObject.class);
                            String inputId = json.get("input").getAsString();
                            String outputId = json.get("output").getAsString();
                            int cost = json.get("entropy_cost").getAsInt();

                            Item input = Registries.ITEM.get(Identifier.of(inputId));
                            Item output = Registries.ITEM.get(Identifier.of(outputId));
                            String name = file.getFileName().toString().replace(".json", "");

                            add(input, output, cost, name);
                            CoreBoticsMain.LOGGER.info("Loaded recipe from file: {}", name);

                        } catch (Exception e) {
                            CoreBoticsMain.LOGGER.error("Failed to load recipe from file: {}", file.getFileName(), e);
                        }
                    });
                } else if (url.getProtocol().equals("jar")) {
                    var jarPath = url.getPath().split("!")[0].replace("file:", "");
                    try (var jar = new java.util.jar.JarFile(jarPath)) {
                        jar.stream()
                                .filter(entry -> entry.getName().startsWith(folder) && entry.getName().endsWith(".json"))
                                .forEach(entry -> {
                                    try (var is = jar.getInputStream(entry)) {
                                        String jsonString = new String(is.readAllBytes());
                                        JsonObject json = new Gson().fromJson(jsonString, JsonObject.class);

                                        String inputId = json.get("input").getAsString();
                                        String outputId = json.get("output").getAsString();
                                        int cost = json.get("entropy_cost").getAsInt();

                                        Item input = Registries.ITEM.get(Identifier.of(inputId));
                                        Item output = Registries.ITEM.get(Identifier.of(outputId));
                                        String name = entry.getName().replace(folder, "").replace(".json", "");

                                        add(input, output, cost, name);
                                        CoreBoticsMain.LOGGER.info("Loaded recipe from jar: {}", name);
                                    } catch (Exception e) {
                                        CoreBoticsMain.LOGGER.error("Failed to load recipe from jar entry: {}", entry.getName(), e);
                                    }
                                });
                    }
                }
            }
        } catch (Exception e) {
            CoreBoticsMain.LOGGER.error("Failed to load entropy recipes from resources", e);
        }
        CoreBoticsMain.LOGGER.info("Finished loading entropy recipes. Total recipes: {}", RECIPES.size());
    }}
