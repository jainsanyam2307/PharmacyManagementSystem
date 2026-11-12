package com.pharmacy.ui.main;

import com.pharmacy.config.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * ExpensesPanel
 *
 * Allows pharmacy staff to track, add, edit, and delete expenses.
 * UI-only. Uses in-memory sample data. No database, DAO, service,
 * or JDBC code is used in this phase.
 */
public class ExpensesPanel extends JPanel {

    private static final String[] CATEGORIES = {
            "Electricity", "Rent", "Staff Salary", "Internet",
            "Maintenance", "Transportation", "Packaging", "Miscellaneous"
    };

    private static final String[] PAYMENT_STATUSES = {"Paid", "Pending"};

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    // Simple in-memory model for an expense record.
    private static class Expense {
        String id;
        LocalDate date;
        String category;
        String description;
        double amount;
        String paymentStatus;
        String notes;

        Expense(String id, LocalDate date, String category, String description,
                double amount, String paymentStatus, String notes) {
            this.id = id;
            this.date = date;
            this.category = category;
            this.description = description;
            this.amount = amount;
            this.paymentStatus = paymentStatus;
            this.notes = notes;
        }
    }

    private final List<Expense> allExpenses = new ArrayList<>();
    private int nextExpenseNumber = 1;

    private DefaultTableModel tableModel;
    private JTable expenseTable;
    private JTextField searchField;

    private JLabel todayExpensesValue;
    private JLabel monthExpensesValue;
    private JLabel pendingExpensesValue;
    private JLabel totalExpensesValue;

    public ExpensesPanel() {
        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);

