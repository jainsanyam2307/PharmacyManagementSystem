package com.pharmacy.ui.main;
import com.pharmacy.config.AppTheme;
import com.pharmacy.dao.InventoryDAO;
import com.pharmacy.ui.common.ModernTableStyler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import java.util.List;
import java.util.Map;
/**
 * InventoryPanel
 *
 * Pharmacy inventory management UI.
 *
 * Features:
 * - MySQL inventory data
 * - Inventory summary cards
 * - Search
 * - Category filtering
 * - Stock status filtering
 * - Stock adjustment
 * - Add stock
 * - Remove stock
 */
public class InventoryPanel extends JPanel {

    private final Color backgroundColor =
            AppTheme.BACKGROUND;

    private final Color surfaceColor =
            AppTheme.SURFACE;

    private final Color primaryColor =
            AppTheme.PRIMARY;

    private final Color primaryLightColor =
            AppTheme.PRIMARY_LIGHT;

    private final Color primaryDarkColor =
            AppTheme.PRIMARY_DARK;

    private final Color textPrimaryColor =
            AppTheme.TEXT_PRIMARY;

    private final Color textSecondaryColor =
            AppTheme.TEXT_SECONDARY;

    private final Color textMutedColor =
            AppTheme.TEXT_MUTED;

    private final Color borderColor =
            AppTheme.BORDER;

    private final InventoryDAO inventoryDAO =
            new InventoryDAO();

    private JTable inventoryTable;

    private DefaultTableModel tableModel;

    private TableRowSorter<DefaultTableModel> sorter;

    private JTextField searchField;

    private JComboBox<String> categoryComboBox;

    private JComboBox<String> statusComboBox;

    private JLabel totalItemsLabel;

    private JLabel totalUnitsLabel;

    private JLabel lowStockLabel;

    private JLabel outOfStockLabel;

    private final String[] columns = {
            "Inventory ID",
            "Medicine Name",
            "Category",
            "Batch No.",
            "Current Stock",
            "Min. Stock",
            "Purchase Price",
            "Selling Price",
            "Stock Value",
            "Expiry Date",
            "Status"
    };

    public InventoryPanel() {

        setLayout(
                new BorderLayout()
        );

        setBackground(
                backgroundColor
        );

        add(
                buildHeader(),
                BorderLayout.NORTH
        );

        add(
                buildMainContent(),
                BorderLayout.CENTER
        );
    }

    // ============================================================
    // HEADER
    // ============================================================

