package com.raspix.fabric.cobble_contests.util;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.reactive.SimpleObservable;
import com.cobblemon.mod.common.client.battle.ClientBattleMessageQueue;
import com.cobblemon.mod.common.client.render.SnowstormParticle;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.net.messages.client.effect.SpawnSnowstormEntityParticlePacket;
import com.cobblemon.mod.common.net.messages.client.effect.SpawnSnowstormParticlePacket;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.raspix.fabric.cobble_contests.network.CBSendContestantMessage;
import com.raspix.fabric.cobble_contests.network.CBUpdateContestInfo;
import com.raspix.fabric.cobble_contests.network.CBWalletScreenParty;
import com.raspix.fabric.cobble_contests.pokemon.CVs;
import com.raspix.fabric.cobble_contests.pokemon.Ribbons;
import kotlin.Unit;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.locale.Language;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import com.cobblemon.mod.common.util.PlayerExtensionsKt;
import com.cobblemon.mod.common.client.CobblemonResources;
import java.util.*;

import static com.cobblemon.mod.common.util.MiscUtilsKt.cobblemonResource;

public class Contest {
    private UUID host;
    private int contestType; // Cool, Beauty, Cute, Clever, Tough
    private int contestTier; // "Normal", "Super", "Hyper", "Ultra", "Master" Only used for ranked matches
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
    private static int RESULTS_TIME = 15; // The time the player can see the results before they are released from the contest
    private static int SEND_OUT_TIMER = 3; // The time in between each pokemon getting sent out
    private static int TEMP_TALENT_TIME = 5; // The placeholder timer for players in the talent portion of the contest

    private static int[][] INTRO_HEARTS = new int[][]{ // Max 8 hearts
            {0, 11, 21, 31, 41, 51, 61, 71, 81}, // Normal
            {0, 91, 111, 131, 151, 171, 191, 211, 231}, // Super
            {0, 171, 201, 231, 261, 291, 321, 351, 381}, // Hyper
            {0, 321, 361, 401, 441, 481, 521, 561, 601}, // Ultra TODO
            {0, 321, 361, 401, 441, 481, 521, 561, 601} // Master
    };

    public static final String[] CONTEST_TYPES = new String[]{
            ""
    };

    /** 10 sec to explain,
     * Intro
     *      -5 sec explain
     *      -60 sec choices
     *      -5 sec "done"
     *      -time for all out
     */



    public class Contestant{
        private UUID player;
        private UUID pokemon; //not sure what to reference here
        private int hearts;
        private ClientBattleMessageQueue contestMessages;

