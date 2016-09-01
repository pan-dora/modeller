package org.blume.modeller.ui.handlers.iiif;

import org.blume.modeller.ui.jpanel.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

public class CreateListsExecutor extends AbstractActionCommandExecutor {
    private BagView bagView;

    public CreateListsExecutor(BagView bagView) {
        super();
        setEnabled(true);
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        bagView.createListsHandler.openCreateListsFrame();
    }

}
