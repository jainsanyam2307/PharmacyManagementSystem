package com.pharmacy.dao;

import java.math.BigDecimal;
import java.sql.Date;

public class PurchaseItem {

    private long medicineId;
    private String batchNumber;
    private Date expiryDate;
    private int quantity;
    private BigDecimal unitCost;
    private BigDecimal taxPercent;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private BigDecimal sellingPrice;

    public PurchaseItem(
            long medicineId,
            String batchNumber,
            Date expiryDate,
            int quantity,
            BigDecimal unitCost,
            BigDecimal taxPercent,
            BigDecimal discountAmount,
            BigDecimal totalAmount,
            BigDecimal sellingPrice
    ) {
        this.medicineId = medicineId;
        this.batchNumber = batchNumber;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.unitCost = unitCost;
        this.taxPercent = taxPercent;
        this.discountAmount = discountAmount;
        this.totalAmount = totalAmount;
        this.sellingPrice = sellingPrice;
    }

    public long getMedicineId() {
        return medicineId;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public BigDecimal getTaxPercent() {
        return taxPercent;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }
}