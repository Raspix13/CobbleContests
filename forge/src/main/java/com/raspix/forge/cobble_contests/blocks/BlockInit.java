package com.raspix.forge.cobble_contests.blocks;

import com.raspix.common.cobble_contests.CobbleContests;
import com.raspix.forge.cobble_contests.CobbleContestsForge;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CobbleContestsForge.MOD_ID);

    public static final RegistryObject<Block> CONTEST_BLOCK = BLOCKS.register("contest_block", () -> new ContestBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 6.0F).sound(SoundType.METAL)));

    public static final RegistryObject<Block> POFFIN_POT = BLOCKS.register("poffin_pot", () -> new PoffinPot(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(5.0F, 6.0F).sound(SoundType.METAL)));

}
