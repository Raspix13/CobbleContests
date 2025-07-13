package com.raspix.fabric.cobble_contests.util;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveSet;
import com.cobblemon.mod.common.api.reactive.SimpleObservable;
import com.cobblemon.mod.common.client.battle.ClientBattleMessageQueue;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.net.messages.client.effect.SpawnSnowstormParticlePacket;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.raspix.fabric.cobble_contests.events.ContestMoves;
import com.raspix.fabric.cobble_contests.network.CB.CBLobRetReq;
import com.raspix.fabric.cobble_contests.network.CB.CBSendContestantMessage;
import com.raspix.fabric.cobble_contests.network.CB.CBSendContestantStatus;
import com.raspix.fabric.cobble_contests.network.CB.CBUpdateContestInfo;
import com.raspix.fabric.cobble_contests.network.NetworkablePokemonData;
import com.raspix.fabric.cobble_contests.pokemon.CVs;
import com.raspix.fabric.cobble_contests.pokemon.Ribbons;
import com.raspix.fabric.cobble_contests.util.data.ContestLevel;
import com.raspix.fabric.cobble_contests.util.data.ContestType;
import kotlin.Unit;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.locale.Language;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import com.cobblemon.mod.common.api.pokemon.PokemonPropertyExtractor;

import java.util.*;

import static com.cobblemon.mod.common.util.LocalizationUtilsKt.lang;
import static com.cobblemon.mod.common.util.MiscUtilsKt.cobblemonResource;

/**
 * This is some really bad code, so sorry to anyone looking at it
 *
 */


public class Contest {
    private UUID host;
    private int contestType; // Cool, Beauty, Cute, Clever, Tough
    private int contestTier; // "Normal", "Super", "Hyper", "Ultra", "Master" Only used for ranked matches
    private ScheduledActionManager scheduledActionManager;
    private ItemStack reward;
    private Map<UUID, Contestant> contestants = new HashMap<>();
    private ArrayList<UUID> contestantsOrdered;
    private ContestPhase round;
    private float timer;
    private int timerInt;
    private int contestantIdx;

    private static int TICKS_PER_SECOND = 20;
    //time for each phase in seconds
    private static int LOBBY_TIMEOUT = 60; // The amount of time a hosted lobby can be idle before it times out and gets deleted
    private static int WAITING_TIME = 5; // The time the contestents have to get ready for the contest to start
    private static int DRESSUP_TIME = 10; // The time a player has to choose stickers, should be 60 sec
    private static int RESULTS_TIME = 10; // The time the player can see the results before they are released from the contest
    private static int SEND_OUT_TIMER = 3; // The time in between each pokemon getting sent out
    private static int TEMP_TALENT_TIME = 20; // The placeholder timer for players in the talent portion of the contest
    private static int SHOWCASE_ROUND_TIME = 30; // The max time to choose moves
    private static int SHOWCASE_PER_CONTESTANT = 5; // The time for each contestant to showcase their moves

    private static int NUM_SHOWCASE_ROUNDS = 2;

    private static int MAX_CONTESTANTS = 4;

    private static int[][] INTRO_HEARTS = new int[][]{ // Max 8 hearts
            {0, 11, 21, 31, 41, 51, 61, 71, 81}, // Normal
            {0, 91, 111, 131, 151, 171, 191, 211, 231}, // Super
            {0, 171, 201, 231, 261, 291, 321, 351, 381}, // Hyper
            {0, 321, 361, 401, 441, 481, 521, 561, 601}, // Ultra TODO
            {0, 321, 361, 401, 441, 481, 521, 561, 601} // Master
    };

    int showcaseRound = 0; // Which round of showcase moves it is
    boolean roundReady; // are all moves chosen
    boolean runningRound;

    /**private static final HashMap<ContestType, List<ContestType>> oppositeType = new HashMap<>(){{
        put(ContestType.Beauty, Arrays.asList(ContestType.Tough, ContestType.Smart));
        put(ContestType.Cool, Arrays.asList(ContestType.Cute, ContestType.Smart));
        put(ContestType.Smart, Arrays.asList(ContestType.Beauty, ContestType.Cool));
        put(ContestType.Cute, Arrays.asList(ContestType.Cool, ContestType.Tough));
        put(ContestType.Tough, Arrays.asList(ContestType.Cute, ContestType.Beauty));
        put(ContestType.None, Arrays.asList(ContestType.None, ContestType.None));
    }};*/

    /** 10 sec to explain,
     * Intro
     *      -5 sec explain
     *      -60 sec choices
     *      -5 sec "done"
     *      -time for all out
     */

    /**public Contest(UUID hostId, int contestType, int contestTier, ItemStack reward){
        this.host = hostId;
        this.contestType = contestType;
        this.contestTier = contestTier;
        this.reward = reward;
        this.contestants = new HashMap<>();
        this.round = ContestPhase.WAITING;
        this.roundReady = false;
    }*/

