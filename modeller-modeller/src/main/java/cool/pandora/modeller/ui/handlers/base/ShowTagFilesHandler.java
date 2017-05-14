package cool.pandora.modeller.ui.handlers.base;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.TagFilesFrame;

/**
 * Show Tag Files Handler
 *
 * @author gov.loc
 */
public class ShowTagFilesHandler extends AbstractAction {
    private static final long serialVersionUID = 1L;
    BagView bagView;
    DefaultBag bag;

    /**
     * @param bagView BagView
     */
    public ShowTagFilesHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        showTagFiles();
    }

    private void showTagFiles() {
        bag = bagView.getBag();
        bagView.tagManifestPane.updateCompositePaneTabs(bag);
        final TagFilesFrame tagFilesFrame = new TagFilesFrame(bagView.getPropertyMessage("bagView.tagFrame.title"));
        tagFilesFrame.addComponents(bagView.tagManifestPane);
        tagFilesFrame.addComponents(bagView.tagManifestPane);
        tagFilesFrame.setVisible(true);
        tagFilesFrame.setAlwaysOnTop(true);
    }
}
