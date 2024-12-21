package com.raspix.fabric.cobble_contests.menus.widgets;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

@Environment(EnvType.CLIENT)
public class FixedImageButton extends Button {
    protected static final net.minecraft.client.gui.components.Button.CreateNarration DEFAULT_NARRATION = (supplier) -> {
        return (MutableComponent)supplier.get();
    };

    protected final WidgetSprites sprites;

    public FixedImageButton(int i, int j, int k, int l, WidgetSprites widgetSprites, net.minecraft.client.gui.components.Button.OnPress onPress) {
        this(i, j, k, l, widgetSprites, onPress, CommonComponents.EMPTY);
    }

    public FixedImageButton(int i, int j, int k, int l, WidgetSprites widgetSprites, net.minecraft.client.gui.components.Button.OnPress onPress, net.minecraft.network.chat.Component component) {
        //super(i, j, k, l, component, onPress, DEFAULT_NARRATION);
        this.sprites = widgetSprites;
    }

    public FixedImageButton(int i, int j, WidgetSprites widgetSprites, net.minecraft.client.gui.components.Button.OnPress onPress, Component component) {
        this(0, 0, i, j, widgetSprites, onPress, component);
    }

    public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        ResourceLocation resourceLocation = this.sprites.get(this.isEnabled(), this.isFocusOwner());//isHoveredOrFocused());
        guiGraphics.blitSprite(resourceLocation, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
}
