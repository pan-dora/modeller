package cool.pandora.modeller.ui.handlers.base;

import cool.pandora.modeller.ui.jpanel.base.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

/**
 * Create Bag in Place Executor
 *
 * @author gov.loc
 */
public class CreateBagInPlaceExecutor extends AbstractActionCommandExecutor {
    BagView bagView;

    /**
     * @param bagView BagView
     */
    public CreateBagInPlaceExecutor(final BagView bagView) {
        super();
        setEnabled(true);
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        bagView.createBagInPlaceHandler.createBagInPlace();
    }

}
