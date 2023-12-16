package com.pizzadelivery.style;

import javax.swing.*;
import java.awt.*;

/**
 * SSplitPanel is a custom split panel class that extends SPanel.
 * It contains two SPanel instances (left and right) which occupy exactly 50% of the split panel each.
 */
public class SSplitPanel extends SPanel {

    private SPanel leftPanel;
    private SPanel rightPanel;

    /**
     * Creates a SSplitPanel with two empty SPanel instances.
     */
    public SSplitPanel() {
        this(new SPanel(), new SPanel());
    }

    /**
     * Creates a SSplitPanel with specified left and right SPanel instances.
     *
     * @param left  the SPanel for the left side
     * @param right the SPanel for the right side
     */
    public SSplitPanel(SPanel left, SPanel right) {
        initializeSplitPanel(left, right);
    }

    /**
     * Initializes the SSplitPanel with given left and right panels.
     * Sets the layout and divides the space equally between the two panels.
     *
     * @param left  the SPanel for the left side
     * @param right the SPanel for the right side
     */
    private void initializeSplitPanel(SPanel left, SPanel right) {
        setLayout(new GridLayout(1, 2)); // Grid layout with 1 row and 2 columns
        this.leftPanel = left;
        this.rightPanel = right;
        add(leftPanel);
        add(rightPanel);
    }

    /**
     * Sets or replaces the left panel of the split panel.
     *
     * @param panel the new SPanel to set on the left side
     */
    public void setLeftPanel(SPanel panel) {
        remove(leftPanel); // Remove current left panel
        this.leftPanel = panel;
        add(leftPanel, 0); // Add new panel at index 0 (left side)
        validate();
        repaint();
    }

    /**
     * Sets or replaces the right panel of the split panel.
     *
     * @param panel the new SPanel to set on the right side
     */
    public void setRightPanel(SPanel panel) {
        remove(rightPanel); // Remove current right panel
        this.rightPanel = panel;
        add(rightPanel, 1); // Add new panel at index 1 (right side)
        validate();
        repaint();
    }

    /**
     * Gets the left panel.
     *
     * @return the left SPanel
     */
    public SPanel getLeftPanel() {
        return leftPanel;
    }

    /**
     * Gets the right panel.
     *
     * @return the right SPanel
     */
    public SPanel getRightPanel() {
        return rightPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SSplitPanel::createAndShowGUI);
    }

    /**
     * Creates and shows a SSplitPanel test window.
     * Here you have an example of how to use the SSplitPanel class.
     */
    private static void createAndShowGUI() {
        // Create the main window
        JFrame frame = new JFrame("SSplitPanel Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the SSplitPanel
        SSplitPanel splitPanel = new SSplitPanel();

        // Customize left panel
        SPanel leftPanel = splitPanel.getLeftPanel();
        leftPanel.setBackground(Style.PRIMARY_COLOR); // Set a different background color for visibility

        // Customize right panel
        SPanel rightPanel = splitPanel.getRightPanel();
        rightPanel.setBackground(Style.SECONDARY_COLOR); // Set a different background color for visibility

        // Add the split panel to the frame
        frame.add(splitPanel);

        // Set frame size and make it visible
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);
    }
}
