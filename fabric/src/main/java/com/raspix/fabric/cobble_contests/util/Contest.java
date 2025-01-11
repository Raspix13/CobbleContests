package com.raspix.fabric.cobble_contests.util;

import com.raspix.fabric.cobble_contests.network.CBUpdateContestInfo;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class Contest {
    private UUID host;
    private int contestType;
    private int contestTier;
    private ItemStack reward;
    private Map<UUID, Contestant> contestants = new HashMap<>();
    private ContestPhase round;
    private float timer;
    private int timerInt;
    private static int TICKS_PER_SECOND = 20;
    //time for each phase in seconds
    private static int WAITING_TIME = 5;
    private static int DRESSUP_TIME = 60;
    private static int RESULTS_TIME = 15;

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

        public Contestant(UUID player, UUID pokemon){
            this.player = player;
            this.pokemon = pokemon;
            this.hearts = 0;
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
    }

    public enum ContestPhase{
        IDLE,
        WAITING,
        DRESSUP, // select stickers
        INTRODUCTION, // the pokemon are sent out
        TALENT, // moves
        RESULTS; // results


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

    public void update(float timeChange, MinecraftServer server) {

        if(!(round == ContestPhase.IDLE || round == ContestPhase.INTRODUCTION || round == ContestPhase.TALENT)){
            timer += timeChange;//Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
        }

        if(round == ContestPhase.IDLE || round == ContestPhase.INTRODUCTION || round == ContestPhase.TALENT) {

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
            }
        }else if (round == ContestPhase.RESULTS && timer >= RESULTS_TIME * TICKS_PER_SECOND){
            System.out.println("Finished Contest");
            round = ContestPhase.IDLE;
            updateContestants(server);
            EndContest();
        }


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
        this.round = ContestPhase.WAITING;
        addContestants(hostId, pokeIdx);
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

    public boolean StartContest(){
        timer = 0f;
        return false;
    }

    public UUID getContestentPokemon(UUID uuid){
        return contestants.get(uuid).getPokemon();
    }

    public Map<UUID, Contestant> getContestants(){
        return contestants;
    }

    public boolean EndContest(){
        ContestManager.INSTANCE.EndContest(this);
        return false;
    }


}
