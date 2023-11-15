package style;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * SButton is a custom JButton class that applies predefined styles from the Style interface.
 * It offers constructors to create primary, secondary, and error buttons with a consistent look.
 */
public class SButton extends JButton {

    /**
     * Enum for different button types supported by SButton.
     */
    public enum ButtonType {
        PRIMARY, SECONDARY, ERROR
    }

    /**
     * Creates a new SButton with a specified type and text.
     *
     * @param text The text to be displayed on the button
     * @param type The type of the button (primary, secondary, or error)
     */
    public SButton(String text, ButtonType type) {
        super(text);
        initializeStyle(type, 5);
        initializeHoverEffect(type, 5);
    }

    /**
     * Creates a new SButton with a specified type and text.
     *
     * @param text    The text to be displayed on the button
     * @param type    The type of the button (primary, secondary, or error)
     * @param padding The padding to apply to the button
     */
    public SButton(String text, ButtonType type, int padding) {
        super(text);
        initializeStyle(type, padding);
        initializeHoverEffect(type, padding);
    }

    /**
     * Initializes the style of the button based on its type.
     *
     * @param type The type of the button
     */
    private void initializeStyle(ButtonType type, int padding) {
        setFont(new Font("Barlow", Font.BOLD, 16)); // Equivalent to 1rem and font-weight 600
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(true);
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        Color borderColor;
        switch (type) {
            case PRIMARY:
                setBackground(Style.PRIMARY_COLOR);
                setForeground(Style.TEXT_COLOR);
                borderColor = Style.PRIMARY_COLOR;
                break;
            case SECONDARY:
                setBackground(Style.TEXT_COLOR);
                setForeground(Style.PRIMARY_COLOR);
                borderColor = Style.PRIMARY_COLOR; // Ou une autre couleur si nécessaire
                break;
            case ERROR:
                setBackground(Style.SECONDARY_COLOR);
                setForeground(Style.TEXT_COLOR);
                borderColor = Style.SECONDARY_COLOR;
                break;
            default:
                borderColor = Style.PRIMARY_COLOR;
        }

        // Création du CompoundBorder avec LineBorder et EmptyBorder
        Border lineBorder = BorderFactory.createLineBorder(borderColor, 1, true);
        Border paddingBorder = new EmptyBorder(padding, padding, padding, padding);
        setBorder(new CompoundBorder(lineBorder, paddingBorder));
    }

    /**
     * Initializes hover effect for the button.
     *
     * @param type The type of the button
     */
    private void initializeHoverEffect(ButtonType type, int padding) {
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                Color hoverBorderColor = switch (type) {
                    case PRIMARY -> {
                        setBackground(Style.PRIMARY_HOVER_COLOR);
                        yield Style.PRIMARY_HOVER_COLOR;
                    }
                    case SECONDARY -> {
                        setForeground(Style.PRIMARY_HOVER_COLOR);
                        yield Style.TEXT_COLOR;
                    }
                    case ERROR -> {
                        setBackground(Style.SECONDARY_HOVER_COLOR);
                        yield Style.SECONDARY_HOVER_COLOR;
                    }
                };

                Border lineBorder = BorderFactory.createLineBorder(hoverBorderColor, 1, true);
                Border paddingBorder = new EmptyBorder(padding, padding, padding, padding);
                setBorder(new CompoundBorder(lineBorder, paddingBorder));
            }

            public void mouseExited(MouseEvent e) {
                initializeStyle(type, padding); // Reset to original style when mouse exits
            }
        });
    }
}
