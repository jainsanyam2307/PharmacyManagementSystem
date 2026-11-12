package com.pharmacy.ui.common;

import com.pharmacy.config.AppTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class ModernCard extends JPanel {

    private int cornerRadius = 16;
    private Color cardBackground = AppTheme.SURFACE;
    private Color hoverBackground = null;
    private Color borderColor = AppTheme.BORDER;
    private boolean isHovered = false;
    private boolean hoverable = false;

    public ModernCard(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        setOpaque(false);
        setBackground(cardBackground);
    }

    public ModernCard() {
        this(16);
    }

    public void setFeatured(boolean featured) {
        if (featured) {
            this.cardBackground = AppTheme.PRIMARY;
            this.borderColor = AppTheme.SIDEBAR_BORDER;
            this.hoverBackground = AppTheme.SIDEBAR_HOVER;
        } else {
            this.cardBackground = AppTheme.SURFACE;
            this.borderColor = AppTheme.BORDER;
            this.hoverBackground = AppTheme.PRIMARY_LIGHT;
        }
        repaint();
    }

    public void setHoverable(boolean hoverable) {
        this.hoverable = hoverable;
        if (hoverable && getMouseListeners().length == 0) {
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
    }

    public void setCardBackground(Color bg) {
        this.cardBackground = bg;
        repaint();
    }

    public void setBorderColor(Color border) {
        this.borderColor = border;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        Color bg = (hoverable && isHovered && hoverBackground != null) ? hoverBackground : cardBackground;

        Shape shape = new RoundRectangle2D.Float(0, 0, w - 1, h - 1, cornerRadius, cornerRadius);

        g2.setColor(bg);
        g2.fill(shape);

        if (borderColor != null) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1.2f));
            g2.draw(new RoundRectangle2D.Float(0.6f, 0.6f, w - 2.2f, h - 2.2f, cornerRadius, cornerRadius));
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
