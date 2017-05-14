package cool.pandora.modeller.ui.handlers.iiif;

import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

/**
 * Publish Bag Executor
 *
 * @author Christopher Johnson
 */
public class PublishBagExecutor extends AbstractActionCommandExecutor {
    private final BagView bagView;

    /**
     * @param bagView BagView
     */
    public PublishBagExecutor(final BagView bagView) {
        super();
        setEnabled(true);
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        final DefaultBag bag = bagView.getBag();
        final String message = ApplicationContextUtil.getMessage("bag.message.bagpublished");
        final String baglabel = bag.getName();
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
        } catch (final Exception e) {
            ApplicationContextUtil.addConsoleMessage(getMessage(e));
        }
    }
}
