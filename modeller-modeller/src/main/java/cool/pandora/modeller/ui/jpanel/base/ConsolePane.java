package cool.pandora.modeller.ui.jpanel.base;

import cool.pandora.modeller.model.BagStatus;
import cool.pandora.modeller.ui.StatusImageLabel;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import cool.pandora.modeller.ui.util.LayoutUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Date;

/**
 * ConsolePane
 *
 * @author gov.loc
 */
public class ConsolePane extends JPanel {
    private static final int MAX_CONSOLE_MESSAGE_LENGTH = 50000;

    private static final long serialVersionUID = -4290352509246639528L;
    protected static final Logger log = LoggerFactory.getLogger(ConsolePane.class);

    public static final String CONSOLE_PANE = "consolePane";
    private final Dimension maxDimension = new Dimension(400, 300);
    private final Dimension preferredDimension = new Dimension(400, 150);
    private final Color textBackground = new Color(240, 240, 240);
    private final Border emptyBorder = new EmptyBorder(10, 10, 10, 10);
    private JTextArea serializedArea;

    /**
     * @param messages String
     */
    public ConsolePane(final String messages) {
        super();
        this.setLayout(new GridBagLayout());
        createFormControl();
        addConsoleMessages(messages);
    }

    /**
     *
     */
    private void createFormControl() {
        this.setMaximumSize(maxDimension);

        GridBagConstraints gbc = LayoutUtil
                .buildGridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
        this.add(createStatusPannel(), gbc);

        gbc = LayoutUtil.buildGridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        this.add(createConsoleArea(), gbc);

        this.setBorder(emptyBorder);
        this.setPreferredSize(preferredDimension);

    }

    /**
     * @return statusPanel
     */
    private static JPanel createStatusPannel() {
        final JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new GridBagLayout());
        final int row = 0;
        int col = 0;
        final BagStatus bagStatus = BagStatus.getInstance();

        // Complete Status
        final JLabel completeLabel =
                new JLabel(ApplicationContextUtil.getMessage("compositePane.message.isComplete") + " ");
        completeLabel.setToolTipText(ApplicationContextUtil.getMessage("consolepane.iscomplete.help"));
        GridBagConstraints gbc = LayoutUtil
                .buildGridBagConstraints(col++, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        statusPanel.add(completeLabel, gbc);

        final JLabel completeStatus = new StatusImageLabel(bagStatus.getCompletenessStatus());
        gbc = LayoutUtil.buildGridBagConstraints(col++, row, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST);
        statusPanel.add(completeStatus, gbc);

        // Validation Status
        final JLabel validationLabel =
                new JLabel(ApplicationContextUtil.getMessage("compositePane.message.isValid") + " ");
        validationLabel.setToolTipText(ApplicationContextUtil.getMessage("consolepane.isvalid.help"));
        gbc = LayoutUtil
                .buildGridBagConstraints(col++, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        statusPanel.add(validationLabel, gbc);

        final JLabel validationStatus = new StatusImageLabel(bagStatus.getValidationStatus());
        gbc = LayoutUtil.buildGridBagConstraints(col++, row, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST);
        statusPanel.add(validationStatus, gbc);

        // Profile Compliance Status
        final JLabel profileComplianceLabel =
                new JLabel(ApplicationContextUtil.getMessage("compositePane.message" + ".isMetadata") + " ");
        profileComplianceLabel.setToolTipText(ApplicationContextUtil.getMessage("consolepane.ismetadata.help"));
        gbc = LayoutUtil
                .buildGridBagConstraints(col++, row, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.WEST);
        statusPanel.add(profileComplianceLabel, gbc);

        final JLabel profileComplianceStatus = new StatusImageLabel(bagStatus.getProfileComplianceStatus());
        gbc = LayoutUtil.buildGridBagConstraints(col++, row, 1, 1, 1, 1, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.WEST);
        statusPanel.add(profileComplianceStatus, gbc);

        return statusPanel;
    }

    /**
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
     * @param message String
     */
    public void addConsoleMessages(final String message) {
        if (message != null && message.trim().length() != 0) {
            final Document consoleMessageDoc = serializedArea.getDocument();
            final String date = new Date().toString();
            serializedArea.append("\n[" + date + "]: " + message);

            if (consoleMessageDoc.getLength() > MAX_CONSOLE_MESSAGE_LENGTH) {
                try {
                    consoleMessageDoc.remove(0, consoleMessageDoc.getLength() - MAX_CONSOLE_MESSAGE_LENGTH);
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
     *
     */
    public void clearConsoleMessages() {
        serializedArea.setText("");
    }
}
