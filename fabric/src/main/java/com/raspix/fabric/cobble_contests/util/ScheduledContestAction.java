package com.raspix.fabric.cobble_contests.util;

import java.util.ArrayList;
import java.util.List;

public class ScheduledContestAction {
    private Runnable action;
    private float scheduledTime;

    public ScheduledContestAction(Runnable action, float scheduledTime) {
        this.action = action;
        this.scheduledTime = scheduledTime;
    }

    public void executeAction() {
        action.run();
    }

    public float getScheduledTime() {
        return scheduledTime;
    }

}