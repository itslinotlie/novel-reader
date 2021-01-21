package tools;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Button hover styling
 */
public class ButtonStyle implements MouseListener {
    @Override
    public void mouseEntered(MouseEvent e) {
        JButton button = (JButton) e.getSource();
        button.setFocusable(false);
        button.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        button.setBackground(Design.novelButtonBackgroundDark);
    }
    @Override
    public void mouseExited(MouseEvent e) {
        JButton button = (JButton) e.getSource();
        button.setFocusable(false);
        button.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
        button.setBackground(Design.novelButtonBackground);
    }
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
}
