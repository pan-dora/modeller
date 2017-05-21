package cool.pandora.modeller.ui.handlers.iiif;

import java.awt.event.ActionEvent;
import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

import javax.swing.AbstractAction;

import cool.pandora.modeller.ModellerClientFailedException;
import cool.pandora.modeller.ProfileOptions;
import cool.pandora.modeller.bag.BagInfoField;
import cool.pandora.modeller.ui.handlers.base.SaveBagHandler;
import cool.pandora.modeller.ui.handlers.common.IIIFObjectURI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.Progress;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import cool.pandora.modeller.ui.jpanel.iiif.CreateDefaultContainersFrame;
import cool.pandora.modeller.ModellerClient;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

/**
 * Create Default Containers Handler
 *
 * @author Christopher Johnson
 */
public class CreateDefaultContainersHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(SaveBagHandler.class);
    private static final long serialVersionUID = 1L;
    private final BagView bagView;

    /**
     * @param bagView BagView
     */
    public CreateDefaultContainersHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
    }

    @Override
    public void execute() {
        final String message = ApplicationContextUtil.getMessage("bag.message.containercreated");
        final DefaultBag bag = bagView.getBag();
        final Map<String, BagInfoField> map = bag.getInfo().getFieldMap();

        final URI collectionIDURI = IIIFObjectURI.getCollectionIdURI(map);
        final URI objektURI = IIIFObjectURI.getObjektURI(map);

        try {
            ModellerClient.doPut(collectionIDURI);
        } catch (final ModellerClientFailedException e) {
            ApplicationContextUtil.addConsoleMessage(getMessage(e));
        }
        try {
            ModellerClient.doPut(objektURI);
        } catch (final ModellerClientFailedException e) {
            ApplicationContextUtil.addConsoleMessage(getMessage(e));
        }

        final String[] Containers;
        final String[] IIIFContainers =
                new String[]{ProfileOptions.RESOURCE_CONTAINER_KEY, ProfileOptions.MANIFEST_RESOURCE_LABEL,
                        ProfileOptions.SEQUENCE_CONTAINER_KEY, ProfileOptions.RANGE_CONTAINER_KEY,
                        ProfileOptions.CANVAS_CONTAINER_KEY, ProfileOptions.LIST_CONTAINER_KEY,
                        ProfileOptions.LAYER_CONTAINER_KEY};

        final String[] TextContainers =
                new String[]{ProfileOptions.TEXT_PAGE_CONTAINER_KEY, ProfileOptions.TEXT_AREA_CONTAINER_KEY,
                        ProfileOptions.TEXT_LINE_CONTAINER_KEY, ProfileOptions.TEXT_WORD_CONTAINER_KEY};

        if (bag.hasText()) {
            Containers =
                    Stream.concat(Arrays.stream(IIIFContainers), Arrays.stream(TextContainers)).toArray(String[]::new);
        } else {
            Containers = IIIFContainers;
        }

        for (final String containerKey : Containers) {
            final URI containerURI = IIIFObjectURI.buildContainerURI(map, containerKey);
            try {
                ModellerClient.doPut(containerURI);
                ApplicationContextUtil.addConsoleMessage(message + " " + containerURI);
            } catch (final ModellerClientFailedException e) {
                ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
        bagView.getControl().invalidate();
    }

    void openCreateDefaultContainersFrame() {
        final DefaultBag bag = bagView.getBag();
        final CreateDefaultContainersFrame createDefaultContainersFrame =
                new CreateDefaultContainersFrame(bagView, bagView.getPropertyMessage("bag.frame.put"));
        createDefaultContainersFrame.setBag(bag);
        createDefaultContainersFrame.setVisible(true);
    }
}