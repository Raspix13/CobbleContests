package com.raspix.fabric.cobble_contests.menus.screens;

import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.storage.ClientParty;
import com.raspix.common.cobble_contests.CobbleContests;
import com.raspix.fabric.cobble_contests.blocks.entity.ContestBlockEntity;
import com.raspix.fabric.cobble_contests.menus.ContestBoothMenu;
import com.raspix.fabric.cobble_contests.menus.widgets.LobbyContestantsScroll;
import com.raspix.fabric.cobble_contests.menus.widgets.buttons.FixedImageButton;
import com.raspix.fabric.cobble_contests.menus.widgets.HostedContestPanel;
import com.raspix.fabric.cobble_contests.menus.widgets.buttons.PokemonContestBoothSlotButton;
import com.raspix.fabric.cobble_contests.network.SB.SBCheckContestParticipation;
import com.raspix.fabric.cobble_contests.network.SB.SBConBoothScrReqHostList;
import com.raspix.fabric.cobble_contests.network.SB.SBReqJoinLob;
import com.raspix.fabric.cobble_contests.network.SB.SBUpdateContestInfo;
import com.raspix.fabric.cobble_contests.util.Contest;
import com.raspix.fabric.cobble_contests.util.data.ContestLevel;
import com.raspix.fabric.cobble_contests.util.data.ContestType;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.cobblemon.mod.common.client.render.RenderHelperKt.drawScaledText;

public class ContestBoothScreen extends AbstractContainerScreen<ContestBoothMenu> {

    private final int STARTING_PAGE = 0; // The page everyone sees at the start
    private final int CONTEST_TYPE_SELECTION = 1; // The page the user sees if trying to host or run rank
    private final int CONTEST_WAITING_PAGE = 2; // The page all users see before the contest begins
    private final int POKEMON_SELECTION_PAGE = 3; // The page users trying to join a contest see when joining
    private final int CONTEST_LEVEL_SELECTION_PAGE = 4;
    private final int LOBBY_PAGE = 5; // The page where people wait when someone is hosting
    private final int FIND_A_CON_PAGE = 6; // The page where people join
    private final int IN_RUNNING_CONTEST = 7; // The page a player sees when they are already in a contest

    private int pageIndex;
    private UUID pokemonIndex;
    private int contestLevel;
    private int colorIndex;
    private int hostPanelIdx = -1;

    private int contestRunningType = -1; //0 is rank, 1 is host, 2 is participant

    private ClientParty clientParty;
    private List<Button> homeButtons;
    private List<Button> waitButtons;
    private List<Button> typeButtons;
    private List<Button> partyButtons;
    private Button noPokemonButton;
    private Button confirmationButton;
    private Button startHostedContestButton;

    private Button debugReloadButton;

