package com.raspix.forge.cobble_contests.blocks.entity;

import com.raspix.common.cobble_contests.CobbleContests;
import com.raspix.forge.cobble_contests.blocks.BlockInit;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CobbleContests.MOD_ID);

    public static final RegistryObject<BlockEntityType<ContestBlockEntity>> CONTEST_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("contest_block", () -> BlockEntityType.Builder.of
                            (ContestBlockEntity::new, BlockInit.CONTEST_BLOCK.get())
                    .build(null));

    public static final RegistryObject<BlockEntityType<PoffinPotBlockEntity>> POFFIN_POT_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("poffin_pot", () -> BlockEntityType.Builder.of
                            (PoffinPotBlockEntity::new, BlockInit.POFFIN_POT.get())
                    .build(null));
}
