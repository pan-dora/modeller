package org.blume.modeller.ui.handlers.iiif;

import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

import org.blume.modeller.ui.jpanel.base.BagView;

public class CreateDefaultContainersExecutor extends AbstractActionCommandExecutor {
    BagView bagView;

    public CreateDefaultContainersExecutor(BagView bagView) {
        super();
        setEnabled(true);
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        bagView.createDefaultContainersHandler.openCreateDefaultContainersFrame();
    }

}