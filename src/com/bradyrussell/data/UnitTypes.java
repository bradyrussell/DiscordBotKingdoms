package com.bradyrussell.data;

public enum UnitTypes {
    Scout(null, 10, 1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, ""),
    Swordsman(null, 10, 1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, ""),
    Archer(null, 10, 1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, ""),
    Wizard(null, 10, 1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, ""),
    Cleric(null, 10, 1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, ""),
    Cavalry(null, 10, 1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, ""),
    Catapult(null, 10, 1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, ""),
    SiegeTower("Siege Tower", 10, 1, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, ""),
    ;

    UnitTypes(String displayName, long trainingCost, long upkeepCost, int trainingTime, int maxLevel, int maxAllowedInKingdom, String description) {
        DisplayName = displayName == null ? this.name() : displayName;
        TrainingTime = trainingTime;
        Description = description;
        TrainingCost = trainingCost;
        UpkeepCost = upkeepCost;
        MaxLevel = maxLevel;
        MaxAllowedInKingdom = maxAllowedInKingdom;
    }

    private final String DisplayName;
    private final String Description;
    private final long TrainingCost;
    private final long UpkeepCost;
    private final int TrainingTime;
    private final int MaxLevel;
    private final int MaxAllowedInKingdom;
}
