package com.raspix.fabric.cobble_contests.blocks;

import com.raspix.fabric.cobble_contests.CobbleContestsFabric;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

public class BlockInit {

    public static final Block CONTEST_BOOTH = registerBlock("contest_booth", new ContestBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(2.0F).sound(SoundType.WOOD)));
    public static final Block POFFIN_POT = registerBlock("poffin_pot", new PoffinPot(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).strength(0.5F).sound(SoundType.ANVIL)));


    /**private static Block registerBlock(@Nullable String type) {
        return registerBlock(type,
                new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    }*/


    private static Block registerBlock(String name, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, name), block);
    }

    public static void registerBlocks() {
        CobbleContestsFabric.LOGGER.info("Registering Blocks for " + CobbleContestsFabric.MOD_ID);
    }

}
