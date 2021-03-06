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

package cool.pandora.modeller.ui.handlers.base;

import cool.pandora.modeller.ui.Progress;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.progress.BusyIndicator;


/**
 * Add Data Handler.
 *
 * @author gov.loc
 */
public class AddDataHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(AddDataHandler.class);
    private static final long serialVersionUID = 1L;
    BagView bagView;

    /**
     * AddDataHandler.
     *
     * @param bagView BagView
     */
    public AddDataHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    /**
     * getFileNames.
     *
     * @param files File[]
     * @return FileNames
     */
    private static String getFileNames(final File[] files) {
        final StringBuilder stringBuff = new StringBuilder();
        final int totalFileCount = files.length;
        int displayCount = 20;
        if (totalFileCount < 20) {
            displayCount = totalFileCount;
        }
        for (int i = 0; i < displayCount; i++) {
            if (i != 0) {
                stringBuff.append("\n");
            }
            stringBuff.append(files[i].getAbsolutePath());
        }
        if (totalFileCount > displayCount) {
            stringBuff.append("\n").append(totalFileCount - displayCount).append(" more...");
        }
        return stringBuff.toString();
    }

    /**
     * execute.
     */
    @Override
    public void execute() {
        BagView.statusBarEnd();
    }

    /**
     * actionPerformed.
     *
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        BusyIndicator.showAt(Application.instance().getActiveWindow().getControl());
        addData();
        BusyIndicator.clearAt(Application.instance().getActiveWindow().getControl());
    }

    /**
     * addData.
     */
    void addData() {
        final File selectFile = new File(File.separator + ".");
        final JFrame frame = new JFrame();
        final JFileChooser fc = new JFileChooser(selectFile);
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        fc.setMultiSelectionEnabled(true);
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fc.setDialogTitle("Add File or Directory");
        final int option = fc.showOpenDialog(frame);

        if (option == JFileChooser.APPROVE_OPTION) {
            final File[] files = fc.getSelectedFiles();
            final String message = ApplicationContextUtil.getMessage("bag.message.filesadded");
            if (files != null && files.length > 0) {
                addBagData(files);
                ApplicationContextUtil.addConsoleMessage(message + " " + getFileNames(files));
            } else {
                final File file = fc.getSelectedFile();
                addBagData(file);
                ApplicationContextUtil.addConsoleMessage(message + " " + file.getAbsolutePath());
            }
            bagView.bagPayloadTreePanel.refresh(bagView.bagPayloadTree);
            bagView.updateAddData();
        }
    }

    /**
     * addBagData.
     *
     * @param files File[]
     */
    private void addBagData(final File[] files) {
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                log.info("addBagData[{}] {}", i, files[i].getName());
                addBagData(files[i]);
            }
        }
    }

    /**
     * addBagData.
     *
     * @param file File
     */
    private void addBagData(final File file) {
        BusyIndicator.showAt(Application.instance().getActiveWindow().getControl());
        try {
            bagView.getBag().addFileToPayload(file);
            final boolean alreadyExists = bagView.bagPayloadTree.addNodes(file, false);
            if (alreadyExists) {
                BagView.showWarningErrorDialog("Warning - file already exists",
                        "File: " + file.getName() + "\n" + "already exists in bag.");
            }
        } catch (final Exception e) {
            log.error("Failed to add bag file", e);
            BagView.showWarningErrorDialog("Error - file not added",
                    "Error adding bag file: " + file + "\ndue to:\n" + e.getMessage());
        }
        BusyIndicator.clearAt(Application.instance().getActiveWindow().getControl());
    }

}
