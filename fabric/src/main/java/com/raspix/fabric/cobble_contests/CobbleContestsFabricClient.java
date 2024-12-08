package com.raspix.fabric.cobble_contests;

import com.raspix.fabric.cobble_contests.menus.MenuInit;
import com.raspix.fabric.cobble_contests.menus.screens.PoffinPotScreen;
import com.raspix.fabric.cobble_contests.menus.screens.SecondTestScreen;
import com.raspix.fabric.cobble_contests.network.MessagesInit;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class CobbleContestsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        //MenuScreens.register(MenuInit.CONTEST_MENU.get(), SecondTestScreen::new);
        //MenuScreens.register(MenuInit.PLAYER_CONTEST_INFO_MENU.get(), PlayerContestInfoScreen::new);
        MenuScreens.register(MenuInit.POFFIN_POT_MENU, PoffinPotScreen::new);
        MenuScreens.register(MenuInit.CONTEST_MENU, SecondTestScreen::new);
        MenuInit.registerScreens();

        MessagesInit.registerS2CPackets();
    }
}
