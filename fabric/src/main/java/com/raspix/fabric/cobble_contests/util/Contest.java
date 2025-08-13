package com.raspix.fabric.cobble_contests.util;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.MoveSet;
import com.cobblemon.mod.common.api.reactive.SimpleObservable;
import com.cobblemon.mod.common.client.battle.ClientBattleMessageQueue;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.net.messages.client.animation.PlayPosableAnimationPacket;
import com.cobblemon.mod.common.net.messages.client.effect.SpawnSnowstormParticlePacket;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.authlib.GameProfile;
import com.raspix.fabric.cobble_contests.events.ContestMoves;
import com.raspix.fabric.cobble_contests.network.CB.*;
import com.raspix.fabric.cobble_contests.network.NetworkablePokemonData;
import com.raspix.fabric.cobble_contests.pokemon.CVs;
import com.raspix.fabric.cobble_contests.pokemon.Ribbons;
import com.raspix.fabric.cobble_contests.util.data.ContestLevel;
import com.raspix.fabric.cobble_contests.util.data.ContestType;
import com.raspix.fabric.cobble_contests.util.data.ParticleEffectList;
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

/**
 * This is some really bad code, so sorry to anyone looking at it
 *
 */


public class Contest {
    private UUID host;
    private int contestType; // Cool, Beauty, Cute, Clever, Tough
    private int contestTier; // "Normal", "Super", "Hyper", "Ultra", "Master" Only used for ranked matches
    private ItemStack reward;
    private Map<UUID, Contestant> contestants = new HashMap<>();
    private ArrayList<UUID> contestantsOrdered;
    private ArrayList<UUID> spectators;

    private ContestPhase round;
    private float timer;
    private int timerInt;

    private ShowcaseHelper showcaseHelper;
    private ScheduledActionManager scheduledActionManager;

    private static int TICKS_PER_SECOND = 20; //time for each phase in seconds
    private static int LOBBY_TIMEOUT = 60; // The amount of time a hosted lobby can be idle before it times out and gets deleted
    private static int WAITING_TIME = 5; // The time the contestents have to get ready for the contest to start
    private static int DRESSUP_TIME = 5; // The time a player has to choose stickers, should be 60 sec
    private static int RESULTS_TIME = 15; // The time the player can see the results before they are released from the contest
    private static int SEND_OUT_TIMER = 3; // The time in between each pokemon getting sent out
    private static int SHOWCASE_ROUND_TIME = 30; // The max time to choose moves
    private static int SHOWCASE_PER_CONTESTANT = 5; // The time for each contestant to showcase their moves

    // Should be configurable later
    private static int MAX_CONTESTANTS = 4;
    private static int NUM_SHOWCASE_ROUNDS = 3;
    private static int MAX_APPLAUSE = 5;

    private int[] thresholds = {7, 11, 15, 18, 21 };// {5, 40, 100, 175, 245};

    private static int[][] INTRO_HEARTS = new int[][]{ // Max 8 hearts
            {0, 11, 21, 31, 41, 51, 61, 71, 81}, // Normal
            {0, 91, 111, 131, 151, 171, 191, 211, 231}, // Super
            {0, 171, 201, 231, 261, 291, 321, 351, 381}, // Hyper
            {0, 221, 261, 301, 341, 381, 421, 461, 501}, // Ultra TODO
            {0, 321, 361, 401, 441, 481, 521, 561, 601} // Master
    };

    private int contestantIdx;
    int showcaseRound = 0; // Which round of showcase moves it is
    boolean roundReady; // are all moves chosen
    boolean runningRound;
    private int rankedIdx; // the index of which ranks have been revealed yet, going down
    private List<List<Contestant>> finalRankedContestantList;
    private int applause;

    /** Status effect idea
     * Burn: +Tough -Cute -Beauty
     * Freeze: +Smart -Beauty -Cool
     * Paralysis: +Cool -Cute -Smart
     * Poison: +Beauty -Tough -Smart
     * Sleep: +Cute -Cool -Tough
     */

    /** 10 sec to explain,
     * Intro
     *      -5 sec explain
     *      -60 sec choices
     *      -5 sec "done"
     *      -time for all out
     */

    public Contest(MinecraftServer server, UUID hostId, int contestType, int contestTier, ItemStack reward, boolean hostParticipate, UUID pokeIdx){
        this.host = hostId;
        this.contestType = contestType;
        this.contestTier = contestTier;
        this.reward = reward;
        this.contestants = new HashMap<>();
        this.contestantsOrdered = new ArrayList<>();
        this.round = ContestPhase.IDLE;
        //this.showcaseHelper = new ShowcaseHelper(this);
        this.contestantIdx = 0;
        this.scheduledActionManager = new ScheduledActionManager();
        addHostAsContestant(server, hostId, pokeIdx);
        //StartContest();
    }


