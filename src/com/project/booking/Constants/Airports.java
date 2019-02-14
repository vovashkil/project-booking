package com.project.booking.Constants;

public enum Airports {
    KBP("Kiev Boryspil"),
    FRA("Frankfurt"),
    KATOWICE("Katowice");

    private final String name;

    Airports(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
