package com.pharmacy.ui.common;

import com.pharmacy.config.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;

public class ModernSearchBar extends JPanel {

    private final JTextField textField;
    private final String placeholder;
    private boolean isFocused = false;

    public ModernSearchBar(String placeholder, int columns) {
        this.placeholder = placeholder != null ? placeholder : "Search...";
        setLayout(new BorderLayout(8, 0));
        setOpaque(false);
        setBorder(new EmptyBorder(6, 14, 6, 14));

        JLabel iconLabel = new JLabel(ModernIcons.get("search", 16, AppTheme.TEXT_MUTED));
        add(iconLabel, BorderLayout.WEST);

        textField = new JTextField(columns) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(AppTheme.TEXT_MUTED);
                    g2.setFont(getFont());
                    FontMetrics fm = g2.getFontMetrics();
                    int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString(placeholder, getInsets().left, y);
                    g2.dispose();
                }
            }
        };

        textField.setFont(AppTheme.BODY_FONT);
        textField.setForeground(AppTheme.TEXT_PRIMARY);
        textField.setOpaque(false);
        textField.setBorder(null);

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                isFocused = true;
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                isFocused = false;
                repaint();
            }
        });

        add(textField, BorderLayout.CENTER);
    }

    public ModernSearchBar(String placeholder) {
        this(placeholder, 20);
    }

    public ModernSearchBar() {
        this("Search...", 20);
    }

    public JTextField getTextField() {
        return textField;
    }

    public String getText() {
        return textField.getText();
    }

    public void setText(String text) {
        textField.setText(text);
    }

    public void addDocumentListener(DocumentListener listener) {
        textField.getDocument().addDocumentListener(listener);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int arc = h;

        Shape shape = new RoundRectangle2D.Float(0, 0, w - 1, h - 1, arc, arc);

        g2.setColor(AppTheme.SURFACE);
        g2.fill(shape);

        if (isFocused) {
            g2.setColor(AppTheme.PRIMARY);
            g2.setStroke(new BasicStroke(1.5f));
        } else {
            g2.setColor(AppTheme.BORDER);
            g2.setStroke(new BasicStroke(1.2f));
        }
        g2.draw(new RoundRectangle2D.Float(0.6f, 0.6f, w - 2.2f, h - 2.2f, arc, arc));

        g2.dispose();
        super.paintComponent(g);
    }
}
