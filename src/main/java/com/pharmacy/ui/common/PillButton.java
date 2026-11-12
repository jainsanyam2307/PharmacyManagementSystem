package com.pharmacy.ui.common;

import com.pharmacy.config.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class PillButton extends JButton {

    public enum Variant {
        LIME,
        DARK,
        SECONDARY,
        GHOST,
        SOFT_MUTED,
        CIRCLE
    }

    private Variant variant;
    private Color customBg;
    private Color customHoverBg;
    private Color customFg;
    private boolean isHovered = false;
    private boolean isPressed = false;
    private int cornerRadius = 999;

    public PillButton(String text, Icon icon, Variant variant) {
        super(text, icon);
        this.variant = variant;
        init();
    }

    public PillButton(String text, Variant variant) {
        this(text, null, variant);
    }

    public PillButton(Icon icon, Variant variant) {
        this(null, icon, variant);
    }

    public static PillButton lime(String text, Icon icon) {
        return new PillButton(text, icon, Variant.LIME);
    }

    public static PillButton lime(String text) {
        return new PillButton(text, null, Variant.LIME);
    }

    public static PillButton dark(String text, Icon icon) {
        return new PillButton(text, icon, Variant.DARK);
    }

    public static PillButton dark(String text) {
        return new PillButton(text, null, Variant.DARK);
    }

    public static PillButton secondary(String text, Icon icon) {
        return new PillButton(text, icon, Variant.SECONDARY);
    }

    public static PillButton secondary(String text) {
        return new PillButton(text, null, Variant.SECONDARY);
    }

    public static PillButton softMuted(String text, Icon icon) {
        return new PillButton(text, icon, Variant.SOFT_MUTED);
    }

    public static PillButton iconCircle(Icon icon, int diameter) {
        PillButton btn = new PillButton(null, icon, Variant.CIRCLE);
        btn.setPreferredSize(new Dimension(diameter, diameter));
        btn.setMinimumSize(new Dimension(diameter, diameter));
        btn.setMaximumSize(new Dimension(diameter, diameter));
        return btn;
    }

    private void init() {
        setFont(AppTheme.BUTTON_FONT);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setIconTextGap(8);

        if (variant == Variant.CIRCLE) {
            setBorder(new EmptyBorder(6, 6, 6, 6));
        } else {
            setBorder(new EmptyBorder(8, 16, 8, 16));
        }

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

            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }

    public void setCustomColors(Color bg, Color hoverBg, Color fg) {
        this.customBg = bg;
        this.customHoverBg = hoverBg;
        this.customFg = fg;
        repaint();
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        Color bg;
        Color borderC = null;
        Color fg;

        if (customBg != null) {
            bg = isPressed ? customHoverBg.darker() : (isHovered ? customHoverBg : customBg);
            fg = customFg != null ? customFg : AppTheme.TEXT_PRIMARY;
        } else {
            switch (variant) {
                case LIME:
                    bg = isPressed ? AppTheme.ACCENT_DARK.darker() : (isHovered ? AppTheme.ACCENT_DARK : AppTheme.ACCENT);
                    fg = AppTheme.ACCENT_TEXT;
                    break;
                case DARK:
                    bg = isPressed ? AppTheme.PRIMARY_DARK : (isHovered ? AppTheme.SIDEBAR_HOVER : AppTheme.PRIMARY);
                    fg = Color.WHITE;
                    break;
                case SECONDARY:
                    bg = isPressed ? AppTheme.SURFACE_ALT : (isHovered ? AppTheme.SURFACE_ALT : AppTheme.SURFACE);
                    borderC = AppTheme.BORDER;
                    fg = AppTheme.TEXT_PRIMARY;
                    break;
                case SOFT_MUTED:
                    bg = isPressed ? AppTheme.BORDER : (isHovered ? AppTheme.BORDER : AppTheme.SURFACE_ALT);
                    fg = AppTheme.TEXT_SECONDARY;
                    break;
                case CIRCLE:
                    bg = isPressed ? AppTheme.SURFACE_ALT.darker() : (isHovered ? AppTheme.SURFACE_ALT : AppTheme.SURFACE);
                    borderC = AppTheme.BORDER;
                    fg = AppTheme.TEXT_SECONDARY;
                    break;
                case GHOST:
                default:
                    bg = isHovered ? AppTheme.SURFACE_ALT : new Color(0, 0, 0, 0);
                    fg = AppTheme.TEXT_PRIMARY;
                    break;
            }
        }

        setForeground(fg);

        int actualRadius = cornerRadius >= 999 ? Math.min(w, h) : cornerRadius;
        Shape shape = new RoundRectangle2D.Float(0, 0, w - 1, h - 1, actualRadius, actualRadius);

        g2.setColor(bg);
        g2.fill(shape);

        if (borderC != null) {
            g2.setColor(borderC);
            g2.setStroke(new BasicStroke(1.2f));
            g2.draw(new RoundRectangle2D.Float(0.6f, 0.6f, w - 2.2f, h - 2.2f, actualRadius, actualRadius));
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
