package com.pharmacy.ui.main;

import com.pharmacy.config.AppTheme;
import com.pharmacy.dao.PurchaseDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import javax.swing.event.DocumentEvent;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
/**
 * PurchasesPanel
 *
 * UI-only purchase management module.
 *
 * Current version:
 * - Sample in-memory data
 * - Purchase entry form
 * - Supplier information
 * - Medicine/batch information
 * - Purchase history
 * - Payment status
 * - Purchase total calculation
 *
 * Database integration will be added later.
 */
public class PurchasesPanel extends JPanel {

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
    private final PurchaseDAO purchaseDAO =
            new PurchaseDAO();
    private JTable purchaseTable;
    private DefaultTableModel purchaseTableModel;

    private JTextField invoiceField;
    private JTextField supplierField;
    private JTextField medicineField;
    private JTextField batchField;
    private JTextField quantityField;
    private JTextField priceField;
    private JTextField expiryField;

    private JComboBox<String> paymentStatusComboBox;

    private JLabel totalPurchasesLabel;
    private JLabel pendingPaymentsLabel;
    private JLabel totalItemsLabel;
    private JLabel monthlyPurchaseLabel;

    public PurchasesPanel() {

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
                new JLabel("Purchases");

        title.setFont(
                AppTheme.HEADING_FONT
        );

        title.setForeground(
                textPrimary
        );

        JLabel subtitle =
                new JLabel(
                        "Manage supplier purchases, incoming stock and purchase payments."
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

        JButton newPurchaseButton =
                createPrimaryButton(
                        "New Purchase"
                );

        newPurchaseButton.addActionListener(
                e -> showNewPurchaseDialog()
        );

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        header.add(
                newPurchaseButton,
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
                        new BorderLayout(0, 16)
                );

        content.setBackground(background);

        content.setBorder(
                new EmptyBorder(
                        0,
                        28,
                        28,
                        28
                )
        );

        content.add(
                buildSummaryCards(),
                BorderLayout.NORTH
        );

        content.add(
                buildPurchaseHistory(),
                BorderLayout.CENTER
        );

        return content;
    }

    // ============================================================
    // SUMMARY CARDS
    // ============================================================

    private JComponent buildSummaryCards() {

        JPanel cards =
                new JPanel(
                        new GridLayout(1, 4, 14, 0)
                );

        cards.setBackground(
                background
        );

        cards.setPreferredSize(
                new Dimension(0, 100)
        );

        Map<String, Object> summary =
                purchaseDAO.getPurchaseSummary();

        BigDecimal totalPurchases =
                (BigDecimal) summary.getOrDefault(
                        "total_purchases",
                        BigDecimal.ZERO
                );

        BigDecimal pendingPayments =
                (BigDecimal) summary.getOrDefault(
                        "pending_payments",
                        BigDecimal.ZERO
                );

        int totalItems =
                ((Number) summary.getOrDefault(
                        "total_items",
                        0
                )).intValue();

        BigDecimal monthlyPurchases =
                (BigDecimal) summary.getOrDefault(
                        "monthly_purchases",
                        BigDecimal.ZERO
                );

        cards.add(
                createSummaryCard(
                        "Total Purchases",
                        String.format(
                                "%,.2f",
                                totalPurchases
                        ),
                        "Total purchase value",
                        primary
                )
        );

        cards.add(
                createSummaryCard(
                        "Pending Payments",
                        String.format(
                                "%,.2f",
                                pendingPayments
                        ),
                        "Amount payable",
                        AppTheme.WARNING
                )
        );

        cards.add(
                createSummaryCard(
                        "Items Purchased",
                        String.format(
                                "%,d",
                                totalItems
                        ),
                        "Total units purchased",
                        new Color(70, 130, 180)
                )
        );
        cards.add(
                createSummaryCard(
                        "This Month",
                        String.format(
                                "₹%,.2f",
                                monthlyPurchases
                        ),
                        "Current month purchases",
                        AppTheme.SUCCESS
                )
        );

        return cards;
    }

    private JComponent createSummaryCard(
            String title,
            String value,
            String description,
            Color accent
    ) {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(surface);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                border
                        ),
                        new EmptyBorder(
                                14,
                                16,
                                14,
                                16
                        )
                )
        );

        JPanel accentPanel =
                new JPanel();

        accentPanel.setBackground(accent);

        accentPanel.setPreferredSize(
                new Dimension(
                        4,
                        0
                )
        );

        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setFont(
                AppTheme.SMALL_FONT
        );

        titleLabel.setForeground(
                textSecondary
        );
        JLabel valueLabel = new JLabel(value);

        valueLabel.setFont(
                AppTheme.HEADING_FONT
        );

        valueLabel.setForeground(
                textPrimary
        );

        JLabel descriptionLabel =
                new JLabel(description);

        descriptionLabel.setFont(
                AppTheme.SMALL_FONT
        );

        descriptionLabel.setForeground(
                textMuted
        );

        JPanel text =
                new JPanel();

        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );

        text.setOpaque(false);

        text.add(titleLabel);

        text.add(
                Box.createVerticalStrut(4)
        );

        text.add(valueLabel);

        text.add(
                Box.createVerticalStrut(2)
        );

        text.add(descriptionLabel);

        card.add(
                accentPanel,
                BorderLayout.WEST
        );

        card.add(
                text,
                BorderLayout.CENTER
        );

        return card;
    }

    // ============================================================
    // PURCHASE HISTORY
    // ============================================================

    private JComponent buildPurchaseHistory() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(0, 12)
                );

        panel.setBackground(surface);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                border
                        ),
                        new EmptyBorder(
                                14,
                                14,
                                14,
                                14
                        )
                )
        );

        JPanel top =
                new JPanel(
                        new BorderLayout()
                );

        top.setBackground(surface);

        JLabel title =
                new JLabel(
                        "Purchase History"
                );

        title.setFont(
                AppTheme.SUBHEADING_FONT
        );

        title.setForeground(
                textPrimary
        );

        JTextField searchField =
                createTextField();

        JButton updatePaymentButton =
                createSecondaryButton("Update Payment");

        updatePaymentButton.addActionListener(
                e -> updatePaymentStatus()
        );

        searchField.setPreferredSize(
                new Dimension(
                        260,
                        34
                )
        );

        searchField.setToolTipText(
                "Search invoice, supplier or medicine"
        );

        top.add(
                title,
                BorderLayout.WEST
        );

        JPanel actionsPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        actionsPanel.setBackground(surface);

        actionsPanel.add(updatePaymentButton);
        actionsPanel.add(searchField);

        top.add(
                actionsPanel,
                BorderLayout.EAST
        );
        panel.add(
                top,
                BorderLayout.NORTH
        );

        purchaseTableModel =
                new DefaultTableModel(
                        new String[]{
                                "Invoice No.",
                                "Supplier",
                                "Medicine",
                                "Batch No.",
                                "Quantity",
                                "Purchase Date",
                                "Total Amount",
                                "Payment Status"
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

        loadSamplePurchases();

        purchaseTable =
                new JTable(
                        purchaseTableModel
                );

        configurePurchaseTable();

        JScrollPane scrollPane =
                new JScrollPane(
                        purchaseTable
                );

        scrollPane.setBorder(null);

        scrollPane
                .getVerticalScrollBar()
                .setUnitIncrement(16);

        panel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        searchField
                .getDocument()
                .addDocumentListener(
                        new javax.swing.event.DocumentListener() {

                            private void filter() {

                                String text =
                                        searchField
                                                .getText()
                                                .trim()
                                                .toLowerCase();

                                for (int i =
                                     purchaseTableModel.getRowCount() - 1;
                                     i >= 0;
                                     i--) {

                                    boolean match =
                                            false;

                                    for (int j = 0;
                                         j < purchaseTableModel
                                                 .getColumnCount();
                                         j++) {

                                        Object value =
                                                purchaseTableModel
                                                        .getValueAt(
                                                                i,
                                                                j
                                                        );

                                        if (value != null &&
                                                value.toString()
                                                        .toLowerCase()
                                                        .contains(text)) {

                                            match = true;
                                            break;
                                        }
                                    }

                                    if (!match &&
                                            !text.isEmpty()) {

                                        purchaseTable
                                                .getSelectionModel()
                                                .removeSelectionInterval(
                                                        i,
                                                        i
                                                );
                                    }
                                }

                                /*
                                 * The actual database-backed filtering
                                 * will be implemented later.
                                 */
                            }

                            @Override
                            public void insertUpdate(
                                    DocumentEvent e
                            ) {
                                filter();
                            }

                            @Override
                            public void removeUpdate(
                                    DocumentEvent e
                            ) {
                                filter();
                            }

                            @Override
                            public void changedUpdate(
                                    DocumentEvent e
                            ) {
                                filter();
                            }
                        }
                );

        purchaseTable.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent e
                    ) {

                        if (e.getClickCount() == 2) {
                            showPurchaseDetails();
                        }
                    }
                }
        );

        return panel;
    }

    // ============================================================
    // SAMPLE DATA
    // ============================================================

    private void loadSamplePurchases() {

        Object[][] data = {

                {
                        "PUR-2026-001",
                        "MediSource Pvt Ltd",
                        "Paracetamol 650mg",
                        "PCM24051",
                        500,
                        "01 Aug 2026",
                        "₹14,250.00",
                        "Paid"
                },

                {
                        "PUR-2026-002",
                        "HealthCare Distributors",
                        "Azithromycin 500mg",
                        "AZT24031",
                        200,
                        "02 Aug 2026",
                        "₹12,400.00",
                        "Pending"
                },

                {
                        "PUR-2026-003",
                        "MedLife Suppliers",
                        "Metformin 500mg",
                        "MET24055",
                        400,
                        "03 Aug 2026",
                        "₹8,400.00",
                        "Paid"
                },

                {
                        "PUR-2026-004",
                        "PharmaLink",
                        "Atorvastatin 10mg",
                        "ATR24039",
                        300,
                        "04 Aug 2026",
                        "₹11,400.00",
                        "Paid"
                },

                {
                        "PUR-2026-005",
                        "Wellness Pharma",
                        "Vitamin D3 60000 IU",
                        "VD324067",
                        150,
                        "05 Aug 2026",
                        "₹11,700.00",
                        "Pending"
                },

                {
                        "PUR-2026-006",
                        "MediSource Pvt Ltd",
                        "Pantoprazole 40mg",
                        "PNT24018",
                        250,
                        "06 Aug 2026",
                        "₹7,250.00",
                        "Paid"
                }
        };

        for (Object[] row : data) {
            purchaseTableModel.addRow(row);
        }
    }

    // ============================================================
    // NEW PURCHASE
    // ============================================================

    private void showNewPurchaseDialog() {

        JDialog dialog =
                new JDialog(
                        SwingUtilities.getWindowAncestor(this),
                        "New Purchase",
                        Dialog.ModalityType.APPLICATION_MODAL
                );

        dialog.setSize(
                600,
                600
        );

        dialog.setLocationRelativeTo(this);

        JPanel content =
                new JPanel(
                        new BorderLayout()
                );

        content.setBackground(surface);

        content.setBorder(
                new EmptyBorder(
                        20,
                        22,
                        20,
                        22
                )
        );

        JLabel title =
                new JLabel(
                        "Record New Purchase"
                );

        title.setFont(
                AppTheme.SUBHEADING_FONT
        );

        title.setForeground(
                textPrimary
        );

        JPanel form =
                new JPanel(
                        new GridBagLayout()
                );

        form.setBackground(surface);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        8,
                        6,
                        8,
                        6
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1;

        invoiceField =
                createTextField();

        invoiceField.setText(
                "PUR-2026-007"
        );

        supplierField =
                createTextField();

        medicineField =
                createTextField();

        batchField =
                createTextField();

        quantityField =
                createTextField();

        priceField =
                createTextField();

        expiryField =
                createTextField();

        paymentStatusComboBox =
                new JComboBox<>(
                        new String[]{
                                "Paid",
                                "Pending",
                                "Partially Paid"
                        }
                );

        paymentStatusComboBox.setFont(
                AppTheme.BODY_FONT
        );

        addFormRow(
                form,
                gbc,
                0,
                "Invoice Number",
                invoiceField
        );

        addFormRow(
                form,
                gbc,
                1,
                "Supplier",
                supplierField
        );

        addFormRow(
                form,
                gbc,
                2,
                "Medicine",
                medicineField
        );

        addFormRow(
                form,
                gbc,
                3,
                "Batch Number",
                batchField
        );

        addFormRow(
                form,
                gbc,
                4,
                "Quantity",
                quantityField
        );

        addFormRow(
                form,
                gbc,
                5,
                "Purchase Price",
                priceField
        );

        addFormRow(
                form,
                gbc,
                6,
                "Expiry Date",
                expiryField
        );

        addFormRow(
                form,
                gbc,
                7,
                "Payment Status",
                paymentStatusComboBox
        );

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        buttons.setBackground(surface);

        JButton cancelButton =
                createSecondaryButton(
                        "Cancel"
                );

        JButton saveButton =
                createPrimaryButton(
                        "Save Purchase"
                );

        cancelButton.addActionListener(
                e -> dialog.dispose()
        );

        saveButton.addActionListener(
                e -> savePurchase(dialog)
        );

        buttons.add(cancelButton);
        buttons.add(saveButton);

        content.add(
                title,
                BorderLayout.NORTH
        );

        content.add(
                form,
                BorderLayout.CENTER
        );

        content.add(
                buttons,
                BorderLayout.SOUTH
        );

        dialog.setContentPane(content);

        dialog.setVisible(true);
    }

    private void addFormRow(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String label,
            Component component
    ) {

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.35;

        panel.add(
                createLabel(label),
                gbc
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        panel.add(
                component,
                gbc
        );
    }

    // ============================================================
    // SAVE PURCHASE
    // ============================================================

    private void savePurchase(
            JDialog dialog
    ) {

        String invoice =
                invoiceField
                        .getText()
                        .trim();

        String supplier =
                supplierField
                        .getText()
                        .trim();

        String medicine =
                medicineField
                        .getText()
                        .trim();

        String batch =
                batchField
                        .getText()
                        .trim();

        if (invoice.isEmpty() ||
                supplier.isEmpty() ||
                medicine.isEmpty() ||
                batch.isEmpty()) {

            JOptionPane.showMessageDialog(
                    dialog,
                    "Please fill in all required fields.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int quantity;

        double price;

        try {

            quantity =
                    Integer.parseInt(
                            quantityField
                                    .getText()
                                    .trim()
                    );

            price =
                    Double.parseDouble(
                            priceField
                                    .getText()
                                    .trim()
                    );

            if (quantity <= 0 ||
                    price < 0) {

                throw new NumberFormatException();
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    dialog,
                    "Enter a valid quantity and purchase price.",
                    "Invalid Data",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        double total =
                quantity * price;

        String status =
                String.valueOf(
                        paymentStatusComboBox
                                .getSelectedItem()
                );

        purchaseTableModel.addRow(
                new Object[]{
                        invoice,
                        supplier,
                        medicine,
                        batch,
                        quantity,
                        "08 Aug 2026",
                        currencyFormat.format(total),
                        status
                }
        );

        dialog.dispose();

        JOptionPane.showMessageDialog(
                this,
                "Purchase recorded successfully.\n\n"
                        + "Invoice: "
                        + invoice
                        + "\n"
                        + "Total: "
                        + currencyFormat.format(total)
                        + "\n\n"
                        + "Database and inventory synchronization "
                        + "will be added later.",
                "Purchase Saved",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ============================================================
    // DETAILS
    // ============================================================

    private void showPurchaseDetails() {

        int selectedRow =
                purchaseTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        StringBuilder details =
                new StringBuilder();

        details.append("Purchase Details\n\n");

        for (int column = 0;
             column < purchaseTableModel.getColumnCount();
             column++) {

            details.append(
                    purchaseTableModel
                            .getColumnName(column)
            );

            details.append(": ");

            details.append(
                    purchaseTableModel
                            .getValueAt(
                                    selectedRow,
                                    column
                            )
            );

            details.append("\n");
        }

        JOptionPane.showMessageDialog(
                this,
                details.toString(),
                "Purchase Details",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ============================================================
// UPDATE PAYMENT STATUS
// ============================================================

    private void updatePaymentStatus() {

        int selectedRow = purchaseTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a purchase first.",
                    "Select Purchase",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String invoice = String.valueOf(
                purchaseTableModel.getValueAt(selectedRow, 0)
        );

        String currentStatus = String.valueOf(
                purchaseTableModel.getValueAt(selectedRow, 7)
        );

        JComboBox<String> statusComboBox =
                new JComboBox<>(
                        new String[]{
                                "Paid",
                                "Pending",
                                "Partially Paid"
                        }
                );

        statusComboBox.setSelectedItem(currentStatus);

        JPanel panel = new JPanel(
                new GridLayout(2, 1, 5, 8)
        );

        panel.add(
                new JLabel("Invoice: " + invoice)
        );

        panel.add(statusComboBox);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Update Payment Status",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {

            String newStatus = String.valueOf(
                    statusComboBox.getSelectedItem()
            );

            purchaseTableModel.setValueAt(
                    newStatus,
                    selectedRow,
                    7
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Payment status updated successfully.\n\n"
                            + "Invoice: " + invoice
                            + "\n"
                            + "Status: " + newStatus,
                    "Payment Updated",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
    // ============================================================
    // TABLE CONFIGURATION
    // ============================================================

    private void configurePurchaseTable() {

        purchaseTable.setFont(
                AppTheme.SMALL_FONT
        );

        purchaseTable.setForeground(
                textPrimary
        );

        purchaseTable.setBackground(
                surface
        );

        purchaseTable.setGridColor(
                border
        );

        purchaseTable.setRowHeight(40);

        purchaseTable.setShowVerticalLines(false);

        purchaseTable.setShowHorizontalLines(true);

        purchaseTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        purchaseTable.getTableHeader()
                .setFont(
                        AppTheme.SMALL_FONT
                );

        purchaseTable.getTableHeader()
                .setBackground(
                        background
                );

        purchaseTable.getTableHeader()
                .setForeground(
                        textSecondary
                );

        purchaseTable.getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                40
                        )
                );

        purchaseTable.setDefaultRenderer(
                Object.class,
                new PurchaseTableRenderer()
        );
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

        button.setForeground(
                Color.WHITE
        );

        button.setBackground(
                primary
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

    // ============================================================
    // TABLE RENDERER
    // ============================================================

    private class PurchaseTableRenderer
            extends javax.swing.table.DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean selected,
                boolean focus,
                int row,
                int column
        ) {

            Component component =
                    super.getTableCellRendererComponent(
                            table,
                            value,
                            selected,
                            focus,
                            row,
                            column
                    );

            setBorder(
                    new EmptyBorder(
                            0,
                            7,
                            0,
                            7
                    )
            );

            if (selected) {

                setBackground(
                        primaryLight
                );

                setForeground(
                        primaryDark
                );

            } else {

                setBackground(
                        surface
                );

                setForeground(
                        textPrimary
                );
            }

            if (column == 4 ||
                    column == 6 ||
                    column == 7) {

                setHorizontalAlignment(
                        SwingConstants.CENTER
                );

            } else {

                setHorizontalAlignment(
                        SwingConstants.LEFT
                );
            }

            if (column == 7 &&
                    value != null) {

                String status =
                        value.toString();

                setHorizontalAlignment(
                        SwingConstants.CENTER
                );

                if (status.equals("Pending")) {

                    setForeground(
                            AppTheme.WARNING
                    );

                } else if (
                        status.equals("Partially Paid")
                ) {

                    setForeground(
                            AppTheme.WARNING
                    );

                } else {

                    setForeground(
                            AppTheme.SUCCESS
                    );
                }
            }

            return component;
        }
    }
}