package com.bradyrussell.data.objects;

import com.bradyrussell.data.SqlObjectBase;

import java.sql.*;

public class Kingdom extends SqlObjectBase {
    public long id;
    public long owner;
    public String name;
    public int level;
    public Timestamp created;

    public static Kingdom get(Connection connection, long id) throws SQLException {
        Kingdom kingdom = new Kingdom();
        kingdom.id = id;
        if(kingdom.refresh(connection)) {
            return kingdom;
        }
        return null;
    }

    @Override
    public boolean create(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO kingdoms (owner, name) VALUES (?, ?)");
        statement.setLong(1, owner);
        statement.setString(2, name);
        //statement.setInt(3, level);
        boolean success = statement.executeUpdate() == 1;
        if(success) {
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    id = generatedKeys.getLong(1);
                }
                else {
                    throw new SQLException("Failed to obtain auto id!");
                }
            }
        }
        return success;
    }

    @Override
    public boolean update(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE kingdoms SET owner=?,name=?,level=? WHERE id=?");
        statement.setLong(1, owner);
        statement.setString(2, name);
        statement.setInt(3, level);
        statement.setLong(4, id);
        return statement.executeUpdate() == 1;
    }

    @Override
    public boolean refresh(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM kingdoms WHERE id = ?");
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();

        if(resultSet.next()) {
            //skip id
            owner = resultSet.getLong("owner");
            name = resultSet.getString("name");
            level = resultSet.getInt("level");
            created = resultSet.getTimestamp("created");
            resultSet.close();
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM kingdoms WHERE id=?");
        statement.setLong(1, id);
        return statement.executeUpdate() == 1;
    }
}
