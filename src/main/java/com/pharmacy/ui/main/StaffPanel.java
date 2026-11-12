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
 * StaffPanel
 *
 * Allows pharmacy staff/admins to view, add, edit, and manage the status
 * of staff members. UI-only. Uses in-memory sample data. No database,
 * DAO, service, or JDBC code is used in this phase.
 */
public class StaffPanel extends JPanel {

    private static final String[] ROLES = {
            "Administrator", "Pharmacist", "Cashier", "Inventory Manager", "Assistant"
    };

    private static final String[] STATUSES = {"Active", "On Leave", "Inactive"};

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    // Simple in-memory model for a staff record.
    private static class Staff {
        String id;
        String name;
        String phone;
        String email;
        String role;
        LocalDate joiningDate;
        String status;
        String address;

        Staff(String id, String name, String phone, String email, String role,
              LocalDate joiningDate, String status, String address) {
            this.id = id;
            this.name = name;
            this.phone = phone;
            this.email = email;
            this.role = role;
            this.joiningDate = joiningDate;
            this.status = status;
            this.address = address;
        }
    }

    private final List<Staff> allStaff = new ArrayList<>();
    private int nextStaffNumber = 1;

    private DefaultTableModel tableModel;
    private JTable staffTable;
    private JTextField searchField;

    private JLabel totalStaffValue;
    private JLabel activeStaffValue;
    private JLabel onLeaveValue;
    private JLabel adminsValue;

