package org.blume.modeller.ui.handlers;

import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

import org.blume.modeller.ui.BagView;

public class UploadBagExecutor extends AbstractActionCommandExecutor {
    BagView bagView;

    public UploadBagExecutor(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        bagView.uploadBagHandler.openUploadBagFrame();
    }

}