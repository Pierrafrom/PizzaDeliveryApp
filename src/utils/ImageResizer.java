package utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * ImageResizer is a utility class for resizing images in a Java application.
 * It provides methods to resize an image while maintaining its aspect ratio and quality.
 */
public class ImageResizer {

    /**
     * Resizes an image to a specified width and height.
     * This method maintains the original aspect ratio of the image.
     *
     * @param originalImage The original image to be resized.
     * @param targetWidth   The desired width.
     * @param targetHeight  The desired height.
     * @return A new BufferedImage object containing the resized image.
     * <p>
     * Example Usage:
     * BufferedImage originalImage = ImageIO.read(new File("path/to/image.jpg"));
     * BufferedImage resizedImage = ImageResizer.resizeImage(originalImage, 100, 100);
     */
    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        // Calculate the new dimensions while maintaining the aspect ratio
        double ratio = Math.min((double) targetWidth / originalImage.getWidth(),
                (double) targetHeight / originalImage.getHeight());
        int width = (int) (originalImage.getWidth() * ratio);
        int height = (int) (originalImage.getHeight() * ratio);

        // Create a new buffered image with the new size
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();

        // Draw the original image, scaled to the new size
        g2d.drawImage(originalImage, 0, 0, width, height, null);
        g2d.dispose();

        return resizedImage;
    }

    /**
     * Converts an ImageIcon to a BufferedImage.
     *
     * @param icon The ImageIcon to be converted.
     * @return A BufferedImage representing the ImageIcon.
     * <p>
     * Example Usage:
     * ImageIcon icon = new ImageIcon("path/to/icon.jpg");
     * BufferedImage image = ImageResizer.iconToBufferedImage(icon);
     * BufferedImage resizedImage = ImageResizer.resizeImage(image, 100, 100);
     */
    public static BufferedImage iconToBufferedImage(ImageIcon icon) {
        Image image = icon.getImage();
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image onto the buffered image
        Graphics2D bGr = bufferedImage.createGraphics();
        bGr.drawImage(image, 0, 0, null);
        bGr.dispose();

        return bufferedImage;
    }
}
