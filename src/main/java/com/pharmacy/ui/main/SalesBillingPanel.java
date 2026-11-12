package com.pharmacy.ui.main;

import com.pharmacy.config.AppTheme;
import com.pharmacy.dao.SalesDAO;
import com.pharmacy.ui.common.ModernTableStyler;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.math.BigDecimal;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * SalesBillingPanel
 *
 * UI-only Sales & Billing module.
 *
 * Current version:
 * - Uses sample medicine data
 * - Allows adding medicines to a bill
 * - Calculates subtotal, discount, tax and grand total
 * - Supports payment method selection
 * - Generates a simulated bill
 * - No database/JDBC connection yet
 */
public class SalesBillingPanel extends JPanel {
    private final SalesDAO salesDAO = new SalesDAO();
    private final Color background = AppTheme.BACKGROUND;
    private final Color surface = AppTheme.SURFACE;
    private final Color border = AppTheme.BORDER;
    private final Color textPrimary = AppTheme.TEXT_PRIMARY;
    private final Color textSecondary = AppTheme.TEXT_SECONDARY;
    private final Color textMuted = AppTheme.TEXT_MUTED;
    private final Color primary = AppTheme.PRIMARY;
    private final Color primaryLight = AppTheme.PRIMARY_LIGHT;
    private final Color primaryDark = AppTheme.PRIMARY_DARK;

    private final DecimalFormat currencyFormat =
            new DecimalFormat("₹#,##0.00");

    private JTable billTable;
    private DefaultTableModel billTableModel;

    private JTable salesHistoryTable;
    private DefaultTableModel salesHistoryTableModel;
    private JTextField salesHistorySearchField;

    private JTextField customerNameField;
    private JTextField customerPhoneField;
    private JTextField medicineSearchField;
    private JTextField quantityField;
    private JTextField discountField;

    private JComboBox<String> paymentMethodComboBox;

    private JLabel subtotalLabel;
    private JLabel discountLabel;
    private JLabel taxLabel;
    private JLabel totalLabel;

    public SalesBillingPanel() {

        setLayout(new BorderLayout());
        setBackground(background);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildMainContent(), BorderLayout.CENTER);
    }

    // ============================================================
    // HEADER
    // ============================================================

    private JComponent buildHeader() {

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(background);

        header.setBorder(
                new EmptyBorder(
                        24,
                        28,
                        18,
                        28
                )
        );

        JPanel titlePanel = new JPanel();

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        titlePanel.setBackground(background);

        JLabel title =
                new JLabel("Sales & Billing");

        title.setFont(
                AppTheme.HEADING_FONT
        );

        title.setForeground(
                textPrimary
        );

        JLabel subtitle =
                new JLabel(
                        "Create customer bills and process pharmacy sales quickly."
                );

        subtitle.setFont(
                AppTheme.BODY_FONT
        );

        subtitle.setForeground(
                textSecondary
        );

        titlePanel.add(title);

        titlePanel.add(
                Box.createVerticalStrut(5)
        );

        titlePanel.add(subtitle);

        JButton newSaleButton =
                createPrimaryButton("New Sale");

        newSaleButton.addActionListener(
                e -> clearSale()
        );

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        header.add(
                newSaleButton,
                BorderLayout.EAST
        );

        return header;
    }

    // ============================================================
    // MAIN CONTENT
    // ============================================================

    private JComponent buildMainContent() {

        JPanel content =
                new JPanel(
                        new BorderLayout(16, 0)
                );

        content.setBackground(
                background
        );

        content.setBorder(
                new EmptyBorder(
                        0,
                        28,
                        28,
                        28
                )
        );

        content.add(
                buildLeftPanel(),
                BorderLayout.CENTER
        );

        content.add(
                buildRightBillPanel(),
                BorderLayout.EAST
        );

        return content;
    }

    private JComponent buildSalesHistoryPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(
                surface
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                border
                        ),
                        new EmptyBorder(
                                12,
                                12,
                                12,
                                12
                        )
                )
        );

        JPanel historyHeader =
                new JPanel(
                        new BorderLayout(12, 0)
                );

        historyHeader.setBackground(
                surface
        );

        JLabel title =
                new JLabel(
                        "Recent Sales"
                );

        title.setFont(
                AppTheme.SUBHEADING_FONT
        );

        title.setForeground(
                textPrimary
        );