    private JComponent buildHeader() {

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(
                backgroundColor
        );

        header.setBorder(
                new EmptyBorder(
                        24,
                        28,
                        18,
                        28
                )
        );

        JPanel titlePanel =
                new JPanel();

        titlePanel.setLayout(
                new javax.swing.BoxLayout(
                        titlePanel,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        titlePanel.setBackground(
                backgroundColor
        );

        JLabel title =
                new JLabel("Inventory");

        title.setFont(
                AppTheme.HEADING_FONT
        );

        title.setForeground(
                textPrimaryColor
        );

        JLabel subtitle =
                new JLabel(
                        "Monitor stock levels, inventory value and medicine availability."
                );

        subtitle.setFont(
                AppTheme.BODY_FONT
        );

        subtitle.setForeground(
                textSecondaryColor
        );

        titlePanel.add(title);

        titlePanel.add(
                javax.swing.Box.createVerticalStrut(5)
        );

        titlePanel.add(subtitle);

        // ============================================================
        // HEADER BUTTONS
        // ============================================================

        JButton addInventoryButton =
                createPrimaryButton(
                        "Add Inventory"
                );

        addInventoryButton.addActionListener(
                e -> showAddInventoryDialog()
        );

        JButton adjustmentButton =
                createSecondaryButton(
                        "Adjust Stock"
                );

        adjustmentButton.addActionListener(
                e -> showStockAdjustmentDialog()
        );

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        buttonPanel.setBackground(
                backgroundColor
        );

        buttonPanel.add(
                addInventoryButton
        );

        buttonPanel.add(
                adjustmentButton
        );

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        header.add(
                buttonPanel,
                BorderLayout.EAST
        );

        return header;
    }

    private void showAddInventoryDialog() {

        List<Map<String, Object>> medicines =
                inventoryDAO.getActiveMedicines();

        List<Map<String, Object>> suppliers =
                inventoryDAO.getActiveSuppliers();

        // ------------------------------------------------------------
        // CHECK MEDICINES
        // ------------------------------------------------------------

        if (medicines.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No active medicines found.\nPlease add a medicine first.",
                    "No Medicines",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // ------------------------------------------------------------
        // DIALOG
        // ------------------------------------------------------------

        JDialog dialog =
                new JDialog(
                        SwingUtilities.getWindowAncestor(this),
                        "Add Inventory",
                        Dialog.ModalityType.APPLICATION_MODAL
                );

        dialog.setSize(520, 650);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel content =
                new JPanel(
                        new BorderLayout(0, 15)
                );

        content.setBackground(surfaceColor);

        content.setBorder(
                new EmptyBorder(
                        20,
                        24,
                        20,
                        24
                )
        );

        // ------------------------------------------------------------
        // TITLE
        // ------------------------------------------------------------

        JLabel title =
                new JLabel("Add New Inventory");

        title.setFont(
                AppTheme.HEADING_FONT
        );

        title.setForeground(
                textPrimaryColor
        );

        JLabel subtitle =
                new JLabel(
                        "Add a new medicine batch to inventory."
                );

        subtitle.setFont(
                AppTheme.SMALL_FONT
        );

        subtitle.setForeground(
                textSecondaryColor
        );

        JPanel titlePanel =
                new JPanel();

        titlePanel.setLayout(
                new BoxLayout(
                        titlePanel,
                        BoxLayout.Y_AXIS
                )
        );

        titlePanel.setBackground(surfaceColor);

        titlePanel.add(title);

        titlePanel.add(
                Box.createVerticalStrut(4)
        );

        titlePanel.add(subtitle);

        // ------------------------------------------------------------
        // FORM
        // ------------------------------------------------------------

        JPanel form =
                new JPanel(
                        new GridBagLayout()
                );

        form.setBackground(surfaceColor);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        6,
                        5,
                        6,
                        5
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1.0;

        // ------------------------------------------------------------
        // MEDICINE
        // ------------------------------------------------------------

        JComboBox<String> medicineComboBox =
                new JComboBox<>();

        medicineComboBox.setFont(
                AppTheme.BODY_FONT
        );

        for (Map<String, Object> medicine : medicines) {

            medicineComboBox.addItem(
                    String.valueOf(
                            medicine.get("medicine_name")
                    )
            );
        }

        // ------------------------------------------------------------
        // SUPPLIER
        // ------------------------------------------------------------

        JComboBox<String> supplierComboBox =
                new JComboBox<>();

        supplierComboBox.setFont(
                AppTheme.BODY_FONT
        );

        supplierComboBox.addItem(
                "No Supplier"
        );

        for (Map<String, Object> supplier : suppliers) {

            supplierComboBox.addItem(
                    String.valueOf(
                            supplier.get("supplier_name")
                    )
            );
        }

        // ------------------------------------------------------------
        // INPUT FIELDS
        // ------------------------------------------------------------

        JTextField batchField =
                createInventoryInputField(
                        "e.g. PCM24051"
                );

        JTextField expiryField =
                createInventoryInputField(
                        "YYYY-MM-DD"
                );

        JTextField quantityField =
                createInventoryInputField(
                        "e.g. 100"
                );

        JTextField reorderField =
                createInventoryInputField(
                        "e.g. 20"
                );

        JTextField purchasePriceField =
                createInventoryInputField(
                        "e.g. 18.50"
                );

        JTextField sellingPriceField =
                createInventoryInputField(
                        "e.g. 28.00"
                );

        JTextField locationField =
                createInventoryInputField(
                        "e.g. Shelf A1"
                );

        // ------------------------------------------------------------
        // FORM ROWS
        // ------------------------------------------------------------

        int row = 0;

        addInventoryFormRow(
                form,
                gbc,
                row++,
                "Medicine *",
                medicineComboBox
        );

        addInventoryFormRow(
                form,
                gbc,
                row++,
                "Supplier",
                supplierComboBox
        );

        addInventoryFormRow(
                form,
                gbc,
                row++,
                "Batch Number *",
                batchField
        );

        addInventoryFormRow(
                form,
                gbc,
                row++,
                "Expiry Date *",
                expiryField
        );

        addInventoryFormRow(
                form,
                gbc,
                row++,
                "Quantity *",
                quantityField
        );

        addInventoryFormRow(
                form,
                gbc,
                row++,
                "Minimum Stock",
                reorderField
        );

        addInventoryFormRow(
                form,
                gbc,
                row++,
                "Purchase Price *",
                purchasePriceField
        );

        addInventoryFormRow(
                form,
                gbc,
                row++,
                "Selling Price *",
                sellingPriceField
        );

        addInventoryFormRow(
                form,
                gbc,
                row++,
                "Location",
                locationField
        );

        // ------------------------------------------------------------
        // BUTTONS
        // ------------------------------------------------------------

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                10,
                                0
                        )
                );

        buttons.setBackground(surfaceColor);

        JButton cancelButton =
                createSecondaryButton(
                        "Cancel"
                );

        JButton saveButton =
                createPrimaryButton(
                        "Save Inventory"
                );

        cancelButton.addActionListener(
                e -> dialog.dispose()
        );

        // ------------------------------------------------------------
        // SAVE ACTION
        // ------------------------------------------------------------

        saveButton.addActionListener(e -> {

            // --------------------------------------------------------
            // MEDICINE
            // --------------------------------------------------------

            int medicineIndex =
                    medicineComboBox.getSelectedIndex();

            if (medicineIndex < 0) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Please select a medicine.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            long medicineId =
                    ((Number)
                            medicines
                                    .get(medicineIndex)
                                    .get("medicine_id")
                    ).longValue();

            // --------------------------------------------------------
            // SUPPLIER
            // --------------------------------------------------------

            Long supplierId = null;

            int supplierIndex =
                    supplierComboBox.getSelectedIndex();

            if (supplierIndex > 0) {

                supplierId =
                        ((Number)
                                suppliers
                                        .get(supplierIndex - 1)
                                        .get("supplier_id")
                        ).longValue();
            }

            // --------------------------------------------------------
            // READ INPUT
            // --------------------------------------------------------

            String batchNumber =
                    batchField
                            .getText()
                            .trim();

            String expiryText =
                    expiryField
                            .getText()
                            .trim();

            String quantityText =
                    quantityField
                            .getText()
                            .trim();

            String reorderText =
                    reorderField
                            .getText()
                            .trim();

            String purchasePriceText =
                    purchasePriceField
                            .getText()
                            .trim();

            String sellingPriceText =
                    sellingPriceField
                            .getText()
                            .trim();

            String location =
                    locationField
                            .getText()
                            .trim();

            // --------------------------------------------------------
            // BASIC VALIDATION
            // --------------------------------------------------------

            if (batchNumber.isEmpty()) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Please enter a batch number.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            if (expiryText.isEmpty()) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Please enter an expiry date.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            // --------------------------------------------------------
            // EXPIRY DATE
            // --------------------------------------------------------

            java.sql.Date expiryDate;

            try {

                expiryDate =
                        java.sql.Date.valueOf(
                                expiryText
                        );

            } catch (IllegalArgumentException ex) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Expiry date must use YYYY-MM-DD format.\n"
                                + "Example: 2027-12-15",
                        "Invalid Date",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            // --------------------------------------------------------
            // NUMERIC VALUES
            // --------------------------------------------------------

            int quantity;
            int reorderLevel;

            java.math.BigDecimal purchasePrice;
            java.math.BigDecimal sellingPrice;

            try {

                quantity =
                        Integer.parseInt(
                                quantityText
                        );

                reorderLevel =
                        reorderText.isEmpty()
                                ? 0
                                : Integer.parseInt(
                                reorderText
                        );

                purchasePrice =
                        new java.math.BigDecimal(
                                purchasePriceText
                        );

                sellingPrice =
                        new java.math.BigDecimal(
                                sellingPriceText
                        );

            } catch (NumberFormatException ex) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Please enter valid numeric values.",
                        "Invalid Input",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            // --------------------------------------------------------
            // NUMBER VALIDATION
            // --------------------------------------------------------

            if (quantity < 0) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Quantity cannot be negative.",
                        "Invalid Quantity",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            if (reorderLevel < 0) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Minimum stock cannot be negative.",
                        "Invalid Minimum Stock",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            if (purchasePrice.compareTo(
                    java.math.BigDecimal.ZERO
            ) < 0) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Purchase price cannot be negative.",
                        "Invalid Price",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            if (sellingPrice.compareTo(
                    java.math.BigDecimal.ZERO
            ) < 0) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Selling price cannot be negative.",
                        "Invalid Price",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            // --------------------------------------------------------
            // SAVE TO DATABASE
            // --------------------------------------------------------

            long inventoryId =
                    inventoryDAO.addInventory(
                            medicineId,
                            supplierId,
                            batchNumber,
                            expiryDate,
                            quantity,
                            purchasePrice,
                            sellingPrice,
                            reorderLevel,
                            location.isEmpty()
                                    ? null
                                    : location
                    );

            // --------------------------------------------------------
            // SAVE FAILED
            // --------------------------------------------------------

            if (inventoryId == -1) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Failed to add inventory.\n"
                                + "Please check the console for database details.",
                        "Save Failed",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            // --------------------------------------------------------
            // SUCCESS
            // --------------------------------------------------------

            dialog.dispose();

            refreshInventoryPanel();

            JOptionPane.showMessageDialog(
                    this,
                    "Inventory added successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        buttons.add(cancelButton);
        buttons.add(saveButton);

        // ------------------------------------------------------------
        // FINAL LAYOUT
        // ------------------------------------------------------------

        content.add(
                titlePanel,
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


    private JTextField createInventoryInputField(
            String tooltip
    ) {

        JTextField field =
                new JTextField();

        field.setFont(
                AppTheme.BODY_FONT
        );

        field.setForeground(
                textPrimaryColor
        );

        field.setBackground(
                backgroundColor
        );

        field.setToolTipText(
                tooltip
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                borderColor
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

    private void addInventoryFormRow(
            JPanel form,
            GridBagConstraints gbc,
            int row,
            String labelText,
            JComponent component
    ) {

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.35;

        JLabel label =
                new JLabel(
                        labelText
                );

        label.setFont(
                AppTheme.SMALL_FONT
        );

        label.setForeground(
                textSecondaryColor
        );

        form.add(
                label,
                gbc
        );

        gbc.gridx = 1;
        gbc.weightx = 0.65;

        form.add(
                component,
                gbc
        );
    }

    private void refreshInventoryPanel() {

        removeAll();

        add(
                buildHeader(),
                BorderLayout.NORTH
        );

        add(
                buildMainContent(),
                BorderLayout.CENTER
        );

        revalidate();
        repaint();
    }
    // ============================================================
    // MAIN CONTENT
    // ============================================================

    private JComponent buildMainContent() {

        JPanel content =
                new JPanel(
                        new BorderLayout(
                                0,
                                16
                        )
                );

        content.setBackground(
                backgroundColor
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
                buildSummaryCards(),
                BorderLayout.NORTH
        );

        JPanel center =
                new JPanel(
                        new BorderLayout(
                                0,
                                14
                        )
                );

        center.setBackground(
                backgroundColor
        );

        center.add(
                buildToolbar(),
                BorderLayout.NORTH
        );

        center.add(
                buildTablePanel(),
                BorderLayout.CENTER
        );

        content.add(
                center,
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
                        new GridLayout(
                                1,
                                4,
                                14,
                                0
                        )
                );

        cards.setBackground(
                backgroundColor
        );

        cards.setPreferredSize(
                new Dimension(
                        0,
                        100
                )
        );

        Map<String, Integer> summary =
                inventoryDAO.getInventorySummary();

        int totalMedicines =
                summary.getOrDefault(
                        "total_medicines",
                        0
                );

        int totalUnits =
                summary.getOrDefault(
                        "total_units",
                        0
                );

        int lowStock =
                summary.getOrDefault(
                        "low_stock",
                        0
                );

        int outOfStock =
                summary.getOrDefault(
                        "out_of_stock",
                        0
                );

        cards.add(
                createSummaryCard(
                        "Total Medicines",
                        String.format(
                                "%,d",
                                totalMedicines
                        ),
                        "Items in catalogue",
                        primaryColor
                )
        );

        cards.add(
                createSummaryCard(
                        "Total Units",
                        String.format(
                                "%,d",
                                totalUnits
                        ),
                        "Units currently available",
                        new Color(
                                70,
                                130,
                                180
                        )
                )
        );

        cards.add(
                createSummaryCard(
                        "Low Stock",
                        String.format(
                                "%,d",
                                lowStock
                        ),
                        "Needs restocking",
                        AppTheme.WARNING
                )
        );

        cards.add(
                createSummaryCard(
                        "Out of Stock",
                        String.format(
                                "%,d",
                                outOfStock
                        ),
                        "Requires immediate action",
                        AppTheme.DANGER
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

        card.setBackground(
                surfaceColor
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                borderColor
                        ),
                        new EmptyBorder(
                                14,
                                16,
                                14,
                                16
                        )
                )
        );

        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setFont(
                AppTheme.SMALL_FONT
        );

        titleLabel.setForeground(
                textSecondaryColor
        );

        JLabel valueLabel =
                new JLabel(value);

        valueLabel.setFont(
                AppTheme.HEADING_FONT
        );

        valueLabel.setForeground(
                textPrimaryColor
        );

        JLabel descriptionLabel =
                new JLabel(description);

        descriptionLabel.setFont(
                AppTheme.SMALL_FONT
        );

        descriptionLabel.setForeground(
                textMutedColor
        );

        if ("Total Medicines".equals(title)) {
            totalItemsLabel = valueLabel;
        }

        if ("Total Units".equals(title)) {
            totalUnitsLabel = valueLabel;
        }

        if ("Low Stock".equals(title)) {
            lowStockLabel = valueLabel;
        }

        if ("Out of Stock".equals(title)) {
            outOfStockLabel = valueLabel;
        }

        JPanel text =
                new JPanel();

        text.setLayout(
                new javax.swing.BoxLayout(
                        text,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        text.setOpaque(false);

        text.add(titleLabel);

        text.add(
                javax.swing.Box.createVerticalStrut(4)
        );

        text.add(valueLabel);

        text.add(
                javax.swing.Box.createVerticalStrut(2)
        );

        text.add(descriptionLabel);

        JPanel accentPanel =
                new JPanel();

        accentPanel.setBackground(
                accent
        );

        accentPanel.setPreferredSize(
                new Dimension(
                        4,
                        0
                )
        );

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
    // REFRESH SUMMARY
    // ============================================================

    private void refreshSummaryCards() {

        Map<String, Integer> summary =
                inventoryDAO.getInventorySummary();

        int totalMedicines =
                summary.getOrDefault(
                        "total_medicines",
                        0
                );

        int totalUnits =
                summary.getOrDefault(
                        "total_units",
                        0
                );

        int lowStock =
                summary.getOrDefault(
                        "low_stock",
                        0
                );

        int outOfStock =
                summary.getOrDefault(
                        "out_of_stock",
                        0
                );

        if (totalItemsLabel != null) {

            totalItemsLabel.setText(
                    String.format(
                            "%,d",
                            totalMedicines
                    )
            );
        }

        if (totalUnitsLabel != null) {

            totalUnitsLabel.setText(
                    String.format(
                            "%,d",
                            totalUnits
                    )
            );
        }

        if (lowStockLabel != null) {

            lowStockLabel.setText(
                    String.format(
                            "%,d",
                            lowStock
                    )
            );
        }

        if (outOfStockLabel != null) {

            outOfStockLabel.setText(
                    String.format(
                            "%,d",
                            outOfStock
                    )
            );
        }
    }

    // ============================================================
    // TOOLBAR
    // ============================================================

    private JComponent buildToolbar() {

        JPanel toolbar =
                new JPanel(
                        new BorderLayout(
                                12,
                                0
                        )
                );

        toolbar.setBackground(
                surfaceColor
        );

        toolbar.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                borderColor
                        ),
                        new EmptyBorder(
                                12,
                                14,
                                12,
                                14
                        )
                )
        );

        JPanel searchPanel =
                new JPanel(
                        new BorderLayout(
                                8,
                                0
                        )
                );

        searchPanel.setBackground(
                surfaceColor
        );

        JLabel searchLabel =
                new JLabel("Search");

        searchLabel.setFont(
                AppTheme.SMALL_FONT
        );

        searchLabel.setForeground(
                textMutedColor
        );

        searchField =
                new JTextField();

        searchField.setFont(
                AppTheme.BODY_FONT
        );

        searchField.setForeground(
                textPrimaryColor
        );

        searchField.setBackground(
                backgroundColor
        );

        searchField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                borderColor
                        ),
                        new EmptyBorder(
                                7,
                                10,
                                7,
                                10
                        )
                )
        );

        searchField.setToolTipText(
                "Search medicine, ID or batch number"
        );

        searchField
                .getDocument()
                .addDocumentListener(
                        new DocumentListener() {

                            @Override
                            public void insertUpdate(
                                    DocumentEvent e
                            ) {
                                applyFilters();
                            }

                            @Override
                            public void removeUpdate(
                                    DocumentEvent e
                            ) {
                                applyFilters();
                            }

                            @Override
                            public void changedUpdate(
                                    DocumentEvent e
                            ) {
                                applyFilters();
                            }
                        }
                );

        searchPanel.add(
                searchLabel,
                BorderLayout.WEST
        );

        searchPanel.add(
                searchField,
                BorderLayout.CENTER
        );

        categoryComboBox =
                new JComboBox<>(
                        new String[]{
                                "All Categories",
                                "Antibiotic",
                                "Analgesic",
                                "Antacid",
                                "Antidiabetic",
                                "Antihistamine",
                                "Cardiovascular",
                                "Vitamin & Supplement"
                        }
                );

        categoryComboBox.setFont(
                AppTheme.SMALL_FONT
        );

        categoryComboBox.addActionListener(
                e -> applyFilters()
        );

        statusComboBox =
                new JComboBox<>(
                        new String[]{
                                "All Status",
                                "In Stock",
                                "Low Stock",
                                "Critical",
                                "Out of Stock"
                        }
                );

        statusComboBox.setFont(
                AppTheme.SMALL_FONT
        );

        statusComboBox.addActionListener(
                e -> applyFilters()
        );

        JPanel filters =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        filters.setBackground(
                surfaceColor
        );

        filters.add(
                new JLabel("Category")
        );

        filters.add(
                categoryComboBox
        );

        filters.add(
                new JLabel("Status")
        );

        filters.add(
                statusComboBox
        );

        toolbar.add(
                searchPanel,
                BorderLayout.CENTER
        );

        toolbar.add(
                filters,
                BorderLayout.EAST
        );

        return toolbar;
    }

    // ============================================================
    // TABLE
    // ============================================================

    private JComponent buildTablePanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(
                surfaceColor
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                borderColor
                        ),
                        new EmptyBorder(
                                10,
                                10,
                                10,
                                10
                        )
                )
        );

        tableModel =
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

        inventoryTable =
                new JTable(tableModel);

        configureTable();

        sorter =
                new TableRowSorter<>(
                        tableModel
                );

        inventoryTable.setRowSorter(
                sorter
        );

        loadInventoryData();

        JScrollPane scrollPane = ModernTableStyler.createScrollPane(inventoryTable);

        panel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        return panel;
    }

    private void configureTable() {
        ModernTableStyler.styleTable(inventoryTable);
        ModernTableStyler.setStatusColumn(inventoryTable, 10);
    }

    // ============================================================
    // LOAD INVENTORY
    // ============================================================

    private void loadInventoryData() {

        List<Map<String, Object>> inventory =
                inventoryDAO.getAllInventory();

        if (tableModel == null) {
            return;
        }

        tableModel.setRowCount(0);

        for (Map<String, Object> row : inventory) {

            Object inventoryIdObject =
                    row.get("inventory_id");

            if (!(inventoryIdObject instanceof Number)) {
                continue;
            }

            long inventoryId =
                    ((Number) inventoryIdObject)
                            .longValue();

            String medicineName =
                    row.get("medicine_name") == null
                            ? "-"
                            : String.valueOf(
                            row.get("medicine_name")
                    );

            String category =
                    row.get("category") == null
                            ? "-"
                            : String.valueOf(
                            row.get("category")
                    );

            String batchNumber =
                    row.get("batch_number") == null
                            ? "-"
                            : String.valueOf(
                            row.get("batch_number")
                    );

            int quantity =
                    row.get("quantity") == null
                            ? 0
                            : ((Number) row.get("quantity"))
                            .intValue();

            int reorderLevel =
                    row.get("reorder_level") == null
                            ? 0
                            : ((Number) row.get("reorder_level"))
                            .intValue();

            Object purchasePriceObject =
                    row.get("purchase_price");
            Object sellingPriceObject =
                    row.get("selling_price");

            double purchasePrice =
                    purchasePriceObject == null
                            ? 0.0
                            : ((Number) purchasePriceObject)
                            .doubleValue();

            double sellingPrice =
                    sellingPriceObject == null
                            ? 0.0
                            : ((Number) sellingPriceObject)
                            .doubleValue();

            double stockValue =
                    quantity * sellingPrice;

            String expiryDate =
                    row.get("expiry_date") == null
                            ? "-"
                            : String.valueOf(
                            row.get("expiry_date")
                    );

            String status =
                    calculateStatus(
                            quantity,
                            reorderLevel
                    );

            tableModel.addRow(
                    new Object[]{
                            inventoryId,
                            medicineName,
                            category,
                            batchNumber,
                            quantity,
                            reorderLevel,
                            String.format(
                                    "₹%,.2f",
                                    purchasePrice
                            ),
                            String.format(
                                    "₹%,.2f",
                                    sellingPrice
                            ),
                            String.format(
                                    "₹%,.2f",
                                    stockValue
                            ),
                            expiryDate,
                            status
                    }
            );
        }

        if (sorter != null) {
            sorter.setRowFilter(null);
        }
    }

    // ============================================================
    // STATUS
    // ============================================================

    private String calculateStatus(
            int stock,
            int minimum
    ) {

        if (stock == 0) {
            return "Out of Stock";
        }

        if (stock <= 5) {
            return "Critical";
        }

        if (stock < minimum) {
            return "Low Stock";
        }

        return "In Stock";
    }

    // ============================================================
    // FILTERS
    // ============================================================

    private void applyFilters() {

        if (sorter == null) {
            return;
        }

        String search =
                searchField
                        .getText()
                        .trim()
                        .toLowerCase();

        String category =
                String.valueOf(
                        categoryComboBox
                                .getSelectedItem()
                );

        String status =
                String.valueOf(
                        statusComboBox
                                .getSelectedItem()
                );

        RowFilter<DefaultTableModel, Object> filter =
                new RowFilter<DefaultTableModel, Object>() {

                    @Override
                    public boolean include(
                            Entry<? extends DefaultTableModel, ? extends Object> entry
                    ) {

                        boolean searchMatch =
                                search.isEmpty();

                        if (!searchMatch) {

                            for (
                                    int i = 0;
                                    i < entry.getValueCount();
                                    i++
                            ) {

                                String value =
                                        String.valueOf(
                                                entry.getValue(i)
                                        ).toLowerCase();

                                if (value.contains(search)) {

                                    searchMatch = true;
                                    break;
                                }
                            }
                        }

                        boolean categoryMatch =
                                category.equals(
                                        "All Categories"
                                )
                                        ||
                                        String.valueOf(
                                                entry.getValue(2)
                                        ).equals(category);

                        boolean statusMatch =
                                status.equals(
                                        "All Status"
                                )
                                        ||
                                        String.valueOf(
                                                entry.getValue(10)
                                        ).equals(status);

                        return searchMatch
                                && categoryMatch
                                && statusMatch;
                    }
                };

        sorter.setRowFilter(filter);
    }

    // ============================================================
    // STOCK ADJUSTMENT
    // ============================================================

    private void showStockAdjustmentDialog() {

        int selectedRow =
                inventoryTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a medicine from the inventory table first.",
                    "Select Medicine",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int modelRow =
                inventoryTable
                        .convertRowIndexToModel(
                                selectedRow
                        );

        Object inventoryIdObject =
                tableModel.getValueAt(
                        modelRow,
                        0
                );

        if (!(inventoryIdObject instanceof Number)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid inventory ID.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        long inventoryId =
                ((Number) inventoryIdObject)
                        .longValue();

        String medicineName =
                String.valueOf(
                        tableModel.getValueAt(
                                modelRow,
                                1
                        )
                );

        Object stockObject =
                tableModel.getValueAt(
                        modelRow,
                        4
                );

        if (!(stockObject instanceof Number)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid stock quantity.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        int currentStock =
                ((Number) stockObject)
                        .intValue();

        JDialog dialog =
                new JDialog(
                        SwingUtilities.getWindowAncestor(this),
                        "Adjust Stock",
                        Dialog.ModalityType.APPLICATION_MODAL
                );

        dialog.setSize(
                430,
                330
        );

        dialog.setLocationRelativeTo(
                this
        );

        JPanel content =
                new JPanel(
                        new BorderLayout()
                );

        content.setBackground(
                surfaceColor
        );

        content.setBorder(
                new EmptyBorder(
                        20,
                        22,
                        20,
                        22
                )
        );

        JLabel medicineLabel =
                new JLabel(
                        medicineName
                                + "  |  Current Stock: "
                                + currentStock
                );

        medicineLabel.setFont(
                AppTheme.BODY_FONT
        );

        medicineLabel.setForeground(
                textPrimaryColor
        );

        JComboBox<String> actionCombo =
                new JComboBox<>(
                        new String[]{
                                "Add Stock",
                                "Remove Stock"
                        }
                );

        actionCombo.setFont(
                AppTheme.BODY_FONT
        );

        JTextField quantityField =
                new JTextField();

        quantityField.setFont(
                AppTheme.BODY_FONT
        );

        quantityField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                borderColor
                        ),
                        new EmptyBorder(
                                7,
                                9,
                                7,
                                9
                        )
                )
        );

        JPanel form =
                new JPanel(
                        new GridBagLayout()
                );

        form.setBackground(
                surfaceColor
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        8,
                        5,
                        8,
                        5
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        form.add(
                medicineLabel,
                gbc
        );

        gbc.gridwidth = 1;

        gbc.gridy++;

        form.add(
                new JLabel("Action"),
                gbc
        );

        gbc.gridx = 1;

        form.add(
                actionCombo,
                gbc
        );

        gbc.gridx = 0;

        gbc.gridy++;

        form.add(
                new JLabel("Quantity"),
                gbc
        );

        gbc.gridx = 1;

        form.add(
                quantityField,
                gbc
        );

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        buttons.setBackground(
                surfaceColor
        );

        JButton cancel =
                createSecondaryButton(
                        "Cancel"
                );

        JButton apply =
                createPrimaryButton(
                        "Apply"
                );

        cancel.addActionListener(
                e -> dialog.dispose()
        );

        apply.addActionListener(e -> {

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
                        dialog,
                        "Please enter a valid positive quantity.",
                        "Invalid Quantity",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            String action =
                    String.valueOf(
                            actionCombo
                                    .getSelectedItem()
                    );

            int newStock;

            if ("Add Stock".equals(action)) {

                newStock =
                        currentStock + quantity;

            } else {

                newStock =
                        currentStock - quantity;

                if (newStock < 0) {

                    JOptionPane.showMessageDialog(
                            dialog,
                            "Stock cannot become negative.",
                            "Invalid Stock",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }
            }

            boolean updated =
                    inventoryDAO.updateStock(
                            inventoryId,
                            newStock
                    );

            if (!updated) {

                JOptionPane.showMessageDialog(
                        dialog,
                        "Failed to update stock in database.",
                        "Update Failed",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            // Reload table from MySQL
            loadInventoryData();

            // Refresh dashboard-style inventory summary
            refreshSummaryCards();

            dialog.dispose();

            JOptionPane.showMessageDialog(
                    this,
                    "Stock updated successfully.",
                    "Inventory Updated",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        buttons.add(cancel);

        buttons.add(apply);

        content.add(
                form,
                BorderLayout.CENTER
        );

        content.add(
                buttons,
                BorderLayout.SOUTH
        );

        dialog.setContentPane(
                content
        );

        dialog.setVisible(
                true
        );
    }

    // ============================================================
    // PRIMARY BUTTON
    // ============================================================

    private JButton createPrimaryButton(
            String text
    ) {
        JButton button = new JButton(text);
        button.setFont(AppTheme.BODY_BOLD_FONT);
        button.setForeground(AppTheme.ACCENT_TEXT);
        button.setBackground(AppTheme.ACCENT);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.putClientProperty("FlatLaf.style", "arc: 999; hoverBackground: " + AppTheme.toHex(AppTheme.ACCENT_HOVER));
        button.setBorder(BorderFactory.createEmptyBorder(9, 20, 9, 20));
        return button;
    }

    // ============================================================
    // SECONDARY BUTTON
    // ============================================================

    private JButton createSecondaryButton(
            String text
    ) {
        JButton button = new JButton(text);
        button.setFont(AppTheme.BODY_FONT);
        button.setForeground(textPrimaryColor);
        button.setBackground(surfaceColor);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.putClientProperty("FlatLaf.style", "arc: 999;");
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1, true),
                new EmptyBorder(8, 18, 8, 18)
        ));
        return button;
    }

    // ============================================================
    // TABLE RENDERER
    // ============================================================

    private class InventoryTableRenderer
            extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            Component component =
                    super.getTableCellRendererComponent(
                            table,
                            value,
                            isSelected,
                            hasFocus,
                            row,
                            column
                    );

            if (isSelected) {

                component.setBackground(
                        primaryLightColor
                );

                component.setForeground(
                        Color.WHITE
                );

            } else {

                component.setBackground(
                        surfaceColor
                );

                component.setForeground(
                        textPrimaryColor
                );
            }

            setBorder(
                    BorderFactory.createEmptyBorder(
                            0,
                            8,
                            0,
                            8
                    )
            );

            // Status column
            if (column == 10 && !isSelected) {

                String status =
                        String.valueOf(
                                value
                        );

                if ("Out of Stock".equals(status)) {

                    component.setForeground(
                            AppTheme.DANGER
                    );

                } else if ("Critical".equals(status)) {

                    component.setForeground(
                            AppTheme.DANGER
                    );

                } else if ("Low Stock".equals(status)) {

                    component.setForeground(
                            AppTheme.WARNING
                    );
                }
            }

            return component;
        }
    }
}
