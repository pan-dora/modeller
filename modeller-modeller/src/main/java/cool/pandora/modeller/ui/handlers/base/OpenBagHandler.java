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

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagit.impl.AbstractBagConstants;

/**
 * Open Bag Handler
 *
 * @author gov.loc
 */
public class OpenBagHandler extends AbstractAction {
    protected static final Logger log = LoggerFactory.getLogger(OpenBagHandler.class);
    private static final long serialVersionUID = 1L;
    BagView bagView;

    /**
     * @param bagView BagView
     */
    public OpenBagHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        openBag();
    }

    void openBag() {
        final File selectFile = new File(File.separator + ".");
        final JFrame frame = new JFrame();
        final JFileChooser fo = new JFileChooser(selectFile);
        fo.setDialogType(JFileChooser.OPEN_DIALOG);
        // fo.addChoosableFileFilter(bagView.infoInputPane.tarFilter);
        fo.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if (bagView.getBagRootPath() != null) {
            fo.setCurrentDirectory(bagView.getBagRootPath().getParentFile());
        }
        fo.setDialogTitle("Existing Bag Location");
        final int option = fo.showOpenDialog(frame);

        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fo.getSelectedFile();
            if (file == null) {
                file = bagView.getBagRootPath();
            }
            openExistingBag(file);
        }
    }

    void openExistingBag(final File file) {
        bagView.infoInputPane.bagInfoInputPane.enableForms(true);
        bagView.clearBagHandler.clearExistingBag();

        try {
            bagView.clearBagHandler.newDefaultBag(file);
            ApplicationContextUtil.addConsoleMessage("Opened the bag " + file.getAbsolutePath());
        } catch (final Exception ex) {
            ApplicationContextUtil.addConsoleMessage("Failed to create bag message: " + ex.getMessage());
            ApplicationContextUtil.addConsoleMessage("Failed to create bag exception: " + ex);
            log.error("Failed to create bag", ex);
            // showWarningErrorDialog("Warning - file not opened",
            // "Error trying to open file: " + file + "\n" + ex.getMessage());
            return;
        }
        final DefaultBag bag = bagView.getBag();
        bagView.infoInputPane.setBagVersion(bag.getVersion());
        bagView.infoInputPane.setProfile(bag.getProfile().getName());
        final String fileName;
        fileName = file.getAbsolutePath();
        bagView.infoInputPane.setBagName(fileName);

        final String s = file.getName();
        final int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            final String sub = s.substring(i + 1).toLowerCase();
      /*
       * if (sub.contains("gz")) {
       * bagView.infoInputPane.serializeValue.setText(DefaultBag.TAR_GZ_LABEL);
       * //bagView.infoInputPane.tarGzButton.setSelected(true);
       * bag.setSerialMode(DefaultBag.TAR_GZ_MODE);
       * bag.isSerial(true);
       * } else if (sub.contains("bz2")) {
       * bagView.infoInputPane.serializeValue.setText(DefaultBag.TAR_BZ2_LABEL);
       * //bagView.infoInputPane.tarBz2Button.setSelected(true);
       * bag.setSerialMode(DefaultBag.TAR_BZ2_MODE);
       * bag.isSerial(true);
       * } else if (sub.contains(DefaultBag.TAR_LABEL)) {
       * bagView.infoInputPane.serializeValue.setText(DefaultBag.TAR_LABEL);
       * //bagView.infoInputPane.tarButton.setSelected(true);
       * bag.setSerialMode(DefaultBag.TAR_MODE);
       * bag.isSerial(true);
       * } else if (sub.contains(DefaultBag.ZIP_LABEL)) {
       */
            if (sub.contains(DefaultBag.ZIP_LABEL)) {
                bagView.infoInputPane.serializeValue.setText(DefaultBag.ZIP_LABEL);
                // bagView.infoInputPane.zipButton.setSelected(true);
                bag.setSerialMode(DefaultBag.ZIP_MODE);
                bag.isSerial(true);
            } else {
                bagView.infoInputPane.serializeValue.setText(DefaultBag.NO_LABEL);
                // bagView.infoInputPane.noneButton.setSelected(true);
                bag.setSerialMode(DefaultBag.NO_MODE);
                bag.isSerial(false);
            }
        } else {
            bagView.infoInputPane.serializeValue.setText(DefaultBag.NO_LABEL);
            // bagView.infoInputPane.noneButton.setSelected(true);
            bag.setSerialMode(DefaultBag.NO_MODE);
            bag.isSerial(false);
        }
        bagView.infoInputPane.serializeValue.invalidate();

        if (bag.isHoley()) {
            bagView.infoInputPane.setHoley("true");
        } else {
            bagView.infoInputPane.setHoley("false");
        }
        bagView.infoInputPane.holeyValue.invalidate();

        bagView.updateBaggerRules();
        bagView.setBagRootPath(file);
        final File rootSrc;
        final String path;
        if (bag.getFetchTxt() != null) {
            path = bag.getFetch().getBaseURL();
            rootSrc = new File(file, bag.getFetchTxt().getFilepath());
        } else {
            path = AbstractBagConstants.DATA_DIRECTORY;
            rootSrc = new File(file, bag.getDataDirectory());
        }
        bagView.bagPayloadTree.populateNodes(bag, path, rootSrc);
        bagView.bagPayloadTreePanel.refresh(bagView.bagPayloadTree);
        bagView.updateManifestPane();
        bagView.enableBagSettings(true);
        final String msgs = bag.validateMetadata();
        if (msgs != null) {
            ApplicationContextUtil.addConsoleMessage(msgs);
        }
        bagView.infoInputPane.bagInfoInputPane.populateForms(bag);
        bagView.updateOpenBag();
        BagView.statusBarEnd();
    }
}
