package com.raspix.fabric.cobble_contests.util;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.raspix.fabric.cobble_contests.util.data.ContestLevel;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;

import java.util.ArrayList;
import java.util.UUID;

import static com.cobblemon.mod.common.util.LocalizationUtilsKt.lang;

/**
 * A part of every contest. Is in charge of handling data and logic of showcases
 *
 */
public class ShowcaseHelper {

    /**private Contest contest;
    private ArrayList<UUID> contestantsOrdered;
    private int contestantIdx;
    int showcaseRound = 0; // Which round of showcase moves it is
    boolean roundReady; // are all moves chosen
    boolean runningRound;

    private static int TICKS_PER_SECOND = 20; //time for each phase in seconds
    private static int NUM_SHOWCASE_ROUNDS = 2;
    private static int SHOWCASE_ROUND_TIME = 30; // The max time to choose moves
    private static int SHOWCASE_PER_CONTESTANT = 5; // The time for each contestant to showcase their moves

    public ShowcaseHelper(Contest partentContest){
        this.contest = partentContest;
        this.contestantsOrdered = new ArrayList<>();
        this.contestantIdx = 0;
        this.showcaseRound = 0;

    }



    public void Update(float timeChange, MinecraftServer server, float timer){
        if(timerInt != getTimer()){
            updateContestants(server);
            timerInt = getTimer();
        }


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
            updateContestants(server);

            for(int i = 0; i < contestantsOrdered.size(); i++){
                PlayerList playerList = server.getPlayerList();
                Contest.Contestant contestant = contestants.get(contestantsOrdered.get(contestantIdx));
                ServerPlayer player = playerList.getPlayer(contestant.player);

                contest.scheduleAction();
            }



        }









        if(runningRound){

            if(timer >= (2 + (contestantIdx * SHOWCASE_PER_CONTESTANT)) * TICKS_PER_SECOND) {// For each contestant to do their moves

                if (contestantIdx < contestantsOrdered.size()) {

                    System.out.println("Move being performed");
                    PlayerList playerList = server.getPlayerList();
                    Contest.Contestant contestant = contestants.get(contestantsOrdered.get(contestantIdx));
                    ServerPlayer player = playerList.getPlayer(contestant.player);

                    assert player != null;
                    Pokemon poke = Cobblemon.INSTANCE.getStorage().getParty(player).get(contestant.pokemon);

                    assert poke != null;
                    addContestantMessage(server, null, "cobble_contests.contest_showoff.move_used", poke.getDisplayName().getString(), lang("move." + contestant.getCurrentMove()));

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
        if(showcaseRound >= NUM_SHOWCASE_ROUNDS){ // Ends showcase
            System.out.println("Finished Talent");
            contest.SetContestRound(Contest.ContestPhase.RESULTS);
            updateContestants(server);
            if(contestTier != ContestLevel.Multiplayer.getIntValue()){
                for(UUID id: contestants.keySet()){
                    evaluateRankedWinConditions(server, id);
                }

            }else {
                determineContestResults(server);
            }
            addContestantMessage(server, null, "And thats the end of the Showcase Round! Lets see those results");

        }
    }*/

}
