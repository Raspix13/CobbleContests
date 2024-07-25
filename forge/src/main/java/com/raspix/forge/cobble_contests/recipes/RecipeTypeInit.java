package com.raspix.forge.cobble_contests.recipes;

import com.raspix.forge.cobble_contests.CobbleContestsForge;
import com.raspix.forge.cobble_contests.blocks.ContestBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeTypeInit {

    /**public static final DeferredRegister<RecipeType> RECIPES = DeferredRegister.create((ResourceLocation) ForgeRegistries.RECIPE_TYPES, CobbleContestsForge.MOD_ID);

    public static final RegistryObject<RecipeType> POFFIN_RECIPES = RECIPES.register(BuiltInRegistries.RECIPE_TYPE,new ResourceLocation("contest_block"),  new RecipeType<T>() {
        public String toString() {
            return "contest_block";
        }
    });

    RecipeType<CraftingRecipe> CRAFTING = register("crafting");
    RecipeType<SmeltingRecipe> SMELTING = register("smelting");
    RecipeType<BlastingRecipe> BLASTING = register("blasting");
    RecipeType<SmokingRecipe> SMOKING = register("smoking");
    RecipeType<CampfireCookingRecipe> CAMPFIRE_COOKING = register("campfire_cooking");
    RecipeType<StonecutterRecipe> STONECUTTING = register("stonecutting");
    RecipeType<SmithingRecipe> SMITHING = register("smithing");

    static <T extends Recipe<?>> RecipeType<T> register(final String string) {
        return (RecipeType) Registry.register(BuiltInRegistries.RECIPE_TYPE, new ResourceLocation(string), new RecipeType<T>() {
            public String toString() {
                return string;
            }
        });
    }

    static <T extends Recipe<?>> RecipeType<T> simple(ResourceLocation name) {
        final String toString = name.toString();
        return new RecipeType<T>() {
            public String toString() {
                return toString;
            }
        };
    }*/
}
