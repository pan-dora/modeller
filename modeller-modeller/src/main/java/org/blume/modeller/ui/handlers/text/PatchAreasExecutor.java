package org.blume.modeller.ui.handlers.text;

import org.blume.modeller.ui.jpanel.base.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

public class PatchAreasExecutor extends AbstractActionCommandExecutor {
    private BagView bagView;

    public PatchAreasExecutor(BagView bagView) {
        super();
        setEnabled(true);
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        bagView.patchAreasHandler.openPatchAreasFrame();
    }

}