    public Contest(MinecraftServer server, UUID hostId, int contestType, int contestTier, ItemStack reward, boolean hostParticipate, UUID pokeIdx){
        this.host = hostId;
        this.contestType = contestType;
        this.contestTier = contestTier;
        this.reward = reward;
        this.contestants = new HashMap<>();
        this.contestantsOrdered = new ArrayList<>();
        this.round = ContestPhase.IDLE;
        this.contestantIdx = 0;
        this.scheduledActionManager = new ScheduledActionManager();
        addHostAsContestant(server, hostId, pokeIdx);
        //StartContest();
    }


    public class Contestant{
        private UUID player;
        private UUID pokemon; //not sure what to reference here
        private int hearts;
        private ClientBattleMessageQueue contestMessages;


        String currentMove;
        String lastMove;
        int cumulativeHearts;
        int turnHearts;
        int turnJam;


        public Contestant(UUID player, UUID pokemon){
            this.player = player;
            this.pokemon = pokemon;
            this.hearts = 0;
            this.contestMessages = new ClientBattleMessageQueue();
            this.currentMove = "";
            this.lastMove = "";
            this.cumulativeHearts = 0;
            this.turnHearts = 0;
            this.turnJam = 0;

        }


        // Getters and Setters for player
        public UUID getPlayer() {
            return player;
        }

        public void setPlayer(UUID player) {
            this.player = player;
        }

        // Getters and Setters for pokemon
        public UUID getPokemon() {
            return pokemon;
        }

        public void setPokemon(UUID pokemon) {
            this.pokemon = pokemon;
        }

        // Getters and Setters for hearts
        public int getHearts() {
            return hearts;
        }

        public void setHearts(int hearts) {
            this.hearts = hearts;
        }

        public void setTurnHearts(int hearts) {
            this.turnHearts = hearts;
        }

        public int getTurnHearts(){
            return turnHearts;
        }

        public void addHearts(int hearts){
            this.hearts += hearts;
            System.out.println("has " + hearts + " hearts");
        }


        public boolean setMove(String newMove){
            if(currentMove.isEmpty()){
                this.currentMove = newMove;
                return true;
            }
            return false;
        }

        public boolean setRandomMove(MinecraftServer server){
            PlayerList playerList = server.getPlayerList();

            if(currentMove.isEmpty()){

                ServerPlayer play = playerList.getPlayer(player);
                Pokemon poke = Cobblemon.INSTANCE.getStorage().getParty(play).get(pokemon);
                MoveSet moveSet = poke.getMoveSet();
                List<Move> moves = moveSet.getMoves();
                List<Move> nonNullMoves = new ArrayList<>();
                for (Move move : moves) {
                    if (move != null) {
                        nonNullMoves.add(move);
                    }
                }
                if (!nonNullMoves.isEmpty()) {
                    Random random = new Random();
                    int randomIndex = random.nextInt(nonNullMoves.size());
                    this.currentMove = nonNullMoves.get(randomIndex).getName();
                } else {
                    this.currentMove = "default";
                }
                //this.currentMove = newMove;
                return true;
            }
            return false;
        }

        public String getCurrentMove(){
            return currentMove;
        }

        public String getLastMove(){
            return lastMove;
        }

        public boolean isMoveChosen(){
            return !currentMove.isEmpty();
        }

        public void formatForNextTurn(){
            this.lastMove = currentMove;
            this.currentMove = "";
            addHearts(Math.max(turnHearts-turnJam, 0));
            this.turnHearts = 0;
            this.turnJam = 0;
        }

        /**private void useMove(String usingMove, ContestType type){
            ContestMoves.MoveData moveData = ContestMoves.instance.getMoveData(usingMove);
            ContestMoves.FunctionData functionData = ContestMoves.instance.ALL_FUNCTION_DATA.get(moveData.getFunctionType());

            int typeMod = 0;
            if(type.equals(moveData.getType())){
                typeMod = 1;
            }else if(type.equals(oppositeType.get(type).getFirst()) || type.equals(oppositeType.get(type).get(1))){
                typeMod = -1;
            }

            this.turnHearts = functionData.getAppeal() + typeMod;

        }

        private void useMove(ContestType type){
            ContestMoves.MoveData moveData = ContestMoves.instance.getMoveData(currentMove);
            ContestMoves.FunctionData functionData = ContestMoves.instance.ALL_FUNCTION_DATA.get(moveData.getFunctionType());

            int typeMod = 0;
            if(type.equals(moveData.getType())){
                typeMod = 1;
            }else if(type.equals(oppositeType.get(type).getFirst()) || type.equals(oppositeType.get(type).get(1))){
                typeMod = -1;
            }

            this.turnHearts = functionData.getAppeal() + typeMod;

            //functionData.onUse(this);

        }*/

        private void applyTurn(){
            this.cumulativeHearts += turnHearts;
            formatForNextTurn();
        }

    }

    // TODO: look into combo moves

    public class ContestantShowcaseState{

        Contest contest;
        ContestType type = ContestType.None;



        int showcaseRound;


        String currentMove;
        String lastMove;
        int cumulativeHearts;
        int turnHearts;
        int turnJam;

