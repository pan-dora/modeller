package org.blume.modeller.ui.handlers.iiif;

import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

import org.blume.modeller.ui.jpanel.base.BagView;

public class PatchCanvasExecutor extends AbstractActionCommandExecutor {
    BagView bagView;

    public  PatchCanvasExecutor(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        bagView.patchCanvasHandler.openPatchCanvasFrame();
    }

}
