package org.blume.modeller.ui.handlers.iiif;

import org.blume.modeller.ui.jpanel.base.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

public class CreateXmlFilesExecutor extends AbstractActionCommandExecutor {
    private BagView bagView;

    public CreateXmlFilesExecutor(BagView bagView) {
        super();
        setEnabled(true);
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        bagView.createXmlFilesHandler.openCreateXmlFilesFrame();
    }

}
