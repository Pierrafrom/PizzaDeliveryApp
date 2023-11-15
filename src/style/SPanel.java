package style;

import javax.swing.*;
import java.awt.*;

/**
 * SPanel is a custom JPanel class that uses predefined styles from the Style interface.
 * It provides various constructors to handle common use cases in Swing application development.
 */
public class SPanel extends JPanel {

    /**
     * Creates a new SPanel with a default layout manager and preferred size.
     */
    public SPanel() {
        this(null, new Dimension(200, 200)); // Default size can be adjusted as needed
    }

    /**
     * Creates a new SPanel with a specified layout manager and a default preferred size.
     *
     * @param layout the LayoutManager to use
     */
    public SPanel(LayoutManager layout) {
        this(layout, new Dimension(200, 200)); // Default size can be adjusted as needed
    }

    /**
     * Creates a new SPanel with a specified layout manager and preferred size.
     *
     * @param layout        the LayoutManager to use
     * @param preferredSize the preferred size of the panel
     */
    public SPanel(LayoutManager layout, Dimension preferredSize) {
        super(layout);
        setPreferredSize(preferredSize);
        initializeStyle();
    }

    /**
     * Initializes the style of the panel using constants from the Style interface.
     */
    private void initializeStyle() {
        setBackground(Style.BACKGROUND_COLOR);
    }

}