// Search field
        salesHistorySearchField =
                new JTextField();

        salesHistorySearchField.setFont(
                AppTheme.SMALL_FONT
        );

        salesHistorySearchField.setForeground(
                textPrimary
        );

        salesHistorySearchField.setBackground(
                background
        );

        salesHistorySearchField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                border
                        ),
                        new EmptyBorder(
                                8,
                                10,
                                8,
                                10
                        )
                )
        );

        salesHistorySearchField.setToolTipText(
                "Search by invoice number"
        );

        historyHeader.add(
                title,
                BorderLayout.WEST
        );

        historyHeader.add(
                salesHistorySearchField,
                BorderLayout.EAST
        );

        panel.add(
                historyHeader,
                BorderLayout.NORTH
        );

        // ------------------------------------------------------------
        // TABLE MODEL
        // ------------------------------------------------------------

        salesHistoryTableModel =
                new DefaultTableModel(
                        new Object[]{
                                "Invoice",
                                "Date",
                                "Subtotal",
                                "Discount",
                                "Tax",
                                "Total",
                                "Payment",
                                "Status"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        salesHistoryTable =
                new JTable(
                        salesHistoryTableModel
                );

        salesHistoryTable.setFont(
                AppTheme.SMALL_FONT
        );

        salesHistoryTable.setForeground(
                textPrimary
        );

        salesHistoryTable.setBackground(
                surface
        );

        salesHistoryTable.setGridColor(
                border
        );

        salesHistoryTable.setRowHeight(
                32
        );

        salesHistoryTable.setShowVerticalLines(
                false
        );

        salesHistoryTable.getTableHeader()
                .setFont(
                        AppTheme.SMALL_FONT
                );

        salesHistoryTable.getTableHeader()
                .setBackground(
                        background
                );

        salesHistoryTable.getTableHeader()
                .setForeground(
                        textSecondary
                );

        salesHistoryTable.getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                35
                        )
                );

        JScrollPane scrollPane =
                new JScrollPane(
                        salesHistoryTable
                );

        scrollPane.setBorder(
                null
        );

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(
                        16
                );

        panel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        loadSalesHistory();

        return panel;
    }

    private void loadSalesHistory() {

        if (salesHistoryTableModel == null) {
            return;
        }

        salesHistoryTableModel.setRowCount(0);

        List<Map<String, Object>> sales =
                salesDAO.getRecentSales();

        for (Map<String, Object> sale : sales) {

            String invoiceNumber =
                    String.valueOf(
                            sale.get("invoice_number")
                    );

            String saleDate =
                    String.valueOf(
                            sale.get("sale_date")
                    );

            BigDecimal subtotal =
                    (BigDecimal)
                            sale.get("subtotal");

            BigDecimal discount =
                    (BigDecimal)
                            sale.get("discount_amount");

            BigDecimal tax =
                    (BigDecimal)
                            sale.get("tax_amount");

            BigDecimal total =
                    (BigDecimal)
                            sale.get("total_amount");

            String paymentMethod =
                    String.valueOf(
                            sale.get("payment_method")
                    );

            String paymentStatus =
                    String.valueOf(
                            sale.get("payment_status")
                    );

            salesHistoryTableModel.addRow(
                    new Object[]{
                            invoiceNumber,
                            saleDate,
                            currencyFormat.format(
                                    subtotal
                            ),
                            currencyFormat.format(
                                    discount
                            ),
                            currencyFormat.format(
                                    tax
                            ),
                            currencyFormat.format(
                                    total
                            ),
                            paymentMethod,
                            paymentStatus
                    }
            );
        }
    }
    // ============================================================
    // LEFT PANEL
    // ============================================================

    private JComponent buildLeftPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(0, 14)
                );

        panel.setBackground(
                background
        );

        panel.add(
                buildCustomerPanel(),
                BorderLayout.NORTH
        );

        panel.add(
                buildMedicinePanel(),
                BorderLayout.CENTER
        );

        return panel;
    }

    // ============================================================
    // CUSTOMER
    // ============================================================

    private JComponent buildCustomerPanel() {

        JPanel panel =
                createCardPanel();

        panel.setLayout(
                new GridBagLayout()
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        6,
                        8,
                        6,
                        8
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1;

        JLabel title =
                new JLabel("Customer Information");

        title.setFont(
                AppTheme.SUBHEADING_FONT
        );

        title.setForeground(
                textPrimary
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        panel.add(
                title,
                gbc
        );

        gbc.gridwidth = 1;
        gbc.gridy++;

        JLabel nameLabel =
                createLabel("Customer Name");

        customerNameField =
                createTextField();

        gbc.gridx = 0;

        panel.add(
                nameLabel,
                gbc
        );

        gbc.gridx = 1;

        panel.add(
                customerNameField,
                gbc
        );

        gbc.gridx = 0;
        gbc.gridy++;

        JLabel phoneLabel =
                createLabel("Phone Number");

        customerPhoneField =
                createTextField();

        gbc.gridx = 0;

        panel.add(
                phoneLabel,
                gbc
        );

        gbc.gridx = 1;

        panel.add(
                customerPhoneField,
                gbc
        );

        return panel;
    }

    // ============================================================
    // MEDICINE PANEL
    // ============================================================

    private JComponent buildMedicinePanel() {

        JPanel panel =
                createCardPanel();

        panel.setLayout(
                new BorderLayout(
                        0,
                        12
                )
        );

        JLabel title =
                new JLabel(
                        "Add Medicine"
                );

        title.setFont(
                AppTheme.SUBHEADING_FONT
        );

        title.setForeground(
                textPrimary
        );

        panel.add(
                title,
                BorderLayout.NORTH
        );

        JPanel form =
                new JPanel(
                        new GridBagLayout()
                );

        form.setBackground(
                surface
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        5,
                        5,
                        5,
                        5
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1;

        JLabel searchLabel =
                createLabel("Medicine");

        medicineSearchField =
                createTextField();

        medicineSearchField.setToolTipText(
                "Enter medicine name"
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;

        form.add(
                searchLabel,
                gbc
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        form.add(
                medicineSearchField,
                gbc
        );

        JLabel quantityLabel =
                createLabel("Quantity");

        quantityField =
                createTextField();

        quantityField.setText("1");

        gbc.gridx = 2;
        gbc.weightx = 0;

        form.add(
                quantityLabel,
                gbc
        );

        gbc.gridx = 3;
        gbc.weightx = 0.3;

        form.add(
                quantityField,
                gbc
        );

        JButton addButton =
                createPrimaryButton(
                        "Add to Bill"
                );

        addButton.addActionListener(
                e -> addMedicineToBill()
        );

        gbc.gridx = 4;
        gbc.weightx = 0;

        form.add(
                addButton,
                gbc
        );

        panel.add(
                form,
                BorderLayout.CENTER
        );

        return panel;
    }

    // ============================================================
    // RIGHT BILL PANEL
    // ============================================================

    private JComponent buildRightBillPanel() {

        JPanel panel =
                createCardPanel();

        panel.setPreferredSize(
                new Dimension(
                        390,
                        0
                )
        );

        panel.setLayout(
                new BorderLayout(
                        0,
                        12
                )
        );

        JLabel title =
                new JLabel(
                        "Current Bill"
                );

        title.setFont(
                AppTheme.SUBHEADING_FONT
        );

        title.setForeground(
                textPrimary
        );

        panel.add(
                title,
                BorderLayout.NORTH
        );

        panel.add(
                buildBillTable(),
                BorderLayout.CENTER
        );

        panel.add(
                buildBillSummary(),
                BorderLayout.SOUTH
        );

        return panel;
    }

    // ============================================================
    // BILL TABLE
    // ============================================================

    private JComponent buildBillTable() {

        String[] columns = {
                "Medicine",
                "Qty",
                "Price",
                "Total"
        };

        billTableModel =
                new DefaultTableModel(
                        columns,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        billTable =
                new JTable(
                        billTableModel
                );

        billTable.setFont(
                AppTheme.SMALL_FONT
        );

        billTable.setRowHeight(38);

        billTable.setBackground(
                surface
        );

        billTable.setForeground(
                textPrimary
        );

        billTable.setGridColor(
                border
        );

        billTable.setShowVerticalLines(
                false
        );

        billTable.getTableHeader()
                .setFont(
                        AppTheme.SMALL_FONT
                );

        billTable.getTableHeader()
                .setBackground(
                        background
                );

        billTable.getTableHeader()
                .setForeground(
                        textSecondary
                );

        JScrollPane scrollPane =
                new JScrollPane(
                        billTable
                );

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        border
                )
        );

        return scrollPane;
    }

    // ============================================================
    // BILL SUMMARY
    // ============================================================

    private JComponent buildBillSummary() {

        JPanel summary =
                new JPanel();

        summary.setLayout(
                new BoxLayout(
                        summary,
                        BoxLayout.Y_AXIS
                )
        );

        summary.setBackground(
                surface
        );

        summary.setBorder(
                new EmptyBorder(
                        8,
                        4,
                        4,
                        4
                )
        );

        subtotalLabel =
                createSummaryLabel(
                        "Subtotal: ₹0.00"
                );

        discountLabel =
                createSummaryLabel(
                        "Discount: ₹0.00"
                );

        taxLabel =
                createSummaryLabel(
                        "GST (5%): ₹0.00"
                );

        totalLabel =
                createSummaryLabel(
                        "Total: ₹0.00"
                );

        totalLabel.setFont(
                AppTheme.SUBHEADING_FONT
        );

        totalLabel.setForeground(
                primaryDark
        );

        summary.add(
                subtotalLabel
        );

        summary.add(
                Box.createVerticalStrut(5)
        );

        JPanel discountPanel =
                new JPanel(
                        new BorderLayout(
                                8,
                                0
                        )
                );

        discountPanel.setBackground(
                surface
        );

        JLabel discountText =
                new JLabel(
                        "Discount (%)"
                );

        discountText.setFont(
                AppTheme.SMALL_FONT
        );

        discountText.setForeground(
                textSecondary
        );

        discountField =
                createTextField();

        discountField.setText("0");

        discountField.setPreferredSize(
                new Dimension(
                        70,
                        32
                )
        );

        discountPanel.add(
                discountText,
                BorderLayout.CENTER
        );

        discountPanel.add(
                discountField,
                BorderLayout.EAST
        );

        summary.add(
                discountPanel
        );

        JButton calculateButton =
                createSecondaryButton(
                        "Calculate Total"
                );

        calculateButton.addActionListener(
                e -> updateBillTotals()
        );

        summary.add(
                Box.createVerticalStrut(7)
        );

        summary.add(
                calculateButton
        );

        summary.add(
                Box.createVerticalStrut(8)
        );

        summary.add(
                discountLabel
        );

        summary.add(
                Box.createVerticalStrut(5)
        );

        summary.add(
                taxLabel
        );

        summary.add(
                Box.createVerticalStrut(5)
        );

        summary.add(
                totalLabel
        );

        summary.add(
                Box.createVerticalStrut(10)
        );

        JLabel paymentLabel =
                createLabel(
                        "Payment Method"
                );

        summary.add(
                paymentLabel
        );

        paymentMethodComboBox =
                new JComboBox<>(
                        new String[]{
                                "Cash",
                                "UPI",
                                "Debit Card",
                                "Credit Card"
                        }
                );

        paymentMethodComboBox.setFont(
                AppTheme.BODY_FONT
        );

        summary.add(
                paymentMethodComboBox
        );

        summary.add(
                Box.createVerticalStrut(10)
        );

        JButton generateBillButton =
                createPrimaryButton(
                        "Generate Bill"
                );

        generateBillButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        generateBillButton.addActionListener(
                e -> generateBill()
        );

        summary.add(
                generateBillButton
        );

        JButton removeButton =
                createSecondaryButton(
                        "Remove Selected Item"
                );

        removeButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        removeButton.addActionListener(
                e -> removeSelectedItem()
        );

        summary.add(
                Box.createVerticalStrut(7)
        );

        summary.add(
                removeButton
        );

        return summary;
    }

    // ============================================================
    // ADD MEDICINE
    // ============================================================

    private void addMedicineToBill() {

        String medicine =
                medicineSearchField
                        .getText()
                        .trim();

        if (medicine.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a medicine name.",
                    "Medicine Required",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Read requested quantity
        int quantity;

        try {

            quantity =
                    Integer.parseInt(
                            quantityField
                                    .getText()
                                    .trim()
                    );

            if (quantity <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid positive quantity.",
                    "Invalid Quantity",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // Get medicine information from database
        Map<String, Object> medicineData =
                salesDAO.findMedicine(medicine);

        if (medicineData == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Medicine not found or currently out of stock.",
                    "Medicine Not Available",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String medicineName =
                String.valueOf(
                        medicineData.get("medicine_name")
                );

        int availableStock =
                ((Number)
                        medicineData.get("quantity")
                ).intValue();

        BigDecimal sellingPrice =
                (BigDecimal)
                        medicineData.get("selling_price");

        if (sellingPrice == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selling price is not available for this medicine.",
                    "Price Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // Check if medicine is already present in bill
        for (
                int row = 0;
                row < billTableModel.getRowCount();
                row++
        ) {

            String existingMedicine =
                    String.valueOf(
                            billTableModel.getValueAt(
                                    row,
                                    0
                            )
                    );

            if (
                    existingMedicine.equalsIgnoreCase(
                            medicineName
                    )
            ) {

                int existingQuantity =
                        Integer.parseInt(
                                String.valueOf(
                                        billTableModel.getValueAt(
                                                row,
                                                1
                                        )
                                )
                        );

                int newQuantity =
                        existingQuantity + quantity;

                // Check total requested quantity against stock
                if (newQuantity > availableStock) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Only "
                                    + availableStock
                                    + " units are available.\n"
                                    + "Already added: "
                                    + existingQuantity
                                    + "\n"
                                    + "You are trying to add: "
                                    + quantity,
                            "Insufficient Stock",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }

                BigDecimal total =
                        sellingPrice.multiply(
                                BigDecimal.valueOf(
                                        newQuantity
                                )
                        );

                // Update quantity
                billTableModel.setValueAt(
                        newQuantity,
                        row,
                        1
                );

                // Update price
                billTableModel.setValueAt(
                        currencyFormat.format(
                                sellingPrice
                        ),
                        row,
                        2
                );

                // Update total
                billTableModel.setValueAt(
                        currencyFormat.format(
                                total
                        ),
                        row,
                        3
                );

                medicineSearchField.setText("");
                quantityField.setText("1");

                updateBillTotals();

                return;
            }
        }

        // Medicine is not already in bill

        if (quantity > availableStock) {

            JOptionPane.showMessageDialog(
                    this,
                    "Only "
                            + availableStock
                            + " units are available.",
                    "Insufficient Stock",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        BigDecimal total =
                sellingPrice.multiply(
                        BigDecimal.valueOf(quantity)
                );

        billTableModel.addRow(
                new Object[]{
                        medicineName,
                        quantity,
                        currencyFormat.format(
                                sellingPrice
                        ),
                        currencyFormat.format(
                                total
                        )
                }
        );

        medicineSearchField.setText("");
        quantityField.setText("1");

        updateBillTotals();
    }

    private double getSamplePrice(
            String medicine
    ) {

        String lower =
                medicine.toLowerCase();

        if (lower.contains("paracetamol")) {
            return 28.50;
        }

        if (lower.contains("azithromycin")) {
            return 85.00;
        }

        if (lower.contains("pantoprazole")) {
            return 42.00;
        }

        if (lower.contains("metformin")) {
            return 32.00;
        }

        if (lower.contains("cetirizine")) {
            return 18.00;
        }

        if (lower.contains("amoxicillin")) {
            return 62.00;
        }

        if (lower.contains("atorvastatin")) {
            return 55.00;
        }

        return 50.00;
    }

    // ============================================================
    // REMOVE ITEM
    // ============================================================

    private void removeSelectedItem() {

        int selectedRow =
                billTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an item from the bill.",
                    "Select Item",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        billTableModel.removeRow(
                selectedRow
        );

        updateBillTotals();
    }

    // ============================================================
    // TOTAL CALCULATION
    // ============================================================

    private void updateBillTotals() {

        double subtotal = 0;

        for (int row = 0;
             row < billTableModel.getRowCount();
             row++) {

            String totalValue =
                    String.valueOf(
                            billTableModel.getValueAt(
                                    row,
                                    3
                            )
                    );

            totalValue =
                    totalValue
                            .replace("₹", "")
                            .replace(",", "");

            try {

                subtotal +=
                        Double.parseDouble(
                                totalValue
                        );

            } catch (NumberFormatException ignored) {
            }
        }

        double discountPercent = 0;

        try {

            discountPercent =
                    Double.parseDouble(
                            discountField
                                    .getText()
                                    .trim()
                    );

            if (discountPercent < 0) {
                discountPercent = 0;
            }

            if (discountPercent > 100) {
                discountPercent = 100;
            }

        } catch (NumberFormatException ignored) {
        }

        double discount =
                subtotal
                        * discountPercent
                        / 100.0;

        double taxableAmount =
                subtotal - discount;

        double tax =
                taxableAmount * 0.05;

        double grandTotal =
                taxableAmount + tax;

        subtotalLabel.setText(
                "Subtotal: "
                        + currencyFormat.format(
                        subtotal
                )
        );

        discountLabel.setText(
                "Discount: "
                        + currencyFormat.format(
                        discount
                )
        );

        taxLabel.setText(
                "GST (5%): "
                        + currencyFormat.format(
                        tax
                )
        );

        totalLabel.setText(
                "Total: "
                        + currencyFormat.format(
                        grandTotal
                )
        );
    }

    // ============================================================
    // GENERATE BILL
    // ============================================================

    private void generateBill() {

        if (billTableModel.getRowCount() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please add at least one medicine to the bill.",
                    "Empty Bill",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // ============================================================
        // PAYMENT METHOD
        // ============================================================

        String paymentMethod =
                String.valueOf(
                        paymentMethodComboBox.getSelectedItem()
                );

        // ============================================================
        // CALCULATE TOTALS
        // ============================================================

        BigDecimal subtotal =
                BigDecimal.ZERO;

        for (
                int row = 0;
                row < billTableModel.getRowCount();
                row++
        ) {

            String totalValue =
                    String.valueOf(
                            billTableModel.getValueAt(
                                    row,
                                    3
                            )
                    );

            totalValue =
                    totalValue
                            .replace("₹", "")
                            .replace(",", "")
                            .trim();

            try {

                subtotal =
                        subtotal.add(
                                new BigDecimal(
                                        totalValue
                                )
                        );

            } catch (NumberFormatException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid amount in bill.",
                        "Billing Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }
        }

        // ============================================================
        // DISCOUNT
        // ============================================================

        BigDecimal discountPercent =
                BigDecimal.ZERO;

        try {

            String discountText =
                    discountField
                            .getText()
                            .trim();

            if (!discountText.isEmpty()) {

                discountPercent =
                        new BigDecimal(
                                discountText
                        );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid discount percentage.",
                    "Invalid Discount",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (
                discountPercent.compareTo(
                        BigDecimal.ZERO
                ) < 0
        ) {
            discountPercent =
                    BigDecimal.ZERO;
        }

        if (
                discountPercent.compareTo(
                        new BigDecimal("100")
                ) > 0
        ) {
            discountPercent =
                    new BigDecimal("100");
        }

        BigDecimal discountAmount =
                subtotal
                        .multiply(discountPercent)
                        .divide(
                                new BigDecimal("100"),
                                2,
                                RoundingMode.HALF_UP
                        );

        // ============================================================
        // GST 5%
        // ============================================================

        BigDecimal taxableAmount =
                subtotal.subtract(
                        discountAmount
                );

        BigDecimal taxAmount =
                taxableAmount
                        .multiply(
                                new BigDecimal("0.05")
                        )
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );

        BigDecimal totalAmount =
                taxableAmount
                        .add(taxAmount)
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );

        // ============================================================
        // PREPARE BILL ITEMS
        // ============================================================

        List<Map<String, Object>> items =
                new ArrayList<>();

        for (
                int row = 0;
                row < billTableModel.getRowCount();
                row++
        ) {

            Map<String, Object> item =
                    new HashMap<>();

            item.put(
                    "medicine_name",
                    String.valueOf(
                            billTableModel.getValueAt(
                                    row,
                                    0
                            )
                    )
            );

            item.put(
                    "quantity",
                    Integer.parseInt(
                            String.valueOf(
                                    billTableModel.getValueAt(
                                            row,
                                            1
                                    )
                            )
                    )
            );

            items.add(item);
        }

        // ============================================================
        // SAVE SALE TO DATABASE
        // ============================================================

        String invoiceNumber =
                salesDAO.createSale(
                        items,
                        subtotal,
                        discountAmount,
                        taxAmount,
                        totalAmount,
                        paymentMethod
                );

        if (invoiceNumber == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "The bill could not be saved.\n\n"
                            + "No changes were made to the database.",
                    "Billing Failed",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // ============================================================
        // SUCCESS
        // ============================================================

        JOptionPane.showMessageDialog(
                this,
                "Bill generated successfully!\n\n"
                        + "Invoice: "
                        + invoiceNumber
                        + "\n"
                        + "Total: "
                        + currencyFormat.format(
                        totalAmount.doubleValue()
                )
                        + "\n"
                        + "Payment: "
                        + paymentMethod,
                "Bill Generated",
                JOptionPane.INFORMATION_MESSAGE
        );

        billTableModel.setRowCount(0);

        medicineSearchField.setText("");
        quantityField.setText("1");
        discountField.setText("0");

        updateBillTotals();

        loadSalesHistory();
    }

    // ============================================================
    // CLEAR SALE
    // ============================================================

    private void clearSale() {

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Start a new sale?\n"
                                + "Current bill information will be cleared.",
                        "New Sale",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        customerNameField.setText("");
        customerPhoneField.setText("");
        medicineSearchField.setText("");
        quantityField.setText("1");
        discountField.setText("0");

        billTableModel.setRowCount(0);

        updateBillTotals();
    }

    // ============================================================
    // UI HELPERS
    // ============================================================

    private JPanel createCardPanel() {

        JPanel panel =
                new JPanel();

        panel.setBackground(
                surface
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                border
                        ),
                        new EmptyBorder(
                                16,
                                16,
                                16,
                                16
                        )
                )
        );

        return panel;
    }

    private JLabel createLabel(
            String text
    ) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                AppTheme.SMALL_FONT
        );

        label.setForeground(
                textSecondary
        );

        return label;
    }

    private JLabel createSummaryLabel(
            String text
    ) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                AppTheme.BODY_FONT
        );

        label.setForeground(
                textSecondary
        );

        return label;
    }

    private JTextField createTextField() {

        JTextField field =
                new JTextField();

        field.setFont(
                AppTheme.BODY_FONT
        );

        field.setForeground(
                textPrimary
        );

        field.setBackground(
                background
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                border
                        ),
                        new EmptyBorder(
                                7,
                                9,
                                7,
                                9
                        )
                )
        );

        return field;
    }

    private JButton createPrimaryButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setFont(
                AppTheme.BUTTON_FONT
        );

        Color primaryDark_TEXT = null;
        button.setForeground(
                primaryDark_TEXT
        );

        button.setBackground(
                primaryDark
        );
        button.putClientProperty("FlatLaf.style", "arc: 999; hoverBackground: " + AppTheme.toHex(AppTheme.ACCENT_HOVER)
        );

        button.setFocusPainted(false);

        button.setBorder(
                new EmptyBorder(
                        10,
                        16,
                        10,
                        16
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }

    private JButton createSecondaryButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setFont(
                AppTheme.BUTTON_FONT
        );

        button.setForeground(
                textPrimary
        );

        button.setBackground(
                surface
        );

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                border
                        ),
                        new EmptyBorder(
                                9,
                                16,
                                9,
                                16
                        )
                )
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }
}