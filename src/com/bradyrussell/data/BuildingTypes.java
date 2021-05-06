package com.bradyrussell.data;

import java.util.Map;

public enum BuildingTypes {
    //Command
    ThroneRoom("Throne Room", 0, 60, Integer.MAX_VALUE, 100, 1, 0, null,  "Somewhere for you to sit."),
    Keep(null, 1, 60, Integer.MAX_VALUE, 100, 1, 1, Map.of(BuildingTypes.ThroneRoom, 1),  "The center of your fortifications."),
    Castle(null, 1, 60, Integer.MAX_VALUE, 100, 1, 1, Map.of(BuildingTypes.Keep, 1),  "No peasants allowed!"),

    //Defense
    Gatehouse("Gatehouse", 1, 60, Integer.MAX_VALUE, 100, 1, 25, Map.of(BuildingTypes.Castle, 1),  "Well we can't just let anyone in!"),
    CastleWalls("Castle Walls", 1, 60, Integer.MAX_VALUE, 100, 1, 25, Map.of(BuildingTypes.Castle, 1),  "Walls are an important part of any castle."),
    Moat(null, 1, 60, Integer.MAX_VALUE, 100, 1, 50, Map.of(BuildingTypes.Castle, 5),  "Who knows what lurks in that water..."),
    KingdomWalls("Kingdom Walls", 1, 60, Integer.MAX_VALUE, 100, 1, 100, Map.of(BuildingTypes.Castle, 10),  "A great wall around the entire kingdom."),

    //Unit Production
    Barracks(null, 1, 60, Integer.MAX_VALUE, 100, 1, 5, Map.of(BuildingTypes.ThroneRoom, 1),  ""),
    Stable(null, 1, 60, Integer.MAX_VALUE,100, 1, 5, Map.of(BuildingTypes.ThroneRoom, 1),  ""),
    ArcheryRange("Archery Range", 1, 60, Integer.MAX_VALUE, 100, 1, 5, Map.of(BuildingTypes.ThroneRoom, 1), ""),
    WizardTower("Wizard Tower", 1, 60, Integer.MAX_VALUE, 100, 1, 5, Map.of(BuildingTypes.ThroneRoom, 1),  ""),
    Temple(null, 1, 60, Integer.MAX_VALUE, 100, 1, 5, Map.of(BuildingTypes.ThroneRoom, 1),  ""),
    SiegeWorkshop("Siege Workshop", 1, 60, Integer.MAX_VALUE, 100, 1, 5, Map.of(BuildingTypes.ThroneRoom, 1),  ""),

    //Resource Production
    Farm(null, 1, 60, Integer.MAX_VALUE, 100, 1, 3, Map.of(BuildingTypes.ThroneRoom, 1),  ""),
    Granary(null, 1, 60, Integer.MAX_VALUE, 100, 1, 3, Map.of(BuildingTypes.Farm, 1),  ""),
    CopperMine("Copper Mine", 1, 60, Integer.MAX_VALUE, 100, 1, 3, Map.of(BuildingTypes.ThroneRoom, 1),  ""),
    IronMine("Iron Mine", 1, 60, Integer.MAX_VALUE, 100, 1, 3, Map.of(BuildingTypes.CopperMine, 1),  ""),
    SilverMine("Silver Mine", 1, 60, Integer.MAX_VALUE, 100, 1, 3, Map.of(BuildingTypes.IronMine, 1),  ""),
    GoldMine("Gold Mine", 1, 60, Integer.MAX_VALUE, 100, 1, 3, Map.of(BuildingTypes.SilverMine, 1),  ""),
    Mint(null, 1, 60, Integer.MAX_VALUE, 100, 1, 3, Map.of(BuildingTypes.GoldMine, 1), "Money printer go brrr..."),
    ;

    BuildingTypes(String displayName, long buildCost, int buildingTime, int maxLevel, int maxHealth, int maxAllowedInKingdom, int defenseLayer, Map<BuildingTypes, Integer> prerequisites, String description) {
        DisplayName = (displayName == null ? this.name() : displayName);
        BuildCost = buildCost;
        BuildingTime = buildingTime;
        MaxLevel = maxLevel;
        MaxHealth = maxHealth;
        MaxAllowedInKingdom = maxAllowedInKingdom;
        DefenseLayer = defenseLayer;
        Prerequisites = prerequisites;
        Description = description;
    }

    public static BuildingTypes search(String search){
        for (BuildingTypes value : BuildingTypes.values()) {
            if(value.name().equalsIgnoreCase(search) || value.DisplayName.equalsIgnoreCase(search)) return value;
        }
        return null;
    }

    public String prerequisitesString() {
        if(this.Prerequisites == null) return "None";
        StringBuilder sb = new StringBuilder();

        boolean bFirst = true;



        for (Map.Entry<BuildingTypes, Integer> entry : this.Prerequisites.entrySet()) {
            if(!bFirst) {
                sb.append(", ");
            } else {
                bFirst = false;
            }
            sb.append(entry.getKey().DisplayName).append(" Level ").append(entry.getValue());
        }
        return sb.toString();
    }

    public final String DisplayName;
    public final String Description;
    public final long BuildCost;
    public final int MaxLevel;
    public final int MaxHealth;
    public final int MaxAllowedInKingdom;
    public final int BuildingTime;
    // buildings with a higher number take damage first when kingdom is under attack
    public final int DefenseLayer;
                        //building , level
    public final Map<BuildingTypes, Integer> Prerequisites;
}
