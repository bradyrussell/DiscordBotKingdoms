package com.bradyrussell.data;

public enum BuildingTypes {
    //Command
    ThroneRoom("Throne Room", 0, Integer.MAX_VALUE, 1, ""),
    Keep(null, 1, Integer.MAX_VALUE, 1, ""),
    Castle(null, 1, Integer.MAX_VALUE, 1, ""),

    //Defense
    CastleWalls("Castle Walls", 1, Integer.MAX_VALUE, 1, ""),
    Moat(null, 1, Integer.MAX_VALUE, 1, ""),
    KingdomWalls("Castle Walls", 1, Integer.MAX_VALUE, 1, "A great wall around the entire kingdom."),

    //Unit Production
    Barracks(null, 1, Integer.MAX_VALUE, 1, ""),
    Stable(null, 1, Integer.MAX_VALUE, 1, ""),

    //Resource Production
    Farm(null, 1, Integer.MAX_VALUE, 1, ""),
    Granary(null, 1, Integer.MAX_VALUE, 1, ""),
    CopperMine("Copper Mine", 1, Integer.MAX_VALUE, 1, ""),
    IronMine("Iron Mine", 1, Integer.MAX_VALUE, 1, ""),
    SilverMine("Silver Mine", 1, Integer.MAX_VALUE, 1, ""),
    GoldMine("Gold Mine", 1, Integer.MAX_VALUE, 1, ""),
    ;

    BuildingTypes(String displayName, long buildCost, int maxLevel, int maxAllowedInKingdom, String description) {
        DisplayName = displayName == null ? this.name() : displayName;
        BuildCost = buildCost;
        MaxLevel = maxLevel;
        MaxAllowedInKingdom = maxAllowedInKingdom;
        Description = description;
    }

    private final String DisplayName;
    private final String Description;
    private final long BuildCost;
    private final int MaxLevel;
    private final int MaxAllowedInKingdom;

}
