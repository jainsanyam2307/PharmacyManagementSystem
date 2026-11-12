package com.pharmacy.test;

import com.pharmacy.config.AppTheme;
import com.pharmacy.ui.landing.LandingFrame;
import com.pharmacy.ui.landing.LandingPanel;
import com.pharmacy.ui.auth.LoginPanel;
import com.pharmacy.ui.main.MainAppPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class RenderPreview {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                AppTheme.apply();

                LandingFrame dummyFrame = new LandingFrame();
                dummyFrame.addNotify();
                dummyFrame.setSize(1280, 820);
                dummyFrame.validate();

                File outputDir = new File("C:/Users/hi/.gemini/antigravity/brain/975e09b8-994c-4efb-b112-9f64040a04ed");
                outputDir.mkdirs();

                // 1. Landing Screen
                LandingPanel landing = new LandingPanel(dummyFrame);
                captureComponent(landing, new File(outputDir, "preview_landing.png"), 1280, 820);

                // 2. Login Screen
                LoginPanel login = new LoginPanel(dummyFrame);
                captureComponent(login, new File(outputDir, "preview_login.png"), 1280, 820);

                // 3. Dashboard
                MainAppPanel mainDashboard = new MainAppPanel(dummyFrame);
                mainDashboard.showDashboard();
                captureComponent(mainDashboard, new File(outputDir, "preview_dashboard.png"), 1280, 820);

                // 4. Product List (Medicines)
                MainAppPanel mainProducts = new MainAppPanel(dummyFrame);
                mainProducts.showMedicines();
                captureComponent(mainProducts, new File(outputDir, "preview_product_list.png"), 1280, 820);

                System.out.println("PREVIEWS_GENERATED_SUCCESSFULLY");
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        });
    }

    private static void captureComponent(JComponent component, File file, int width, int height) throws Exception {
        component.setSize(width, height);
        component.doLayout();
        layoutTree(component);

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(AppTheme.BACKGROUND);
        g2.fillRect(0, 0, width, height);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        component.printAll(g2);
        g2.dispose();

        ImageIO.write(img, "png", file);
        System.out.println("Saved: " + file.getAbsolutePath());
    }

    private static void layoutTree(Container c) {
        c.doLayout();
        if (c instanceof JScrollPane) {
            JScrollPane sp = (JScrollPane) c;
            System.out.println("DEBUG JScrollPane bounds: " + sp.getBounds() + ", colHeader: " + (sp.getColumnHeader() != null ? sp.getColumnHeader().getBounds() : "null"));
            if (sp.getColumnHeader() != null) {
                sp.getColumnHeader().doLayout();
                if (sp.getColumnHeader().getView() != null) {
                    sp.getColumnHeader().getView().doLayout();
                }
            }
        }
        for (Component child : c.getComponents()) {
            if (child instanceof Container) {
                layoutTree((Container) child);
            }
        }
    }
}
