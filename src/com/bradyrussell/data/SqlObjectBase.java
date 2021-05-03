package com.bradyrussell.data;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class SqlObjectBase {
    // insert the object into the table
    public abstract boolean create(Connection connection) throws SQLException;
    // update the object
    public abstract boolean update(Connection connection) throws SQLException;
    // reload the object from the database
    public abstract boolean refresh(Connection connection) throws SQLException;
    //delete the object from the database
    public abstract boolean delete(Connection connection) throws SQLException;
}
