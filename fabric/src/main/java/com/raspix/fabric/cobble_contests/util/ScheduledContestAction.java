package com.raspix.fabric.cobble_contests.util;

import java.util.ArrayList;
import java.util.List;

public class ScheduledContestAction {
    private Runnable action;
    private long scheduledTime;

    public ScheduledContestAction(Runnable action, long scheduledTime) {
        this.action = action;
        this.scheduledTime = scheduledTime;
    }

    public void executeAction() {
        action.run();
    }

    public long getScheduledTime() {
        return scheduledTime;
    }

}