        public ContestantShowcaseState(Contest contest){
            this.contest = contest;
        }



        /**private void useMove(String usingMove){
            ContestMoves.MoveData moveData = ContestMoves.instance.getMoveData(usingMove);
            ContestMoves.FunctionData functionData = ContestMoves.instance.ALL_FUNCTION_DATA.get(moveData.getFunctionType());

            ContestType moveType = moveData.getType();

            int typeMod = 0;
            if(this.type.equals(moveType)){
                typeMod = 1;
            }else if(moveType.equals(oppositeType.get(this.type).getFirst()) || moveType.equals(oppositeType.get(this.type).get(1))){
                typeMod = -1;
            }
            System.out.println("This contest type is " + this.type.name() + " with opposites of " + oppositeType.get(this.type).getFirst().name() + " and " + oppositeType.get(this.type).get(1).name() + " and the move type was " + moveType.name());

            this.turnHearts = functionData.getAppeal() + typeMod;

        }*/

        private void applyTurn(){
            this.cumulativeHearts += turnHearts;
            this.turnHearts = 0;
        }


    }


    public class ShowcaseHelper{


        private List<UUID> contestantsOrderer;
        private int applause; // 0-5, goes up when type move is used

    }

    public enum ContestPhase{
        IDLE, // The time before the start
        WAITING, // The few seconds at the start for contestents to get ready & in position
        DRESSUP, // player selects stickers
        INTRODUCTION, // the pokemon are sent out with particle effects and introduced
        TALENT, // moves
        RESULTS, // results
        ENDING; // The contest has ended and needs a different state


        // Serialize the enum to a String
        public String serialize() {
            return this.name();
        }

        // Deserialize a String to the enum value
        public static ContestPhase deserialize(String name) {
            return ContestPhase.valueOf(name);
        }

        // Store the enum in a CompoundTag
        public void toTag(CompoundTag tag, String key) {
            tag.putString(key, serialize());
        }

        // Retrieve the enum from a CompoundTag
        public static ContestPhase fromTag(CompoundTag tag, String key) {
            return deserialize(tag.getString(key));
        }
    }


    public boolean contestantPickMove(UUID playerId, String moveName){
        //System.out.println("Player picked a move");
        if(!contestants.containsKey(playerId) || roundReady){
            return false;
        }
        Contestant con = contestants.get(playerId);
        if(con.setMove(moveName)){
            checkIfMoveReady();
            return true;
        }
        return false;
    }

    public void checkIfMoveReady(){
        boolean ready = true;
        for(Contestant contestant: contestants.values()){
            if(!contestant.isMoveChosen()){
                ready = false;
                break;
            }
        }
        roundReady = ready;
        System.out.println("All contestants chosen?: " + roundReady);
    }

    public void runMoves(){

        for(UUID contestantID: contestantsOrdered){
            Contestant contestant = contestants.get(contestantID);
            //contestant.useMove(ContestType.Beauty);
        }
    }

    public void runContestantMove(MinecraftServer server, Contestant contestant){
        //contestant.useMove(ContestType.getFromInt(contestType));
        ContestMoves.MoveData moveData = ContestMoves.instance.getMoveData(contestant.currentMove);
        ContestMoves.FunctionData functionData = ContestMoves.instance.getFunctionDataFromName(moveData.getFunctionType());

        functionData.onUse(server,this, contestant);

        sendEveryoneContestants(server);

    }

    public void applyMoves(MinecraftServer server){

        for(UUID contestantID: contestantsOrdered){
            Contestant contestant = contestants.get(contestantID);
            contestant.applyTurn();
        }
        sendEveryoneContestants(server);

        roundReady = false;
    }


    public CompoundTag generateContestantDataTag(MinecraftServer server){
        CompoundTag tag = new CompoundTag();

        PlayerList playerList = server.getPlayerList();
        tag.putInt("contestants_size", contestants.size());



        for(int i = 0; i < contestantsOrdered.size(); i ++){
            UUID contestantID = contestantsOrdered.get(i);
            Contestant contestant = contestants.get(contestantID);
            ServerPlayer serverPlayer = playerList.getPlayer(contestantID);

            Pokemon poke = Cobblemon.INSTANCE.getStorage().getParty(serverPlayer).get(contestant.pokemon);

            NetworkablePokemonData data = new NetworkablePokemonData(
                    poke.getUuid(),
                    contestantID,
                    poke.getDisplayName().getString(),
                    serverPlayer.getDisplayName().getString(),
                    0, 0,
                    contestant.getTurnHearts(),
                    0,
                    poke.createPokemonProperties(PokemonPropertyExtractor.SPECIES, PokemonPropertyExtractor.GENDER, PokemonPropertyExtractor.SHINY, PokemonPropertyExtractor.FORM),
                    poke.getAspects());

            //data.getAsBuf(buf);
            tag.put("contestant" + i, data.getAsTag());
        }

        return tag.copy();
    }

