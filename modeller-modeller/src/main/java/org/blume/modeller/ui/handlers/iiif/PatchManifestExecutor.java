package org.blume.modeller.ui.handlers.iiif;

import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

import org.blume.modeller.ui.jpanel.BagView;

public class PatchManifestExecutor extends AbstractActionCommandExecutor {
    BagView bagView;

    public  PatchManifestExecutor(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        bagView.patchManifestHandler.openPatchManifestFrame();
    }

}
