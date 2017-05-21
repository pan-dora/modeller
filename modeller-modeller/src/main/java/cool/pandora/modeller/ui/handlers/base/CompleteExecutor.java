package cool.pandora.modeller.ui.handlers.base;

import cool.pandora.modeller.ui.jpanel.base.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

/**
 * Complete Executor
 *
 * @author gov.loc
 */
public class CompleteExecutor extends AbstractActionCommandExecutor {
    BagView bagView;

    /**
     * @param bagView BagView
     */
    public CompleteExecutor(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        bagView.completeBagHandler.completeBag();
    }

}
