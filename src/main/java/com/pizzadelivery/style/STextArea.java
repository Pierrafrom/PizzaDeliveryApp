package com.pizzadelivery.style;

import javax.swing.*;

/**
 * STextArea is a custom JTextArea class that applies predefined styles from the Style interface.
 * It offers multiple constructors to accommodate common use cases in Swing applications,
 * and handles various text area display options.
 */
public class STextArea extends JTextArea {

    /**
     * Enum for different font styles supported by STextArea.
     */
    public enum FontStyle {
        REGULAR, BOLD, SEMIBOLD, TITLE
    }

    /**
     * Creates a new STextArea with default text.
     */
    public STextArea() {
        this(""); // Calls the constructor with a String parameter
    }

    /**
     * Creates a new STextArea with specified text.
     *
     * @param text The text to be displayed in the text area
     */
    public STextArea(String text) {
        super(text);
        initializeStyle();
    }

    /**
     * Creates a new STextArea with specified text and a specific font style.
     *
     * @param text      The text to be displayed in the text area
     * @param fontStyle The style of font to apply (regular, bold, semibold, title)
     */
    public STextArea(String text, FontStyle fontStyle) {
        super(text);
        initializeStyleWithFont(fontStyle);
    }

    /**
     * Initializes the style of the text area using constants from the Style interface.
     */
    private void initializeStyle() {
        setForeground(Style.TEXT_COLOR);
        setBackground(Style.BACKGROUND_COLOR);
        setFont(Style.TEXT_FONT);
        // Additional default styling can be added here
    }

    /**
     * Initializes the style of the text area with a specific font style.
     *
     * @param fontStyle The style of font to apply
     */
    private void initializeStyleWithFont(FontStyle fontStyle) {
        setForeground(Style.TEXT_COLOR);
        setBackground(Style.BACKGROUND_COLOR);
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
