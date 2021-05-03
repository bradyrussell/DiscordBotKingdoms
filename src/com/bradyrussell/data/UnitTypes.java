package com.bradyrussell.data;

public enum UnitTypes {
    Scout(null, 10, 1, 1, 100, Integer.MAX_VALUE, Integer.MAX_VALUE, ""),
    Swordsman(null, 10, 1, 1, 100, Integer.MAX_VALUE, Integer.MAX_VALUE, ""),
    Archer(null, 10, 1, 1, 100, Integer.MAX_VALUE, Integer.MAX_VALUE, ""),
    Wizard(null, 10, 1, 1, 100, Integer.MAX_VALUE, Integer.MAX_VALUE, ""),
    Cleric(null, 10, 1, 1, 100, Integer.MAX_VALUE, Integer.MAX_VALUE, ""),
    Cavalry(null, 10, 1, 1, 100, Integer.MAX_VALUE, Integer.MAX_VALUE, ""),
    Catapult(null, 10, 1, 1, 100, Integer.MAX_VALUE, Integer.MAX_VALUE, ""),
    SiegeTower("Siege Tower", 10, 1, 1, 100, Integer.MAX_VALUE, Integer.MAX_VALUE, ""),
    ;

    UnitTypes(String displayName, long trainingCost, long upkeepCost, int trainingTime, int maxHealth, int maxLevel, int maxAllowedInKingdom, String description) {
        DisplayName = displayName == null ? this.name() : displayName;
        TrainingTime = trainingTime;
        MaxHealth = maxHealth;
        Description = description;
        TrainingCost = trainingCost;
        UpkeepCost = upkeepCost;
        MaxLevel = maxLevel;
        MaxAllowedInKingdom = maxAllowedInKingdom;
    }

    public final String DisplayName;
    public final String Description;
    public final long TrainingCost;
    public final long UpkeepCost;
    public final int TrainingTime;
    public final int MaxHealth;
    public final int MaxLevel;
    public final int MaxAllowedInKingdom;

}
