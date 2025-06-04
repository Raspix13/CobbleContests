package com.raspix.fabric.cobble_contests.menus.widgets;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractScrollWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.UUID;

import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;

public class HostedContestPanel extends FixedImageButton {

    private UUID hostID;
    private String hostName;


    public HostedContestPanel(int screenX, int screenY, int buttonWidth, int buttonHeight, int xTexStart, int yTexStart, int pressedTexYOffset, ResourceLocation resourceLocation, int textureWidth, int textureHeight, OnPress onPress) {
        super(screenX, screenY, buttonWidth, buttonHeight, xTexStart, yTexStart, pressedTexYOffset, resourceLocation, textureWidth, textureHeight, onPress);
        this.hostName = "Not Assigned";
    }

    public void setUpVisuals(String hostName){
        this.hostName = hostName;
    }

    public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        super.renderWidget(guiGraphics, i, j, f);
        drawScaledText(guiGraphics, Component.literal(hostName).getVisualOrderText(),
                (Number) (this.getX() + 20),
                (Number) (this.getY()),
                1.0f, 1.0f, 1f, 0x00918b99, true, false);
    }

    private void getPlayerSkull(){


    }


}
