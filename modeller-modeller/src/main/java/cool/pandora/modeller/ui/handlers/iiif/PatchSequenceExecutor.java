package cool.pandora.modeller.ui.handlers.iiif;

import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

import cool.pandora.modeller.ui.jpanel.base.BagView;

/**
 * Patch Sequence Executor
 *
 * @author Christopher Johnson
 */
public class PatchSequenceExecutor extends AbstractActionCommandExecutor {
    BagView bagView;

    /**
     * @param bagView BagView
     */
    public PatchSequenceExecutor(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        bagView.patchSequenceHandler.openPatchSequenceFrame();
    }

}
