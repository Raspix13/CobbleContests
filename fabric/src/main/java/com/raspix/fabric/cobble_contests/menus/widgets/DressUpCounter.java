package com.raspix.fabric.cobble_contests.menus.widgets;

import com.raspix.common.cobble_contests.CobbleContests;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class DressUpCounter extends AbstractWidget {

    private static final ResourceLocation NUMBERS = ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/gui/numbers.png");

    int tenstDigit;
    int onesDigit;
    int leftPos;
    int topPos;

    private int max_counter = 60;

    public DressUpCounter(int i, int j, int k, int l, Component component) {
        super(i, j, k, l, component);
        this.leftPos = i;
        this.topPos = j;
        this.max_counter = 60;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        renderDigit(guiGraphics, 0, tenstDigit); // tens place
        renderDigit(guiGraphics, 1, onesDigit); // ones place
    }

    private void renderDigit(GuiGraphics guiGraphics, int digitIndex, int digitValue){
        guiGraphics.blit(NUMBERS, this.leftPos + (digitIndex * 13), this.topPos, 13 * digitValue, 0, 13, 21, 130, 21);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    public void updateTime(int seconds){
        int time = Math.max(max_counter - seconds, 0);
        this.tenstDigit = time / 10;
        this.onesDigit = time % 10;
    }

    public void updateTime(int maxSeconds, int seconds){
        int time = maxSeconds - seconds;
        this.tenstDigit = time / 10;
        this.onesDigit = time % 10;
    }

    public void changePos(int newX, int newY, int newMax){
        this.leftPos = newX;
        this.topPos = newY;
        this.max_counter = newMax;
    }
}
