package com.raspix.fabric.cobble_contests.menus.screens.subscreens;

import com.cobblemon.mod.common.client.battle.ClientBattlePokemon;
import com.cobblemon.mod.common.client.gui.trade.ModelWidget;
import com.cobblemon.mod.common.pokemon.Species;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.raspix.common.cobble_contests.CobbleContests;
import com.raspix.fabric.cobble_contests.network.NetworkablePokemonData;
import com.raspix.fabric.cobble_contests.util.data.ContestType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import com.cobblemon.mod.common.client.render.models.blockbench.repository.PokemonModelRepository;

import java.util.HashMap;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;
import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.drawPosablePortrait;
import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;

public class ContestantTile {

    public static final int PANEL_WIDTH = 29;
    public static final int EXTENDED_PANEL_WIDTH = 118;
    public static final int PANEL_HEIGHT = 24;

    public final int xOffset;
    public final int yOffset;

    private final ResourceLocation moveTexture = ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/gui/contestant_panel.png");
    private final ResourceLocation moveOverlayTexture = ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/gui/contestant_panel_overlay.png");
    private final ResourceLocation contestTypeIcons = ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/gui/contest_type_icons.png");
    private final ResourceLocation hearts = ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/gui/hearts.png");

    private final ResourceLocation pokeFont = ResourceLocation.parse("uniform");

    private float x;
    private float y;

    protected final Minecraft minecraft;
    private boolean expanded = false;  // if the default, non-hovered state is expanded

    private ModelWidget modelWidget;

    private String contestantName;
    private String pokeName;
    private NetworkablePokemonData pokemon;

    private int appeal;
    private int appealChange;

    public ContestantTile(float x, float y) {
        //this.x = x;
        //this.y = y;
        this.xOffset = (int) x;
        this.yOffset = (int) y;
        this.minecraft = Minecraft.getInstance();
        correctSize(0, 0);
        setupButton("Player", "Pokemon", null);
    }

    public void setupButton(String playerName, String pokeName, NetworkablePokemonData pokemonData){
        this.contestantName = playerName;
        this.pokeName = pokeName;
        if(pokemonData != null){
            this.pokemon = pokemonData;
            this.appeal = pokemonData.getNumHearts();
            this.appealChange = pokemonData.getNumChangeHearts();
        }


    }

    /**private void setSelectedModel() {

        if(clientParty != null && clientParty.getSlots().size() > 0 && clientParty.get(pokemonIndex) != null){
            Pokemon poke = clientParty.get(pokemonIndex);
            modelWidget = new ModelWidget(
                    (int) (this.x + 6 + 15),
                    (int) (this.y + 27 -5),
                    66,
                    66,
                    poke.asRenderablePokemon(),
                    2F,
                    325F,
                    -10.0
            );
        }else {
            modelWidget = null;
        }
    }*/


    /**public MoveActionResponse getResponse() {
     return new MoveActionResponse(move.getId(), getTargetPnx());
     }*/

    public boolean isSelectable() {
        return true;
    }

    public String getPlayerName(){
        return contestantName;
    }

    public String getPokeName(){
        return pokeName;
    }

    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {

        correctSize(mouseX, mouseY);

        boolean expandedForm = (isSelectable() && isHovered(mouseX, mouseY)) || expanded;

        if(expandedForm){ // need to have two of these if statements because of ordering layers
            this.blit(context, moveTexture, (int) this.x + 10, (int) this.y, 0, 0, 108, PANEL_HEIGHT, 108, PANEL_HEIGHT, 1f, 1f, 1f, 1f);
        }

        context.blit(moveOverlayTexture, (int) this.x, (int) this.y, 0, 0, expandedForm ? EXTENDED_PANEL_WIDTH : PANEL_WIDTH, PANEL_HEIGHT, EXTENDED_PANEL_WIDTH, PANEL_HEIGHT);


        if(pokemon != null){

            renderPortrait(context, delta);

        }
        if(expandedForm){

            drawScaledText(context, pokeFont, Component.translatable("cobble_contests.contest_showoff.contestant_and_poke", contestantName, pokeName),
                    (Number) (this.x + 30),
                    (Number) (this.y + 2),
                    0.75f, 1f, 2147483647, 0xFFFFFF, false, false, null, null);

            /**drawPosablePortrait(
                 Species.getBY_IDENTIFIER_CODEC()
                 identifier = species.resourceIdentifier,
                 matrixStack = matrixStack,
                 scale = 18F * (ballState?.scale ?: 1F) * if (isCompact) 0.65F else 1.0f,
                 contextScale = species.getForm(state.currentAspects).baseScale,
                 repository = PokemonModelRepository,
                 reversed = reversed,
                 state = state,
                 partialTicks = partialTicks
             )*/

            context.blit(hearts, (int) (this.x + 35), (int) this.y + 10, 1, 1, 1 + appeal * 8, 9, 83, 33);

        }

    }

    private void renderPortrait(GuiGraphics context, float delta){

        PoseStack matrixStack = context.pose();


        float portraitStartX = this.x + 2;

        float portraitOffsetY = 2;

        int portraitDiameter = 19;

        // TODO: figure out why changing the window size makes this disappear
        // Second render the Pokémon through the scissors
        context.enableScissor(
                (int)portraitStartX,
                (int)(y + portraitOffsetY),
                (int)(portraitStartX + portraitDiameter),
                (int)(y + portraitDiameter + portraitOffsetY));
        matrixStack.pushPose();
        matrixStack.translate(
                portraitStartX + (portraitDiameter / 2.0),
                y + portraitOffsetY - 15.0,
                0.0);


        Species species = pokemon.getSpecies();
        System.out.println("Species found as " + species.getName());
        drawPosablePortrait(species.getResourceIdentifier(), matrixStack, 13F, species.getForm(pokemon.getState().getCurrentAspects()).getBaseScale(), false, pokemon.getState(), PokemonModelRepository.INSTANCE, delta,
                0F,
                0F,
                0F,
                0F,
                0F,
                1F,
                1F,
                1F,
                1F);

        matrixStack.popPose();
        context.disableScissor();
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
        return mouseX >= x && mouseX <= x + EXTENDED_PANEL_WIDTH && mouseY >= y && mouseY <= y + PANEL_HEIGHT;
    }

    private void correctSize(int mouseX, int mouseY) {
        this.y = getAppropriateY();
        this.x = getAppropriateX(mouseX, mouseY);
    }

    private int getAppropriateX(int mouseX, int mouseY) {
        return minecraft.getWindow().getGuiScaledWidth() - (expanded || isHovered(mouseX, mouseY)? EXTENDED_PANEL_WIDTH : PANEL_WIDTH);
    }

    private int getAppropriateY() {
        return minecraft.getWindow().getGuiScaledHeight() - minecraft.getWindow().getGuiScaledHeight() + 30 + yOffset;
    }

    public void setExpanded(boolean shouldExpand){
        this.expanded = shouldExpand;
    }

}


