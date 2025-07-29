package com.raspix.fabric.cobble_contests.util;

import java.util.ArrayList;
import java.util.List;

public class ScheduledActionManager {

    private float timeCounter;
    private List<ScheduledContestAction> scheduledActions = new ArrayList<>();

    public ScheduledActionManager(){
        this.timeCounter = 0f;
    }

    public void scheduleAction(Runnable action, float delay) {
        float scheduledTime = timeCounter + delay;
        ScheduledContestAction scheduledAction = new ScheduledContestAction(action, scheduledTime);
        scheduledActions.add(scheduledAction);
    }

    public void update(float timeChange) {
        float currentTime = timeCounter + timeChange;
        for (ScheduledContestAction scheduledAction : new ArrayList<>(scheduledActions)) {
            if (currentTime >= scheduledAction.getScheduledTime()) {
                scheduledAction.executeAction();
                scheduledActions.remove(scheduledAction);
            }
        }
        timeCounter += timeChange;
    }
}