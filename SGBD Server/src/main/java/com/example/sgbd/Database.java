package com.example.sgbd;

import java.sql.*;

public class Database {
    static Database database;

    private final Connection connection;

    private Database() throws SQLException, ClassNotFoundException {
        Class.forName ("oracle.jdbc.OracleDriver");

        String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
        String USER = "STUDENT";
        String PASS = "STUDENT";
        connection = DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public static Database getInstance() throws SQLException, ClassNotFoundException {
        if (database == null)
            database = new Database();
        return database;
    }

    public ResultSet executeQuery(String query) throws SQLException {
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    public Connection getConnection(){
        return connection;
    }

}
