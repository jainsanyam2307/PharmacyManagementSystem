package com.pharmacy.ui.main;

import com.pharmacy.config.AppTheme;
import com.pharmacy.dao.SalesDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class SalesHistoryPanel extends JPanel {

    private final Color background = AppTheme.BACKGROUND;
    private final Color surface = AppTheme.SURFACE;
    private final Color border = AppTheme.BORDER;
    private final Color textPrimary = AppTheme.TEXT_PRIMARY;
    private final Color textSecondary = AppTheme.TEXT_SECONDARY;

    private final SalesDAO salesDAO =
            new SalesDAO();

    private final java.text.DecimalFormat currencyFormat =
            new java.text.DecimalFormat("₹#,##0.00");

    private JTable salesTable;
    private DefaultTableModel salesTableModel;

    private JTextField searchField;

    public SalesHistoryPanel() {

        setLayout(
                new BorderLayout()
        );

        setBackground(
                background
        );

        buildUI();

        loadSalesHistory();
    }

    // ============================================================
    // BUILD UI
    // ============================================================

    private void buildUI() {

        add(
                buildHeader(),
                BorderLayout.NORTH
        );

        add(
                buildTablePanel(),
                BorderLayout.CENTER
        );
    }

    // ============================================================
    // HEADER
    // ============================================================

    private JPanel buildHeader() {

        JPanel header =
                new JPanel(
                        new BorderLayout(16, 0)
                );

        header.setBackground(
                background
        );

        header.setBorder(
                new EmptyBorder(
                        24,
                        28,
                        16,
                        28
                )
        );

        JLabel title =
                new JLabel(
                        "Sales History"
                );

        title.setFont(
                AppTheme.HEADING_FONT
        );

        title.setForeground(
                textPrimary
        );

        JLabel subtitle =
                new JLabel(
                        "View previously generated invoices"
                );

        subtitle.setFont(
                AppTheme.SMALL_FONT
        );

        subtitle.setForeground(
                textSecondary
        );

        JPanel titlePanel =
                new JPanel(
                        new GridLayout(
                                2,
                                1,
                                0,
                                4
                        )
                );

        titlePanel.setBackground(
                background
        );

        titlePanel.add(title);
        titlePanel.add(subtitle);

        // --------------------------------------------------------
        // SEARCH
        // --------------------------------------------------------

        searchField =
                new JTextField();

        searchField.setPreferredSize(
                new Dimension(
                        260,
                        38
                )
        );

        searchField.setFont(
                AppTheme.SMALL_FONT
        );

        searchField.setForeground(
                textPrimary
        );

        searchField.setBackground(
                surface
        );

        searchField.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                border
                        ),
                        new EmptyBorder(
                                8,
                                12,
                                8,
                                12
                        )
                )
        );

        searchField.setToolTipText(
                "Search by invoice number"
        );

        JButton searchButton =
                new JButton(
                        "Search"
                );

        searchButton.setFont(
                AppTheme.SMALL_FONT
        );

        searchButton.setFocusPainted(
                false
        );

        searchButton.addActionListener(
                e -> searchSales()
        );

        JPanel searchPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        searchPanel.setBackground(
                background
        );

        searchPanel.add(
                searchField
        );

        searchPanel.add(
                searchButton
        );

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        header.add(
                searchPanel,
                BorderLayout.EAST
        );

        return header;
    }

    // ============================================================
    // TABLE
    // ============================================================

    private JPanel buildTablePanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(
                surface
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                border
                        ),
                        new EmptyBorder(
                                12,
                                12,
                                12,
                                12
                        )
                )
        );

        salesTableModel =
                new DefaultTableModel(
                        new Object[]{
                                "Invoice",
                                "Date",
                                "Subtotal",
                                "Discount",
                                "Tax",
                                "Total",
                                "Payment",
                                "Status"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        salesTable =
                new JTable(
                        salesTableModel
                );

        salesTable.setFont(
                AppTheme.SMALL_FONT
        );

        salesTable.setForeground(
                textPrimary
        );

        salesTable.setBackground(
                surface
        );

        salesTable.setGridColor(
                border
        );

        salesTable.setRowHeight(
                32
        );

        salesTable.setShowVerticalLines(
                false
        );

        salesTable.getTableHeader()
                .setFont(
                        AppTheme.SMALL_FONT
                );

        salesTable.getTableHeader()
                .setBackground(
                        background
                );

        salesTable.getTableHeader()
                .setForeground(
                        textSecondary
                );

        salesTable.getTableHeader()
                .setPreferredSize(
                        new Dimension(
                                0,
                                35
                        )
                );

        JScrollPane scrollPane =
                new JScrollPane(
                        salesTable
                );

        scrollPane.setBorder(
                null
        );

        panel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        return panel;
    }

    // ============================================================
    // LOAD SALES
    // ============================================================

    private void loadSalesHistory() {

        salesTableModel.setRowCount(0);

        List<Map<String, Object>> sales =
                salesDAO.getRecentSales();

        for (Map<String, Object> sale : sales) {

            BigDecimal subtotal =
                    (BigDecimal)
                            sale.get("subtotal");

            BigDecimal discount =
                    (BigDecimal)
                            sale.get("discount_amount");

            BigDecimal tax =
                    (BigDecimal)
                            sale.get("tax_amount");

            BigDecimal total =
                    (BigDecimal)
                            sale.get("total_amount");

            salesTableModel.addRow(
                    new Object[]{
                            sale.get(
                                    "invoice_number"
                            ),

                            sale.get(
                                    "sale_date"
                            ),

                            formatCurrency(
                                    subtotal
                            ),

                            formatCurrency(
                                    discount
                            ),

                            formatCurrency(
                                    tax
                            ),

                            formatCurrency(
                                    total
                            ),

                            sale.get(
                                    "payment_method"
                            ),

                            sale.get(
                                    "payment_status"
                            )
                    }
            );
        }
    }

    // ============================================================
    // SEARCH
    // ============================================================

    private void searchSales() {

        String searchText =
                searchField
                        .getText()
                        .trim()
                        .toLowerCase();

        if (searchText.isEmpty()) {

            loadSalesHistory();

            return;
        }

        salesTableModel.setRowCount(0);

        List<Map<String, Object>> sales =
                salesDAO.getRecentSales();

        for (Map<String, Object> sale : sales) {

            String invoice =
                    String.valueOf(
                            sale.get(
                                    "invoice_number"
                            )
                    ).toLowerCase();

            if (!invoice.contains(
                    searchText
            )) {
                continue;
            }

            BigDecimal subtotal =
                    (BigDecimal)
                            sale.get("subtotal");

            BigDecimal discount =
                    (BigDecimal)
                            sale.get(
                                    "discount_amount"
                            );

            BigDecimal tax =
                    (BigDecimal)
                            sale.get(
                                    "tax_amount"
                            );

            BigDecimal total =
                    (BigDecimal)
                            sale.get(
                                    "total_amount"
                            );

            salesTableModel.addRow(
                    new Object[]{
                            sale.get(
                                    "invoice_number"
                            ),

                            sale.get(
                                    "sale_date"
                            ),

                            formatCurrency(
                                    subtotal
                            ),

                            formatCurrency(
                                    discount
                            ),

                            formatCurrency(
                                    tax
                            ),

                            formatCurrency(
                                    total
                            ),

                            sale.get(
                                    "payment_method"
                            ),

                            sale.get(
                                    "payment_status"
                            )
                    }
            );
        }
    }

    // ============================================================
    // CURRENCY FORMAT
    // ============================================================

    private String formatCurrency(
            BigDecimal amount
    ) {

        if (amount == null) {
            return "₹0.00";
        }

        return currencyFormat.format(
                amount
        );
    }
}