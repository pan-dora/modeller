package cool.pandora.modeller.ui.handlers.text;

import cool.pandora.modeller.ui.jpanel.base.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

/**
 * Patch Lines Executor
 *
 * @author Christopher Johnson
 */
public class PatchLinesExecutor extends AbstractActionCommandExecutor {
    private final BagView bagView;

    /**
     * @param bagView BagView
     */
    public PatchLinesExecutor(final BagView bagView) {
        super();
        setEnabled(true);
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        bagView.patchLinesHandler.openPatchLinesFrame();
    }

}