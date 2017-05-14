package cool.pandora.modeller.ui.handlers.iiif;

import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

import cool.pandora.modeller.ui.jpanel.base.BagView;

/**
 * Create Default Containers Executor
 *
 * @author Christopher Johnson
 */
public class CreateDefaultContainersExecutor extends AbstractActionCommandExecutor {
    BagView bagView;

    /**
     * @param bagView BagView
     */
    public CreateDefaultContainersExecutor(final BagView bagView) {
        super();
        setEnabled(true);
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        bagView.createDefaultContainersHandler.openCreateDefaultContainersFrame();
    }

}