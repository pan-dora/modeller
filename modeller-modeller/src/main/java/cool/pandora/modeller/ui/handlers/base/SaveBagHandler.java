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

import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.Progress;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;

import gov.loc.repository.bagit.BagFactory;
import gov.loc.repository.bagit.writer.Writer;
import gov.loc.repository.bagit.writer.impl.FileSystemWriter;
//import gov.loc.repository.bagit.writer.impl.TarBz2Writer;
//import gov.loc.repository.bagit.writer.impl.TarGzWriter;
//import gov.loc.repository.bagit.writer.impl.TarWriter;
import gov.loc.repository.bagit.writer.impl.ZipWriter;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.dialog.ConfirmationDialog;


/**
 * Save Bag Handler.
 *
 * @author gov.loc
 */
public class SaveBagHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(SaveBagHandler.class);
    private static final long serialVersionUID = 1L;
    private final BagView bagView;
    private File tmpRootPath;
    private boolean clearAfterSaving = false;
    private String messages;

    /**
     * SaveBagHandler.
     *
     * @param bagView BagView
     */
    public SaveBagHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final DefaultBag bag = bagView.getBag();
        bagView.infoInputPane.updateBagHandler.updateBag(bag);
        if (bagView.getBagRootPath().exists()) {
            tmpRootPath = bagView.getBagRootPath();
            confirmWriteBag();
        } else {
            saveBag(bagView.getBagRootPath());
        }
    }

    @Override
    public void execute() {
        final DefaultBag bag = bagView.getBag();

        try {
            final BagFactory bagFactory = new BagFactory();
            final Writer bagWriter = getWriter(bagFactory, bag);
      /*
         * else if (mode == DefaultBag.TAR_MODE) {
         * bagWriter = new TarWriter(bagFactory);
         * } else if (mode == DefaultBag.TAR_GZ_MODE) {
         * bagWriter = new TarGzWriter(bagFactory);
         * } else if (mode == DefaultBag.TAR_BZ2_MODE) {
         * bagWriter = new TarBz2Writer(bagFactory);
         * }
         */
            if (bagWriter != null) {
                bagWriter.addProgressListener(bagView.task);
                bagView.longRunningProcess = bagWriter;
                messages = bag.write(bagWriter);

                if (messages != null && !messages.trim().isEmpty()) {
                    BagView.showWarningErrorDialog("Warning - bag not saved", "Problem saving "
                            + "bag:\n" + messages);
                } else {
                    BagView.showWarningErrorDialog("Bag saved", "Bag saved successfully.\n");
                }
            } else {
                BagView.showWarningErrorDialog("Warning - bag not saved", "Could not get writer "
                        + "for bag");
            }

            SwingUtilities.invokeLater(() -> {
                final DefaultBag bag1 = bagView.getBag();
                if (bag1.isSerialized()) {
                    if (clearAfterSaving) {
                        BagView.statusBarEnd();
                        bagView.clearBagHandler.clearExistingBag();
                        setClearAfterSaving(false);
                    } else {
                        if (bag1.isValidateOnSave()) {
                            bagView.validateBagHandler.validateBag();
                        }
                        BagView.statusBarEnd();
                        final File bagFile = bag1.getBagFile();
                        log.info("BagView.openExistingBag: {}", bagFile);
                        bagView.openBagHandler.openExistingBag(bagFile);
                        bagView.updateSaveBag();
                    }
                } else {
                    ApplicationContextUtil.addConsoleMessage(messages);
                    bagView.updateManifestPane();
                }
            });
        } finally {
            bagView.task.done();
            BagView.statusBarEnd();
        }
    }

    private static Writer getWriter(final BagFactory bagFactory, final DefaultBag bag) {
        if (bag.getSerialMode() == DefaultBag.NO_MODE) {
            return new FileSystemWriter(bagFactory);
        } else if (bag.getSerialMode() == DefaultBag.ZIP_MODE) {
            return new ZipWriter(bagFactory);
        }
        return null;
    }

    void setTmpRootPath(final File f) {
        this.tmpRootPath = f;
    }

    void setClearAfterSaving(final boolean b) {
        this.clearAfterSaving = b;
    }

    void saveBag(final File file) {
        final DefaultBag bag = bagView.getBag();
        bag.setRootDir(file);
        bagView.statusBarBegin(this, "Writing bag...", null);
    }

    void confirmWriteBag() {
        final ConfirmationDialog dialog = new ConfirmationDialog() {
            boolean isCancel = true;

            @Override
            protected void onConfirm() {
                final DefaultBag bag = bagView.getBag();
                if (bag.getSize() > DefaultBag.MAX_SIZE) {
                    confirmAcceptBagSize();
                } else {
                    bagView.setBagRootPath(tmpRootPath);
                    saveBag(bagView.getBagRootPath());
                }
            }

            @Override
            protected void onCancel() {
                super.onCancel();
                if (isCancel) {
                    cancelWriteBag();
                    isCancel = false;
                }
            }
        };

        dialog.setCloseAction(CloseAction.DISPOSE);
        dialog.setTitle(bagView.getPropertyMessage("bag.dialog.title.create"));
        dialog.setConfirmationMessage(bagView.getPropertyMessage("bag.dialog.message.create"));
        dialog.showDialog();
    }

    private void cancelWriteBag() {
        clearAfterSaving = false;
    }

    private void confirmAcceptBagSize() {
        final ConfirmationDialog dialog = new ConfirmationDialog() {
            @Override
            protected void onConfirm() {
                bagView.setBagRootPath(tmpRootPath);
                saveBag(bagView.getBagRootPath());
            }
        };

        dialog.setCloseAction(CloseAction.DISPOSE);
        dialog.setTitle(bagView.getPropertyMessage("bag.dialog.title.create"));
        dialog.setConfirmationMessage(bagView.getPropertyMessage("bag.dialog.message.accept"));
        dialog.showDialog();
    }

    /**
     * save.
     *
     * @param file File
     */
    public void save(final File file) {
        final DefaultBag bag = bagView.getBag();
        final File newfile;
        if (file != null) {
            newfile = file;
        } else {
            newfile = bagView.getBagRootPath();
        }
        bag.setName(newfile.getName());
        final File bagFile = new File(newfile, bag.getName());
        if (bagFile.exists()) {
            tmpRootPath = newfile;
            confirmWriteBag();
        } else {
            if (bag.getSize() > DefaultBag.MAX_SIZE) {
                tmpRootPath = newfile;
                confirmAcceptBagSize();
            } else {
                bagView.setBagRootPath(newfile);
                saveBag(bagView.getBagRootPath());
            }
        }
        final String fileName = bagFile.getAbsolutePath();
        bagView.infoInputPane.setBagName(fileName);
        bagView.getControl().invalidate();
    }
}
