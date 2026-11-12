package com.pharmacy.ui.main;

import com.pharmacy.config.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * CustomersPanel
 *
 * UI-only customer management module.
 *
 * Current version:
 * - Sample in-memory data
 * - Customer summary
 * - Customer search
 * - Customer table
 * - Add customer
 * - Edit customer
 * - View customer
 * - Disable customer
 *
 * Database integration will be added later.
 */
public class CustomersPanel extends JPanel {

    private JTable customerTable;
    private DefaultTableModel customerTableModel;

    private JTextField searchField;

    public CustomersPanel() {

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

        JLabel title = new JLabel("Customers");
        title.setFont(AppTheme.HEADING_FONT);
        title.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel(
                "Manage customer information and purchase relationships."
        );
        subtitle.setFont(AppTheme.BODY_FONT);
        subtitle.setForeground(AppTheme.TEXT_MUTED);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

        JButton addButton = createPrimaryButton("Add Customer");

        addButton.addActionListener(
                e -> showCustomerDialog(-1)
        );

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        header.add(
                addButton,
                BorderLayout.EAST
        );

        return header;
    }

    // ============================================================
    // MAIN CONTENT
    // ============================================================

    private JComponent buildMainContent() {

        JPanel content = new JPanel(
                new BorderLayout(0, 16)
        );

        content.setBackground(AppTheme.BACKGROUND);

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
                buildCustomerTable(),
                BorderLayout.CENTER
        );

