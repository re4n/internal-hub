package com.re4n.internalhub.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/internal_hub?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private static final String URL = System.getenv("DB_URL") != null ? System.getenv("DB_URL") : DEFAULT_URL;
    private static final String USER = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "root";
    private static final String PWD = System.getenv("DB_PWD") != null ? System.getenv("DB_PWD") : "";

    public static Connection createConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL,USER,PWD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL not found!", e);
        }catch (SQLException e){
            throw new SQLException("Failed to connect!" + e.getMessage());
        }
    }
}

