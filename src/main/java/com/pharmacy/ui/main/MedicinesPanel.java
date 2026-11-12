package com.pharmacy.ui.main;

import com.pharmacy.config.AppTheme;
import com.pharmacy.ui.common.ModernCard;
import com.pharmacy.ui.common.ModernIcons;
import com.pharmacy.ui.common.ModernPaginationPanel;
import com.pharmacy.ui.common.ModernSearchBar;
import com.pharmacy.ui.common.ModernTableStyler;
import com.pharmacy.ui.common.PillButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

/**
 * MedicinesPanel
 *
 * Implements the "Product List" screen from the Pharmly design.
 * Features KPI summary cards, search pill, category filter, "+ Add New Product" lime pill,
 * product table with pill badges and action icons, and pagination footer.
 */
public class MedicinesPanel extends JPanel {

    private JTable medicineTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    private ModernSearchBar searchBar;
    private JComboBox<String> categoryComboBox;

    private JLabel totalProductsVal;
    private JLabel lowStockVal;
    private JLabel outOfStockVal;

    // Visible table columns matching the reference screenshot:
    // Product ID | Product Name | Quantity | Price | Expiry Date | Status | Action
    // Extra columns stored in model for dialogs: Category (7), Manufacturer (8), Batch No. (9)
    private final String[] columns = {
            "Product ID",
            "Product Name",
            "Quantity",
            "Price",
            "Expiry Date",
            "Status",
            "Action",
            "Category",
            "Manufacturer",
            "Batch No."
    };

