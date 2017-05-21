package cool.pandora.modeller.ui.handlers.iiif;

import cool.pandora.modeller.ui.jpanel.base.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

/**
 * Create Lists Executor
 *
 * @author Christopher Johnson
 */
public class CreateListsExecutor extends AbstractActionCommandExecutor {
    private final BagView bagView;

    /**
     * @param bagView BagView
     */
    public CreateListsExecutor(final BagView bagView) {
        super();
        setEnabled(true);
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        bagView.createListsHandler.openCreateListsFrame();
    }

}
