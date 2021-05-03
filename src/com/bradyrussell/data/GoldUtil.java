package com.bradyrussell.data;

public class GoldUtil {
    public static long getSilver(long Copper) {
        return Copper / 100;
    }

    public static long getGold(long Copper) {
        return Copper / 10000;
    }

    public static long getCopperPart(long Copper) {
        return Copper % 100;
    }

    public static long getSilverPart(long Copper) {
        return getSilver(Copper % 10000);
    }

    public static String getGoldSilverCopper(long Copper) {
        if(Copper <= 0) return "0 copper";
        long gold = getGold(Copper);
        long silver = getSilverPart(Copper);
        long copper = getCopperPart(Copper);

        return (gold > 0 ? gold + " gold" : "") + (gold > 0 && silver > 0 || gold > 0 && copper > 0 ? ", ":"") + (silver > 0 ? silver + " silver" : "") + (silver > 0 && copper > 0 ? ", ":"") + (copper > 0 ? copper + " copper" : "");
    }
}
