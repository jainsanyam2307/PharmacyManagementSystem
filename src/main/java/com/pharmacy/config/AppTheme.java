package com.pharmacy.config;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public final class AppTheme {

    // Prevent object creation
    private AppTheme() {
    }

    // ==============================
    // Application Colors (Pharmly Palette)
    // ==============================

    // Forest Green Brand Colors
    public static final Color PRIMARY = new Color(14, 43, 38);          // #0E2B26
    public static final Color PRIMARY_DARK = new Color(8, 28, 24);       // #081C18
    public static final Color PRIMARY_LIGHT = new Color(232, 243, 238);   // #E8F3EE

    // Vibrant Electric Lime Accent (from reference image)
    public static final Color ACCENT = new Color(212, 247, 69);          // #D4F745
    public static final Color ACCENT_DARK = new Color(185, 222, 44);     // #B9DE2C
    public static final Color ACCENT_HOVER = new Color(185, 222, 44);
    public static final Color ACCENT_LIGHT = new Color(243, 253, 206);   // #F3FDCE
    public static final Color ACCENT_TEXT = new Color(14, 43, 38);

    // Sidebar Specific Palette
    public static final Color SIDEBAR_BG = new Color(14, 43, 38);        // #0E2B26
    public static final Color SIDEBAR_HOVER = new Color(24, 61, 53);     // #183D35
    public static final Color SIDEBAR_BORDER = new Color(25, 66, 58);
    public static final Color SIDEBAR_TEXT = new Color(156, 184, 175);
    public static final Color SIDEBAR_ACTIVE_BG = new Color(212, 247, 69);
    public static final Color SIDEBAR_ACTIVE_TEXT = new Color(14, 43, 38);

    // Status Colors
    public static final Color SUCCESS = new Color(22, 163, 74);
    public static final Color WARNING = new Color(217, 119, 6);
    public static final Color DANGER = new Color(220, 38, 38);

    // Pill Badge Colors
    public static final Color BADGE_GREEN_BG = new Color(223, 246, 232);
    public static final Color BADGE_GREEN_TEXT = new Color(22, 128, 61);
    public static final Color BADGE_YELLOW_BG = new Color(254, 243, 199);
    public static final Color BADGE_YELLOW_TEXT = new Color(180, 83, 9);
    public static final Color BADGE_RED_BG = new Color(254, 226, 226);
    public static final Color BADGE_RED_TEXT = new Color(220, 38, 38);

    // Canvas & Surface
    public static final Color BACKGROUND = new Color(245, 247, 246);     // #F5F7F6
    public static final Color SURFACE = Color.WHITE;
    public static final Color SURFACE_ALT = new Color(238, 243, 241);    // #EEF3F1

    // Typography
    public static final Color TEXT_PRIMARY = new Color(17, 30, 27);      // #111E1B
    public static final Color TEXT_SECONDARY = new Color(93, 114, 107);  // #5D726B
    public static final Color TEXT_MUTED = new Color(138, 158, 151);     // #8A9E97

    // Borders & Dividers
    public static final Color BORDER = new Color(228, 234, 231);         // #E4EAE7

    // ==============================
    // Dimensions
    // ==============================

    public static final int CORNER_RADIUS = 16;
    public static final int PILL_RADIUS = 999;

    public static final int SMALL_PADDING = 8;
    public static final int MEDIUM_PADDING = 16;
    public static final int LARGE_PADDING = 24;
    public static final int EXTRA_LARGE_PADDING = 32;

    // ==============================
    // Fonts (Prefer Segoe UI, fallback to SansSerif)
    // ==============================

    private static final String FONT_FAMILY = getPreferredFontFamily();

    private static String getPreferredFontFamily() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for (String name : ge.getAvailableFontFamilyNames()) {
            if ("Segoe UI".equalsIgnoreCase(name) || "Inter".equalsIgnoreCase(name)) {
                return name;
            }
        }
        return "SansSerif";
    }

    public static final Font TITLE_FONT =
            new Font(FONT_FAMILY, Font.BOLD, 28);

    public static final Font HEADING_FONT =
            new Font(FONT_FAMILY, Font.BOLD, 22);

    public static final Font SUBHEADING_FONT =
            new Font(FONT_FAMILY, Font.BOLD, 16);

    public static final Font BODY_FONT =
            new Font(FONT_FAMILY, Font.PLAIN, 14);

    public static final Font BODY_BOLD_FONT =
            new Font(FONT_FAMILY, Font.BOLD, 14);

    public static final Font SMALL_FONT =
            new Font(FONT_FAMILY, Font.PLAIN, 12);

    public static final Font SMALL_BOLD_FONT =
            new Font(FONT_FAMILY, Font.BOLD, 12);

    public static final Font BUTTON_FONT =
            new Font(FONT_FAMILY, Font.BOLD, 13);

    // ==============================
    // Apply Application Theme
    // ==============================

    public static void apply() {

        try {
            FlatLightLaf.setup();
        } catch (Exception exception) {
            System.err.println("Failed to initialize FlatLaf.");
        }

        UIManager.put("Panel.background", BACKGROUND);
        UIManager.put("Viewport.background", BACKGROUND);

        UIManager.put("Button.arc", 999);
        UIManager.put("Component.arc", 14);
        UIManager.put("TextComponent.arc", 20);

        UIManager.put("Button.font", BUTTON_FONT);
        UIManager.put("Label.font", BODY_FONT);

        UIManager.put("TextField.font", BODY_FONT);
        UIManager.put("PasswordField.font", BODY_FONT);
        UIManager.put("ComboBox.font", BODY_FONT);

        UIManager.put("Table.font", BODY_FONT);
        UIManager.put("Table.rowHeight", 42);
        UIManager.put("Table.showHorizontalLines", true);
        UIManager.put("Table.showVerticalLines", false);
        UIManager.put("Table.gridColor", BORDER);
        UIManager.put("Table.selectionBackground", PRIMARY_LIGHT);
        UIManager.put("Table.selectionForeground", TEXT_PRIMARY);
        UIManager.put("Table.selectionInactiveBackground", PRIMARY_LIGHT);
        UIManager.put("Table.selectionInactiveForeground", TEXT_PRIMARY);

        UIManager.put("TableHeader.background", SURFACE_ALT);
        UIManager.put("TableHeader.foreground", TEXT_SECONDARY);
        UIManager.put("TableHeader.font", SMALL_BOLD_FONT);
        UIManager.put("TableHeader.separatorColor", BORDER);
        UIManager.put("TableHeader.bottomSeparatorColor", BORDER);
        UIManager.put("TableHeader.height", 42);

        UIManager.put("ScrollBar.width", 8);
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.thumb", new Color(208, 218, 213));
        UIManager.put("ScrollBar.track", BACKGROUND);

        UIManager.put("Component.focusWidth", 1);
        UIManager.put("Component.innerFocusWidth", 0);

        UIManager.put("TextComponent.focusColor", ACCENT);
        UIManager.put("Component.focusColor", ACCENT);
    }

    // ==============================
    // Common Border Helpers
    // ==============================

    public static EmptyBorder padding(int top,
                                      int left,
                                      int bottom,
                                      int right) {

        return new EmptyBorder(top, left, bottom, right);
    }

    public static EmptyBorder padding(int amount) {

        return new EmptyBorder(
                amount,
                amount,
                amount,
                amount
        );
    }

    public static String toHex(Color c) {
        if (c == null) return "#000000";
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }
}
