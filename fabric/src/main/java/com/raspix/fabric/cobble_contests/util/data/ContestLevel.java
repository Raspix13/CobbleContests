package com.raspix.fabric.cobble_contests.util.data;

public enum ContestLevel {

    Normal(0),
    Super(1),
    Hyper(2),
    Ultra(3),
    Master(4),
    None(-2), // when the level is not yet set
    Multiplayer(-1);

    private final int value;

    ContestLevel(int value) {
        this.value = value;
    }

    public int getIntValue() {
        return this.value;
    }

    public static ContestLevel getFromInt(int value) {
        for (ContestLevel type : ContestLevel.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return None; // If no matching ContestLevel is found
    }
}
