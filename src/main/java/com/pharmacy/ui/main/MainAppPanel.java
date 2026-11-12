package com.pharmacy.ui.main;

import com.pharmacy.config.AppTheme;
import com.pharmacy.ui.common.ModernIcons;
import com.pharmacy.ui.common.ModernSearchBar;
import com.pharmacy.ui.common.PillButton;
import com.pharmacy.ui.landing.LandingFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * MainAppPanel
 *
 * The authenticated application shell with the Pharmly dark-forest green sidebar,
 * top navigation bar with profile and search, and dynamic card content area.
 */
public class MainAppPanel extends JPanel {

    // Card identifiers for the content area
    private static final String DASHBOARD = "dashboard";
    private static final String MEDICINES = "medicines";
    private static final String INVENTORY = "inventory";
    private static final String EXPIRY = "expiry";
    private static final String SALES = "sales";
    private static final String PURCHASES = "purchases";
    private static final String SUPPLIERS = "suppliers";
    private static final String CUSTOMERS = "customers";
    private static final String REPORTS = "reports";
    private static final String EXPENSES = "expenses";
    private static final String STAFF = "staff";
    private static final String SETTINGS = "settings";

    private final LandingFrame landingFrame;

    private final CardLayout contentCardLayout;
    private final JPanel contentPanel;

    private final JLabel pageTitleLabel;
    private final JLabel pageSubtitleLabel;

    // Sidebar navigation buttons, keyed by card name
    private final Map<String, SidebarNavItem> navButtons = new LinkedHashMap<>();
    private String activeCard = DASHBOARD;

    public MainAppPanel(LandingFrame landingFrame) {
        this.landingFrame = landingFrame;

        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);

        contentCardLayout = new CardLayout();
        contentPanel = new JPanel(contentCardLayout);
        contentPanel.setBackground(AppTheme.BACKGROUND);

        pageTitleLabel = new JLabel();
        pageSubtitleLabel = new JLabel();

        add(buildSidebar(), BorderLayout.WEST);

        JPanel rightSide = new JPanel(new BorderLayout());
        rightSide.setBackground(AppTheme.BACKGROUND);
        rightSide.add(buildTopBar(), BorderLayout.NORTH);
        rightSide.add(buildContentArea(), BorderLayout.CENTER);

        add(rightSide, BorderLayout.CENTER);