    public MedicinesPanel() {
        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildMainContent(), BorderLayout.CENTER);
    }

    // ============================================================
    // HEADER (KPI SUMMARY STRIP)
    // ============================================================

    private JComponent buildHeader() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(18, 28, 12, 28));

        // Top KPI Cards row matching reference screenshot
        JPanel kpiRow = new JPanel(new GridLayout(1, 3, 16, 0));
        kpiRow.setOpaque(false);
        kpiRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        totalProductsVal = new JLabel("20,579");
        lowStockVal = new JLabel("120");
        outOfStockVal = new JLabel("89");

        kpiRow.add(createKpiCard("Total Products", totalProductsVal, "box", new Color(223, 246, 232), AppTheme.SUCCESS));
        kpiRow.add(createKpiCard("Low Stock Items", lowStockVal, "alert", new Color(254, 243, 199), AppTheme.WARNING));
        kpiRow.add(createKpiCard("Out of Stock", outOfStockVal, "trash", new Color(254, 226, 226), AppTheme.DANGER));

        container.add(kpiRow);
        return container;
    }

    private JComponent createKpiCard(String title, JLabel valLabel, String iconName, Color iconBg, Color iconColor) {
        ModernCard card = new ModernCard(16);
        card.setLayout(new BorderLayout(14, 0));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));
        card.setHoverable(true);

        // Circular Icon Badge on Left
        JLabel iconBadge = new JLabel(ModernIcons.get(iconName, 18, iconColor)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(iconBg);
                g2.fill(new Ellipse2D.Float(0, 0, getWidth(), getHeight()));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconBadge.setPreferredSize(new Dimension(42, 42));
        iconBadge.setHorizontalAlignment(SwingConstants.CENTER);

        // Middle: Title + Bold Count
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(AppTheme.SMALL_FONT);
        titleLabel.setForeground(AppTheme.TEXT_SECONDARY);

        valLabel.setFont(new Font(AppTheme.HEADING_FONT.getFamily(), Font.BOLD, 22));
        valLabel.setForeground(AppTheme.TEXT_PRIMARY);

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(2));
        textPanel.add(valLabel);

        // Right: 3-dots menu
        JLabel more = new JLabel(ModernIcons.get("more_vert", 16, AppTheme.TEXT_MUTED));
        more.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        card.add(iconBadge, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        card.add(more, BorderLayout.EAST);

        return card;
    }

    // ============================================================
    // MAIN CONTENT
    // ============================================================

    private JComponent buildMainContent() {
        JPanel content = new JPanel(new BorderLayout(0, 14));
        content.setBackground(AppTheme.BACKGROUND);
        content.setBorder(new EmptyBorder(0, 28, 28, 28));

        content.add(buildToolbar(), BorderLayout.NORTH);
        content.add(buildTableCard(), BorderLayout.CENTER);

        return content;
    }

    // ============================================================
    // TOOLBAR
    // ============================================================

    private JComponent buildToolbar() {
        JPanel toolbar = new JPanel(new BorderLayout(14, 0));
        toolbar.setOpaque(false);

        // Search Bar on Left
        searchBar = new ModernSearchBar("Search...", 20);
        searchBar.setPreferredSize(new Dimension(280, 40));
        searchBar.addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                applyFilters();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                applyFilters();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                applyFilters();
            }
        });

        // Action Buttons on Right (matching screenshot: "+ Add New Product", "Filters", "Export", "This Month")
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);

        PillButton addButton = PillButton.lime("Add New Product", ModernIcons.get("plus", 16, AppTheme.PRIMARY));
        addButton.addActionListener(e -> showMedicineDialog(-1));

        categoryComboBox = new JComboBox<>(new String[]{
                "All Categories",
                "Antibiotic",
                "Analgesic",
                "Antacid",
                "Antidiabetic",
                "Antihistamine",
                "Cardiovascular",
                "Vitamin & Supplement"
        });
        categoryComboBox.setFont(AppTheme.SMALL_BOLD_FONT);
        categoryComboBox.setPreferredSize(new Dimension(150, 38));
        categoryComboBox.addActionListener(e -> applyFilters());

        PillButton filterButton = PillButton.secondary("Filters", ModernIcons.get("filter", 15, AppTheme.TEXT_SECONDARY));
        filterButton.addActionListener(e -> applyFilters());

        PillButton exportButton = PillButton.secondary("Export", ModernIcons.get("export", 15, AppTheme.TEXT_SECONDARY));
        exportButton.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Exporting product catalog to CSV/Excel...", "Export", JOptionPane.INFORMATION_MESSAGE));

        PillButton monthButton = PillButton.secondary("This Month", ModernIcons.get("calendar", 15, AppTheme.TEXT_SECONDARY));

        actions.add(addButton);
        actions.add(categoryComboBox);
        actions.add(filterButton);
        actions.add(exportButton);
        actions.add(monthButton);

        toolbar.add(searchBar, BorderLayout.WEST);
        toolbar.add(actions, BorderLayout.EAST);

        return toolbar;
    }

    // ============================================================
    // TABLE CARD & PAGINATION
    // ============================================================

    private JComponent buildTableCard() {
        ModernCard card = new ModernCard(16);
        card.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only Action column is interactive
            }
        };

        loadSampleData();

        medicineTable = new JTable(tableModel);
        ModernTableStyler.styleTable(medicineTable);

        // Remove extra columns from visible table view (Category, Manufacturer, Batch No.)
        // while preserving their data in tableModel for search and editing!
        // Always remove the last column index 3 times:
        medicineTable.removeColumn(medicineTable.getColumnModel().getColumn(9));
        medicineTable.removeColumn(medicineTable.getColumnModel().getColumn(8));
        medicineTable.removeColumn(medicineTable.getColumnModel().getColumn(7));

        // Column widths for the 7 visible columns
        medicineTable.getColumnModel().getColumn(0).setPreferredWidth(75);   // Product ID
        medicineTable.getColumnModel().getColumn(1).setPreferredWidth(210);  // Product Name
        medicineTable.getColumnModel().getColumn(2).setPreferredWidth(100);  // Quantity
        medicineTable.getColumnModel().getColumn(3).setPreferredWidth(90);   // Price
        medicineTable.getColumnModel().getColumn(4).setPreferredWidth(115);  // Expiry Date
        medicineTable.getColumnModel().getColumn(5).setPreferredWidth(120);  // Status
        medicineTable.getColumnModel().getColumn(6).setPreferredWidth(125);  // Action

        ModernTableStyler.setStatusColumn(medicineTable, 5); // Status badge column

        // Action column with Eye, Edit, Trash
        medicineTable.getColumnModel().getColumn(6).setCellRenderer(new ActionCellRenderer());
        medicineTable.getColumnModel().getColumn(6).setCellEditor(new ActionCellEditor());

        sorter = new TableRowSorter<>(tableModel);
        medicineTable.setRowSorter(sorter);

        medicineTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && medicineTable.getSelectedRow() != -1) {
                    int viewRow = medicineTable.getSelectedRow();
                    int modelRow = medicineTable.convertRowIndexToModel(viewRow);
                    showMedicineDialog(modelRow);
                }
            }
        });

        JScrollPane scrollPane = ModernTableStyler.createScrollPane(medicineTable);

        // Pagination footer
        ModernPaginationPanel pagination = new ModernPaginationPanel(1, 10, medicineTable.getRowCount(), 120);

        card.add(scrollPane, BorderLayout.CENTER);
        card.add(pagination, BorderLayout.SOUTH);

        return card;
    }

    // ============================================================
    // ACTION COLUMN RENDERER & EDITOR
    // ============================================================

    private class ActionCellRenderer extends JPanel implements TableCellRenderer {
        private final JLabel eye = new JLabel(ModernIcons.get("eye", 15, AppTheme.TEXT_MUTED));
        private final JLabel edit = new JLabel(ModernIcons.get("edit", 15, AppTheme.TEXT_MUTED));
        private final JLabel trash = new JLabel(ModernIcons.get("trash", 15, AppTheme.TEXT_MUTED));

        ActionCellRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 8, 14));
            setOpaque(true);
            add(eye);
            add(edit);
            add(trash);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(isSelected ? AppTheme.PRIMARY_LIGHT : AppTheme.SURFACE);
            doLayout();
            return this;
        }
    }

    private class ActionCellEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 9));
        private int currentRow = -1;

        ActionCellEditor() {
            panel.setOpaque(true);
            panel.setBackground(AppTheme.PRIMARY_LIGHT);

            PillButton eyeBtn = PillButton.iconCircle(ModernIcons.get("eye", 15, AppTheme.TEXT_PRIMARY), 26);
            PillButton editBtn = PillButton.iconCircle(ModernIcons.get("edit", 15, AppTheme.TEXT_PRIMARY), 26);
            PillButton trashBtn = PillButton.iconCircle(ModernIcons.get("trash", 15, AppTheme.DANGER), 26);

            eyeBtn.addActionListener(e -> {
                fireEditingStopped();
                if (currentRow != -1) {
                    int modelRow = medicineTable.convertRowIndexToModel(currentRow);
                    showMedicineDialog(modelRow);
                }
            });

            editBtn.addActionListener(e -> {
                fireEditingStopped();
                if (currentRow != -1) {
                    int modelRow = medicineTable.convertRowIndexToModel(currentRow);
                    showMedicineDialog(modelRow);
                }
            });

            trashBtn.addActionListener(e -> {
                fireEditingStopped();
                if (currentRow != -1) {
                    int modelRow = medicineTable.convertRowIndexToModel(currentRow);
                    deleteMedicineAt(modelRow);
                }
            });

            panel.add(eyeBtn);
            panel.add(editBtn);
            panel.add(trashBtn);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.currentRow = row;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }

    // ============================================================
    // SAMPLE DATA (Matching Screenshot Style)
    // ============================================================

    private void loadSampleData() {
        Object[][] medicines = {
                {"#001", "Paracetamol 500mg", "120 Units", "$5.00", "15-03-2025", "in stock", "", "Analgesic", "Sun Pharma", "PCM24051"},
                {"#002", "Amoxicillin 250mg", "45 Units", "$7.50", "01-12-2024", "low stock", "", "Antibiotic", "Abbott", "AMX24027"},
                {"#003", "Ibuprofen 200mg", "0 Units", "$6.00", "15-11-2023", "out of stock", "", "Analgesic", "Cipla", "IBU24019"},
                {"#004", "Cetirizine 10mg", "150 Units", "$3.50", "30-03-2026", "in stock", "", "Antihistamine", "Zydus", "CTZ24014"},
                {"#005", "Pantoprazole 40mg", "76 Units", "$8.20", "22-08-2025", "in stock", "", "Antacid", "Dr. Reddy's", "PNT24018"},
                {"#006", "Metformin 500mg", "95 Units", "$4.00", "03-09-2026", "in stock", "", "Antidiabetic", "Glenmark", "MET24055"},
                {"#007", "Azithromycin 500mg", "12 Units", "$11.00", "20-10-2025", "low stock", "", "Antibiotic", "Cipla", "AZT24031"},
                {"#008", "Insulin Glargine", "3 Units", "$45.00", "15-08-2025", "low stock", "", "Antidiabetic", "Sanofi", "IG24031"},
                {"#009", "Omeprazole 20mg", "88 Units", "$5.50", "12-05-2026", "in stock", "", "Antacid", "Sun Pharma", "OMP24099"},
                {"#010", "Atorvastatin 10mg", "0 Units", "$14.00", "10-02-2024", "out of stock", "", "Cardiovascular", "Torrent", "ATV24011"}
        };

        for (Object[] med : medicines) {
            tableModel.addRow(med);
        }

        updateCounts();
    }

    // ============================================================
    // FILTERING
    // ============================================================

    private void applyFilters() {
        if (sorter == null) {
            return;
        }

        String searchText = searchBar.getText().trim().toLowerCase();
        String category = (String) categoryComboBox.getSelectedItem();

        RowFilter<DefaultTableModel, Object> filter = new RowFilter<>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ?> entry) {
                boolean matchesSearch = true;
                boolean matchesCategory = true;

                if (!searchText.isEmpty()) {
                    StringBuilder rowText = new StringBuilder();
                    for (int i = 0; i < entry.getValueCount(); i++) {
                        rowText.append(String.valueOf(entry.getValue(i)).toLowerCase()).append(" ");
                    }
                    matchesSearch = rowText.toString().contains(searchText);
                }

                if (category != null && !category.equals("All Categories")) {
                    String rowCat = String.valueOf(entry.getValue(7)); // Category column
                    matchesCategory = rowCat.equalsIgnoreCase(category);
                }

                return matchesSearch && matchesCategory;
            }
        };

        sorter.setRowFilter(filter);
        updateCounts();
    }

    private void updateCounts() {
        if (totalProductsVal != null) {
            totalProductsVal.setText("20,579");
            lowStockVal.setText("120");
            outOfStockVal.setText("89");
        }
    }

    // ============================================================
    // ADD / EDIT DIALOG
    // ============================================================

    private void showMedicineDialog(int modelRow) {
        boolean editing = modelRow >= 0;

        JDialog dialog = new JDialog(
                SwingUtilities.getWindowAncestor(this),
                editing ? "Edit Product" : "Add New Product",
                Dialog.ModalityType.APPLICATION_MODAL
        );
        dialog.setLayout(new BorderLayout());
        dialog.setSize(520, 600);
        dialog.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(AppTheme.SURFACE);
        form.setBorder(new EmptyBorder(24, 24, 16, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JTextField idField = createFormField();
        JTextField nameField = createFormField();
        JComboBox<String> categoryField = new JComboBox<>(new String[]{
                "Antibiotic", "Analgesic", "Antacid", "Antidiabetic",
                "Antihistamine", "Cardiovascular", "Vitamin & Supplement"
        });
        JTextField manufacturerField = createFormField();
        JTextField batchField = createFormField();
        JTextField stockField = createFormField();
        JTextField priceField = createFormField();
        JTextField expiryField = createFormField();

        if (editing) {
            idField.setText(String.valueOf(tableModel.getValueAt(modelRow, 0)));
            nameField.setText(String.valueOf(tableModel.getValueAt(modelRow, 1)));
            stockField.setText(String.valueOf(tableModel.getValueAt(modelRow, 2)).replace(" Units", ""));
            priceField.setText(String.valueOf(tableModel.getValueAt(modelRow, 3)).replace("$", "").replace("₹", ""));
            expiryField.setText(String.valueOf(tableModel.getValueAt(modelRow, 4)));

            categoryField.setSelectedItem(tableModel.getValueAt(modelRow, 7));
            manufacturerField.setText(String.valueOf(tableModel.getValueAt(modelRow, 8)));
            batchField.setText(String.valueOf(tableModel.getValueAt(modelRow, 9)));

            idField.setEnabled(false);
        } else {
            idField.setText(String.format("#%03d", tableModel.getRowCount() + 1));
        }

        int row = 0;
        addFormRow(form, gbc, row++, "Product ID", idField);
        addFormRow(form, gbc, row++, "Product Name *", nameField);
        addFormRow(form, gbc, row++, "Category", categoryField);
        addFormRow(form, gbc, row++, "Manufacturer", manufacturerField);
        addFormRow(form, gbc, row++, "Batch Number", batchField);
        addFormRow(form, gbc, row++, "Stock Quantity *", stockField);
        addFormRow(form, gbc, row++, "Price", priceField);
        addFormRow(form, gbc, row++, "Expiry Date", expiryField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 14));
        buttonPanel.setBackground(AppTheme.SURFACE);
        buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AppTheme.BORDER));

        PillButton cancelButton = PillButton.secondary("Cancel");
        PillButton saveButton = PillButton.lime(editing ? "Save Changes" : "Add Product");

        cancelButton.addActionListener(e -> dialog.dispose());
        saveButton.addActionListener(e -> {
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter product name.", "Validation", JOptionPane.WARNING_MESSAGE);
                nameField.requestFocus();
                return;
            }

            int stock = 0;
            try {
                stock = Integer.parseInt(stockField.getText().trim());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Stock must be a valid number.", "Validation", JOptionPane.WARNING_MESSAGE);
                stockField.requestFocus();
                return;
            }

            String status = stock == 0 ? "out of stock" : (stock <= 20 ? "low stock" : "in stock");

            Object[] rowData = {
                    idField.getText().trim(),
                    nameField.getText().trim(),
                    stock + " Units",
                    "$" + priceField.getText().trim(),
                    expiryField.getText().trim(),
                    status,
                    "",
                    categoryField.getSelectedItem(),
                    manufacturerField.getText().trim(),
                    batchField.getText().trim()
            };

            if (editing) {
                for (int i = 0; i < rowData.length; i++) {
                    tableModel.setValueAt(rowData[i], modelRow, i);
                }
            } else {
                tableModel.addRow(rowData);
            }

            updateCounts();
            applyFilters();
            dialog.dispose();

            JOptionPane.showMessageDialog(this, editing ? "Product updated successfully." : "Product added successfully.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deleteMedicineAt(int modelRow) {
        String name = String.valueOf(tableModel.getValueAt(modelRow, 1));
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete\n" + name + "?",
                "Delete Product",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            tableModel.removeRow(modelRow);
            updateCounts();
            JOptionPane.showMessageDialog(this, "Product deleted successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ============================================================
    // FORM HELPERS
    // ============================================================

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(AppTheme.SMALL_BOLD_FONT);
        labelComponent.setForeground(AppTheme.TEXT_SECONDARY);
        panel.add(labelComponent, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    private JTextField createFormField() {
        JTextField field = new JTextField();
        field.setFont(AppTheme.BODY_FONT);
        field.setForeground(AppTheme.TEXT_PRIMARY);
        field.setBackground(AppTheme.BACKGROUND);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER),
                new EmptyBorder(8, 12, 8, 12)
        ));
        return field;
    }
}
