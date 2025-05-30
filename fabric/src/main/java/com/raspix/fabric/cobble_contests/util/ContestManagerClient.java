package com.raspix.fabric.cobble_contests.util;

import com.cobblemon.mod.common.client.battle.ClientBattleMessageQueue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Environment(EnvType.CLIENT)
public class ContestManagerClient {

    public static ContestManagerClient INSTANCE = new ContestManagerClient();
    private static Map<UUID, ClientBattleMessageQueue> contestantMessages;

    public void OnServerSetUp(){
        contestantMessages = new HashMap<>();
    }

    public ClientBattleMessageQueue getContestantMessages(UUID id){
        if(!contestantMessages.containsKey(id)){
            addContestantMessage(id);
        }
        return contestantMessages.get(id);

    }

    public void addContestantMessage(UUID id){
        contestantMessages.put(id, new ClientBattleMessageQueue());
    }

    public void deleteContestantMessage(UUID id){
        contestantMessages.remove(id);
    }



}
