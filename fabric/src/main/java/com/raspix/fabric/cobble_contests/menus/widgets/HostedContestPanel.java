package com.raspix.fabric.cobble_contests.menus.widgets;

import com.raspix.common.cobble_contests.CobbleContests;
import com.raspix.fabric.cobble_contests.menus.widgets.buttons.FixedImageButton;
import com.raspix.fabric.cobble_contests.util.data.ContestType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;
import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;

public class HostedContestPanel extends FixedImageButton {

    private UUID hostID;
    private String hostName;
    private ContestType type;
    private int offset;
    private int numContestants;

    private final ResourceLocation contestTypeIcons = ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/gui/contest_type_icons.png");

    public HostedContestPanel(int screenX, int screenY, int buttonWidth, int buttonHeight, int xTexStart, int yTexStart, int pressedTexYOffset, ResourceLocation resourceLocation, int textureWidth, int textureHeight, OnPress onPress) {
        super(screenX, screenY, buttonWidth, buttonHeight, xTexStart, yTexStart, pressedTexYOffset, resourceLocation, textureWidth, textureHeight, onPress);
        this.hostName = "Not Assigned";
        offset = 5; // setting it to none before start
    }

    public void setUpVisuals(String hostName, ContestType type, int numContestants){
        this.hostName = hostName;
        this.type = type;
        this.offset = type.getIntValue()<0? 5: type.getIntValue();
        this.numContestants = numContestants;
    }

    public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        super.renderWidget(guiGraphics, i, j, f);
        drawScaledText(guiGraphics, Component.literal(hostName).getVisualOrderText(),
                (Number) (this.getX() + 27),
                (Number) (this.getY() + 2),
                1.0f, 1.0f, 1f, 0x00918b99, true, false);

        drawScaledText(guiGraphics, Component.translatable("cobble_contests.contest_text.contestant_num", numContestants, "4").getVisualOrderText(),
                (Number) (this.getX() + 110),
                (Number) (this.getY() + 9),
                0.8f, 1.0f, 1f, 0x00918b99, true, false);

        blitk(
                guiGraphics.pose(),
                contestTypeIcons,
                (this.getX() - 10) * 2,
                (this.getY()) * 2,
                36,
                36,
                36 * offset,
                0,
                216, 36,
                0,
                1,
                1,
                1,
                1f,
                true,
                0.5f
        );
    }

    private void getPlayerSkull(){


    }


}