    public class Contestant{
        private UUID playerId;
        private UUID pokemon; //not sure what to reference here

        private ClientBattleMessageQueue contestMessages;

        String currentMove;
        String lastMove;

        private int cumulativeHearts; // total number of hearts earned in the showcase
        private int hearts;
        private int introHearts; // the hearts gained from the intro
        int turnHearts; // hearts gained this round of showcase
        int turnJam; // jam for this round of showcase

        private class ShowcaseTurnData{
            int turnHearts;
            int turnJam;

            public ShowcaseTurnData(){

            }

        }


        public Contestant(UUID playerId, UUID pokemon){
            this.playerId = playerId;
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
        public UUID getPlayerId() {
            return playerId;
        }

        public void setPlayerId(UUID playerId) {
            this.playerId = playerId;
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

        public void setTurnHearts(int hearts) {
            this.turnHearts = hearts;
        }

        public void addTurnHearts(int hearts) {
            this.turnHearts += hearts;
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
            //PlayerList playerList = server.getPlayerList();

            if(currentMove.isEmpty()){

                //ServerPlayer play = playerList.getPlayer(playerId);
                Pokemon poke = getPokemonFromID(server, playerId, pokemon);//Cobblemon.INSTANCE.getStorage().getParty(play).get(pokemon);
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
            System.out.println("current hearts is " + hearts + " and adding " + (turnHearts - turnJam));
            addHearts(Math.max(turnHearts-turnJam, 0));
            System.out.println("current hearts is now " + hearts );
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

        private void applyApplauseBonus(){
            // can use showcase round if that is needed
            addHearts(6);
        }

    }

    // TODO: look into combo moves


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


    // region Main Contest Logic

    /**
     * Should be used to start an already existing contest that has a lobby
     * @param playerID the assumed host
     * @return if the contest could be started
     */
    public boolean startContest(MinecraftServer server, UUID playerID){
        if(playerID.equals(host)) {
            timer = 0L;
            this.contestantsOrdered = new ArrayList<>(contestants.keySet());
            this.round = ContestPhase.WAITING;
            ContestManager.INSTANCE.startContest(this);
            updateContestantsStarting(server);
            return true;
        }
        return false;
    }

    public boolean EndContest(MinecraftServer server){

        ContestManager.INSTANCE.EndContest(this, server);
        return false;
    }

    public void TimeoutContest(MinecraftServer server){
        ContestManager.INSTANCE.TimeoutContest(this, server);
    }

    public void update(float timeChange, MinecraftServer server) {


        scheduledActionManager.update(timeChange);


        if(round == ContestPhase.WAITING && timer == 0f){
            addContestantMessage(server, null, "cobble_contests.contest_showoff.start", ContestLevel.getFromInt(contestTier).name(), ContestType.getFromInt(contestType).name());
        }

        if(!(round == ContestPhase.ENDING)){
            timer += timeChange;
        }

        if(round == ContestPhase.IDLE && timer >= LOBBY_TIMEOUT * TICKS_PER_SECOND) {
            System.out.println("Contest Lobby Timed Out");
            // Should notify anyone who was in the lobby
            round = ContestPhase.ENDING;
            updateContestants(server);
            TimeoutContest(server);

        }
        else if(round == ContestPhase.WAITING && timer >= WAITING_TIME * TICKS_PER_SECOND){
            System.out.println("Moved to Dressup phase");
            round = ContestPhase.DRESSUP;
            timer = 0;
            timerInt = 0;
            updateContestants(server);
        }
        else if(round == ContestPhase.DRESSUP){
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
        }
        else if (round == ContestPhase.RESULTS && timer >= RESULTS_TIME * TICKS_PER_SECOND){
            System.out.println("Finished Contest");
            round = ContestPhase.ENDING;

            updateContestants(server);
            EndContest(server);
        }
        else if(round == ContestPhase.INTRODUCTION){
            if(contestantIdx >= contestants.size() && timer >= ((contestantIdx * SEND_OUT_TIMER + 2) * TICKS_PER_SECOND)){
                this.contestantIdx += 1;
                System.out.println("Finished Introduction");
                timer = 0;
                round = ContestPhase.TALENT;
                this.showcaseRound = 0;
                updateContestants(server);
                sendEveryoneContestants(server);
            } else if(timer >= ((contestantIdx * SEND_OUT_TIMER + 2) * TICKS_PER_SECOND)){
                PlayerList playerList = server.getPlayerList();
                Contestant contestant = contestants.get(contestantsOrdered.get(contestantIdx));

                GameProfile profile = server.getProfileCache().get(contestant.playerId).get();
                String playerName = profile.getName();
                Pokemon poke = getPokemonFromID(server, contestant.playerId, contestant.pokemon);//Cobblemon.INSTANCE.getStorage().getParty(player).get(contestant.pokemon);

                assert poke != null;
                //addContestantMessage(player.getDisplayName().getString() + " entered " + poke.getDisplayName().getString() + " the " + poke.getSpecies().getName());
                addContestantMessage(server, null, "cobble_contests.contest_showoff.intro", playerName, poke.getDisplayName().getString(), poke.getSpecies().getName());
                //addContestantMessage(Component.translatable("cobble_contests.contest_showoff.intro", player.getDisplayName().getString(), poke.getDisplayName().getString(), poke.getSpecies().getName()));

                sendOutPokemon(server, contestantIdx);
                this.contestantIdx += 1;
                /**if(contestantIdx >= contestants.size()){
                 System.out.println("Finished Introduction");
                 timer = 0;
                 round = ContestPhase.TALENT;
                 this.showcaseRound = 0;
                 updateContestants(server);
                 sendEveryoneContestants(server);
                 }*/
            }
        }
        else if(round == ContestPhase.TALENT){

            //showcaseHelper.Update(timeChange, server, timer);

            if(timerInt != getTimer()){
                updateContestants(server);
                timerInt = getTimer();
            }
            /**if(runningRound){

                if(timer >= (2 + (contestantIdx * SHOWCASE_PER_CONTESTANT)) * TICKS_PER_SECOND) {// For each contestant to do their moves

                    if (contestantIdx < contestantsOrdered.size()) {

                        System.out.println("Move being performed");
                        PlayerList playerList = server.getPlayerList();
                        Contestant contestant = contestants.get(contestantsOrdered.get(contestantIdx));
                        ServerPlayer player = playerList.getPlayer(contestant.player);

                        assert player != null;
                        Pokemon poke = Cobblemon.INSTANCE.getStorage().getParty(player).get(contestant.pokemon);

                        assert poke != null;
                        //addContestantMessage(server, null, "cobble_contests.contest_showoff.move_used", poke.getDisplayName().getString(), lang("move." + contestant.getCurrentMove()));

                        runContestantMove(server, contestants.get(contestantsOrdered.get(contestantIdx)), poke);
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

            }else */

            if(roundReady || timer >= SHOWCASE_ROUND_TIME * TICKS_PER_SECOND){ // End of move choice

                if(!roundReady){
                    selectMovesForMissingContestants(server);
                }

                addContestantMessage(server, null,"cobble_contests.contest_showoff.start_round", showcaseRound);

                System.out.println("Move Choice Done");
                runningRound = true;
                roundReady = false;
                timer = 0;
                contestantIdx = 0;
                updateContestantsWithRound(server);

                PlayerList playerList = server.getPlayerList();
                for(int i = 0; i < contestantsOrdered.size(); i ++){

                    int finalI = i;
                    Contestant contestant = contestants.get(contestantsOrdered.get(i));
                    //ServerPlayer player = playerList.getPlayer(contestant.playerId);

                    //assert player != null;
                    Pokemon poke = getPokemonFromID(server, contestant.playerId, contestant.pokemon);//Cobblemon.INSTANCE.getStorage().getParty(player).get(contestant.pokemon);

                    assert poke != null;
                    float timeDelay = (2 + (finalI * SHOWCASE_PER_CONTESTANT)) * TICKS_PER_SECOND;
                    scheduleAction(() -> {runContestantMove(server, contestant, poke);}, timeDelay);
                }
                scheduleAction(() -> {
                    EndRound(server);}, (2 + (contestantsOrdered.size() * SHOWCASE_PER_CONTESTANT)) * TICKS_PER_SECOND);

            }
            //if(timer >= (TEMP_TALENT_TIME * TICKS_PER_SECOND)){
            /**if(showcaseRound >= NUM_SHOWCASE_ROUNDS){ // Ends showcase
                System.out.println("Finished Talent");
                round = ContestPhase.RESULTS;
                timer = 0;
                updateContestants(server);
                if(contestTier != ContestLevel.Multiplayer.getIntValue()){
                    for(UUID id: contestants.keySet()){
                        evaluateRankedWinConditions(server, id);
                    }

                }else {
                    determineContestResults(server);
                }
                addContestantMessage(server, null, "And thats the end of the Showcase Round! Lets see those results");

            }*/

        }


    }

    public void scheduleAction(Runnable action, float timeTil){
        scheduledActionManager.scheduleAction(action, timeTil);
    }

    public void SetContestRound(ContestPhase newPhase){
        round = newPhase;
        timer = 0;
    }

    public Pokemon getPokemonFromID(MinecraftServer server, UUID playerUUid, UUID pokeUUid){
        Pokemon contestParticipant = Cobblemon.INSTANCE.getStorage().getParty(playerUUid, server.registryAccess()).get(pokeUUid);
        if(contestParticipant == null){ // if was not in party
            contestParticipant = Cobblemon.INSTANCE.getStorage().getPC(playerUUid, server.registryAccess()).get(pokeUUid);
        }
        return contestParticipant;

    }

    // endregion

    // region Participant Modifiers

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

    public boolean AddContestant(UUID contestantID, int pokemon){
        //check if contestant is already in a contest
        //check if pokemon exists
        //check if pokemon has high enough tier
        //add new contestant
        //add to list of contestant uuids in ContestManageer
        return false;
    }


    // endregion

    // region Data Sending

    public CompoundTag generateContestantDataTag(MinecraftServer server){
        CompoundTag tag = new CompoundTag();

        PlayerList playerList = server.getPlayerList();
        tag.putInt("contestants_size", contestants.size());



        for(int i = 0; i < contestantsOrdered.size(); i ++){
            UUID contestantID = contestantsOrdered.get(i);
            Contestant contestant = contestants.get(contestantID);
            Pokemon poke = getPokemonFromID(server, contestantID, contestant.pokemon);//Cobblemon.INSTANCE.getStorage().getParty(serverPlayer).get(contestant.pokemon);
            ServerPlayer serverPlayer = playerList.getPlayer(contestantID);
            String playerName = "";
            if(serverPlayer != null){
                playerName = serverPlayer.getDisplayName().getString();
            }else{
                GameProfile profile = server.getProfileCache().get(contestantID).get();
                playerName = profile.getName();//"[Offline Player]";
            }

            NetworkablePokemonData data = new NetworkablePokemonData(
                    poke.getUuid(),
                    contestantID,
                    poke.getDisplayName().getString(),
                    playerName,
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

    public NetworkablePokemonData getDataFromPokemon(MinecraftServer server, UUID playerUUID, UUID pokeUuid){
        Contestant contestant = contestants.get(playerUUID);
        Pokemon poke = getPokemonFromID(server, playerUUID, pokeUuid);
        PlayerList playerList = server.getPlayerList();
        ServerPlayer serverPlayer = playerList.getPlayer(playerUUID);
        String playerName = "";
        if(serverPlayer != null){
            playerName = serverPlayer.getDisplayName().getString();
        }else{
            GameProfile profile = server.getProfileCache().get(playerUUID).get();
            playerName = profile.getName();//"[Offline Player]";
        }

        NetworkablePokemonData data = new NetworkablePokemonData(
                poke.getUuid(),
                playerUUID,
                poke.getDisplayName().getString(),
                playerName,
                0, 0,
                contestant.getTurnHearts(),
                0,
                poke.createPokemonProperties(PokemonPropertyExtractor.SPECIES, PokemonPropertyExtractor.GENDER, PokemonPropertyExtractor.SHINY, PokemonPropertyExtractor.FORM),
                poke.getAspects());

        return data;

    }

    public void sendEveryoneContestants(MinecraftServer server){

        PlayerList playerList = server.getPlayerList();

        CompoundTag tag = generateContestantDataTag(server);

        for(UUID contestantID: contestantsOrdered){ // TODO: should send to all viewers
            ServerPlayer serverPlayer = playerList.getPlayer(contestantID);

            if(serverPlayer != null){
                ServerPlayNetworking.send(serverPlayer, new CBSendContestantStatus(serverPlayer.getUUID(), tag.copy(), applause));
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
                ServerPlayNetworking.send(serverPlayer, new CBSendContestantStatus(serverPlayer.getUUID(), tag.copy(), applause));
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

    private void sendIncrementedContestantRanks(MinecraftServer server, int contestantsRank, int numHearts, int numContestants, List<Contestant> contestantsRanked){
        PlayerList playerList = server.getPlayerList();
        String result = playerList.getPlayer(contestantsRanked.get(0).playerId).getDisplayName().getString();
        for(int i = 1; i < contestantsRanked.size(); i ++){
            result += " and " + playerList.getPlayer(contestantsRanked.get(i).playerId).getDisplayName().getString();
        }
        result += " placed " + contestantsRank + " with " + numHearts + " hearts";
        System.out.println(result);


    }

    /**
     * For ranked contests if the player won or not
     * @param server
     * @param contestant
     * @param result
     * @param numHearts
     */
    private void sendConditionalContestantRankForms(MinecraftServer server, Contestant contestant, boolean result, int numHearts){
        System.out.println("Sending results for rank " + result);
        rankedIdx = 0;
        GameProfile profile = server.getProfileCache().get(contestant.playerId).get();
        String playerName = profile.getName();
        String translatableString = result ? "cobble_contests.contest_result.placed_first" :
                "cobble_contests.contest_result.failed_ranked";
        addContestantMessage(server, null, translatableString, playerName, getPokemonFromID(server, contestant.playerId, contestant.pokemon).getDisplayName().getString(), numHearts);
        updateContestantsWithRankedResults(server, contestant, result);

    }

    /**
     * For Multiplayer contests
     * @param server
     * @param contestantsRank
     * @param numHearts
     * @param contestantsRanked
     */
    private void sendIncrementedContestantRankForms(MinecraftServer server, int contestantsRank, int numHearts, List<Contestant> contestantsRanked){
        System.out.println("Sending results for rank index " + contestantsRank + " with " + contestantsRanked.size() + " of that rank");
        Contestant contestant = contestantsRanked.get(0); // TODO should eventually get all of a rank
        rankedIdx = contestantsRank;
        GameProfile profile = server.getProfileCache().get(contestant.playerId).get();
        String playerName = profile.getName();
        String translatableString = contestantsRank == 0? "cobble_contests.contest_result.placed_first" :
                (contestantsRank == 1? "cobble_contests.contest_result.placed_second":
                        (contestantsRank == 2 ? "cobble_contests.contest_result.placed_third" : "cobble_contests.contest_result.your_placement"));
        addContestantMessage(server, null, translatableString, playerName, getPokemonFromID(server, contestant.playerId, contestant.pokemon).getDisplayName().getString(), numHearts);
        for(int i = 1; i < contestantsRanked.size(); i ++){ // If more than one won a placement
            contestant = contestantsRanked.get(i);
            profile = server.getProfileCache().get(contestant.playerId).get();
            playerName = profile.getName();
            addContestantMessage(server, null, translatableString, playerName, getPokemonFromID(server, contestant.playerId, contestant.pokemon).getDisplayName().getString() + " also ", numHearts);
        }
        updateContestantsWithResults(server);

    }

    private void sendPersonalRankMessage(MinecraftServer server){
        // TODO for each contestant send a chat message with their info
        System.out.println("Sending messages");
        for(int i = 0; i < finalRankedContestantList.size(); i++){
            int numInRank = finalRankedContestantList.get(i).size();
            for(Contestant contestant: finalRankedContestantList.get(i)){
                GameProfile profile = server.getProfileCache().get(contestant.playerId).get();
                String playerName = profile.getName();
                sendClientChatMessage(server, contestant.playerId, Component.translatable("cobble_contests.contest_result.your_placement" + (numInRank > 1? "_tied": ""), getPokemonFromID(server, contestant.playerId, contestant.pokemon).getDisplayName().getString(), i + 1, contestant.getHearts()));
            }
        }
        updateContestantsWithResults(server);

    }

    private void updateContestantsWithRankedResults(MinecraftServer server, Contestant contestant, boolean didWin){
        PlayerList playerList = server.getPlayerList();
        for(Contestant conts: contestants.values()){
            CompoundTag tag = new CompoundTag();
            tag.putUUID("index", conts.pokemon);
            round.toTag(tag, "phase");
            tag.putInt("seconds", getTimer());


            if(round.equals(Contest.ContestPhase.RESULTS)){
                tag.putInt("is_ranked", 1);
                tag.putBoolean("did_win", didWin);
                CompoundTag addedTag = getDataFromPokemon(server, contestant.getPlayerId(), contestant.getPokemon()).getAsTag().copy();
                tag.put("contestant", addedTag);
            }

            //tag.putInt("showcase_round", showcaseRound);
            //tag.putBoolean("can_choose_move", getCanChooseMove());
            ServerPlayer play = playerList.getPlayer(conts.playerId);
            if(play != null){
                ServerPlayNetworking.send(play, new CBUpdateContestInfo(conts.playerId, tag));
            }
        }
    }

    private void updateContestantsWithResults(MinecraftServer server){
        PlayerList playerList = server.getPlayerList();
        for(Contestant conts: contestants.values()){
            CompoundTag tag = new CompoundTag();
            tag.putUUID("index", conts.pokemon);
            round.toTag(tag, "phase");
            tag.putInt("seconds", getTimer());


            if(round.equals(Contest.ContestPhase.RESULTS)){
                if(getContestantAtRank(0) != null){
                    List<Contest.Contestant> contestantsInRank = getContestantAtRank(0);
                    tag.putInt("num_first", contestantsInRank.size());
                    CompoundTag firstTag = new CompoundTag();
                    for(int i = 0; i < contestantsInRank.size(); i++){
                        Contest.Contestant contestant = contestantsInRank.get(i);
                        CompoundTag addedTag = getDataFromPokemon(server, contestant.getPlayerId(), contestant.getPokemon()).getAsTag().copy();
                        firstTag.put("contestant" + i, addedTag);
                    }
                    tag.put("first_rank", firstTag);

                }
            }

            //tag.putInt("showcase_round", showcaseRound);
            //tag.putBoolean("can_choose_move", getCanChooseMove());
            ServerPlayer play = playerList.getPlayer(conts.playerId);
            if(play != null){
                ServerPlayNetworking.send(play, new CBUpdateContestInfo(conts.playerId, tag));
            }
        }
    }

    private void updateContestantsStarting(MinecraftServer server){
        PlayerList playerList = server.getPlayerList();
        for(Contestant conts: contestants.values()){
            CompoundTag tag = new CompoundTag();
            tag.putBoolean("in_contest", true);
            round.toTag(tag, "phase");
            tag.putBoolean("is_host", isPlayerHost(conts.playerId));
            ServerPlayer play = playerList.getPlayer(conts.playerId);
            if(play != null){
                ServerPlayNetworking.send(play, new CBAlertContestStarting(conts.playerId, tag));
            }
        }
    }

    public void updateContestants(MinecraftServer server){
        PlayerList playerList = server.getPlayerList();
        for(Contestant conts: contestants.values()){
            CompoundTag tag = new CompoundTag();
            tag.putUUID("index", conts.pokemon);
            round.toTag(tag, "phase");
            tag.putInt("seconds", getTimer());

            //tag.putInt("showcase_round", showcaseRound);
            //tag.putBoolean("can_choose_move", getCanChooseMove());
            ServerPlayer play = playerList.getPlayer(conts.playerId);
            if(play != null){
                ServerPlayNetworking.send(play, new CBUpdateContestInfo(conts.playerId, tag));
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
            tag.putBoolean("all_moves_picked", getRunningRound());
            tag.putInt("applause", getApplause());
            ServerPlayer play = playerList.getPlayer(conts.playerId);
            if(play != null){
                ServerPlayNetworking.send(play, new CBUpdateContestInfo(conts.playerId, tag));
            }
        }
    }

    public void updateAllContestantLobbies(MinecraftServer server){
        PlayerList playerList = server.getPlayerList();

        CompoundTag tag = generateContestantDataTag(server);
        for(UUID contestantID: contestants.keySet()){
            if( playerList.getPlayer(contestantID) != null){
                ServerPlayer player = playerList.getPlayer(contestantID);
                ServerPlayNetworking.send((ServerPlayer) player, new CBSendContestantStatus(contestantID, tag, applause));
            }

        }
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


    // endregion

    // region Introduction Logic

    private void sendOutPokemon(MinecraftServer server, int contestantIndex){
        PlayerList playerList = server.getPlayerList();

        Contestant contestant = contestants.get(contestantsOrdered.get(contestantIndex));

        //for(Contestant contestant: contestants.values()) {
        ServerPlayer play = playerList.getPlayer(contestant.playerId);
        if(play == null){
            return;
        }
        Pokemon poke = getPokemonFromID(server, contestant.playerId, contestant.pokemon);//Cobblemon.INSTANCE.getStorage().getParty(play).get(contestant.pokemon);
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
            poke.getEntity().cry();
            //cry(poke);
        }

        //SnowstormParticleReader.INSTANCE.loadEffect()

        PokemonEntity pokeEnt = poke.getEntity();
        //ServerPlayNetworking.send(play, new CBSendPlayersParticles(play.getId(), "rainbow", pokeEnt.position().toVector3f()));
        new SpawnSnowstormParticlePacket(ParticleEffectList.HEART_SMOKEBURST, pokeEnt.position())
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

    /**
     * Placeholder to eventually have pokemon play move animation
     * @param pokemon
     */
    public void doAnimation(Pokemon pokemon, String anim) {
        PokemonEntity pokemonEntity = pokemon.getEntity();
        if(pokemonEntity == null){
            return;
        }
        if (pokemonEntity.isSilent()) return;
        PlayPosableAnimationPacket pkt = new PlayPosableAnimationPacket(pokemonEntity.getId(), Set.of(anim), Collections.emptyList());
        Vec3 pos = pokemonEntity.position();
        pkt.sendToPlayersAround(pos.x, pos.y, pos.z, 64, pokemonEntity.level().dimension(), player -> false);
    }

    public void evaluateIntroductionPoints(MinecraftServer server){
        for(Contestant contestant: contestants.values()){
            Pokemon poke = getPokemonFromID(server, contestant.playerId, contestant.pokemon);

            //
            // ZServerPlayer play = playerList.getPlayer(contestant.playerId);
            //if(play != null) {
                //Pokemon poke = Cobblemon.INSTANCE.getStorage().getParty(play).get(contestant.pokemon);
            CVs cvs = CVs.getFromTag(poke.getPersistentData().getCompound("CVs"));
            int totalPoints = 0;
            for(int i = 0; i < 6; i ++){ // goes through all 5 conditions and sheen
                totalPoints += (int) (cvs.getConditionFromIdx(i) * (i == contestType? 1f : 0.5f));
            }

            int hearts = getNumHearts( totalPoints);
            contestant.addHearts(hearts);

                //System.out.println("Player " + play.getDisplayName() + " won " + hearts + "hearts");
            //}
        }
    }

    private int getNumHearts(int points){
        for(int i = 1; i < 9; i ++){
            if(points < INTRO_HEARTS[contestTier < 0? 4:(contestTier < 5? contestTier:4)][i]){
                return i - 1;
            }
        }
        return 8;
    }


    // endregion

    // region Showcase MoveLogic

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

    public void selectMovesForMissingContestants(MinecraftServer server){
        for(Contestant contestant: contestants.values()){
            if(!contestant.isMoveChosen()){
                contestant.setRandomMove(server);
            }
        }
    }

    public boolean getCanChooseMove(){
        return !runningRound;
    }

    public void runContestantMove(MinecraftServer server, Contestant contestant, Pokemon pokemon){

        addContestantMessage(server, null, "cobble_contests.contest_showoff.move_used", pokemon.getDisplayName().getString(), lang("move." + contestant.getCurrentMove()));

        //contestant.useMove(ContestType.getFromInt(contestType));
        ContestMoves.MoveData moveData = ContestMoves.instance.getMoveData(contestant.currentMove);
        ContestMoves.FunctionData functionData = ContestMoves.instance.getFunctionDataFromName(moveData.getFunctionType());

        functionData.onUse(server,this, contestant);

        List<Move> moves = pokemon.getMoveSet().getMoves();
        Move move = null;
        for(int i = 0; i < 4; i++){ // There has to be a better way to get movedata from name
            if(moves.size() > i && moves.get(i) != null){
                if(moves.get(i).getName().equals(contestant.currentMove)){
                    move = moves.get(i);
                    break;
                }
            }
        }
        if(move != null){
            doAnimation(pokemon, move.getDamageCategory().getName());
        }

        sendEveryoneContestants(server);

        contestantIdx += 1;

        updateContestants(server);
    }

    public void applyMoves(MinecraftServer server){

        for(UUID contestantID: contestantsOrdered){
            Contestant contestant = contestants.get(contestantID);
            contestant.applyTurn();
        }
        sendEveryoneContestants(server);

        roundReady = false;
    }

    public void reorderContestants(){

    }

    public void EndRound(MinecraftServer server){
        System.out.println("All moves performed");
        timer = 0;
        runningRound = false;
        showcaseRound += 1;
        roundReady = false;
        contestantIdx = 0;
        applyMoves(server);
        reorderContestants();
        if(showcaseRound >= NUM_SHOWCASE_ROUNDS){
            addContestantMessage(server, null, "cobble_contests.contest_showoff.showcase_end");
            scheduleAction(() -> {EndShowcase(server);}, 2 * TICKS_PER_SECOND);
        }else{
            addContestantMessage(server, null, "cobble_contests.contest_showoff.round_switch");
            updateContestantsWithRound(server);
        }
    }

    public void EndShowcase(MinecraftServer server){
        System.out.println("Finished Talent");
        round = ContestPhase.RESULTS;
        timer = 0;
        updateContestants(server);
        if(contestTier != ContestLevel.Multiplayer.getIntValue()){
            for(UUID id: contestants.keySet()){
                evaluateRankedWinConditions(server, id);
            }

        }else {
            determineContestResults(server);
        }
        //addContestantMessage(server, null, "And thats the end of the Showcase Round! Lets see those results");

    }

    public void IncreaseApplause(MinecraftServer server, Contestant contestant){
        applause += 1;
        if(applause >= MAX_APPLAUSE){
            ApplyApplausePoints(server, contestant);
        }
    }

    public void DecreaseApplause(MinecraftServer server, Contestant contestant){
        applause = Math.max(applause - 1, 0);
    }

    public void ApplyApplausePoints(MinecraftServer server, Contestant contestant){
        contestant.applyApplauseBonus();
        applause = 0;
        addContestantMessage(server, ChatFormatting.AQUA,  "cobble_contests.contest_showcase.award_applause");
    }

    // endregion

    // region Result Logic

    /**
     * For ranked competition checking
     */
    public boolean evaluateRankedWinConditions(MinecraftServer server, UUID uuid){

        Contestant contestant = contestants.get(uuid);
        int totalHearts = contestant.getHearts();

        boolean result;
        if (contestTier < 5 &&
                totalHearts >= thresholds[contestTier]){
            result = true;
        } else {
            result = false;
        }

        ServerPlayer player = server.getPlayerList().getPlayer(uuid);
        //Pokemon pokemon = Cobblemon.INSTANCE.getStorage().getParty(player).get(contestant.pokemon);
        Pokemon pokemon = getPokemonFromID(server, uuid, contestant.pokemon);
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

        scheduleAction(() -> {
            sendConditionalContestantRankForms(server, contestant, result, totalHearts);
        }, (1 * TICKS_PER_SECOND));
        //scheduleAction(() -> {sendPersonalRankMessage(server);}, 1 * TICKS_PER_SECOND);
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

    private boolean runDidBeatRanked(Pokemon poke, int typeVal){
        boolean result = false;
        if (contestTier < 5 &&
                typeVal >= thresholds[contestTier]){
            result = true;
        }
        return result;
    }

    private boolean runContest(Pokemon poke) {
        boolean result = false;
        CVs cvs = CVs.getFromTag(poke.getPersistentData().getCompound("CVs"));
        Ribbons ribbons = Ribbons.getFromTag(poke.getPersistentData().getCompound("Ribbons"));
        switch (contestType) {
            case 0:
                if(runDidBeatRanked(poke, cvs.getCool())) {
                    ribbons.setRankedCool(contestTier, true);
                    result = true;
                }
                break;
            case 1:
                if(runDidBeatRanked(poke, cvs.getBeauty())) {
                    ribbons.setRankedBeauty(contestTier, true);
                    result = true;
                }
                break;
            case 2:
                if(runDidBeatRanked(poke, cvs.getCute())) {
                    ribbons.setRankedCute(contestTier, true);
                    result = true;
                }
                break;
            case 3:
                if(runDidBeatRanked(poke, cvs.getSmart())) {
                    ribbons.setRankedSmart(contestTier, true);
                    result = true;
                }
                break;
            case 4:
                if(runDidBeatRanked(poke, cvs.getTough())) {
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


    public void determineContestResults(MinecraftServer server){
        List<List<Contestant>> rankedContestants = new ArrayList<>();
        for (Contestant contestant : contestants.values()){
            if(rankedContestants.isEmpty()){
                rankedContestants.add(new ArrayList<>(){{add(contestant);}});
            }else{
                for(int i = 0; i < rankedContestants.size(); i++){
                    int numHearts = contestant.getHearts();
                    if(numHearts == rankedContestants.get(i).get(0).getHearts()){ // if tied
                        rankedContestants.get(i).add(contestant);
                        break;
                    }else if(numHearts > rankedContestants.get(i).get(0).getHearts()){ // if beats
                        rankedContestants.add(i, new ArrayList<>(){{add(contestant);}});
                        break;
                    }else if(i == rankedContestants.size() - 1){ // if lowest rank so far
                        rankedContestants.add(new ArrayList<>(){{add(contestant);}});
                    }
                }
            }
        }

        finalRankedContestantList = rankedContestants;
        int rankIndex = contestants.size();
        long delay = 1;
        System.out.println("Calculated results for " + contestants.size() + " contestants with " + rankedContestants.size() + " ranks");
        for(int i = 0; i < Math.min(rankedContestants.size(), 3); i++){
            int j = Math.min(rankedContestants.size(), 3) - 1 - i;
            List<Contestant> rankedContestantList = rankedContestants.get(j);
            int totalHearts = rankedContestantList.getFirst().getHearts();

            //rankIndex -= rankedContestantList.size();

            int finalRankIndex = j;//rankIndex;
            //rankIndex = 4;
            scheduleAction(() -> {
                sendIncrementedContestantRankForms(server, finalRankIndex, totalHearts, rankedContestantList);
                }, (1 + (1 * i) * TICKS_PER_SECOND));
            delay += 1;
            //System.out.println("player ranked " + rankIndex + " with " + totalHearts + " hearts");

        }
        scheduleAction(() -> {sendPersonalRankMessage(server);}, delay * TICKS_PER_SECOND);


    }
    // endregion

    // region Getters and Questions
    public boolean isPlayerHost(UUID playerID){
        //System.out.println("Player " + playerID + " is checked against host " + host + " and is " + playerID.equals(host));
        return playerID.equals(host);
    }

    public UUID getHost(){
        return host;
    }

    public int getShowcaseRound(){
        return showcaseRound;
    }

    public int getTimer(){
        return (int)(timer/20);
    }

    public ContestPhase getRound(){
        return round;
    }

    public UUID getContestentPokemon(UUID uuid){
        return contestants.get(uuid).getPokemon();
    }

    public Map<UUID, Contestant> getContestants(){
        return contestants;
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

    public int getContestLevel() {
        return contestTier;
    }

    public ContestType getContestType(){
        // TODO: make it return right type
        return ContestType.getFromInt(contestType);//ContestType.Beauty; // contestType;
    }

    public int getNumContestants(){
        return contestants.size();
    }

    public List<Contestant> getContestantAtRank(int rank){
        if(finalRankedContestantList.size() >= rank){
            return finalRankedContestantList.get(rank);
        }
        return null;
    }

    public int getRankedIdx(){
        return rankedIdx;
    }

    public int getApplause(){
        return applause;
    }

    public boolean getRunningRound(){
        return runningRound;
    }

    // endregion

}