    private void sendEveryoneContestants(MinecraftServer server){// TODO: send out packet here?

        PlayerList playerList = server.getPlayerList();

        CompoundTag tag = generateContestantDataTag(server);

        for(UUID contestantID: contestantsOrdered){ // TODO: should send to all viewers
            ServerPlayer serverPlayer = playerList.getPlayer(contestantID);

            if(serverPlayer != null){
                ServerPlayNetworking.send(serverPlayer, new CBSendContestantStatus(serverPlayer.getUUID(), tag.copy()));
            }
        }
    }

    /**
     * When a player reloads their screen by opening it or resizing it
     */
    public void sendPlayerContestants(MinecraftServer server, ServerPlayer serverPlayer){
        CompoundTag tag = generateContestantDataTag(server);

        if(!round.equals(ContestPhase.IDLE)) {

            if (serverPlayer != null) {
                ServerPlayNetworking.send(serverPlayer, new CBSendContestantStatus(serverPlayer.getUUID(), tag.copy()));
            }
        }

    }


    public void addContestantMessage(MinecraftServer server, ChatFormatting optionalColor, String transLine, Object ... objects){
        PlayerList playerList = server.getPlayerList();


        for(UUID contestantID: contestantsOrdered){
            ServerPlayer serverPlayer = playerList.getPlayer(contestantID);
            MutableComponent line = Component.translatable(transLine, objects).copy().withStyle(ChatFormatting.BOLD);//.withStyle(CobblemonResources.INSTANCE.getDEFAULT_LARGE());
            if(optionalColor != null){
                line = line.withStyle(optionalColor);
            }
            Component finalLine = line;
            if(serverPlayer != null){
                ServerPlayNetworking.send(serverPlayer, new CBSendContestantMessage(contestantID, new ArrayList<>(){{add(finalLine);}}));
            }

            //contestants.get(contestantID).contestMessages.add(new ArrayList<>(Collections.singletonList(Component.literal(line).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.LIGHT_PURPLE).getVisualOrderText())));
        }
    }


    public void addContestantMessage1(String transLine, Object ... objects){
        for(UUID contestantID: contestantsOrdered){
            Font textRenderer = Minecraft.getInstance().font;
            Component line = Component.translatable(transLine, objects).copy().withStyle(ChatFormatting.BOLD);//.withStyle(CobblemonResources.INSTANCE.getDEFAULT_LARGE());
            List<FormattedCharSequence> lines = Language.getInstance().getVisualOrder(textRenderer.getSplitter().splitLines(line, ContestMessagePane.LINE_WIDTH, line.getStyle()));
            contestants.get(contestantID).contestMessages.add(lines);
            //contestants.get(contestantID).contestMessages.add(new ArrayList<>(Collections.singletonList(Component.literal(line).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.LIGHT_PURPLE).getVisualOrderText())));
        }
    }

    public void addContestantMessage(Component com){
        for(UUID contestantID: contestantsOrdered){
            Font textRenderer = Minecraft.getInstance().font;
            Component line = com.copy().withStyle(ChatFormatting.BOLD);//.withStyle(CobblemonResources.INSTANCE.getDEFAULT_LARGE());
            List<FormattedCharSequence> lines = Language.getInstance().getVisualOrder(textRenderer.getSplitter().splitLines(line, ContestMessagePane.LINE_WIDTH, line.getStyle()));
            contestants.get(contestantID).contestMessages.add(lines);
            //contestants.get(contestantID).contestMessages.add(new ArrayList<>(Collections.singletonList(Component.literal(line).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.LIGHT_PURPLE).getVisualOrderText())));
        }
    }

    public boolean isPlayerHost(UUID playerID){
        //System.out.println("Player " + playerID + " is checked against host " + host + " and is " + playerID.equals(host));
        return playerID.equals(host);
    }

