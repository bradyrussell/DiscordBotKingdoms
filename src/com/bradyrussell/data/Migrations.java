package com.bradyrussell.data;

import com.bradyrussell.data.migrations.MigrationKingdom;
import com.bradyrussell.data.migrations.MigrationPlayers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.bradyrussell.Main.DatabaseConnectionString;

public class Migrations {
    public static void run(Connection connection) {
        ArrayList<MigrationBase> Migrations = new ArrayList<>(Arrays.asList(
                new MigrationPlayers(),
                new MigrationKingdom()
        ));

        System.out.println("Running migrations...");

        for (MigrationBase migration : Migrations) {
            System.out.print("Running migration "+migration.getName()+"...");
            try {
                migration.revertMigration(connection);
                migration.performMigration(connection);
            } catch (SQLException e){
                e.printStackTrace();
            }

            System.out.println(" Finished!");
        }

        System.out.println("Finished all migrations.");
    }

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DatabaseConnectionString)) {
            if (conn != null) {
                run(conn);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
