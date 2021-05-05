package com.bradyrussell.data;

public enum KingdomSizes {
    Settlement(null,1),
    Hamlet(null,3),
    Village(null,5),
    Shire(null,10),
    Town(null,15),
    District(null,25),
    County(null,50),
    Nation(null,100),
    Empire(null,500),
    ;

    public final String DisplayName;
    public final int MinimumLevel;

    KingdomSizes(String displayName, int minimumLevel) {
        DisplayName = displayName == null ? this.name() : displayName;
        MinimumLevel = minimumLevel;
    }

    public static KingdomSizes getByLevel(int level) {
        KingdomSizes size = null;
        for (KingdomSizes value : KingdomSizes.values()) {
            if(level >= value.MinimumLevel) size = value;
        }
        return size;
    }
}
