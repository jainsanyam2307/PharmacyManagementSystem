package com.pharmacy.ui.main;

import com.pharmacy.config.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * SuppliersPanel
 *
 * UI-only supplier management module.
 *
 * Current version:
 * - Sample in-memory data
 * - Supplier summary
 * - Supplier search
 * - Supplier table
 * - Add supplier
 * - Edit supplier
 * - View supplier
 * - Disable supplier
 *
 * Database integration will be added later.
 */
public class SuppliersPanel extends JPanel {

    private JTable supplierTable;
    private DefaultTableModel supplierTableModel;

    private JTextField searchField;

    public SuppliersPanel() {

        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildMainContent(), BorderLayout.CENTER);
    }

    // ============================================================
    // HEADER
    // ============================================================

    private JComponent buildHeader() {

        JPanel header = new JPanel(new BorderLayout());

        header.setBackground(AppTheme.BACKGROUND);

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

        titlePanel.setBackground(AppTheme.BACKGROUND);

        JLabel title =
                new JLabel("Suppliers");

        title.setFont(AppTheme.HEADING_FONT);
        title.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel subtitle =
                new JLabel(
                        "Manage supplier information, contacts and purchase relationships."
                );

        subtitle.setFont(AppTheme.BODY_FONT);
        subtitle.setForeground(AppTheme.TEXT_MUTED);

        titlePanel.add(title);

        titlePanel.add(
                Box.createVerticalStrut(5)
        );

        titlePanel.add(subtitle);

        JButton addSupplierButton =
                createPrimaryButton("Add Supplier");

        addSupplierButton.addActionListener(
                e -> showSupplierDialog(null, -1)
        );

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        header.add(
                addSupplierButton,
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

        content.setBackground(
                AppTheme.BACKGROUND
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

        content.add(
                buildSupplierTable(),
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
                AppTheme.BACKGROUND
        );

        cards.setPreferredSize(
                new Dimension(
                        0,
                        100
                )
        );

        cards.add(
                createSummaryCard(
                        "Total Suppliers",
                        "24",
                        "All registered suppliers",
                        AppTheme.PRIMARY
                )
        );

        cards.add(
                createSummaryCard(
                        "Active Suppliers",
                        "21",
                        "Currently supplying medicines",
                        AppTheme.SUCCESS
                )
        );

        cards.add(
                createSummaryCard(
                        "Pending Payments",
                        "₹38,400",
                        "Amount payable",
                        AppTheme.WARNING
                )
        );

        cards.add(
                createSummaryCard(
                        "This Month",
                        "8",
                        "Suppliers with purchases",
                        AppTheme.PRIMARY
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
                AppTheme.SURFACE
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                AppTheme.BORDER
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

        accentPanel.setBackground(
                accent
        );

        accentPanel.setPreferredSize(
                new Dimension(
                        4,
                        0
                )
        );

        JPanel textPanel =
                new JPanel();

        textPanel.setLayout(
                new BoxLayout(
                        textPanel,
                        BoxLayout.Y_AXIS
                )
        );

        textPanel.setOpaque(false);

        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setFont(
                AppTheme.SMALL_FONT
        );

        titleLabel.setForeground(
                AppTheme.TEXT_MUTED
        );

        JLabel valueLabel =
                new JLabel(value);

        valueLabel.setFont(
                AppTheme.HEADING_FONT
        );

        valueLabel.setForeground(
                AppTheme.TEXT_PRIMARY
        );

        JLabel descriptionLabel =
                new JLabel(description);

        descriptionLabel.setFont(
                AppTheme.SMALL_FONT
        );

        descriptionLabel.setForeground(
                AppTheme.TEXT_MUTED
        );

        textPanel.add(titleLabel);

        textPanel.add(
                Box.createVerticalStrut(4)
        );

        textPanel.add(valueLabel);

        textPanel.add(
                Box.createVerticalStrut(2)
        );

        textPanel.add(descriptionLabel);

        card.add(
                accentPanel,
                BorderLayout.WEST
        );

        card.add(
                textPanel,
                BorderLayout.CENTER
        );

        return card;
    }

    // ============================================================
    // SUPPLIER TABLE
    // ============================================================

    private JComponent buildSupplierTable() {

        JPanel panel =
                new JPanel(
                        new BorderLayout(0, 12)
                );

        panel.setBackground(
                AppTheme.SURFACE
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                AppTheme.BORDER
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

        top.setBackground(
                AppTheme.SURFACE
        );

        JLabel title =
                new JLabel(
                        "Supplier Directory"
                );

        title.setFont(
                AppTheme.SUBHEADING_FONT
        );

        title.setForeground(
                AppTheme.TEXT_PRIMARY
        );

        searchField =
                createTextField();

        searchField.setPreferredSize(
                new Dimension(
                        280,
                        36
                )
        );

        searchField.setToolTipText(
                "Search supplier, phone or email"
        );

        top.add(
                title,
                BorderLayout.WEST
        );

        top.add(
                searchField,
                BorderLayout.EAST
        );

        panel.add(
                top,
                BorderLayout.NORTH
        );

        supplierTableModel =
                new DefaultTableModel(
                        new String[]{
                                "Supplier ID",
                                "Supplier Name",
                                "Contact Person",
                                "Phone",
                                "Email",
                                "City",
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

        loadSampleSuppliers();

        supplierTable =
                new JTable(
                        supplierTableModel
                );

        configureSupplierTable();

        JScrollPane scrollPane =
                new JScrollPane(
                        supplierTable
                );

        scrollPane.setBorder(null);

        scrollPane
                .getVerticalScrollBar()
                .setUnitIncrement(16);

        panel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        JPanel bottom =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        bottom.setBackground(
                AppTheme.SURFACE
        );

        JButton viewButton =
                createSecondaryButton(
                        "View"
                );

        JButton editButton =
                createSecondaryButton(
                        "Edit"
                );

        JButton disableButton =
                createSecondaryButton(
                        "Disable"
                );

        viewButton.addActionListener(
                e -> viewSelectedSupplier()
        );

        editButton.addActionListener(
                e -> editSelectedSupplier()
        );

        disableButton.addActionListener(
                e -> disableSelectedSupplier()
        );

        bottom.add(viewButton);
        bottom.add(editButton);
        bottom.add(disableButton);

        panel.add(
                bottom,
                BorderLayout.SOUTH
        );

        searchField
                .getDocument()
                .addDocumentListener(
                        new javax.swing.event.DocumentListener() {

                            private void filter() {

                                String search =
                                        searchField
                                                .getText()
                                                .trim()
                                                .toLowerCase();

                                for (
                                        int row =
                                        supplierTableModel
                                                .getRowCount() - 1;
                                        row >= 0;
                                        row--
                                ) {

                                    if (search.isEmpty()) {
                                        continue;
                                    }

                                    boolean found = false;

                                    for (
                                            int column = 0;
                                            column <
                                                    supplierTableModel
                                                            .getColumnCount();
                                            column++
                                    ) {

                                        Object value =
                                                supplierTableModel
                                                        .getValueAt(
                                                                row,
                                                                column
                                                        );

                                        if (
                                                value != null &&
                                                        value.toString()
                                                                .toLowerCase()
                                                                .contains(search)
                                        ) {

                                            found = true;
                                            break;
                                        }
                                    }

                                    if (!found) {

                                        supplierTable
                                                .getSelectionModel()
                                                .removeSelectionInterval(
                                                        row,
                                                        row
                                                );
                                    }
                                }
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

        supplierTable.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent e
                    ) {

                        if (e.getClickCount() == 2) {
                            viewSelectedSupplier();
                        }
                    }
                }
        );

        return panel;
    }

    // ============================================================
    // SAMPLE DATA
    // ============================================================

    private void loadSampleSuppliers() {

        Object[][] data = {

                {
                        "SUP-001",
                        "MediSource Pvt Ltd",
                        "Rajesh Sharma",
                        "9876543210",
                        "sales@medisource.com",
                        "Jaipur",
                        "Active"
                },

                {
                        "SUP-002",
                        "HealthCare Distributors",
                        "Amit Verma",
                        "9812345678",
                        "contact@healthcare.com",
                        "Jaipur",
                        "Active"
                },

                {
                        "SUP-003",
                        "MedLife Suppliers",
                        "Neha Gupta",
                        "9823456712",
                        "info@medlife.com",
                        "Delhi",
                        "Active"
                },

                {
                        "SUP-004",
                        "PharmaLink",
                        "Suresh Meena",
                        "9898765432",
                        "sales@pharmalink.com",
                        "Kota",
                        "Active"
                },

                {
                        "SUP-005",
                        "Wellness Pharma",
                        "Priya Singh",
                        "9765432189",
                        "contact@wellness.com",
                        "Ajmer",
                        "Inactive"
                },

                {
                        "SUP-006",
                        "LifeCare Medicines",
                        "Anil Kumar",
                        "9956781234",
                        "sales@lifecare.com",
                        "Jaipur",
                        "Active"
                }
        };

        for (Object[] row : data) {
            supplierTableModel.addRow(row);
        }
    }

    // ============================================================
    // ADD / EDIT SUPPLIER
    // ============================================================

    private void showSupplierDialog(
            String supplierId,
            int selectedRow
    ) {

        boolean editing =
                supplierId != null &&
                        selectedRow >= 0;

        JDialog dialog =
                new JDialog(
                        SwingUtilities.getWindowAncestor(this),
                        editing
                                ? "Edit Supplier"
                                : "Add Supplier",
                        Dialog.ModalityType.APPLICATION_MODAL
                );

        dialog.setSize(
                520,
                520
        );

        dialog.setLocationRelativeTo(this);

        JPanel content =
                new JPanel(
                        new BorderLayout()
                );

        content.setBackground(
                AppTheme.SURFACE
        );

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
                        editing
                                ? "Edit Supplier"
                                : "Add New Supplier"
                );

        title.setFont(
                AppTheme.SUBHEADING_FONT
        );

        title.setForeground(
                AppTheme.TEXT_PRIMARY
        );

        JPanel form =
                new JPanel(
                        new GridBagLayout()
                );

        form.setBackground(
                AppTheme.SURFACE
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        7,
                        6,
                        7,
                        6
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1;

        JTextField nameField =
                createTextField();

        JTextField contactField =
                createTextField();

        JTextField phoneField =
                createTextField();

        JTextField emailField =
                createTextField();

        JTextField cityField =
                createTextField();

        JTextField addressField =
                createTextField();

        JTextField gstField =
                createTextField();

        if (editing) {

            nameField.setText(
                    String.valueOf(
                            supplierTableModel
                                    .getValueAt(
                                            selectedRow,
                                            1
                                    )
                    )
            );

            contactField.setText(
                    String.valueOf(
                            supplierTableModel
                                    .getValueAt(
                                            selectedRow,
                                            2
                                    )
                    )
            );

            phoneField.setText(
                    String.valueOf(
                            supplierTableModel
                                    .getValueAt(
                                            selectedRow,
                                            3
                                    )
                    )
            );

            emailField.setText(
                    String.valueOf(
                            supplierTableModel
                                    .getValueAt(
                                            selectedRow,
                                            4
                                    )
                    )
            );

            cityField.setText(
                    String.valueOf(
                            supplierTableModel
                                    .getValueAt(
                                            selectedRow,
                                            5
                                    )
                    )
            );
        }

        addFormRow(
                form,
                gbc,
                0,
                "Supplier Name *",
                nameField
        );

        addFormRow(
                form,
                gbc,
                1,
                "Contact Person",
                contactField
        );

        addFormRow(
                form,
                gbc,
                2,
                "Phone",
                phoneField
        );

        addFormRow(
                form,
                gbc,
                3,
                "Email",
                emailField
        );

        addFormRow(
                form,
                gbc,
                4,
                "City",
                cityField
        );

        addFormRow(
                form,
                gbc,
                5,
                "Address",
                addressField
        );

        addFormRow(
                form,
                gbc,
                6,
                "GST Number",
                gstField
        );

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        buttons.setBackground(
                AppTheme.SURFACE
        );

        JButton cancelButton =
                createSecondaryButton(
                        "Cancel"
                );

        JButton saveButton =
                createPrimaryButton(
                        editing
                                ? "Save Changes"
                                : "Add Supplier"
                );

        cancelButton.addActionListener(
                e -> dialog.dispose()
        );

        saveButton.addActionListener(
                e -> {

                    String name =
                            nameField
                                    .getText()
                                    .trim();

                    if (name.isEmpty()) {

                        JOptionPane.showMessageDialog(
                                dialog,
                                "Supplier name is required.",
                                "Missing Information",
                                JOptionPane.WARNING_MESSAGE
                        );

                        return;
                    }

                    if (editing) {

                        supplierTableModel
                                .setValueAt(
                                        name,
                                        selectedRow,
                                        1
                                );

                        supplierTableModel
                                .setValueAt(
                                        contactField
                                                .getText()
                                                .trim(),
                                        selectedRow,
                                        2
                                );

                        supplierTableModel
                                .setValueAt(
                                        phoneField
                                                .getText()
                                                .trim(),
                                        selectedRow,
                                        3
                                );

                        supplierTableModel
                                .setValueAt(
                                        emailField
                                                .getText()
                                                .trim(),
                                        selectedRow,
                                        4
                                );

                        supplierTableModel
                                .setValueAt(
                                        cityField
                                                .getText()
                                                .trim(),
                                        selectedRow,
                                        5
                                );

                        JOptionPane.showMessageDialog(
                                dialog,
                                "Supplier updated successfully.",
                                "Supplier Updated",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                    } else {

                        String newId =
                                String.format(
                                        "SUP-%03d",
                                        supplierTableModel
                                                .getRowCount() + 1
                                );

                        supplierTableModel.addRow(
                                new Object[]{
                                        newId,
                                        name,
                                        contactField
                                                .getText()
                                                .trim(),
                                        phoneField
                                                .getText()
                                                .trim(),
                                        emailField
                                                .getText()
                                                .trim(),
                                        cityField
                                                .getText()
                                                .trim(),
                                        "Active"
                                }
                        );

                        JOptionPane.showMessageDialog(
                                dialog,
                                "Supplier added successfully.",
                                "Supplier Added",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }

                    dialog.dispose();
                }
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
    // VIEW SUPPLIER
    // ============================================================

    private void viewSelectedSupplier() {

        int row =
                supplierTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a supplier first.",
                    "Select Supplier",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        StringBuilder details =
                new StringBuilder();

        details.append(
                "Supplier Details\n\n"
        );

        for (
                int column = 0;
                column <
                        supplierTableModel
                                .getColumnCount();
                column++
        ) {

            details.append(
                    supplierTableModel
                            .getColumnName(column)
            );

            details.append(": ");

            details.append(
                    supplierTableModel
                            .getValueAt(
                                    row,
                                    column
                            )
            );

            details.append("\n");
        }

        details.append(
                "\nPurchase history will be available "
                        + "after database integration."
        );

        JOptionPane.showMessageDialog(
                this,
                details.toString(),
                "Supplier Details",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ============================================================
    // EDIT SUPPLIER
    // ============================================================

    private void editSelectedSupplier() {

        int row =
                supplierTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a supplier first.",
                    "Select Supplier",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String supplierId =
                String.valueOf(
                        supplierTableModel
                                .getValueAt(
                                        row,
                                        0
                                )
                );

        showSupplierDialog(
                supplierId,
                row
        );
    }

    // ============================================================
    // DISABLE SUPPLIER
    // ============================================================

    private void disableSelectedSupplier() {

        int row =
                supplierTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a supplier first.",
                    "Select Supplier",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String currentStatus =
                String.valueOf(
                        supplierTableModel
                                .getValueAt(
                                        row,
                                        6
                                )
                );

        String supplierName =
                String.valueOf(
                        supplierTableModel
                                .getValueAt(
                                        row,
                                        1
                                )
                );

        if (currentStatus.equals("Inactive")) {

            JOptionPane.showMessageDialog(
                    this,
                    "This supplier is already inactive.",
                    "Supplier Status",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Disable supplier \""
                                + supplierName
                                + "\"?",
                        "Disable Supplier",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (choice ==
                JOptionPane.YES_OPTION) {

            supplierTableModel.setValueAt(
                    "Inactive",
                    row,
                    6
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Supplier disabled successfully.",
                    "Supplier Disabled",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    // ============================================================
    // TABLE CONFIGURATION
    // ============================================================

    private void configureSupplierTable() {

        supplierTable.setFont(
                AppTheme.SMALL_FONT
        );

        supplierTable.setForeground(
                AppTheme.TEXT_PRIMARY
        );

        supplierTable.setBackground(
                AppTheme.SURFACE
        );

        supplierTable.setGridColor(
                AppTheme.BORDER
        );

        supplierTable.setRowHeight(40);

        supplierTable.setShowVerticalLines(false);

        supplierTable.setShowHorizontalLines(true);

        supplierTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        supplierTable.getTableHeader()
                .setFont(
                        AppTheme.SMALL_FONT
                );

        supplierTable.getTableHeader()
                .setBackground(
                        AppTheme.BACKGROUND
                );

        supplierTable.getTableHeader()
                .setForeground(
                        AppTheme.TEXT_MUTED
                );

        supplierTable.getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                40
                        )
                );

        supplierTable.setDefaultRenderer(
                Object.class,
                new SupplierTableRenderer()
        );
    }

    // ============================================================
    // UI HELPERS
    // ============================================================

    private JLabel createLabel(
            String text
    ) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                AppTheme.SMALL_FONT
        );

        label.setForeground(
                AppTheme.TEXT_MUTED
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
                AppTheme.TEXT_PRIMARY
        );

        field.setBackground(
                AppTheme.BACKGROUND
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                AppTheme.BORDER
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
                AppTheme.PRIMARY
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
                AppTheme.TEXT_PRIMARY
        );

        button.setBackground(
                AppTheme.SURFACE
        );

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                AppTheme.BORDER
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

    private class SupplierTableRenderer
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
                        AppTheme.PRIMARY_LIGHT
                );

                setForeground(
                        AppTheme.PRIMARY_DARK
                );

            } else {

                setBackground(
                        AppTheme.SURFACE
                );

                setForeground(
                        AppTheme.TEXT_PRIMARY
                );
            }

            if (column == 6 &&
                    value != null) {

                String status =
                        value.toString();

                setHorizontalAlignment(
                        SwingConstants.CENTER
                );

                if (status.equals("Active")) {

                    setForeground(
                            AppTheme.SUCCESS
                    );

                } else {

                    setForeground(
                            AppTheme.TEXT_MUTED
                    );
                }

            } else {

                setHorizontalAlignment(
                        SwingConstants.LEFT
                );
            }

            return this;
        }
    }
}