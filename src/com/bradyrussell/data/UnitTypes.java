package com.bradyrussell.data;

public enum UnitTypes {
    Scout(null, BuildingTypes.Keep, 1, 10, 1, 100, 100, Integer.MAX_VALUE, Integer.MAX_VALUE, "Scouts go on scouting missions. Cannot be assigned to an army."),
    Swordsman(null, BuildingTypes.Barracks, 1, 10, 1, 1, 100, Integer.MAX_VALUE, Integer.MAX_VALUE, "Does damage to enemy units and buildings."),
    Archer(null, BuildingTypes.ArcheryRange, 1, 10, 1, 1, 100, Integer.MAX_VALUE, Integer.MAX_VALUE, "Does damage to enemies. If living melee units are in the same army, takes much less damage."),
    Wizard(null, BuildingTypes.WizardTower, 1, 10, 1, 1, 100, Integer.MAX_VALUE, Integer.MAX_VALUE, "idk"),
    Cleric(null, BuildingTypes.Temple, 1, 10, 1, 1, 100, Integer.MAX_VALUE, Integer.MAX_VALUE, "Heals injured units."),
    Cavalry(null, BuildingTypes.Stable, 1, 10, 1, 1, 100, Integer.MAX_VALUE, Integer.MAX_VALUE, "idk"),
    Catapult(null, BuildingTypes.SiegeWorkshop, 5, 10, 1, 1, 100, Integer.MAX_VALUE, Integer.MAX_VALUE, "Does damage to enemy units and buildings. Does significant damage to buildings."),
    SiegeTower("Siege Tower", BuildingTypes.SiegeWorkshop, 1, 10, 1, 1, 100, Integer.MAX_VALUE, Integer.MAX_VALUE, "An army with a living siege tower does significantly more damage to the outermost layer of a kingdom."),
    ;

    UnitTypes(String displayName, BuildingTypes trainingBuilding, int trainingBuildingLevel, long trainingCost, long upkeepCost, int trainingTime, int maxHealth, int maxLevel, int maxAllowedInKingdom, String description) {
        DisplayName = (displayName == null ? this.name() : displayName);
        TrainingBuilding = trainingBuilding;
        TrainingBuildingLevel = trainingBuildingLevel;
        TrainingTime = trainingTime;
        MaxHealth = maxHealth;
        Description = description;
        TrainingCost = trainingCost;
        UpkeepCost = upkeepCost;
        MaxLevel = maxLevel;
        MaxAllowedInKingdom = maxAllowedInKingdom;
    }

    public static UnitTypes search(String search){
        for (UnitTypes value : UnitTypes.values()) {
            if(value.name().equalsIgnoreCase(search) || value.DisplayName.equalsIgnoreCase(search)) return value;
        }
        return null;
    }

    public final String DisplayName;
    public final String Description;
    public final long TrainingCost;
    public final BuildingTypes TrainingBuilding;
    public final int TrainingBuildingLevel;
    public final long UpkeepCost;
    public final int TrainingTime;
    public final int MaxHealth;
    public final int MaxLevel;
    public final int MaxAllowedInKingdom;

}
