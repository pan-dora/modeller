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

package cool.pandora.modeller.ui.jpanel.base;

import cool.pandora.modeller.model.BagStatus;
import cool.pandora.modeller.ui.StatusImageLabel;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import cool.pandora.modeller.ui.util.LayoutUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ConsolePane.
 *
 * @author gov.loc
 */
public class ConsolePane extends JPanel {
    public static final String CONSOLE_PANE = "consolePane";
    protected static final Logger log = LoggerFactory.getLogger(ConsolePane.class);
    private static final int MAX_CONSOLE_MESSAGE_LENGTH = 50000;
    private static final long serialVersionUID = -4290352509246639528L;
    private final Dimension maxDimension = new Dimension(400, 300);
    private final Dimension preferredDimension = new Dimension(400, 150);
    private final Color textBackground = new Color(240, 240, 240);
    private final Border emptyBorder = new EmptyBorder(10, 10, 10, 10);
    private JTextArea serializedArea;

    /**
     * ConsolePane.
     *
     * @param messages String
     */
    public ConsolePane(final String messages) {
        super();
        this.setLayout(new GridBagLayout());
        createFormControl();
        addConsoleMessages(messages);
    }

    /**
     * createStatusPanel.
     *
     * @return statusPanel
     */
    private static JPanel createStatusPanel() {
        final JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new GridBagLayout());
        final int row = 0;
        int col = 0;
        final BagStatus bagStatus = BagStatus.getInstance();

        // Complete Status
        final JLabel completeLabel = new JLabel(
                ApplicationContextUtil.getMessage("compositePane.message.isComplete") + " ");
        completeLabel.setToolTipText(
                ApplicationContextUtil.getMessage("consolepane.iscomplete" + ".help"));
        GridBagConstraints gbc = LayoutUtil
                .buildGridBagConstraints(col++, row, 1, 1, 0, 0, GridBagConstraints.NONE,
                        GridBagConstraints.WEST);
        statusPanel.add(completeLabel, gbc);

        final JLabel completeStatus = new StatusImageLabel(bagStatus.getCompletenessStatus());
        gbc = LayoutUtil
                .buildGridBagConstraints(col++, row, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST);
        statusPanel.add(completeStatus, gbc);

        // Validation Status
        final JLabel validationLabel = new JLabel(
                ApplicationContextUtil.getMessage("compositePane.message.isValid") + "" + " ");
        validationLabel
                .setToolTipText(ApplicationContextUtil.getMessage("consolepane.isvalid" + ".help"));
        gbc = LayoutUtil.buildGridBagConstraints(col++, row, 1, 1, 0, 0, GridBagConstraints.NONE,
                GridBagConstraints.WEST);
        statusPanel.add(validationLabel, gbc);

        final JLabel validationStatus = new StatusImageLabel(bagStatus.getValidationStatus());
        gbc = LayoutUtil
                .buildGridBagConstraints(col++, row, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST);
        statusPanel.add(validationStatus, gbc);

        // Profile Compliance Status
        final JLabel profileComplianceLabel = new JLabel(
                ApplicationContextUtil.getMessage("compositePane.message" + "" + ".isMetadata")
                        + " ");
        profileComplianceLabel.setToolTipText(
                ApplicationContextUtil.getMessage("consolepane" + ".ismetadata.help"));
        gbc = LayoutUtil.buildGridBagConstraints(col++, row, 1, 1, 0, 0, GridBagConstraints.NONE,
                GridBagConstraints.WEST);
        statusPanel.add(profileComplianceLabel, gbc);

        final JLabel profileComplianceStatus =
                new StatusImageLabel(bagStatus.getProfileComplianceStatus());
        gbc = LayoutUtil
                .buildGridBagConstraints(col++, row, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST);
        statusPanel.add(profileComplianceStatus, gbc);

        return statusPanel;
    }

    /**
     * createFormControl.
     */
    private void createFormControl() {
        this.setMaximumSize(maxDimension);

        GridBagConstraints gbc = LayoutUtil
                .buildGridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST);
        this.add(createStatusPanel(), gbc);

        gbc = LayoutUtil.buildGridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.BOTH,
                GridBagConstraints.CENTER);
        this.add(createConsoleArea(), gbc);

        this.setBorder(emptyBorder);
        this.setPreferredSize(preferredDimension);

    }

    /**
     * createConsoleArea.
     *
     * @return JScrollPane
     */
    private JScrollPane createConsoleArea() {
        serializedArea = new JTextArea();
        serializedArea.setToolTipText(ApplicationContextUtil.getMessage("consolepane.msg.help"));

        serializedArea.setEditable(false);
        serializedArea.setLineWrap(true);
        serializedArea.setBackground(textBackground);
        serializedArea.setWrapStyleWord(true);
        serializedArea.setAutoscrolls(true);
        serializedArea.setBorder(BorderFactory.createLineBorder(Color.black));

        return new JScrollPane(serializedArea);

    }

    /**
     * addConsoleMessages.
     *
     * @param message String
     */
    public void addConsoleMessages(final String message) {
        if (message != null && message.trim().length() != 0) {
            final Document consoleMessageDoc = serializedArea.getDocument();
            final String date = new Date().toString();
            serializedArea.append("\n[" + date + "]: " + message);

            if (consoleMessageDoc.getLength() > MAX_CONSOLE_MESSAGE_LENGTH) {
                try {
                    consoleMessageDoc
                            .remove(0, consoleMessageDoc.getLength() - MAX_CONSOLE_MESSAGE_LENGTH);
                } catch (BadLocationException e) {
                    log.error("Could not remove message from console", e);
                    throw new RuntimeException(e);
                }
            }
            serializedArea.setAutoscrolls(true);
            serializedArea.setCaretPosition(consoleMessageDoc.getLength());
        }
    }

    /**
     * clearConsoleMessages.
     */
    public void clearConsoleMessages() {
        serializedArea.setText("");
    }
}
