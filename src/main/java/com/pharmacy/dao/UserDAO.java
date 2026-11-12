package com.pharmacy.dao;
import com.pharmacy.db.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.mindrot.jbcrypt.BCrypt;
public class UserDAO {

    // =========================================================
    // Authenticate User
    // =========================================================

    public boolean authenticate(String username, String password) {

        String sql = """
            SELECT password_hash
            FROM users
            WHERE username = ?
              AND is_active = TRUE
            """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    String storedHash =
                            resultSet.getString("password_hash");

                    return BCrypt.checkpw(
                            password,
                            storedHash
                    );
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    // =========================================================
    // Create User + Staff
    // =========================================================

    public boolean createUser(
            String fullName,
            String username,
            String email,
            String phone,
            String password
    ) {

        String userSql = """
        INSERT INTO users (
            username,
            email,
            password_hash,
            role,
            is_active
        )
        VALUES (?, ?, ?, 'ASSISTANT', TRUE)
        """;

        String staffSql = """
        INSERT INTO staff (
            user_id,
            full_name,
            phone,
            email,
            role,
            joining_date,
            status
        )
        VALUES (?, ?, ?, ?, 'ASSISTANT', CURDATE(), 'ACTIVE')
        """;
        Connection connection = null;

        try {

            connection = DatabaseConnection.getConnection();

            // Start transaction
            connection.setAutoCommit(false);

            long userId;

            // -------------------------------------------------
            // Insert into users
            // -------------------------------------------------

            try (PreparedStatement userStatement =
                         connection.prepareStatement(
                                 userSql,
                                 PreparedStatement.RETURN_GENERATED_KEYS
                         )) {

                userStatement.setString(1, username);
                userStatement.setString(2, email);
                String passwordHash =
                        BCrypt.hashpw(
                                password,
                                BCrypt.gensalt(12)
                        );

                userStatement.setString(3, passwordHash);

                int rowsInserted =
                        userStatement.executeUpdate();

                if (rowsInserted == 0) {
                    connection.rollback();
                    return false;
                }

                try (ResultSet keys =
                             userStatement.getGeneratedKeys()) {

                    if (!keys.next()) {
                        connection.rollback();
                        return false;
                    }

                    userId = keys.getLong(1);
                }
            }

            // -------------------------------------------------
            // Insert into staff
            // -------------------------------------------------

            try (PreparedStatement staffStatement =
                         connection.prepareStatement(staffSql)) {

                staffStatement.setLong(1, userId);
                staffStatement.setString(2, fullName);
                staffStatement.setString(3, phone);
                staffStatement.setString(4, email);

                int rowsInserted =
                        staffStatement.executeUpdate();

                if (rowsInserted == 0) {
                    connection.rollback();
                    return false;
                }
            }

            // Everything succeeded
            connection.commit();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (Exception rollbackException) {
                rollbackException.printStackTrace();
            }

            return false;
        } finally {

            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (Exception closeException) {
                closeException.printStackTrace();
            }
        }
    }
}