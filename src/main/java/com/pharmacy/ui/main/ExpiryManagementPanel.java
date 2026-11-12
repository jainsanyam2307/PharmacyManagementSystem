package com.pharmacy.ui.main;

import com.pharmacy.config.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

/**
 * ExpiryManagementPanel
 *
 * UI-only expiry management module.
 * Uses sample in-memory data for now.
 * Database integration will be added later.
 */
public class ExpiryManagementPanel extends JPanel {

    private JTable expiryTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    private JTextField searchField;
    private JComboBox<String> categoryComboBox;
    private JComboBox<String> statusComboBox;

    private final Color background = AppTheme.BACKGROUND;
    private final Color surface = AppTheme.SURFACE;
    private final Color border = AppTheme.BORDER;
    private final Color textPrimary = AppTheme.TEXT_PRIMARY;
    private final Color textSecondary = AppTheme.TEXT_SECONDARY;
    private final Color textMuted = AppTheme.TEXT_MUTED;
    private final Color primary = AppTheme.PRIMARY;
    private final Color primaryLight = AppTheme.PRIMARY_LIGHT;
    private final Color primaryDark = AppTheme.PRIMARY_DARK;

    private final String[] columns = {
            "Medicine ID",
            "Medicine Name",
            "Category",
            "Batch No.",
            "Stock",
            "Supplier",
            "Purchase Date",
            "Expiry Date",
            "Days Left",
            "Status"
    };

