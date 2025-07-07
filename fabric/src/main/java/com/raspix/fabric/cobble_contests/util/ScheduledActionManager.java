package com.raspix.fabric.cobble_contests.util;

import java.util.ArrayList;
import java.util.List;

public class ScheduledActionManager {
    private List<ScheduledContestAction> scheduledActions = new ArrayList<>();

    public void scheduleAction(Runnable action, long delay) {
        long scheduledTime = System.currentTimeMillis() + delay;
        ScheduledContestAction scheduledAction = new ScheduledContestAction(action, scheduledTime);
        scheduledActions.add(scheduledAction);
    }

    public void update() {
        long currentTime = System.currentTimeMillis();
        for (ScheduledContestAction scheduledAction : new ArrayList<>(scheduledActions)) {
            if (currentTime >= scheduledAction.getScheduledTime()) {
                scheduledAction.executeAction();
                scheduledActions.remove(scheduledAction);
            }
        }
    }
}