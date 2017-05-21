package cool.pandora.modeller.ui.handlers.iiif;

import cool.pandora.modeller.ui.jpanel.base.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

/**
 * Upload Bag Executor
 *
 * @author Christopher Johnson
 */
public class UploadBagExecutor extends AbstractActionCommandExecutor {
    BagView bagView;

    /**
     * @param bagView BagView
     */
    public UploadBagExecutor(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        bagView.uploadBagHandler.openUploadBagFrame();
    }

}