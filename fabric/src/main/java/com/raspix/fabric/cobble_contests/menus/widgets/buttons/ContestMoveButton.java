package com.raspix.fabric.cobble_contests.menus.widgets.buttons;

import com.raspix.common.cobble_contests.CobbleContests;
import com.raspix.fabric.cobble_contests.events.ContestMoves;
import com.raspix.fabric.cobble_contests.util.ContestType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class ContestMoveButton extends FixedImageButton{

    private ContestMoves.MoveData move;

    private String moveName;
    private ContestType conType;
    //private final Color rgb;

    private static final ResourceLocation moveTex = ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/gui/contest_move.png");
    private static final ResourceLocation moveOverlayTex = ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/gui/contest_move_overlay.png");


    public ContestMoveButton(int screenX, int screenY, int buttonWidth, int buttonHeight, OnPress onPress) {
        super(screenX, screenY, buttonWidth, buttonHeight, 0, 0, moveTex, onPress);
    }


    public void renderThis(GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        //renderThis(guiGraphics, i, j, f);
        int yOff = 0;
        if(this.isHoveredOrFocused()){
            yOff = 24;
            System.out.println("Heya");
        }
        guiGraphics.blit(this.spriteLocation, this.getX(), this.getY(), this.xTexStart, this.yTexStart + yOff, this.getWidth(), this.getHeight(), 92, 48);
        //guiGraphics.blit(moveOverlayTex, this.getX(), this.getY(), this.xTexStart, this.yTexStart, this.getWidth(), this.getHeight(), 92, this.textureHeight);

    }
}
