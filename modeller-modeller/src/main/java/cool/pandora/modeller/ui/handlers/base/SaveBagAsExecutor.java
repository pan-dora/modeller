package cool.pandora.modeller.ui.handlers.base;

import cool.pandora.modeller.ui.jpanel.base.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

/**
 * Save Bag As Executor
 *
 * @author gov.loc
 */
public class SaveBagAsExecutor extends AbstractActionCommandExecutor {
    BagView bagView;

    /**
     * @param bagView BagView
     */
    public SaveBagAsExecutor(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        bagView.saveBagAsHandler.openSaveBagAsFrame();
    }

}
