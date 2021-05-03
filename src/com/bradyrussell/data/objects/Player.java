package com.bradyrussell.data.objects;

import com.bradyrussell.data.SqlObjectBase;

import java.sql.*;

public class Player extends SqlObjectBase {
    public long userid;
    public long joined;

    public static Player get(Connection connection, long userid) throws SQLException {
        Player player = new Player();
        player.userid = userid;
        if(player.refresh(connection)) {
            return player;
        }
        return null;
    }

    @Override
    public boolean create(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO players (userid) VALUES (?)");
        statement.setLong(1, userid);
        return statement.executeUpdate() == 1;
    }

    @Override
    public boolean update(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE players SET /*joined=?*/ WHERE userid=?");
        //statement.setLong(2, userid);
        return statement.executeUpdate() == 1;
    }

    @Override
    public boolean refresh(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM players WHERE userid = ?");
        statement.setLong(1, userid);
        ResultSet resultSet = statement.executeQuery();

        if(resultSet.next()) {
            //skip userid
            joined = resultSet.getLong("joined");
            resultSet.close();
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM players WHERE userid=?");
        statement.setLong(1, userid);
        return statement.executeUpdate() == 1;
    }
}
