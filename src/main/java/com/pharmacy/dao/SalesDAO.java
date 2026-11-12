package com.pharmacy.dao;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.pharmacy.db.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SalesDAO {

    /**
     * Finds the available inventory record for a medicine
     * using the medicine name.
     *
     * Returns:
     * inventory_id
     * medicine_id
     * medicine_name
     * quantity
     * selling_price
     * batch_number
     *
     * Returns null if the medicine is not available.
     */
    public java.util.Map<String, Object> findMedicine(
            String medicineName
    ) {

        String sql = """
                SELECT
                    i.inventory_id,
                    i.medicine_id,
                    m.medicine_name,
                    i.quantity,
                    i.selling_price,
                    i.batch_number
                FROM inventory i
                INNER JOIN medicines m
                    ON i.medicine_id = m.medicine_id
                WHERE LOWER(m.medicine_name) = LOWER(?)
                  AND i.quantity > 0
                  AND m.status = 'ACTIVE'
                ORDER BY i.expiry_date ASC
                LIMIT 1
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    medicineName.trim()
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    java.util.Map<String, Object> medicine =
                            new java.util.HashMap<>();

                    medicine.put(
                            "inventory_id",
                            resultSet.getLong("inventory_id")
                    );

                    medicine.put(
                            "medicine_id",
                            resultSet.getLong("medicine_id")
                    );

                    medicine.put(
                            "medicine_name",
                            resultSet.getString("medicine_name")
                    );

                    medicine.put(
                            "quantity",
                            resultSet.getInt("quantity")
                    );

                    medicine.put(
                            "selling_price",
                            resultSet.getBigDecimal(
                                    "selling_price"
                            )
                    );

                    medicine.put(
                            "batch_number",
                            resultSet.getString("batch_number")
                    );

                    return medicine;
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return null;
    }

    public String createSale(
            List<Map<String, Object>> items,
            BigDecimal subtotal,
            BigDecimal discountAmount,
            BigDecimal taxAmount,
            BigDecimal totalAmount,
            String paymentMethod
    ) {

        if (items == null || items.isEmpty()) {
            return null;
        }

        String invoiceNumber =
                "INV-"
                        + System.currentTimeMillis();

        Connection connection = null;

        try {

            connection =
                    DatabaseConnection.getConnection();

            connection.setAutoCommit(false);

            // =========================================================
            // 1. CREATE SALE
            // =========================================================

            String saleSql = """
                INSERT INTO sales (
                    customer_id,
                    invoice_number,
                    subtotal,
                    tax_amount,
                    discount_amount,
                    total_amount,
                    payment_status,
                    payment_method,
                    paid_amount
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

            long saleId;

            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    saleSql,
                                    Statement.RETURN_GENERATED_KEYS
                            )
            ) {

                // Customer is optional for now.
                statement.setNull(
                        1,
                        java.sql.Types.BIGINT
                );

                statement.setString(
                        2,
                        invoiceNumber
                );

                statement.setBigDecimal(
                        3,
                        subtotal
                );

                statement.setBigDecimal(
                        4,
                        taxAmount
                );

                statement.setBigDecimal(
                        5,
                        discountAmount
                );

                statement.setBigDecimal(
                        6,
                        totalAmount
                );

                statement.setString(
                        7,
                        "PAID"
                );

                statement.setString(
                        8,
                        paymentMethod
                );

                statement.setBigDecimal(
                        9,
                        totalAmount
                );

                int rows =
                        statement.executeUpdate();

                if (rows == 0) {
                    throw new SQLException(
                            "Sale could not be created."
                    );
                }

                try (
                        ResultSet keys =
                                statement.getGeneratedKeys()
                ) {

                    if (!keys.next()) {
                        throw new SQLException(
                                "Sale ID was not generated."
                        );
                    }

                    saleId =
                            keys.getLong(1);
                }
            }

            // =========================================================
            // 2. INSERT SALE ITEMS + REDUCE INVENTORY
            // =========================================================

            String inventorySql = """
                SELECT
                    i.inventory_id,
                    i.medicine_id,
                    i.batch_number,
                    i.quantity,
                    i.selling_price
                FROM inventory i
                INNER JOIN medicines m
                    ON i.medicine_id = m.medicine_id
                WHERE LOWER(m.medicine_name) = LOWER(?)
                  AND m.status = 'ACTIVE'
                  AND i.quantity > 0
                ORDER BY i.expiry_date ASC
                LIMIT 1
                FOR UPDATE
                """;

            String itemSql = """
                INSERT INTO sale_items (
                    sale_id,
                    medicine_id,
                    inventory_id,
                    batch_number,
                    quantity,
                    unit_price,
                    discount_amount,
                    tax_percent,
                    total_amount
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

            String updateInventorySql = """
                UPDATE inventory
                SET quantity = quantity - ?
                WHERE inventory_id = ?
                  AND quantity >= ?
                """;

            BigDecimal calculatedSubtotal =
                    BigDecimal.ZERO;

            for (Map<String, Object> item : items) {

                String medicineName =
                        String.valueOf(
                                item.get("medicine_name")
                        );

                int quantity =
                        ((Number)
                                item.get("quantity")
                        ).intValue();

                if (quantity <= 0) {
                    throw new SQLException(
                            "Invalid quantity for "
                                    + medicineName
                    );
                }

                // -----------------------------------------------------
                // Find inventory row and lock it
                // -----------------------------------------------------

                long inventoryId;
                long medicineId;
                String batchNumber;
                int availableQuantity;
                BigDecimal unitPrice;

                try (
                        PreparedStatement statement =
                                connection.prepareStatement(
                                        inventorySql
                                )
                ) {

                    statement.setString(
                            1,
                            medicineName
                    );

                    try (
                            ResultSet resultSet =
                                    statement.executeQuery()
                    ) {

                        if (!resultSet.next()) {

                            throw new SQLException(
                                    "Medicine not available: "
                                            + medicineName
                            );
                        }

                        inventoryId =
                                resultSet.getLong(
                                        "inventory_id"
                                );

                        medicineId =
                                resultSet.getLong(
                                        "medicine_id"
                                );

                        batchNumber =
                                resultSet.getString(
                                        "batch_number"
                                );

                        availableQuantity =
                                resultSet.getInt(
                                        "quantity"
                                );

                        unitPrice =
                                resultSet.getBigDecimal(
                                        "selling_price"
                                );
                    }
                }

                if (quantity > availableQuantity) {

                    throw new SQLException(
                            "Insufficient stock for "
                                    + medicineName
                                    + ". Available: "
                                    + availableQuantity
                    );
                }

                if (unitPrice == null) {

                    throw new SQLException(
                            "Selling price missing for "
                                    + medicineName
                    );
                }

                // -----------------------------------------------------
                // Calculate item subtotal
                // -----------------------------------------------------

                BigDecimal itemSubtotal =
                        unitPrice.multiply(
                                BigDecimal.valueOf(
                                        quantity
                                )
                        );

                calculatedSubtotal =
                        calculatedSubtotal.add(
                                itemSubtotal
                        );

                // -----------------------------------------------------
                // Allocate overall discount proportionally
                // -----------------------------------------------------

                BigDecimal itemDiscount =
                        BigDecimal.ZERO;

                if (subtotal.compareTo(
                        BigDecimal.ZERO
                ) > 0) {

                    itemDiscount =
                            discountAmount
                                    .multiply(itemSubtotal)
                                    .divide(
                                            subtotal,
                                            2,
                                            RoundingMode.HALF_UP
                                    );
                }

                BigDecimal taxableItemAmount =
                        itemSubtotal.subtract(
                                itemDiscount
                        );

                // 5% GST
                BigDecimal itemTax =
                        taxableItemAmount
                                .multiply(
                                        new BigDecimal("0.05")
                                )
                                .setScale(
                                        2,
                                        RoundingMode.HALF_UP
                                );

                BigDecimal itemTotal =
                        taxableItemAmount
                                .add(itemTax)
                                .setScale(
                                        2,
                                        RoundingMode.HALF_UP
                                );

                // -----------------------------------------------------
                // Insert sale item
                // -----------------------------------------------------

                try (
                        PreparedStatement statement =
                                connection.prepareStatement(
                                        itemSql
                                )
                ) {

                    statement.setLong(
                            1,
                            saleId
                    );

                    statement.setLong(
                            2,
                            medicineId
                    );

                    statement.setLong(
                            3,
                            inventoryId
                    );

                    statement.setString(
                            4,
                            batchNumber
                    );

                    statement.setInt(
                            5,
                            quantity
                    );

                    statement.setBigDecimal(
                            6,
                            unitPrice
                    );

                    statement.setBigDecimal(
                            7,
                            itemDiscount
                    );

                    statement.setBigDecimal(
                            8,
                            new BigDecimal("5.00")
                    );

                    statement.setBigDecimal(
                            9,
                            itemTotal
                    );

                    statement.executeUpdate();
                }

                // -----------------------------------------------------
                // Reduce inventory
                // -----------------------------------------------------

                try (
                        PreparedStatement statement =
                                connection.prepareStatement(
                                        updateInventorySql
                                )
                ) {

                    statement.setInt(
                            1,
                            quantity
                    );

                    statement.setLong(
                            2,
                            inventoryId
                    );

                    statement.setInt(
                            3,
                            quantity
                    );

                    int updated =
                            statement.executeUpdate();

                    if (updated == 0) {

                        throw new SQLException(
                                "Inventory update failed for "
                                        + medicineName
                        );
                    }
                }
            }

            // =========================================================
            // 3. VERIFY SUBTOTAL
            // =========================================================

            calculatedSubtotal =
                    calculatedSubtotal.setScale(
                            2,
                            RoundingMode.HALF_UP
                    );

            subtotal =
                    subtotal.setScale(
                            2,
                            RoundingMode.HALF_UP
                    );

            if (calculatedSubtotal.compareTo(
                    subtotal
            ) != 0) {

                throw new SQLException(
                        "Sale subtotal mismatch."
                );
            }

            // =========================================================
            // 4. COMMIT EVERYTHING
            // =========================================================

            connection.commit();

            return invoiceNumber;

        } catch (SQLException e) {

            if (connection != null) {

                try {
                    connection.rollback();
                } catch (SQLException rollbackError) {
                    rollbackError.printStackTrace();
                }
            }

            e.printStackTrace();

            return null;

        } finally {

            if (connection != null) {

                try {

                    connection.setAutoCommit(true);
                    connection.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }




    public List<Map<String, Object>> getRecentSales() {

        List<Map<String, Object>> sales =
                new ArrayList<>();

        String sql = """
            SELECT
                sale_id,
                invoice_number,
                sale_date,
                subtotal,
                tax_amount,
                discount_amount,
                total_amount,
                payment_status,
                payment_method,
                paid_amount
            FROM sales
            ORDER BY sale_date DESC
            LIMIT 100
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

                Map<String, Object> sale =
                        new HashMap<>();

                sale.put(
                        "sale_id",
                        resultSet.getLong("sale_id")
                );

                sale.put(
                        "invoice_number",
                        resultSet.getString(
                                "invoice_number"
                        )
                );

                sale.put(
                        "sale_date",
                        resultSet.getTimestamp(
                                "sale_date"
                        )
                );

                sale.put(
                        "subtotal",
                        resultSet.getBigDecimal(
                                "subtotal"
                        )
                );

                sale.put(
                        "tax_amount",
                        resultSet.getBigDecimal(
                                "tax_amount"
                        )
                );

                sale.put(
                        "discount_amount",
                        resultSet.getBigDecimal(
                                "discount_amount"
                        )
                );

                sale.put(
                        "total_amount",
                        resultSet.getBigDecimal(
                                "total_amount"
                        )
                );

                sale.put(
                        "payment_status",
                        resultSet.getString(
                                "payment_status"
                        )
                );

                sale.put(
                        "payment_method",
                        resultSet.getString(
                                "payment_method"
                        )
                );

                sale.put(
                        "paid_amount",
                        resultSet.getBigDecimal(
                                "paid_amount"
                        )
                );

                sales.add(sale);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return sales;
    }
}