    public void update(float timeChange, MinecraftServer server) {


        scheduledActionManager.update();


        if(round == ContestPhase.WAITING && timer == 0f){
            addContestantMessage(server, null, "cobble_contests.contest_showoff.start", ContestLevel.getFromInt(contestTier).name(), ContestType.getFromInt(contestType).name());
        }
        //System.out.println("Contest Time: " + timer);

        if(!(round == ContestPhase.ENDING)){
            timer += timeChange;// * TICKS_PER_SECOND;//Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
        }


        if(round == ContestPhase.IDLE && timer >= LOBBY_TIMEOUT * TICKS_PER_SECOND) {
            System.out.println("Contest Lobby Timed Out");
            // Should notify anyone who was in the lobby
            round = ContestPhase.ENDING;
            updateContestants(server);
            TimeoutContest(server);

        }else if(round == ContestPhase.WAITING && timer >= WAITING_TIME * TICKS_PER_SECOND){
            System.out.println("Moved to Dressup phase");
            round = ContestPhase.DRESSUP;
            timer = 0;
            timerInt = 0;
            updateContestants(server);
        }else if(round == ContestPhase.DRESSUP){
            if(timerInt != getTimer()){
                updateContestants(server);
                timerInt = getTimer();
            }
            if(timer >= DRESSUP_TIME * TICKS_PER_SECOND){
                System.out.println("Moved to INTRODUCTION phase");
                round = ContestPhase.INTRODUCTION;
                timer = 0;
                updateContestants(server);
                evaluateIntroductionPoints(server);
                addContestantMessage(server, null, "And that's time! Now to meet the contestants!\n");
            }
        }else if (round == ContestPhase.RESULTS && timer >= RESULTS_TIME * TICKS_PER_SECOND){
            System.out.println("Finished Contest");
            round = ContestPhase.ENDING;
            updateContestants(server);
            EndContest(server);
        }else if(round == ContestPhase.INTRODUCTION){
            if(timer >= ((contestantIdx * SEND_OUT_TIMER + 2) * TICKS_PER_SECOND)){
                PlayerList playerList = server.getPlayerList();
                Contestant contestant = contestants.get(contestantsOrdered.get(contestantIdx));
                ServerPlayer player = playerList.getPlayer(contestant.player);

                assert player != null;
                Pokemon poke = Cobblemon.INSTANCE.getStorage().getParty(player).get(contestant.pokemon);

                assert poke != null;
                //addContestantMessage(player.getDisplayName().getString() + " entered " + poke.getDisplayName().getString() + " the " + poke.getSpecies().getName());
                addContestantMessage(server, null, "cobble_contests.contest_showoff.intro", player.getDisplayName().getString(), poke.getDisplayName().getString(), poke.getSpecies().getName());
                //addContestantMessage(Component.translatable("cobble_contests.contest_showoff.intro", player.getDisplayName().getString(), poke.getDisplayName().getString(), poke.getSpecies().getName()));

                sendOutPokemon(server, contestantIdx);
                this.contestantIdx += 1;
                if(contestantIdx >= contestants.size()){
                    System.out.println("Finished Introduction");
                    timer = 0;
                    round = ContestPhase.TALENT;
                    this.showcaseRound = 0;
                    updateContestants(server);
                    sendEveryoneContestants(server);
                }
            }
        }else if(round == ContestPhase.TALENT){
            if(timerInt != getTimer()){
                updateContestants(server);
                timerInt = getTimer();
            }
            if(runningRound){

                if(timer >= (2 + (contestantIdx * SHOWCASE_PER_CONTESTANT)) * TICKS_PER_SECOND) {// For each contestant to do their moves

                    if (contestantIdx < contestantsOrdered.size()) {

                        System.out.println("Move being performed");
                        PlayerList playerList = server.getPlayerList();
                        Contestant contestant = contestants.get(contestantsOrdered.get(contestantIdx));
                        ServerPlayer player = playerList.getPlayer(contestant.player);

                        assert player != null;
                        Pokemon poke = Cobblemon.INSTANCE.getStorage().getParty(player).get(contestant.pokemon);

                        assert poke != null;
                        addContestantMessage(server, null, "cobble_contests.contest_showoff.move_used", poke.getDisplayName().getString(), lang("move." + contestant.getCurrentMove()));

                        runContestantMove(server, contestants.get(contestantsOrdered.get(contestantIdx)));
                    }
                    contestantIdx += 1;

                }

                if (contestantIdx > contestantsOrdered.size()) { // When all contestants have gone
                    System.out.println("All moves performed");
                    timer = 0;
                    runningRound = false;
                    showcaseRound += 1;
                    roundReady = false;
                    contestantIdx = 0;
                    applyMoves(server);
                    reorderContestants();
                    updateContestantsWithRound(server);
                }

                updateContestants(server);

            }else if(roundReady || timer >= SHOWCASE_ROUND_TIME * TICKS_PER_SECOND){ // End of move choice

                if(!roundReady){
                    selectMovesForMissingContestants(server);
                }

                addContestantMessage(server, null,"cobble_contests.contest_showoff.start_round", showcaseRound);

                System.out.println("Move Choice Done");
                runningRound = true;
                roundReady = false;
                timer = 0;
                contestantIdx = 0;
                updateContestants(server);



            }
            //if(timer >= (TEMP_TALENT_TIME * TICKS_PER_SECOND)){
            if(showcaseRound >= 2){ // Ends showcase
                System.out.println("Finished Talent");
                round = ContestPhase.RESULTS;
                timer = 0;
                updateContestants(server);
                if(contestTier != ContestLevel.Multiplayer.getIntValue()){
                    for(UUID id: contestants.keySet()){
                        evaluateRankedWinConditions(server, id);
                    }

                }else {

                }

            }

        }


    }

    public void selectMovesForMissingContestants(MinecraftServer server){
        for(Contestant contestant: contestants.values()){
            if(!contestant.isMoveChosen()){
                contestant.setRandomMove(server);
            }
        }
    }

    public void scheduleAction(Runnable action, long timeTil){
        scheduledActionManager.scheduleAction(action, timeTil);
    }

    public void reorderContestants(){

    }


