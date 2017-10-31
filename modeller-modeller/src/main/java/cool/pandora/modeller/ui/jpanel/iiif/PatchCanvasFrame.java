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

package cool.pandora.modeller.ui.jpanel.iiif;

import cool.pandora.modeller.bag.BagInfoField;
import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.handlers.common.IIIFObjectURI;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.dialog.TitlePane;
import org.springframework.richclient.util.GuiStandardUtils;

/**
 * PatchCanvasFrame.
 *
 * @author Christopher Johnson
 */
public class PatchCanvasFrame extends JFrame implements ActionListener {
    protected static final Logger log = LoggerFactory.getLogger(PatchCanvasFrame.class);
    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_FINISH_COMMAND_ID = "okCommand";
    private static final String DEFAULT_CANCEL_COMMAND_ID = "cancelCommand";
    private final JPanel savePanel;
    transient BagView bagView;
    private Map<String, BagInfoField> map;
    private transient ActionCommand finishCommand;
    private transient ActionCommand cancelCommand;

    /**
     * PatchCanvasFrame.
     *
     * @param bagView BagView
     * @param title   String
     */
    public PatchCanvasFrame(final BagView bagView, final String title) {
        super(title);
        this.bagView = bagView;
        if (bagView != null) {
            getContentPane().removeAll();
            savePanel = createComponents();
        } else {
            savePanel = new JPanel();
        }
        getContentPane().add(savePanel, BorderLayout.CENTER);
        final Dimension preferredDimension = new Dimension(600, 400);
        setPreferredSize(preferredDimension);
        this.setBounds(300, 200, 600, 400);
        pack();
    }

    /**
     * getFinishCommandId.
     *
     * @return DEFAULT_FINISH_COMMAND_ID
     */
    private static String getFinishCommandId() {
        return DEFAULT_FINISH_COMMAND_ID;
    }

    /**
     * getCancelCommandId.
     *
     * @return DEFAULT_CANCEL_COMMAND_ID
     */
    private static String getCancelCommandId() {
        return DEFAULT_CANCEL_COMMAND_ID;
    }

    /**
     * buildConstraints.
     *
     * @param gbc    GridBagConstraints
     * @param x      int
     * @param y      int
     * @param w      int
     * @param h      int
     * @param wx     int
     * @param wy     int
     * @param fill   int
     * @param anchor int
     */
    private static void buildConstraints(final GridBagConstraints gbc, final int x, final int y,
                                         final int w, final int h, final int wx, final int wy,
                                         final int fill, final int anchor) {
        gbc.gridx = x; // start cell in a row
        gbc.gridy = y; // start cell in a column
        gbc.gridwidth = w; // how many column does the control occupy in the row
        gbc.gridheight = h; // how many column does the control occupy in the column
        gbc.weightx = wx; // relative horizontal size
        gbc.weighty = wy; // relative vertical size
        gbc.fill = fill; // the way how the control fills cells
        gbc.anchor = anchor; // alignment
    }

    /**
     * createButtonBar.
     *
     * @return buttonBar
     */
    private JComponent createButtonBar() {
        final CommandGroup dialogCommandGroup =
                CommandGroup.createCommandGroup(null, getCommandGroupMembers());
        final JComponent buttonBar = dialogCommandGroup.createButtonBar();
        GuiStandardUtils.attachDialogBorder(buttonBar);
        return buttonBar;
    }

    /**
     * getCommandGroupMembers.
     *
     * @return AbstractCommand
     */
    private Object[] getCommandGroupMembers() {
        return new AbstractCommand[]{finishCommand, cancelCommand};
    }

    /**
     * Initialize the standard commands needed on a Dialog: Ok/Cancel.
     */
    private void initStandardCommands() {
        finishCommand = new ActionCommand(getFinishCommandId()) {
            @Override
            public void doExecuteCommand() {

                new OkPatchCanvasHandler().actionPerformed(null);

            }
        };

        cancelCommand = new ActionCommand(getCancelCommandId()) {

            @Override
            public void doExecuteCommand() {
                new CancelPatchCanvasHandler().actionPerformed(null);
            }
        };
    }

    private JPanel createComponents() {
        final Border border = new EmptyBorder(5, 5, 5, 5);

        final TitlePane titlePane = new TitlePane();
        initStandardCommands();
        final JPanel pageControl = new JPanel(new BorderLayout());
        final JPanel titlePaneContainer = new JPanel(new BorderLayout());
        titlePane.setTitle(bagView.getPropertyMessage("PatchCanvasFrame.title"));
        titlePane.setMessage(new DefaultMessage(bagView.getPropertyMessage("Patch Canvases")));
        titlePaneContainer.add(titlePane.getControl());
        titlePaneContainer.add(new JSeparator(), BorderLayout.SOUTH);
        pageControl.add(titlePaneContainer, BorderLayout.NORTH);
        final JPanel contentPane = new JPanel();

        final DefaultBag bag = bagView.getBag();
        if (bag != null) {
            map = bag.getInfo().getFieldMap();
        }

        final JLabel urlLabel = new JLabel(bagView.getPropertyMessage("baseURL.label"));
        urlLabel.setToolTipText(bagView.getPropertyMessage("baseURL.description"));
        final JTextField urlField = new JTextField("");
        final URI uri = IIIFObjectURI.getCanvasContainerURI(map);
        try {
            urlField.setText(uri != null ? uri.toString() : null);
        } catch (final Exception e) {
            log.error("Failed to set url label", e);
        }

        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints glbc = new GridBagConstraints();
        final JPanel panel = new JPanel(layout);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        int row = 0;

        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE,
                GridBagConstraints.WEST);
        layout.setConstraints(urlLabel, glbc);
        panel.add(urlLabel);
        buildConstraints(glbc, 1, row, 1, 1, 80, 50, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER);
        layout.setConstraints(urlField, glbc);
        panel.add(urlField);
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE,
                GridBagConstraints.WEST);
        buildConstraints(glbc, 1, row, 2, 1, 80, 50, GridBagConstraints.HORIZONTAL,
                GridBagConstraints.CENTER);

        GuiStandardUtils.attachDialogBorder(contentPane);
        pageControl.add(panel);
        final JComponent buttonBar = createButtonBar();
        pageControl.add(buttonBar, BorderLayout.SOUTH);

        this.pack();
        return pageControl;

    }

    /**
     * setBag.
     *
     * @param bag DefaultBag
     */
    public void setBag(final DefaultBag bag) {
        savePanel.invalidate();
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        invalidate();
        repaint();
    }

    /**
     * getMessage.
     *
     * @param property String
     * @return message
     */
    private String getMessage(final String property) {
        return bagView.getPropertyMessage(property);
    }

    private class OkPatchCanvasHandler extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            setVisible(false);
            final String bagFileName = "";
            bagView.getBag().setName(bagFileName);
            bagView.patchCanvasHandler.execute();
        }
    }

    private class CancelPatchCanvasHandler extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            setVisible(false);
        }
    }
}