        showDashboard();
    }

    // =====================================================================
    // SIDEBAR
    // =====================================================================

    private JComponent buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(AppTheme.SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, AppTheme.SIDEBAR_BORDER));

        sidebar.add(buildSidebarHeader());
        sidebar.add(Box.createVerticalStrut(10));

        // Scrollable nav list
        JPanel navList = new JPanel();
        navList.setLayout(new BoxLayout(navList, BoxLayout.Y_AXIS));
        navList.setOpaque(false);
        navList.setBorder(new EmptyBorder(0, 12, 0, 12));

        navList.add(buildNavSection("MAIN"));
        navList.add(buildNavItem("Dashboard", "grid", DASHBOARD, this::showDashboard));

        navList.add(buildNavSection("INVENTORY"));
        navList.add(buildNavItem("Products", "pill", MEDICINES, this::showMedicines));
        navList.add(buildNavItem("Stock Inventory", "box", INVENTORY, this::showInventory));
        navList.add(buildNavItem("Expiry Alerts", "alert", EXPIRY, this::showExpiryManagement));

        navList.add(buildNavSection("TRANSACTIONS"));
        navList.add(buildNavItem("Sales & Billing", "cart", SALES, this::showSales));
        navList.add(buildNavItem("Purchases", "dollar", PURCHASES, this::showPurchases));

        navList.add(buildNavSection("PEOPLE"));
        navList.add(buildNavItem("Suppliers", "user", SUPPLIERS, this::showSuppliers));
        navList.add(buildNavItem("Customers", "user", CUSTOMERS, this::showCustomers));

        navList.add(buildNavSection("MANAGEMENT"));
        navList.add(buildNavItem("Reports & Analytics", "chart", REPORTS, this::showReports));
        navList.add(buildNavItem("Expenses", "dollar", EXPENSES, this::showExpenses));
        navList.add(buildNavItem("Staff Management", "user", STAFF, this::showStaffManagement));

        navList.add(buildNavSection("SYSTEM"));
        navList.add(buildNavItem("Settings", "settings", SETTINGS, this::showSettings));

        JScrollPane navScroll = new JScrollPane(navList);
        navScroll.setBorder(null);
        navScroll.setOpaque(false);
        navScroll.getViewport().setOpaque(false);
        navScroll.getVerticalScrollBar().setUnitIncrement(16);
        navScroll.getVerticalScrollBar().setPreferredSize(new Dimension(5, 0));
        sidebar.add(navScroll);

        // Upgrade Card & Logout at bottom
        sidebar.add(buildUpgradeCard());
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(buildLogoutItem());
        sidebar.add(Box.createVerticalStrut(12));

        return sidebar;
    }

    private JComponent buildSidebarHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(22, 18, 16, 18));
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));

        JPanel brandArea = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        brandArea.setOpaque(false);

        // Pharmly Pill Icon
        JLabel logoBadge = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppTheme.ACCENT);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));

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
        logoBadge.setPreferredSize(new Dimension(32, 32));

        JLabel brandName = new JLabel("Pharmly");
        brandName.setFont(new Font(AppTheme.TITLE_FONT.getFamily(), Font.BOLD, 20));
        brandName.setForeground(Color.WHITE);

        brandArea.add(logoBadge);
        brandArea.add(brandName);

        // Toggle / collapse icon
        JLabel toggleIcon = new JLabel(ModernIcons.get("grid", 16, AppTheme.SIDEBAR_TEXT));
        toggleIcon.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        header.add(brandArea, BorderLayout.WEST);
        header.add(toggleIcon, BorderLayout.EAST);

        return header;
    }

    private JComponent buildNavSection(String title) {
        JLabel label = new JLabel(title);
        label.setFont(new Font(AppTheme.SMALL_FONT.getFamily(), Font.BOLD, 10));
        label.setForeground(new Color(105, 138, 129));
        label.setBorder(new EmptyBorder(14, 12, 6, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JComponent buildNavItem(String label, String iconName, String cardName, Runnable onClick) {
        SidebarNavItem item = new SidebarNavItem(label, iconName);
        item.addActionListener(e -> {
            onClick.run();
            setActiveNavItem(cardName);
        });
        navButtons.put(cardName, item);
        return item;
    }

    private JComponent buildUpgradeCard() {
        JPanel cardWrapper = new JPanel(new BorderLayout());
        cardWrapper.setOpaque(false);
        cardWrapper.setBorder(new EmptyBorder(8, 14, 8, 14));
        cardWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(20, 56, 49));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 14, 14));
                g2.setColor(new Color(27, 69, 61));
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new RoundRectangle2D.Float(0.6f, 0.6f, getWidth() - 2.2f, getHeight() - 2.2f, 14, 14));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(12, 14, 12, 14));

        JLabel titleLabel = new JLabel("Upgrade Pro", ModernIcons.get("lock", 14, AppTheme.ACCENT), SwingConstants.LEFT);
        titleLabel.setFont(AppTheme.BODY_BOLD_FONT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLabel = new JLabel("<html><div style='color:#9CB8AF;font-size:10px;'>Master your pharmacy with detailed analytics and clear graphs.</div></html>");
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        PillButton upgradeBtn = PillButton.lime("Upgrade Now");
        upgradeBtn.setFont(AppTheme.SMALL_BOLD_FONT);
        upgradeBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        upgradeBtn.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(descLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(upgradeBtn);

        cardWrapper.add(card, BorderLayout.CENTER);
        return cardWrapper;
    }

    private JComponent buildLogoutItem() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(4, 14, 4, 14));
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        JButton logoutButton = new JButton("Logout", ModernIcons.get("logout", 16, new Color(248, 113, 113)));
        logoutButton.setFont(AppTheme.BODY_FONT);
        logoutButton.setHorizontalAlignment(SwingConstants.LEFT);
        logoutButton.setIconTextGap(12);
        logoutButton.setForeground(new Color(248, 113, 113));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setContentAreaFilled(false);
        logoutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        logoutButton.addActionListener(e -> handleLogout());
        wrapper.add(logoutButton, BorderLayout.CENTER);

        return wrapper;
    }

    private void setActiveNavItem(String cardName) {
        activeCard = cardName;
        for (Map.Entry<String, SidebarNavItem> entry : navButtons.entrySet()) {
            entry.getValue().setActive(entry.getKey().equals(cardName));
        }
    }

    // =====================================================================
    // TOP BAR
    // =====================================================================

    private JComponent buildTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(AppTheme.SURFACE);
        topBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER),
                new EmptyBorder(12, 28, 12, 28)
        ));

        // Left Title & Subtitle
        JPanel titleArea = new JPanel();
        titleArea.setLayout(new BoxLayout(titleArea, BoxLayout.Y_AXIS));
        titleArea.setOpaque(false);

        pageTitleLabel.setFont(AppTheme.HEADING_FONT);
        pageTitleLabel.setForeground(AppTheme.TEXT_PRIMARY);
        pageTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        pageSubtitleLabel.setFont(AppTheme.SMALL_FONT);
        pageSubtitleLabel.setForeground(AppTheme.TEXT_MUTED);
        pageSubtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleArea.add(pageTitleLabel);
        titleArea.add(Box.createVerticalStrut(2));
        titleArea.add(pageSubtitleLabel);

        // Center / Right Controls
        JPanel rightArea = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        rightArea.setOpaque(false);

        ModernSearchBar searchBar = new ModernSearchBar("Search...", 18);
        searchBar.setPreferredSize(new Dimension(240, 38));

        // Notification button with badge
        PillButton notifBtn = PillButton.iconCircle(ModernIcons.get("bell", 18, AppTheme.TEXT_SECONDARY), 38);
        notifBtn.setToolTipText("Notifications");

        // Profile Card
        JPanel profilePill = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        profilePill.setOpaque(false);
        profilePill.setBorder(new EmptyBorder(4, 8, 4, 8));

        JLabel avatar = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppTheme.PRIMARY);
                g2.fill(new Ellipse2D.Float(0, 0, getWidth(), getHeight()));
                g2.setColor(AppTheme.ACCENT);
                g2.setFont(new Font(getFont().getFamily(), Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                String initials = "JB";
                int tx = (getWidth() - fm.stringWidth(initials)) / 2;
                int ty = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(initials, tx, ty);
                g2.dispose();
            }
        };
        avatar.setPreferredSize(new Dimension(34, 34));

        JPanel nameBox = new JPanel();
        nameBox.setLayout(new BoxLayout(nameBox, BoxLayout.Y_AXIS));
        nameBox.setOpaque(false);

        JLabel nameLabel = new JLabel("James Bond");
        nameLabel.setFont(AppTheme.BODY_BOLD_FONT);
        nameLabel.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel roleLabel = new JLabel("@james.bond");
        roleLabel.setFont(AppTheme.SMALL_FONT);
        roleLabel.setForeground(AppTheme.TEXT_MUTED);

        nameBox.add(nameLabel);
        nameBox.add(roleLabel);

        profilePill.add(avatar);
        profilePill.add(nameBox);

        rightArea.add(searchBar);
        rightArea.add(notifBtn);
        rightArea.add(profilePill);

        topBar.add(titleArea, BorderLayout.WEST);
        topBar.add(rightArea, BorderLayout.EAST);

        return topBar;
    }

    private void updateTopBar(String title, String subtitle) {
        pageTitleLabel.setText(title);
        pageSubtitleLabel.setText(subtitle);
    }

    // =====================================================================
    // CONTENT AREA
    // =====================================================================

    private JComponent buildContentArea() {
        contentPanel.add(new DashboardPanel(), DASHBOARD);
        contentPanel.add(new MedicinesPanel(), MEDICINES);
        contentPanel.add(new InventoryPanel(), INVENTORY);
        contentPanel.add(new ExpiryManagementPanel(), EXPIRY);
        contentPanel.add(new SalesBillingPanel(), SALES);
        contentPanel.add(new PurchasesPanel(), PURCHASES);
        contentPanel.add(new SuppliersPanel(), SUPPLIERS);
        contentPanel.add(new CustomersPanel(), CUSTOMERS);
        contentPanel.add(new ReportsPanel(), REPORTS);
        contentPanel.add(new ExpensesPanel(), EXPENSES);
        contentPanel.add(new StaffPanel(), STAFF);
        contentPanel.add(new SettingsPanel(), SETTINGS);

        return contentPanel;
    }

    // =====================================================================
    // NAVIGATION METHODS
    // =====================================================================

    public void showDashboard() {
        contentCardLayout.show(contentPanel, DASHBOARD);
        updateTopBar("Dashboard", "Let's check your pharmacy today");
        setActiveNavItem(DASHBOARD);
    }

    public void showMedicines() {
        contentCardLayout.show(contentPanel, MEDICINES);
        updateTopBar("Product List", "Let's check your pharmacy today");
        setActiveNavItem(MEDICINES);
    }

    public void showInventory() {
        contentCardLayout.show(contentPanel, INVENTORY);
        updateTopBar("Inventory", "Track stock levels & batch items");
        setActiveNavItem(INVENTORY);
    }

    public void showExpiryManagement() {
        contentCardLayout.show(contentPanel, EXPIRY);
        updateTopBar("Expiry Management", "Monitor near-expiry stock & urgent items");
        setActiveNavItem(EXPIRY);
    }

    public void showSales() {
        contentCardLayout.show(contentPanel, SALES);
        updateTopBar("Sales & Billing", "Create bills quickly and manage customer sales");
        setActiveNavItem(SALES);
    }

    public void showPurchases() {
        contentCardLayout.show(contentPanel, PURCHASES);
        updateTopBar("Purchases", "Manage purchase orders and supplier invoices");
        setActiveNavItem(PURCHASES);
    }

    public void showSuppliers() {
        contentCardLayout.show(contentPanel, SUPPLIERS);
        updateTopBar("Suppliers", "Manage supplier directory and accounts");
        setActiveNavItem(SUPPLIERS);
    }

    public void showCustomers() {
        contentCardLayout.show(contentPanel, CUSTOMERS);
        updateTopBar("Customers", "Customer profiles and purchase histories");
        setActiveNavItem(CUSTOMERS);
    }

    public void showReports() {
        contentCardLayout.show(contentPanel, REPORTS);
        updateTopBar("Reports & Analytics", "View comprehensive pharmacy performance insights");
        setActiveNavItem(REPORTS);
    }

    public void showExpenses() {
        contentCardLayout.show(contentPanel, EXPENSES);
        updateTopBar("Expenses", "Track pharmacy operations spending & bills");
        setActiveNavItem(EXPENSES);
    }

    public void showStaffManagement() {
        contentCardLayout.show(contentPanel, STAFF);
        updateTopBar("Staff Management", "Manage pharmacy staff and permissions");
        setActiveNavItem(STAFF);
    }

    public void showSettings() {
        contentCardLayout.show(contentPanel, SETTINGS);
        updateTopBar("Settings", "Configure application preferences & profile");
        setActiveNavItem(SETTINGS);
    }

    // =====================================================================
    // LOGOUT
    // =====================================================================

    private void handleLogout() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            performLogout();
        }
    }

    private void performLogout() {
        landingFrame.showLogin();
    }

    // =====================================================================
    // SIDEBAR NAV ITEM COMPONENT
    // =====================================================================

    private static class SidebarNavItem extends JButton {
        private final String iconName;
        private boolean isActive = false;
        private boolean isHovered = false;

        SidebarNavItem(String label, String iconName) {
            super(label);
            this.iconName = iconName;

            setFont(AppTheme.BODY_FONT);
            setHorizontalAlignment(SwingConstants.LEFT);
            setAlignmentX(Component.LEFT_ALIGNMENT);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
            setBorder(new EmptyBorder(9, 14, 9, 14));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setIconTextGap(12);

            updateIconAndColors();

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
            });
        }

        void setActive(boolean active) {
            this.isActive = active;
            updateIconAndColors();
            repaint();
        }

        private void updateIconAndColors() {
            if (isActive) {
                setForeground(AppTheme.SIDEBAR_ACTIVE_TEXT);
                setFont(AppTheme.BODY_BOLD_FONT);
                setIcon(ModernIcons.get(iconName, 17, AppTheme.SIDEBAR_ACTIVE_TEXT));
            } else {
                setForeground(AppTheme.SIDEBAR_TEXT);
                setFont(AppTheme.BODY_FONT);
                setIcon(ModernIcons.get(iconName, 17, AppTheme.SIDEBAR_TEXT));
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            if (isActive) {
                g2.setColor(AppTheme.SIDEBAR_ACTIVE_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 12, 12));
            } else if (isHovered) {
                g2.setColor(AppTheme.SIDEBAR_HOVER);
                g2.fill(new RoundRectangle2D.Float(0, 0, w, h, 12, 12));
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }
}