    private void sendOutPokemon(MinecraftServer server, int contestantIndex){
        PlayerList playerList = server.getPlayerList();

        Contestant contestant = contestants.get(contestantsOrdered.get(contestantIndex));

        //for(Contestant contestant: contestants.values()) {
            ServerPlayer play = playerList.getPlayer(contestant.player);
            Pokemon poke = Cobblemon.INSTANCE.getStorage().getParty(play).get(contestant.pokemon);
            if(poke.getEntity() == null) {
                Vec3 position = null;//play.raycastSafeSendout(poke, 12.0, 5.0, ClipContext.Fluid.ANY);
                if (position != null) {
                    poke.sendOutWithAnimation(play, play.serverLevel(), position, null, true, null, pokemonEntity -> {
                        return Unit.INSTANCE;
                    });
                } else {
                    poke.sendOutWithAnimation(play, play.serverLevel(), play.position(), null, true, null, pokemonEntity -> {
                        return Unit.INSTANCE;
                    });
                }
            }else{
                //play cry animation
            }

            //SnowstormParticleReader.INSTANCE.loadEffect()

            PokemonEntity pokeEnt = poke.getEntity();
            //ServerPlayNetworking.send(play, new CBSendPlayersParticles(play.getId(), "rainbow", pokeEnt.position().toVector3f()));
            new SpawnSnowstormParticlePacket(cobblemonResource("snow_swirl"), pokeEnt.position())
                    .sendToPlayersAround(pokeEnt.getX(), pokeEnt.getY(), pokeEnt.getZ(), 64.0, pokeEnt.level().dimension(), serverPlayer -> {
                        return false;
                    });
            /**new SpawnSnowstormEntityParticlePacket(cobblemonResource("rainbow"), play.getId(), Arrays.asList())
                    .sendToPlayersAround(pokeEnt.getX(), pokeEnt.getY(), pokeEnt.getZ(), 64.0, pokeEnt.level().dimension(), serverPlayer -> {
                        return false;
                    });//ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "loading.png")*
            /**new SpawnSnowstormEntityParticlePacket(cobblemonResource("shiny_ring"), it.getId(), Arrays.asList("shiny_particles", "middle"))
                    .sendToPlayersAround(it.getX(), it.getY(), it.getZ(), 64.0, it.level().dimension(), serverPlayer -> {
                        return false;
                    });*/

        //}

        addContestantMessage(server, ChatFormatting.AQUA, "Wow, the audience seems to really like this pokemon");
    }

    private void updateContestants(MinecraftServer server){
        PlayerList playerList = server.getPlayerList();
        for(Contestant conts: contestants.values()){
            CompoundTag tag = new CompoundTag();
            tag.putUUID("index", conts.pokemon);
            round.toTag(tag, "phase");
            tag.putInt("seconds", getTimer());

            //tag.putInt("showcase_round", showcaseRound);
            //tag.putBoolean("can_choose_move", getCanChooseMove());
            ServerPlayer play = playerList.getPlayer(conts.player);
            if(play != null){
                ServerPlayNetworking.send(play, new CBUpdateContestInfo(conts.player, tag));
            }
        }
    }

    private void updateContestantsWithRound(MinecraftServer server){
        PlayerList playerList = server.getPlayerList();
        for(Contestant conts: contestants.values()){
            CompoundTag tag = new CompoundTag();
            tag.putUUID("index", conts.pokemon);
            round.toTag(tag, "phase");
            tag.putInt("seconds", getTimer());

            tag.putInt("showcase_round", showcaseRound);
            tag.putBoolean("can_choose_move", getCanChooseMove());
            ServerPlayer play = playerList.getPlayer(conts.player);
            if(play != null){
                ServerPlayNetworking.send(play, new CBUpdateContestInfo(conts.player, tag));
            }
        }
    }

    public int getShowcaseRound(){
        return showcaseRound;
    }

    public boolean getCanChooseMove(){
        return !runningRound;
    }

    public int getTimer(){
        return (int)(timer/20);
    }



    public void addHostAsContestant(MinecraftServer server, UUID uuid, UUID pokeIdx){
        contestants.put(uuid, new Contestant(uuid, pokeIdx));
        contestantsOrdered.add(uuid);
        updateAllContestantLobbies(server);
    }

    public boolean addContestants(MinecraftServer server, ServerPlayer player, UUID uuid, UUID pokeIdx){
        if(contestants.size() >= MAX_CONTESTANTS){
            // TODO let player know there are too many
            System.out.println("too many contestants already");
            return false;
        }
        contestants.put(uuid, new Contestant(uuid, pokeIdx));
        contestantsOrdered.add(uuid);
        CompoundTag tag = generateContestantDataTag(server);
        ServerPlayNetworking.send((ServerPlayer) player, new CBLobRetReq(uuid, tag));
        updateAllContestantLobbies(server);
        return true;
    }

    public void removeContestants(MinecraftServer server, ServerPlayer player, UUID uuid){
        contestants.remove(uuid);
        // TODO: packet that updates removed player's screen and notifies them
        //ServerPlayNetworking.send((ServerPlayer) player, new CBLobRetReq(uuid));
        updateAllContestantLobbies(server);
    }

