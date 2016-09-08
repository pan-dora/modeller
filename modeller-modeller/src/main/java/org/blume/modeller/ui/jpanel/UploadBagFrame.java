/*
 * Copyright 2002-2004 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.blume.modeller.ui.jpanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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

import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.bag.BagInfoField;

public class UploadBagFrame extends JFrame implements ActionListener {
    protected static final Logger log = LoggerFactory.getLogger(SaveBagFrame.class);
    private static final long serialVersionUID = 1L;
    transient BagView bagView;
    File bagFile;
    private Map<String, BagInfoField> map;
    private JPanel savePanel;
    JRadioButton noneButton;
    JRadioButton zipButton;

    public UploadBagFrame(BagView bagView, String title) {
        super(title);
        this.bagView = bagView;
        if (bagView != null) {
            getContentPane().removeAll();
            savePanel = createComponents();
        } else {
            savePanel = new JPanel();
        }
        getContentPane().add(savePanel, BorderLayout.CENTER);
        Dimension preferredDimension = new Dimension(600, 400);
        setPreferredSize(preferredDimension);
        this.setBounds(300, 200, 600, 400);
        pack();
    }

    protected JComponent createButtonBar() {
        CommandGroup dialogCommandGroup = CommandGroup.createCommandGroup(null, getCommandGroupMembers());
        JComponent buttonBar = dialogCommandGroup.createButtonBar();
        GuiStandardUtils.attachDialogBorder(buttonBar);
        return buttonBar;
    }

    protected Object[] getCommandGroupMembers() {
        return new AbstractCommand[]{finishCommand, cancelCommand};
    }

    /**
     * Initialize the standard commands needed on a Dialog: Ok/Cancel.
     */
    private void initStandardCommands() {
        finishCommand = new ActionCommand(getFinishCommandId()) {
            @Override
            public void doExecuteCommand() {

                new OkUploadBagHandler().actionPerformed(null);

            }
        };

        cancelCommand = new ActionCommand(getCancelCommandId()) {

            @Override
            public void doExecuteCommand() {
                new CancelUploadBagHandler().actionPerformed(null);
            }
        };
    }

    protected String getFinishCommandId() {
        return DEFAULT_FINISH_COMMAND_ID;
    }

    protected String getCancelCommandId() {
        return DEFAULT_CANCEL_COMMAND_ID;
    }

    protected static final String DEFAULT_FINISH_COMMAND_ID = "okCommand";

    protected static final String DEFAULT_CANCEL_COMMAND_ID = "cancelCommand";

    private transient ActionCommand finishCommand;

    private transient ActionCommand cancelCommand;

    private JPanel createComponents() {
        Border border = new EmptyBorder(5, 5, 5, 5);

        TitlePane titlePane = new TitlePane();
        initStandardCommands();
        JPanel pageControl = new JPanel(new BorderLayout());
        JPanel titlePaneContainer = new JPanel(new BorderLayout());
        titlePane.setTitle(bagView.getPropertyMessage("UploadBagFrame.title"));
        titlePane.setMessage(new DefaultMessage(bagView.getPropertyMessage("Upload Resources to:")));
        titlePaneContainer.add(titlePane.getControl());
        titlePaneContainer.add(new JSeparator(), BorderLayout.SOUTH);
        pageControl.add(titlePaneContainer, BorderLayout.NORTH);
        JPanel contentPane = new JPanel();
        DefaultBag bag = bagView.getBag();

        if (bag != null) {
            map = bag.getInfo().getFieldMap();
        }

        JLabel urlLabel = new JLabel(bagView.getPropertyMessage("baseURL.label"));
        urlLabel.setToolTipText(bagView.getPropertyMessage("baseURL.description"));
        JTextField urlField = new JTextField("");
        String uri = bagView.uploadBagHandler.getResourceContainer(map);
        try {
            urlField.setText(uri);
        } catch (Exception e) {
            log.error("Failed to set url label", e);
        }
        urlField.setEnabled(false);

        //only if bag is not null
        if (bag != null) {
            urlLabel.setEnabled(true);
        }

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints glbc = new GridBagConstraints();
        JPanel panel = new JPanel(layout);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        int row = 0;

        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST);
        layout.setConstraints(urlLabel, glbc);
        panel.add(urlLabel);
        buildConstraints(glbc, 1, row, 1, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
        layout.setConstraints(urlField, glbc);
        panel.add(urlField);
        row++;
        buildConstraints(glbc, 0, row, 1, 1, 1, 50, GridBagConstraints.NONE, GridBagConstraints.WEST);
        buildConstraints(glbc, 1, row, 2, 1, 80, 50, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);

        GuiStandardUtils.attachDialogBorder(contentPane);
        pageControl.add(panel);
        JComponent buttonBar = createButtonBar();
        pageControl.add(buttonBar, BorderLayout.SOUTH);

        this.pack();
        return pageControl;

    }

    public void setBag(DefaultBag bag) {
        savePanel.invalidate();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        invalidate();
        repaint();
    }

    private class OkUploadBagHandler extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            String bagFileName = "";
            bagView.getBag().setName(bagFileName);
            bagView.uploadBagHandler.execute();
        }
    }

    private class CancelUploadBagHandler extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
        }
    }

    private void buildConstraints(GridBagConstraints gbc, int x, int y, int w, int h, int wx, int wy, int fill, int anchor) {
        gbc.gridx = x; // start cell in a row
        gbc.gridy = y; // start cell in a column
        gbc.gridwidth = w; // how many column does the control occupy in the row
        gbc.gridheight = h; // how many column does the control occupy in the column
        gbc.weightx = wx; // relative horizontal size
        gbc.weighty = wy; // relative vertical size
        gbc.fill = fill; // the way how the control fills cells
        gbc.anchor = anchor; // alignment
    }

    private String getMessage(String property) {
        return bagView.getPropertyMessage(property);
    }
}
