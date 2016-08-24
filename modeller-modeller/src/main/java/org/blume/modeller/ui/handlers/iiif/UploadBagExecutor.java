package org.blume.modeller.ui.handlers.iiif;

import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

import org.blume.modeller.ui.jpanel.BagView;

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