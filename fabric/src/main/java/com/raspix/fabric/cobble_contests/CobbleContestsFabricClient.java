package com.raspix.fabric.cobble_contests;

//import com.raspix.fabric.cobble_contests.blocks.BlockInit;
//import com.raspix.fabric.cobble_contests.menus.MenuInit;
//import com.raspix.fabric.cobble_contests.menus.screens.PoffinPotScreen;
//import com.raspix.fabric.cobble_contests.menus.screens.SecondTestScreen;
//import com.raspix.fabric.cobble_contests.network.MessagesInit;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CobbleContestsFabricClient implements ClientModInitializer {
    public static final String MOD_ID = "cobble_contests";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


    @Override
    public void onInitializeClient() {
        //MenuInit.registerScreens();

        //MessagesInit.registerS2CPackets();

        //BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.CONTEST_BOOTH, RenderType.cutout());
    }
}
