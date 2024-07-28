package com.raspix.forge.cobble_contests.menus.screens;

import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.storage.ClientParty;
import com.raspix.common.cobble_contests.CobbleContests;
import com.raspix.forge.cobble_contests.menus.ContestMenu;
import com.raspix.forge.cobble_contests.menus.widgets.PokemonContestSlotButton;
import com.raspix.forge.cobble_contests.network.PacketHandler;
import com.raspix.forge.cobble_contests.network.SBInfoScreenParty;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;
import static com.cobblemon.mod.common.util.MiscUtilsKt.cobblemonResource;

@OnlyIn(Dist.CLIENT)
public class SecondTestScreen extends AbstractContainerScreen<ContestMenu> {

    private final int STARTING_PAGE = 0; // The page everyone sees at the start
    private final int CONTEST_TYPE_SELECTION = 1; // The page the user sees if trying to host
    private final int CONTEST_WAITING_PAGE = 2; // The page all users see before the contest begins
    private final int POKEMON_SELECTION_PAGE = 3; // The page users trying to join a contest see when joining
    private final int RESULTS_PAGE = 4;

    private int pageIndex;
    private int pokemonIndex;
    private int colorIndex;


    private ClientParty clientParty;
    private List<Button> homeButtons;
    private List<Button> waitButtons;
    private List<Button> typeButtons;
    private List<Button> partyButtons;

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

    private static final ResourceLocation TEXTURE = new ResourceLocation(CobbleContests.MOD_ID, "textures/gui/contest_booth.png");
    private static final ResourceLocation TEXTURE2 = new ResourceLocation(CobbleContests.MOD_ID, "textures/gui/contest_profile.png");
    private static final ResourceLocation MOVE_PANELS = new ResourceLocation(CobbleContests.MOD_ID, "textures/gui/move_panels.png");
    private Inventory playerInv;
    private ContestMenu contestInfoMenu;

    private UUID playerID;


    public SecondTestScreen(ContestMenu containerID, Inventory playerInv, Component title) {
        super(containerID, playerInv, title);
        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 288;//256;//291;//362;
        this.imageHeight = 224;//192;//194;
        this.playerInv = playerInv;
        this.contestInfoMenu = containerID;
        PacketHandler.sendToServer(new SBInfoScreenParty(playerInv.player.getUUID()));
    }

    @Override
    protected void init() {
        super.init();
        pokemonIndex = 0;
        pageIndex = 0;
        colorIndex = -1;
        clientParty = CobblemonClient.INSTANCE.getStorage().getMyParty();
        playerID = playerInv.player.getUUID();


        createHomeButtons();
        createWaitingButtons();
        createTypeButtons();
        createPartyButtons();

        setPageIndex(0);
    }

    private void createHomeButtons(){
        homeButtons = new ArrayList<>();
        this.homeButtons.add(this.addRenderableWidget(new ImageButton(this.leftPos + 60, this.topPos + 80, 64, 18, 289, 43, 18, TEXTURE, 1000, 750, btn -> {
            setPageIndex(CONTEST_TYPE_SELECTION);
        })));
        this.homeButtons.add(this.addRenderableWidget(new ImageButton(this.leftPos + 130, this.topPos + 80, 64, 18, 353, 43, 18, TEXTURE, 1000, 750, btn -> {
            setPageIndex(POKEMON_SELECTION_PAGE);
        })));
    }

    private void createWaitingButtons(){
        waitButtons = new ArrayList<>();
        this.waitButtons.add(this.addRenderableWidget(new ImageButton(this.leftPos + 60, this.topPos + 80, 64, 18, 289, 43, 18, TEXTURE, 1000, 750, btn -> {
            startContest();
            //start contest
            //setPageIndex(CONTEST_TYPE_SELECTION);
        })));
    }

