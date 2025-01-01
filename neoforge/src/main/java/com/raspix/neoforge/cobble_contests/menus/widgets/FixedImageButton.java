package com.raspix.neoforge.cobble_contests.menus.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FixedImageButton extends Button {

    protected final ResourceLocation spriteLocation;
    int xTexStart;
    int yTexStart;
    int yDiffTex;
    int textureWidth;
    int textureHeight;

    protected static final CreateNarration DEFAULT_NARRATION = (supplier) -> {
        return (MutableComponent)supplier.get();
    };

    public FixedImageButton(int i, int j, int k, int l, int m, int n, ResourceLocation resourceLocation, OnPress onPress) {
        this(i, j, k, l, m, n, l, resourceLocation, 256, 256, onPress);
    }

    public FixedImageButton(int i, int j, int k, int l, int m, int n, int o, ResourceLocation resourceLocation, OnPress onPress) {
        this(i, j, k, l, m, n, o, resourceLocation, 256, 256, onPress);
    }

    public FixedImageButton(int i, int j, int k, int l, int m, int n, int o, ResourceLocation resourceLocation, int p, int q, OnPress onPress) {
        this(i, j, k, l, m, n, o, resourceLocation, p, q, onPress, CommonComponents.EMPTY);
    }

    public FixedImageButton(int i, int j, int k, int l, int m, int n, int o, ResourceLocation resourceLocation, int p, int q, OnPress onPress, Component component) {
        super(i, j, k, l, component, onPress, DEFAULT_NARRATION);
        this.textureWidth = p;
        this.textureHeight = q;
        this.xTexStart = m;
        this.yTexStart = n;
        this.yDiffTex = o;
        this.spriteLocation = resourceLocation;
    }

    public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        int yOff = 0;
        if(this.isHoveredOrFocused()){
            yOff = this.yDiffTex;
        }
        guiGraphics.blit(this.spriteLocation, this.getX(), this.getY(), this.xTexStart, this.yTexStart + yOff, this.getWidth(), this.getHeight(), this.textureWidth, this.textureHeight);
    }
}