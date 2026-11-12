package com.pharmacy.ui.landing;

import com.pharmacy.config.AppTheme;
import com.pharmacy.ui.common.ModernCard;
import com.pharmacy.ui.common.ModernIcons;
import com.pharmacy.ui.common.PillBadge;
import com.pharmacy.ui.common.PillButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class LandingPanel extends JPanel {

    private final LandingFrame landingFrame;

    public LandingPanel(LandingFrame landingFrame) {
        this.landingFrame = landingFrame;

        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
    }

    // =========================================================
    // HEADER
    // =========================================================

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppTheme.SURFACE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER),
                new EmptyBorder(16, 36, 16, 36)
        ));

        // Brand
        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        brandPanel.setOpaque(false);

        JLabel logo = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppTheme.ACCENT);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.setColor(AppTheme.PRIMARY);
                g2.setFont(new Font(getFont().getFamily(), Font.BOLD, 18));
                FontMetrics fm = g2.getFontMetrics();
                String text = "P";
                int tx = (getWidth() - fm.stringWidth(text)) / 2;
                int ty = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(text, tx, ty);
                g2.dispose();
            }
        };
        logo.setPreferredSize(new Dimension(36, 36));

        JPanel brandText = new JPanel();
        brandText.setOpaque(false);
        brandText.setLayout(new BoxLayout(brandText, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Pharmly");
        title.setFont(new Font(AppTheme.TITLE_FONT.getFamily(), Font.BOLD, 20));
        title.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Modern Pharmacy Intelligence System");
        subtitle.setFont(AppTheme.SMALL_FONT);
        subtitle.setForeground(AppTheme.TEXT_MUTED);

        brandText.add(title);
        brandText.add(subtitle);

        brandPanel.add(logo);
        brandPanel.add(brandText);

        // Header Buttons
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        actions.setOpaque(false);

        PillButton loginButton = PillButton.secondary("Sign In");
        loginButton.addActionListener(e -> landingFrame.showLogin());

        PillButton signupButton = PillButton.lime("Get Started Free");
        signupButton.addActionListener(e -> landingFrame.showSignup());

        actions.add(loginButton);
        actions.add(signupButton);

        header.add(brandPanel, BorderLayout.WEST);
        header.add(actions, BorderLayout.EAST);

        return header;
    }

    // =========================================================
    // MAIN CONTENT
    // =========================================================

    private JPanel createMainContent() {
        JPanel main = new JPanel(new BorderLayout());
        main.setOpaque(false);
        main.setBorder(new EmptyBorder(32, 50, 32, 50));

        main.add(createHeroSection(), BorderLayout.NORTH);
        main.add(createFeatureSection(), BorderLayout.CENTER);

        return main;
    }

    // =========================================================
    // HERO SECTION
    // =========================================================

    private JPanel createHeroSection() {
        JPanel hero = new JPanel();
        hero.setOpaque(false);
        hero.setLayout(new BoxLayout(hero, BoxLayout.Y_AXIS));

        PillBadge badge = new PillBadge("PHARMLY • ALL-IN-ONE PHARMACY SOLUTION", AppTheme.PRIMARY_LIGHT, AppTheme.PRIMARY);
        badge.setAlignmentX(Component.CENTER_ALIGNMENT);
        badge.setBorder(new EmptyBorder(6, 16, 6, 16));

        JLabel heading = new JLabel(
                "<html><div style='text-align:center;'>"
                        + "Manage Your Pharmacy<br>"
                        + "With Precision & Clarity"
                        + "</div></html>"
        );
        heading.setFont(new Font(AppTheme.TITLE_FONT.getFamily(), Font.BOLD, 36));
        heading.setForeground(AppTheme.TEXT_PRIMARY);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel description = new JLabel(
                "<html><div style='text-align:center;'>"
                        + "A simple, powerful, and intuitive platform to manage medicines, inventory, "
                        + "sales, purchases, customers, and financial reports from one unified dashboard."
                        + "</div></html>"
        );
        description.setFont(new Font(AppTheme.BODY_FONT.getFamily(), Font.PLAIN, 15));
        description.setForeground(AppTheme.TEXT_SECONDARY);
        description.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel ctaRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        ctaRow.setOpaque(false);

        PillButton startBtn = PillButton.lime("Get Started Free", ModernIcons.get("check", 16, AppTheme.PRIMARY));
        startBtn.addActionListener(e -> landingFrame.showSignup());

        PillButton loginBtn = PillButton.secondary("Sign In to Account");
        loginBtn.addActionListener(e -> landingFrame.showLogin());

        ctaRow.add(startBtn);
        ctaRow.add(loginBtn);

        hero.add(badge);
        hero.add(Box.createVerticalStrut(14));
        hero.add(heading);
        hero.add(Box.createVerticalStrut(10));
        hero.add(description);
        hero.add(Box.createVerticalStrut(18));
        hero.add(ctaRow);
        hero.add(Box.createVerticalStrut(24));

        return hero;
    }

    // =========================================================
    // FEATURE SECTION
    // =========================================================

    private JPanel createFeatureSection() {
        JPanel container = new JPanel(new BorderLayout(0, 14));
        container.setOpaque(false);

        JLabel sectionTitle = new JLabel("Everything You Need to Run Your Pharmacy Smoothly");
        sectionTitle.setFont(AppTheme.SUBHEADING_FONT);
        sectionTitle.setForeground(AppTheme.TEXT_PRIMARY);
        sectionTitle.setHorizontalAlignment(SwingConstants.CENTER);

        container.add(sectionTitle, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(2, 4, 16, 16));
        cards.setOpaque(false);

        cards.add(createFeatureCard("Medicines", "Manage catalogue details, batch numbers, selling prices, and units.", "pill"));
        cards.add(createFeatureCard("Stock Inventory", "Real-time tracking of in-stock, low-stock, and out-of-stock items.", "box"));
        cards.add(createFeatureCard("Sales & Billing", "Generate accurate bills quickly, compute discounts, and process payments.", "cart"));
        cards.add(createFeatureCard("Purchases", "Manage purchase orders, supplier shipments, and automatic stock updates.", "dollar"));
        cards.add(createFeatureCard("Customers", "Maintain organized customer contact info, purchase trends, and profiles.", "user"));
        cards.add(createFeatureCard("Expiry Management", "Proactively identify expired and near-expiry medications to reduce losses.", "alert"));
        cards.add(createFeatureCard("Reports & Analytics", "Actionable insights into revenue, margins, bestsellers, and costs.", "chart"));
        cards.add(createFeatureCard("Operating Expenses", "Record routine utility, rent, and overhead expenses with clear categorization.", "settings"));

        container.add(cards, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 14));
        bottom.setOpaque(false);

        PillButton exploreButton = PillButton.secondary("Explore Features");
        exploreButton.addActionListener(e -> showFeatureMessage());

        bottom.add(exploreButton);
        container.add(bottom, BorderLayout.SOUTH);

        return container;
    }

    private JPanel createFeatureCard(String title, String description, String iconName) {
        ModernCard card = new ModernCard(16);
        card.setLayout(new BorderLayout(0, 8));
        card.setBorder(new EmptyBorder(16, 16, 16, 16));
        card.setHoverable(true);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel titleLabel = new JLabel(title, ModernIcons.get(iconName, 18, AppTheme.PRIMARY), SwingConstants.LEFT);
        titleLabel.setFont(AppTheme.BODY_BOLD_FONT);
        titleLabel.setForeground(AppTheme.TEXT_PRIMARY);
        titleLabel.setIconTextGap(8);

        top.add(titleLabel, BorderLayout.WEST);

        JTextArea descriptionArea = new JTextArea(description);
        descriptionArea.setFont(AppTheme.SMALL_FONT);
        descriptionArea.setForeground(AppTheme.TEXT_SECONDARY);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setFocusable(false);
        descriptionArea.setOpaque(false);

        card.add(top, BorderLayout.NORTH);
        card.add(descriptionArea, BorderLayout.CENTER);

        return card;
    }

    private void showFeatureMessage() {
        JOptionPane.showMessageDialog(
                this,
                "The complete Pharmly suite is available immediately after signing in.",
                "Pharmly System",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
