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

import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import cool.pandora.modeller.ui.util.LayoutUtil;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationPage;
import org.springframework.richclient.application.PageComponent;
import org.springframework.richclient.command.AbstractCommand;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.core.DefaultMessage;
import org.springframework.richclient.dialog.TitlePane;
import org.springframework.richclient.util.GuiStandardUtils;

/**
 * NewBagInPlaceFrame.
 *
 * @author gov.loc
 */
public class NewBagInPlaceFrame extends JFrame implements ActionListener {
    protected static final Logger log = LoggerFactory.getLogger(NewBagInPlaceFrame.class);
    private static final long serialVersionUID = 1L;
    private final transient BagView bagView;
    private transient DefaultBag bag = null;
    private final JPanel createPanel;
    private JTextField bagNameField;
    private File bagFile;
    private JComboBox<String> profileList;

    /**
     * NewBagInPlaceFrame.
     *
     * @param bagView BagView
     * @param title String
     */
    public NewBagInPlaceFrame(final BagView bagView, final String title) {
        super(title);
        final Application app = Application.instance();
        final ApplicationPage page = app.getActiveWindow().getPage();
        final PageComponent component = page.getActiveComponent();
        if (component != null) {
            this.bagView = ApplicationContextUtil.getBagView();
        } else {
            this.bagView = bagView;
        }
        if (bagView != null) {
            bag = bagView.getBag();
            getContentPane().removeAll();
            createPanel = createComponents();
        } else {
            createPanel = new JPanel();
        }
        getContentPane().add(createPanel, BorderLayout.CENTER);
        final Dimension preferredDimension = new Dimension(400, 230);
        setPreferredSize(preferredDimension);
        setLocation(200, 100);
        pack();
    }

    /**
     * createComponents.
     *
     * @return pageControl
     */
    private JPanel createComponents() {

        final TitlePane titlePane = new TitlePane();
        initStandardCommands();
        final JPanel pageControl = new JPanel(new BorderLayout());
        final JPanel titlePaneContainer = new JPanel(new BorderLayout());
        titlePane.setTitle(bagView.getPropertyMessage("NewBagInPlace.title"));
        titlePane.setMessage(new DefaultMessage(bagView.getPropertyMessage("NewBagInPlace"
                + ".description")));
        titlePaneContainer.add(titlePane.getControl());
        titlePaneContainer.add(new JSeparator(), BorderLayout.SOUTH);
        pageControl.add(titlePaneContainer, BorderLayout.NORTH);

        final JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        int row = 0;
        layoutSelectDataContent(contentPanel, row++);
        layoutProfileSelectionContent(contentPanel, row++);
        layoutAddKeepFilesToEmptyCheckBox(contentPanel, row++);
        layoutSpacer(contentPanel, row++);

        GuiStandardUtils.attachDialogBorder(contentPanel);
        pageControl.add(contentPanel);
        final JComponent buttonBar = createButtonBar();
        pageControl.add(buttonBar, BorderLayout.SOUTH);

        this.pack();
        return pageControl;

    }

    /**
     * layoutSelectDataContent.
     *
     * @param contentPanel JPanel
     * @param row int
     */
    private void layoutSelectDataContent(final JPanel contentPanel, final int row) {

        final JLabel location = new JLabel("Select Data:");
        final JButton saveAsButton = new JButton(bagView.getPropertyMessage("bag.button.browse"));
        saveAsButton.addActionListener(new BrowseFileHandler());
        saveAsButton.setEnabled(true);
        saveAsButton.setToolTipText(bagView.getPropertyMessage("bag.button.browse.help"));

        String fileName = "";
        if (bag != null) {
            fileName = bag.getName();
        }
        bagNameField = new JTextField(fileName);
        bagNameField.setCaretPosition(fileName.length());
        bagNameField.setEditable(false);
        bagNameField.setEnabled(false);

        GridBagConstraints glbc = new GridBagConstraints();
        glbc = LayoutUtil
                .buildGridBagConstraints(0, row, 1, 1, 1, 50, GridBagConstraints.NONE,
                        GridBagConstraints.WEST);
        contentPanel.add(location, glbc);

        glbc = LayoutUtil
                .buildGridBagConstraints(2, row, 1, 1, 1, 50, GridBagConstraints.NONE,
                        GridBagConstraints.EAST);
        contentPanel.add(saveAsButton, glbc);

        glbc = LayoutUtil
                .buildGridBagConstraints(1, row, 1, 1, 80, 50, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST);
        glbc.ipadx = 0;
        contentPanel.add(bagNameField, glbc);
    }

