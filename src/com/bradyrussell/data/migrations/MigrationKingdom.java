package com.bradyrussell.data.migrations;

import com.bradyrussell.data.MigrationBase;

import java.sql.Connection;
import java.sql.SQLException;

public class MigrationKingdom extends MigrationBase {
    @Override
    public void performMigration(Connection connection) throws SQLException {
        connection.prepareStatement("CREATE TABLE kingdoms (" +
                "id integer NOT NULL," +
                "owner integer NOT NULL," +
                "name text NOT NULL," +
                "level integer NOT NULL," +
                "population integer NOT NULL," +
                "created integer NOT NULL," +
                "updated integer NOT NULL, " +
                "PRIMARY KEY (id))"
        ).execute();
    }

    @Override
    public void revertMigration(Connection connection) throws SQLException {
        connection.prepareStatement("DROP TABLE IF EXISTS kingdoms").execute();
    }

    @Override
    public String getName() {
        return "Kingdoms";
    }
}
