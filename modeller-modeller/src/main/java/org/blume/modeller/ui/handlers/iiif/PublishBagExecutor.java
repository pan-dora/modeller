package org.blume.modeller.ui.handlers.iiif;

import org.blume.modeller.ModellerClientFailedException;
import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.ui.jpanel.base.BagView;
import org.blume.modeller.ui.util.ApplicationContextUtil;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

public class PublishBagExecutor extends AbstractActionCommandExecutor {
    private BagView bagView;

    public PublishBagExecutor(BagView bagView) {
        super();
        setEnabled(true);
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        DefaultBag bag = bagView.getBag();
        String message = ApplicationContextUtil.getMessage("bag.message.bagpublished");
        String baglabel = bag.getName();
        try {
            bagView.createDefaultContainersHandler.execute();
            bagView.uploadBagHandler.execute();
            bagView.patchResourceHandler.execute();
            bagView.createListsHandler.execute();
            bagView.createCanvasesHandler.execute();
            bagView.patchCanvasHandler.execute();
            bagView.createSequencesHandler.execute();
            bagView.patchSequenceHandler.execute();
            bagView.patchManifestHandler.execute();
            ApplicationContextUtil.addConsoleMessage(message + " " + baglabel);
        } catch (Exception e) {
            ApplicationContextUtil.addConsoleMessage(getMessage(e));
        }
    }
}