    private void createTypeButtons(){
        typeButtons = new ArrayList<>();
        this.typeButtons.add(this.addRenderableWidget(new ImageButton(this.leftPos + 134, this.topPos + 49, 20, 20, 288, 0, 21, TEXTURE, 1000, 750, btn -> {
            setContestType(0);
        })));
        this.typeButtons.add(this.addRenderableWidget(new ImageButton(this.leftPos + 210, this.topPos + 78, 20, 20, 308, 0, 21, TEXTURE, 1000, 750, btn -> {
            setContestType(1);
        })));
        this.typeButtons.add(this.addRenderableWidget(new ImageButton(this.leftPos + 173, this.topPos + 155, 20, 20, 328, 0, 21, TEXTURE, 1000, 750, btn -> {
            setContestType(2);
        })));
        this.typeButtons.add(this.addRenderableWidget(new ImageButton(this.leftPos + 95, this.topPos + 155, 20, 20, 348, 0, 21, TEXTURE, 1000, 750, btn -> {
            setContestType(3);
        })));
        this.typeButtons.add(this.addRenderableWidget(new ImageButton(this.leftPos + 58, this.topPos + 78, 20, 20, 368, 0, 21, TEXTURE, 1000, 750, btn -> {
            setContestType(4);
        })));
    }

    private void createPartyButtons(){
        partyButtons = new ArrayList<>();

        for(int i = 0; i < clientParty.getSlots().size(); i++){
            if(clientParty.get(i) != null) {
                int buttonX = this.leftPos + 39 + (73 * (i % 3));
                int buttonY = this.topPos + 36 + (81 * (i / 3));
                int finalI = i;
                this.partyButtons.add(this.addRenderableWidget(new PokemonContestSlotButton(buttonX, buttonY, 20, 20, 289, 0, 21, TEXTURE, 1000, 750, btn -> {
                    joinContestWithPokemon(finalI);
                }, clientParty.get(i))));
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int xMousePos, int yMousePos) {
        if(pageIndex == CONTEST_TYPE_SELECTION){
            guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 1000, 750);
        }else if(pageIndex == POKEMON_SELECTION_PAGE){
            guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 225, this.imageWidth, this.imageHeight, 1000, 750);
        }else{
            guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 450, this.imageWidth, this.imageHeight, 1000, 750);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int xMousePos, int yMousePos, float partialTick) { //
        super.render(guiGraphics, xMousePos, yMousePos, partialTick);
        if(pageIndex == 2){
            drawScaledText(guiGraphics, Component.translatable(getContestResult()).getVisualOrderText(),
                    (Number) (this.leftPos + 50),
                    (Number) (this.topPos + 50),
                    0.5f, 0.5f, 1f, 0x00000000, true, false);
        }

    }

    @Override
    protected void renderLabels(GuiGraphics arg, int i, int j) {
    }

    private void joinContestWithPokemon(int index){
        pokemonIndex = index;
        setPageIndex(CONTEST_WAITING_PAGE);

        //should have packet here
    }

    private void setPageIndex(int index){
        pageIndex = index;
        for (Button homeButton : homeButtons) {
            homeButton.visible = index == STARTING_PAGE;
        }
        for (Button waitButton : waitButtons) {
            waitButton.visible = index == CONTEST_WAITING_PAGE;
        }
        for (Button typeButton : typeButtons) {
            typeButton.visible = index == CONTEST_TYPE_SELECTION;
        }
        for (Button partyButton : partyButtons) {
            partyButton.visible = index == POKEMON_SELECTION_PAGE;
        }
    }

    private void setContestType(int type){
        colorIndex = type;
        if(menu.hostSelectType(playerID, type)){
            setPageIndex(CONTEST_WAITING_PAGE);
        }else {
            setPageIndex(STARTING_PAGE);
        }
        //should have packet type
    }

    private void startContest(){
        menu.startContest(colorIndex, pokemonIndex, playerInv.player.getUUID());
        menu.startStatAssesment(playerInv.player.getUUID(), pokemonIndex, colorIndex, 0);
        // should have packet
    }

    private String getContestResult(){
        return menu.getContestResults();
        //should have packet
    }



}
