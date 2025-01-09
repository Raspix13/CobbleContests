package com.raspix.fabric.cobble_contests.menus.widgets;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;


@Environment(EnvType.CLIENT)
public class FixedImageButton extends Button {

    protected final ResourceLocation spriteLocation;
    int xTexStart;
    int yTexStart;
    int pressedTexYOffset;
    int textureWidth;
    int textureHeight;

    protected static final net.minecraft.client.gui.components.Button.CreateNarration DEFAULT_NARRATION = (supplier) -> {
        return (MutableComponent)supplier.get();
    };

    public FixedImageButton(int screenX, int screenY, int buttonWidth, int buttonHeight, int xTexStart, int yTexStart, ResourceLocation resourceLocation, net.minecraft.client.gui.components.Button.OnPress onPress) {
        this(screenX, screenY, buttonWidth, buttonHeight, xTexStart, yTexStart, buttonHeight, resourceLocation, 256, 256, onPress);
    }

    public FixedImageButton(int screenX, int screenY, int buttonWidth, int buttonHeight, int xTexStart, int yTexStart, int pressedTexYOffset, ResourceLocation resourceLocation,  net.minecraft.client.gui.components.Button.OnPress onPress) {
        this(screenX, screenY, buttonWidth, buttonHeight, xTexStart, yTexStart, pressedTexYOffset, resourceLocation, 256, 256, onPress);
    }

    public FixedImageButton(int screenX, int screenY, int buttonWidth, int buttonHeight, int xTexStart, int yTexStart, int pressedTexYOffset, ResourceLocation resourceLocation, int textureWidth, int textureHeight,  net.minecraft.client.gui.components.Button.OnPress onPress) {
        this(screenX, screenY, buttonWidth, buttonHeight, xTexStart, yTexStart, pressedTexYOffset, resourceLocation, textureWidth, textureHeight, onPress, CommonComponents.EMPTY);
    }

    public FixedImageButton(int screenX, int screenY, int buttonWidth, int buttonHeight, int xTexStart, int yTexStart, int pressedTexYOffset, ResourceLocation resourceLocation, int textureWidth, int textureHeight,  net.minecraft.client.gui.components.Button.OnPress onPress, Component component) {
        super(screenX, screenY, buttonWidth, buttonHeight, component, onPress, DEFAULT_NARRATION);
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.xTexStart = xTexStart;
        this.yTexStart = yTexStart;
        this.pressedTexYOffset = pressedTexYOffset;
        this.spriteLocation = resourceLocation;
    }

    public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        int yOff = 0;
        if(this.isHoveredOrFocused()){
            yOff = this.pressedTexYOffset;
        }
        guiGraphics.blit(this.spriteLocation, this.getX(), this.getY(), this.xTexStart, this.yTexStart + yOff, this.getWidth(), this.getHeight(), this.textureWidth, this.textureHeight);
    }
}
