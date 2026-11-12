package com.pharmacy.ui.main;

import com.pharmacy.config.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * SettingsPanel
 *
 * Allows the user to view and adjust pharmacy profile, business, invoice,
 * notification, and user preference settings. UI-only. All values are
 * held in memory for the current session. No database, DAO, service,
 * or JDBC code is used in this phase.
 */
public class SettingsPanel extends JPanel {

    private static final String[] CURRENCIES = {"INR (₹)", "USD ($)", "EUR (€)", "GBP (£)"};
    private static final String[] PAYMENT_METHODS = {"Cash", "Card", "UPI", "Bank Transfer"};

    // Pharmacy Profile fields
    private JTextField pharmacyNameField;
    private JTextField ownerNameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField addressField;
    private JTextField gstNumberField;
    private JTextField licenseNumberField;
    private JButton editSaveProfileButton;
    private boolean profileEditable = false;

    // Business Settings fields
    private JComboBox<String> currencyCombo;
    private JTextField taxPercentField;
    private JTextField invoicePrefixField;
    private JComboBox<String> paymentMethodCombo;
    private JTextField lowStockThresholdField;
    private JTextField expiryWarningDaysField;

    // Invoice Settings checkboxes
    private JCheckBox showPharmacyNameCheck;
    private JCheckBox showGstCheck;
    private JCheckBox showCustomerInfoCheck;
    private JCheckBox showPharmacistInfoCheck;

    // Notification Settings checkboxes
    private JCheckBox lowStockAlertsCheck;
    private JCheckBox nearExpiryAlertsCheck;
    private JCheckBox expiredMedicineAlertsCheck;
    private JCheckBox pendingPaymentAlertsCheck;

    // User Preferences
    private JLabel themeDisplayLabel;
    private JCheckBox confirmBeforeDeleteCheck;
    private JCheckBox autoRefreshCheck;

    public SettingsPanel() {
        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildScrollableContent(), BorderLayout.CENTER);

        setProfileFieldsEditable(false);
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

        JLabel title = new JLabel("Settings");
        title.setFont(AppTheme.HEADING_FONT);
        title.setForeground(AppTheme.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Manage your pharmacy application preferences");
        subtitle.setFont(AppTheme.SMALL_FONT);
        subtitle.setForeground(AppTheme.TEXT_MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleArea.add(title);
        titleArea.add(Box.createVerticalStrut(4));
        titleArea.add(subtitle);

        header.add(titleArea, BorderLayout.WEST);

        return header;
    }

    // =====================================================================
    // SCROLLABLE CONTENT
    // =====================================================================

    private JComponent buildScrollableContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(AppTheme.BACKGROUND);
        content.setBorder(new EmptyBorder(20, 24, 24, 24));

        content.add(buildPharmacyProfileSection());
        content.add(Box.createVerticalStrut(20));
        content.add(buildBusinessSettingsSection());
        content.add(Box.createVerticalStrut(20));
        content.add(buildInvoiceSettingsSection());
        content.add(Box.createVerticalStrut(20));
        content.add(buildNotificationSettingsSection());
        content.add(Box.createVerticalStrut(20));
        content.add(buildUserPreferencesSection());
        content.add(Box.createVerticalStrut(20));
        content.add(buildDataManagementSection());
        content.add(Box.createVerticalStrut(20));
        content.add(buildSecuritySection());
        content.add(Box.createVerticalStrut(20));
        content.add(buildAboutSection());

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(AppTheme.BACKGROUND);
        scrollPane.getViewport().setBackground(AppTheme.BACKGROUND);

        return scrollPane;
    }

    // =====================================================================
    // PHARMACY PROFILE
    // =====================================================================

    private JComponent buildPharmacyProfileSection() {
        JPanel card = createCardContainer("Pharmacy Profile", "Basic details about your pharmacy");

        JPanel formGrid = new JPanel(new GridLayout(0, 2, 20, 14));
        formGrid.setOpaque(false);
        formGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        formGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        pharmacyNameField = new JTextField("City Care Pharmacy");
        ownerNameField = new JTextField("Ravi Sharma");
        phoneField = new JTextField("9876543210");
        emailField = new JTextField("citycarepharmacy@example.com");
        addressField = new JTextField("12 MG Road, Jaipur, Rajasthan");
        gstNumberField = new JTextField("08ABCDE1234F1Z5");
        licenseNumberField = new JTextField("RJ-PH-2023-4521");

        formGrid.add(buildFieldGroup("Pharmacy Name", pharmacyNameField));
        formGrid.add(buildFieldGroup("Owner Name", ownerNameField));
        formGrid.add(buildFieldGroup("Phone Number", phoneField));
        formGrid.add(buildFieldGroup("Email", emailField));
        formGrid.add(buildFieldGroup("Pharmacy Address", addressField));
        formGrid.add(buildFieldGroup("GST Number", gstNumberField));
        formGrid.add(buildFieldGroup("License Number", licenseNumberField));

        card.add(Box.createVerticalStrut(14));
        card.add(formGrid);
        card.add(Box.createVerticalStrut(14));

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        editSaveProfileButton = createPrimaryButton("Edit Profile");
        editSaveProfileButton.addActionListener(e -> toggleProfileEdit());

        buttonRow.add(editSaveProfileButton);
        card.add(buttonRow);

        return card;
    }

