/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cool.pandora.modeller.ui;

import java.awt.event.KeyEvent;
import javax.swing.JTextArea;

/**
 * NoTabTextArea.
 *
 * @author gov.loc
 */
public class NoTabTextArea extends JTextArea {
    private static final long serialVersionUID = 1L;

    /**
     * NoTabTextArea.
     *
     * @param row  int
     * @param cols int
     */
    NoTabTextArea(final int row, final int cols) {
        super(row, cols);
        // this.setFocusTraversalKeysEnabled(false);
    }

    /**
     * processComponentKeyEvent.
     *
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
