import style.SButton;
import style.SButton.ButtonType;
import style.SFrame;
import style.SLabel;
import style.SPanel;
import utils.ImageLoader;
import utils.ImageResizer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        // Création de la fenêtre principale
        SFrame frame = new SFrame("Test SComponents", 400, 300);

        // Création et ajout des composants stylisés
        JPanel contentPanel = new SPanel();
        contentPanel.setLayout(new FlowLayout());

        // Test SButton
        SButton primaryButton = new SButton("Primary", ButtonType.PRIMARY);
        SButton secondaryButton = new SButton("Secondary", ButtonType.SECONDARY);
        SButton errorButton = new SButton("Error", ButtonType.ERROR);

        // Test SLabel
        SLabel label = new SLabel("Test Label", SLabel.FontStyle.TITLE);

        ImageIcon imageIcon = ImageLoader.loadImageIcon("logo.png"); // Assurez-vous que "logo.png" est le bon chemin
        if (imageIcon != null) {
            // Convertir ImageIcon en BufferedImage
            BufferedImage bufferedImage = ImageResizer.iconToBufferedImage(imageIcon);

            // Redimensionner l'image
            BufferedImage resizedImage = ImageResizer.resizeImage(bufferedImage, 100, 100);

            // Créer un nouvel ImageIcon à partir de l'image redimensionnée
            ImageIcon resizedIcon = new ImageIcon(resizedImage);

            // Ajouter l'image redimensionnée à un JLabel et l'ajouter au panneau
            JLabel imageLabel = new JLabel(resizedIcon);
            contentPanel.add(imageLabel);
        } else {
            System.err.println("Image not found or could not be loaded.");
        }

        // Ajout des composants au panel
        contentPanel.add(primaryButton);
        contentPanel.add(secondaryButton);
        contentPanel.add(errorButton);
        contentPanel.add(label);

        // Ajout du panel à la fenêtre
        frame.setContentPane(contentPanel);
        frame.setVisible(true);
    }
}
