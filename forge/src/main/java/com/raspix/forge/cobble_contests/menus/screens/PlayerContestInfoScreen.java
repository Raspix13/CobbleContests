package com.raspix.forge.cobble_contests.menus.screens;

import com.cobblemon.mod.common.api.gui.ColourLibrary;
import com.cobblemon.mod.common.api.gui.MultiLineLabelK;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.CobblemonResources;
import com.cobblemon.mod.common.client.storage.ClientParty;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.raspix.common.cobble_contests.CobbleContests;
import com.raspix.forge.cobble_contests.CobbleContestsMoves;
import com.raspix.forge.cobble_contests.events.ContestMoves;
import com.raspix.forge.cobble_contests.menus.PlayerContestInfoMenu;
import com.raspix.forge.cobble_contests.menus.widgets.PokemonInfoSlotButton;
import com.raspix.forge.cobble_contests.network.PacketHandler;
import com.raspix.forge.cobble_contests.network.SBInfoScreenParty;
import com.raspix.forge.cobble_contests.pokemon.CVs;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.*;

import static com.cobblemon.mod.common.client.gui.PokemonGuiUtilsKt.drawProfilePokemon;
import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;
import static com.cobblemon.mod.common.util.LocalizationUtilsKt.lang;
import static com.cobblemon.mod.common.util.MiscUtilsKt.cobblemonResource;

@OnlyIn(Dist.CLIENT)
public class PlayerContestInfoScreen extends AbstractContainerScreen<PlayerContestInfoMenu> {

    private static final int BASE_WIDTH = 349;
    private static final int BASE_HEIGHT = 205;
    private static final int MAX_STATS = 255;

    private static int PORTRAIT_DIAMETER = 25;
    private int pokemonIndex;
    private int pageIndex;
    private List<CVs> cvList;

    private List<Button> buttons;

    private ResourceLocation baseResource = cobblemonResource("textures/gui/summary/summary_base.png");
    private ResourceLocation portraitBackgroundResource = cobblemonResource("textures/gui/summary/portrait_background.png");
    private ResourceLocation typeSpacerResource = cobblemonResource("textures/gui/summary/type_spacer.png");
    private ResourceLocation typeSpacerDoubleResource = cobblemonResource("textures/gui/summary/type_spacer_double.png");
    private ResourceLocation sideSpacerResource = cobblemonResource("textures/gui/summary/summary_side_spacer.png");
    private ResourceLocation evolveButtonResource = cobblemonResource("textures/gui/summary/summary_evolve_button.png");
    ResourceLocation iconShinyResource = cobblemonResource("textures/gui/summary/icon_shiny.png");
    private ResourceLocation slotResource = cobblemonResource("textures/gui/summary/summary_party_slot.png");
    private ResourceLocation slotFaintedResource = cobblemonResource("textures/gui/summary/summary_party_slot_fainted.png");
    private ResourceLocation slotEmptyResource = cobblemonResource("textures/gui/summary/summary_party_slot_empty.png");
    ResourceLocation genderIconMale = cobblemonResource("textures/gui/party/party_gender_male.png");
    ResourceLocation genderIconFemale = cobblemonResource("textures/gui/party/party_gender_female.png");

    private static final ResourceLocation TEXTURE = new ResourceLocation(CobbleContests.MOD_ID, "textures/gui/contest_profile.png");
    private static final ResourceLocation MOVE_PANELS = new ResourceLocation(CobbleContests.MOD_ID, "textures/gui/move_panels.png");
    private Inventory playerInv;
    private PlayerPartyStore playerPartyStore;
    private ClientParty clientParty;

    private PlayerContestInfoMenu contestInfoMenu;


    public PlayerContestInfoScreen(PlayerContestInfoMenu containerID, Inventory playerInv, Component title) {
        super(containerID, playerInv, title);
        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 291;//362;
        this.imageHeight = 194;
        this.playerInv = playerInv;
        this.contestInfoMenu = containerID;
        PacketHandler.sendToServer(new SBInfoScreenParty(playerInv.player.getUUID()));
    }

