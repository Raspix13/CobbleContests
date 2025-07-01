package com.raspix.fabric.cobble_contests.menus.widgets.to_delete;

import com.raspix.fabric.cobble_contests.menus.screens.ContestBoothScreen;
import com.raspix.fabric.cobble_contests.menus.widgets.buttons.FixedImageButton;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.FittingMultiLineTextWidget;
import net.minecraft.network.chat.Component;

import java.util.*;

/**
 * Meant to be used for both players joined a contest lobby and finding a contest
 */


public class MultiplayerContestScrollPlane extends FittingMultiLineTextWidget {

    ContestBoothScreen screen;

    private List<FixedImageButton> playerPanels;

    private HashMap<UUID, FixedImageButton> assignedPanels;

    public MultiplayerContestScrollPlane(ContestBoothScreen screen, int i, int j, int k, int l, Component component, Font font) {
        super(i, j, k, l, component, font);
        this.screen = screen;
        playerPanels = new ArrayList<>();
        assignedPanels = new HashMap<>();
    }

    public void AddPanel(UUID playerId){
        //if(playerPanels.containsKey(playerId)){
            //return;
        //}

    }

    public void reloadPanels(List<UUID> playerList){
        assignedPanels.clear();

        //if(){

        //}
    }

    public void clearAllPanels(){
        //for(FixedImageButton panel: playerPanels){
            //screen.redoPlayerPanes();

        //}
    }



}
