package com.re4n.internalhub.config;

import com.re4n.internalhub.enums.AppError;
import com.re4n.internalhub.exception.AppException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/internalhub?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private static final String URL = System.getenv("DB_URL") != null ? System.getenv("DB_URL") : DEFAULT_URL;
    private static final String USER = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "root";
    private static final String PWD = System.getenv("DB_PWD") != null ? System.getenv("DB_PWD") : ".";

    public static Connection createConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL,USER,PWD);
        } catch (ClassNotFoundException e) {
            throw new AppException(AppError.DB_DRIVER_NOT_FOUND, e);
        }catch (SQLException e){
            throw new AppException(AppError.DB_CONNECTION_FAILED, e);
        }
    }
}

