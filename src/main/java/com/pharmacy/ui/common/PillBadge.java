package com.pharmacy.ui.common;

import com.pharmacy.config.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class PillBadge extends JLabel {

    private Color badgeBackground;
    private Color badgeBorder;

    public PillBadge(String text, Color bg, Color fg, Color border) {
        super(text, SwingConstants.CENTER);
        this.badgeBackground = bg;
        this.badgeBorder = border;
        setForeground(fg);
        setFont(AppTheme.SMALL_BOLD_FONT);
        setOpaque(false);
        setBorder(new EmptyBorder(4, 12, 4, 12));
    }

    public PillBadge(String text, Color bg, Color fg) {
        this(text, bg, fg, null);
    }

    public static PillBadge inStock() {
        return new PillBadge("in stock", AppTheme.BADGE_GREEN_BG, AppTheme.BADGE_GREEN_TEXT);
    }

    public static PillBadge lowStock() {
        return new PillBadge("low stock", AppTheme.BADGE_YELLOW_BG, AppTheme.BADGE_YELLOW_TEXT);
    }

    public static PillBadge outOfStock() {
        return new PillBadge("out of stock", AppTheme.BADGE_RED_BG, AppTheme.BADGE_RED_TEXT);
    }

    public static PillBadge critical() {
        return new PillBadge("critical", AppTheme.BADGE_RED_BG, AppTheme.BADGE_RED_TEXT);
    }

    public static PillBadge forStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return new PillBadge("", AppTheme.SURFACE_ALT, AppTheme.TEXT_MUTED);
        }
        String s = status.trim().toLowerCase();
        if (s.contains("in stock") || s.contains("available") || s.contains("active") || s.contains("paid")) {
            return new PillBadge(status, AppTheme.BADGE_GREEN_BG, AppTheme.BADGE_GREEN_TEXT);
        } else if (s.contains("low") || s.contains("expiring") || s.contains("soon") || s.contains("monitor") || s.contains("pending")) {
            return new PillBadge(status, AppTheme.BADGE_YELLOW_BG, AppTheme.BADGE_YELLOW_TEXT);
        } else if (s.contains("out") || s.contains("critical") || s.contains("urgent") || s.contains("expired") || s.contains("inactive")) {
            return new PillBadge(status, AppTheme.BADGE_RED_BG, AppTheme.BADGE_RED_TEXT);
        }
        return new PillBadge(status, AppTheme.SURFACE_ALT, AppTheme.TEXT_SECONDARY);
    }

    public static PillBadge trend(double percent) {
        boolean positive = percent >= 0;
        String text = (positive ? "+" : "") + String.format("%.1f%%", percent) + " Since last week";
        Color bg = positive ? AppTheme.BADGE_GREEN_BG : AppTheme.BADGE_RED_BG;
        Color fg = positive ? AppTheme.BADGE_GREEN_TEXT : AppTheme.BADGE_RED_TEXT;
        return new PillBadge(text, bg, fg);
    }

    public Color getBadgeBackground() {
        return badgeBackground;
    }

    public void setBadgeBackground(Color bg) {
        this.badgeBackground = bg;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int arc = h;

        Shape shape = new RoundRectangle2D.Float(0, 0, w - 1, h - 1, arc, arc);

        if (badgeBackground != null) {
            g2.setColor(badgeBackground);
            g2.fill(shape);
        }

        if (badgeBorder != null) {
            g2.setColor(badgeBorder);
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, w - 2f, h - 2f, arc, arc));
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
