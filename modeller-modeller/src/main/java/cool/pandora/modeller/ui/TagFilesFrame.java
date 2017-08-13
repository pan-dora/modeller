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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 * Tag Files Frame.
 *
 * @author gov.loc
 */
public class TagFilesFrame extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    /**
     * TagFilesFrame.
     *
     * @param title String
     */
    public TagFilesFrame(final String title) {
        super(title);
    }

    /**
     * addComponents.
     *
     * @param tabs JTabbedPane
     */
    public void addComponents(final JTabbedPane tabs) {
        getContentPane().removeAll();
        getContentPane().add(tabs, BorderLayout.CENTER);
        setPreferredSize(tabs.getPreferredSize());
        pack();
    }

    /**
     * actionPerformed.
     *
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        invalidate();
        repaint();
    }

}