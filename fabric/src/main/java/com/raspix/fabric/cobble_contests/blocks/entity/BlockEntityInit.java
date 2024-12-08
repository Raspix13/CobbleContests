package com.raspix.fabric.cobble_contests.blocks.entity;

import com.raspix.common.cobble_contests.CobbleContests;
import com.raspix.fabric.cobble_contests.CobbleContestsFabric;
import com.raspix.fabric.cobble_contests.blocks.BlockInit;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.intellij.lang.annotations.Identifier;
import org.jetbrains.annotations.Nullable;

public class BlockEntityInit {

    /**public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CobbleContests.MOD_ID);

    public static final RegistryObject<BlockEntityType<ContestBlockEntity>> CONTEST_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("contest_block", () -> BlockEntityType.Builder.of
                            (ContestBlockEntity::new, BlockInit.CONTEST_BOOTH.get())
                    .build(null));

    public static final RegistryObject<BlockEntityType<PoffinPotBlockEntity>> POFFIN_POT_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("poffin_pot", () -> BlockEntityType.Builder.of
                            (PoffinPotBlockEntity::new, BlockInit.POFFIN_POT.get())
                    .build(null));*/


    public static final BlockEntityType CONTEST_BLOCK_ENTITY = registerBlockEntity("contest_block", BlockEntityType.Builder.of(ContestBlockEntity::new, BlockInit.CONTEST_BOOTH).build(null));
    public static final BlockEntityType POFFIN_POT_BLOCK_ENTITY = registerBlockEntity("poffin_pot", BlockEntityType.Builder.of(PoffinPotBlockEntity::new, BlockInit.POFFIN_POT).build(null));


    private static BlockEntityType registerBlockEntity(String name, BlockEntityType blockEntityType) {
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(CobbleContestsFabric.MOD_ID, name), blockEntityType);
    }

    public static void registerBlockEntities() {
        CobbleContestsFabric.LOGGER.info("Registering Block Entities for " + CobbleContestsFabric.MOD_ID);
    }

}