    private void toggleProfileEdit() {
        if (!profileEditable) {
            setProfileFieldsEditable(true);
            editSaveProfileButton.setText("Save Changes");
            profileEditable = true;
        } else {
            if (pharmacyNameField.getText().trim().isEmpty()) {
                showValidationError(this, "Pharmacy Name cannot be empty.");
                return;
            }
            setProfileFieldsEditable(false);
            editSaveProfileButton.setText("Edit Profile");
            profileEditable = false;
            showSuccessMessage("Pharmacy profile updated successfully.");
        }
    }

    private void setProfileFieldsEditable(boolean editable) {
        pharmacyNameField.setEditable(editable);
        ownerNameField.setEditable(editable);
        phoneField.setEditable(editable);
        emailField.setEditable(editable);
        addressField.setEditable(editable);
        gstNumberField.setEditable(editable);
        licenseNumberField.setEditable(editable);
    }

    // =====================================================================
    // BUSINESS SETTINGS
    // =====================================================================

    private JComponent buildBusinessSettingsSection() {
        JPanel card = createCardContainer("Business Settings", "Configure pricing and inventory defaults");

        JPanel formGrid = new JPanel(new GridLayout(0, 2, 20, 14));
        formGrid.setOpaque(false);
        formGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        formGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 190));

        currencyCombo = new JComboBox<>(CURRENCIES);
        currencyCombo.setFont(AppTheme.BODY_FONT);

        taxPercentField = new JTextField("12");
        invoicePrefixField = new JTextField("INV-");

        paymentMethodCombo = new JComboBox<>(PAYMENT_METHODS);
        paymentMethodCombo.setFont(AppTheme.BODY_FONT);

        lowStockThresholdField = new JTextField("10");
        expiryWarningDaysField = new JTextField("30");

        formGrid.add(buildFieldGroup("Currency", currencyCombo));
        formGrid.add(buildFieldGroup("Tax / GST Percentage", taxPercentField));
        formGrid.add(buildFieldGroup("Invoice Prefix", invoicePrefixField));
        formGrid.add(buildFieldGroup("Default Payment Method", paymentMethodCombo));
        formGrid.add(buildFieldGroup("Low Stock Threshold", lowStockThresholdField));
        formGrid.add(buildFieldGroup("Expiry Warning Days", expiryWarningDaysField));

        card.add(Box.createVerticalStrut(14));
        card.add(formGrid);
        card.add(Box.createVerticalStrut(14));

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton saveButton = createPrimaryButton("Save Changes");
        saveButton.addActionListener(e -> {
            String taxText = taxPercentField.getText().trim();
            String lowStockText = lowStockThresholdField.getText().trim();
            String expiryText = expiryWarningDaysField.getText().trim();

            if (!isValidNumber(taxText) || !isValidNumber(lowStockText) || !isValidNumber(expiryText)) {
                showValidationError(this, "Tax Percentage, Low Stock Threshold and Expiry Warning Days must be valid numbers.");
                return;
            }

            showSuccessMessage("Business settings saved successfully.");
        });

        buttonRow.add(saveButton);
        card.add(buttonRow);

        return card;
    }

    // =====================================================================
    // INVOICE SETTINGS
    // =====================================================================

    private JComponent buildInvoiceSettingsSection() {
        JPanel card = createCardContainer("Invoice Settings", "Choose what appears on printed invoices");

        showPharmacyNameCheck = createSettingCheckBox("Show pharmacy name on invoice", true);
        showGstCheck = createSettingCheckBox("Show GST on invoice", true);
        showCustomerInfoCheck = createSettingCheckBox("Show customer information", true);
        showPharmacistInfoCheck = createSettingCheckBox("Show pharmacist information", false);

        card.add(Box.createVerticalStrut(10));
        card.add(showPharmacyNameCheck);
        card.add(showGstCheck);
        card.add(showCustomerInfoCheck);
        card.add(showPharmacistInfoCheck);
        card.add(Box.createVerticalStrut(14));

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton saveButton = createPrimaryButton("Save Changes");
        saveButton.addActionListener(e -> showSuccessMessage("Invoice settings saved successfully."));

        buttonRow.add(saveButton);
        card.add(buttonRow);

        return card;
    }

    // =====================================================================
    // NOTIFICATION SETTINGS
    // =====================================================================

    private JComponent buildNotificationSettingsSection() {
        JPanel card = createCardContainer("Notification Settings", "Control which alerts you receive");

        lowStockAlertsCheck = createSettingCheckBox("Low stock alerts", true);
        nearExpiryAlertsCheck = createSettingCheckBox("Near expiry alerts", true);
        expiredMedicineAlertsCheck = createSettingCheckBox("Expired medicine alerts", true);
        pendingPaymentAlertsCheck = createSettingCheckBox("Pending payment alerts", false);

        card.add(Box.createVerticalStrut(10));
        card.add(lowStockAlertsCheck);
        card.add(nearExpiryAlertsCheck);
        card.add(expiredMedicineAlertsCheck);
        card.add(pendingPaymentAlertsCheck);
        card.add(Box.createVerticalStrut(14));

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton saveButton = createPrimaryButton("Save Changes");
        saveButton.addActionListener(e -> showSuccessMessage("Notification settings saved successfully."));

        buttonRow.add(saveButton);
        card.add(buttonRow);

        return card;
    }

    // =====================================================================
    // USER PREFERENCES
    // =====================================================================

    private JComponent buildUserPreferencesSection() {
        JPanel card = createCardContainer("User Preferences", "Personalize your application experience");

        JPanel themeRow = new JPanel(new BorderLayout());
        themeRow.setOpaque(false);
        themeRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        themeRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel themeLabel = new JLabel("Application Theme");
        themeLabel.setFont(AppTheme.BODY_FONT);
        themeLabel.setForeground(AppTheme.TEXT_PRIMARY);

        themeDisplayLabel = new JLabel("Light (Default)");
        themeDisplayLabel.setFont(AppTheme.SMALL_FONT);
        themeDisplayLabel.setForeground(AppTheme.TEXT_MUTED);

        themeRow.add(themeLabel, BorderLayout.WEST);
        themeRow.add(themeDisplayLabel, BorderLayout.EAST);

        confirmBeforeDeleteCheck = createSettingCheckBox("Confirm before deleting records", true);
        autoRefreshCheck = createSettingCheckBox("Automatically refresh data", true);

        card.add(Box.createVerticalStrut(10));
        card.add(themeRow);
        card.add(Box.createVerticalStrut(10));
        card.add(confirmBeforeDeleteCheck);
        card.add(autoRefreshCheck);
        card.add(Box.createVerticalStrut(14));

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton saveButton = createPrimaryButton("Save Changes");
        saveButton.addActionListener(e -> showSuccessMessage("Preferences saved successfully."));

        buttonRow.add(saveButton);
        card.add(buttonRow);

        return card;
    }

    // =====================================================================
    // DATA MANAGEMENT
    // =====================================================================

    private JComponent buildDataManagementSection() {
        JPanel card = createCardContainer("Data Management", "Export or back up your pharmacy data");

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton exportButton = createSecondaryButton("Export Data");
        exportButton.addActionListener(e -> showDataManagementNotice("Export Data"));

        JButton backupButton = createSecondaryButton("Backup Data");
        backupButton.addActionListener(e -> showDataManagementNotice("Backup Data"));

        JButton restoreButton = createSecondaryButton("Restore Data");
        restoreButton.addActionListener(e -> showDataManagementNotice("Restore Data"));

        buttonRow.add(exportButton);
        buttonRow.add(backupButton);
        buttonRow.add(restoreButton);

        card.add(Box.createVerticalStrut(12));
        card.add(buttonRow);

        return card;
    }

    private void showDataManagementNotice(String actionName) {
        JOptionPane.showMessageDialog(
                this,
                actionName + " will be available once database integration is complete.",
                actionName,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =====================================================================
    // SECURITY
    // =====================================================================

    private JComponent buildSecuritySection() {
        JPanel card = createCardContainer("Security", "Manage your account password");

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton changePasswordButton = createSecondaryButton("Change Password");
        changePasswordButton.addActionListener(e -> openChangePasswordDialog());

        buttonRow.add(changePasswordButton);

        card.add(Box.createVerticalStrut(12));
        card.add(buttonRow);

        return card;
    }

    private void openChangePasswordDialog() {
        JDialog dialog = new JDialog(
                SwingUtilities.getWindowAncestor(this),
                "Change Password",
                Dialog.ModalityType.APPLICATION_MODAL
        );
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(AppTheme.SURFACE);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(AppTheme.SURFACE);
        form.setBorder(new EmptyBorder(24, 28, 24, 28));

        JPasswordField currentPasswordField = addPasswordField(form, "Current Password");
        JPasswordField newPasswordField = addPasswordField(form, "New Password");
        JPasswordField confirmPasswordField = addPasswordField(form, "Confirm New Password");

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton cancelButton = createSecondaryButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());

        JButton updateButton = createPrimaryButton("Update Password");
        updateButton.addActionListener(e -> {
            String currentPassword = new String(currentPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                showValidationError(dialog, "Please fill in all password fields.");
                return;
            }

            if (newPassword.length() < 6) {
                showValidationError(dialog, "New password must be at least 6 characters long.");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                showValidationError(dialog, "New password and confirmation do not match.");
                return;
            }

            JOptionPane.showMessageDialog(
                    dialog,
                    "Password updated for this session. This will be connected to authentication later.",
                    "Password Updated",
                    JOptionPane.INFORMATION_MESSAGE
            );
            dialog.dispose();
        });

        buttonRow.add(cancelButton);
        buttonRow.add(updateButton);

        form.add(buttonRow);

        dialog.add(form, BorderLayout.CENTER);
        dialog.setSize(380, 340);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    private JPasswordField addPasswordField(JPanel form, String labelText) {
        JLabel label = createFieldLabel(labelText);

        JPasswordField field = new JPasswordField();
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

    // =====================================================================
    // ABOUT APPLICATION
    // =====================================================================

    private JComponent buildAboutSection() {
        JPanel card = createCardContainer("About Application", null);

        JLabel appName = new JLabel("Pharmacy Management System");
        appName.setFont(AppTheme.SUBHEADING_FONT);
        appName.setForeground(AppTheme.TEXT_PRIMARY);
        appName.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel versionLabel = new JLabel("Version 1.0");
        versionLabel.setFont(AppTheme.SMALL_FONT);
        versionLabel.setForeground(AppTheme.TEXT_MUTED);
        versionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descriptionLabel = new JLabel(
                "<html><div style='width:500px;'>A modern pharmacy management solution for managing "
                        + "medicines, inventory, sales, purchases, customers and business operations.</div></html>"
        );
        descriptionLabel.setFont(AppTheme.BODY_FONT);
        descriptionLabel.setForeground(AppTheme.TEXT_MUTED);
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(Box.createVerticalStrut(10));
        card.add(appName);
        card.add(Box.createVerticalStrut(4));
        card.add(versionLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(descriptionLabel);

        return card;
    }

    // =====================================================================
    // SHARED HELPERS
    // =====================================================================

    private JPanel createCardContainer(String title, String subtitle) {
        JPanel outer = new JPanel();
        outer.setLayout(new BoxLayout(outer, BoxLayout.Y_AXIS));
        outer.setBackground(AppTheme.SURFACE);
        outer.setAlignmentX(Component.LEFT_ALIGNMENT);
        outer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER),
                new EmptyBorder(18, 20, 18, 20)
        ));
        outer.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(AppTheme.SUBHEADING_FONT);
        titleLabel.setForeground(AppTheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        outer.add(titleLabel);

        if (subtitle != null) {
            JLabel subtitleLabel = new JLabel(subtitle);
            subtitleLabel.setFont(AppTheme.SMALL_FONT);
            subtitleLabel.setForeground(AppTheme.TEXT_MUTED);
            subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            outer.add(Box.createVerticalStrut(2));
            outer.add(subtitleLabel);
        }

        return outer;
    }

    private JComponent buildFieldGroup(String labelText, JComponent field) {
        JPanel group = new JPanel();
        group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));
        group.setOpaque(false);

        JLabel label = createFieldLabel(labelText);

        if (field instanceof JTextField) {
            ((JTextField) field).setFont(AppTheme.BODY_FONT);
            field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(AppTheme.BORDER),
                    new EmptyBorder(5, 8, 5, 8)
            ));
        }
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));

        group.add(label);
        group.add(Box.createVerticalStrut(6));
        group.add(field);

        return group;
    }

    private JCheckBox createSettingCheckBox(String text, boolean initiallySelected) {
        JCheckBox checkBox = new JCheckBox(text, initiallySelected);
        checkBox.setFont(AppTheme.BODY_FONT);
        checkBox.setForeground(AppTheme.TEXT_PRIMARY);
        checkBox.setOpaque(false);
        checkBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        checkBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return checkBox;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(AppTheme.SMALL_FONT);
        label.setForeground(AppTheme.TEXT_MUTED);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private boolean isValidNumber(String text) {
        try {
            double value = Double.parseDouble(text);
            return value >= 0;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private void showValidationError(Component parent, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Success",
                JOptionPane.INFORMATION_MESSAGE
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