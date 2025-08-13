package com.raspix.fabric.cobble_contests.menus.screens.subscreens;

import com.cobblemon.mod.common.CobblemonSounds;
import com.cobblemon.mod.common.api.gui.ColourLibrary;
//import com.cobblemon.mod.common.api.gui.MultiLineLabelK;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.raspix.common.cobble_contests.CobbleContests;
import com.raspix.fabric.cobble_contests.menus.widgets.MultiLineLabel;
import com.raspix.fabric.cobble_contests.util.data.ContestType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

import java.util.HashMap;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;
import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.drawString;
import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;
import static com.cobblemon.mod.common.util.LocalizationUtilsKt.lang;

public class MoveTile {

    public static final int MOVE_WIDTH = 92;
    public static final int MOVE_HEIGHT = 24;
    public static final float MOVE_VERTICAL_SPACING = 5F;
    public static final float MOVE_HORIZONTAL_SPACING = 13F;

    public final int xOffset;
    public final int yOffset;

    private final ResourceLocation moveTexture = ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/gui/contest_move.png");
    private final ResourceLocation moveOverlayTexture = ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/gui/contest_move_overlay.png");
    private final ResourceLocation moreInfoIcon = ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/gui/more_info_icon.png");
    private final ResourceLocation contestTypeIcons = ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/gui/contest_type_icons.png");
    private final ResourceLocation hearts = ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/gui/hearts.png");

    private final ResourceLocation pokeFont = ResourceLocation.parse("uniform");

    private float x;
    private float y;

    protected final Minecraft minecraft;

    public static final HashMap<ContestType, Integer[]> typeHues = new HashMap<>(){{
        put(ContestType.Cool, new Integer[]{0xE55C32, 0});
        put(ContestType.Beauty, new Integer[]{0x5bc1f5, 1});//0x5bc1f5
        put(ContestType.Cute, new Integer[]{0xed6dc5, 2});
        put(ContestType.Smart, new Integer[]{0x66c831, 3});
        put(ContestType.Tough, new Integer[]{0xf0dd30, 4});
        put(ContestType.None, new Integer[]{0xFFFFFF, 5});

    }};

    private ContestType type;
    private int hue;
    private int offset;
    private String name;
    private String description;
    private int appeal;
    private int jam;

    public MoveTile(float x, float y) {
        //this.x = x;
        //this.y = y;
        this.xOffset = (int) x;
        this.yOffset = (int) y;
        this.minecraft = Minecraft.getInstance();
        correctSize();
        setupButton(ContestType.Beauty, "Placeholder", "Placeholder desc", 2, 3);
    }

    public void setupButton(ContestType type, String name, String description, int appeal, int jam){
        this.type = type;
        this.hue = typeHues.get(type)[0];
        this.offset = typeHues.get(type)[1];
        this.name = name;
        this.description = description;
        this.appeal = appeal;
        this.jam = jam;

    }

    public String getData(){
        return name + " at " + x + " and " + y;
    }


    /**public MoveActionResponse getResponse() {
        return new MoveActionResponse(move.getId(), getTargetPnx());
    }*/

    public boolean isSelectable() {
        return true;
    }

