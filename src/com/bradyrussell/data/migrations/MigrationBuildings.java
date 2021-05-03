package com.bradyrussell.data.migrations;

import com.bradyrussell.data.MigrationBase;

import java.sql.Connection;
import java.sql.SQLException;

public class MigrationBuildings extends MigrationBase {
    @Override
    public void performMigration(Connection connection) throws SQLException {
        connection.prepareStatement("CREATE TABLE buildings (" +
                "id integer NOT NULL," +
                "kingdom integer NOT NULL," +
                "type text NOT NULL," +
                "level integer NOT NULL," +
                "health integer NOT NULL," +
                "created integer NOT NULL," +
                "updated integer NOT NULL, " +
                "PRIMARY KEY (id))"
        ).execute();
    }

    @Override
    public void revertMigration(Connection connection) throws SQLException {
        connection.prepareStatement("DROP TABLE IF EXISTS buildings").execute();
    }

    @Override
    public String getName() {
        return "Buildings";
    }
}
