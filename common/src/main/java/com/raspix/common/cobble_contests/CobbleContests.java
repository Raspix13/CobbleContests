package com.raspix.common.cobble_contests;

import com.google.common.eventbus.EventBus;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class CobbleContests {

    public static final String MOD_ID = "cobble_contests";
    public static final Logger LOGGER = LogUtils.getLogger();


    public static void init() {
        LOGGER.info("Initializing " + MOD_ID);

    }



}
