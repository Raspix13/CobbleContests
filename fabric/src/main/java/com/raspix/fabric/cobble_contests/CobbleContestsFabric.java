package com.raspix.fabric.cobble_contests;

import com.raspix.common.cobble_contests.CobbleContests;
import com.raspix.common.cobble_contests.ExampleCommandRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class CobbleContestsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CobbleContests.init();
        CommandRegistrationCallback.EVENT.register(ExampleCommandRegistry::registerCommands);
    }

}
