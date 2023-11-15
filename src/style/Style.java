package style;

import utils.FontLoader;

import java.awt.*;

/**
 * The Style interface contains constants for colors and fonts used throughout the application.
 * It defines a standard look and feel by providing a set of pre-defined colors and custom fonts.
 */
public interface Style {

    // Base colors
    /**
     * Primary color used for major elements in the application, a shade of green.
     */
    Color PRIMARY_COLOR = Color.decode("#4FA053");

    /**
     * Secondary color used for secondary elements, a shade of red.
     */
    Color SECONDARY_COLOR = Color.decode("#AD2831");

    /**
     * Neutral color, a shade of grey, used for neutral elements.
     */
    Color NEUTRAL_COLOR = Color.decode("#857C8D");

    /**
     * Background color for the application, a dark shade.
     */
    Color BACKGROUND_COLOR = Color.decode("#202020");

    /**
     * Text color, generally white for readability against darker backgrounds.
     */
    Color TEXT_COLOR = Color.decode("#FFFFFF");

    // Hover state colors
    /**
     * Color for text hover state, a lighter white with 30% opacity.
     */
    Color TEXT_HOVER_COLOR = new Color(255, 255, 255, 77);

    /**
     * Primary hover color, a darker shade of the primary color.
     */
    Color PRIMARY_HOVER_COLOR = Color.decode("#305231");

    /**
     * Secondary hover color, a darker shade of the secondary color.
     */
    Color SECONDARY_HOVER_COLOR = Color.decode("#811d23");

    // Additional colors
    /**
     * Color used for glass effects, a very light white with 10% opacity.
     */
    Color GLASS_COLOR = new Color(255, 255, 255, 26);

    /**
     * Fully transparent color.
     */
    Color TRANSPARENT_COLOR = new Color(0, 0, 0, 0);

    // Fonts names and sizes
    /**
     * The file name of the font used for titles.
     */
    String TITLE_FONT_NAME = "LobsterTwo-Bold.otf";

    /**
     * The file name of the font used for regular text.
     */
    String TEXT_FONT_NAME = "Barlow-Regular.otf";

    String TEXT_BOLD_FONT_NAME = "Barlow-Bold.otf";
    String TEXT_SEMIBOLD_FONT_NAME = "Barlow-SemiBold.otf";

    /**
     * The size of the title font.
     */
    float TITLE_FONT_SIZE = 48f;

    /**
     * The size of the regular text font.
     */
    float TEXT_FONT_SIZE = 16f;

    /**
     * The size of the regular text font.
     */
    float TEXT_BOLD_FONT_SIZE = 16f;

    /**
     * The size of the regular text font.
     */
    float TEXT_SEMIBOLD_FONT_SIZE = 16f;

    // Fonts
    /**
     * The custom font used for titles throughout the application.
     */
    Font TITLE_FONT = FontLoader.loadFont(TITLE_FONT_NAME, TITLE_FONT_SIZE);

    /**
     * The custom font used for regular text throughout the application.
     */
    Font TEXT_FONT = FontLoader.loadFont(TEXT_FONT_NAME, TEXT_FONT_SIZE);

    /**
     * The custom font used for regular text throughout the application.
     */
    Font TEXT_BOLD_FONT = FontLoader.loadFont(TEXT_BOLD_FONT_NAME, TEXT_BOLD_FONT_SIZE);

    /**
     * The custom font used for regular text throughout the application.
     */
    Font TEXT_SEMIBOLD_FONT = FontLoader.loadFont(TEXT_SEMIBOLD_FONT_NAME, TEXT_SEMIBOLD_FONT_SIZE);
}
