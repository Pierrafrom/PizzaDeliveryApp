package utils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * ImageLoader is a utility class for loading images into a Java Swing application.
 * It provides methods to load an image from the resources directory.
 */
public class ImageLoader {
    // Path to the directory where image files are stored
    private static final String IMAGES_DIRECTORY = "/ressources/img/";

    /**
     * Loads an image from the specified file name within the image directory.
     * The image file should be located in the resources/images directory of the project.
     *
     * @param imageFileName The name of the image file (e.g., "MyImage.png")
     * @return The loaded Image object, or null if the image could not be loaded
     */
    public static Image loadImage(String imageFileName) {
        try {
            URL imageUrl = ImageLoader.class.getResource(IMAGES_DIRECTORY + imageFileName);
            if (imageUrl == null) {
                throw new IOException("Image file not found: " + imageFileName);
            }
            return Toolkit.getDefaultToolkit().getImage(imageUrl);
        } catch (IOException e) {
            System.err.println("Error reading image file: " + e.getMessage());
            return null;
        }
    }

    /**
     * Loads an ImageIcon from the specified file name within the image directory.
     * This is useful for loading images that will be used in components that accept ImageIcon.
     *
     * @param imageFileName The name of the image file (e.g., "MyImage.png")
     * @return The loaded ImageIcon object, or null if the image could not be loaded
     */
    public static ImageIcon loadImageIcon(String imageFileName) {
        Image image = loadImage(imageFileName);
        return (image != null) ? new ImageIcon(image) : null;
    }
}
