package com.raspix.fabric.cobble_contests.menus.screens;

import com.cobblemon.mod.common.api.gui.ColourLibrary;
import com.cobblemon.mod.common.api.gui.MultiLineLabelK;
import com.mojang.blaze3d.vertex.PoseStack;
import com.raspix.common.cobble_contests.CobbleContests;
import com.raspix.fabric.cobble_contests.menus.PoffinPotMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class PoffinPotScreen extends AbstractContainerScreen<PoffinPotMenu> {
    private static final ResourceLocation POT_TEXTURE = new ResourceLocation(CobbleContests.MOD_ID, "textures/gui/poffin_pot_gui.png");

    private Inventory playerInv;
    private List<Button> buttons;

    private boolean showInstructions;

    @Override
    public void init() {
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        buttons = new ArrayList<>();
        /**this.buttons.add(this.addRenderableWidget(new ExtendedButton(this.leftPos + 5, this.topPos + 5, 12, 12, Component.literal("i"), (arg) -> {
            toggleInstructions(arg);
            //this.leftPos = updateScreenPosition(this.width, this.imageWidth);
            //arg.setPosition(this.leftPos + 20, this.height / 2 - 49);
        })));*/
    }

    public int updateScreenPosition(int j, int k) {
        int i = 0;


        return i;
    }

    private void toggleInstructions(Button arg) {
        this.showInstructions = !this.showInstructions;
        //this.leftPos = updateScreenPosition(this.width, this.imageWidth);
        /**if (this.showInstructions) {
            this.leftPos = ((this.width - this.imageWidth) / 2) + 10;
        } else {
            this.leftPos = (this.width - this.imageWidth) / 2;
        }*/
        arg.setPosition(this.leftPos + 5, this.topPos + 5);
    }

    public PoffinPotScreen(PoffinPotMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 176;
        this.imageHeight = 176;
        this.playerInv = playerInv;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(POT_TEXTURE, k, l, 0, 0, this.imageWidth, this.imageHeight);

        if (menu.isLit()) {
            int prog = this.menu.getLitProgress();
            guiGraphics.blit(POT_TEXTURE, this.leftPos + 71, this.topPos + 52 + 15 - prog, 176, 15 - prog, 33, prog + 2);
        }

        int prog = this.menu.getBurnProgress();
        guiGraphics.blit(POT_TEXTURE, this.leftPos + 113, this.topPos + 41, 176, 17, prog + 1, 16);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int xMousePos, int yMousePos, float partialTick) {
        super.render(guiGraphics, xMousePos, yMousePos, partialTick);
        renderTooltip(guiGraphics, xMousePos, yMousePos);
        drawInstructions(guiGraphics);
    }

    private void drawInstructions(GuiGraphics guiGraphics) {
        if (showInstructions) {
            /**drawScaledText(
                    guiGraphics,
                    null,
                    Component.literal(""),
                    //Component.translatable("ui.lv.number", pokemon.getLevel()),
                    this.leftPos + 0,
                    this.topPos + 0,
                    0.5f,
                    1f,
                    Integer.MAX_VALUE,
                    0x00FFFFFF,
                    true,
                    true, null, null
            );*/
            PoseStack poses = guiGraphics.pose();
            poses.pushPose();
            poses.scale(0.5f, 0.5f, 1F);
            MultiLineLabelK.Companion.create(
                    Component.translatable("cobble_contests.poffin_pot_instructions"),
                    65 / 0.5f,
                    9
            ).renderLeftAligned(
                    guiGraphics,
                    (this.leftPos + 4) / 0.5f,
                    (this.topPos + 41) / 0.5f,
                    5.5 / 0.5f,
                    ColourLibrary.WHITE,
                    true
            );
            poses.popPose();
        }
    }

    @Override
    protected void renderLabels(GuiGraphics arg, int i, int j) {
        //arg.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        //arg.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
    }
}
