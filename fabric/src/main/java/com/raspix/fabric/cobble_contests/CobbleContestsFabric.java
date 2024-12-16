package com.raspix.fabric.cobble_contests;

import com.raspix.common.cobble_contests.ExampleCommandRegistry;
//import com.raspix.fabric.cobble_contests.blocks.BlockInit;
//import com.raspix.fabric.cobble_contests.blocks.entity.BlockEntityInit;
import com.raspix.fabric.cobble_contests.blocks.BlockInit;
import com.raspix.fabric.cobble_contests.blocks.entity.BlockEntityInit;
import com.raspix.fabric.cobble_contests.items.ItemInit;
//import com.raspix.fabric.cobble_contests.menus.MenuInit;
//import com.raspix.fabric.cobble_contests.menus.screens.PoffinPotScreen;
//import com.raspix.fabric.cobble_contests.network.MessagesInit;
import com.raspix.fabric.cobble_contests.menus.MenuInit;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.gui.screens.MenuScreens;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CobbleContestsFabric implements ModInitializer {
    public static final String MOD_ID = "cobble_contests";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    //public static final CobbleContestsDataProvider dataProvider = new CobbleContestsDataProvider();

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
        //CobbleContests.init();
        //CommandRegistrationCallback.EVENT.register(ExampleCommandRegistry::registerCommands);

        BlockEntityInit.registerBlockEntities();
        ItemInit.registerItems();
        BlockInit.registerBlocks();
        MenuInit.registerMenus();

        //MessagesInit.registerC2SPackets();

        //dataProvider.registerDefaults();


        //MessagesInit.registerS2CPackets();

    }




}
