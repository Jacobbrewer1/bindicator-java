package com.github.jacobbrewer1.enums;

import java.util.List;

public enum BinType {

    RUBBISH("Rubbish"),
    FOOD_WASTE("Food Waste"),
    RECYCLING("Recycling"),
    GARDEN_WASTE("Garden Waste");

    public boolean isIn(BinType ... binTypes) {
        return List.of(binTypes).contains(this);
    }

    BinType(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    private final String friendlyName;

    public String getFriendlyName() {
        return friendlyName;
    }
}
