package cool.pandora.modeller.ui.handlers.text;

import cool.pandora.modeller.ui.jpanel.base.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

/**
 * Create Pages Executor
 *
 * @author Christopher Johnson
 */
public class CreatePagesExecutor extends AbstractActionCommandExecutor {
    private final BagView bagView;

    /**
     * @param bagView BagView
     */
    public CreatePagesExecutor(final BagView bagView) {
        super();
        setEnabled(true);
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        bagView.createPagesHandler.openCreatePagesFrame();
    }

}