        return content;
    }

    // ============================================================
    // SUMMARY CARDS
    // ============================================================

    private JComponent buildSummaryCards() {

        JPanel cards = new JPanel(
                new GridLayout(
                        1,
                        4,
                        14,
                        0
                )
        );

        cards.setBackground(AppTheme.BACKGROUND);

        cards.setPreferredSize(
                new Dimension(
                        0,
                        100
                )
        );

        cards.add(
                createSummaryCard(
                        "Total Customers",
                        "186",
                        "All registered customers",
                        AppTheme.PRIMARY
                )
        );

        cards.add(
                createSummaryCard(
                        "Active Customers",
                        "174",
                        "Currently active",
                        AppTheme.SUCCESS
                )
        );

        cards.add(
                createSummaryCard(
                        "Today's Sales",
                        "₹24,850",
                        "Customer purchases today",
                        AppTheme.PRIMARY
                )
        );

        cards.add(
                createSummaryCard(
                        "Total Purchases",
                        "₹8.42 L",
                        "Customer purchase value",
                        AppTheme.WARNING
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

        JPanel card = new JPanel(
                new BorderLayout()
        );

        card.setBackground(AppTheme.SURFACE);

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

        JPanel accentPanel = new JPanel();
        accentPanel.setBackground(accent);

        accentPanel.setPreferredSize(
                new Dimension(
                        4,
                        0
                )
        );

        JPanel textPanel = new JPanel();

        textPanel.setLayout(
                new BoxLayout(
                        textPanel,
                        BoxLayout.Y_AXIS
                )
        );

        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(AppTheme.SMALL_FONT);
        titleLabel.setForeground(AppTheme.TEXT_MUTED);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(AppTheme.HEADING_FONT);
        valueLabel.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel descriptionLabel =
                new JLabel(description);

        descriptionLabel.setFont(
                AppTheme.SMALL_FONT
        );

        descriptionLabel.setForeground(
                AppTheme.TEXT_MUTED
        );

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(valueLabel);
        textPanel.add(Box.createVerticalStrut(2));
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
    // CUSTOMER TABLE
    // ============================================================

    private JComponent buildCustomerTable() {

        JPanel panel = new JPanel(
                new BorderLayout(0, 12)
        );

        panel.setBackground(AppTheme.SURFACE);

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

        JPanel top = new JPanel(
                new BorderLayout()
        );

        top.setBackground(AppTheme.SURFACE);

        JLabel title =
                new JLabel("Customer Directory");

        title.setFont(
                AppTheme.SUBHEADING_FONT
        );

        title.setForeground(
                AppTheme.TEXT_PRIMARY
        );

        searchField = createTextField();

        searchField.setPreferredSize(
                new Dimension(
                        280,
                        36
                )
        );

        searchField.setToolTipText(
                "Search customer, phone or email"
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

        customerTableModel =
                new DefaultTableModel(
                        new String[]{
                                "Customer ID",
                                "Customer Name",
                                "Phone",
                                "Email",
                                "City",
                                "Total Purchases",
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

        loadSampleCustomers();

        customerTable =
                new JTable(customerTableModel);

        configureCustomerTable();

        JScrollPane scrollPane =
                new JScrollPane(customerTable);

        scrollPane.setBorder(null);

        scrollPane
                .getVerticalScrollBar()
                .setUnitIncrement(16);

        panel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        JPanel bottom = new JPanel(
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
                createSecondaryButton("View");

        JButton editButton =
                createSecondaryButton("Edit");

        JButton disableButton =
                createSecondaryButton("Disable");

        viewButton.addActionListener(
                e -> viewSelectedCustomer()
        );

        editButton.addActionListener(
                e -> editSelectedCustomer()
        );

        disableButton.addActionListener(
                e -> disableSelectedCustomer()
        );

        bottom.add(viewButton);
        bottom.add(editButton);
        bottom.add(disableButton);

        panel.add(
                bottom,
                BorderLayout.SOUTH
        );

        installSearchListener();

        customerTable.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent e
                    ) {

                        if (e.getClickCount() == 2) {
                            viewSelectedCustomer();
                        }
                    }
                }
        );

        return panel;
    }

    // ============================================================
    // SAMPLE DATA
    // ============================================================

    private void loadSampleCustomers() {

        Object[][] data = {

                {
                        "CUS-001",
                        "Rahul Sharma",
                        "9876543210",
                        "rahul.sharma@email.com",
                        "Jaipur",
                        "₹18,450",
                        "Active"
                },

                {
                        "CUS-002",
                        "Priya Verma",
                        "9812345678",
                        "priya.verma@email.com",
                        "Jaipur",
                        "₹12,800",
                        "Active"
                },

                {
                        "CUS-003",
                        "Amit Gupta",
                        "9823456712",
                        "amit.gupta@email.com",
                        "Kota",
                        "₹24,650",
                        "Active"
                },

                {
                        "CUS-004",
                        "Neha Singh",
                        "9898765432",
                        "neha.singh@email.com",
                        "Ajmer",
                        "₹8,420",
                        "Active"
                },

                {
                        "CUS-005",
                        "Suresh Kumar",
                        "9765432189",
                        "suresh.kumar@email.com",
                        "Jaipur",
                        "₹31,200",
                        "Active"
                },

                {
                        "CUS-006",
                        "Pooja Meena",
                        "9956781234",
                        "pooja.meena@email.com",
                        "Alwar",
                        "₹5,600",
                        "Inactive"
                }
        };

        for (Object[] row : data) {
            customerTableModel.addRow(row);
        }
    }

    // ============================================================
    // SEARCH
    // ============================================================

    private void installSearchListener() {

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

                                if (search.isEmpty()) {

                                    customerTable.clearSelection();
                                    return;
                                }

                                int firstMatch = -1;

                                for (
                                        int row = 0;
                                        row <
                                                customerTableModel
                                                        .getRowCount();
                                        row++
                                ) {

                                    boolean found = false;

                                    for (
                                            int column = 0;
                                            column <
                                                    customerTableModel
                                                            .getColumnCount();
                                            column++
                                    ) {

                                        Object value =
                                                customerTableModel
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

                                    if (found) {

                                        firstMatch = row;
                                        break;
                                    }
                                }

                                customerTable.clearSelection();

                                if (firstMatch >= 0) {

                                    customerTable
                                            .setRowSelectionInterval(
                                                    firstMatch,
                                                    firstMatch
                                            );

                                    customerTable
                                            .scrollRectToVisible(
                                                    customerTable
                                                            .getCellRect(
                                                                    firstMatch,
                                                                    0,
                                                                    true
                                                            )
                                            );
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
    }

    // ============================================================
    // ADD / EDIT CUSTOMER
    // ============================================================

    private void showCustomerDialog(
            int selectedRow
    ) {

        boolean editing =
                selectedRow >= 0;

        JDialog dialog =
                new JDialog(
                        SwingUtilities.getWindowAncestor(this),
                        editing
                                ? "Edit Customer"
                                : "Add Customer",
                        Dialog.ModalityType.APPLICATION_MODAL
                );

        dialog.setSize(
                520,
                470
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
                                ? "Edit Customer"
                                : "Add New Customer"
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

        JTextField phoneField =
                createTextField();

        JTextField emailField =
                createTextField();

        JTextField cityField =
                createTextField();

        JTextField addressField =
                createTextField();

        if (editing) {

            nameField.setText(
                    String.valueOf(
                            customerTableModel
                                    .getValueAt(
                                            selectedRow,
                                            1
                                    )
                    )
            );

            phoneField.setText(
                    String.valueOf(
                            customerTableModel
                                    .getValueAt(
                                            selectedRow,
                                            2
                                    )
                    )
            );

            emailField.setText(
                    String.valueOf(
                            customerTableModel
                                    .getValueAt(
                                            selectedRow,
                                            3
                                    )
                    )
            );

            cityField.setText(
                    String.valueOf(
                            customerTableModel
                                    .getValueAt(
                                            selectedRow,
                                            4
                                    )
                    )
            );
        }

        addFormRow(
                form,
                gbc,
                0,
                "Customer Name *",
                nameField
        );

        addFormRow(
                form,
                gbc,
                1,
                "Phone",
                phoneField
        );

        addFormRow(
                form,
                gbc,
                2,
                "Email",
                emailField
        );

        addFormRow(
                form,
                gbc,
                3,
                "City",
                cityField
        );

        addFormRow(
                form,
                gbc,
                4,
                "Address",
                addressField
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
                createSecondaryButton("Cancel");

        JButton saveButton =
                createPrimaryButton(
                        editing
                                ? "Save Changes"
                                : "Add Customer"
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
                                "Customer name is required.",
                                "Missing Information",
                                JOptionPane.WARNING_MESSAGE
                        );

                        return;
                    }

                    if (editing) {

                        customerTableModel
                                .setValueAt(
                                        name,
                                        selectedRow,
                                        1
                                );

                        customerTableModel
                                .setValueAt(
                                        phoneField
                                                .getText()
                                                .trim(),
                                        selectedRow,
                                        2
                                );

                        customerTableModel
                                .setValueAt(
                                        emailField
                                                .getText()
                                                .trim(),
                                        selectedRow,
                                        3
                                );

                        customerTableModel
                                .setValueAt(
                                        cityField
                                                .getText()
                                                .trim(),
                                        selectedRow,
                                        4
                                );

                        JOptionPane.showMessageDialog(
                                dialog,
                                "Customer updated successfully.",
                                "Customer Updated",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                    } else {

                        String newId =
                                String.format(
                                        "CUS-%03d",
                                        customerTableModel
                                                .getRowCount() + 1
                                );

                        customerTableModel.addRow(
                                new Object[]{
                                        newId,
                                        name,
                                        phoneField
                                                .getText()
                                                .trim(),
                                        emailField
                                                .getText()
                                                .trim(),
                                        cityField
                                                .getText()
                                                .trim(),
                                        "₹0",
                                        "Active"
                                }
                        );

                        JOptionPane.showMessageDialog(
                                dialog,
                                "Customer added successfully.",
                                "Customer Added",
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

        JLabel labelComponent =
                new JLabel(label);

        labelComponent.setFont(
                AppTheme.SMALL_FONT
        );

        labelComponent.setForeground(
                AppTheme.TEXT_MUTED
        );

        panel.add(
                labelComponent,
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
    // VIEW CUSTOMER
    // ============================================================

    private void viewSelectedCustomer() {

        int row =
                customerTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a customer first.",
                    "Select Customer",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        StringBuilder details =
                new StringBuilder();

        details.append(
                "Customer Details\n\n"
        );

        for (
                int column = 0;
                column <
                        customerTableModel
                                .getColumnCount();
                column++
        ) {

            details.append(
                    customerTableModel
                            .getColumnName(column)
            );

            details.append(": ");

            details.append(
                    customerTableModel
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
                "Customer Details",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ============================================================
    // EDIT CUSTOMER
    // ============================================================

    private void editSelectedCustomer() {

        int row =
                customerTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a customer first.",
                    "Select Customer",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        showCustomerDialog(row);
    }

    // ============================================================
    // DISABLE CUSTOMER
    // ============================================================

    private void disableSelectedCustomer() {

        int row =
                customerTable.getSelectedRow();

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a customer first.",
                    "Select Customer",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String currentStatus =
                String.valueOf(
                        customerTableModel
                                .getValueAt(
                                        row,
                                        6
                                )
                );

        String customerName =
                String.valueOf(
                        customerTableModel
                                .getValueAt(
                                        row,
                                        1
                                )
                );

        if (currentStatus.equals("Inactive")) {

            JOptionPane.showMessageDialog(
                    this,
                    "This customer is already inactive.",
                    "Customer Status",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Disable customer \""
                                + customerName
                                + "\"?",
                        "Disable Customer",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (choice ==
                JOptionPane.YES_OPTION) {

            customerTableModel.setValueAt(
                    "Inactive",
                    row,
                    6
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Customer disabled successfully.",
                    "Customer Disabled",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    // ============================================================
    // TABLE CONFIGURATION
    // ============================================================

    private void configureCustomerTable() {

        customerTable.setFont(
                AppTheme.SMALL_FONT
        );

        customerTable.setForeground(
                AppTheme.TEXT_PRIMARY
        );

        customerTable.setBackground(
                AppTheme.SURFACE
        );

        customerTable.setGridColor(
                AppTheme.BORDER
        );

        customerTable.setRowHeight(40);

        customerTable.setShowVerticalLines(false);

        customerTable.setShowHorizontalLines(true);

        customerTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        customerTable.getTableHeader()
                .setFont(
                        AppTheme.SMALL_FONT
                );

        customerTable.getTableHeader()
                .setBackground(
                        AppTheme.BACKGROUND
                );

        customerTable.getTableHeader()
                .setForeground(
                        AppTheme.TEXT_MUTED
                );

        customerTable.getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                40
                        )
                );

        customerTable.setDefaultRenderer(
                Object.class,
                new CustomerTableRenderer()
        );
    }

    // ============================================================
    // UI HELPERS
    // ============================================================

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

        button.setForeground(Color.WHITE);

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

    private class CustomerTableRenderer
            extends DefaultTableCellRenderer {

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

            if (
                    column == 6 &&
                            value != null
            ) {

                setHorizontalAlignment(
                        SwingConstants.CENTER
                );

                if (
                        value.toString()
                                .equals("Active")
                ) {

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