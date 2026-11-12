package com.pharmacy.ui.common;

import com.pharmacy.config.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class ModernTableStyler {

    private ModernTableStyler() {
    }

    public static void styleTable(JTable table) {
        table.setFont(AppTheme.BODY_FONT);
        table.setForeground(AppTheme.TEXT_PRIMARY);
        table.setBackground(AppTheme.SURFACE);
        table.setGridColor(new Color(242, 246, 244));
        table.setRowHeight(44);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(AppTheme.PRIMARY_LIGHT);
        table.setSelectionForeground(AppTheme.TEXT_PRIMARY);
        table.setFillsViewportHeight(true);

        // Header Styling
        JTableHeader header = table.getTableHeader();
        header.setFont(AppTheme.SMALL_BOLD_FONT);
        header.setForeground(AppTheme.TEXT_SECONDARY);
        header.setBackground(AppTheme.SURFACE_ALT);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER));
        header.setDefaultRenderer(new ModernHeaderRenderer());

        // Row Hover Support
        final int[] hoveredRow = {-1};
        table.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row != hoveredRow[0]) {
                    hoveredRow[0] = row;
                    table.repaint();
                }
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                hoveredRow[0] = -1;
                table.repaint();
            }
        });

        // Default cell renderer with hover and padding
        table.setDefaultRenderer(Object.class, new ModernCellRenderer(hoveredRow));
    }

    public static void setStatusColumn(JTable table, int columnIndex) {
        if (columnIndex >= 0 && columnIndex < table.getColumnCount()) {
            table.getColumnModel().getColumn(columnIndex).setCellRenderer(new StatusBadgeCellRenderer());
        }
    }

    public static JScrollPane createScrollPane(JTable table) {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.getViewport().setBackground(AppTheme.SURFACE);
        scrollPane.setBackground(AppTheme.SURFACE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    public static class ModernHeaderRenderer extends DefaultTableCellRenderer {
        public ModernHeaderRenderer() {
            setHorizontalAlignment(SwingConstants.LEFT);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setText(value != null ? value.toString() : "");
            setBackground(AppTheme.SURFACE_ALT);
            setForeground(AppTheme.TEXT_SECONDARY);
            setFont(AppTheme.SMALL_BOLD_FONT);
            setBorder(new EmptyBorder(0, 14, 0, 14));
            return this;
        }
    }

    private static class ModernCellRenderer extends DefaultTableCellRenderer {
        private final int[] hoveredRow;

        ModernCellRenderer(int[] hoveredRow) {
            this.hoveredRow = hoveredRow;
            setBorder(new EmptyBorder(0, 14, 0, 14));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setText(value != null ? value.toString() : "");
            setBorder(new EmptyBorder(0, 14, 0, 14));
            setFont(AppTheme.BODY_FONT);

            if (isSelected) {
                setBackground(AppTheme.PRIMARY_LIGHT);
                setForeground(AppTheme.TEXT_PRIMARY);
            } else if (hoveredRow != null && row == hoveredRow[0]) {
                setBackground(new Color(246, 249, 248));
                setForeground(AppTheme.TEXT_PRIMARY);
            } else {
                setBackground(AppTheme.SURFACE);
                setForeground(AppTheme.TEXT_PRIMARY);
            }

            return this;
        }
    }

    public static class StatusBadgeCellRenderer extends PillBadge implements TableCellRenderer {
        public StatusBadgeCellRenderer() {
            super("", AppTheme.BADGE_GREEN_BG, AppTheme.BADGE_GREEN_TEXT);
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            String text = value != null ? value.toString() : "";
            PillBadge proto = PillBadge.forStatus(text);
            setText(proto.getText());
            setForeground(proto.getForeground());
            setBadgeBackground(proto.getBadgeBackground());
            return this;
        }
    }
}
