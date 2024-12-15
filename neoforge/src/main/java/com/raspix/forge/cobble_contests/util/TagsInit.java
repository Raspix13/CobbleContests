package com.raspix.forge.cobble_contests.util;

import com.raspix.common.cobble_contests.CobbleContests;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class TagsInit {

    public static class Blocks{
        //public static final TagKey<Item> COBBLEMON_BERRIES = tag("cobblemon_berries");


        private static TagKey<Block> tag(String name){
            return BlockTags.create(new ResourceLocation(CobbleContests.MOD_ID, name));
        }
    }

    public static class Items{
        public static final TagKey<Item> COBBLEMON_BERRIES = tag("cobblemon_berries");


        private static TagKey<Item> tag(String name){
            //return ItemTags.create(new ResourceLocation(CobbleContests.MOD_ID, name));

            return TagKey.create(Registries.ITEM, new ResourceLocation(CobbleContests.MOD_ID, name));
        }
        //TagKey[minecraft:item / cobble_contests:cobblemon_berries]
    }

}
