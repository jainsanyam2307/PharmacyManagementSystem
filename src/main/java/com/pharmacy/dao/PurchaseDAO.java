package com.pharmacy.dao;

import com.pharmacy.db.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class PurchaseDAO {

    public boolean createPurchase(
            long supplierId,
            String invoiceNumber,
            Date purchaseDate,
            BigDecimal subtotal,
            BigDecimal taxAmount,
            BigDecimal discountAmount,
            BigDecimal totalAmount,
            String paymentStatus,
            String paymentMethod,
            BigDecimal paidAmount,
            String notes,
            long createdBy,
            List<PurchaseItem> items
    ) {

        String purchaseSql = """
                INSERT INTO purchases (
                    supplier_id,
                    invoice_number,
                    purchase_date,
                    subtotal,
                    tax_amount,
                    discount_amount,
                    total_amount,
                    payment_status,
                    payment_method,
                    paid_amount,
                    notes,
                    created_by
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        String itemSql = """
                INSERT INTO purchase_items (
                    purchase_id,
                    medicine_id,
                    batch_number,
                    expiry_date,
                    quantity,
                    unit_cost,
                    tax_percent,
                    discount_amount,
                    total_amount
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        String inventoryCheckSql = """
                SELECT inventory_id
                FROM inventory
                WHERE medicine_id = ?
                  AND batch_number = ?
                LIMIT 1
                """;

        String inventoryUpdateSql = """
                UPDATE inventory
                SET quantity = quantity + ?,
                    supplier_id = ?,
                    expiry_date = ?,
                    purchase_price = ?,
                    selling_price = ?,
                    updated_at = CURRENT_TIMESTAMP
                WHERE inventory_id = ?
                """;

        String inventoryInsertSql = """
                INSERT INTO inventory (
                    medicine_id,
                    supplier_id,
                    batch_number,
                    expiry_date,
                    quantity,
                    purchase_price,
                    selling_price,
                    reorder_level
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {

                long purchaseId;

                // -------------------------------------------------
                // 1. CREATE PURCHASE
                // -------------------------------------------------

                try (PreparedStatement statement =
                             connection.prepareStatement(
                                     purchaseSql,
                                     Statement.RETURN_GENERATED_KEYS
                             )) {

                    statement.setLong(1, supplierId);
                    statement.setString(2, invoiceNumber);
                    statement.setDate(3, purchaseDate);
                    statement.setBigDecimal(4, subtotal);
                    statement.setBigDecimal(5, taxAmount);
                    statement.setBigDecimal(6, discountAmount);
                    statement.setBigDecimal(7, totalAmount);
                    statement.setString(8, paymentStatus);
                    statement.setString(9, paymentMethod);
                    statement.setBigDecimal(10, paidAmount);
                    statement.setString(11, notes);
                    statement.setLong(12, createdBy);

                    statement.executeUpdate();

                    try (ResultSet keys =
                                 statement.getGeneratedKeys()) {

                        if (!keys.next()) {
                            throw new SQLException(
                                    "Unable to create purchase."
                            );
                        }

                        purchaseId =
                                keys.getLong(1);
                    }
                }

                // -------------------------------------------------
                // 2. CREATE PURCHASE ITEMS
                // -------------------------------------------------

                for (PurchaseItem item : items) {

                    try (PreparedStatement statement =
                                 connection.prepareStatement(
                                         itemSql
                                 )) {

                        statement.setLong(
                                1,
                                purchaseId
                        );

                        statement.setLong(
                                2,
                                item.getMedicineId()
                        );

                        statement.setString(
                                3,
                                item.getBatchNumber()
                        );

                        statement.setDate(
                                4,
                                item.getExpiryDate()
                        );

                        statement.setInt(
                                5,
                                item.getQuantity()
                        );

                        statement.setBigDecimal(
                                6,
                                item.getUnitCost()
                        );

                        statement.setBigDecimal(
                                7,
                                item.getTaxPercent()
                        );

                        statement.setBigDecimal(
                                8,
                                item.getDiscountAmount()
                        );

                        statement.setBigDecimal(
                                9,
                                item.getTotalAmount()
                        );

                        statement.executeUpdate();
                    }

                    // -------------------------------------------------
                    // 3. CHECK WHETHER INVENTORY BATCH EXISTS
                    // -------------------------------------------------

                    Long inventoryId = null;

                    try (PreparedStatement statement =
                                 connection.prepareStatement(
                                         inventoryCheckSql
                                 )) {

                        statement.setLong(
                                1,
                                item.getMedicineId()
                        );

                        statement.setString(
                                2,
                                item.getBatchNumber()
                        );

                        try (ResultSet resultSet =
                                     statement.executeQuery()) {

                            if (resultSet.next()) {

                                inventoryId =
                                        resultSet.getLong(
                                                "inventory_id"
                                        );
                            }
                        }
                    }

                    // -------------------------------------------------
                    // 4. UPDATE EXISTING INVENTORY
                    // -------------------------------------------------

                    if (inventoryId != null) {

                        try (PreparedStatement statement =
                                     connection.prepareStatement(
                                             inventoryUpdateSql
                                     )) {

                            statement.setInt(
                                    1,
                                    item.getQuantity()
                            );

                            statement.setLong(
                                    2,
                                    supplierId
                            );

                            statement.setDate(
                                    3,
                                    item.getExpiryDate()
                            );

                            statement.setBigDecimal(
                                    4,
                                    item.getUnitCost()
                            );

                            statement.setBigDecimal(
                                    5,
                                    item.getSellingPrice()
                            );

                            statement.setLong(
                                    6,
                                    inventoryId
                            );

                            statement.executeUpdate();
                        }

                    } else {

                        // -------------------------------------------------
                        // 5. CREATE NEW INVENTORY BATCH
                        // -------------------------------------------------

                        try (PreparedStatement statement =
                                     connection.prepareStatement(
                                             inventoryInsertSql
                                     )) {

                            statement.setLong(
                                    1,
                                    item.getMedicineId()
                            );

                            statement.setLong(
                                    2,
                                    supplierId
                            );

                            statement.setString(
                                    3,
                                    item.getBatchNumber()
                            );

                            statement.setDate(
                                    4,
                                    item.getExpiryDate()
                            );

                            statement.setInt(
                                    5,
                                    item.getQuantity()
                            );

                            statement.setBigDecimal(
                                    6,
                                    item.getUnitCost()
                            );

                            statement.setBigDecimal(
                                    7,
                                    item.getSellingPrice()
                            );

                            statement.setInt(
                                    8,
                                    10
                            );

                            statement.executeUpdate();
                        }
                    }
                }

                connection.commit();

                return true;

            } catch (Exception e) {

                connection.rollback();

                e.printStackTrace();

                return false;

            } finally {

                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }

    public List<Map<String, Object>> getAllPurchases() {

        List<Map<String, Object>> purchases =
                new ArrayList<>();

        String sql = """
            SELECT
                p.purchase_id,
                p.invoice_number,
                p.purchase_date,
                p.total_amount,
                p.payment_status,
                p.paid_amount,
                s.supplier_name,

                pi.batch_number,
                pi.quantity,
                m.medicine_name

            FROM purchases p

            INNER JOIN suppliers s
                ON p.supplier_id = s.supplier_id

            LEFT JOIN purchase_items pi
                ON p.purchase_id = pi.purchase_id

            LEFT JOIN medicines m
                ON pi.medicine_id = m.medicine_id

            ORDER BY p.purchase_id DESC
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
                        "purchase_id",
                        resultSet.getLong("purchase_id")
                );

                row.put(
                        "invoice_number",
                        resultSet.getString("invoice_number")
                );

                row.put(
                        "purchase_date",
                        resultSet.getDate("purchase_date")
                );

                row.put(
                        "supplier_name",
                        resultSet.getString("supplier_name")
                );

                row.put(
                        "medicine_name",
                        resultSet.getString("medicine_name")
                );

                row.put(
                        "batch_number",
                        resultSet.getString("batch_number")
                );

                row.put(
                        "quantity",
                        resultSet.getInt("quantity")
                );

                row.put(
                        "total_amount",
                        resultSet.getBigDecimal("total_amount")
                );

                row.put(
                        "payment_status",
                        resultSet.getString("payment_status")
                );

                row.put(
                        "paid_amount",
                        resultSet.getBigDecimal("paid_amount")
                );

                purchases.add(row);
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return purchases;
    }

    public Map<String, Object> getPurchaseSummary() {

        Map<String, Object> summary =
                new HashMap<>();

        String sql = """
            SELECT

                COALESCE(
                    SUM(total_amount),
                    0
                ) AS total_purchases,

                COALESCE(
                    SUM(
                        total_amount - paid_amount
                    ),
                    0
                ) AS pending_payments,

                COALESCE(
                    (
                        SELECT SUM(quantity)
                        FROM purchase_items
                    ),
                    0
                ) AS total_items,

                COALESCE(
                    SUM(
                        CASE
                            WHEN MONTH(purchase_date)
                                 = MONTH(CURRENT_DATE)
                            AND YEAR(purchase_date)
                                 = YEAR(CURRENT_DATE)
                            THEN total_amount
                            ELSE 0
                        END
                    ),
                    0
                ) AS monthly_purchases

            FROM purchases
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
                        "total_purchases",
                        resultSet.getBigDecimal(
                                "total_purchases"
                        )
                );

                summary.put(
                        "pending_payments",
                        resultSet.getBigDecimal(
                                "pending_payments"
                        )
                );

                summary.put(
                        "total_items",
                        resultSet.getInt(
                                "total_items"
                        )
                );

                summary.put(
                        "monthly_purchases",
                        resultSet.getBigDecimal(
                                "monthly_purchases"
                        )
                );
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return summary;
    }
}