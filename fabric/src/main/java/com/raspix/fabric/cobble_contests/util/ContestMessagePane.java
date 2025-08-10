package com.raspix.fabric.cobble_contests.util;

import com.cobblemon.mod.common.client.battle.ClientBattleMessageQueue;
import com.cobblemon.mod.common.client.gui.CobblemonRenderable;
import com.cobblemon.mod.common.client.gui.battle.widgets.BattleMessagePane;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;
import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;
import static com.cobblemon.mod.common.util.LocalizationUtilsKt.lang;
import static com.cobblemon.mod.common.util.MiscUtilsKt.cobblemonResource;


public class ContestMessagePane extends ObjectSelectionList<ContestMessagePane.ContestMessageLine> implements CobblemonRenderable {
    private static final int LINE_HEIGHT = 10;
    public static final int LINE_WIDTH = 146;
    private static final int FRAME_WIDTH = 169;
    private static final int FRAME_HEIGHT = 55;
    private static final int FRAME_EXPANDED_HEIGHT = 101;
    private static final int TEXT_BOX_WIDTH = 153;
    private static final int TEXT_BOX_HEIGHT = 46;
    private static final int EXPAND_TOGGLE_SIZE = 5;
    private static final ResourceLocation BATTLE_MESSAGE_PANE_FRAME_RESOURCE = cobblemonResource("textures/gui/battle/battle_log.png");
    private static final ResourceLocation BATTLE_MESSAGE_PANE_FRAME_EXPANDED_RESOURCE = cobblemonResource("textures/gui/battle/battle_log_expanded.png");
    private static final ResourceLocation BATTLE_MESSAGE_HIGHLIGHT = cobblemonResource("textures/gui/battle/battle_log_row_selected_color.png");

    private float opacity = 1.0f;
    private boolean scrolling = false;
    private static boolean expanded = false;
    private int hue = 0xbb94d4;//876b99;

    public ContestMessagePane(ClientBattleMessageQueue messageQueue) {
        super(Minecraft.getInstance(), FRAME_WIDTH, FRAME_HEIGHT, 1, LINE_HEIGHT);
        correctSize();
        messageQueue.subscribe(it -> {
            boolean fullyScrolledDown = getMaxScroll() - getScrollAmount() < 10;
            addEntry(new ContestMessageLine(this, it));
            if (fullyScrolledDown) {
                setScrollAmount(getMaxScroll());
            }
            return null;
        });
    }

    private void correctSize() {
        int textBoxHeight = expanded ? TEXT_BOX_HEIGHT * 2 : TEXT_BOX_HEIGHT;
        setRectangle(TEXT_BOX_WIDTH, textBoxHeight, getAppropriateY() + 6, getAppropriateY() + 6);
        setX(getAppropriateX());
    }

    @Override
    public int addEntry(ContestMessageLine entry) {
        return super.addEntry(entry);
    }

    @Override
    public int getRowLeft() {
        return super.getRowLeft() + 4;
    }

    @Override
    public void renderSelection(GuiGraphics guiGraphics, int top, int width, int height, int outerColor, int innerColor) {
        //blitk(guiGraphics.pose(), BATTLE_MESSAGE_HIGHLIGHT, getX() + 6, top - 2, 1, 10, opacity);
        //blitk(guiGraphics.pose(), BATTLE_MESSAGE_HIGHLIGHT, getX() + 6, top - 2, LINE_WIDTH, 1, opacity);
        //blitk(guiGraphics.pose(), BATTLE_MESSAGE_HIGHLIGHT, getX() + 6, top + 7, LINE_WIDTH, 1, opacity);
        //blitk(guiGraphics.pose(), BATTLE_MESSAGE_HIGHLIGHT, getX() + 6 + LINE_WIDTH - 1, top - 2, 1, 10, opacity);
    }

    @Override
    public int getRowWidth() {
        return LINE_WIDTH;
    }

    @Override
    public int getScrollbarPosition() {
        return this.getX() + 154;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        correctSize();
        blitk(guiGraphics.pose(),
                expanded ? BATTLE_MESSAGE_PANE_FRAME_EXPANDED_RESOURCE : BATTLE_MESSAGE_PANE_FRAME_RESOURCE,
                this.getX(),
                getAppropriateY(),
                expanded ? FRAME_EXPANDED_HEIGHT : FRAME_HEIGHT,
                FRAME_WIDTH,
                0, 0,
                FRAME_WIDTH,
                expanded ? FRAME_EXPANDED_HEIGHT : FRAME_HEIGHT,
                1,
                ((hue & 0xFF0000) >> 16)/255f, ((hue & 0xFF00) >> 8)/255f, (hue & 0xFF)/255f,
                opacity);
        int textBoxHeight = expanded ? TEXT_BOX_HEIGHT * 2 : TEXT_BOX_HEIGHT;
        guiGraphics.enableScissor(this.getX() + 5, getAppropriateY() + 6, this.getX() + 5 + width, getAppropriateY() + 6 + textBoxHeight);
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTicks);
        guiGraphics.disableScissor();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int toggleOffsetY = expanded ? 92 : 46;
        if (mouseX > this.getX() + 160 && mouseX < this.getX() + 160 + EXPAND_TOGGLE_SIZE && mouseY > getAppropriateY() + toggleOffsetY && mouseY < getAppropriateY() + toggleOffsetY + EXPAND_TOGGLE_SIZE) {
            expanded = !expanded;
        }
        updateScrollingState(mouseX, mouseY);
        if (scrolling) {
            setFocused(getEntryAtPosition(mouseX, mouseY));
            setDragging(true);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (scrolling) {
            if (mouseY < this.getY()) {
                setScrollAmount(0.0);
            } else if (mouseY > getBottom()) {
                setScrollAmount(getMaxScroll());
            } else {
                setScrollAmount(getScrollAmount() + deltaY);
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    private void updateScrollingState(double mouseX, double mouseY) {
        scrolling = mouseX >= this.getScrollbarPosition() && mouseX < this.getScrollbarPosition() + 3 && mouseY >= this.getY() && mouseY < getBottom();
    }

    private int getAppropriateX() {
        return minecraft.getWindow().getGuiScaledWidth() - (FRAME_WIDTH + 12);
    }

    private int getAppropriateY() {
        return minecraft.getWindow().getGuiScaledHeight() - (30 + (expanded ? FRAME_EXPANDED_HEIGHT : FRAME_HEIGHT));
    }

    public static class ContestMessageLine extends Entry<ContestMessageLine> {
        private final ContestMessagePane pane;
        private final FormattedCharSequence line;

        public ContestMessageLine(ContestMessagePane pane, FormattedCharSequence line) {
            this.pane = pane;
            this.line = line;
        }

        @Override
        public Component getNarration() {
            return Component.empty();
        }

        @Override
        public void render(GuiGraphics context, int index, int rowTop, int rowLeft, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean isHovered, float partialTicks) {
            drawScaledText(context, line, rowLeft, rowTop - 2, 1f, 1f, pane.opacity, 0x00FFFFFF + ((int)((float)pane.opacity * 255) << 24), false, false);
            //Style style = MutableComponent.getStyle().withFont(ChatFormatting.FONT);
            /**drawScaledText(context, ResourceLocation.parse("uniform"), line,
                    rowLeft,
                    rowTop - 2,
                    1f, pane.opacity, 2147483647, 0xFFFFFF + ((int)((float)pane.opacity * 255) << 24), false, false, null, null);*/
            //drawScaledText(context, line, rowLeft, rowTop - 2, pane.opacity);
        }
    }
}