        public Contestant(UUID player, UUID pokemon){
            this.player = player;
            this.pokemon = pokemon;
            this.hearts = 0;
            this.contestMessages = new ClientBattleMessageQueue();
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

        public void addHearts(int hearts){
            this.hearts += hearts;
        }

        public ClientBattleMessageQueue getContestMessages(){
            return contestMessages;
        }
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

    public void addContestantMessage(MinecraftServer server, String transLine, Object ... objects){
        PlayerList playerList = server.getPlayerList();


        for(UUID contestantID: contestantsOrdered){
            ServerPlayer serverPlayer = playerList.getPlayer(contestantID);
            Component line = Component.translatable(transLine, objects).copy().withStyle(ChatFormatting.BOLD);//.withStyle(CobblemonResources.INSTANCE.getDEFAULT_LARGE());
            if(serverPlayer != null){
                ServerPlayNetworking.send(serverPlayer, new CBSendContestantMessage(contestantID, line.toFlatList()));
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

        if(round == ContestPhase.WAITING && timer == 0f){
            addContestantMessage(server, "The Contest is starting! Contestants should get into position\n");
        }

        if(!(round == ContestPhase.ENDING)){
            timer += timeChange;//Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
        }



        if(round == ContestPhase.IDLE && timer >= LOBBY_TIMEOUT * TICKS_PER_SECOND) {
            System.out.println("Contest Lobby Timed Out");
            // Should notify anyone who was in the lobby
            round = ContestPhase.ENDING;
            updateContestants(server);
            EndContest(server);

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
                addContestantMessage(server, "And that's time! Now to meet the contestants!\n");
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
                addContestantMessage(server, "cobble_contests.contest_showoff.intro", player.getDisplayName().getString(), poke.getDisplayName().getString(), poke.getSpecies().getName());
                //addContestantMessage(Component.translatable("cobble_contests.contest_showoff.intro", player.getDisplayName().getString(), poke.getDisplayName().getString(), poke.getSpecies().getName()));

                sendOutPokemon(server, contestantIdx);
                this.contestantIdx += 1;
                if(contestantIdx >= contestants.size()){
                    System.out.println("Finished Introduction");
                    timer = 0;
                    round = ContestPhase.TALENT;
                    updateContestants(server);
                }
            }
        }else if(round == ContestPhase.TALENT &&  timer >= (TEMP_TALENT_TIME * TICKS_PER_SECOND)){
            System.out.println("Finished Talent");
            round = ContestPhase.RESULTS;
            timer = 0;
            updateContestants(server);
        }


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
            new SpawnSnowstormParticlePacket(cobblemonResource("rainbow"), pokeEnt.position())
                    .sendToPlayersAround(pokeEnt.getX(), pokeEnt.getY(), pokeEnt.getZ(), 64.0, pokeEnt.level().dimension(), serverPlayer -> {
                        return false;
                    });
            new SpawnSnowstormEntityParticlePacket(cobblemonResource("rainbow"), play.getId(), Arrays.asList())
                    .sendToPlayersAround(pokeEnt.getX(), pokeEnt.getY(), pokeEnt.getZ(), 64.0, pokeEnt.level().dimension(), serverPlayer -> {
                        return false;
                    });//ResourceLocation.fromNamespaceAndPath(CobbleContests.MOD_ID, "loading.png")*
            /**new SpawnSnowstormEntityParticlePacket(cobblemonResource("shiny_ring"), it.getId(), Arrays.asList("shiny_particles", "middle"))
                    .sendToPlayersAround(it.getX(), it.getY(), it.getZ(), 64.0, it.level().dimension(), serverPlayer -> {
                        return false;
                    });*/

        //}
    }

    private void updateContestants(MinecraftServer server){
        PlayerList playerList = server.getPlayerList();
        for(Contestant conts: contestants.values()){
            CompoundTag tag = new CompoundTag();
            tag.putUUID("index", conts.pokemon);
            round.toTag(tag, "phase");
            tag.putInt("seconds", getTimer());
            ServerPlayer play = playerList.getPlayer(conts.player);
            if(play != null){
                ServerPlayNetworking.send(play, new CBUpdateContestInfo(conts.player, tag));
            }
        }
    }

    public int getTimer(){
        return (int)(timer/20);
    }

    public Contest(UUID hostId, int contestType, int contestTier, ItemStack reward){
        this.host = hostId;
        this.contestType = contestType;
        this.contestTier = contestTier;
        this.reward = reward;
        this.contestants = new HashMap<>();
        this.round = ContestPhase.WAITING;
    }

    public Contest(UUID hostId, int contestType, int contestTier, ItemStack reward, boolean hostParticipate, UUID pokeIdx){
        this.host = hostId;
        this.contestType = contestType;
        this.contestTier = contestTier;
        this.reward = reward;
        this.contestants = new HashMap<>();
        this.round = ContestPhase.IDLE;
        this.contestantIdx = 0;
        addContestants(hostId, pokeIdx);
        //StartContest();
    }

    public void addContestants(UUID uuid, UUID pokeIdx){
        contestants.put(uuid, new Contestant(uuid, pokeIdx));
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
            timer = 0f;
            this.contestantsOrdered = new ArrayList<>(contestants.keySet());
            this.round = ContestPhase.WAITING;
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

    private int getNumHearts(int points){
        for(int i = 1; i < 9; i ++){
            if(points < INTRO_HEARTS[contestTier][i]){
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
}
