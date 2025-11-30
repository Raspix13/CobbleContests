package com.raspix.fabric.cobble_contests.menus.widgets;

import com.cobblemon.mod.common.client.gui.trade.ModelWidget;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.RenderablePokemon;
import com.raspix.common.cobble_contests.CobbleContests;
import com.raspix.fabric.cobble_contests.network.NetworkablePokemonData;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;
import static com.cobblemon.mod.common.util.LocalizationUtilsKt.lang;

public class ResultsScreen extends AbstractWidget {

    private ModelWidget firstModelWidget;
    private ModelWidget secondModelWidget;
    private ModelWidget thirdModelWidget;
    String yourRank;
    boolean didWin = true;

    private static final ResourceLocation CONTEST_PANEL_TEXTURE = ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/gui/intro_editor_screen.png");
    private final ResourceLocation pokeFont = ResourceLocation.parse("uniform");


    public ResultsScreen(int i, int j, int k, int l, Component component) {
        super(i, j, k, l, component);
    }

    public void UpdateRankedInfo(boolean didWin, NetworkablePokemonData firstRank){
        this.didWin = didWin;
        setFirstRankedModel(firstRank);
    }

    public void UpdateInfo(NetworkablePokemonData firstRank){
        setFirstRankedModel(firstRank);
    }

    public void UpdateInfo(CompoundTag tag){

        if(tag.contains("first_rank")){
            NetworkablePokemonData firstRank = new NetworkablePokemonData(tag.getCompound("first_rank"));
            setFirstRankedModel(firstRank);
        }

        if(tag.contains("second_rank")){
            NetworkablePokemonData secondRank = new NetworkablePokemonData(tag.getCompound("second_rank"));
            setSecondRankedModel(secondRank);
        }

        if(tag.contains("third_rank")){
            NetworkablePokemonData thirdRank = new NetworkablePokemonData(tag.getCompound("third_rank"));
            setThirdRankedModel(thirdRank);
        }


        this.yourRank = tag.getString("your_rank");
    }

    @Override
    protected void renderWidget(GuiGraphics context, int xMousePos, int yMousePos, float partialTick) {
        String resultText = didWin? "Winner": "Loss";
        drawScaledText(context, pokeFont, Component.translatable(resultText),
                (Number) (getX() + 146),
                (Number) (getY() + 4),
                1f, 1f, 2147483647, 0xFFFFFF, true, false, null, null);
        if(firstModelWidget != null) {
            context.blit(CONTEST_PANEL_TEXTURE, this.getX() + 118, this.getY() + 14, 7, 466, 55, 100, 948, 600);
            firstModelWidget.visible = true;
            firstModelWidget.render(context, xMousePos, yMousePos, partialTick);

        }
        if(secondModelWidget != null) {
            context.blit(CONTEST_PANEL_TEXTURE, this.getX() + 40, this.getY()+ 14, 7, 466, 55, 100, 948, 600);
            secondModelWidget.visible = true;
            secondModelWidget.render(context, xMousePos, yMousePos, partialTick);
        }
        if(thirdModelWidget != null) {
            context.blit(CONTEST_PANEL_TEXTURE, this.getX() + 194, this.getY()+ 14, 7, 466, 55, 100, 948, 600);
            thirdModelWidget.visible = true;
            thirdModelWidget.render(context, xMousePos, yMousePos, partialTick);
        }

        if(yourRank != null){
            drawScaledText(context, pokeFont, Component.translatable("Your Rank is " + yourRank),
                    (Number) (getX() + 15),
                    (Number) (getY() + 20),
                    2f, 1f, 2147483647, 0xFFFFFF, false, false, null, null);
        }

    }

    private void setFirstRankedModel(NetworkablePokemonData data) {
        RenderablePokemon renderablePokemon = new RenderablePokemon(data.getSpecies(), data.getAspects(), data.getHeldItem());
        firstModelWidget = new ModelWidget(
                this.getX() + 118,
                this.getY() + 32,
                66,
                66,
                renderablePokemon,
                2.5F,
                35F,
                -10.0
        );
    }

    private void setSecondRankedModel(NetworkablePokemonData data) {
        RenderablePokemon renderablePokemon = new RenderablePokemon(data.getSpecies(), data.getAspects(), data.getHeldItem());
        secondModelWidget = new ModelWidget(
                this.getX() + 26 ,
                this.getY() + 24,
                66,
                66,
                renderablePokemon,
                1.5F,
                325F,
                -10.0
        );
    }

    private void setThirdRankedModel(NetworkablePokemonData data) {
        RenderablePokemon renderablePokemon = new RenderablePokemon(data.getSpecies(), data.getAspects(), data.getHeldItem());
        thirdModelWidget = new ModelWidget(
                this.getX() + 6 + 145,
                this.getY() + 24,
                66,
                66,
                renderablePokemon,
                1F,
                325F,
                -10.0
        );
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
