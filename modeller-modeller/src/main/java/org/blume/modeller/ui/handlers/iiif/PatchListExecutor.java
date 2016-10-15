package org.blume.modeller.ui.handlers.iiif;

import org.blume.modeller.ui.jpanel.base.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

public class PatchListExecutor extends AbstractActionCommandExecutor {
    BagView bagView;

    public PatchListExecutor(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        bagView.patchListHandler.openPatchListFrame();
    }

}
