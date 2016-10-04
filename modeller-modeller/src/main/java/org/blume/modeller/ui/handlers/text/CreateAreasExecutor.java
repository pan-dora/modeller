package org.blume.modeller.ui.handlers.text;

import org.blume.modeller.ui.jpanel.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

public class CreateAreasExecutor extends AbstractActionCommandExecutor {
    private BagView bagView;

    public CreateAreasExecutor(BagView bagView) {
        super();
        setEnabled(true);
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        bagView.createAreasHandler.openCreateAreasFrame();
    }

}