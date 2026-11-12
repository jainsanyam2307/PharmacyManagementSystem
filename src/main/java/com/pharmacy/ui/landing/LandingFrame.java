package com.pharmacy.ui.landing;

import com.pharmacy.config.AppTheme;
import com.pharmacy.ui.auth.LoginPanel;
import com.pharmacy.ui.auth.SignupPanel;
import com.pharmacy.ui.main.MainAppPanel;

import javax.swing.*;
import java.awt.*;

public class LandingFrame extends JFrame {

    private static final String LANDING = "landing";
    private static final String LOGIN = "login";
    private static final String SIGNUP = "signup";
    private static final String MAIN_APP = "mainApp";

    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    public LandingFrame() {

        setTitle("Pharmly - Pharmacy Management System");

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        configureWindowSize();

        cardLayout = new CardLayout();

        contentPanel = new JPanel(
                cardLayout
        );

        contentPanel.setBackground(
                AppTheme.BACKGROUND
        );

        // Landing screen
        contentPanel.add(
                new LandingPanel(this),
                LANDING
        );

        // Login screen
        contentPanel.add(
                new LoginPanel(this),
                LOGIN
        );

        // Signup screen
        contentPanel.add(
                new SignupPanel(this),
                SIGNUP
        );

        // Main application shell (post-login)
        contentPanel.add(
                new MainAppPanel(this),
                MAIN_APP
        );

        setContentPane(
                contentPanel
        );

        setLocationRelativeTo(null);
    }

    // =========================================================
    // WINDOW SIZE
    // =========================================================

    private void configureWindowSize() {

        Dimension screenSize =
                Toolkit.getDefaultToolkit()
                        .getScreenSize();

        int width =
                (int) (
                        screenSize.width * 0.80
                );

        int height =
                (int) (
                        screenSize.height * 0.80
                );

        setSize(
                width,
                height
        );

        setMinimumSize(
                new Dimension(
                        900,
                        600
                )
        );
    }

    // =========================================================
    // NAVIGATION
    // =========================================================

    public void showLanding() {

        cardLayout.show(
                contentPanel,
                LANDING
        );
    }

    public void showLogin() {

        cardLayout.show(
                contentPanel,
                LOGIN
        );
    }

    public void showSignup() {

        cardLayout.show(
                contentPanel,
                SIGNUP
        );
    }

    public void showMainApp() {

        cardLayout.show(
                contentPanel,
                MAIN_APP
        );
    }
}