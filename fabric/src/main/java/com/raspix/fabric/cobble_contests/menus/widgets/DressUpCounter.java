package com.raspix.fabric.cobble_contests.menus.widgets;

import com.raspix.common.cobble_contests.CobbleContests;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;

public class DressUpCounter extends AbstractWidget {

    private static final ResourceLocation NUMBERS = ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/gui/numbers.png");
    private static final ResourceLocation COUNTER_BALL = ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/gui/counter_ball.png");

    int tenstDigit;
    int onesDigit;
    int leftPos;
    int topPos;
    boolean renderBall;
    float sizeMod;

    private int max_counter = 60;

    public DressUpCounter(int i, int j, int k, int l, Component component) {
        super(i, j, k, l, component);
        this.leftPos = i;
        this.topPos = j;
        this.max_counter = 60;
        this.renderBall = false;
        this.sizeMod = 1f;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        if(renderBall) {
            correctSize();
            blitk(
                    guiGraphics.pose(),
                    COUNTER_BALL,
                    (this.leftPos - 37),
                    (this.topPos - 32),
                    98,
                    98,
                    0,
                    0,
                    98,
                    98,
                    1,
                    1, 1, 1, 1,
                    false,
                    sizeMod
            );
        }

        renderDigit(guiGraphics, 0, tenstDigit); // tens place
        renderDigit(guiGraphics, 1, onesDigit); // ones place
    }

    private void renderDigit(GuiGraphics guiGraphics, int digitIndex, int digitValue){

        blitk(
                guiGraphics.pose(),
                NUMBERS,
                (this.leftPos + (digitIndex * 13)),
                (this.topPos),
                21,
                13,
                13 * digitValue,
                0,
                130,
                21,
                1,
                1, 1, 1, 1,
                true,
                sizeMod
        );
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
        this.renderBall = true;
        this.sizeMod = 0.9f;

    }

    private void correctSize() {
        //int textBoxHeight = expanded ? TEXT_BOX_HEIGHT * 2 : TEXT_BOX_HEIGHT;
        //setRectangle(TEXT_BOX_WIDTH, textBoxHeight, getAppropriateY() + 6, getAppropriateY() + 6);
        this.topPos = getAppropriateY();
        this.leftPos = getAppropriateX();
    }

    private int getAppropriateX() {
        //return minecraft.getWindow().getGuiScaledWidth() - (MOVE_WIDTH + 12) + xOffset;
        return 15;
    }

    private int getAppropriateY() {
        return 15;
    }




}
