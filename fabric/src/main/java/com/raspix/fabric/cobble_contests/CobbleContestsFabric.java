package com.raspix.fabric.cobble_contests;

//import com.raspix.fabric.cobble_contests.blocks.BlockInit;
//import com.raspix.fabric.cobble_contests.blocks.entity.BlockEntityInit;
import com.cobblemon.mod.common.platform.events.PlatformEvents;
import com.cobblemon.mod.common.platform.events.ServerTickEvent;
import com.raspix.fabric.cobble_contests.blocks.BlockInit;
import com.raspix.fabric.cobble_contests.blocks.entity.BlockEntityInit;
import com.raspix.fabric.cobble_contests.items.ItemInit;
//import com.raspix.fabric.cobble_contests.menus.MenuInit;
//import com.raspix.fabric.cobble_contests.menus.screens.PoffinPotScreen;
//import com.raspix.fabric.cobble_contests.network.MessagesInit;
import com.raspix.fabric.cobble_contests.menus.MenuInit;
import com.raspix.fabric.cobble_contests.network.MessagesInit;
import com.raspix.fabric.cobble_contests.network.SBWalletScreenParty;
import com.raspix.fabric.cobble_contests.util.ContestManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.level.BlockEventData;
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

        //PayloadTypeRegistry.playC2S().register(SBWalletScreenParty.PACKET_ID, SBWalletScreenParty.PACKET_CODEC);
        //PayloadTypeRegistry.playS2C().register(SBWalletScreenParty.PACKET_ID, SBWalletScreenParty.PACKET_CODEC);

        MessagesInit.registerC2SPackets();

        //dataProvider.registerDefaults();

        ContestManager.INSTANCE.OnServerSetUp();

        //MessagesInit.registerS2CPackets();

        //ServerTickEvents.START_SERVER_TICK..register {server -> PlatformEvents.SERVER_TICK_PRE.post(new ServerTickEvent.Pre(server)) }
        ServerTickEvents.START_SERVER_TICK.register(server ->
                //PlatformEvents.SERVER_TICK_PRE.post(new ServerTickEvent.Pre(server))
                ContestManager.INSTANCE.update(server)
        ); //new ServerTickEvent.Pre(server)

        ContestConfig.loadConfig();

    }


}
