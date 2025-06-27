package com.raspix.fabric.cobble_contests.menus.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.stream.Collectors;

import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;

public class MultiLineLabel {
    private final List<TextWithWidth> comps;
    private final ResourceLocation font;

    private static final Font mcFont = Minecraft.getInstance().font;

    public MultiLineLabel(List<TextWithWidth> comps, ResourceLocation font) {
        this.comps = comps;
        this.font = font;
    }

    public static MultiLineLabel create(Component component, Number width, Number maxLines) {
        return create(component, width, maxLines, null);
    }

    public static MultiLineLabel create(Component component, Number width, Number maxLines, ResourceLocation font) {
        List<TextWithWidth> textWithWidths = mcFont.getSplitter().splitLines(component, width.intValue(), Style.EMPTY).stream()
                .limit(maxLines.longValue())
                .map(it -> new TextWithWidth(it, mcFont.width(it)))
                .collect(Collectors.toList());
        return new MultiLineLabel(textWithWidths, font);
    }

    public void renderLeftAligned(GuiGraphics context, Number x, Number y, Number YStartOffset, Number ySpacing, int colour, float scale, boolean shadow) {
        context.pose().pushPose();
        context.pose().scale(scale, scale, 1F);
        for (int index = 0; index < comps.size(); index++) {
            TextWithWidth textWithWidth = comps.get(index);
            float yOffset = (index == 0) ? YStartOffset.floatValue() : 0;
            drawScaledText(context, font, Component.literal(textWithWidth.text.getString()),
                    x.floatValue() / scale, (y.floatValue() + yOffset + ySpacing.floatValue() * index) / scale,
                    scale, 1f, 500, colour, false, shadow, null, null);

            //drawString(context, x.floatValue() / scale, (y.floatValue() + yOffset + ySpacing.floatValue() * index) / scale, colour, shadow, textWithWidth.text.getString(), font);
        }
        context.pose().popPose();
    }

    public static class TextWithWidth {
        public final FormattedText text;
        public final int width;

        public TextWithWidth(FormattedText text, int width) {
            this.text = text;
            this.width = width;
        }
    }

    private void drawString(GuiGraphics context, float x, float y, int colour, boolean shadow, String text, ResourceLocation font) {
        // Implementation of drawString method goes here
    }
}

