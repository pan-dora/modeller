package cool.pandora.modeller.ui;

import javax.swing.JTextArea;
import java.awt.event.KeyEvent;

/**
 * NoTabTextArea
 *
 * @author gov.loc
 */
public class NoTabTextArea extends JTextArea {
    private static final long serialVersionUID = 1L;

    /**
     * @param row  int
     * @param cols int
     */
    NoTabTextArea(final int row, final int cols) {
        super(row, cols);
        // this.setFocusTraversalKeysEnabled(false);
    }

    /**
     * @param e KeyEvent
     */
    @Override
    protected void processComponentKeyEvent(final KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_TAB) {
            e.consume();
            if (e.isShiftDown()) {
                transferFocusBackward();
            } else {
                transferFocus();
            }
        } else {
            super.processComponentKeyEvent(e);
        }
    }
}
