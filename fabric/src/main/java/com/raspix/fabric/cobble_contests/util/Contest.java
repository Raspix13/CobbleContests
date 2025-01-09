package com.raspix.fabric.cobble_contests.util;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class Contest implements Tickable {
    private UUID host;
    private int contestType;
    private int contestTier;
    private ItemStack reward;
    private Map<UUID, Contestant> contestants = new HashMap<>();
    private ContestPhase round;
    private float timer;

    /** 10 sec to explain,
     * Intro
     *      -5 sec explain
     *      -60 sec choices
     *      -5 sec "done"
     *      -time for all out
     */



    public class Contestant{
        private UUID player;
        private int pokemon; //not sure what to reference here
        private int hearts;

        public Contestant(UUID player, int pokemon){
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
        public int getPokemon() {
            return pokemon;
        }

        public void setPokemon(int pokemon) {
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
        WAITING,
        DRESSUP, // select stickers
        INTRODUCTION, // the pokemon are sent out
        SHOWOFF, // moves
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

    @Override
    public void tick() {

        timer += 1;

        if(timer > 200 && timer < 12000){
            round = ContestPhase.DRESSUP;
        }else if(timer > 12000){
            round = ContestPhase.INTRODUCTION;
        }

        System.out.println(timer);


    }

    public Contest(UUID hostId, int contestType, int contestTier, ItemStack reward){
        this.host = hostId;
        this.contestType = contestType;
        this.contestTier = contestTier;
        this.reward = reward;
        this.contestants = new HashMap<>();
        this.round = ContestPhase.WAITING;
    }

    public Contest(UUID hostId, int contestType, int contestTier, ItemStack reward, boolean hostParticipate, int pokeIdx){
        this.host = hostId;
        this.contestType = contestType;
        this.contestTier = contestTier;
        this.reward = reward;
        this.contestants = new HashMap<>();
        this.round = ContestPhase.WAITING;
        addContestants(hostId, pokeIdx);
    }

    public void addContestants(UUID uuid, int pokeIdx){
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

    public int getContestentPokemon(UUID uuid){
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
