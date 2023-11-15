package style;

import javax.swing.*;

/**
 * SFrame is a custom JFrame class that applies predefined styles from the Style interface.
 * It offers multiple constructors to accommodate common use cases in Swing applications.
 */
public class SFrame extends JFrame {

    /**
     * Creates a new SFrame with a default title and size.
     */
    public SFrame() {
        this("SFrame", 400, 300); // Default size can be adjusted as needed
    }

    /**
     * Creates a new SFrame with a specified title and a default size.
     *
     * @param title The title of the frame
     */
    public SFrame(String title) {
        this(title, 400, 300); // Default size can be adjusted as needed
    }

    /**
     * Creates a new SFrame with a specified title and size.
     *
     * @param title  The title of the frame
     * @param width  The width of the frame
     * @param height The height of the frame
     */
    public SFrame(String title, int width, int height) {
        super(title);
        initializeFrame(width, height);
    }

    /**
     * Initializes the frame with specified width and height.
     *
     * @param width  The width of the frame
     * @param height The height of the frame
     */
    private void initializeFrame(int width, int height) {
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame
        applyStyle();
    }

    /**
     * Applies the styles defined in the Style interface to the frame.
     */
    private void applyStyle() {
        getContentPane().setBackground(Style.BACKGROUND_COLOR);
    }
}
