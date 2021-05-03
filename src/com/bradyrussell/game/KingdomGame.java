package com.bradyrussell.game;

import java.sql.Connection;

public class KingdomGame {
    private Connection database;

    public KingdomGame(Connection database) {
        this.database = database;
    }


}
