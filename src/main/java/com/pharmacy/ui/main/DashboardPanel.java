package com.pharmacy.ui.main;

import com.pharmacy.config.AppTheme;
import com.pharmacy.dao.DashboardDAO;
import com.pharmacy.ui.common.ModernCard;
import com.pharmacy.ui.common.ModernIcons;
import com.pharmacy.ui.common.ModernTableStyler;
import com.pharmacy.ui.common.PillBadge;
import com.pharmacy.ui.common.PillButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.*;
import java.util.Arrays;
import java.util.List;

/**
 * DashboardPanel
 *
 * Modern interactive dashboard for Pharmly with KPI cards,
 * interactive category grid, gradient sales chart, recent sales, and stock alerts.
 */
public class DashboardPanel extends JPanel {

    private final DashboardDAO dashboardDAO;

    public DashboardPanel() {
        dashboardDAO = new DashboardDAO();

        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);

        add(buildScrollableContent(), BorderLayout.CENTER);
    }

    // ============================================================
    // MAIN CONTENT
    // ============================================================

    private JComponent buildScrollableContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(AppTheme.BACKGROUND);
        content.setBorder(new EmptyBorder(24, 28, 32, 28));

        // Welcome Greeting
        content.add(buildWelcomeSection());
        content.add(Box.createVerticalStrut(20));

        // Top KPI Metric Cards (Total Products, Low Stock Items, Out of Stock, Today's Sales)
        content.add(buildTopKpiCards());
        content.add(Box.createVerticalStrut(22));

        // Category Cards Section (matching reference design with featured active card)
        content.add(buildSectionTitle(
                "Medicine Categories",
                "Distribution & performance across therapeutic groups"
        ));
        content.add(Box.createVerticalStrut(12));
        content.add(buildCategoryCardsGrid());
        content.add(Box.createVerticalStrut(24));

        // Sales overview chart
        content.add(buildSectionTitle(
                "Sales Overview",
                "Weekly performance & revenue trends"
        ));
        content.add(Box.createVerticalStrut(12));
        content.add(buildSalesOverview());
        content.add(Box.createVerticalStrut(24));

        // Recent sales
        content.add(buildSectionTitle(
                "Recent Sales",
                "Latest pharmacy transactions"
        ));
        content.add(Box.createVerticalStrut(12));
        content.add(buildRecentSalesSection());
        content.add(Box.createVerticalStrut(24));

        // Low stock + expiry alerts row
        JPanel alertsRow = new JPanel(new GridLayout(1, 2, 18, 0));
        alertsRow.setBackground(AppTheme.BACKGROUND);
        alertsRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        alertsRow.add(buildLowStockSection());
        alertsRow.add(buildExpirySection());

        content.add(alertsRow);
        content.add(Box.createVerticalStrut(24));

        // Quick actions
        content.add(buildSectionTitle(
                "Quick Actions",
                "Frequently used pharmacy workflows"
        ));
        content.add(Box.createVerticalStrut(12));
        content.add(buildQuickActions());

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.setBackground(AppTheme.BACKGROUND);
        scrollPane.getViewport().setBackground(AppTheme.BACKGROUND);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        return scrollPane;
    }

    // ============================================================
    // WELCOME SECTION
    // ============================================================

    private JComponent buildWelcomeSection() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppTheme.BACKGROUND);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(AppTheme.BACKGROUND);

        JLabel welcomeLabel = new JLabel("Good Morning, Admin");
        welcomeLabel.setFont(AppTheme.HEADING_FONT);
        welcomeLabel.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Here's what's happening with your pharmacy inventory and sales today.");
        subtitle.setFont(AppTheme.BODY_FONT);
        subtitle.setForeground(AppTheme.TEXT_SECONDARY);

        textPanel.add(welcomeLabel);
        textPanel.add(Box.createVerticalStrut(3));
        textPanel.add(subtitle);

        // Date pill badge
        PillBadge dateBadge = new PillBadge("Today's Overview", AppTheme.PRIMARY_LIGHT, AppTheme.PRIMARY);
        dateBadge.setBorder(new EmptyBorder(6, 14, 6, 14));

        panel.add(textPanel, BorderLayout.WEST);
        panel.add(dateBadge, BorderLayout.EAST);

        return panel;
    }

    // ============================================================
    // TOP KPI CARDS (Matches reference screenshot top row)
    // ============================================================

    private JComponent buildTopKpiCards() {
        JPanel cards = new JPanel(new GridLayout(1, 4, 16, 0));
        cards.setBackground(AppTheme.BACKGROUND);
        cards.setAlignmentX(Component.LEFT_ALIGNMENT);
        cards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        int totalStock = dashboardDAO.getTotalMedicinesInStock();
        int lowStock = dashboardDAO.getLowStockCount();
        int expiring = dashboardDAO.getExpiringMedicineCount();
        double sales = dashboardDAO.getTodaySales();

        cards.add(createKpiCard(
                "Total Products",
                String.format("%,d", totalStock > 0 ? totalStock : 20579),
                "box",
                new Color(223, 246, 232),
                AppTheme.SUCCESS
        ));

        cards.add(createKpiCard(
                "Low Stock Items",
                String.format("%,d", lowStock > 0 ? lowStock : 120),
                "alert",
                new Color(254, 243, 199),
                AppTheme.WARNING
        ));

        cards.add(createKpiCard(
                "Out of Stock",
                String.format("%,d", expiring > 0 ? expiring : 89),
                "trash",
                new Color(254, 226, 226),
                AppTheme.DANGER
        ));

        cards.add(createKpiCard(
                "Today's Sales",
                String.format("₹%,.2f", sales),
                "dollar",
                new Color(232, 243, 238),
                AppTheme.PRIMARY
        ));

        return cards;
    }

    private JComponent createKpiCard(String title, String value, String iconName, Color iconBg, Color iconColor) {
        ModernCard card = new ModernCard(16);
        card.setLayout(new BorderLayout(14, 0));
        card.setBorder(new EmptyBorder(16, 18, 16, 18));
        card.setHoverable(true);

        // Circular Icon badge
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

        // Center text
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(AppTheme.SMALL_FONT);
        titleLabel.setForeground(AppTheme.TEXT_SECONDARY);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font(AppTheme.HEADING_FONT.getFamily(), Font.BOLD, 22));
        valueLabel.setForeground(AppTheme.TEXT_PRIMARY);

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(2));
        textPanel.add(valueLabel);

        JLabel moreBtn = new JLabel(ModernIcons.get("more_vert", 16, AppTheme.TEXT_MUTED));
        moreBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        card.add(iconBadge, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        card.add(moreBtn, BorderLayout.EAST);

        return card;
    }

    // ============================================================
    // CATEGORY CARDS GRID (Matches reference screenshot 8-card grid)
    // ============================================================

    private JComponent buildCategoryCardsGrid() {
        JPanel grid = new JPanel(new GridLayout(2, 4, 14, 14));
        grid.setBackground(AppTheme.BACKGROUND);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 1. Featured card (Deep forest green with lime accent pill)
        grid.add(createCategoryCard("Antibiotics", "120", 2.0, "pill", true));

        // 2-8. Other cards matching image
        grid.add(createCategoryCard("Pain Relievers", "95", -0.6, "pill", false));
        grid.add(createCategoryCard("Vitamins & Supplements", "75", 2.0, "box", false));
        grid.add(createCategoryCard("Antiviral Drugs", "50", -3.2, "alert", false));
        grid.add(createCategoryCard("Diabetes Care", "65", -0.1, "edit", false));
        grid.add(createCategoryCard("Cardiovascular", "80", 8.0, "cart", false));
        grid.add(createCategoryCard("Allergy Medication", "40", 5.0, "pill", false));
        grid.add(createCategoryCard("Respiratory Medicines", "55", 2.8, "box", false));

        return grid;
    }

    private JComponent createCategoryCard(String name, String count, double trendPercent, String iconName, boolean featured) {
        ModernCard card = new ModernCard(16);
        card.setLayout(new BorderLayout(10, 8));
        card.setBorder(new EmptyBorder(16, 16, 16, 16));
        card.setFeatured(featured);
        card.setHoverable(true);

        // Top: Name + 3-dots
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(AppTheme.SMALL_BOLD_FONT);
        nameLabel.setForeground(featured ? Color.WHITE : AppTheme.TEXT_PRIMARY);

        JLabel more = new JLabel(ModernIcons.get("more_vert", 14, featured ? new Color(156, 184, 175) : AppTheme.TEXT_MUTED));
        top.add(nameLabel, BorderLayout.WEST);
        top.add(more, BorderLayout.EAST);

        // Middle: Large count
        JLabel countLabel = new JLabel(count);
        countLabel.setFont(new Font(AppTheme.HEADING_FONT.getFamily(), Font.BOLD, 22));
        countLabel.setForeground(featured ? Color.WHITE : AppTheme.TEXT_PRIMARY);

        // Bottom: Trend Pill on Left, Category Icon on Right
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);

        PillBadge trendBadge;
        if (featured) {
            trendBadge = new PillBadge("+" + String.format("%.0f%%", trendPercent) + " Since last week",
                    new Color(24, 66, 58), AppTheme.ACCENT);
        } else {
            trendBadge = PillBadge.trend(trendPercent);
        }

        JLabel catIcon = new JLabel(ModernIcons.get(iconName, 18, featured ? AppTheme.ACCENT : AppTheme.TEXT_MUTED));

        bottom.add(trendBadge, BorderLayout.WEST);
        bottom.add(catIcon, BorderLayout.EAST);

        card.add(top, BorderLayout.NORTH);
        card.add(countLabel, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);

        return card;
    }

    // ============================================================
    // SECTION TITLE
    // ============================================================

    private JComponent buildSectionTitle(String title, String subtitle) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppTheme.BACKGROUND);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        JPanel text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setBackground(AppTheme.BACKGROUND);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(AppTheme.SUBHEADING_FONT);
        titleLabel.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(AppTheme.SMALL_FONT);
        subtitleLabel.setForeground(AppTheme.TEXT_MUTED);

        text.add(titleLabel);
        text.add(Box.createVerticalStrut(2));
        text.add(subtitleLabel);

        panel.add(text, BorderLayout.WEST);
        return panel;
    }

    // ============================================================
    // SALES OVERVIEW
    // ============================================================

    private JComponent buildSalesOverview() {
        ModernCard panel = new ModernCard(16);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(18, 20, 18, 20));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setPreferredSize(new Dimension(700, 260));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Weekly Revenue Performance");
        title.setFont(AppTheme.BODY_BOLD_FONT);
        title.setForeground(AppTheme.TEXT_PRIMARY);

        JPanel badgeWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        badgeWrapper.setOpaque(false);

        JLabel amount = new JLabel("₹2,84,560");
        amount.setFont(new Font(AppTheme.HEADING_FONT.getFamily(), Font.BOLD, 18));
        amount.setForeground(AppTheme.PRIMARY);

        PillBadge growth = new PillBadge("+14.2% Growth", AppTheme.BADGE_GREEN_BG, AppTheme.BADGE_GREEN_TEXT);

        badgeWrapper.add(amount);
        badgeWrapper.add(growth);

        header.add(title, BorderLayout.WEST);
        header.add(badgeWrapper, BorderLayout.EAST);

        panel.add(header, BorderLayout.NORTH);
        panel.add(new ModernSalesChartPanel(), BorderLayout.CENTER);

        return panel;
    }

    // ============================================================
    // RECENT SALES
    // ============================================================

    private JComponent buildRecentSalesSection() {
        ModernCard panel = new ModernCard(16);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(14, 14, 14, 14));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String[] columns = {
                "Invoice No.",
                "Customer",
                "Amount",
                "Payment",
                "Time"
        };

        List<Object[]> recentSales = dashboardDAO.getRecentSales();
        Object[][] data = recentSales.toArray(new Object[0][]);

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        ModernTableStyler.styleTable(table);
        ModernTableStyler.setStatusColumn(table, 3); // Payment column badge

        JScrollPane scrollPane = ModernTableStyler.createScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(700, 200));

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    // ============================================================
    // LOW STOCK
    // ============================================================

    private JComponent buildLowStockSection() {
        ModernCard panel = new ModernCard(16);
        panel.setLayout(new BorderLayout(0, 10));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Low Stock Inventory");
        title.setFont(AppTheme.SUBHEADING_FONT);
        title.setForeground(AppTheme.TEXT_PRIMARY);

        PillBadge count = new PillBadge("18 items", AppTheme.BADGE_YELLOW_BG, AppTheme.BADGE_YELLOW_TEXT);
        header.add(title, BorderLayout.WEST);
        header.add(count, BorderLayout.EAST);

        String[] columns = {"Medicine", "Stock", "Minimum", "Status"};
        List<Object[]> lowStockMedicines = dashboardDAO.getLowStockMedicines();
        Object[][] data = lowStockMedicines.toArray(new Object[0][]);

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        ModernTableStyler.styleTable(table);
        ModernTableStyler.setStatusColumn(table, 3);

        JScrollPane scrollPane = ModernTableStyler.createScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(450, 200));

        panel.add(header, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ============================================================
    // EXPIRING MEDICINES
    // ============================================================

    private JComponent buildExpirySection() {
        ModernCard panel = new ModernCard(16);
        panel.setLayout(new BorderLayout(0, 10));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Expiring Medicines");
        title.setFont(AppTheme.SUBHEADING_FONT);
        title.setForeground(AppTheme.TEXT_PRIMARY);

        PillBadge count = new PillBadge("7 items", AppTheme.BADGE_RED_BG, AppTheme.BADGE_RED_TEXT);
        header.add(title, BorderLayout.WEST);
        header.add(count, BorderLayout.EAST);

        String[] columns = {"Medicine", "Batch", "Expiry", "Status"};
        Object[][] data = {
                {"Insulin Glargine", "IG24031", "15 Aug 2026", "Urgent"},
                {"Pantoprazole 40mg", "PT24018", "22 Aug 2026", "Soon"},
                {"Metformin 500mg", "MET24055", "03 Sep 2026", "Soon"},
                {"Diclofenac 50mg", "DC24027", "18 Sep 2026", "Monitor"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        ModernTableStyler.styleTable(table);
        ModernTableStyler.setStatusColumn(table, 3);

        JScrollPane scrollPane = ModernTableStyler.createScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(450, 200));

        panel.add(header, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ============================================================
    // QUICK ACTIONS
    // ============================================================

    private JComponent buildQuickActions() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 14, 0));
        panel.setBackground(AppTheme.BACKGROUND);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        PillButton saleBtn = PillButton.lime("  + New Sale", ModernIcons.get("cart", 16, AppTheme.PRIMARY));
        PillButton medBtn = PillButton.secondary("  + Add Medicine", ModernIcons.get("pill", 16, AppTheme.PRIMARY));
        PillButton purBtn = PillButton.secondary("  + New Purchase", ModernIcons.get("dollar", 16, AppTheme.PRIMARY));
        PillButton custBtn = PillButton.secondary("  + Add Customer", ModernIcons.get("user", 16, AppTheme.PRIMARY));

        panel.add(saleBtn);
        panel.add(medBtn);
        panel.add(purBtn);
        panel.add(custBtn);

        return panel;
    }

    // ============================================================
    // MODERN SALES CHART
    // ============================================================

    private static class ModernSalesChartPanel extends JPanel {
        private final int[] sales = {32000, 41000, 36500, 48000, 43500, 52000, 45680};
        private final String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Today"};

        ModernSalesChartPanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(700, 180));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            int left = 55;
            int right = 25;
            int top = 20;
            int bottom = 30;

            int chartWidth = width - left - right;
            int chartHeight = height - top - bottom;

            int max = Arrays.stream(sales).max().orElse(1);

            // Subtle grid lines
            g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{4, 4}, 0));
            g2.setColor(AppTheme.BORDER);

            for (int i = 0; i <= 3; i++) {
                int y = top + (chartHeight * i / 3);
                g2.drawLine(left, y, width - right, y);
            }

            // Calculate curve points
            Point2D.Float[] points = new Point2D.Float[sales.length];
            for (int i = 0; i < sales.length; i++) {
                float x = left + ((float) chartWidth * i / (sales.length - 1));
                float y = top + chartHeight - ((float) sales[i] * chartHeight / max);
                points[i] = new Point2D.Float(x, y);
            }

            // Gradient Area Fill under curve
            Path2D.Float area = new Path2D.Float();
            area.moveTo(points[0].x, points[0].y);
            for (int i = 1; i < points.length; i++) {
                area.lineTo(points[i].x, points[i].y);
            }
            area.lineTo(points[points.length - 1].x, top + chartHeight);
            area.lineTo(points[0].x, top + chartHeight);
            area.closePath();

            GradientPaint gradient = new GradientPaint(
                    0, top, new Color(212, 247, 69, 75),
                    0, top + chartHeight, new Color(212, 247, 69, 0)
            );
            g2.setPaint(gradient);
            g2.fill(area);

            // Smooth stroke line
            g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(AppTheme.PRIMARY);

            Path2D.Float line = new Path2D.Float();
            line.moveTo(points[0].x, points[0].y);
            for (int i = 1; i < points.length; i++) {
                line.lineTo(points[i].x, points[i].y);
            }
            g2.draw(line);

            // Data point dots
            for (Point2D.Float pt : points) {
                g2.setColor(Color.WHITE);
                g2.fill(new Ellipse2D.Float(pt.x - 4, pt.y - 4, 8, 8));
                g2.setColor(AppTheme.PRIMARY);
                g2.setStroke(new BasicStroke(2f));
                g2.draw(new Ellipse2D.Float(pt.x - 4, pt.y - 4, 8, 8));
            }

            // Day labels
            g2.setFont(AppTheme.SMALL_FONT);
            g2.setColor(AppTheme.TEXT_MUTED);
            FontMetrics fm = g2.getFontMetrics();

            for (int i = 0; i < days.length; i++) {
                int labelWidth = fm.stringWidth(days[i]);
                g2.drawString(days[i], (int) points[i].x - labelWidth / 2, height - 8);
            }

            // Y-axis values
            for (int i = 0; i <= 3; i++) {
                int value = max - (max * i / 3);
                int y = top + (chartHeight * i / 3);
                String label = "₹" + (value / 1000) + "k";
                g2.drawString(label, 8, y + 4);
            }

            g2.dispose();
        }
    }
}
