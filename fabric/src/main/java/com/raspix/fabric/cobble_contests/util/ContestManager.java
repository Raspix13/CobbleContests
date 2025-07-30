package com.raspix.fabric.cobble_contests.util;

import com.raspix.fabric.cobble_contests.network.CB.CBClearMessageQueue;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.item.ItemStack;

import java.util.*;

//@Environment(EnvType.SERVER)
public class ContestManager {

    public static ContestManager INSTANCE = new ContestManager();
    private static List<Contest> contests; // A list of all contests
    private static List<Contest> publicContests;
    private static List<Contest> activeContests; // A list of all contests that are actively being run
    private static Map<UUID, Contest> activeContestents; // A list of all hosts, contestants, and spectators
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
        if(activeContestents.containsKey(potential)){
            return activeContestents.get(potential);
        }
        return null;
    }


    /**
     * When the host creates a new lobby
     */
    public Contest AddContest(MinecraftServer server, UUID hostId, int contestType, int contestTier, ItemStack reward, boolean hostParticipates, UUID pokeIdx){
        if(IsAlreadyInContest(hostId)){
            System.out.println("Already in contest");
            return null;
        }
        Contest newCon = new Contest(server, hostId, contestType, contestTier, reward, hostParticipates, pokeIdx);
        contests.add(newCon);
        activeContestents.put(hostId, newCon);

        return newCon;
    }


    /**
     * When the host begins the contest for the lobby
     * @param startingContest
     * @return
     */
    public boolean startContest(Contest startingContest){

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

    public boolean addContestantToLobby(MinecraftServer server, ServerPlayer player, UUID hostId, UUID uuid, UUID pokeIdx){
        if(IsAlreadyInContest(uuid)){
            return false;
        }
        Contest goalContest = getPlayersContest(hostId);
        boolean wasPlayerAdded = false;
        if(goalContest != null){
            wasPlayerAdded = goalContest.addContestants(server , player, uuid, pokeIdx);
            if(wasPlayerAdded){
                activeContestents.put(uuid, goalContest);
            }
        }

        return wasPlayerAdded;


    }

    public boolean addSpectatorToLobby(MinecraftServer server, ServerPlayer player, UUID uuid, UUID pokeIdx){
        //Contest goalContest = ContestManager.INSTANCE.getPlayersContest(getHostId());

        //goalContest.addContestants(server ,(ServerPlayer) player, getId(), getPokeId());
        return false;
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
                if(play != null){
                    notifyPlayerContestResults(contestant, endingContest, play);
                    activeContestents.remove(contestant);
                    ServerPlayNetworking.send((ServerPlayer) play, new CBClearMessageQueue(contestant));
                    //ContestManagerClient.INSTANCE.deleteContestantMessage(contestant);
                }

            }
            activeContestents.remove(endingContest.getHost());
            //contests.remove(endingContest);
            return true;
        }else{
            return false;
        }
    }

    public boolean TimeoutContest(Contest endingContest, MinecraftServer server){
        PlayerList playerList = server.getPlayerList();

        if(contests.contains(endingContest)){
            activeContests.remove(endingContest);
            contests.remove(endingContest);
            Map<UUID, Contest.Contestant> contestants = endingContest.getContestants();
            for (UUID contestant : contestants.keySet()){
                // tell players results
                ServerPlayer play = playerList.getPlayer(contestant);
                notifyPlayerContestTimeout(contestant, endingContest, play);
                activeContestents.remove(contestant);
                ServerPlayNetworking.send((ServerPlayer) play, new CBClearMessageQueue(contestant));
            }
            activeContestents.remove(endingContest.getHost());
            return true;
        }else{
            return false;
        }
    }

    public void update(MinecraftServer server){
        //tempTimer += Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false);
        //System.out.println(tempTimer);
        float timeChange = 1f;//(System.currentTimeMillis()-lastTime)/1000.0f;//server.getCurrentSmoothedTickTime()/1000f;//Minecraft.getInstance().getTimer().getRealtimeDeltaTicks();
        lastTime = System.currentTimeMillis();
        //System.out.println("Time Change: " + timeChange);
        if(!contests.isEmpty()){
            PlayerList playerList = server.getPlayerList();
            List<Contest> activeContests2 = new ArrayList<>(contests);
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

    public void notifyPlayerContestTimeout(UUID id, Contest endingContest, ServerPlayer player){
        ServerPlayer sPlayer = player;
        if (!sPlayer.level().isClientSide()) {
            sPlayer.displayClientMessage(Component.translatable("cobble_contests.contest_text.timeout"), false);
        }

    }

    public List<Contest> getContests(){
        return contests;
    }



}
