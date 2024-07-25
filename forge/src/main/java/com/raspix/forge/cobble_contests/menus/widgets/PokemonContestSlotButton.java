package com.raspix.forge.cobble_contests.menus.widgets;

import com.cobblemon.mod.common.client.CobblemonResources;
import com.cobblemon.mod.common.client.render.models.blockbench.pokemon.PokemonFloatingState;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.status.PersistentStatusContainer;
import com.mojang.blaze3d.vertex.PoseStack;
import kotlin.Pair;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

import static com.cobblemon.mod.common.api.gui.GuiUtilsKt.blitk;
import static com.cobblemon.mod.common.client.gui.PokemonGuiUtilsKt.drawProfilePokemon;
import static com.cobblemon.mod.common.client.render.RenderHelperKt.*;
import static com.cobblemon.mod.common.util.LocalizationUtilsKt.lang;
import static com.cobblemon.mod.common.util.MiscUtilsKt.cobblemonResource;

public class PokemonContestSlotButton extends ImageButton {

    private final int WIDTH = 46;
    private final int HEIGHT = 27;
    private final int PORTRAIT_DIAMETER = 25;
    private final Pokemon pokemon;

    private ResourceLocation slotResource = cobblemonResource("textures/gui/summary/summary_party_slot.png");
    private ResourceLocation slotFaintedResource = cobblemonResource("textures/gui/summary/summary_party_slot_fainted.png");
    private ResourceLocation slotEmptyResource = cobblemonResource("textures/gui/summary/summary_party_slot_empty.png");
    ResourceLocation genderIconMale = cobblemonResource("textures/gui/party/party_gender_male.png");
    ResourceLocation genderIconFemale = cobblemonResource("textures/gui/party/party_gender_female.png");

    public PokemonContestSlotButton(int i, int j, int k, int l, int m, int n, ResourceLocation arg, OnPress arg2, Pokemon pokemon) {
        this(i, j, k, l, m, n, l, arg, 256, 256, arg2, pokemon);
    }

    public PokemonContestSlotButton(int i, int j, int k, int l, int m, int n, int o, ResourceLocation arg, OnPress arg2, Pokemon pokemon) {
        this(i, j, k, l, m, n, o, arg, 256, 256, arg2, pokemon);
    }

    public PokemonContestSlotButton(int i, int j, int k, int l, int m, int n, int o, ResourceLocation arg, int p, int q, OnPress arg2, Pokemon pokemon) {
        //ResourceLocation buttonText = getSlotTexture(pokemon);
        this(i, j, k, l, m, n, o, arg, p, q, arg2, CommonComponents.EMPTY, pokemon);
    }

    public PokemonContestSlotButton(int i, int j, int k, int l, int m, int n, int o, ResourceLocation arg, int p, int q, OnPress arg2, Component arg3, Pokemon pokemon) {
        super(i, j, k, l, m, n, o, arg, p, q, arg2, arg3);
        this.pokemon = pokemon;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mX, int mY, float f) {
        super.renderWidget(guiGraphics, mX, mY, f);
        if (pokemon != null) {
            float halfScale = 0.5f;
            PoseStack poses = guiGraphics.pose();

            ResourceLocation stateIcon = pokemon.getState().getIcon(pokemon);
            if (stateIcon != null) {
                blitk(
                        poses, stateIcon, (Number)((getX() + 24.5) / halfScale), (Number)((getY() + 3) / halfScale),
                        (Number)17, (Number)24, (Number)0, (Number)0, (Number) width, (Number) height, (Number)0,
                        (Number)1f, (Number)1, (Number)1, (Number)1, true, halfScale
                );
            }

            ResourceLocation ballIcon = cobblemonResource("textures/gui/ball/" + pokemon.getCaughtBall().getName().getPath() + ".png");
            int ballHeight = 22;
            blitk(
                    poses, ballIcon, (getX() - 2) / halfScale, (getY() - 3) / halfScale, ballHeight,
                    18, (Number)0, (Number)0, (Number) 18, ballHeight * 2, (Number)0,
                    (Number)1f, (Number)1, (Number)1, (Number)1, true, halfScale
            );


            PersistentStatusContainer status = pokemon.getStatus();
            if (!pokemon.isFainted() && status != null) {
                String statusName = status.getStatus().getShowdownName();
                blitk(
                        poses, cobblemonResource("textures/gui/party/status_$statusName.png"),
                        getX() + 42, getY() + 5, 14,
                        4,(Number)0, (Number)0, (Number) width, (Number) height, (Number)0,
                        (Number)1f, (Number)1, (Number)1, (Number)1, true, 1f
                );
            }

            float hpRatio = pokemon.getCurrentHealth() / (float)pokemon.getHp();
            int barWidthMax = 37;
            float barWidth = hpRatio * barWidthMax;
            Pair<Float, Float> cols = getDepletableRedGreen(hpRatio, 0.5f, 0.2f);

            blitk(
                    poses,
                    CobblemonResources.INSTANCE.getWHITE(),
                    getX() + 4,
                    getY() + 25,
                    1,
                    barWidth,
                    barWidthMax - barWidth, 0,
                    barWidth / hpRatio,
                    10,
                    0,
                    cols.component1() * 0.8F,
                    cols.component2() * 0.8F,
                    0.27F, 1, true, 1
            );


            drawScaledText(guiGraphics, pokemon.getDisplayName().getVisualOrderText(),
                    (Number) (this.getX() + 4),
                    (Number) (this.getY() + 20),
                    0.5f, 0.5f, 1f, 0x00FFFFFF, false, false);




            poses.translate(this.getX() + (PORTRAIT_DIAMETER / 2.0), this.getY()-10, 0f);
            poses.pushPose();
            drawProfilePokemon(pokemon.getSpecies().getResourceIdentifier(), pokemon.getAspects(),
                    poses, new Quaternionf().rotationXYZ((float) Math.toRadians(13f), (float) Math.toRadians(35f), 0F),
                    new PokemonFloatingState(), 2.42f, 12f);

            poses.popPose();
            poses.translate(-(this.getX() + (PORTRAIT_DIAMETER / 2.0)), -(this.getY()-10), 0f);

            /**if (pokemon.getGender() != Gender.GENDERLESS) {
                blitk(
                        poses, (pokemon.getGender() == Gender.MALE)? genderIconMale : genderIconFemale,
                        (getX() + 40) / halfScale, (getY() + 20) / halfScale, height = 7, width = 5,
                        0, 0, 5, 7, 0, 1, 1, 1, 1, true,
                        halfScale
                );
            }*/

            drawScaledText(
                    guiGraphics,
                    null,
                    lang("ui.lv.number", pokemon.getLevel()),
                    //Component.translatable("ui.lv.number", pokemon.getLevel()),
                    getX() + 31,
                    getY() + 13,
                     halfScale,
                    1f,
                    Integer.MAX_VALUE,
                    0x00FFFFFF,
                    true,
                    true, null, null
            );

            // Held Item
            ItemStack heldItem = pokemon.heldItem();
            if (!heldItem.isEmpty()) {
                renderScaledGuiItemIcon(
                        heldItem,
                        getX() + 14.0,
                        getY() + 9.5,
                        0.5,
                        100f,
                        poses
                );
            }

        }

    }

}
