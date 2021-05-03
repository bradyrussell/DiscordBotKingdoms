package com.bradyrussell;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static String DatabaseConnectionString = "jdbc:sqlite:kingdom";
    public static String TestDatabaseConnectionString = "jdbc:sqlite:kingdom_test";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DatabaseConnectionString)) {
            if (conn != null) {

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
