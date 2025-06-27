package com.raspix.fabric.cobble_contests.menus.widgets;

import com.raspix.fabric.cobble_contests.util.data.ContestType;
import net.minecraft.client.gui.GuiGraphics;

import java.awt.*;

public class ContestTypeIcon {
    private static final int TYPE_ICON_DIAMETER = 36;
    private static final float SCALE = 0.5F;

    private static final Image typesResource = cobblemonResource("textures/gui/types.png");
    private static final Image smallTypesResource = cobblemonResource("textures/gui/types_small.png");

    private final Number x;
    private final Number y;
    private final ContestType type;
    //private final ElementalType secondaryType;
    private final boolean centeredX;
    private final boolean small;
    private final float secondaryOffset;
    private final float doubleCenteredOffset;
    private final float opacity;

    public final int textureXMultiplie;


    public ContestTypeIcon(Number x, Number y, ContestType type, boolean centeredX, boolean small, float secondaryOffset, float doubleCenteredOffset, float opacity) {
        this.x = x;
        this.y = y;
        this.type = type;

        this.textureXMultiplie = getType(type);
        this.centeredX = centeredX;
        this.small = small;
        this.secondaryOffset = secondaryOffset;
        this.doubleCenteredOffset = doubleCenteredOffset;
        this.opacity = opacity;
    }

    public ContestTypeIcon(Number x, Number y, ContestType type) {
        this(x, y, type, false, false, 15F, 7.5F, 1F);
    }

    public int getType(ContestType type){
        int ret = switch (type) {
            case ContestType.Cool -> 0;
            case ContestType.Beauty -> 1;
            case ContestType.Cute -> 2;
            case ContestType.Smart -> 3;
            case ContestType.Tough -> 4;
            default -> 0;
        };
        return ret;

    }

    public void render(GuiGraphics context) {
        int diameter = small ? (TYPE_ICON_DIAMETER / 2) : TYPE_ICON_DIAMETER;
        float offsetX = centeredX ? (((diameter / 2) * SCALE)) : 0F;

        blitk(
                context.pose(),
                small ? smallTypesResource : typesResource,
                (x.floatValue() - offsetX) / SCALE,
                y.floatValue() / SCALE,
                diameter,
                diameter,
                diameter * textureXMultiplie + 0.1,
                diameter * 18,
                opacity,
                SCALE
        );
    }

    private void blitk(Object matrixStack, Image texture, float x, float y, int height, int width, double uOffset, int textureWidth, float alpha, float scale) {
        // Implementation of blitk method
    }

    private static Image cobblemonResource(String path) {
        // Implementation of cobblemonResource method
        return null; // Placeholder
    }
}

