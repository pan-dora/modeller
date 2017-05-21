package cool.pandora.modeller.ui.handlers.iiif;

import cool.pandora.modeller.ui.jpanel.base.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

/**
 * Patch List Executor
 *
 * @author Christopher Johnson
 */
public class PatchListExecutor extends AbstractActionCommandExecutor {
    BagView bagView;

    /**
     * @param bagView BagView
     */
    public PatchListExecutor(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        bagView.patchListHandler.openPatchListFrame();
    }

}