    public void updateAllContestantLobbies(MinecraftServer server){
        PlayerList playerList = server.getPlayerList();

        CompoundTag tag = generateContestantDataTag(server);
        for(UUID contestantID: contestants.keySet()){
            if( playerList.getPlayer(contestantID) != null){
                ServerPlayer player = playerList.getPlayer(contestantID);
                ServerPlayNetworking.send((ServerPlayer) player, new CBSendContestantStatus(contestantID, tag));
            }

        }
    }

    public UUID getHost(){
        return host;
    }

    public ContestPhase getRound(){
        return round;
    }

    public boolean AddContestant(UUID contestantID, int pokemon){
        //check if contestant is already in a contest
        //check if pokemon exists
        //check if pokemon has high enough tier
        //add new contestant
        //add to list of contestant uuids in ContestManageer
        return false;
    }

    /**
     * Should be used to start an already existing contest that has a lobby
     * @param playerID the assumed host
     * @return if the contest could be started
     */
    public boolean startContest(UUID playerID){
        if(playerID.equals(host)) {
            timer = 0L;
            this.contestantsOrdered = new ArrayList<>(contestants.keySet());
            this.round = ContestPhase.WAITING;
            ContestManager.INSTANCE.startContest(this);
            return true;
        }
        return false;
    }

    public UUID getContestentPokemon(UUID uuid){
        return contestants.get(uuid).getPokemon();
    }

    public Map<UUID, Contestant> getContestants(){
        return contestants;
    }

    public boolean EndContest(MinecraftServer server){

        ContestManager.INSTANCE.EndContest(this, server);
        return false;
    }

    public void TimeoutContest(MinecraftServer server){

        ContestManager.INSTANCE.TimeoutContest(this, server);
    }

    public void evaluateIntroductionPoints(MinecraftServer server){
        PlayerList playerList = server.getPlayerList();
        for(Contestant contestant: contestants.values()){
            ServerPlayer play = playerList.getPlayer(contestant.player);
            if(play != null) {
                Pokemon poke = Cobblemon.INSTANCE.getStorage().getParty(play).get(contestant.pokemon);
                CVs cvs = CVs.getFromTag(poke.getPersistentData().getCompound("CVs"));
                int totalPoints = 0;
                for(int i = 0; i < 6; i ++){ // goes through all 5 conditions and sheen
                    totalPoints += (int) (cvs.getConditionFromIdx(i) * (i == contestType? 1f : 0.5f));
                }

                int hearts = getNumHearts( totalPoints);
                contestant.addHearts(hearts);

                System.out.println("Player " + play.getDisplayName() + " won " + hearts + "hearts");
            }
        }
    }



    /**
     * For ranked competition checking
     */
    public boolean evaluateRankedWinConditions(MinecraftServer server, UUID uuid){


        Contestant contestant = contestants.get(uuid);
        int totalHearts = contestant.getHearts();

        boolean result = false;
        if (contestTier < 5 &&
            totalHearts >= thresholds[contestTier]){
            result = true;
        }

        ServerPlayer player = server.getPlayerList().getPlayer(uuid);


        Pokemon pokemon = Cobblemon.INSTANCE.getStorage().getParty(player).get(contestant.pokemon);

        addContestantMessage(server, ChatFormatting.GOLD, "cobble_contests.contest_showoff.num_hearts", pokemon.getDisplayName(), totalHearts);

        Component componentOutput;

        if (result) {
            componentOutput = Component.translatable("cobble_contests.contest_result.won_ranked", pokemon.getDisplayName(), ContestLevel.getFromInt(contestTier).name(), getContestTypeString(contestType)).withStyle(ChatFormatting.LIGHT_PURPLE);
        } else {
            componentOutput = Component.translatable("cobble_contests.contest_result.lost_ranked", pokemon.getDisplayName(), ContestLevel.getFromInt(contestTier).name(), getContestTypeString(contestType)).withStyle(ChatFormatting.LIGHT_PURPLE);
        }

        if(result){
            awardRibbon(player, pokemon);
        }

        sendClientChatMessage(server, uuid, componentOutput);

        return result;

    }

    /**
     * For multiplayer competition checking
     */
    public void evaluateRankedWinConditions(MinecraftServer server){


    }

    public void awardRibbon(ServerPlayer player, Pokemon pokemon){
        Ribbons ribbons = Ribbons.getFromTag(pokemon.getPersistentData().getCompound("Ribbons"));
        switch (contestType) {
            case 0:
                ribbons.setRankedCool(contestTier, true);
                break;
            case 1:
                ribbons.setRankedBeauty(contestTier, true);
                break;
            case 2:
                ribbons.setRankedCute(contestTier, true);
                break;
            case 3:
                ribbons.setRankedSmart(contestTier, true);
                break;
            case 4:
                ribbons.setRankedTough(contestTier, true);
                break;
            default:
                break;
        }
        Map<String, CompoundTag> myData = new HashMap<String, CompoundTag>() {};
        myData.put("Ribbons", ribbons.saveToNBT());
        saveRibbons(pokemon, myData);
    }

