package com.parking.gui;

import java.awt.*;

public class Theme {
    public static final Color BG_PRIMARY    = new Color(255, 255, 255);
    public static final Color BG_SECONDARY  = new Color(245, 245, 243);
    public static final Color BG_SIDEBAR    = new Color(28,  28,  30);
    public static final Color ACCENT_BLUE   = new Color(24,  95, 165);
    public static final Color ACCENT_TEAL   = new Color(15, 110,  86);
    public static final Color ACCENT_GREEN  = new Color(59, 109,  17);
    public static final Color ACCENT_RED    = new Color(163, 45,  45);
    public static final Color ACCENT_AMBER  = new Color(133,  79,  11);
    public static final Color TEXT_PRIMARY  = new Color(30,  30,  28);
    public static final Color TEXT_MUTED    = new Color(100, 100, 96);
    public static final Color BORDER        = new Color(210, 208, 200);
    public static final Color SLOT_EMPTY    = new Color(225, 245, 238);
    public static final Color SLOT_TAKEN    = new Color(252, 235, 235);
    public static final Color SLOT_BORDER_EMPTY  = new Color(15, 110, 86);
    public static final Color SLOT_BORDER_TAKEN  = new Color(163, 45, 45);

    public static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD,   14);
    public static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN,  13);
    public static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN,  11);
    public static final Font FONT_HEADER  = new Font("Segoe UI", Font.BOLD,   22);
    public static final Font FONT_NAV     = new Font("Segoe UI", Font.PLAIN,  13);

    public static javax.swing.border.Border cardBorder() {
        return javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(BORDER, 1, true),
            javax.swing.BorderFactory.createEmptyBorder(14, 16, 14, 16));
    }
    public static javax.swing.border.Border sectionPadding() {
        return javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20);
    }
}
