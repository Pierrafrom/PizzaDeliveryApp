package style;

import javax.swing.*;

/**
 * SLabel is a custom JLabel class that applies predefined styles from the Style interface.
 * It offers multiple constructors to accommodate common use cases in Swing applications,
 * and handles various combinations of text display options.
 */
public class SLabel extends JLabel {

    /**
     * Enum for different font styles supported by SLabel.
     */
    public enum FontStyle {
        REGULAR, BOLD, SEMIBOLD, TITLE
    }

    /**
     * Creates a new SLabel with default text.
     */
    public SLabel() {
        this(""); // Calls the constructor with a String parameter
    }

    /**
     * Creates a new SLabel with specified text.
     *
     * @param text The text to be displayed in the label
     */
    public SLabel(String text) {
        super(text);
        initializeStyle();
    }

    /**
     * Creates a new SLabel with specified text and a specific font style.
     *
     * @param text      The text to be displayed in the label
     * @param fontStyle The style of font to apply (regular, bold, semibold, title)
     */
    public SLabel(String text, FontStyle fontStyle) {
        super(text);
        initializeStyleWithFont(fontStyle);
    }

    /**
     * Initializes the style of the label using constants from the Style interface.
     */
    private void initializeStyle() {
        setForeground(Style.TEXT_COLOR);
        setFont(Style.TEXT_FONT);
        // Additional default styling can be added here
    }

    /**
     * Initializes the style of the label with a specific font style.
     *
     * @param fontStyle The style of font to apply
     */
    private void initializeStyleWithFont(FontStyle fontStyle) {
        setForeground(Style.TEXT_COLOR);
        switch (fontStyle) {
            case TITLE:
                setFont(Style.TITLE_FONT);
                break;
            case BOLD:
                setFont(Style.TEXT_BOLD_FONT);
                break;
            case SEMIBOLD:
                setFont(Style.TEXT_SEMIBOLD_FONT);
                break;
            case REGULAR:
            default:
                setFont(Style.TEXT_FONT);
                break;
        }
    }
}
