package cool.pandora.modeller.ui.handlers.iiif;

import cool.pandora.modeller.ui.jpanel.base.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

/**
 * Patch Canvas Executor
 *
 * @author Christopher Johnson
 */
public class PatchCanvasExecutor extends AbstractActionCommandExecutor {
    BagView bagView;

    /**
     * @param bagView BagView
     */
    public PatchCanvasExecutor(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        bagView.patchCanvasHandler.openPatchCanvasFrame();
    }

}
