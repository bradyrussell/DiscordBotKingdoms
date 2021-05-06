package com.bradyrussell.data;

public enum AssetEmojis {
    CoinCopper("<:copper_coin:839290682309214260>"),
    CoinSilver("<:silver_coin:839290682413678683>"),
    CoinGold("<:gold_coin:839290682103038014>"),
    CoinStack("<:coin_stack:839294455453974549>"),

    Sword("<:sword:839294455524884541>"),
    Sword2("<:sword_2:839294455769202718>"),
    Sword3("<:sword_3:839294455676010516>"),

    PotionBlue("<:blue_potion:839294455617683486>"), // can apply potions to armies than apply buffs next combat?
    PotionGreen("<:green_potion:839294455692787712>"),
    PotionRed("<:red_potion:839294455596580884>"),

    Helmet("<:helmet_1:839294455303372821>"),
    Helmet2("<:helmet_2:839294455595925504>"),

    Bow("<:bow:839294455608508436>"),

    Ruby("<:ruby:839294455663951872>"),
    Sapphire("<:sapphire:839294455358292009>"),
    Emerald("<:emerald:839294455524884531>"),

    KeyCopper("<:copper_key:839294455399710772>"),
    KeySilver("<:silver_key:839294455679811604>"),
    KeyGold("<:gold_key:839294455386734614> "),

    Scroll("<:scroll:839294455667621909>"),
    Tablet("<:tablet:839294455354097716>"),
    ;

    AssetEmojis(String code) {
        this.code = code;
    }

    public String get() {
        return code;
    }

    public final String code;
}
