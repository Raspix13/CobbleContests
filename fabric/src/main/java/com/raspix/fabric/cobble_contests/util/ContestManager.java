package com.raspix.fabric.cobble_contests.util;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.raspix.fabric.cobble_contests.network.CB.CBClearMessageQueue;
import com.raspix.fabric.cobble_contests.network.CB.CBUpdateContestInfo;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
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
    private static List<Contest> activeContests;
    private static Map<UUID, Contest> activeContestents;
    private float tempTimer;
    //private static List<UUID> activeContestents;
    private long lastTime;


    public void OnServerSetUp(){
        contests = new ArrayList<>();
        activeContests = new ArrayList<>();
        activeContestents = new HashMap<>();

        tempTimer = 0f;
        lastTime = System.currentTimeMillis();

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

    public boolean startContest(Contest startingContest){
        //PlayerList playerList = server.getPlayerList();

        if(contests.contains(startingContest)){
            Map<UUID, Contest.Contestant> contestants = startingContest.getContestants();
            for (UUID contestant : contestants.keySet()){
                //ServerPlayer play = playerList.getPlayer(contestant);
                //notifyPlayerContestResults(contestant, endingContest, play);
                activeContestents.put(contestant, startingContest);
            }
            activeContests.add(startingContest);
            return true;
        }else{
            return false;
        }
    }



    public boolean EndContest(Contest endingContest, MinecraftServer server){
        PlayerList playerList = server.getPlayerList();

        if(contests.contains(endingContest)){
            activeContests.remove(endingContest);
            contests.remove(endingContest);
            Map<UUID, Contest.Contestant> contestants = endingContest.getContestants();
            for (UUID contestant : contestants.keySet()){
                // tell players results
                ServerPlayer play = playerList.getPlayer(contestant);
                notifyPlayerContestResults(contestant, endingContest, play);
                activeContestents.remove(contestant);
                ServerPlayNetworking.send((ServerPlayer) play, new CBClearMessageQueue(contestant));
                //ContestManagerClient.INSTANCE.deleteContestantMessage(contestant);
            }
            activeContestents.remove(endingContest.getHost());
            //contests.remove(endingContest);
            return true;
        }else{
            return false;
        }
    }

    public void update(MinecraftServer server){
        //tempTimer += Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
        //System.out.println(tempTimer);
        float timeChange = (System.currentTimeMillis()-lastTime)/1000.0f;//server.getCurrentSmoothedTickTime()/1000f;//Minecraft.getInstance().getTimer().getRealtimeDeltaTicks();
        lastTime = System.currentTimeMillis();
        //System.out.println("Time Change: " + timeChange);
        if(!activeContests.isEmpty()){
            PlayerList playerList = server.getPlayerList();
            List<Contest> activeContests2 = new ArrayList<>(activeContests);
            for(Contest contest: activeContests2){
                contest.update(timeChange, server);
            }
        }
        /**if(!activeContestents.isEmpty()){
            PlayerList playerList = server.getPlayerList();
            for(UUID id: activeContestents.keySet()){
                if(playerList.getPlayer(id) != null){
                    Contest contest = activeContestents.get(id);
                    contest.update(timeChange, server);
                }else{
                    activeContestents.remove(id); //if someone leaves the server before contest ends
                }

            }
        }*/

    }

    public void notifyPlayerContestResults(UUID id, Contest endingContest, ServerPlayer player){
        Component componentOutput;

        componentOutput = endingContest.tempRunContestResults(id);

        ServerPlayer sPlayer = player;//poke.getOwnerPlayer();
        if (!sPlayer.level().isClientSide()) {
            sPlayer.displayClientMessage(componentOutput, false);
        }

    }

    public List<Contest> getContests(){
        return contests;
    }



}
