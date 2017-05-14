package cool.pandora.modeller.ui.handlers.base;

import cool.pandora.modeller.ui.jpanel.base.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

/**
 * Clear Bag Executor
 *
 * @author gov.loc
 */
public class ClearBagExecutor extends AbstractActionCommandExecutor {
    BagView bagView;

    /**
     * @param bagView BagView
     */
    public ClearBagExecutor(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    /**
     *
     */
    @Override
    public void execute() {
        bagView.clearBagHandler.closeExistingBag();
    }

}
