package com.raspix.fabric.cobble_contests.menus.widgets;

import com.cobblemon.mod.common.client.battle.ClientBattleMessageQueue;
import com.cobblemon.mod.common.client.gui.CobblemonRenderable;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;
import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;
import static com.cobblemon.mod.common.util.MiscUtilsKt.cobblemonResource;

public class ContestMessagePaneFail extends ObjectSelectionList<ContestMessagePaneFail.ContestMessageLine> implements CobblemonRenderable {

    private static final int LINE_HEIGHT = 10;
    private static final int LINE_WIDTH = 146;
    private static final int FRAME_WIDTH = 169;
    private static final int FRAME_HEIGHT = 55;
    private static final int FRAME_EXPANDED_HEIGHT = 101;
    private static final int TEXT_BOX_WIDTH = 153;
    private static final int TEXT_BOX_HEIGHT = 46;
    private static final int EXPAND_TOGGLE_SIZE = 5;

    private static final ResourceLocation BATTLE_MESSAGE_PANE_FRAME_RESOURCE = cobblemonResource("textures/gui/battle/battle_log.png");
    private static final ResourceLocation BATTLE_MESSAGE_PANE_FRAME_EXPANDED_RESOURCE = cobblemonResource("textures/gui/battle/battle_log_expanded.png");
    private static final ResourceLocation BATTLE_MESSAGE_HIGHLIGHT = cobblemonResource("textures/gui/battle/battle_log_row_selected_color.png");
    private static boolean expanded = false;

    private float opacity = 1F;
    private boolean scrolling = false;
    private final ClientBattleMessageQueue messageQueue;


    private int appropriateY;
    private int width;
    private int height;
    private int x;
    private int y;
    private int scrollbarPosition;
    private int bottom;
    private double scrollAmount;
    private int maxScroll;
    private boolean isDragging;
    private boolean focused;

    public ContestMessagePaneFail(ClientBattleMessageQueue messageQueue) {
        super(Minecraft.getInstance(), FRAME_WIDTH, FRAME_HEIGHT, 1, LINE_HEIGHT);
        this.messageQueue = messageQueue;
        correctSize();

        /**messageQueue.subscribe(message -> {
            boolean fullyScrolledDown = maxScroll - (int) getScrollAmount() < 10;
            addEntry(new ContestMessageLine(this, message));
            if (fullyScrolledDown) {
                setScrollAmount(maxScroll);
            }
            return null;
        });*/
    }

    private void correctSize() {
        int textBoxHeight = expanded ? TEXT_BOX_HEIGHT * 2 : TEXT_BOX_HEIGHT;
        setRectangle(TEXT_BOX_WIDTH, textBoxHeight, getAppropriateY() + 6, getAppropriateY() + 6);
        this.x = getAppropriateX();
    }

    public int getAppropriateX() {
        return Minecraft.getInstance().getWindow().getGuiScaledWidth() - (FRAME_WIDTH + 12);
    }

    public int getAppropriateY() {
        return Minecraft.getInstance().getWindow().getGuiScaledHeight() - (30 + (expanded ? FRAME_EXPANDED_HEIGHT : FRAME_HEIGHT));
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
        blitk(
                guiGraphics.pose(),
                BATTLE_MESSAGE_HIGHLIGHT,
                x + 6,
                top - 2,
                10,
                1,
                opacity
        );

        blitk(
                guiGraphics.pose(),
                BATTLE_MESSAGE_HIGHLIGHT,
                x + 6,
                top - 2,
                1,
                LINE_WIDTH,
                opacity
        );

        blitk(
                guiGraphics.pose(),
                BATTLE_MESSAGE_HIGHLIGHT,
                x + 6,
                top + 7,
                1,
                LINE_WIDTH,
                opacity
        );

        blitk(
                guiGraphics.pose(),
                BATTLE_MESSAGE_HIGHLIGHT,
                x + 6 + LINE_WIDTH - 1,
                top - 2,
                10,
                1,
                opacity
        );
    }

    @Override
    public int getRowWidth() {
        return LINE_WIDTH;
    }

    @Override
    public int getScrollbarPosition() {
        return this.x + 154;
    }

