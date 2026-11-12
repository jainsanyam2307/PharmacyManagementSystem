package com.pharmacy;

import com.pharmacy.config.AppTheme;
import com.pharmacy.ui.landing.LandingFrame;

import javax.swing.*;

public class App {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            AppTheme.apply();

            LandingFrame landingFrame =
                    new LandingFrame();

            landingFrame.setVisible(true);
        });
    }
}