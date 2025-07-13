package com.raspix.fabric.cobble_contests.menus.widgets;

import com.raspix.common.cobble_contests.CobbleContests;
import com.raspix.fabric.cobble_contests.blocks.entity.ContestBlockEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;

public class LobbyContestantPanel {

    private ResourceLocation background = ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/gui/contest_booth.png");
    private String hostName;
    private final int panelWidth = 142;
    private final int panelHeight = 10;

    private String name;
    private int x;
    private int y;

    public LobbyContestantPanel(String name, int x, int y){
        this.name = name;
        this.x = x;
        this.y = y;
    }


    public final void render(GuiGraphics guiGraphics, int i, int j, float f) {

        guiGraphics.blit(background, x, y, 294, 246, panelWidth, panelHeight, 1000, 750);
        drawScaledText(guiGraphics, Component.literal(name).getVisualOrderText(),
                x + 10,
                y + 2,
                1f, 1f, 1f, 0x00918b99, false, false);

    }
}
