package com.raspix.fabric.cobble_contests.util;

import com.raspix.fabric.cobble_contests.CobbleContestsFabric;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

import java.util.function.UnaryOperator;

public class DataComponentsInit {

    /**private static <T>DataComponentType<T> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator){
        return Registry.register(Registries.DATA_COMPONENT_TYPE, ResourceLocation.fromNamespaceAndPath(CobbleContestsFabric.MOD_ID, name), builderOperator.apply(DataComponentType.builder()).build());
    }*/



    public static void registerDataComponentTypes(){
        CobbleContestsFabric.LOGGER.info("Registering Data Types for " + CobbleContestsFabric.MOD_ID);
    }


}
