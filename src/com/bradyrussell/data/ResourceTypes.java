package com.bradyrussell.data;

public enum ResourceTypes {
    CopperMetal("Copper Ore"),
    IronMetal("Iron Ore"),
    SilverMetal("Silver Ore"),
    GoldMetal("Gold Ore"),
    Wheat("Wheat"),
    ;
    public final String DisplayName;

    ResourceTypes(String displayName) {
        DisplayName = displayName;
    }
}
