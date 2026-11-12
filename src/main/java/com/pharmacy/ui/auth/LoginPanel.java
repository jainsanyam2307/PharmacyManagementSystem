package com.pharmacy.ui.auth;

import com.pharmacy.config.AppTheme;
import com.pharmacy.dao.UserDAO;
import com.pharmacy.ui.common.ModernCard;
import com.pharmacy.ui.common.ModernIcons;
import com.pharmacy.ui.common.PillBadge;
import com.pharmacy.ui.common.PillButton;
import com.pharmacy.ui.landing.LandingFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class LoginPanel extends JPanel {

    private final LandingFrame landingFrame;
    private final UserDAO userDAO;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox rememberMeCheckBox;

    public LoginPanel(LandingFrame landingFrame) {
        this.landingFrame = landingFrame;
        this.userDAO = new UserDAO();

        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);

        add(createHeader(), BorderLayout.NORTH);
        add(createLoginArea(), BorderLayout.CENTER);
    }

    // =========================================================
    // Header
    // =========================================================

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppTheme.SURFACE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER),
                new EmptyBorder(16, 32, 16, 32)
        ));

        // Brand
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
        backButton.addActionListener(e -> landingFrame.showLanding());

        header.add(brandPanel, BorderLayout.WEST);
        header.add(backButton, BorderLayout.EAST);

        return header;
    }

    // =========================================================
    // Login Area
    // =========================================================

    private JPanel createLoginArea() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(30, 20, 30, 20));

        ModernCard card = new ModernCard(20);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(32, 36, 32, 36));
        card.setPreferredSize(new Dimension(420, 520));

        JLabel title = new JLabel("Welcome Back");
        title.setFont(new Font(AppTheme.TITLE_FONT.getFamily(), Font.BOLD, 24));
        title.setForeground(AppTheme.TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Sign in to access your pharmacy operations");
        subtitle.setFont(AppTheme.BODY_FONT);
        subtitle.setForeground(AppTheme.TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(title);
        card.add(Box.createVerticalStrut(4));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(24));

        // Username
        JLabel usernameLabel = createFieldLabel("Username");
        usernameField = new JTextField();
        styleTextField(usernameField);

        card.add(usernameLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(16));

        // Password
        JLabel passwordLabel = createFieldLabel("Password");
        passwordField = new JPasswordField();
        styleTextField(passwordField);

        card.add(passwordLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(14));

        // Remember me
        rememberMeCheckBox = new JCheckBox("Remember me");
        rememberMeCheckBox.setFont(AppTheme.SMALL_FONT);
        rememberMeCheckBox.setForeground(AppTheme.TEXT_SECONDARY);
        rememberMeCheckBox.setOpaque(false);
        rememberMeCheckBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(rememberMeCheckBox);

        card.add(Box.createVerticalStrut(20));

        // Login Button
        PillButton loginButton = PillButton.lime("Sign In", ModernIcons.get("check", 16, AppTheme.PRIMARY));
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(e -> handleLogin());

        card.add(loginButton);
        card.add(Box.createVerticalStrut(16));

        // Signup section
        JPanel signupPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        signupPanel.setOpaque(false);

        JLabel accountLabel = new JLabel("Don't have an account?");
        accountLabel.setFont(AppTheme.SMALL_FONT);
        accountLabel.setForeground(AppTheme.TEXT_SECONDARY);

        JButton signupButton = new JButton("Create Account");
        signupButton.setFont(AppTheme.SMALL_BOLD_FONT);
        signupButton.setForeground(AppTheme.PRIMARY);
        signupButton.setBorderPainted(false);
        signupButton.setContentAreaFilled(false);
        signupButton.setFocusPainted(false);
        signupButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signupButton.addActionListener(e -> landingFrame.showSignup());

        signupPanel.add(accountLabel);
        signupPanel.add(signupButton);

        card.add(signupPanel);
        card.add(Box.createVerticalStrut(18));

        // Demo credentials box
        JPanel demoPanel = new JPanel();
        demoPanel.setLayout(new BoxLayout(demoPanel, BoxLayout.Y_AXIS));
        demoPanel.setBackground(AppTheme.PRIMARY_LIGHT);
        demoPanel.setBorder(new EmptyBorder(10, 14, 10, 14));
        demoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel demoTitle = new JLabel("Demo Credentials", ModernIcons.get("lock", 12, AppTheme.PRIMARY), SwingConstants.LEFT);
        demoTitle.setFont(AppTheme.SMALL_BOLD_FONT);
        demoTitle.setForeground(AppTheme.PRIMARY);

        JLabel demoCredentials = new JLabel("Username: admin   |   Password: admin123");
        demoCredentials.setFont(AppTheme.SMALL_FONT);
        demoCredentials.setForeground(AppTheme.TEXT_SECONDARY);

        demoPanel.add(demoTitle);
        demoPanel.add(Box.createVerticalStrut(2));
        demoPanel.add(demoCredentials);

        card.add(demoPanel);

        wrapper.add(card);
        return wrapper;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(AppTheme.SMALL_BOLD_FONT);
        label.setForeground(AppTheme.TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void styleTextField(JTextField field) {
        field.setFont(AppTheme.BODY_FONT);
        field.setForeground(AppTheme.TEXT_PRIMARY);
        field.setBackground(AppTheme.BACKGROUND);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER),
                new EmptyBorder(8, 12, 8, 12)
        ));
    }

    // =========================================================
    // Login Logic
    // =========================================================

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty()) {
            showError("Please enter your username.");
            usernameField.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            showError("Please enter your password.");
            passwordField.requestFocus();
            return;
        }

        boolean authenticated = userDAO.authenticate(username, password);

        if (authenticated) {
            JOptionPane.showMessageDialog(
                    this,
                    "Login successful.\nRedirecting to dashboard...",
                    "Welcome to Pharmly",
                    JOptionPane.INFORMATION_MESSAGE
            );
            landingFrame.showMainApp();
        } else {
            showError("Invalid username or password.");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Authentication Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
