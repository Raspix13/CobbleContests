package com.raspix.forge.cobble_contests.recipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

public class PoffinRecipe extends AbstractCookingRecipe {
    public PoffinRecipe(RecipeType<?> arg, ResourceLocation arg2, String string, CookingBookCategory arg3, Ingredient arg4, ItemStack arg5, float f, int i) {
        super(arg, arg2, string, arg3, arg4, arg5, f, i);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }
}
