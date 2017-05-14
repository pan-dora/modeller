package cool.pandora.modeller.ui.handlers.base;

import cool.pandora.modeller.ui.jpanel.base.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

/**
 * Save Bag Executor
 *
 * @author gov.loc
 */
public class SaveBagExecutor extends AbstractActionCommandExecutor {
    BagView bagView;

    /**
     * @param bagView BagView
     */
    public SaveBagExecutor(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        if (bagView.getBagRootPath().exists()) {
            bagView.saveBagHandler.setTmpRootPath(bagView.getBagRootPath());
            bagView.saveBagHandler.confirmWriteBag();
        } else {
            bagView.saveBagHandler.saveBag(bagView.getBagRootPath());
        }
    }

}
