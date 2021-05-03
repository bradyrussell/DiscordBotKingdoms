package com.bradyrussell.data;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class MigrationBase {
    public abstract void performMigration(Connection connection) throws SQLException;
    public abstract void revertMigration(Connection connection) throws SQLException;
    public abstract String getName();
}
