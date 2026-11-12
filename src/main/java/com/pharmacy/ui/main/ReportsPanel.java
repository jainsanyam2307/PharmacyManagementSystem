package com.pharmacy.ui.main;

import com.pharmacy.config.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import java.util.ArrayList;

/**
 * ReportsPanel
 *
 * Displays pharmacy business insights: sales/purchases/expenses summary,
 * a lightweight custom-painted sales chart, top selling medicines,
 * payment overview and additional analytics.
 *
 * UI-only. Uses realistic sample/in-memory data. No database, DAO,
 * service, or JDBC code is used in this phase.
 */
public class ReportsPanel extends JPanel {

    private JComboBox<String> dateRangeCombo;
    private SalesChartPanel salesChartPanel;

    // Summary value labels, refreshed when the date range changes.
    private JLabel totalSalesValue;
    private JLabel totalPurchasesValue;
    private JLabel totalExpensesValue;
    private JLabel netProfitValue;

    public ReportsPanel() {
        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildScrollableContent(), BorderLayout.CENTER);

        // Populate initial values for the default date range.
        refreshData();
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

        JLabel title = new JLabel("Reports & Analytics");
        title.setFont(AppTheme.HEADING_FONT);
        title.setForeground(AppTheme.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Business insights across sales, purchases, expenses and stock");
        subtitle.setFont(AppTheme.SMALL_FONT);
        subtitle.setForeground(AppTheme.TEXT_MUTED);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        titleArea.add(title);
        titleArea.add(Box.createVerticalStrut(4));
        titleArea.add(subtitle);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controls.setOpaque(false);

        dateRangeCombo = new JComboBox<>(new String[]{"Today", "This Week", "This Month", "This Year"});
        dateRangeCombo.setSelectedItem("This Month");
        dateRangeCombo.setFont(AppTheme.BODY_FONT);
        dateRangeCombo.setFocusable(false);
        dateRangeCombo.addActionListener(e -> refreshData());

        JButton refreshButton = createSecondaryButton("Refresh");
        refreshButton.addActionListener(e -> refreshData());

        JButton exportButton = createPrimaryButton("Export Report");
        exportButton.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "Export functionality will be connected later.",
                "Export Report",
                JOptionPane.INFORMATION_MESSAGE
        ));

        controls.add(dateRangeCombo);
        controls.add(refreshButton);
        controls.add(exportButton);

        header.add(titleArea, BorderLayout.WEST);
        header.add(controls, BorderLayout.EAST);

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

        content.add(buildSummaryCardsRow());
        content.add(Box.createVerticalStrut(20));
        content.add(buildSalesOverviewSection());
        content.add(Box.createVerticalStrut(20));
        content.add(buildTopSellingSection());
        content.add(Box.createVerticalStrut(20));
        content.add(buildPaymentOverviewSection());
        content.add(Box.createVerticalStrut(20));
        content.add(buildAdditionalAnalyticsSection());

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(AppTheme.BACKGROUND);
        scrollPane.getViewport().setBackground(AppTheme.BACKGROUND);

        return scrollPane;
    }

    // =====================================================================
    // SUMMARY CARDS
    // =====================================================================

    private JComponent buildSummaryCardsRow() {
        JPanel row = new JPanel(new GridLayout(1, 4, 16, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        totalSalesValue = new JLabel();
        totalPurchasesValue = new JLabel();
        totalExpensesValue = new JLabel();
        netProfitValue = new JLabel();

        row.add(buildSummaryCard("Total Sales", totalSalesValue, AppTheme.PRIMARY));
        row.add(buildSummaryCard("Total Purchases", totalPurchasesValue, AppTheme.WARNING));
        row.add(buildSummaryCard("Total Expenses", totalExpensesValue, AppTheme.DANGER));
        row.add(buildSummaryCard("Net Profit", netProfitValue, AppTheme.SUCCESS));

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
    // SALES OVERVIEW (custom-painted chart, no external libraries)
    // =====================================================================

    private JComponent buildSalesOverviewSection() {
        JPanel section = createSectionContainer("Sales Overview", "Visual summary of sales for the selected period");
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        salesChartPanel = new SalesChartPanel();
        salesChartPanel.setPreferredSize(new Dimension(100, 210));
        salesChartPanel.setOpaque(false);

        section.add(Box.createVerticalStrut(12));
        section.add(salesChartPanel);

        return section;
    }

    /**
     * Lightweight custom-painted bar chart. Avoids external charting
     * libraries entirely, keeping the module fast and dependency-free.
     */
    private class SalesChartPanel extends JPanel {

        private String[] labels = new String[0];
        private double[] values = new double[0];

        void setData(String[] labels, double[] values) {
            this.labels = labels;
            this.values = values;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (values.length == 0) {
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int bottomMargin = 26;
            int topMargin = 10;
            int chartHeight = height - bottomMargin - topMargin;

            double maxValue = 0;
            for (double v : values) {
                maxValue = Math.max(maxValue, v);
            }
            if (maxValue <= 0) {
                maxValue = 1;
            }

            int barCount = values.length;
            int gap = 18;
            int barWidth = Math.max(14, (width - gap * (barCount + 1)) / barCount);

            int x = gap;
            for (int i = 0; i < barCount; i++) {
                int barHeight = (int) ((values[i] / maxValue) * chartHeight);
                int y = topMargin + (chartHeight - barHeight);

                RoundRectangle2D bar = new RoundRectangle2D.Double(x, y, barWidth, barHeight, 8, 8);
                g2.setColor(AppTheme.PRIMARY);
                g2.fill(bar);

                g2.setColor(AppTheme.TEXT_MUTED);
                g2.setFont(AppTheme.SMALL_FONT);
                FontMetrics fm = g2.getFontMetrics();
                String label = labels[i];
                int labelWidth = fm.stringWidth(label);
                g2.drawString(label, x + (barWidth - labelWidth) / 2, height - 8);

                x += barWidth + gap;
            }

            g2.dispose();
        }
    }

    // =====================================================================
    // TOP SELLING MEDICINES
    // =====================================================================

    private JComponent buildTopSellingSection() {
        JPanel section = createSectionContainer("Top Selling Medicines", "Best performing products for the selected period");

        String[] columns = {"Medicine", "Category", "Units Sold", "Revenue"};

        Object[][] data = {
                {"Paracetamol 500mg", "Pain Relief", 420, "₹12,600"},
                {"Amoxicillin 250mg", "Antibiotics", 310, "₹18,600"},
                {"Cetirizine 10mg", "Allergy", 275, "₹5,500"},
                {"Vitamin C 1000mg", "Supplements", 260, "₹7,800"},
                {"Omeprazole 20mg", "Gastric Care", 190, "₹9,500"},
                {"Cough Syrup 100ml", "Cold & Cough", 165, "₹6,600"}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        styleTable(table);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER));
        tableScroll.setPreferredSize(new Dimension(100, 210));
        tableScroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        section.add(Box.createVerticalStrut(12));
        section.add(tableScroll);

        return section;
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
    // PAYMENT OVERVIEW
    // =====================================================================

    private JComponent buildPaymentOverviewSection() {
        JPanel section = createSectionContainer("Payment Overview", "Breakdown of sales by payment status");

        JPanel row = new JPanel(new GridLayout(1, 3, 16, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        row.add(buildPaymentStatusCard("Paid", "₹1,84,200", AppTheme.SUCCESS));
        row.add(buildPaymentStatusCard("Pending", "₹23,400", AppTheme.DANGER));
        row.add(buildPaymentStatusCard("Partial", "₹11,850", AppTheme.WARNING));

        section.add(Box.createVerticalStrut(12));
        section.add(row);

        return section;
    }

    private JComponent buildPaymentStatusCard(String label, String amount, Color accent) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(AppTheme.SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER),
                new EmptyBorder(14, 16, 14, 16)
        ));

        JPanel dot = new JPanel();
        dot.setBackground(accent);
        dot.setPreferredSize(new Dimension(10, 10));

        JPanel dotWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        dotWrapper.setOpaque(false);
        dotWrapper.add(dot);

        JPanel textArea = new JPanel();
        textArea.setLayout(new BoxLayout(textArea, BoxLayout.Y_AXIS));
        textArea.setOpaque(false);

        JLabel labelText = new JLabel(label);
        labelText.setFont(AppTheme.SMALL_FONT);
        labelText.setForeground(AppTheme.TEXT_MUTED);
        labelText.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel amountText = new JLabel(amount);
        amountText.setFont(AppTheme.SUBHEADING_FONT);
        amountText.setForeground(AppTheme.TEXT_PRIMARY);
        amountText.setAlignmentX(Component.LEFT_ALIGNMENT);

        textArea.add(labelText);
        textArea.add(Box.createVerticalStrut(4));
        textArea.add(amountText);

        card.add(dotWrapper, BorderLayout.WEST);
        card.add(textArea, BorderLayout.CENTER);

        return card;
    }

    // =====================================================================
    // ADDITIONAL ANALYTICS
    // =====================================================================

    private JComponent buildAdditionalAnalyticsSection() {
        JPanel section = createSectionContainer("Additional Analytics", "Key operational indicators at a glance");

        JPanel grid = new JPanel(new GridLayout(1, 4, 16, 0));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        grid.add(buildMetricCard("Low Stock Items", "14", AppTheme.WARNING));
        grid.add(buildMetricCard("Expiring Medicines", "7", AppTheme.DANGER));
        grid.add(buildMetricCard("Customers", "312", AppTheme.PRIMARY));
        grid.add(buildMetricCard("Suppliers", "28", AppTheme.SUCCESS));

        section.add(Box.createVerticalStrut(12));
        section.add(grid);

        return section;
    }

    private JComponent buildMetricCard(String label, String value, Color accent) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(AppTheme.SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER),
                new EmptyBorder(14, 16, 14, 16)
        ));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(AppTheme.HEADING_FONT);
        valueLabel.setForeground(accent);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel labelText = new JLabel(label);
        labelText.setFont(AppTheme.SMALL_FONT);
        labelText.setForeground(AppTheme.TEXT_MUTED);
        labelText.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(valueLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(labelText);

        return card;
    }

    // =====================================================================
    // SHARED SECTION CONTAINER
    // =====================================================================

    private JPanel createSectionContainer(String title, String subtitle) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(AppTheme.BACKGROUND);
        section.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(AppTheme.SUBHEADING_FONT);
        titleLabel.setForeground(AppTheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(AppTheme.SMALL_FONT);
        subtitleLabel.setForeground(AppTheme.TEXT_MUTED);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        section.add(titleLabel);
        section.add(Box.createVerticalStrut(2));
        section.add(subtitleLabel);

        return section;
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

    // =====================================================================
    // SAMPLE DATA REFRESH
    // =====================================================================

    /**
     * Refreshes summary values and the sales chart based on the selected
     * date range. Uses realistic in-memory sample data only.
     */
    private void refreshData() {
        String range = (String) dateRangeCombo.getSelectedItem();
        if (range == null) {
            range = "This Month";
        }

        double totalSales;
        double totalPurchases;
        double totalExpenses;
        String[] chartLabels;
        double[] chartValues;

        switch (range) {
            case "Today":
                totalSales = 8400;
                totalPurchases = 5200;
                totalExpenses = 900;
                chartLabels = new String[]{"9AM", "12PM", "3PM", "6PM", "9PM"};
                chartValues = new double[]{1200, 2100, 1800, 2400, 900};
                break;
            case "This Week":
                totalSales = 52300;
                totalPurchases = 31800;
                totalExpenses = 4200;
                chartLabels = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
                chartValues = new double[]{6200, 7100, 5800, 8400, 9200, 10600, 5000};
                break;
            case "This Year":
                totalSales = 1842000;
                totalPurchases = 1120000;
                totalExpenses = 168000;
                chartLabels = new String[]{"Q1", "Q2", "Q3", "Q4"};
                chartValues = new double[]{412000, 468000, 455000, 507000};
                break;
            case "This Month":
            default:
                totalSales = 219450;
                totalPurchases = 138200;
                totalExpenses = 18600;
                chartLabels = new String[]{"Wk 1", "Wk 2", "Wk 3", "Wk 4"};
                chartValues = new double[]{48200, 56100, 52300, 62850};
                break;
        }

        double netProfit = totalSales - totalPurchases - totalExpenses;

        totalSalesValue.setText(formatCurrency(totalSales));
        totalPurchasesValue.setText(formatCurrency(totalPurchases));
        totalExpensesValue.setText(formatCurrency(totalExpenses));
        netProfitValue.setText(formatCurrency(netProfit));
        netProfitValue.setForeground(netProfit >= 0 ? AppTheme.SUCCESS : AppTheme.DANGER);

        if (salesChartPanel != null) {
            salesChartPanel.setData(chartLabels, chartValues);
        }
    }

    private String formatCurrency(double amount) {
        return String.format("₹%,.0f", amount);
    }
}