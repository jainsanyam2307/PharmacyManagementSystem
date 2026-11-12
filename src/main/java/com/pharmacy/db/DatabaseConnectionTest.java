package com.pharmacy.db;

import java.sql.Connection;

public class DatabaseConnectionTest {

    public static void main(String[] args) {

        try (Connection connection = DatabaseConnection.getConnection()) {

            if (connection != null && !connection.isClosed()) {
                System.out.println("Database connection successful!");
            }

        } catch (Exception e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
    }
}