package cool.pandora.modeller.ui.handlers.base;

import cool.pandora.modeller.ui.jpanel.base.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

/**
 * Add Data Executor
 *
 * @author gov.loc
 */
public class AddDataExecutor extends AbstractActionCommandExecutor {
    BagView bagView;

    /**
     * @param bagView BagView
     */
    public AddDataExecutor(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    /**
     *
     */
    @Override
    public void execute() {
        bagView.addDataHandler.addData();
    }

}
