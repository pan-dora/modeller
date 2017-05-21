package cool.pandora.modeller.ui.handlers.text;

import cool.pandora.modeller.ui.jpanel.base.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

/**
 * Create Areas Executor
 *
 * @author Christopher Johnson
 */
public class CreateAreasExecutor extends AbstractActionCommandExecutor {
    private final BagView bagView;

    /**
     * @param bagView BagView
     */
    public CreateAreasExecutor(final BagView bagView) {
        super();
        setEnabled(true);
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        bagView.createAreasHandler.openCreateAreasFrame();
    }

}