    public ExpiryManagementPanel() {

        setLayout(new BorderLayout());
        setBackground(background);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildContent(), BorderLayout.CENTER);
    }

    // ============================================================
    // HEADER
    // ============================================================

    private JComponent buildHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(background);
        header.setBorder(
                new EmptyBorder(24, 28, 18, 28)
        );

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(
                new BoxLayout(titlePanel, BoxLayout.Y_AXIS)
        );
        titlePanel.setBackground(background);

        JLabel title = new JLabel("Expiry Management");
        title.setFont(AppTheme.HEADING_FONT);
        title.setForeground(textPrimary);

        JLabel subtitle = new JLabel(
                "Monitor expired and near-expiry medicines before they become a loss."
        );
        subtitle.setFont(AppTheme.BODY_FONT);
        subtitle.setForeground(textSecondary);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

        JButton disposalButton =
                createPrimaryButton("Disposal Management");

        disposalButton.addActionListener(
                e -> showDisposalDialog()
        );

        header.add(titlePanel, BorderLayout.WEST);
        header.add(disposalButton, BorderLayout.EAST);

        return header;
    }

    // ============================================================
    // CONTENT
    // ============================================================

    private JComponent buildContent() {

        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setBackground(background);
        content.setBorder(
                new EmptyBorder(0, 28, 28, 28)
        );

        content.add(
                buildSummaryCards(),
                BorderLayout.NORTH
        );

        JPanel center = new JPanel(new BorderLayout(0, 14));
        center.setBackground(background);

        center.add(
                buildToolbar(),
                BorderLayout.NORTH
        );

        center.add(
                buildTablePanel(),
                BorderLayout.CENTER
        );

        content.add(center, BorderLayout.CENTER);

        return content;
    }

    // ============================================================
    // SUMMARY CARDS
    // ============================================================

    private JComponent buildSummaryCards() {

        JPanel cards =
                new JPanel(new GridLayout(1, 4, 14, 0));

        cards.setBackground(background);
        cards.setPreferredSize(
                new Dimension(0, 100)
        );

        cards.add(
                createSummaryCard(
                        "Expired",
                        "7",
                        "Requires immediate action",
                        AppTheme.DANGER
                )
        );

        cards.add(
                createSummaryCard(
                        "Within 30 Days",
                        "5",
                        "Urgent attention required",
                        AppTheme.DANGER
                )
        );

        cards.add(
                createSummaryCard(
                        "Within 90 Days",
                        "13",
                        "Plan stock clearance",
                        AppTheme.WARNING
                )
        );

        cards.add(
                createSummaryCard(
                        "Total At Risk",
                        "25",
                        "Batches requiring attention",
                        primary
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

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(surface);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(border),
                        new EmptyBorder(
                                14, 16, 14, 16
                        )
                )
        );

        JPanel accentPanel = new JPanel();
        accentPanel.setBackground(accent);
        accentPanel.setPreferredSize(
                new Dimension(4, 0)
        );

        JPanel text = new JPanel();
        text.setLayout(
                new BoxLayout(text, BoxLayout.Y_AXIS)
        );
        text.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(AppTheme.SMALL_FONT);
        titleLabel.setForeground(textSecondary);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(AppTheme.HEADING_FONT);
        valueLabel.setForeground(textPrimary);

        JLabel descriptionLabel =
                new JLabel(description);

        descriptionLabel.setFont(
                AppTheme.SMALL_FONT
        );

        descriptionLabel.setForeground(textMuted);

        text.add(titleLabel);
        text.add(Box.createVerticalStrut(4));
        text.add(valueLabel);
        text.add(Box.createVerticalStrut(2));
        text.add(descriptionLabel);

        card.add(accentPanel, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);

        return card;
    }

    // ============================================================
    // TOOLBAR
    // ============================================================

    private JComponent buildToolbar() {

        JPanel toolbar =
                new JPanel(new BorderLayout(12, 0));

        toolbar.setBackground(surface);

        toolbar.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(border),
                        new EmptyBorder(
                                12, 14, 12, 14
                        )
                )
        );

        JPanel searchPanel =
                new JPanel(new BorderLayout(8, 0));

        searchPanel.setBackground(surface);

        JLabel searchLabel =
                new JLabel("Search");

        searchLabel.setFont(AppTheme.SMALL_FONT);
        searchLabel.setForeground(textMuted);

        searchField = new JTextField();

        searchField.setFont(AppTheme.BODY_FONT);
        searchField.setForeground(textPrimary);
        searchField.setBackground(background);

        searchField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(border),
                        new EmptyBorder(
                                7, 10, 7, 10
                        )
                )
        );

        searchField.setToolTipText(
                "Search medicine, batch or supplier"
        );

        searchField.getDocument()
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
                                "Expired",
                                "Critical",
                                "Expiring Soon",
                                "Safe"
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

        filters.setBackground(surface);

        JLabel categoryLabel =
                new JLabel("Category");

        JLabel statusLabel =
                new JLabel("Status");

        categoryLabel.setFont(
                AppTheme.SMALL_FONT
        );

        statusLabel.setFont(
                AppTheme.SMALL_FONT
        );

        filters.add(categoryLabel);
        filters.add(categoryComboBox);
        filters.add(statusLabel);
        filters.add(statusComboBox);

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
                new JPanel(new BorderLayout());

        panel.setBackground(surface);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(border),
                        new EmptyBorder(
                                10, 10, 10, 10
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

        loadSampleData();

        expiryTable =
                new JTable(tableModel);

        configureTable();

        sorter =
                new TableRowSorter<>(
                        tableModel
                );

        expiryTable.setRowSorter(sorter);

        expiryTable.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent e
                    ) {

                        if (e.getClickCount() == 2) {
                            showMedicineDetails();
                        }
                    }
                }
        );

        JScrollPane scrollPane =
                new JScrollPane(expiryTable);

        scrollPane.setBorder(null);

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);

        panel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        return panel;
    }

    private void configureTable() {

        expiryTable.setFont(
                AppTheme.SMALL_FONT
        );

        expiryTable.setForeground(
                textPrimary
        );

        expiryTable.setBackground(
                surface
        );

        expiryTable.setGridColor(
                border
        );

        expiryTable.setRowHeight(40);

        expiryTable.setShowVerticalLines(false);

        expiryTable.setShowHorizontalLines(true);

        expiryTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        expiryTable.getTableHeader()
                .setFont(
                        AppTheme.SMALL_FONT
                );

        expiryTable.getTableHeader()
                .setBackground(background);

        expiryTable.getTableHeader()
                .setForeground(textSecondary);

        expiryTable.getTableHeader()
                .setPreferredSize(
                        new Dimension(0, 40)
                );

        expiryTable.setDefaultRenderer(
                Object.class,
                new ExpiryTableRenderer()
        );
    }

    // ============================================================
    // SAMPLE DATA
    // ============================================================

    private void loadSampleData() {

        Object[][] data = {

                {
                        "MED021",
                        "Azithromycin 500mg",
                        "Antibiotic",
                        "AZT24031",
                        8,
                        "MediSource Pvt Ltd",
                        "12 Jan 2026",
                        "20 Aug 2026",
                        12,
                        "Critical"
                },

                {
                        "MED022",
                        "Pantoprazole 40mg",
                        "Antacid",
                        "PNT24018",
                        76,
                        "HealthCare Distributors",
                        "15 Feb 2026",
                        "22 Aug 2026",
                        14,
                        "Critical"
                },

                {
                        "MED023",
                        "Insulin Glargine",
                        "Antidiabetic",
                        "INS24031",
                        14,
                        "MedLife Suppliers",
                        "20 Mar 2026",
                        "15 Aug 2026",
                        7,
                        "Critical"
                },

                {
                        "MED024",
                        "Cetirizine 10mg",
                        "Antihistamine",
                        "CTZ24014",
                        5,
                        "PharmaLink",
                        "05 Apr 2026",
                        "18 Sep 2026",
                        41,
                        "Expiring Soon"
                },

                {
                        "MED025",
                        "Diclofenac 50mg",
                        "Analgesic",
                        "DCF24027",
                        32,
                        "MediSource Pvt Ltd",
                        "12 Apr 2026",
                        "18 Oct 2026",
                        71,
                        "Expiring Soon"
                },

                {
                        "MED026",
                        "Amoxicillin 500mg",
                        "Antibiotic",
                        "AMX24027",
                        10,
                        "HealthCare Distributors",
                        "10 May 2026",
                        "12 Nov 2026",
                        96,
                        "Expiring Soon"
                },

                {
                        "MED027",
                        "Amlodipine 5mg",
                        "Cardiovascular",
                        "AML24041",
                        0,
                        "MedLife Suppliers",
                        "18 Jan 2026",
                        "14 Jun 2026",
                        -55,
                        "Expired"
                },

                {
                        "MED028",
                        "Vitamin D3 60000 IU",
                        "Vitamin & Supplement",
                        "VD324067",
                        64,
                        "Wellness Pharma",
                        "10 Jun 2026",
                        "25 Mar 2027",
                        229,
                        "Safe"
                },

                {
                        "MED029",
                        "Metformin 500mg",
                        "Antidiabetic",
                        "MET24055",
                        95,
                        "MedLife Suppliers",
                        "22 May 2026",
                        "03 Sep 2027",
                        391,
                        "Safe"
                },

                {
                        "MED030",
                        "Atorvastatin 10mg",
                        "Cardiovascular",
                        "ATR24039",
                        48,
                        "PharmaLink",
                        "02 Jun 2026",
                        "17 Jul 2027",
                        343,
                        "Safe"
                }
        };

        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }

    // ============================================================
    // FILTERING
    // ============================================================

    private void applyFilters() {

        if (sorter == null) {
            return;
        }

        String search =
                searchField.getText()
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

                            for (int i = 0;
                                 i < entry.getValueCount();
                                 i++) {

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
                                                entry.getValue(9)
                                        ).equals(status);

                        return searchMatch
                                && categoryMatch
                                && statusMatch;
                    }
                };

        sorter.setRowFilter(filter);
    }

    // ============================================================
    // DETAILS
    // ============================================================

    private void showMedicineDetails() {

        int selectedRow =
                expiryTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        int modelRow =
                expiryTable.convertRowIndexToModel(
                        selectedRow
                );

        StringBuilder details =
                new StringBuilder();

        details.append("Medicine: ")
                .append(tableModel.getValueAt(modelRow, 1))
                .append("\n\n");

        details.append("Medicine ID: ")
                .append(tableModel.getValueAt(modelRow, 0))
                .append("\n");

        details.append("Category: ")
                .append(tableModel.getValueAt(modelRow, 2))
                .append("\n");

        details.append("Batch Number: ")
                .append(tableModel.getValueAt(modelRow, 3))
                .append("\n");

        details.append("Current Stock: ")
                .append(tableModel.getValueAt(modelRow, 4))
                .append("\n");

        details.append("Supplier: ")
                .append(tableModel.getValueAt(modelRow, 5))
                .append("\n");

        details.append("Purchase Date: ")
                .append(tableModel.getValueAt(modelRow, 6))
                .append("\n");

        details.append("Expiry Date: ")
                .append(tableModel.getValueAt(modelRow, 7))
                .append("\n");

        details.append("Days Left: ")
                .append(tableModel.getValueAt(modelRow, 8))
                .append("\n");

        details.append("Status: ")
                .append(tableModel.getValueAt(modelRow, 9));

        JOptionPane.showMessageDialog(
                this,
                details.toString(),
                "Medicine Expiry Details",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ============================================================
    // DISPOSAL
    // ============================================================

    private void showDisposalDialog() {

        int selectedRow =
                expiryTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a medicine batch first.",
                    "Select Batch",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int modelRow =
                expiryTable.convertRowIndexToModel(
                        selectedRow
                );

        String medicine =
                String.valueOf(
                        tableModel.getValueAt(
                                modelRow,
                                1
                        )
                );

        String status =
                String.valueOf(
                        tableModel.getValueAt(
                                modelRow,
                                9
                        )
                );

        if (!status.equals("Expired")) {

            int confirm =
                    JOptionPane.showConfirmDialog(
                            this,
                            "This batch is not expired yet.\n"
                                    + "Do you still want to mark it for disposal?",
                            "Confirm Disposal",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        }

        JOptionPane.showMessageDialog(
                this,
                medicine
                        + " has been marked for disposal.\n\n"
                        + "Actual inventory removal will be connected "
                        + "to the database later.",
                "Marked for Disposal",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ============================================================
    // BUTTONS
    // ============================================================

    private JButton createPrimaryButton(
            String text
    ) {

        JButton button =
                new JButton(text);

        button.setFont(
                AppTheme.BUTTON_FONT
        );

        button.setForeground(Color.WHITE);
        button.setBackground(primary);

        button.setFocusPainted(false);
        button.setBorder(
                new EmptyBorder(
                        10, 16, 10, 16
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

    private class ExpiryTableRenderer
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
                            0, 7, 0, 7
                    )
            );

            if (selected) {

                setBackground(primaryLight);
                setForeground(primaryDark);

            } else {

                setBackground(surface);
                setForeground(textPrimary);
            }

            if (column == 4 ||
                    column == 8 ||
                    column == 9) {

                setHorizontalAlignment(
                        SwingConstants.CENTER
                );

            } else {

                setHorizontalAlignment(
                        SwingConstants.LEFT
                );
            }

            if (column == 9 && value != null) {

                String status =
                        value.toString();

                setHorizontalAlignment(
                        SwingConstants.CENTER
                );

                if (status.equals("Expired")) {

                    setForeground(
                            AppTheme.DANGER
                    );

                } else if (
                        status.equals("Critical")
                ) {

                    setForeground(
                            AppTheme.DANGER
                    );

                } else if (
                        status.equals("Expiring Soon")
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