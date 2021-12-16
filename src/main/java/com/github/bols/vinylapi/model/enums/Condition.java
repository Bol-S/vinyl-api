package com.github.bols.vinylapi.model.enums;

public enum Condition {
    MINT("Mint"),
    NEAR_MINT("Near Mint"),
    VERY_GOOD_PLUS("Very Good Plus"),
    VERY_GOOD("Very Good"),
    GOOD("Good"),
    POOR("Poor");

    private String name;

    Condition(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}