    /**
     * layoutProfileSelectionContent.
     *
     * @param contentPane JPanel
     * @param row int
     */
    private void layoutProfileSelectionContent(final JPanel contentPane, final int row) {
        // content
        // profile selection
        final JLabel bagProfileLabel = new JLabel(bagView.getPropertyMessage("Select Profile:"));
        bagProfileLabel.setToolTipText(bagView.getPropertyMessage("bag.projectlist.help"));

        profileList = new JComboBox<>(bagView.getProfileStore().getProfileNames());
        profileList.setName(bagView.getPropertyMessage("bag.label.projectlist"));
        profileList.setSelectedItem(bagView.getPropertyMessage("bag.project.noproject"));
        profileList.setToolTipText(bagView.getPropertyMessage("bag.projectlist.help"));

        GridBagConstraints glbc = new GridBagConstraints();

        final JLabel spacerLabel = new JLabel();
        glbc = LayoutUtil
                .buildGridBagConstraints(0, row, 1, 1, 5, 50, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST);
        contentPane.add(bagProfileLabel, glbc);
        glbc = LayoutUtil.buildGridBagConstraints(1, row, 1, 1, 40, 50, GridBagConstraints
                        .HORIZONTAL,
                GridBagConstraints.CENTER);
        contentPane.add(profileList, glbc);
        glbc = LayoutUtil
                .buildGridBagConstraints(2, row, 1, 1, 40, 50, GridBagConstraints.NONE,
                        GridBagConstraints.EAST);
        contentPane.add(spacerLabel, glbc);
    }

    /**
     * AddKeepFilesToEmptyFoldersHandler.
     *
     * <p>The actionPerformed method in this class
     * is called each time the ".keep Files in Empty Folder(s):" Check Box
     * is Selected
     */
    private class AddKeepFilesToEmptyFoldersHandler extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(final ActionEvent e) {

            final JCheckBox cb = (JCheckBox) e.getSource();

