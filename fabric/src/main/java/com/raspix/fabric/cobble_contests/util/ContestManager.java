package com.raspix.fabric.cobble_contests.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
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
        if(activeContestents.containsKey(potential)){
            return activeContestents.get(potential);
        }
        return null;
    }



    public boolean AddContest(UUID hostId, int contestType, int contestTier, ItemStack reward, boolean hostParticipates, int pokeIdx){
        if(IsAlreadyInContest(hostId)){
            return false;
        }
        Contest newCon = new Contest(hostId, contestType, contestTier, reward, hostParticipates, pokeIdx);
        contests.add(newCon);
        activeContestents.put(hostId, newCon);

        return true;
    }



    public boolean EndContest(Contest endingContest){
        if(contests.contains(endingContest)){
            for (UUID contestant : endingContest.getContestants().keySet()){
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
        for(Contest contest: contests){
            contest.update(timeChange, server);
        }
    }





}