    public StaffPanel() {
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

        JLabel title = new JLabel("Staff Management");
        title.setFont(AppTheme.HEADING_FONT);
        title.setForeground(AppTheme.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Manage pharmacy staff and their access");
        subtitle.setFont(AppTheme.SMALL_FONT);
        subtitle.setForeground(AppTheme.TEXT_MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleArea.add(title);
        titleArea.add(Box.createVerticalStrut(4));
        titleArea.add(subtitle);

        JButton addStaffButton = createPrimaryButton("+ Add Staff");
        addStaffButton.addActionListener(e -> openStaffDialog(null));

        header.add(titleArea, BorderLayout.WEST);
        header.add(addStaffButton, BorderLayout.EAST);

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

        totalStaffValue = new JLabel();
        activeStaffValue = new JLabel();
        onLeaveValue = new JLabel();
        adminsValue = new JLabel();

        row.add(buildSummaryCard("Total Staff", totalStaffValue, AppTheme.PRIMARY));
        row.add(buildSummaryCard("Active Staff", activeStaffValue, AppTheme.SUCCESS));
        row.add(buildSummaryCard("On Leave", onLeaveValue, AppTheme.WARNING));
        row.add(buildSummaryCard("Administrators", adminsValue, AppTheme.PRIMARY_DARK));

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
        JPanel row = new JPanel(new BorderLayout(12, 0));
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
                "Search by Staff ID, Name, Phone, Role or Status");
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { refreshTable(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { refreshTable(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { refreshTable(); }
        });

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);

        JButton viewButton = createSecondaryButton("View");
        viewButton.addActionListener(e -> viewSelectedStaff());

        JButton editButton = createSecondaryButton("Edit");
        editButton.addActionListener(e -> editSelectedStaff());

        JButton changeStatusButton = createSecondaryButton("Change Status");
        changeStatusButton.addActionListener(e -> changeSelectedStaffStatus());

        actions.add(viewButton);
        actions.add(editButton);
        actions.add(changeStatusButton);

        row.add(searchField, BorderLayout.CENTER);
        row.add(actions, BorderLayout.EAST);

        return row;
    }

    // =====================================================================
    // TABLE
    // =====================================================================

    private JComponent buildTableArea() {
        String[] columns = {"Staff ID", "Name", "Phone", "Email", "Role", "Joining Date", "Status"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        staffTable = new JTable(tableModel);
        staffTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        styleTable(staffTable);

        staffTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewSelectedStaff();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(staffTable);
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

        addStaffInternal("Ravi Sharma", "9876543210", "ravi.sharma@example.com",
                "Administrator", today.minusYears(3), "Active", "12 MG Road, Jaipur");
        addStaffInternal("Anita Verma", "9823456712", "anita.verma@example.com",
                "Pharmacist", today.minusYears(2), "Active", "45 Civil Lines, Jaipur");
        addStaffInternal("Suresh Kumar", "9812345678", "suresh.kumar@example.com",
                "Cashier", today.minusMonths(14), "Active", "8 Tonk Road, Jaipur");
        addStaffInternal("Priya Singh", "9898123456", "priya.singh@example.com",
                "Inventory Manager", today.minusMonths(20), "On Leave", "22 Malviya Nagar, Jaipur");
        addStaffInternal("Mohit Agarwal", "9911223344", "mohit.agarwal@example.com",
                "Assistant", today.minusMonths(6), "Active", "5 C-Scheme, Jaipur");
        addStaffInternal("Kavita Joshi", "9765432109", "kavita.joshi@example.com",
                "Pharmacist", today.minusYears(1), "Active", "17 Vaishali Nagar, Jaipur");
        addStaffInternal("Deepak Yadav", "9654321098", "deepak.yadav@example.com",
                "Cashier", today.minusMonths(9), "Inactive", "30 Sanganer, Jaipur");
        addStaffInternal("Neha Gupta", "9543210987", "neha.gupta@example.com",
                "Administrator", today.minusYears(4), "Active", "3 Raja Park, Jaipur");
    }

    private void addStaffInternal(String name, String phone, String email, String role,
                                  LocalDate joiningDate, String status, String address) {
        String id = "STF" + String.format("%04d", nextStaffNumber++);
        allStaff.add(new Staff(id, name, phone, email, role, joiningDate, status, address));
    }

    // =====================================================================
    // TABLE REFRESH / SEARCH
    // =====================================================================

    private void refreshTable() {
        String query = searchField == null ? "" : searchField.getText().trim().toLowerCase();

        tableModel.setRowCount(0);

        for (Staff staff : allStaff) {
            if (!query.isEmpty() && !matchesSearch(staff, query)) {
                continue;
            }

            tableModel.addRow(new Object[]{
                    staff.id,
                    staff.name,
                    staff.phone,
                    staff.email,
                    staff.role,
                    staff.joiningDate.format(DATE_FORMAT),
                    staff.status
            });
        }
    }

    private boolean matchesSearch(Staff staff, String query) {
        return staff.id.toLowerCase().contains(query)
                || staff.name.toLowerCase().contains(query)
                || staff.phone.toLowerCase().contains(query)
                || staff.role.toLowerCase().contains(query)
                || staff.status.toLowerCase().contains(query);
    }

    private void refreshSummary() {
        int total = allStaff.size();
        int active = 0;
        int onLeave = 0;
        int admins = 0;

        for (Staff staff : allStaff) {
            if ("Active".equals(staff.status)) {
                active++;
            }
            if ("On Leave".equals(staff.status)) {
                onLeave++;
            }
            if ("Administrator".equals(staff.role)) {
                admins++;
            }
        }

        totalStaffValue.setText(String.valueOf(total));
        activeStaffValue.setText(String.valueOf(active));
        onLeaveValue.setText(String.valueOf(onLeave));
        adminsValue.setText(String.valueOf(admins));
    }

    // =====================================================================
    // SELECTION HELPERS
    // =====================================================================

    private Staff getSelectedStaff() {
        int viewRow = staffTable.getSelectedRow();
        if (viewRow < 0) {
            return null;
        }
        String id = (String) tableModel.getValueAt(viewRow, 0);
        return findStaffById(id);
    }

    private Staff findStaffById(String id) {
        for (Staff staff : allStaff) {
            if (staff.id.equals(id)) {
                return staff;
            }
        }
        return null;
    }

    private void requireSelection(Runnable action) {
        Staff selected = getSelectedStaff();
        if (selected == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a staff member first.",
                    "No Staff Selected",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        action.run();
    }

    private void editSelectedStaff() {
        requireSelection(() -> openStaffDialog(getSelectedStaff()));
    }

    private void viewSelectedStaff() {
        Staff selected = getSelectedStaff();
        if (selected == null) {
            return;
        }

        String details = "Staff ID: " + selected.id + "\n"
                + "Name: " + selected.name + "\n"
                + "Phone: " + selected.phone + "\n"
                + "Email: " + (selected.email.isEmpty() ? "-" : selected.email) + "\n"
                + "Role: " + selected.role + "\n"
                + "Joining Date: " + selected.joiningDate.format(DATE_FORMAT) + "\n"
                + "Status: " + selected.status + "\n"
                + "Address: " + (selected.address.isEmpty() ? "-" : selected.address);

        JOptionPane.showMessageDialog(
                this,
                details,
                "Staff Details",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void changeSelectedStaffStatus() {
        Staff selected = getSelectedStaff();
        if (selected == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a staff member first.",
                    "No Staff Selected",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String newStatus = (String) JOptionPane.showInputDialog(
                this,
                "Select new status for " + selected.name + ":",
                "Change Status",
                JOptionPane.PLAIN_MESSAGE,
                null,
                STATUSES,
                selected.status
        );

        if (newStatus != null && !newStatus.equals(selected.status)) {
            selected.status = newStatus;
            refreshTable();
            refreshSummary();
        }
    }

    // =====================================================================
    // ADD / EDIT DIALOG
    // =====================================================================

    /**
     * Opens the Add/Edit Staff dialog. If existingStaff is null, a new
     * staff member is created; otherwise the existing record is updated.
     */
    private void openStaffDialog(Staff existingStaff) {
        boolean isEdit = existingStaff != null;

        JDialog dialog = new JDialog(
                SwingUtilities.getWindowAncestor(this),
                isEdit ? "Edit Staff" : "Add Staff",
                Dialog.ModalityType.APPLICATION_MODAL
        );
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(AppTheme.SURFACE);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(AppTheme.SURFACE);
        form.setBorder(new EmptyBorder(24, 28, 24, 28));

        JTextField nameField = addFormField(form, "Full Name", isEdit ? existingStaff.name : "");
        JTextField phoneField = addFormField(form, "Phone", isEdit ? existingStaff.phone : "");
        JTextField emailField = addFormField(form, "Email", isEdit ? existingStaff.email : "");

        JLabel roleLabel = createFieldLabel("Role");
        JComboBox<String> roleCombo = new JComboBox<>(ROLES);
        roleCombo.setFont(AppTheme.BODY_FONT);
        roleCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        roleCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        if (isEdit) {
            roleCombo.setSelectedItem(existingStaff.role);
        }
        form.add(roleLabel);
        form.add(Box.createVerticalStrut(6));
        form.add(roleCombo);
        form.add(Box.createVerticalStrut(14));

        JTextField joiningDateField = addFormField(form, "Joining Date (dd MMM yyyy)",
                isEdit ? existingStaff.joiningDate.format(DATE_FORMAT) : LocalDate.now().format(DATE_FORMAT));

        JLabel statusLabel = createFieldLabel("Status");
        JComboBox<String> statusCombo = new JComboBox<>(STATUSES);
        statusCombo.setFont(AppTheme.BODY_FONT);
        statusCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        if (isEdit) {
            statusCombo.setSelectedItem(existingStaff.status);
        }
        form.add(statusLabel);
        form.add(Box.createVerticalStrut(6));
        form.add(statusCombo);
        form.add(Box.createVerticalStrut(14));

        JTextField addressField = addFormField(form, "Address", isEdit ? existingStaff.address : "");

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton cancelButton = createSecondaryButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());

        JButton saveButton = createPrimaryButton(isEdit ? "Save Changes" : "Add Staff");
        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String role = (String) roleCombo.getSelectedItem();
            String joiningDateText = joiningDateField.getText().trim();
            String status = (String) statusCombo.getSelectedItem();
            String address = addressField.getText().trim();

            if (name.isEmpty()) {
                showValidationError(dialog, "Name is required.");
                return;
            }

            if (phone.isEmpty()) {
                showValidationError(dialog, "Phone is required.");
                return;
            }

            if (role == null || role.isEmpty()) {
                showValidationError(dialog, "Please select a role.");
                return;
            }

            LocalDate parsedJoiningDate;
            try {
                parsedJoiningDate = LocalDate.parse(joiningDateText, DATE_FORMAT);
            } catch (Exception ex) {
                showValidationError(dialog, "Please enter a valid joining date in the format dd MMM yyyy.");
                return;
            }

            if (isEdit) {
                existingStaff.name = name;
                existingStaff.phone = phone;
                existingStaff.email = email;
                existingStaff.role = role;
                existingStaff.joiningDate = parsedJoiningDate;
                existingStaff.status = status;
                existingStaff.address = address;
            } else {
                addStaffInternal(name, phone, email, role, parsedJoiningDate, status, address);
            }

            refreshTable();
            refreshSummary();
            dialog.dispose();
        });

        buttonRow.add(cancelButton);
        buttonRow.add(saveButton);

        form.add(buttonRow);

        dialog.add(form, BorderLayout.CENTER);
        dialog.setSize(420, 560);
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