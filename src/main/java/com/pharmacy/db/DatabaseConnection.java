package com.pharmacy.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/pharmacy_management"
                    + "?useSSL=false"
                    + "&serverTimezone=Asia/Kolkata"
                    + "&allowPublicKeyRetrieval=true";

    private static final String USERNAME = "root";

    // Replace this with the password you created for MySQL.
    private static final String PASSWORD = "@San123yam";

    private DatabaseConnection() {
        // Prevent object creation.
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                URL,
                USERNAME,
                PASSWORD
        );
    }
}