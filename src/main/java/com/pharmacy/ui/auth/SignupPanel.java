package com.pharmacy.ui.auth;

import com.pharmacy.config.AppTheme;
import com.pharmacy.dao.UserDAO;
import com.pharmacy.ui.common.ModernCard;
import com.pharmacy.ui.common.ModernIcons;
import com.pharmacy.ui.common.PillButton;
import com.pharmacy.ui.landing.LandingFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.regex.Pattern;

/**
 * SignupPanel
 *
 * Account creation screen styled in the modern Pharmly theme.
 */
public class SignupPanel extends JPanel {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[0-9]{10}$");

    private final LandingFrame landingFrame;
    private final UserDAO userDAO;

    private JTextField fullNameField;
    private JTextField usernameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JCheckBox termsCheckBox;

    public SignupPanel(LandingFrame landingFrame) {
        this.landingFrame = landingFrame;
        this.userDAO = new UserDAO();

        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenterContent(), BorderLayout.CENTER);
    }

    // ---------------------------------------------------------------
    // Header
    // ---------------------------------------------------------------
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppTheme.SURFACE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER),
                new EmptyBorder(16, 32, 16, 32)
        ));

        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brandPanel.setOpaque(false);

        JLabel logo = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppTheme.ACCENT);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.setColor(AppTheme.PRIMARY);
                g2.setFont(new Font(getFont().getFamily(), Font.BOLD, 16));
                FontMetrics fm = g2.getFontMetrics();
                String text = "P";
                int tx = (getWidth() - fm.stringWidth(text)) / 2;
                int ty = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(text, tx, ty);
                g2.dispose();
            }
        };
        logo.setPreferredSize(new Dimension(32, 32));

        JLabel brand = new JLabel("Pharmly");
        brand.setFont(new Font(AppTheme.TITLE_FONT.getFamily(), Font.BOLD, 18));
        brand.setForeground(AppTheme.TEXT_PRIMARY);

        brandPanel.add(logo);
        brandPanel.add(brand);

        PillButton backButton = PillButton.secondary("← Back");
        backButton.addActionListener((ActionEvent e) -> landingFrame.showLanding());

        header.add(brandPanel, BorderLayout.WEST);
        header.add(backButton, BorderLayout.EAST);

        return header;
    }

    // ---------------------------------------------------------------
    // Center content
    // ---------------------------------------------------------------
    private JComponent buildCenterContent() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setOpaque(false);
        outer.setBorder(new EmptyBorder(24, 20, 24, 20));

        ModernCard card = new ModernCard(20);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(28, 36, 28, 36));
        card.setPreferredSize(new Dimension(460, 680));

        JLabel title = new JLabel("Create Your Account");
        title.setFont(new Font(AppTheme.TITLE_FONT.getFamily(), Font.BOLD, 22));
        title.setForeground(AppTheme.TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Fill in the details below to join Pharmly");
        subtitle.setFont(AppTheme.BODY_FONT);
        subtitle.setForeground(AppTheme.TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(4));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(20));

        fullNameField = addLabeledField(card, "Full Name");
        usernameField = addLabeledField(card, "Username");
        emailField = addLabeledField(card, "Email Address");
        phoneField = addLabeledField(card, "Phone Number");
        passwordField = addLabeledPasswordField(card, "Password");
        confirmPasswordField = addLabeledPasswordField(card, "Confirm Password");

        termsCheckBox = new JCheckBox("I agree to the Terms & Conditions");
        termsCheckBox.setFont(AppTheme.SMALL_FONT);
        termsCheckBox.setForeground(AppTheme.TEXT_SECONDARY);
        termsCheckBox.setOpaque(false);
        termsCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(termsCheckBox);

        card.add(Box.createVerticalStrut(18));

        PillButton createAccountButton = PillButton.lime("Create Account", ModernIcons.get("check", 16, AppTheme.PRIMARY));
        createAccountButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        createAccountButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createAccountButton.addActionListener(e -> handleCreateAccount());
        card.add(createAccountButton);

        card.add(Box.createVerticalStrut(14));

        JPanel loginRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        loginRow.setOpaque(false);
        loginRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel loginPrompt = new JLabel("Already have an account?");
        loginPrompt.setFont(AppTheme.SMALL_FONT);
        loginPrompt.setForeground(AppTheme.TEXT_SECONDARY);

        JButton loginLink = new JButton("Sign In");
        loginLink.setFont(AppTheme.SMALL_BOLD_FONT);
        loginLink.setForeground(AppTheme.PRIMARY);
        loginLink.setBorderPainted(false);
        loginLink.setContentAreaFilled(false);
        loginLink.setFocusPainted(false);
        loginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLink.addActionListener(e -> landingFrame.showLogin());

        loginRow.add(loginPrompt);
        loginRow.add(loginLink);

        card.add(loginRow);

        outer.add(card);
        return outer;
    }

    // ---------------------------------------------------------------
    // Field builders
    // ---------------------------------------------------------------
    private JTextField addLabeledField(JPanel parent, String labelText) {
        JLabel label = new JLabel(labelText);
        label.setFont(AppTheme.SMALL_BOLD_FONT);
        label.setForeground(AppTheme.TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField field = new JTextField();
        field.setFont(AppTheme.BODY_FONT);
        field.setForeground(AppTheme.TEXT_PRIMARY);
        field.setBackground(AppTheme.BACKGROUND);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER),
                new EmptyBorder(6, 10, 6, 10)
        ));

        parent.add(label);
        parent.add(Box.createVerticalStrut(4));
        parent.add(field);
        parent.add(Box.createVerticalStrut(10));

        return field;
    }

    private JPasswordField addLabeledPasswordField(JPanel parent, String labelText) {
        JLabel label = new JLabel(labelText);
        label.setFont(AppTheme.SMALL_BOLD_FONT);
        label.setForeground(AppTheme.TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPasswordField field = new JPasswordField();
        field.setFont(AppTheme.BODY_FONT);
        field.setForeground(AppTheme.TEXT_PRIMARY);
        field.setBackground(AppTheme.BACKGROUND);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER),
                new EmptyBorder(6, 10, 6, 10)
        ));

        parent.add(label);
        parent.add(Box.createVerticalStrut(4));
        parent.add(field);
        parent.add(Box.createVerticalStrut(10));

        return field;
    }

    // ---------------------------------------------------------------
    // Validation
    // ---------------------------------------------------------------
    private void handleCreateAccount() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty()
                || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }

        if (username.length() < 4) {
            showError("Username must be at least 4 characters long.");
            return;
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            showError("Please enter a valid email address.");
            return;
        }

        if (!PHONE_PATTERN.matcher(phone).matches()) {
            showError("Please enter a valid 10-digit phone number.");
            return;
        }

        if (password.length() < 6) {
            showError("Password must be at least 6 characters long.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            return;
        }

        if (!termsCheckBox.isSelected()) {
            showError("You must agree to the Terms & Conditions.");
            return;
        }

        boolean created = userDAO.createUser(
                fullName,
                username,
                email,
                phone,
                password
        );

        if (created) {
            JOptionPane.showMessageDialog(
                    this,
                    "Account created successfully!",
                    "Registration Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearFields();
            landingFrame.showLogin();
        } else {
            showError(
                    "Unable to create account.\n"
                            + "Username or email may already exist."
            );
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Validation Error",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void clearFields() {
        fullNameField.setText("");
        usernameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        termsCheckBox.setSelected(false);
    }
}
