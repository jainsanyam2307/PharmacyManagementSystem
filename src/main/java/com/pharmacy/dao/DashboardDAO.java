package com.pharmacy.dao;
import java.util.List;
import java.util.ArrayList;
import com.pharmacy.db.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardDAO {

    // ============================================================
    // TODAY'S SALES
    // ============================================================

    public double getTodaySales() {

        String sql = """
                SELECT COALESCE(SUM(total_amount), 0)
                FROM sales
                WHERE DATE(sale_date) = CURDATE()
                """;

        return getDoubleValue(sql);
    }

    // ============================================================
    // TODAY'S PURCHASES
    // ============================================================

    public double getTodayPurchases() {

        String sql = """
                SELECT COALESCE(SUM(total_amount), 0)
                FROM purchases
                WHERE purchase_date = CURDATE()
                """;

        return getDoubleValue(sql);
    }

    // ============================================================
    // TOTAL MEDICINES IN STOCK
    // ============================================================

    public int getTotalMedicinesInStock() {

        String sql = """
                SELECT COALESCE(SUM(quantity), 0)
                FROM inventory
                """;

        return getIntValue(sql);
    }

    // ============================================================
    // LOW STOCK MEDICINES
    // ============================================================

    public int getLowStockCount() {

        String sql = """
                SELECT COUNT(*)
                FROM inventory
                WHERE quantity <= reorder_level
                """;

        return getIntValue(sql);
    }

    // ============================================================
    // EXPIRING MEDICINES
    // ============================================================

    public int getExpiringMedicineCount() {

        String sql = """
                SELECT COUNT(*)
                FROM inventory
                WHERE expiry_date BETWEEN CURDATE()
                AND DATE_ADD(CURDATE(), INTERVAL 30 DAY)
                """;

        return getIntValue(sql);
    }

    // ============================================================
    // TODAY'S CUSTOMERS
    // ============================================================

    public int getTodayCustomerCount() {

        String sql = """
                SELECT COUNT(DISTINCT customer_id)
                FROM sales
                WHERE DATE(sale_date) = CURDATE()
                """;

        return getIntValue(sql);
    }

    // ============================================================
    // HELPER - DOUBLE
    // ============================================================

    private double getDoubleValue(String sql) {

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            if (resultSet.next()) {
                return resultSet.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0.0;
    }

    // ============================================================
    // HELPER - INTEGER
    // ============================================================

    private int getIntValue(String sql) {

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public double getTodayProfit() {

        String sql = """
            SELECT COALESCE(
                SUM(
                    ((si.unit_price * si.quantity)
                    - si.discount_amount)
                    - (i.purchase_price * si.quantity)
                ),
                0
            )
            FROM sale_items si
            JOIN sales s
                ON si.sale_id = s.sale_id
            JOIN inventory i
                ON si.inventory_id = i.inventory_id
            WHERE DATE(s.sale_date) = CURDATE()
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0.0;
    }

    public double getPendingPayments() {

        String sql = """
            SELECT COALESCE(
                SUM(total_amount - paid_amount),
                0
            )
            FROM sales
            WHERE payment_status IN ('PENDING', 'PARTIAL')
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0.0;
    }
    public List<Object[]> getRecentSales() {

        String sql = """
            SELECT
                s.invoice_number,
                COALESCE(c.full_name, 'Walk-in Customer') AS customer_name,
                s.total_amount,
                s.payment_status,
                DATE_FORMAT(s.sale_date, '%h:%i %p') AS sale_time
            FROM sales s
            LEFT JOIN customers c
                ON s.customer_id = c.customer_id
            ORDER BY s.sale_date DESC
            LIMIT 5
            """;

        List<Object[]> sales = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                sales.add(new Object[]{
                        resultSet.getString("invoice_number"),
                        resultSet.getString("customer_name"),
                        String.format(
                                "₹%,.2f",
                                resultSet.getDouble("total_amount")
                        ),
                        resultSet.getString("payment_status"),
                        resultSet.getString("sale_time")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sales;
    }

    public List<Object[]> getLowStockMedicines() {

        String sql = """
            SELECT
               m.medicine_name AS medicine_name,
                SUM(i.quantity) AS stock,
                MAX(i.reorder_level) AS minimum
            FROM inventory i
            INNER JOIN medicines m
                ON i.medicine_id = m.medicine_id
            GROUP BY i.medicine_id, m.medicine_name
            HAVING SUM(i.quantity) <= MAX(i.reorder_level)
            ORDER BY SUM(i.quantity) ASC
            LIMIT 10
            """;

        List<Object[]> medicines = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                int stock = resultSet.getInt("stock");
                int minimum = resultSet.getInt("minimum");

                String status;

                if (stock <= minimum / 2) {
                    status = "Critical";
                } else {
                    status = "Low";
                }

                medicines.add(new Object[]{
                        resultSet.getString("medicine_name"),
                        stock,
                        minimum,
                        status
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return medicines;
    }
}