package com.raspix.fabric.cobble_contests;

import com.mojang.logging.LogUtils;
import com.raspix.common.cobble_contests.CobbleContests;

import com.raspix.common.cobble_contests.ExampleCommandRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CobbleContestsFabric implements ModInitializer {
    public static final String MOD_ID = "cobble_contests";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    /**public static IConfig CONFIG;
    public static AbstractNetworkHandler NETWORK;

    public static void init() {
        NETWORK.registerMessagesServer();
    }

    public static void initClient() {
        InteractionGuiHandler.init();
    }*/
    @Override
    public void onInitialize() {
        CobbleContests.init();
        CommandRegistrationCallback.EVENT.register(ExampleCommandRegistry::registerCommands);
    }

}
