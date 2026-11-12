package com.pharmacy.ui.common;

import com.pharmacy.config.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ModernPaginationPanel extends JPanel {

    private final JLabel infoLabel;
    private final JPanel buttonContainer;

    public ModernPaginationPanel(int currentPage, int totalPages, int shownCount, int totalCount) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(14, 16, 14, 16));

        infoLabel = new JLabel("Showing " + shownCount + " out of " + totalCount);
        infoLabel.setFont(AppTheme.SMALL_FONT);
        infoLabel.setForeground(AppTheme.TEXT_MUTED);

        buttonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        buttonContainer.setOpaque(false);

        // Previous button
        PillButton prevBtn = PillButton.iconCircle(ModernIcons.get("chevron_left", 14, AppTheme.TEXT_SECONDARY), 30);
        buttonContainer.add(prevBtn);

        // Page buttons
        for (int i = 1; i <= Math.min(3, totalPages); i++) {
            PillButton pageBtn;
            if (i == currentPage) {
                pageBtn = PillButton.dark(String.valueOf(i));
                pageBtn.setPreferredSize(new Dimension(30, 30));
            } else {
                pageBtn = PillButton.secondary(String.valueOf(i));
                pageBtn.setPreferredSize(new Dimension(30, 30));
            }
            pageBtn.setFont(AppTheme.SMALL_BOLD_FONT);
            buttonContainer.add(pageBtn);
        }

        if (totalPages > 4) {
            JLabel dots = new JLabel("...");
            dots.setFont(AppTheme.SMALL_BOLD_FONT);
            dots.setForeground(AppTheme.TEXT_MUTED);
            buttonContainer.add(dots);

            PillButton lastBtn = PillButton.secondary(String.valueOf(totalPages));
            lastBtn.setPreferredSize(new Dimension(30, 30));
            lastBtn.setFont(AppTheme.SMALL_BOLD_FONT);
            buttonContainer.add(lastBtn);
        }

        // Next button
        PillButton nextBtn = PillButton.iconCircle(ModernIcons.get("chevron_right", 14, AppTheme.TEXT_SECONDARY), 30);
        buttonContainer.add(nextBtn);

        add(infoLabel, BorderLayout.WEST);
        add(buttonContainer, BorderLayout.EAST);
    }

    public void updateInfo(int shownCount, int totalCount) {
        infoLabel.setText("Showing " + shownCount + " out of " + totalCount);
    }
}
