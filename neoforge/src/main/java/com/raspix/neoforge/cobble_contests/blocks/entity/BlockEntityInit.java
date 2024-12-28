package com.raspix.neoforge.cobble_contests.blocks.entity;

import com.raspix.common.cobble_contests.CobbleContests;
import com.raspix.neoforge.cobble_contests.CobbleContestsForge;
import com.raspix.neoforge.cobble_contests.blocks.BlockInit;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class BlockEntityInit {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, CobbleContestsForge.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ContestBlockEntity>> CONTEST_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("contest_block", () -> BlockEntityType.Builder.of
                            (ContestBlockEntity::new, BlockInit.CONTEST_BOOTH.get())
                    .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PoffinPotBlockEntity>> POFFIN_POT_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("poffin_pot", () -> BlockEntityType.Builder.of
                            (PoffinPotBlockEntity::new, BlockInit.POFFIN_POT.get())
                    .build(null));
}
