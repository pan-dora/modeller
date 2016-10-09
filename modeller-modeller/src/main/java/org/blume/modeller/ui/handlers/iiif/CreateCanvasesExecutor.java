package org.blume.modeller.ui.handlers.iiif;

import org.blume.modeller.ui.jpanel.base.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

public class CreateCanvasesExecutor extends AbstractActionCommandExecutor {
    private BagView bagView;

    public CreateCanvasesExecutor(BagView bagView) {
        super();
        setEnabled(true);
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        bagView.createCanvasesHandler.openCreateCanvasesFrame();
    }

}