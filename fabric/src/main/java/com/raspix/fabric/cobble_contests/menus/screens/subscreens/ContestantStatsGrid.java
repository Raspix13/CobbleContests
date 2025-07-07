package com.raspix.fabric.cobble_contests.menus.screens.subscreens;

import com.cobblemon.mod.common.api.gui.ParentWidget;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.storage.ClientParty;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.blaze3d.platform.InputConstants;
import com.raspix.common.cobble_contests.CobbleContests;
import com.raspix.fabric.cobble_contests.events.ContestMoves;
import com.raspix.fabric.cobble_contests.network.NetworkablePokemonData;
import com.raspix.fabric.cobble_contests.network.SB.SBShowoffRequestContestantInfo;
import com.raspix.fabric.cobble_contests.network.SB.SBUpdateContestInfo;
import com.raspix.fabric.cobble_contests.network.SB.SBUseContestMove;
import com.raspix.fabric.cobble_contests.util.data.ContestType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ContestantStatsGrid extends ParentWidget {

    public static final int MOVE_WIDTH = 92;
    public static final int MOVE_HEIGHT = 24;
    public static final float MOVE_VERTICAL_SPACING = 5F;
    public static final float MOVE_HORIZONTAL_SPACING = 13F;

    private List<ContestantTile> tiles;

    private ContestantTile move1;
    private ContestantTile move2;
    private ContestantTile move3;
    private ContestantTile move4;

    private UUID playerId;


    private static final ResourceLocation moveTex = ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/gui/contest_move.png");


    public ContestantStatsGrid(int i, int j, int k, int l, Component component, UUID playerID) {
        super(i, j, k, l, component);
        this.tiles = new ArrayList<>();
        this.playerId = playerID;

        ClientPlayNetworking.send(new SBShowoffRequestContestantInfo(playerId));

        //initializeTiles(4);

        /**move1 = new ContestantTile(0, MOVE_HEIGHT);
        move2 = new ContestantTile(0, (MOVE_HEIGHT + MOVE_VERTICAL_SPACING) + MOVE_HEIGHT);
        move3 = new ContestantTile(0, 2 * (MOVE_HEIGHT + MOVE_VERTICAL_SPACING) + MOVE_HEIGHT);
        move4 = new ContestantTile(0, 3 * (MOVE_HEIGHT + MOVE_VERTICAL_SPACING) + MOVE_HEIGHT);*/
    }

    private void initializeTiles(int numTiles){
        //tiles = new ArrayList<>();
        for (int i = 0; i < numTiles; i++){
            int finalHeight = (int) ((i * (MOVE_HEIGHT + MOVE_VERTICAL_SPACING)));
            tiles.add(new ContestantTile(0, finalHeight));
            tiles.get(i).setupButton("Owner_name", "Pokemon_name", null);
        }
    }

    public void setExpanded(boolean expanded){
        for (ContestantTile tile : tiles) {
            tile.setExpanded(expanded);
        }
    }

    public void setShowdownContestantsData(CompoundTag tag){
        int num = tag.getInt("contestants_size");

        List<NetworkablePokemonData> contestantDataList = new ArrayList<>();

        // TODO: Figure out why remaking the tiles makes them disappear when size of window changes
        if(tiles.size() != num){
            initializeTiles(num);
        }


        for(int i = 0; i < num; i ++){

            CompoundTag contestantTag = tag.getCompound("contestant" + i);
            NetworkablePokemonData data = new NetworkablePokemonData(contestantTag);

            contestantDataList.add(data);

            String playerName = data.getPlayerName();
            String pokeName = data.getPokeName();

            tiles.get(i).setupButton(playerName, pokeName, data);

        }


    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        for(int i = 0; i < tiles.size(); i++){
            tiles.get(i).render(guiGraphics, mouseX, mouseY, partialTicks);
        }

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

}