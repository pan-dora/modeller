package org.blume.modeller.ui.handlers.text;

import org.blume.modeller.ui.jpanel.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

public class CreateLinesExecutor extends AbstractActionCommandExecutor {
    private BagView bagView;

    public CreateLinesExecutor(BagView bagView) {
        super();
        setEnabled(true);
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        bagView.createLinesHandler.openCreateLinesFrame();
    }

}