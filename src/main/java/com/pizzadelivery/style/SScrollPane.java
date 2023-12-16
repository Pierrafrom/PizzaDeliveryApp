package com.pizzadelivery.style;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The SScrollPane class is a customized JScrollPane that adheres to the application's styling theme.
 * It features custom scroll bars that utilize colors and styles defined in the Style interface.
 * This class extends JScrollPane and can be used wherever a JScrollPane is needed, providing
 * a consistent look and feel with custom behaviors.
 */
public class SScrollPane extends JScrollPane {

    /**
     * Constructor for SScrollPane.
     * Initializes a JScrollPane with a specific view component and applies custom styling.
     *
     * @param view The component to display in the scroll pane.
     */
    public SScrollPane(Component view) {
        super(view);
        initCustomStyle();
        setBorder(null); // Removes the default border to adhere to the custom style
    }

    /**
     * Initializes the custom styling for the scroll bars.
     * Applies a custom UI to both vertical and horizontal scroll bars.
     */
    private void initCustomStyle() {
        getVerticalScrollBar().setUI(new CustomScrollBarUI());
        getHorizontalScrollBar().setUI(new CustomScrollBarUI());
        getVerticalScrollBar().setUnitIncrement(16); // Adjusts the scrolling speed
    }

    /**
     * CustomScrollBarUI is a private static inner class that customizes the look and behavior
     * of the scroll bars. It extends BasicScrollBarUI and overrides necessary methods
     * to apply the custom styles and interactions.
     */
    private static class CustomScrollBarUI extends BasicScrollBarUI {

        private boolean isHovered = false; // Flag to track hover state

        /**
         * Configures the colors of the scroll bar.
         * Sets the thumb and track colors using values from the Style interface.
         */
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = Style.NEUTRAL_COLOR;
            this.trackColor = Style.BACKGROUND_COLOR;
        }

        /**
         * Installs listeners on the scroll bar.
         * Adds mouse listeners to change the thumb color on hover.
         */
        @Override
        protected void installListeners() {
            super.installListeners();
            scrollbar.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    scrollbar.repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    scrollbar.repaint();
                }
            });
        }

        /**
         * Paints the thumb of the scroll bar.
         * Overrides the method to apply the hover effect.
         *
         * @param g           The Graphics object used for painting.
         * @param c           The component being painted.
         * @param thumbBounds The bounds of the thumb to be painted.
         */
        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }

            int w = thumbBounds.width;
            int h = thumbBounds.height;

            g.translate(thumbBounds.x, thumbBounds.y);
            g.setColor(isHovered ? Style.NEUTRAL_HOVER_COLOR : thumbColor); // Changes color on hover
            g.fillRect(0, 0, w, h);
            g.translate(-thumbBounds.x, -thumbBounds.y);
        }

        /**
         * Creates a button with zero size for the decrease button of the scroll bar.
         * This method is overridden to remove the default buttons.
         *
         * @param orientation The orientation of the button (not used here).
         * @return A JButton with zero size.
         */
        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        /**
         * Creates a button with zero size for the increase button of the scroll bar.
         * This method is overridden to remove the default buttons.
         *
         * @param orientation The orientation of the button (not used here).
         * @return A JButton with zero size.
         */
        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        /**
         * Creates and returns a JButton with zero size.
         * This helper method is used to effectively hide the default scroll bar buttons.
         *
         * @return A JButton with zero size.
         */
        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }
    }

    /**
     * Main method to demonstrate the usage of SScrollPane.
     * This is an example showing how to integrate SScrollPane into a Swing application.
     *
     * @param args Command line arguments (not used in this example).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create the main window
            JFrame frame = new JFrame("Test SScrollPane");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setMinimumSize(new Dimension(300, 200)); // Taille minimale

            // Add a STextArea to the frame
            STextArea textArea = new STextArea();
            textArea.setText("Votre texte ici...\n".repeat(20)); // Texte de démonstration

            // Création et ajout du SScrollPane
            SScrollPane scrollPane = new SScrollPane(textArea);
            frame.add(scrollPane, BorderLayout.CENTER);

            // Display the window
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
