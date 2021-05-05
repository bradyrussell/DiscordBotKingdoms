package com.bradyrussell.data.migrations;

import com.bradyrussell.data.MigrationBase;

import java.sql.Connection;
import java.sql.SQLException;

public class MigrationArmies extends MigrationBase {
    @Override
    public void performMigration(Connection connection) throws SQLException {
        connection.prepareStatement("CREATE TABLE armies (" +
                "id integer NOT NULL," +
                "kingdom integer NOT NULL REFERENCES kingdoms(id) ON DELETE CASCADE ," +
                "name text NOT NULL," +
                "ready integer NOT NULL," +
                "created integer NOT NULL," +
                "updated integer NOT NULL, " +
                "PRIMARY KEY (id))"
        ).execute();
    }

    @Override
    public void revertMigration(Connection connection) throws SQLException {
        connection.prepareStatement("DROP TABLE IF EXISTS armies").execute();
    }

    @Override
    public String getName() {
        return "Armies";
    }
}