    private void sendClientChatMessage(MinecraftServer server, UUID uuid, Component componentOutput){
        ServerPlayer player = server.getPlayerList().getPlayer(uuid);
        if (!player.level().isClientSide()) {
            player.displayClientMessage(componentOutput, false);
        }
    }

    private void sendClientChatMessage(ServerPlayer player, Component componentOutput){
        if (!player.level().isClientSide()) {
            player.displayClientMessage(componentOutput, false);
        }
    }

    private int getNumHearts(int points){
        for(int i = 1; i < 9; i ++){
            if(points < INTRO_HEARTS[contestTier < 0? 4:contestTier][i]){
                return i - 1;
            }
        }
        return 8;
    }

    private void saveRibbons(final Pokemon pokemon, final Map<String, CompoundTag> myData) {
        final CompoundTag tag = pokemon.getPersistentData();
        myData.forEach((key, value) -> {
            tag.put(key, value);
        });
        // basically a vanilla "markAsDirty"
        if (pokemon.getChangeObservable() instanceof SimpleObservable<Pokemon>) { //TODO
            ((SimpleObservable<Pokemon>) pokemon.getChangeObservable()).emit(pokemon);
        }else {
            System.out.println("error, not simple observable (ContestBlockEntity)");
        }
    }

    public String getContestTypeString(int contestType){
        return switch (contestType) {
            case 0 -> "Cool";
            case 1 -> "Beauty";
            case 2 -> "Cute";
            case 3 -> "Smart";
            case 4 -> "Tough";
            default -> "ERROR";
        };
    }

    public Component tempRunContestResults(UUID playerId){
        Component componentOutput;
        componentOutput = Component.translatable("cobble_contests.contest_result.maxed_ranked", "pokemon name", getContestTypeString(contestType)).withStyle(ChatFormatting.LIGHT_PURPLE);
        /**if(contestTier < 5) {
            Contestant playerCon = contestants.get(playerId);

            boolean result = runContest(Cobblemon.INSTANCE.getStorage().getParty(player).get(pokeIdx));

            if (result) {
                componentOutput = Component.translatable("cobble_contests.contest_result.won_ranked", pokeName, getContestLevelString(contestLevel), getContestTypeString(contestType)).withStyle(ChatFormatting.LIGHT_PURPLE);
                //contestOutput = pokeName + " Won the " + getContestLevelString(contestLevel) + " " + getContestTypeString(contestType) + " Contest";
            } else {
                componentOutput = Component.translatable("cobble_contests.contest_result.lost_ranked", pokeName, getContestLevelString(contestLevel), getContestTypeString(contestType)).withStyle(ChatFormatting.LIGHT_PURPLE);
                //contestOutput = pokeName + " Lost the " + getContestLevelString(contestLevel) + " " + getContestTypeString(contestType) + " Contest";
            }
        }else{
            componentOutput = Component.translatable("cobble_contests.contest_result.maxed_ranked", pokeName, getContestTypeString(contestType)).withStyle(ChatFormatting.LIGHT_PURPLE);
            //contestOutput = pokeName + " has already beaten all " + getContestTypeString(contestType) + " Contests";
        }*/
        return componentOutput;
    }

    public int getContestLevel() {
        return contestTier;
    }

    private boolean runContest(Pokemon poke) {
        boolean result = false;
        CVs cvs = CVs.getFromTag(poke.getPersistentData().getCompound("CVs"));
        Ribbons ribbons = Ribbons.getFromTag(poke.getPersistentData().getCompound("Ribbons"));
        switch (contestType) {
            case 0:
                if(runAppContest(poke, cvs.getCool())) {
                    ribbons.setRankedCool(contestTier, true);
                    result = true;
                }
                break;
            case 1:
                if(runAppContest(poke, cvs.getBeauty())) {
                    ribbons.setRankedBeauty(contestTier, true);
                    result = true;
                }
                break;
            case 2:
                if(runAppContest(poke, cvs.getCute())) {
                    ribbons.setRankedCute(contestTier, true);
                    result = true;
                }
                break;
            case 3:
                if(runAppContest(poke, cvs.getSmart())) {
                    ribbons.setRankedSmart(contestTier, true);
                    result = true;
                }
                break;
            case 4:
                if(runAppContest(poke, cvs.getTough())) {
                    ribbons.setRankedTough(contestTier, true);
                    result = true;
                }
                break;
            default:
                break;
        }
        Map<String, CompoundTag> myData = new HashMap<String, CompoundTag>() {};
        myData.put("Ribbons", ribbons.saveToNBT());
        saveRibbons(poke, myData);
        return result;
    }

    private int[] thresholds = {5, 40, 100, 175, 245};

    private boolean runAppContest(Pokemon poke, int typeVal){
        boolean result = false;
        if (contestTier < 5 &&
                typeVal >= thresholds[contestTier]){
            result = true;
        }
        return result;
    }

    public ContestType getContestType(){
        // TODO: make it return right type
        return ContestType.getFromInt(contestType);//ContestType.Beauty; // contestType;
    }

    public int getNumContestants(){
        return contestants.size();
    }
}