    public String getName(){
        return name;
    }

    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {

        correctSize();

        this.blit(context, moveTexture, (int) this.x, (int) this.y, 0, isSelectable() && isHovered(mouseX, mouseY) ? MOVE_HEIGHT : 0, MOVE_WIDTH, MOVE_HEIGHT, 92, 48, ((hue & 0xFF0000) >> 16)/255f, ((hue & 0xFF00) >> 8)/255f, (hue & 0xFF)/255f, 1f);
        context.blit(moveOverlayTexture, (int) this.x, (int) this.y, 0, 0, MOVE_WIDTH, MOVE_HEIGHT, 92, 24);
        this.blit(context, moreInfoIcon, (int) this.x + 80, (int) this.y, 0, 0, 10, 11, 10, 11, ((hue & 0xFF0000) >> 16)/255f, ((hue & 0xFF00) >> 8)/255f, (hue & 0xFF)/255f, 1f);
        //context.blit(moreInfoIcon, (int) this.x + 80, (int) this.y, 0, 0, 10, 11, 92, 24);


        /**drawScaledText(context, lang("move." + name).getVisualOrderText(),
                (Number) (this.x + 15),
                (Number) (this.y + 4),
                0.8f, 0.8f, 1f, 0xffffff, false, false);*/

        drawScaledText(context, pokeFont, lang("move." + name),
                (Number) (this.x + 15),
                (Number) (this.y + 4),
                1f, 1f, 2147483647, 0xFFFFFF, false, false, null, null);

        context.blit(hearts, (int) (this.x + 90 - (1 + appeal * 8)), (int) this.y + 15, 1, 1, 1 + appeal * 8, 9, 83, 44);
        context.blit(hearts, (int) this.x + 16, (int) this.y + 15, 1, 23, jam == 0? 0: 1 + jam * 8, 9, 83, 44);

        blitk(
                context.pose(),
                contestTypeIcons,
                (this.x - 10) * 2,
                (this.y + 2) * 2,
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

        if(isInfoHovered(mouseX, mouseY)){
            //context.blit(moveTexture, mouseX, mouseY, 50, 50, 116, 49, 948, 600);
            context.pose().pushPose();
            context.pose().scale(1, 1, 1F);
            context.pose().translate(0, 0, 1);
            blitk(
                    context.pose(),
                    moveTexture,
                    mouseX,
                    mouseY,
                    50,
                    100,
                    116,
                    49,
                    948, 600,
                    0,
                    1,
                    1,
                    1,
                    1f,
                    true,
                    1f
            );

            Component component = Component.translatable(description).setStyle(Component.translatable("").copy().getStyle().withFont(pokeFont));


            /**drawScaledText(context, pokeFont, component.copy(),
                    mouseX + 5,
                    mouseY + 5,
                    1f, 1f, 500, 0xFFFFFF, false, true, null, null);*/
            /**drawString(
                    context,
                    component.getString(),
                    mouseX + 5,
                    mouseY + 5,
                    ColourLibrary.WHITE,
                    true,
                    pokeFont
            );*/

            MultiLineLabel.create(component,
                    47 / 0.5f,
                    5,
                    pokeFont
            ).renderLeftAligned(
                    context,
                    mouseX + 3,
                    mouseY + 1,
                    0,
                    8,
                    ColourLibrary.WHITE,
                    1f,
                    true
            );

            context.pose().popPose();

        }


    }



    public void blit(GuiGraphics context, ResourceLocation resourceLocation, int i, int j, float f, float g, int k, int l, int m, int n, float red, float green, float blue, float alpha) {
        this.blit(context, resourceLocation, i, j, k, l, f, g, k, l, m, n, red, green, blue, alpha);
    }

    public void blit(GuiGraphics context, ResourceLocation resourceLocation, int i, int j, int k, int l, float f, float g, int m, int n, int o, int p, float red, float green, float blue, float alpha) {
        this.blit(context, resourceLocation, i, i + k, j, j + l, 0, m, n, f, g, o, p, red, green, blue, alpha);
    }

    void blit(GuiGraphics context, ResourceLocation resourceLocation, int i, int j, int k, int l, int m, int n, int o, float f, float g, int p, int q, float red, float green, float blue, float alpha) {
        this.innerBlit(context, resourceLocation, i, j, k, l, m, (f + 0.0F) / (float)p, (f + (float)n) / (float)p, (g + 0.0F) / (float)q, (g + (float)o) / (float)q, red, green, blue, alpha);
    }

    void innerBlit(GuiGraphics context, ResourceLocation resourceLocation, int i, int j, int k, int l, int m, float f, float g, float h, float n, float red, float green, float blue, float alpha) {
        RenderSystem.setShaderTexture(0, resourceLocation);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.enableBlend();
        PoseStack pose = context.pose();
        Matrix4f matrix4f = pose.last().pose();
        BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferBuilder.addVertex(matrix4f, (float)i, (float)k, (float)m).setUv(f, h).setColor(red, green, blue, alpha);
        bufferBuilder.addVertex(matrix4f, (float)i, (float)l, (float)m).setUv(f, n).setColor(red, green, blue, alpha);
        bufferBuilder.addVertex(matrix4f, (float)j, (float)l, (float)m).setUv(g, n).setColor(red, green, blue, alpha);
        bufferBuilder.addVertex(matrix4f, (float)j, (float)k, (float)m).setUv(g, h).setColor(red, green, blue, alpha);
        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
    }

    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + MOVE_WIDTH && mouseY >= y && mouseY <= y + MOVE_HEIGHT;
    }

    public boolean isInfoHovered(double mouseX, double mouseY) {
        return mouseX >= x + 80 && mouseX <= x + MOVE_WIDTH && mouseY >= y && mouseY <= y + ((double) MOVE_HEIGHT / 2);
    }

    public void onClick() {
        if (!isSelectable()) return;
        playDownSound(Minecraft.getInstance().getSoundManager());
        //moveSelection.getBattleGUI().selectAction(moveSelection.getRequest(), getResponse());
    }

    public void playDownSound(SoundManager soundManager) {
        soundManager.play(SimpleSoundInstance.forUI(CobblemonSounds.GUI_CLICK, 1.0F));
    }

    private void correctSize() {
        //int textBoxHeight = expanded ? TEXT_BOX_HEIGHT * 2 : TEXT_BOX_HEIGHT;
        //setRectangle(TEXT_BOX_WIDTH, textBoxHeight, getAppropriateY() + 6, getAppropriateY() + 6);
        this.y = getAppropriateY();
        this.x = getAppropriateX();
    }

    private int getAppropriateX() {
        //return minecraft.getWindow().getGuiScaledWidth() - (MOVE_WIDTH + 12) + xOffset;
        return minecraft.getWindow().getGuiScaledWidth() - minecraft.getWindow().getGuiScaledWidth() + 22 + xOffset;
    }

    private int getAppropriateY() {
        return minecraft.getWindow().getGuiScaledHeight() - 55 - yOffset;
    }


}


