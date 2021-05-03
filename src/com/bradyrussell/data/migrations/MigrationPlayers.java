package com.bradyrussell.data.migrations;

import com.bradyrussell.data.MigrationBase;

import java.sql.Connection;
import java.sql.SQLException;

public class MigrationPlayers extends MigrationBase {
    @Override
    public void performMigration(Connection connection) throws SQLException {
        connection.prepareStatement("CREATE TABLE players (" +
                "userid integer NOT NULL, " +
                "joined integer NOT NULL DEFAULT (strftime('%s', 'now')), " +
                "PRIMARY KEY (userid))"
        ).execute();
    }

    @Override
    public void revertMigration(Connection connection) throws SQLException {
        connection.prepareStatement("DROP TABLE IF EXISTS players").execute();
    }

    @Override
    public String getName() {
        return "Players";
    }
}
