package com.raspix.fabric.cobble_contests.menus.widgets;

import com.raspix.common.cobble_contests.CobbleContests;
import com.raspix.fabric.cobble_contests.network.NetworkablePokemonData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractScrollWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class LobbyContestantsScroll extends AbstractScrollWidget {

    private ResourceLocation background = ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/gui/contest_booth.png");
    private List<LobbyContestantPanel> contents;

    public LobbyContestantsScroll(int i, int j, int k, int l, Component component) {
        super(i, j, k, l, component);
        this.contents = new ArrayList<>();
        setContestantsData(new ArrayList<>());
        /**{{
            add("Name 1");
            add("Name 2");
            add("Name 3");
            add("Name 4");
            add("Name 5");
            add("Name 6");
            add("Name 7");
        }});*/
    }

    public void setContestantsDataFromTag(CompoundTag tag){
        if(tag == null){
            System.out.println("DATA IS NULL");
        }else{
            System.out.println("DATA IS NOT NULL, NUM CONTESTANTS IS: " + tag.getInt("contestants_size"));
        }

        int num = tag.getInt("contestants_size");

        List<String> contestantNames = new ArrayList<>();

        for(int i = 0; i < num; i ++){

            CompoundTag contestantTag = tag.getCompound("contestant" + i);
            NetworkablePokemonData data = new NetworkablePokemonData(contestantTag.copy());

            String playerName = data.getPlayerName();

            contestantNames.add(playerName);

        }
        setContestantsData(contestantNames);
    }

    public void setContestantsData(List<String> contestantNames){
        contents = new ArrayList<>();

        for(int i = 0; i < contestantNames.size(); i++){
            int finalI = i * 12;
            this.contents.add(new LobbyContestantPanel(contestantNames.get(i), 0, finalI));
        }

        //this.contents.add(new LobbyContestantPanel("Name 1", 0, 0));
        //this.contents.add(new LobbyContestantPanel("Name 2", 0, 22));
    }

    @Override
    protected int getInnerHeight() {
        return contents.size() * 12;
    }

    @Override
    protected double scrollRate() {
        return (double)9.0F;
    }

    protected void renderBackground(GuiGraphics guiGraphics) {
        //this.renderBorder(guiGraphics, this.getX(), this.getY(), this.getWidth(), this.getHeight());
        guiGraphics.blit(background, this.getX(), this.getY(), 294, 164, this.getWidth(), this.getHeight(), 1000, 750);
    }

    @Override
    protected void renderContents(GuiGraphics guiGraphics, int i, int j, float f) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate((float)(this.getX() + this.innerPadding()), (float)(this.getY() + this.innerPadding()), 0.0F);
        for(LobbyContestantPanel panel: contents){
            panel.render(guiGraphics, i, j, f);
        }
        //this.multilineWidget.render(guiGraphics, i, j, f);
        guiGraphics.pose().popPose();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
