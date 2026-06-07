package net.zhaiji.catburger.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Items;
import net.zhaiji.catburger.init.InitItem;

import java.util.concurrent.CompletableFuture;

public class RecipeProvider extends net.minecraft.data.recipes.RecipeProvider {
    protected RecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        shaped(RecipeCategory.MISC, InitItem.CAT_BURGER.get())
                .pattern(" A ")
                .pattern("BCB")
                .pattern(" A ")
                .define('A', Items.BREAD)
                .define('B', Items.TOTEM_OF_UNDYING)
                .define('C', Items.COOKED_BEEF)
                .unlockedBy("has_totem", has(Items.TOTEM_OF_UNDYING))
                .save(output);
    }

    public static class Runner extends net.minecraft.data.recipes.RecipeProvider.Runner {
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        public String getName() {
            return "CatBurger Recipes";
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new RecipeProvider(registries, output);
        }
    }
}
