package cool.pandora.modeller.ui.handlers.text;

import cool.pandora.modeller.ui.jpanel.base.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

/**
 * Create Words Executor
 *
 * @author Christopher Johnson
 */
public class CreateWordsExecutor extends AbstractActionCommandExecutor {
    private final BagView bagView;

    /**
     * @param bagView BagView
     */
    public CreateWordsExecutor(final BagView bagView) {
        super();
        setEnabled(true);
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        bagView.createWordsHandler.openCreateWordsFrame();
    }

}