            // Determine status
            final boolean isSelected = cb.isSelected();
            if (isSelected) {
                bagView.getBag().isAddKeepFilesToEmptyFolders(true);
                bagView.infoInputPane.serializeValue.setText("true");
            } else {
                bagView.getBag().isAddKeepFilesToEmptyFolders(false);
            }
        }
    }

    /**
     * layoutAddKeepFilesToEmptyCheckBox.
     *
     * <p>Setting and displaying the ".keep Files in Empty Folder(s):" Check Box
     * on the Create Bag In Place Pane
     */
    private void layoutAddKeepFilesToEmptyCheckBox(final JPanel contentPane, final int row) {
        // Delete Empty Folder(s)
        final JLabel addKeepFilesToEmptyFoldersCheckBoxLabel =
                new JLabel(bagView.getPropertyMessage("bag.label" + ".addkeepfilestoemptyfolders"));
        addKeepFilesToEmptyFoldersCheckBoxLabel
                .setToolTipText(bagView.getPropertyMessage("bag" + ".addkeepfilestoemptyfolders"
                        + ".help"));
        final JCheckBox addKeepFilesToEmptyFoldersCheckBox = new JCheckBox(bagView
                .getPropertyMessage(""));
        addKeepFilesToEmptyFoldersCheckBox.setSelected(bag.isAddKeepFilesToEmptyFolders());
        addKeepFilesToEmptyFoldersCheckBox.addActionListener(new
                AddKeepFilesToEmptyFoldersHandler());

        GridBagConstraints glbc = new GridBagConstraints();

        final JLabel spacerLabel = new JLabel();
        glbc = LayoutUtil
                .buildGridBagConstraints(0, row, 1, 1, 5, 50, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST);
        contentPane.add(addKeepFilesToEmptyFoldersCheckBoxLabel, glbc);
        glbc = LayoutUtil.buildGridBagConstraints(1, row, 1, 1, 40, 50, GridBagConstraints
                        .HORIZONTAL,
                GridBagConstraints.CENTER);
        contentPane.add(addKeepFilesToEmptyFoldersCheckBox, glbc);
        glbc = LayoutUtil
                .buildGridBagConstraints(2, row, 1, 1, 40, 50, GridBagConstraints.NONE,
                        GridBagConstraints.EAST);
        contentPane.add(spacerLabel, glbc);
    }

    /**
     * layoutSpacer.
     *
     * @param contentPanel JPanel
     * @param row int
     */
    private static void layoutSpacer(final JPanel contentPanel, final int row) {
        GridBagConstraints glbc = new GridBagConstraints();
        glbc = LayoutUtil
                .buildGridBagConstraints(0, row, 1, 1, 1, 50, GridBagConstraints.NONE,
                        GridBagConstraints.WEST);
        final JLabel spacerLabel = new JLabel("");
        contentPanel.add(spacerLabel, glbc);
    }

    /**
     * createButtonBar.
     *
     * @return buttonBar
     */
    protected JComponent createButtonBar() {
        final CommandGroup dialogCommandGroup = CommandGroup.createCommandGroup(null,
                getCommandGroupMembers());
        final JComponent buttonBar = dialogCommandGroup.createButtonBar();
        GuiStandardUtils.attachDialogBorder(buttonBar);
        return buttonBar;
    }

    /**
     * getCommandGroupMembers.
     *
     * @return AbstractCommand
     */
    protected Object[] getCommandGroupMembers() {
        return new AbstractCommand[]{finishCommand, cancelCommand};
    }

    /**
     * initStandardCommands.
     *
     * <p>Initialize the standard commands needed on a Dialog: Ok/Cancel.
     */
    private void initStandardCommands() {
        finishCommand = new ActionCommand(getFinishCommandId()) {
            @Override
            public void doExecuteCommand() {

                new OkNewBagHandler().actionPerformed(null);

            }
        };

        cancelCommand = new ActionCommand(getCancelCommandId()) {
            @Override
            public void doExecuteCommand() {
                new CancelNewBagHandler().actionPerformed(null);
            }
        };
    }

    /**
     * getFinishCommandId.
     *
     * @return DEFAULT_FINISH_COMMAND_ID
     */
    protected static String getFinishCommandId() {
        return DEFAULT_FINISH_COMMAND_ID;
    }

    /**
     * getCancelCommandId.
     *
     * @return DEFAULT_CANCEL_COMMAND_ID
     */
    protected static String getCancelCommandId() {
        return DEFAULT_CANCEL_COMMAND_ID;
    }

    protected static final String DEFAULT_FINISH_COMMAND_ID = "okCommand";

    protected static final String DEFAULT_CANCEL_COMMAND_ID = "cancelCommand";

    private transient ActionCommand finishCommand;

    private transient ActionCommand cancelCommand;

    /**
     * setBag.
     *
     * @param bag DefaultBag
     */
    public void setBag(final DefaultBag bag) {
        this.bag = bag;
        createPanel.invalidate();
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        invalidate();
        repaint();
    }

    /**
     * BrowseFileHandler.
     */
    private class BrowseFileHandler extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            bag = bagView.getBag();
            final File selectFile = new File(File.separator + ".");
            final JFrame frame = new JFrame();
            final JFileChooser fs = new JFileChooser(selectFile);
            fs.setDialogType(JFileChooser.OPEN_DIALOG);
            fs.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fs.setDialogTitle("Existing Data Location");
            if (bagView.getBagRootPath() != null) {
                fs.setCurrentDirectory(bagView.getBagRootPath().getParentFile());
            }
            fs.setCurrentDirectory(bag.getRootDir());
            if (bag.getName() != null
                    && !bag.getName().equalsIgnoreCase(bagView.getPropertyMessage("bag.label" + ""
                            + ".noname"))) {
                String selectedName = bag.getName();
                if (bag.getSerialMode() == DefaultBag.ZIP_MODE) {
                    selectedName += "." + DefaultBag.ZIP_LABEL;
                }
                fs.setSelectedFile(new File(selectedName));
            }
            final int option = fs.showOpenDialog(frame);

            if (option == JFileChooser.APPROVE_OPTION) {
                bagFile = fs.getSelectedFile();
                final String bagFileName = bagFile.getAbsolutePath();
                // TODO: bag name is bag_<filename>
                // bagView.bagNameField.setText(bagFile.getName());
                bagNameField.setText(bagFileName);
                bagNameField.setCaretPosition(bagFileName.length());
                bagNameField.invalidate();
            }
        }
    }

    /**
     * OkNewBagHandler.
     *
     * <p>The actionPerformed method in this class
     * is called each time the "OK" button is clicked.
     * The Create Bag In Place is created based on the
     * ".keep Files in Empty Folder(s):" Check Box being selected
     */
    private class OkNewBagHandler extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            log.info("BagVersionFrame.OkNewBagHandler");
            setVisible(false);
            if (bagView.getBag().isAddKeepFilesToEmptyFolders()) {
                bagView.createBagInPlaceHandler
                        .createPreBagAddKeepFilesToEmptyFolders(bagFile, (String) profileList
                                .getSelectedItem());
            } else {
                bagView.createBagInPlaceHandler.createPreBag(bagFile, (String) profileList
                        .getSelectedItem());
            }
        }
    }

    private class CancelNewBagHandler extends AbstractAction {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(final ActionEvent e) {
            setVisible(false);
        }
    }

}