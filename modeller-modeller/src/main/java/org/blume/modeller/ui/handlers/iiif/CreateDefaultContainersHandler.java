package org.blume.modeller.ui.handlers.iiif;

import java.awt.event.ActionEvent;
import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

import javax.swing.AbstractAction;

import org.blume.modeller.ModellerClientFailedException;
import org.blume.modeller.ProfileOptions;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.ui.handlers.base.SaveBagHandler;
import org.blume.modeller.ui.handlers.common.IIIFObjectURI;
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
        URI collectionIDURI = IIIFObjectURI.getCollectionIdURI(map);
        URI objektURI = IIIFObjectURI.getObjektURI(map);

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

        String[] Containers;
        String[] IIIFContainers = new String[]{ProfileOptions.RESOURCE_CONTAINER_KEY,
                ProfileOptions.MANIFEST_RESOURCE_LABEL, ProfileOptions.SEQUENCE_CONTAINER_KEY,
                ProfileOptions.RANGE_CONTAINER_KEY, ProfileOptions.CANVAS_CONTAINER_KEY,
                ProfileOptions.LIST_CONTAINER_KEY, ProfileOptions.LAYER_CONTAINER_KEY};

        String[] TextContainers = new String[]{ProfileOptions.TEXT_PAGE_CONTAINER_KEY,
                ProfileOptions.TEXT_AREA_CONTAINER_KEY, ProfileOptions.TEXT_LINE_CONTAINER_KEY,
                ProfileOptions.TEXT_WORD_CONTAINER_KEY};

        if (bag.hasText()) {
            Containers = Stream.concat(Arrays.stream(IIIFContainers), Arrays.stream(TextContainers))
                    .toArray(String[]::new);
        } else {
            Containers = IIIFContainers;
        }

        for (String containerKey: Containers) {
            URI containerURI = IIIFObjectURI.buildContainerURI(map, containerKey);
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
}