    private List<UUID> hostConPanelsIDs;
    private List<HostedContestPanel> hostContestPanels;
    private LobbyContestantsScroll contestantsScroll;


    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "textures/gui/contest_booth.png");
    private Inventory playerInv;
    private ContestBoothMenu contestInfoMenu;

    private UUID playerID;


    public ContestBoothScreen(ContestBoothMenu containerID, Inventory playerInv, Component title) {
        super(containerID, playerInv, title);
        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 288;//256;//291;//362;
        this.imageHeight = 224;//192;//194;
        this.playerInv = playerInv;
        this.contestInfoMenu = containerID;
        //PacketHandler.sendToServer(new SBInfoScreenParty(playerInv.player.getUUID()));
    }


    @Override
    protected void init() {
        super.init();
        pokemonIndex = null;
        pageIndex = 0;
        colorIndex = -1;
        clientParty = CobblemonClient.INSTANCE.getStorage().getParty();
        playerID = playerInv.player.getUUID();

        createHomeButtons();
        createWaitingButtons();
        createTypeButtons();
        createPartyButtons();
        createLobbyButtons();
        this.confirmationButton = this.addRenderableWidget(new FixedImageButton(this.leftPos + 80, this.topPos + 40, 64, 18, 289, 43, 18, TEXTURE, 1000, 750, btn -> {
            setContestLevel();
        }));
        this.contestantsScroll = this.addRenderableWidget(new LobbyContestantsScroll(this.leftPos + 75, this.topPos + 60, 150, 80, Component.literal("")));


        /**this.debugReloadButton = this.addRenderableWidget(new FixedImageButton(this.leftPos + 40, this.topPos + 120, 18, 18, 289, 43, 18, TEXTURE, 1000, 750, btn -> {
            setPageIndex(pageIndex);
        }));*/

        /**this.tempHostLobbyButton = this.addRenderableWidget(new HostedContestPanel(this.leftPos + 40, this.topPos + 120, 18, 18, 289, 43, 18, TEXTURE, 1000, 750, btn -> {
            setPageIndex(pageIndex);
        }));*/


        createContestPanes();

        ClientPlayNetworking.send(new SBCheckContestParticipation(playerID));
        setPageIndex(STARTING_PAGE);


    }




    // region Widget Creations and inits

    private void createContestPanes(){
        hostContestPanels = new ArrayList<>();

        for(int i = 0 ; i < 5; i++){
            int finalI = i;
            hostContestPanels.add(this.addRenderableWidget(new HostedContestPanel(this.leftPos + 76, this.topPos + 35 + (25 * (i + 1)), 128, 18, 289, 43, 18, TEXTURE, 1000, 750, btn -> {
                contestRunningType = 2;
                hostPanelIdx = finalI;
                setPageIndex(POKEMON_SELECTION_PAGE);
                //tryJoinLobby(finalI);
            })));
        }
    }

    private void createLobbyButtons(){
        this.hostConPanelsIDs = new ArrayList<>();

        this.startHostedContestButton = this.addRenderableWidget(new FixedImageButton(this.leftPos + 76, this.topPos + 170, 128, 18, 289, 43, 18, TEXTURE, 1000, 750, btn -> {
            startHostedContest();
        }));

    }

    private void createHomeButtons(){
        homeButtons = new ArrayList<>();
        this.homeButtons.add(this.addRenderableWidget(new FixedImageButton(this.leftPos + 76, this.topPos + 40, 128, 18, 289, 43, 18, TEXTURE, 1000, 750, btn -> {
            this.contestRunningType = 0; // Single player / Ranked
            setPageIndex(CONTEST_TYPE_SELECTION);
        })));
        this.homeButtons.add(this.addRenderableWidget(new FixedImageButton(this.leftPos + 76, this.topPos + 80, 128, 18, 289, 43, 18, TEXTURE, 1000, 750, btn -> {
            this.contestRunningType = 1; // Host
            setPageIndex(CONTEST_TYPE_SELECTION);
        })));
        this.homeButtons.add(this.addRenderableWidget(new FixedImageButton(this.leftPos + 76, this.topPos + 120, 128, 18, 289, 43, 18, TEXTURE, 1000, 750, btn -> {
            this.contestRunningType = 2; // Join
            setPageIndex(FIND_A_CON_PAGE);
        })));

    }

    private void createWaitingButtons(){
        waitButtons = new ArrayList<>();
        this.waitButtons.add(this.addRenderableWidget(new FixedImageButton(this.leftPos + 76, this.topPos + 140, 128, 18, 289, 43, 18, TEXTURE, 1000, 750, btn -> {

            if(contestRunningType == 0) {
                startContest();
            }else if(contestRunningType == 1){
                startHosting();
            }
            //start contest
            //setPageIndex(CONTEST_TYPE_SELECTION);
        })));
    }

    private void createTypeButtons(){
        typeButtons = new ArrayList<>();
        this.typeButtons.add(this.addRenderableWidget(new FixedImageButton(this.leftPos + 134, this.topPos + 49, 20, 20, 288, 0, 21, TEXTURE, 1000, 750, btn -> {
            setContestType(ContestType.Cool.getIntValue());
        })));
        this.typeButtons.add(this.addRenderableWidget(new FixedImageButton(this.leftPos + 210, this.topPos + 78, 20, 20, 308, 0, 21, TEXTURE, 1000, 750, btn -> {
            setContestType(ContestType.Beauty.getIntValue());
        })));
        this.typeButtons.add(this.addRenderableWidget(new FixedImageButton(this.leftPos + 173, this.topPos + 155, 20, 20, 328, 0, 21, TEXTURE, 1000, 750, btn -> {
            setContestType(ContestType.Cute.getIntValue());
        })));
        this.typeButtons.add(this.addRenderableWidget(new FixedImageButton(this.leftPos + 95, this.topPos + 155, 20, 20, 348, 0, 21, TEXTURE, 1000, 750, btn -> {
            setContestType(ContestType.Smart.getIntValue());
        })));
        this.typeButtons.add(this.addRenderableWidget(new FixedImageButton(this.leftPos + 58, this.topPos + 78, 20, 20, 368, 0, 21, TEXTURE, 1000, 750, btn -> {
            setContestType(ContestType.Tough.getIntValue());
        })));
    }

    private void createPartyButtons(){
        partyButtons = new ArrayList<>();

        for(int i = 0; i < clientParty.getSlots().size(); i++){
            if(clientParty.get(i) != null) {
                int buttonX = this.leftPos + 39 + (73 * (i % 3));
                int buttonY = this.topPos + 36 + (81 * (i / 3));
                int finalI = i;
                this.partyButtons.add(this.addRenderableWidget(new PokemonContestBoothSlotButton(buttonX, buttonY, 64, 70, 418, 1, 72, TEXTURE, 1000, 750, btn -> {
                    //joinContestWithPokemon(finalI);
                    selectContestPokemon(finalI);
                }, clientParty.get(i))));
            }
        }
        noPokemonButton = this.addRenderableWidget(new FixedImageButton(this.leftPos + 110, this.topPos + 200, 64, 18, 289, 43, 18, TEXTURE, 1000, 750, btn -> {
            selectNoContestPokemon();
        }));
    }

    // endregion


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
        noPokemonButton.visible = false; //index == POKEMON_SELECTION_PAGE && contestRunningType == 1;

        startHostedContestButton.visible = index == LOBBY_PAGE && contestRunningType == 1;
        //System.out.println("Test: " + (index == LOBBY_PAGE) +  " and " + (contestRunningType == 1) + " With crt = " + contestRunningType);

        confirmationButton.visible = index == CONTEST_LEVEL_SELECTION_PAGE;
        //debugReloadButton.visible = index == CONTEST_LEVEL_SELECTION_PAGE;
        //tempHostLobbyButton.visible = index == FIND_A_CON_PAGE;
        toggleHostContestPanels(index == FIND_A_CON_PAGE);
        contestantsScroll.visible = index == LOBBY_PAGE;
    }

    public void setPageToLobby(CompoundTag tag){
        contestRunningType = 2;
        setPageIndex(LOBBY_PAGE);
        updateLobbyContestants(tag);
    }

    // region Networking, Updaters, and Setters

    public void setScreenForContestState(boolean isInContest, boolean isHost, Contest.ContestPhase contestRound){
        //Contest contest = contestInfoMenu.getJoinedContest(playerID);
        //System.out.println("Is in contest: " + isInContest + " Is Host: " + isHost + " Round: " + contestRound.toString());
        if(isInContest){
            if(contestRound == Contest.ContestPhase.IDLE){

                if(isHost){
                    contestRunningType = 1;
                }else{
                    contestRunningType = 2;
                }
                setPageIndex(LOBBY_PAGE);
            }else{
                setPageIndex(IN_RUNNING_CONTEST);
            }

        }else{
            setPageIndex(STARTING_PAGE);
        }
    }

    public void requestPageInfo(){

    }

    public void setPageInfo(){

    }

    private void startHosting(){
        System.out.println("should start hosting");
        menu.startHosting(playerInv.player, playerInv.player.getUUID(), pokemonIndex, colorIndex);
        //menu.startStatAssesment(playerInv.player, playerInv.player.getUUID(), pokemonIndex, colorIndex);
        setPageIndex(LOBBY_PAGE);
        // should have packet
    }

    private void startHostedContest(){
        System.out.println("should start hosted Contest");
        menu.startHostedContest(playerInv.player.getUUID());
        //menu.startStatAssesment(playerInv.player, playerInv.player.getUUID(), pokemonIndex, colorIndex);
        setPageIndex(IN_RUNNING_CONTEST);
        // should have packet
    }

    private void startContest(){
        System.out.println("should be starting ranked contest");
        menu.startRankedContest(colorIndex, pokemonIndex, playerInv.player.getUUID());
        menu.startStatAssesment(playerInv.player, playerInv.player.getUUID(), pokemonIndex, colorIndex, ContestLevel.None);
        setPageIndex(IN_RUNNING_CONTEST);
        // should have packet
    }

    private String getContestResult(){
        return menu.getContestResults();
        //should have packet
    }

    private void tryJoinLobby(int i){
        System.out.println("(Stub) Trying to join Lobby " + i);
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeUUID(playerID);
        buf.writeUUID(pokemonIndex);
        buf.writeUUID(hostConPanelsIDs.get(i));
        ClientPlayNetworking.send(new SBReqJoinLob(buf));
    }

    private void sendGetHostPanels(){
        //redoPlayerPanes(null);
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeUUID(playerID);
        ClientPlayNetworking.send(new SBConBoothScrReqHostList(buf));
    }

    // Should be called by a packet
    public void redoPlayerPanes(CompoundTag tag){
        ListTag listTag = (ListTag) tag.get("contest_list");
        //System.out.println("The packet has called redoPlayerPanes");
        this.hostConPanelsIDs.clear();
        for(int i = 0; i < hostContestPanels.size(); i++){
            HostedContestPanel pan = hostContestPanels.get(i);
            String name = "missing";
            ContestType type = ContestType.None;
            int numContestants = 0;
            if(i < listTag.size()){ // if there is a contest to add
                hostContestPanels.get(i).visible = true;
                CompoundTag singleContest = (CompoundTag) listTag.get(i);
                hostConPanelsIDs.add(singleContest.getUUID("host_id"));
                name = singleContest.getString("host_name");
                type = ContestType.getFromInt(singleContest.getInt("contest_type"));
                numContestants = singleContest.getInt("num_contestants");
            }else{
                hostContestPanels.get(i).visible = false;
            }
            pan.setUpVisuals(name, type, numContestants);
        }
    }

    // endregion


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
        if(pageIndex == CONTEST_WAITING_PAGE){
            /**drawScaledText(guiGraphics, Component.translatable(getContestResult()).getVisualOrderText(),
                    (Number) (this.leftPos + 50),
                    (Number) (this.topPos + 50),
                    0.5f, 0.5f, 1f, 0x00000000, true, false);*/
            if(contestRunningType == 0){
                drawScaledText(guiGraphics, Component.translatable("cobble_contests.contest_text.start_contest").getVisualOrderText(),
                        (Number) (this.leftPos + 144),
                        (Number) (this.topPos + 145),
                        1f, 1f, 1f, 0x00918b99, true, false);
            }else{
                drawScaledText(guiGraphics, Component.translatable("cobble_contests.contest_text.start_hosting").getVisualOrderText(),
                        (Number) (this.leftPos + 144),
                        (Number) (this.topPos + 145),
                        1f, 1f, 1f, 0x00918b99, true, false);
            }

            drawScaledText(guiGraphics, Component.translatable("cobble_contests.contest_text.info.entering_pokemon", clientParty.findByUUID(pokemonIndex).getDisplayName(false).getString()).getVisualOrderText(),
                    (Number) (this.leftPos + 40),
                    (Number) (this.topPos + 50),
                    1f, 1f, 1f, 0x00918b99, false, false);
            drawScaledText(guiGraphics, Component.translatable("cobble_contests.contest_text.info.contest_type", Component.translatable("cobble_contests.contest_type." + ContestBlockEntity.getContestTypeString1(colorIndex).toLowerCase())).getVisualOrderText(),
                    (Number) (this.leftPos + 40),
                    (Number) (this.topPos + 70),
                    1f, 1f, 1f, 0x00918b99, false, false);
            drawScaledText(guiGraphics, Component.translatable("cobble_contests.contest_text.info.instructions").getVisualOrderText(),
                    (Number) (this.leftPos + 40),
                    (Number) (this.topPos + 90),
                    1f, 1f, 1f, 0x00918b99, false, false);
        }
        if(pageIndex == STARTING_PAGE){
            drawScaledText(guiGraphics, Component.translatable("cobble_contests.contest_text.start").getVisualOrderText(),
                    (Number) (this.leftPos + 143),
                    (Number) (this.topPos + 44),
                    1.5f, 1.5f, 1f, 0x00918b99, true, false);
            drawScaledText(guiGraphics, Component.translatable("cobble_contests.contest_text.host").getVisualOrderText(),
                    (Number) (this.leftPos + 143),
                    (Number) (this.topPos + 44 + 40),
                    1.5f, 1.5f, 1f, 0x00918b99, true, false);
            drawScaledText(guiGraphics, Component.translatable("cobble_contests.contest_text.join").getVisualOrderText(),
                    (Number) (this.leftPos + 143),
                    (Number) (this.topPos + 44 + 80),
                    1.5f, 1.5f, 1f, 0x00918b99, true, false);
        }
        if(pageIndex == CONTEST_TYPE_SELECTION){
            drawScaledText(guiGraphics, Component.translatable("cobble_contests.contest_text.title.select_type").getVisualOrderText(),
                    (Number) (this.leftPos + 145),
                    (Number) (this.topPos + 104),
                    1f, 1f, 1f, 0x00918b99, true, false);
            drawScaledText(guiGraphics, Component.translatable("cobble_contests.contest_type.cool").getVisualOrderText(),
                    (Number) (this.leftPos + 143),
                    (Number) (this.topPos + 78),
                    1f, 1f, 1f, 0x00918b99, true, false);
            drawScaledText(guiGraphics, Component.translatable("cobble_contests.contest_type.beauty").getVisualOrderText(),
                    (Number) (this.leftPos + 220),
                    (Number) (this.topPos + 106),
                    1f, 1f, 1f, 0x00918b99, true, false);
            drawScaledText(guiGraphics, Component.translatable("cobble_contests.contest_type.cute").getVisualOrderText(),
                    (Number) (this.leftPos + 184),
                    (Number) (this.topPos + 184),
                    1f, 1f, 1f, 0x00918b99, true, false);
            drawScaledText(guiGraphics, Component.translatable("cobble_contests.contest_type.smart").getVisualOrderText(),
                    (Number) (this.leftPos + 104),
                    (Number) (this.topPos + 184),
                    1f, 1f, 1f, 0x00918b99, true, false);
            drawScaledText(guiGraphics, Component.translatable("cobble_contests.contest_type.tough").getVisualOrderText(),
                    (Number) (this.leftPos + 68),
                    (Number) (this.topPos + 106),
                    1f, 1f, 1f, 0x00918b99, true, false);
        }
        if(pageIndex == POKEMON_SELECTION_PAGE){
            drawScaledText(guiGraphics, Component.translatable("cobble_contests.contest_text.title.pokemon_select").getVisualOrderText(),
                    (Number) (this.leftPos + 140),
                    (Number) (this.topPos + 23),
                    1f, 1f, 1f, 0x00000000, true, false);
            /**if(contestRunningType == 1 || contestRunningType == 2){
                drawScaledText(guiGraphics, Component.translatable("cobble_contests.contest_text.select_no_pokemon").getVisualOrderText(),
                        (Number) (this.leftPos + 140),
                        (Number) (this.topPos + 200),
                        1f, 1f, 1f, 0x00000000, true, false);
            }*/
        }

        if(pageIndex == LOBBY_PAGE){
            drawScaledText(guiGraphics, Component.translatable("cobble_contests.contest_text.host_lobby").getVisualOrderText(),
                    (Number) (this.leftPos + 143),
                    (Number) (this.topPos + 44),
                    1.5f, 1.5f, 1f, 0x00918b99, true, false);
            if(contestRunningType == 1){
                drawScaledText(guiGraphics, Component.translatable("cobble_contests.contest_text.start_contest").getVisualOrderText(),
                        (Number) (this.leftPos + 143),
                        (Number) (this.topPos + 176),
                        1f, 1f, 1f, 0x00918b99, true, false);
                drawScaledText(guiGraphics, Component.translatable("cobble_contests.contest_text.info.instructions").getVisualOrderText(),
                        (Number) (this.leftPos + 40),
                        (Number) (this.topPos + 150),
                        1f, 1f, 1f, 0x00918b99, false, false);
            }else if(contestRunningType == 2){
                drawScaledText(guiGraphics, Component.translatable("cobble_contests.contest_text.wait_for_start").getVisualOrderText(),
                        (Number) (this.leftPos + 143),
                        (Number) (this.topPos + 176),
                        1f, 1f, 1f, 0x00918b99, true, false);
                drawScaledText(guiGraphics, Component.translatable("cobble_contests.contest_text.info.instructions").getVisualOrderText(),
                        (Number) (this.leftPos + 40),
                        (Number) (this.topPos + 150),
                        1f, 1f, 1f, 0x00918b99, false, false);
            }
        }
        if(pageIndex == IN_RUNNING_CONTEST){
            drawScaledText(guiGraphics, Component.translatable("cobble_contests.contest_text.contest_running").getVisualOrderText(),
                    (Number) (this.leftPos + 143),
                    (Number) (this.topPos + 44),
                    1.5f, 1.5f, 1f, 0x00918b99, true, false);
            /**drawScaledText(guiGraphics, Component.literal("Pokemon: " + clientParty.findByUUID(pokemonIndex).getDisplayName().getString()).getVisualOrderText(),
                    (Number) (this.leftPos + 40),
                    (Number) (this.topPos + 50),
                    1f, 1f, 1f, 0x00918b99, false, false);*/
            drawScaledText(guiGraphics, Component.literal("Contest Type: " + ContestBlockEntity.getContestTypeString1(colorIndex)).getVisualOrderText(),
                    (Number) (this.leftPos + 40),
                    (Number) (this.topPos + 70),
                    1f, 1f, 1f, 0x00918b99, false, false);

        }

        if(pageIndex == FIND_A_CON_PAGE){
            drawScaledText(guiGraphics, Component.translatable("cobble_contests.contest_text.title.fac").getVisualOrderText(),
                    (Number) (this.leftPos + 143),
                    (Number) (this.topPos + 44),
                    1.5f, 1.5f, 1f, 0x00918b99, true, false);

        }

    }

    @Override
    protected void renderLabels(GuiGraphics arg, int i, int j) {
    }


    private void selectContestPokemon(int pokeIndex) {
        this.pokemonIndex = clientParty.get(pokeIndex).getUuid();
        if(contestRunningType == 2){
            tryJoinLobby(hostPanelIdx);
        }else{
            setPageIndex(CONTEST_WAITING_PAGE);
        }
    }

    private void selectNoContestPokemon() {
        setPageIndex(CONTEST_WAITING_PAGE);
    }

    private void toggleHostContestPanels(boolean state){
        for(HostedContestPanel panel: hostContestPanels){
            panel.visible = state;
        }
        if(state){
            sendGetHostPanels();
        }
    }

    private void setContestType(int type){
        colorIndex = type;
        setPageIndex(POKEMON_SELECTION_PAGE);
    }

    private void setContestLevel(){
        if(menu.hostSelectType(playerID, colorIndex)){
            setPageIndex(CONTEST_WAITING_PAGE);
        }else {
            setPageIndex(STARTING_PAGE);
        }
    }

    public void updateLobbyContestants(CompoundTag tag){
        contestantsScroll.setContestantsDataFromTag(tag);
    }



}
