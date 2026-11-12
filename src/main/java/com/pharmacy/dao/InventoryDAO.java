package com.pharmacy.dao;

import com.pharmacy.db.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class InventoryDAO {

    public List<Map<String, Object>> getAllInventory() {

        List<Map<String, Object>> inventoryList =
                new ArrayList<>();

        String sql = """
                SELECT
                    i.inventory_id,
                    i.medicine_id,
                    m.medicine_name,
                    m.generic_name,
                    m.category,
                    i.supplier_id,
                    s.supplier_name,
                    i.batch_number,
                    i.expiry_date,
                    i.quantity,
                    i.purchase_price,
                    i.selling_price,
                    i.reorder_level,
                    i.location
                FROM inventory i
                INNER JOIN medicines m
                    ON i.medicine_id = m.medicine_id
                LEFT JOIN suppliers s
                    ON i.supplier_id = s.supplier_id
               ORDER BY i.inventory_id ASC
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Map<String, Object> row =
                        new HashMap<>();

                row.put(
                        "inventory_id",
                        resultSet.getLong("inventory_id")
                );

                row.put(
                        "medicine_id",
                        resultSet.getLong("medicine_id")
                );

                row.put(
                        "medicine_name",
                        resultSet.getString("medicine_name")
                );

                row.put(
                        "generic_name",
                        resultSet.getString("generic_name")
                );

                row.put(
                        "category",
                        resultSet.getString("category")
                );

                row.put(
                        "supplier_id",
                        resultSet.getLong("supplier_id")
                );

                row.put(
                        "supplier_name",
                        resultSet.getString("supplier_name")
                );

                row.put(
                        "batch_number",
                        resultSet.getString("batch_number")
                );

                row.put(
                        "expiry_date",
                        resultSet.getDate("expiry_date")
                );

                row.put(
                        "quantity",
                        resultSet.getInt("quantity")
                );

                row.put(
                        "purchase_price",
                        resultSet.getBigDecimal(
                                "purchase_price"
                        )
                );

                row.put(
                        "selling_price",
                        resultSet.getBigDecimal(
                                "selling_price"
                        )
                );

                row.put(
                        "reorder_level",
                        resultSet.getInt("reorder_level")
                );

                row.put(
                        "location",
                        resultSet.getString("location")
                );

                inventoryList.add(row);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }


        return inventoryList;
    }

    public Map<String, Integer> getInventorySummary() {

        Map<String, Integer> summary = new HashMap<>();

        String sql = """
            SELECT
                COUNT(DISTINCT medicine_id) AS total_medicines,
                COALESCE(SUM(quantity), 0) AS total_units,
                COALESCE(SUM(
                    CASE
                        WHEN quantity > 0
                             AND quantity <= reorder_level
                        THEN 1
                        ELSE 0
                    END
                ), 0) AS low_stock,
                COALESCE(SUM(
                    CASE
                        WHEN quantity = 0
                        THEN 1
                        ELSE 0
                    END
                ), 0) AS out_of_stock
            FROM inventory
            """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            if (resultSet.next()) {

                summary.put(
                        "total_medicines",
                        resultSet.getInt("total_medicines")
                );

                summary.put(
                        "total_units",
                        resultSet.getInt("total_units")
                );

                summary.put(
                        "low_stock",
                        resultSet.getInt("low_stock")
                );

                summary.put(
                        "out_of_stock",
                        resultSet.getInt("out_of_stock")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return summary;
    }

    public boolean updateStock(
            long inventoryId,
            int newQuantity
    ) {

        String sql = """
            UPDATE inventory
            SET quantity = ?
            WHERE inventory_id = ?
            """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, newQuantity);
            statement.setLong(2, inventoryId);

            int rowsUpdated =
                    statement.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }


    }

    public long addInventory(
            long medicineId,
            Long supplierId,
            String batchNumber,
            Date expiryDate,
            int quantity,
            BigDecimal purchasePrice,
            BigDecimal sellingPrice,
            int reorderLevel,
            String location
    ) {

        String sql = """
        INSERT INTO inventory (
            medicine_id,
            supplier_id,
            batch_number,
            expiry_date,
            quantity,
            purchase_price,
            selling_price,
            reorder_level,
            location
        )
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.setLong(1, medicineId);

            if (supplierId == null) {
                statement.setNull(
                        2,
                        Types.BIGINT
                );
            } else {
                statement.setLong(
                        2,
                        supplierId
                );
            }

            statement.setString(
                    3,
                    batchNumber
            );

            statement.setDate(
                    4,
                    expiryDate
            );

            statement.setInt(
                    5,
                    quantity
            );

            statement.setBigDecimal(
                    6,
                    purchasePrice
            );

            statement.setBigDecimal(
                    7,
                    sellingPrice
            );

            statement.setInt(
                    8,
                    reorderLevel
            );

            statement.setString(
                    9,
                    location
            );

            int rowsInserted =
                    statement.executeUpdate();

            if (rowsInserted == 0) {
                return -1;
            }

            try (
                    ResultSet generatedKeys =
                            statement.getGeneratedKeys()
            ) {

                if (generatedKeys.next()) {

                    return generatedKeys.getLong(1);
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return -1;
    }

    public List<Map<String, Object>> getActiveMedicines() {

        List<Map<String, Object>> medicines =
                new ArrayList<>();

        String sql = """
        SELECT
            medicine_id,
            medicine_name
        FROM medicines
        WHERE status = 'ACTIVE'
        ORDER BY medicine_name
        """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Map<String, Object> medicine =
                        new HashMap<>();

                medicine.put(
                        "medicine_id",
                        resultSet.getLong("medicine_id")
                );

                medicine.put(
                        "medicine_name",
                        resultSet.getString("medicine_name")
                );

                medicines.add(medicine);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return medicines;
    }
    public List<Map<String, Object>> getActiveSuppliers() {

        List<Map<String, Object>> suppliers =
                new ArrayList<>();

        String sql = """
        SELECT
            supplier_id,
            supplier_name
        FROM suppliers
        WHERE status = 'ACTIVE'
        ORDER BY supplier_name
        """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Map<String, Object> supplier =
                        new HashMap<>();

                supplier.put(
                        "supplier_id",
                        resultSet.getLong("supplier_id")
                );

                supplier.put(
                        "supplier_name",
                        resultSet.getString("supplier_name")
                );

                suppliers.add(supplier);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return suppliers;
    }
}