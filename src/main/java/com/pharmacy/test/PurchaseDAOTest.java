package com.pharmacy.test;

import com.pharmacy.dao.PurchaseDAO;
import com.pharmacy.dao.PurchaseItem;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class PurchaseDAOTest {

    public static void main(String[] args) {

        PurchaseDAO purchaseDAO = new PurchaseDAO();

        PurchaseItem item1 = new PurchaseItem(
                1,                          // Paracetamol
                "PCM-001",
                Date.valueOf("2027-12-31"),
                100,
                new BigDecimal("10.00"),
                new BigDecimal("5.00"),
                new BigDecimal("0.00"),
                new BigDecimal("1050.00"),
                new BigDecimal("15.00")
        );

        PurchaseItem item2 = new PurchaseItem(
                2,                          // Azithromycin
                "AZM-001",
                Date.valueOf("2027-10-31"),
                50,
                new BigDecimal("25.00"),
                new BigDecimal("5.00"),
                new BigDecimal("0.00"),
                new BigDecimal("1312.50"),
                new BigDecimal("35.00")
        );

        boolean result = purchaseDAO.createPurchase(
                1,                          // supplier_id
                "TEST-PUR-001",
                Date.valueOf("2026-08-09"),
                new BigDecimal("2250.00"),  // subtotal
                new BigDecimal("112.50"),  // tax
                new BigDecimal("0.00"),     // discount
                new BigDecimal("2362.50"),  // total
                "PAID",
                "CASH",
                new BigDecimal("2362.50"),
                "DAO test purchase",
                1,                          // admin user_id
                List.of(item1, item2)
        );

        if (result) {
            System.out.println("Purchase DAO test SUCCESS");
        } else {
            System.out.println("Purchase DAO test FAILED");
        }
    }
}