        loadSampleData();

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenterContent(), BorderLayout.CENTER);

        refreshTable();
        refreshSummary();
    }

    // =====================================================================
    // HEADER
    // =====================================================================

    private JComponent buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppTheme.SURFACE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER),
                new EmptyBorder(18, 24, 18, 24)
        ));

        JPanel titleArea = new JPanel();
        titleArea.setLayout(new BoxLayout(titleArea, BoxLayout.Y_AXIS));
        titleArea.setOpaque(false);

        JLabel title = new JLabel("Expenses");
        title.setFont(AppTheme.HEADING_FONT);
        title.setForeground(AppTheme.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Track and manage pharmacy expenses");
        subtitle.setFont(AppTheme.SMALL_FONT);
        subtitle.setForeground(AppTheme.TEXT_MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleArea.add(title);
        titleArea.add(Box.createVerticalStrut(4));
        titleArea.add(subtitle);

        JButton addExpenseButton = createPrimaryButton("+ Add Expense");
        addExpenseButton.addActionListener(e -> openExpenseDialog(null));

        header.add(titleArea, BorderLayout.WEST);
        header.add(addExpenseButton, BorderLayout.EAST);

        return header;
    }

    // =====================================================================
    // CENTER CONTENT
    // =====================================================================

    private JComponent buildCenterContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(AppTheme.BACKGROUND);
        content.setBorder(new EmptyBorder(20, 24, 24, 24));

        content.add(buildSummaryCardsRow());
        content.add(Box.createVerticalStrut(20));
        content.add(buildSearchAndActionsRow());
        content.add(Box.createVerticalStrut(12));
        content.add(buildTableArea());

        return content;
    }

    // =====================================================================
    // SUMMARY CARDS
    // =====================================================================

    private JComponent buildSummaryCardsRow() {
        JPanel row = new JPanel(new GridLayout(1, 4, 16, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        todayExpensesValue = new JLabel();
        monthExpensesValue = new JLabel();
        pendingExpensesValue = new JLabel();
        totalExpensesValue = new JLabel();

        row.add(buildSummaryCard("Today's Expenses", todayExpensesValue, AppTheme.PRIMARY));
        row.add(buildSummaryCard("This Month", monthExpensesValue, AppTheme.PRIMARY_DARK));
        row.add(buildSummaryCard("Pending Expenses", pendingExpensesValue, AppTheme.WARNING));
        row.add(buildSummaryCard("Total Expenses", totalExpensesValue, AppTheme.SUCCESS));

        return row;
    }

    private JComponent buildSummaryCard(String label, JLabel valueLabel, Color accent) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(AppTheme.SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER),
                new EmptyBorder(16, 18, 16, 18)
        ));

        JPanel accentBar = new JPanel();
        accentBar.setBackground(accent);
        accentBar.setMaximumSize(new Dimension(36, 4));
        accentBar.setPreferredSize(new Dimension(36, 4));
        accentBar.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel(label);
        titleLabel.setFont(AppTheme.SMALL_FONT);
        titleLabel.setForeground(AppTheme.TEXT_MUTED);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        valueLabel.setFont(AppTheme.HEADING_FONT);
        valueLabel.setForeground(AppTheme.TEXT_PRIMARY);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(accentBar);
        card.add(Box.createVerticalStrut(10));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(valueLabel);

        return card;
    }

    // =====================================================================
    // SEARCH + ACTIONS
    // =====================================================================

    private JComponent buildSearchAndActionsRow() {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        searchField = new JTextField();
        searchField.setFont(AppTheme.BODY_FONT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER),
                new EmptyBorder(6, 10, 6, 10)
        ));
        searchField.putClientProperty("JTextField.placeholderText",
                "Search by Expense ID, Category, Description or Status");
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { refreshTable(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { refreshTable(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { refreshTable(); }
        });

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);

        JButton editButton = createSecondaryButton("Edit");
        editButton.addActionListener(e -> editSelectedExpense());

        JButton deleteButton = createSecondaryButton("Delete");
        deleteButton.addActionListener(e -> deleteSelectedExpense());

        actions.add(editButton);
        actions.add(deleteButton);

        row.add(searchField, BorderLayout.CENTER);
        row.add(actions, BorderLayout.EAST);

        JPanel wrapper = new JPanel(new BorderLayout(12, 0));
        wrapper.setOpaque(false);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        wrapper.add(row, BorderLayout.CENTER);

        return wrapper;
    }

    // =====================================================================
    // TABLE
    // =====================================================================

    private JComponent buildTableArea() {
        String[] columns = {"Expense ID", "Date", "Category", "Description", "Amount", "Payment Status", "Notes"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        expenseTable = new JTable(tableModel);
        styleTable(expenseTable);

        expenseTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showSelectedExpenseDetails();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(expenseTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));
        scrollPane.setPreferredSize(new Dimension(100, 360));
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);

        return scrollPane;
    }

    private void styleTable(JTable table) {
        table.setFont(AppTheme.BODY_FONT);
        table.setRowHeight(30);
        table.setForeground(AppTheme.TEXT_PRIMARY);
        table.setGridColor(AppTheme.BORDER);
        table.setSelectionBackground(AppTheme.PRIMARY_LIGHT);
        table.setSelectionForeground(AppTheme.TEXT_PRIMARY);
        table.setShowVerticalLines(false);
        table.setFillsViewportHeight(true);

        table.getTableHeader().setFont(AppTheme.SMALL_FONT);
        table.getTableHeader().setForeground(AppTheme.TEXT_MUTED);
        table.getTableHeader().setBackground(AppTheme.SURFACE);
        table.getTableHeader().setPreferredSize(new Dimension(100, 34));
    }

    // =====================================================================
    // SAMPLE DATA
    // =====================================================================

    private void loadSampleData() {
        LocalDate today = LocalDate.now();

        addExpenseInternal(today, "Rent", "Monthly shop rent", 25000, "Paid", "Paid via bank transfer");
        addExpenseInternal(today, "Electricity", "Electricity bill - store", 3200, "Pending", "Due end of month");
        addExpenseInternal(today.minusDays(1), "Staff Salary", "Salary - pharmacist assistant", 18000, "Paid", "");
        addExpenseInternal(today.minusDays(2), "Internet", "Broadband monthly plan", 1200, "Paid", "");
        addExpenseInternal(today.minusDays(3), "Maintenance", "AC servicing", 1500, "Paid", "Annual maintenance");
        addExpenseInternal(today.minusDays(5), "Transportation", "Medicine delivery fuel", 800, "Pending", "");
        addExpenseInternal(today.minusDays(6), "Packaging", "Bags and boxes restock", 2200, "Paid", "");
        addExpenseInternal(today.minusDays(8), "Miscellaneous", "Office stationery", 650, "Paid", "");
        addExpenseInternal(today.minusDays(10), "Staff Salary", "Salary - cashier", 15000, "Pending", "Delayed due to cashflow");
        addExpenseInternal(today.minusDays(12), "Electricity", "Backup generator fuel", 950, "Paid", "");
    }

    private void addExpenseInternal(LocalDate date, String category, String description,
                                    double amount, String paymentStatus, String notes) {
        String id = "EXP" + String.format("%04d", nextExpenseNumber++);
        allExpenses.add(new Expense(id, date, category, description, amount, paymentStatus, notes));
    }

    // =====================================================================
    // TABLE REFRESH / SEARCH
    // =====================================================================

    private void refreshTable() {
        String query = searchField == null ? "" : searchField.getText().trim().toLowerCase();

        tableModel.setRowCount(0);

        for (Expense expense : allExpenses) {
            if (!query.isEmpty() && !matchesSearch(expense, query)) {
                continue;
            }

            tableModel.addRow(new Object[]{
                    expense.id,
                    expense.date.format(DATE_FORMAT),
                    expense.category,
                    expense.description,
                    formatCurrency(expense.amount),
                    expense.paymentStatus,
                    expense.notes
            });
        }
    }

    private boolean matchesSearch(Expense expense, String query) {
        return expense.id.toLowerCase().contains(query)
                || expense.category.toLowerCase().contains(query)
                || expense.description.toLowerCase().contains(query)
                || expense.paymentStatus.toLowerCase().contains(query);
    }

    private void refreshSummary() {
        LocalDate today = LocalDate.now();
        LocalDate monthStart = today.withDayOfMonth(1);

        double todayTotal = 0;
        double monthTotal = 0;
        double pendingTotal = 0;
        double grandTotal = 0;

        for (Expense expense : allExpenses) {
            grandTotal += expense.amount;

            if (expense.date.equals(today)) {
                todayTotal += expense.amount;
            }
            if (!expense.date.isBefore(monthStart)) {
                monthTotal += expense.amount;
            }
            if ("Pending".equals(expense.paymentStatus)) {
                pendingTotal += expense.amount;
            }
        }

        todayExpensesValue.setText(formatCurrency(todayTotal));
        monthExpensesValue.setText(formatCurrency(monthTotal));
        pendingExpensesValue.setText(formatCurrency(pendingTotal));
        totalExpensesValue.setText(formatCurrency(grandTotal));
    }

    private String formatCurrency(double amount) {
        return String.format("₹%,.2f", amount);
    }

    // =====================================================================
    // SELECTION HELPERS
    // =====================================================================

    private Expense getSelectedExpense() {
        int viewRow = expenseTable.getSelectedRow();
        if (viewRow < 0) {
            return null;
        }
        String id = (String) tableModel.getValueAt(viewRow, 0);
        return findExpenseById(id);
    }

    private Expense findExpenseById(String id) {
        for (Expense expense : allExpenses) {
            if (expense.id.equals(id)) {
                return expense;
            }
        }
        return null;
    }

    private void editSelectedExpense() {
        Expense selected = getSelectedExpense();
        if (selected == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select an expense to edit.",
                    "No Expense Selected",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        openExpenseDialog(selected);
    }

    private void deleteSelectedExpense() {
        Expense selected = getSelectedExpense();
        if (selected == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select an expense to delete.",
                    "No Expense Selected",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete expense \"" + selected.description + "\"?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            allExpenses.remove(selected);
            refreshTable();
            refreshSummary();
        }
    }

    private void showSelectedExpenseDetails() {
        Expense selected = getSelectedExpense();
        if (selected == null) {
            return;
        }

        String details = "Expense ID: " + selected.id + "\n"
                + "Date: " + selected.date.format(DATE_FORMAT) + "\n"
                + "Category: " + selected.category + "\n"
                + "Description: " + selected.description + "\n"
                + "Amount: " + formatCurrency(selected.amount) + "\n"
                + "Payment Status: " + selected.paymentStatus + "\n"
                + "Notes: " + (selected.notes.isEmpty() ? "-" : selected.notes);

        JOptionPane.showMessageDialog(
                this,
                details,
                "Expense Details",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =====================================================================
    // ADD / EDIT DIALOG
    // =====================================================================

    /**
     * Opens the Add/Edit Expense dialog. If existingExpense is null, a new
     * expense is created; otherwise the existing expense is updated.
     */
    private void openExpenseDialog(Expense existingExpense) {
        boolean isEdit = existingExpense != null;

        JDialog dialog = new JDialog(
                SwingUtilities.getWindowAncestor(this),
                isEdit ? "Edit Expense" : "Add Expense",
                Dialog.ModalityType.APPLICATION_MODAL
        );
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(AppTheme.SURFACE);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(AppTheme.SURFACE);
        form.setBorder(new EmptyBorder(24, 28, 24, 28));

        JTextField dateField = addFormField(form, "Date (dd MMM yyyy)",
                isEdit ? existingExpense.date.format(DATE_FORMAT) : LocalDate.now().format(DATE_FORMAT));

        JLabel categoryLabel = createFieldLabel("Category");
        JComboBox<String> categoryCombo = new JComboBox<>(CATEGORIES);
        categoryCombo.setFont(AppTheme.BODY_FONT);
        categoryCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        categoryCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        if (isEdit) {
            categoryCombo.setSelectedItem(existingExpense.category);
        }
        form.add(categoryLabel);
        form.add(Box.createVerticalStrut(6));
        form.add(categoryCombo);
        form.add(Box.createVerticalStrut(14));

        JTextField descriptionField = addFormField(form, "Description",
                isEdit ? existingExpense.description : "");

        JTextField amountField = addFormField(form, "Amount",
                isEdit ? String.valueOf(existingExpense.amount) : "");

        JLabel statusLabel = createFieldLabel("Payment Status");
        JComboBox<String> statusCombo = new JComboBox<>(PAYMENT_STATUSES);
        statusCombo.setFont(AppTheme.BODY_FONT);
        statusCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        if (isEdit) {
            statusCombo.setSelectedItem(existingExpense.paymentStatus);
        }
        form.add(statusLabel);
        form.add(Box.createVerticalStrut(6));
        form.add(statusCombo);
        form.add(Box.createVerticalStrut(14));

        JTextField notesField = addFormField(form, "Notes",
                isEdit ? existingExpense.notes : "");

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton cancelButton = createSecondaryButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());

        JButton saveButton = createPrimaryButton(isEdit ? "Save Changes" : "Add Expense");
        saveButton.addActionListener(e -> {
            String date = dateField.getText().trim();
            String category = (String) categoryCombo.getSelectedItem();
            String description = descriptionField.getText().trim();
            String amountText = amountField.getText().trim();
            String status = (String) statusCombo.getSelectedItem();
            String notes = notesField.getText().trim();

            if (description.isEmpty()) {
                showValidationError(dialog, "Description cannot be empty.");
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountText);
            } catch (NumberFormatException ex) {
                showValidationError(dialog, "Please enter a valid amount.");
                return;
            }

            if (amount <= 0) {
                showValidationError(dialog, "Amount must be greater than 0.");
                return;
            }

            LocalDate parsedDate;
            try {
                parsedDate = LocalDate.parse(date, DATE_FORMAT);
            } catch (Exception ex) {
                showValidationError(dialog, "Please enter a valid date in the format dd MMM yyyy.");
                return;
            }

            if (isEdit) {
                existingExpense.date = parsedDate;
                existingExpense.category = category;
                existingExpense.description = description;
                existingExpense.amount = amount;
                existingExpense.paymentStatus = status;
                existingExpense.notes = notes;
            } else {
                addExpenseInternal(parsedDate, category, description, amount, status, notes);
            }

            refreshTable();
            refreshSummary();
            dialog.dispose();
        });

        buttonRow.add(cancelButton);
        buttonRow.add(saveButton);

        form.add(buttonRow);

        dialog.add(form, BorderLayout.CENTER);
        dialog.setSize(420, 480);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    private JTextField addFormField(JPanel form, String labelText, String initialValue) {
        JLabel label = createFieldLabel(labelText);

        JTextField field = new JTextField(initialValue);
        field.setFont(AppTheme.BODY_FONT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER),
                new EmptyBorder(5, 8, 5, 8)
        ));

        form.add(label);
        form.add(Box.createVerticalStrut(6));
        form.add(field);
        form.add(Box.createVerticalStrut(14));

        return field;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(AppTheme.SMALL_FONT);
        label.setForeground(AppTheme.TEXT_MUTED);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void showValidationError(Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
        );
    }

    // =====================================================================
    // BUTTON HELPERS
    // =====================================================================

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(AppTheme.BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(AppTheme.PRIMARY);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 16, 8, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(AppTheme.BUTTON_FONT);
        button.setForeground(AppTheme.TEXT_PRIMARY);
        button.setBackground(AppTheme.SURFACE);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER),
                new EmptyBorder(7, 15, 7, 15)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}