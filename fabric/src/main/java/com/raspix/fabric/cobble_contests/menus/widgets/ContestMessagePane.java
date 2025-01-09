package com.raspix.fabric.cobble_contests.menus.widgets;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractScrollWidget;
import net.minecraft.client.gui.components.FittingMultiLineTextWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import static com.cobblemon.mod.common.util.MiscUtilsKt.cobblemonResource;

public class ContestMessagePane extends FittingMultiLineTextWidget {

    private int xPos;
    private int yPos;

    private static final ResourceLocation BATTLE_MESSAGE_PANE_FRAME_RESOURCE = cobblemonResource("textures/gui/battle/battle_log.png");
    private static final ResourceLocation BATTLE_MESSAGE_PANE_FRAME_EXPANDED_RESOURCE = cobblemonResource("textures/gui/battle/battle_log_expanded.png");

    public ContestMessagePane(int i, int j, int k, int l, Component component, Font font) {
        super(i, j, k, l, component, font);
        xPos = i;
        yPos = j;
        System.out.println(font);
    }

    @Override
    protected int getInnerHeight() {
        return 0;
    }

    @Override
    protected double scrollRate() {
        return 0;
    }

    @Override
    protected void renderContents(GuiGraphics guiGraphics, int i, int j, float f) {

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mX, int mY, float partials) {
        super.renderWidget(guiGraphics, mX, mY, partials);
        guiGraphics.blit(BATTLE_MESSAGE_PANE_FRAME_RESOURCE, xPos, yPos, 0, 0, 169, 55, 169, 55);
    }

}
