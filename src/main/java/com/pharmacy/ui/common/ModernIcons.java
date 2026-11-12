package com.pharmacy.ui.common;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

/**
 * ModernIcons
 *
 * Vector-drawn crisp anti-aliased icons for the Pharmly UI theme.
 * All icons scale smoothly to any requested size and respect custom colors.
 */
public final class ModernIcons {

    private ModernIcons() {
    }

    public static Icon get(String name, int size, Color color) {
        return new VectorIcon(name, size, color);
    }

    public static Icon get(String name, int size) {
        return new VectorIcon(name, size, null);
    }

    private static class VectorIcon implements Icon {
        private final String name;
        private final int size;
        private final Color fixedColor;

        VectorIcon(String name, int size, Color fixedColor) {
            this.name = name != null ? name.toLowerCase() : "";
            this.size = size;
            this.fixedColor = fixedColor;
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

            Color paintColor = fixedColor != null ? fixedColor : (c != null ? c.getForeground() : Color.DARK_GRAY);
            g2.setColor(paintColor);

            g2.translate(x, y);
            float scale = size / 24.0f;
            g2.scale(scale, scale);

            float strokeWidth = 1.8f;
            g2.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            drawIcon(g2, name);

            g2.dispose();
        }

        private void drawIcon(Graphics2D g2, String iconName) {
            switch (iconName) {
                case "search": {
                    g2.draw(new Ellipse2D.Float(4, 4, 11, 11));
                    g2.draw(new Line2D.Float(12.5f, 12.5f, 19.5f, 19.5f));
                    break;
                }
                case "bell": {
                    Path2D.Float p = new Path2D.Float();
                    p.moveTo(12, 3);
                    p.curveTo(8, 3, 6, 8, 6, 13);
                    p.lineTo(4, 16);
                    p.lineTo(20, 16);
                    p.lineTo(18, 13);
                    p.curveTo(18, 8, 16, 3, 12, 3);
                    p.closePath();
                    g2.draw(p);
                    g2.draw(new Arc2D.Float(10, 17, 4, 3, 0, -180, Arc2D.OPEN));
                    break;
                }
                case "user": {
                    g2.draw(new Ellipse2D.Float(8, 3, 8, 8));
                    Path2D.Float p = new Path2D.Float();
                    p.moveTo(4, 20);
                    p.curveTo(4, 15, 8, 14, 12, 14);
                    p.curveTo(16, 14, 20, 15, 20, 20);
                    g2.draw(p);
                    break;
                }
                case "plus": {
                    g2.draw(new Line2D.Float(12, 5, 12, 19));
                    g2.draw(new Line2D.Float(5, 12, 19, 12));
                    break;
                }
                case "filter": {
                    Path2D.Float p = new Path2D.Float();
                    p.moveTo(3, 5);
                    p.lineTo(21, 5);
                    p.lineTo(14, 13);
                    p.lineTo(14, 19);
                    p.lineTo(10, 21);
                    p.lineTo(10, 13);
                    p.closePath();
                    g2.draw(p);
                    break;
                }
                case "export": {
                    g2.draw(new Line2D.Float(12, 4, 12, 14));
                    g2.draw(new Line2D.Float(8, 8, 12, 4));
                    g2.draw(new Line2D.Float(16, 8, 12, 4));
                    Path2D.Float p = new Path2D.Float();
                    p.moveTo(4, 12);
                    p.lineTo(4, 19);
                    p.lineTo(20, 19);
                    p.lineTo(20, 12);
                    g2.draw(p);
                    break;
                }
                case "calendar": {
                    g2.draw(new RoundRectangle2D.Float(3, 5, 18, 15, 4, 4));
                    g2.draw(new Line2D.Float(3, 9, 21, 9));
                    g2.draw(new Line2D.Float(8, 3, 8, 6));
                    g2.draw(new Line2D.Float(16, 3, 16, 6));
                    g2.fill(new Ellipse2D.Float(8, 12, 2, 2));
                    g2.fill(new Ellipse2D.Float(14, 12, 2, 2));
                    break;
                }
                case "more_vert": {
                    g2.fill(new Ellipse2D.Float(11, 4, 2.5f, 2.5f));
                    g2.fill(new Ellipse2D.Float(11, 11, 2.5f, 2.5f));
                    g2.fill(new Ellipse2D.Float(11, 18, 2.5f, 2.5f));
                    break;
                }
                case "eye": {
                    Path2D.Float p = new Path2D.Float();
                    p.moveTo(3, 12);
                    p.quadTo(12, 5, 21, 12);
                    p.quadTo(12, 19, 3, 12);
                    p.closePath();
                    g2.draw(p);
                    g2.fill(new Ellipse2D.Float(10, 10, 4, 4));
                    break;
                }
                case "edit": {
                    Path2D.Float p = new Path2D.Float();
                    p.moveTo(15, 4);
                    p.lineTo(20, 9);
                    p.lineTo(8, 21);
                    p.lineTo(3, 21);
                    p.lineTo(3, 16);
                    p.closePath();
                    g2.draw(p);
                    break;
                }
                case "trash": {
                    g2.draw(new Line2D.Float(4, 7, 20, 7));
                    g2.draw(new Line2D.Float(9, 4, 15, 4));
                    Path2D.Float p = new Path2D.Float();
                    p.moveTo(6, 7);
                    p.lineTo(7, 19);
                    p.curveTo(7, 20.5f, 8, 21, 9.5f, 21);
                    p.lineTo(14.5f, 21);
                    p.curveTo(16, 21, 17, 20.5f, 17, 19);
                    p.lineTo(18, 7);
                    g2.draw(p);
                    g2.draw(new Line2D.Float(10, 10, 10, 17));
                    g2.draw(new Line2D.Float(14, 10, 14, 17));
                    break;
                }
                case "pill": {
                    AffineTransform old = g2.getTransform();
                    g2.rotate(Math.toRadians(45), 12, 12);
                    RoundRectangle2D.Float pill = new RoundRectangle2D.Float(6, 9, 12, 6, 6, 6);
                    g2.draw(pill);
                    g2.draw(new Line2D.Float(12, 9, 12, 15));
                    g2.setTransform(old);
                    break;
                }
                case "box": {
                    Path2D.Float p = new Path2D.Float();
                    p.moveTo(12, 3);
                    p.lineTo(20, 7.5f);
                    p.lineTo(20, 16.5f);
                    p.lineTo(12, 21);
                    p.lineTo(4, 16.5f);
                    p.lineTo(4, 7.5f);
                    p.closePath();
                    g2.draw(p);
                    g2.draw(new Line2D.Float(12, 3, 12, 12));
                    g2.draw(new Line2D.Float(12, 12, 20, 7.5f));
                    g2.draw(new Line2D.Float(12, 12, 4, 7.5f));
                    break;
                }
                case "alert": {
                    Path2D.Float p = new Path2D.Float();
                    p.moveTo(12, 3.5f);
                    p.lineTo(21, 19.5f);
                    p.lineTo(3, 19.5f);
                    p.closePath();
                    g2.draw(p);
                    g2.draw(new Line2D.Float(12, 9, 12, 14));
                    g2.fill(new Ellipse2D.Float(11, 16.5f, 2, 2));
                    break;
                }
                case "check": {
                    Path2D.Float p = new Path2D.Float();
                    p.moveTo(4, 12);
                    p.lineTo(9.5f, 17.5f);
                    p.lineTo(20, 6.5f);
                    g2.draw(p);
                    break;
                }
                case "chevron_left": {
                    Path2D.Float p = new Path2D.Float();
                    p.moveTo(15, 5);
                    p.lineTo(8, 12);
                    p.lineTo(15, 19);
                    g2.draw(p);
                    break;
                }
                case "chevron_right": {
                    Path2D.Float p = new Path2D.Float();
                    p.moveTo(9, 5);
                    p.lineTo(16, 12);
                    p.lineTo(9, 19);
                    g2.draw(p);
                    break;
                }
                case "logout": {
                    Path2D.Float p = new Path2D.Float();
                    p.moveTo(13, 4);
                    p.lineTo(6, 4);
                    p.curveTo(5, 4, 4, 5, 4, 6);
                    p.lineTo(4, 18);
                    p.curveTo(4, 19, 5, 20, 6, 20);
                    p.lineTo(13, 20);
                    g2.draw(p);
                    g2.draw(new Line2D.Float(10, 12, 20, 12));
                    g2.draw(new Line2D.Float(16, 8, 20, 12));
                    g2.draw(new Line2D.Float(16, 16, 20, 12));
                    break;
                }
                case "grid": {
                    g2.draw(new RoundRectangle2D.Float(4, 4, 6.5f, 6.5f, 2, 2));
                    g2.draw(new RoundRectangle2D.Float(13.5f, 4, 6.5f, 6.5f, 2, 2));
                    g2.draw(new RoundRectangle2D.Float(4, 13.5f, 6.5f, 6.5f, 2, 2));
                    g2.draw(new RoundRectangle2D.Float(13.5f, 13.5f, 6.5f, 6.5f, 2, 2));
                    break;
                }
                case "cart": {
                    Path2D.Float p = new Path2D.Float();
                    p.moveTo(3, 4);
                    p.lineTo(6, 4);
                    p.lineTo(8.5f, 14);
                    p.lineTo(18, 14);
                    p.lineTo(20, 7);
                    p.lineTo(7, 7);
                    g2.draw(p);
                    g2.fill(new Ellipse2D.Float(8.5f, 17, 2.5f, 2.5f));
                    g2.fill(new Ellipse2D.Float(16.5f, 17, 2.5f, 2.5f));
                    break;
                }
                case "chart": {
                    g2.draw(new Line2D.Float(4, 20, 20, 20));
                    g2.draw(new Line2D.Float(4, 4, 4, 20));
                    Path2D.Float p = new Path2D.Float();
                    p.moveTo(4, 15);
                    p.lineTo(9, 10);
                    p.lineTo(14, 13);
                    p.lineTo(19, 6);
                    g2.draw(p);
                    break;
                }
                case "dollar": {
                    g2.draw(new Ellipse2D.Float(3, 3, 18, 18));
                    g2.draw(new Line2D.Float(12, 6, 12, 18));
                    Path2D.Float p = new Path2D.Float();
                    p.moveTo(15, 9);
                    p.quadTo(12, 7, 9, 9);
                    p.quadTo(8, 10.5f, 12, 12);
                    p.quadTo(16, 13.5f, 15, 15);
                    p.quadTo(12, 17, 9, 15);
                    g2.draw(p);
                    break;
                }
                case "lock": {
                    g2.draw(new RoundRectangle2D.Float(5, 10, 14, 11, 4, 4));
                    g2.draw(new Arc2D.Float(8, 4, 8, 10, 0, 180, Arc2D.OPEN));
                    g2.fill(new Ellipse2D.Float(11, 14, 2, 3));
                    break;
                }
                default: {
                    g2.draw(new Ellipse2D.Float(4, 4, 16, 16));
                    break;
                }
            }
        }
    }
}
