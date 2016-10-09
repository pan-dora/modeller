package org.blume.modeller.ui.handlers.iiif;

import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.swing.AbstractAction;

import org.blume.modeller.ModellerClientFailedException;
import org.blume.modeller.ProfileOptions;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.ui.handlers.base.SaveBagHandler;
import org.blume.modeller.ui.util.URIResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.ui.jpanel.base.BagView;
import org.blume.modeller.ui.Progress;
import org.blume.modeller.ui.util.ApplicationContextUtil;
import org.blume.modeller.ui.jpanel.iiif.CreateDefaultContainersFrame;
import org.blume.modeller.ModellerClient;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

public class CreateDefaultContainersHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(SaveBagHandler.class);
    private static final long serialVersionUID = 1L;
    private BagView bagView;

    public CreateDefaultContainersHandler(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void execute() {
        String message = ApplicationContextUtil.getMessage("bag.message.containercreated");
        DefaultBag bag = bagView.getBag();
        Map<String, BagInfoField> map = bag.getInfo().getFieldMap();

        ModellerClient client = new ModellerClient();
        URI collectionIDURI = getCollectionIDURI(map);
        URI objektURI = getObjektURI(map);

        try {
        client.doPut(collectionIDURI);
        } catch (ModellerClientFailedException e) {
            ApplicationContextUtil.addConsoleMessage(getMessage(e));
        }
        try {
        client.doPut(objektURI);
        } catch (ModellerClientFailedException e) {
            ApplicationContextUtil.addConsoleMessage(getMessage(e));
        }

        String[] IIIFContainers = new String[]{ProfileOptions.RESOURCE_CONTAINER_KEY,
                ProfileOptions.MANIFEST_RESOURCE_LABEL, ProfileOptions.SEQUENCE_CONTAINER_KEY,
                ProfileOptions.RANGE_CONTAINER_KEY, ProfileOptions.CANVAS_CONTAINER_KEY,
                ProfileOptions.LIST_CONTAINER_KEY, ProfileOptions.LAYER_CONTAINER_KEY,
                ProfileOptions.TEXT_PAGE_CONTAINER_KEY,
                ProfileOptions.TEXT_AREA_CONTAINER_KEY, ProfileOptions.TEXT_LINE_CONTAINER_KEY,
                ProfileOptions.TEXT_WORD_CONTAINER_KEY};
        for (String containerKey: IIIFContainers) {
            URI containerURI = buildContainerURI(map, containerKey);
            try {
                client.doPut(containerURI);
                ApplicationContextUtil.addConsoleMessage(message + " " + containerURI);
            } catch (ModellerClientFailedException e) {
                ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
        bagView.getControl().invalidate();
    }

    void openCreateDefaultContainersFrame() {
        DefaultBag bag = bagView.getBag();
        CreateDefaultContainersFrame createDefaultContainersFrame =
                new CreateDefaultContainersFrame(bagView, bagView.getPropertyMessage("bag.frame.put"));
        createDefaultContainersFrame.setBag(bag);
        createDefaultContainersFrame.setVisible(true);
    }

    public URI getObjektURI(Map<String, BagInfoField> map) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .pathType(3)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    private URI getCollectionIDURI(Map<String, BagInfoField> map) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .pathType(2)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    private URI buildContainerURI(Map<String, BagInfoField> map, String containerKey) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(containerKey)
                    .pathType(4)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }
}