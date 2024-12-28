package com.raspix.neoforge.cobble_contests.blocks;

import com.raspix.common.cobble_contests.CobbleContests;
import com.raspix.neoforge.cobble_contests.CobbleContestsForge;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
//import net.minecraftforge.registries.DeferredRegister;
//import net.minecraftforge.registries.ForgeRegistries;
//import net.minecraftforge.registries.RegistryObject;

public class BlockInit {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CobbleContestsForge.MOD_ID);

    public static final DeferredBlock<Block> CONTEST_BOOTH = BLOCKS.register("contest_booth", () -> new ContestBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(2.0F).sound(SoundType.WOOD)));

    public static final DeferredBlock<Block> POFFIN_POT = BLOCKS.register("poffin_pot", () -> new PoffinPot(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).strength(0.5F).sound(SoundType.ANVIL)));

}
