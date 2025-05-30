package com.raspix.fabric.cobble_contests.util;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.item.ItemStack;

import java.util.*;

//@Environment(EnvType.SERVER)
public class ContestManager {

    public static ContestManager INSTANCE = new ContestManager();
    private static List<Contest> contests;
    private static Map<UUID, Contest> activeContestents;
    private float tempTimer;
    //private static List<UUID> activeContestents;


    public void OnServerSetUp(){
        contests = new ArrayList<>();
        activeContestents = new HashMap<>();

        tempTimer = 0f;

    }

    public void OnServerShutDown(){

    }

    public boolean IsAlreadyInContest(UUID potential){
        return activeContestents.containsKey(potential);
    }

    public Contest getPlayersContest(UUID potential){
        //System.out.println("Getting Contest");
        //System.out.println("Num contestants: " + activeContestents.size());
        if(activeContestents.containsKey(potential)){
            //System.out.println("found contest");
            return activeContestents.get(potential);
        }
        //System.out.println("did not find contest");
        return null;
    }


    public boolean AddContest(UUID hostId, int contestType, int contestTier, ItemStack reward, boolean hostParticipates, UUID pokeIdx){
        if(IsAlreadyInContest(hostId)){
            System.out.println("Already in contest");
            return false;
        }
        Contest newCon = new Contest(hostId, contestType, contestTier, reward, hostParticipates, pokeIdx);
        contests.add(newCon);
        activeContestents.put(hostId, newCon);

        return true;
    }



    public boolean EndContest(Contest endingContest, MinecraftServer server){
        PlayerList playerList = server.getPlayerList();

        if(contests.contains(endingContest)){
            Map<UUID, Contest.Contestant> contestants = endingContest.getContestants();
            for (UUID contestant : contestants.keySet()){
                // tell players results
                ServerPlayer play = playerList.getPlayer(contestant);
                notifyPlayerContestResults(contestant, endingContest, play);
                activeContestents.remove(contestant);
            }
            activeContestents.remove(endingContest.getHost());
            contests.remove(endingContest);
            return true;
        }else{
            return false;
        }
    }

    public void update(MinecraftServer server){
        //tempTimer += Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
        //System.out.println(tempTimer);
        float timeChange = 1;//Minecraft.getInstance().getTimer().getRealtimeDeltaTicks();
        if(!activeContestents.isEmpty()){
            for(Contest contest: activeContestents.values()){
                contest.update(timeChange, server);
            }
        }

    }

    public void notifyPlayerContestResults(UUID id, Contest endingContest, ServerPlayer player){
        Component componentOutput;

        componentOutput = endingContest.tempRunContestResults(id);

        ServerPlayer sPlayer = player;//poke.getOwnerPlayer();
        if (!sPlayer.level().isClientSide()) {
            sPlayer.displayClientMessage(componentOutput, false);
        }

    }



}
