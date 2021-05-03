package com.bradyrussell.data;

import java.util.Map;

public enum BuildingTypes {
    //Command
    ThroneRoom("Throne Room", 0, Integer.MAX_VALUE, 100, 1, 0, null, null, "Somewhere for you to sit."),
    Keep(null, 1, Integer.MAX_VALUE, 100, 1, 1, Map.of(BuildingTypes.ThroneRoom, 1), null, "The center of your fortifications."),
    Castle(null, 1, Integer.MAX_VALUE, 100, 1, 1, Map.of(BuildingTypes.Keep, 1), null, "No peasants allowed!"),

    //Defense
    Gatehouse("Gatehouse", 1, Integer.MAX_VALUE, 100, 1, 25, Map.of(BuildingTypes.Castle, 1), null, "Well we can't just let anyone in!"),
    CastleWalls("Castle Walls", 1, Integer.MAX_VALUE, 100, 1, 25, Map.of(BuildingTypes.Castle, 1), null, "Walls are an important part of any castle."),
    Moat(null, 1, Integer.MAX_VALUE, 100, 1, 50, Map.of(BuildingTypes.Castle, 5), null, "Who knows what lurks in that water..."),
    KingdomWalls("Castle Walls", 1, Integer.MAX_VALUE, 100, 1, 100, Map.of(BuildingTypes.Castle, 10), null, "A great wall around the entire kingdom."),

    //Unit Production
    Barracks(null, 1, Integer.MAX_VALUE, 100, 1, 5, Map.of(BuildingTypes.ThroneRoom, 1), Map.of(UnitTypes.Swordsman, 1), ""),
    Stable(null, 1, Integer.MAX_VALUE,100, 1, 5, Map.of(BuildingTypes.ThroneRoom, 1), Map.of(UnitTypes.Cavalry, 1), ""),
    ArcheryRange("Archery Range", 1, Integer.MAX_VALUE, 100, 1, 5, Map.of(BuildingTypes.ThroneRoom, 1), Map.of(UnitTypes.Archer, 1), ""),
    WizardTower("Wizard Tower", 1, Integer.MAX_VALUE, 100, 1, 5, Map.of(BuildingTypes.ThroneRoom, 1), Map.of(UnitTypes.Wizard, 1), ""),
    Temple(null, 1, Integer.MAX_VALUE, 100, 1, 5, Map.of(BuildingTypes.ThroneRoom, 1), Map.of(UnitTypes.Cleric, 1), ""),
    SiegeWorkshop("Siege Workshop", 1, Integer.MAX_VALUE, 100, 1, 5, Map.of(BuildingTypes.ThroneRoom, 1), Map.of(UnitTypes.SiegeTower, 1, UnitTypes.Catapult, 2), ""),

    //Resource Production
    Farm(null, 1, Integer.MAX_VALUE, 100, 1, 3, Map.of(BuildingTypes.ThroneRoom, 1), null, ""),
    Granary(null, 1, Integer.MAX_VALUE, 100, 1, 3, Map.of(BuildingTypes.Farm, 1), null, ""),
    CopperMine("Copper Mine", 1, Integer.MAX_VALUE, 100, 1, 3, Map.of(BuildingTypes.ThroneRoom, 1), null, ""),
    IronMine("Iron Mine", 1, Integer.MAX_VALUE, 100, 1, 3, Map.of(BuildingTypes.CopperMine, 1), null, ""),
    SilverMine("Silver Mine", 1, Integer.MAX_VALUE, 100, 1, 3, Map.of(BuildingTypes.IronMine, 1), null, ""),
    GoldMine("Gold Mine", 1, Integer.MAX_VALUE, 100, 1, 3, Map.of(BuildingTypes.SilverMine, 1), null, ""),
    Mint(null, 1, Integer.MAX_VALUE, 100, 1, 3, Map.of(BuildingTypes.GoldMine, 1), null, "Money printer go brrr..."),
    ;

    BuildingTypes(String displayName, long buildCost, int maxLevel, int maxHealth, int maxAllowedInKingdom, int defenseLayer, Map<BuildingTypes, Integer> prerequisites, Map<UnitTypes, Integer> canTrainUnitAtLevel, String description) {
        DisplayName = displayName == null ? this.name() : displayName;
        BuildCost = buildCost;
        MaxLevel = maxLevel;
        MaxHealth = maxHealth;
        MaxAllowedInKingdom = maxAllowedInKingdom;
        DefenseLayer = defenseLayer;
        Prerequisites = prerequisites;
        CanTrainUnitAtLevel = canTrainUnitAtLevel;
        Description = description;
    }

    public final String DisplayName;
    public final String Description;
    public final long BuildCost;
    public final int MaxLevel;
    public final int MaxHealth;
    public final int MaxAllowedInKingdom;
    // buildings with a higher number take damage first when kingdom is under attack
    public final int DefenseLayer;
                        //building , level
    public final Map<BuildingTypes, Integer> Prerequisites;
                        // unit , level
    public final Map<UnitTypes, Integer> CanTrainUnitAtLevel;
}