    /**@Override
    public void renderWidget(GuiGraphics context, int mouseX, int mouseY, float partialTicks) {
        correctSize();
        context.blit(
                context.pose(),
                expanded ? BATTLE_MESSAGE_PANE_FRAME_EXPANDED_RESOURCE : BATTLE_MESSAGE_PANE_FRAME_RESOURCE,
                this.x,
                getAppropriateY(),
                expanded ? FRAME_EXPANDED_HEIGHT : FRAME_HEIGHT,
                FRAME_WIDTH,
                opacity
        );

        int textBoxHeight = expanded ? TEXT_BOX_HEIGHT * 2 : TEXT_BOX_HEIGHT;
        context.enableScissor(
                this.x + 5,
                getAppropriateY(),
                TEXT_BOX_WIDTH,
                textBoxHeight
        );
    }*/

    @Override
    public void renderWidget(GuiGraphics context, int mouseX, int mouseY, float partialTicks) {
        correctSize();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, expanded ? BATTLE_MESSAGE_PANE_FRAME_EXPANDED_RESOURCE : BATTLE_MESSAGE_PANE_FRAME_RESOURCE);
        blitk(
                context.pose(),
                expanded ? BATTLE_MESSAGE_PANE_FRAME_EXPANDED_RESOURCE : BATTLE_MESSAGE_PANE_FRAME_RESOURCE,
                this.x,
                appropriateY,
                0,
                0,
                FRAME_WIDTH,
                expanded ? FRAME_EXPANDED_HEIGHT : FRAME_HEIGHT,
                opacity
        );

        int textBoxHeight = expanded ? TEXT_BOX_HEIGHT * 2 : TEXT_BOX_HEIGHT;
        RenderSystem.enableScissor(
                this.x + 5,
                appropriateY + 6,
                this.x + 5 + width,
                appropriateY + 6 + textBoxHeight
        );
        super.renderWidget(context, mouseX, mouseY, partialTicks);
        RenderSystem.disableScissor();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int toggleOffsetY = expanded ? 92 : 46;
        if (mouseX > (this.x + 160) && mouseX < (this.x + 160 + EXPAND_TOGGLE_SIZE) && mouseY > (appropriateY + toggleOffsetY) && mouseY < (appropriateY + toggleOffsetY + EXPAND_TOGGLE_SIZE)) {
            expanded = !expanded;
        }

        updateScrollingState(mouseX, mouseY);
        if (scrolling) {
            focused = getEntryAtPosition1(mouseX, mouseY);
            isDragging = true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Nullable
    protected final boolean getEntryAtPosition1(double d, double e) {
        int i = this.getRowWidth() / 2;
        int j = this.getX() + this.width / 2;
        int k = j - i;
        int l = j + i;
        int m = Mth.floor(e - (double)this.getY()) - this.headerHeight + (int)this.getScrollAmount() - 4;
        int n = m / this.itemHeight;
        //return d >= (double)k && d <= (double)l && n >= 0 && m >= 0 && n < this.getItemCount() ? (AbstractSelectionList.Entry)this.children().get(n) : null;
        return d >= (double)k && d <= (double)l && n >= 0 && m >= 0 && n < this.getItemCount() ? true : false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (scrolling) {
            if (mouseY < this.y) {
                scrollAmount = 0.0;
            } else if (mouseY > bottom) {
                scrollAmount = maxScroll;
            } else {
                scrollAmount += deltaY;
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    private void updateScrollingState(double mouseX, double mouseY) {
        scrolling = mouseX >= this.scrollbarPosition
                && mouseX < (this.scrollbarPosition + 3)
                && mouseY >= this.y
                && mouseY < bottom;
    }

    public static class ContestMessageLine extends Entry<ContestMessageLine> {
        private final ContestMessagePaneFail pane;
        private final FormattedCharSequence line;

        public ContestMessageLine(ContestMessagePaneFail pane, FormattedCharSequence line) {
            this.pane = pane;
            this.line = line;
        }

        @Override
        public void render(GuiGraphics context, int index, int rowTop, int rowLeft, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean isHovered, float partialTicks) {
            drawScaledText(
                    context,
                    line,
                    rowLeft,
                    rowTop - 2,
                    1, 1,
                    pane.opacity, 1, true, false
            );
        }

        @Override
        public Component getNarration() {
            return null;
        }

        /**@Override
        public NarrationElementOutput getNarration() {
            return NarrationElementOutput.EMPTY;
        }*/
    }

}
