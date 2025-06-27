package com.raspix.fabric.cobble_contests.util.data;

public enum ContestType {
    Cool(0),
    Beauty(1),
    Cute(2),
    Smart(3),
    Tough(4),
    None(-1);

    private final int value;

    ContestType(int value) {
        this.value = value;
    }

    public int getIntValue() {
        return this.value;
    }

    public static ContestType getFromInt(int value) {
        for (ContestType type : ContestType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return None; // If no matching ContestType is found
    }
}
