package cool.pandora.modeller.ui.handlers.base;

import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.BagTree;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagit.BagFile;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Collection;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 * Add Tag File Handler
 *
 * @author gov.loc
 */
public class AddTagFileHandler extends AbstractAction {
    private static final long serialVersionUID = 1L;
    BagView bagView;

    /**
     * @param bagView BagView
     */
    public AddTagFileHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    /**
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        addTagFile();
    }

    /**
     *
     */
    private void addTagFile() {
        final File selectFile = new File(File.separator + ".");
        final JFrame frame = new JFrame();
        final JFileChooser fo = new JFileChooser(selectFile);
        fo.setDialogType(JFileChooser.OPEN_DIALOG);
        fo.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (bagView.getBagRootPath() != null) {
            fo.setCurrentDirectory(bagView.getBagRootPath().getParentFile());
        }
        fo.setDialogTitle("Tag File Chooser");
        final int option = fo.showOpenDialog(frame);

        if (option == JFileChooser.APPROVE_OPTION) {
            final DefaultBag bag = bagView.getBag();
            final File file = fo.getSelectedFile();
            bag.addTagFile(file);
            bagView.bagTagFileTree = new BagTree(bagView, bag.getName());
            final Collection<BagFile> tags = bag.getTags();
            for (final BagFile bf : tags) {
                bagView.bagTagFileTree.addNode(bf.getFilepath());
            }
            bagView.bagTagFileTreePanel.refresh(bagView.bagTagFileTree);
            ApplicationContextUtil.addConsoleMessage("Tag file added: " + file.getAbsolutePath());
        }
    }

}