    @Override
    protected void init() {
        super.init();
        buttons = new ArrayList<>();
        this.buttons.add(this.addRenderableWidget(new ImageButton(this.leftPos + 12, this.topPos - 13, 17, 14, 292, 0, 16, TEXTURE, 1000, 750, btn -> {
            setPageIndex(0);
        })));
        this.buttons.add(this.addRenderableWidget(new ImageButton(this.leftPos + 30, this.topPos - 13, 17, 14, 309, 0, 16, TEXTURE, 1000, 750, btn -> {
            setPageIndex(1);
        })));
        this.buttons.add(this.addRenderableWidget(new ImageButton(this.leftPos + 48, this.topPos -13, 17, 14, 326, 0, 16, TEXTURE, 1000, 750, btn -> {
            setPageIndex(2);
        })));


        pokemonIndex = 0;
        pageIndex = 0;

        renderParty();

    }

    private void renderParty(){
        System.out.println("rendering party");
        int startingX = this.leftPos - 42;
        int xOffset = 0;
        int startingY = this.topPos + 10;
        int yOffset = 30;

        clientParty = CobblemonClient.INSTANCE.getStorage().getMyParty();
        List<Pokemon> partyPoke = this.clientParty.getSlots();//playerPartyStore.toGappyList();// contestInfoMenu.getPartyStore().toGappyList(); //
        for(int i = 0; i < 6; i++){
            Pokemon poke = partyPoke.get(i);

            System.out.println("PokemonParty");
            int finalI = i;
            this.buttons.add(this.addRenderableWidget(new PokemonInfoSlotButton(startingX + (i * xOffset),
                    startingY + (i * yOffset),
                    46, 27, 0, 0, 27, getSlotTexture(poke), 46, 54, btn -> {
                    setPokemonPage(finalI);
                }, poke)));


        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int xMousePos, int yMousePos) {
        //renderBackground(guiGraphics);
        if(pageIndex == 1){ // badge page
            guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 194, this.imageWidth, this.imageHeight, 1000, 750);
        }else if(pageIndex == 0) { //stat page
            guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 1000, 750);
        }else{
            guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 388, this.imageWidth, this.imageHeight, 1000, 750);
        }

    }

    private ResourceLocation getSlotTexture(Pokemon pokemon){
        if (pokemon != null) {
            if (pokemon.isFainted()) return slotFaintedResource;
            return slotResource;
        }
        return slotEmptyResource;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int xMousePos, int yMousePos, float partialTick) {
        //this.renderBackground(guiGraphics);
        super.render(guiGraphics, xMousePos, yMousePos, partialTick);
        //renderTooltip(guiGraphics, xMousePos, yMousePos);

        //UUID id = playerInv.player.getUUID();
        //PlayerPartyStore playerPartyStore = Cobblemon.INSTANCE.getStorage().getParty(id);
        if(pageIndex == 0){ //stat page
            if(clientParty != null && clientParty.getSlots().size() > 0 && clientParty.get(pokemonIndex) != null && cvList != null){
                Pokemon poke = clientParty.get(pokemonIndex);
                assert poke != null;
                drawStatHexagon(new Vector3f(45f/255f, 237f, 96f/255f), cvList.get(pokemonIndex), guiGraphics);//CVs.getFromTag(poke.getPersistentData().getCompound(PoffinItem.cvsKey)));

            }
        }else if (pageIndex == 1){// badge page
            if(clientParty != null && clientParty.getSlots().size() > 0 && clientParty.get(pokemonIndex) != null){
                Pokemon poke = clientParty.get(pokemonIndex);
                drawContestStats(guiGraphics, poke);
            }
        }


        //drawTriangle(new Vector3f(45, 237, 96), new Vector2f(this.leftPos + 5, this.topPos + 10), new Vector2f(this.leftPos+ 50, this.topPos+50), new Vector2f(this.leftPos+25, this.topPos));

        PoseStack poses = guiGraphics.pose();

        /**poses.translate((PORTRAIT_DIAMETER / 2.0) + 80, 0f, 0f);
        //poses.scale(2.5F, 2.5F, 1F);
        List<Pokemon> partyPoke = this.playerPartyStore.toGappyList();
        for(int i = 0; i < 6; i++){
            Pokemon poke = partyPoke.get(i);
            if(poke != null){
                poses.pushPose();
                poses.translate(0, 40f * i, 0);
                //matrices.translate(getXSize() + (PORTRAIT_DIAMETER / 2.0), getYSize() - 3.0, 0.0)l
                System.out.println("not null");
                drawProfilePokemon(poke.getSpecies().getResourceIdentifier(), poke.getAspects(),
                        poses, new Quaternionf().rotationXYZ((float)Math.toRadians(13f), (float)Math.toRadians(35f), 0F),
                        new PokemonFloatingState(), 2.42f, 20f);

                poses.popPose();
            }

        }*/
    }

    public void drawContestStats(GuiGraphics guiGraphics, Pokemon pokemon){
        List<Move> moves = pokemon.getMoveSet().getMoves();
        int xPos = getGuiLeft() + 15;
        int yPos = getGuiTop()  + 20;
        int xInc = 0;
        int yInc = 40;
        for(int i = 0; i < 4; i++){
            if(moves.size() > i && moves.get(i) != null){
                Move move = moves.get(i);
                drawMovePanel(guiGraphics, move, xPos, yPos + (i * yInc));

            }else {
                guiGraphics.blit(MOVE_PANELS, xPos, yPos + (i * yInc), 0, 0, 262, 32, 291, 400);
            }
        }
    }

    private void drawMovePanel(GuiGraphics guiGraphics, Move move, int xPos, int yPos){
        String moveName = move.getName();
        //System.out.println("Move name is " + moveName);
        int panelOffset = 0;
        int appeal = 1;
        PoseStack poses = guiGraphics.pose();
        String description = "placeholder";
        Map<String, ContestMoves.MoveData> contestMoves = CobbleContestsMoves.INSTANCE.allMoves;//CobbleContestsForge.contestMoves;'
        //System.out.println("length of moves is: " + contestMoves.size());
        if(contestMoves.containsKey(moveName)) {
            ContestMoves.MoveData data = contestMoves.get(moveName);
            String type = data.getType();
            switch (type) {
                case "Cool":
                    panelOffset = 32;
                    break;
                case "Beauty":
                    panelOffset = 64;
                    break;
                case "Cute":
                    panelOffset = 96;
                    break;
                case "Smart":
                    panelOffset = 128;
                    break;
                case "Tough":
                    panelOffset = 160;
                    break;
                default:
                    break;
            }
            appeal = data.getAppeal();
        }
        guiGraphics.blit(MOVE_PANELS, xPos, yPos, 0, panelOffset, 262, 32, 291, 400);
        drawScaledText(guiGraphics, null, lang("move." + moveName),
                (Number) (xPos + 15),
                (Number) (yPos + 5),
                1f, 1f, 2147483647, 0x00FFFFFF, false, true, null, null);
        //System.out.println(lang("move", moveName));
        guiGraphics.blit(MOVE_PANELS, xPos + 110 - (1 + appeal * 8), yPos + 21, 1, 193, 1 + appeal * 8, 9, 291, 400);

        poses.pushPose();
        poses.scale(0.5f, 0.5f, 1F);
        MultiLineLabelK.Companion.create(
                Component.translatable("cobble_contests.move_info." + description),
                145 / 0.5f,
                5
        ).renderLeftAligned(
                guiGraphics,
                (xPos + 120) / 0.5f,
                (yPos + 3) / 0.5f,
                5.5 / 0.5f,
                ColourLibrary.WHITE,
                true
        );
        poses.popPose();


        //display description
    }


    private void drawStatHexagon(Vector3f colour, CVs cvs, GuiGraphics guiGraphics) {
        int maximum = 50;
        int minimum = 1;

        System.out.println(cvs.getAsString());
        int sheenStars = cvs.getSheenStars();

        float upperHexX = (float) (Math.sin(Math.toRadians(72.0)) * (float) maximum);
        float upperHexY = (float) (Math.cos(Math.toRadians(72.0)) * (float) maximum);
        float lowerHexX = (float) (Math.sin(Math.toRadians(36.0)) * (float) maximum);
        float lowerHexY = (float) (Math.cos(Math.toRadians(36.0)) * (float) maximum);

        float hexCenterX = this.leftPos + (this.imageWidth / 2.0F) + 40;
        float hexCenterY = this.topPos + (this.imageHeight / 2.0F) - 14;

        float coolRatio = Math.max(0.0F, Math.min(1.0F, (float)  cvs.getCool()/ (float)MAX_STATS))+ 0.05f;
        float beautyRatio = Math.max(0.0F, Math.min(1.0F, (float)  cvs.getBeauty()/ (float)MAX_STATS))+ 0.05f;
        float cuteRatio = Math.max(0.0F, Math.min(1.0F, (float)  cvs.getCute()/ (float)MAX_STATS)) + 0.05f;
        float smartRatio = Math.max(0.0F, Math.min(1.0F, (float)  cvs.getSmart()/ (float)MAX_STATS))+ 0.05f;
        float toughRatio = Math.max(0.0F, Math.min(1.0F, (float)  cvs.getTough()/ (float)MAX_STATS))+ 0.05f;

        Vector2f centerPoint = new Vector2f(hexCenterX, hexCenterY);
        Vector2f coolPoint = new Vector2f(hexCenterX, hexCenterY - (maximum * coolRatio));
        Vector2f beautyPoint = new Vector2f(hexCenterX + (upperHexX * beautyRatio), hexCenterY - (upperHexY * beautyRatio));
        Vector2f cutePoint = new Vector2f(hexCenterX + (lowerHexX * cuteRatio), hexCenterY + (lowerHexY * cuteRatio));
        Vector2f smartPoint = new Vector2f(hexCenterX - (lowerHexX * smartRatio), hexCenterY + (lowerHexY * smartRatio));
        Vector2f toughPoint = new Vector2f(hexCenterX - (upperHexX * toughRatio), hexCenterY - (upperHexY * toughRatio));


        drawTriangle(colour, coolPoint, centerPoint, beautyPoint); // upper right cool to beauty
        drawTriangle(colour, beautyPoint, centerPoint, cutePoint); // bottom right cute to beauty
        drawTriangle(colour, cutePoint, centerPoint, smartPoint); // bottom center cute to smart
        drawTriangle(colour, smartPoint, centerPoint, toughPoint); // bottom left smart to tough
        drawTriangle(colour, toughPoint, centerPoint, coolPoint);  //upper left tough to cool

        guiGraphics.blit(TEXTURE, this.leftPos + 163, this.topPos + 164, 292, 51, 1+ 8*sheenStars, 14, 1000, 750);
    }

    private void drawTriangle(Vector3f colour, Vector2f v1, Vector2f v2, Vector2f v3) {
        //CobblemonResources.INSTANCE.getWHITE().let(RenderSystem::setShaderTexture);

        RenderSystem.setShaderTexture(0, CobblemonResources.INSTANCE.getWHITE());
        //RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(colour.x, 1, colour.z, 0.2F);

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuilder();

        //RenderSystem.setShaderColor(colour.x, colour.y, colour.z, 1.0F);
        bufferBuilder.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION);
        //bufferBuilder.color(0x2d, 0xed, 0x60, 0x66);
        bufferBuilder.vertex(v1.x, v1.y, 10.0D).endVertex();//.color(0x2d, 0xed, 0x60, 0x66)
        bufferBuilder.vertex(v2.x, v2.y, 10.0D).endVertex();//.color(0x2d, 0xed, 0x60, 0x66)
        bufferBuilder.vertex(v3.x, v3.y, 10.0D).endVertex();//.color(0x2d, 0xed, 0x60, 0x66)
        //bufferBuilder.vertex(v1.x, v1.y, 10.0).next();
        //bufferBuilder.vertex(v2.x, v2.y, 10.0).next();
        //bufferBuilder.vertex(v3.x, v3.y, 10.0).next();
        //BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        bufferBuilder.nextElement();
        tessellator.end();
        //RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        //RenderSystem.setShaderColor(colour.x, colour.y, colour.z, 1.0F);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }


    @Override
    protected void renderLabels(GuiGraphics arg, int i, int j) {
    }


    private void setPokemonPage(int index){
        pokemonIndex = index;
    }

    private void setPageIndex(int index){
        pageIndex = index;
    }


    public void setParty(PlayerPartyStore pps){
        this.playerPartyStore = pps;
        //renderParty();
    }


    public void setCVs(CompoundTag tag) {
        cvList = new ArrayList<>();

        cvList.add(CVs.getFromTag(tag.getCompound("poke0")));
        cvList.add(CVs.getFromTag(tag.getCompound("poke1")));
        cvList.add(CVs.getFromTag(tag.getCompound("poke2")));
        cvList.add(CVs.getFromTag(tag.getCompound("poke3")));
        cvList.add(CVs.getFromTag(tag.getCompound("poke4")));
        cvList.add(CVs.getFromTag(tag.getCompound("poke5")));
        System.out.println("Set up CVs");
    }
}
