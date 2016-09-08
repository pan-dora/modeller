package org.blume.modeller.ui.handlers.iiif;

import org.blume.modeller.ModellerClient;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.ui.Progress;
import org.blume.modeller.ui.handlers.base.SaveBagHandler;
import org.blume.modeller.ui.jpanel.BagView;
import org.blume.modeller.ui.jpanel.CreateCanvasesFrame;
import org.blume.modeller.ui.util.ApplicationContextUtil;
import org.blume.modeller.ui.util.ContainerIRIResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.*;
import static org.blume.modeller.common.uri.FedoraResources.CANVASPREFIX;

public class CreateCanvasesHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(SaveBagHandler.class);
    private static final long serialVersionUID = 1L;
    private BagView bagView;

    public CreateCanvasesHandler(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(ActionEvent e) { openCreateCanvasesFrame(); }

    @Override
    public void execute() {
        String message = ApplicationContextUtil.getMessage("bag.message.canvascreated");
        DefaultBag bag = bagView.getBag();
        Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        ResourceIdentifierList idList = new ResourceIdentifierList(bagView);
        ArrayList<String> resourceIDList = idList.getResourceIdentifierList();
        ModellerClient client = new ModellerClient();
        String canvasContainerURI = getCanvasContainerURI(map);
        for (String resourceID : resourceIDList) {
            String canvasObjectURI = getCanvasObjectURI(canvasContainerURI, resourceID);
            try {
                client.doPut(canvasObjectURI);
            } finally {
                ApplicationContextUtil.addConsoleMessage(message + " " + canvasObjectURI);
            }
        }
        bagView.getControl().invalidate();
    }

    void openCreateCanvasesFrame() {
        DefaultBag bag = bagView.getBag();
        CreateCanvasesFrame createCanvasesFrame = new CreateCanvasesFrame(bagView, bagView.getPropertyMessage("bag.frame.list"));
        createCanvasesFrame.setBag(bag);
        createCanvasesFrame.setVisible(true);
    }

    private String getCanvasObjectURI(String canvasContainerURI, String resourceID) {
        return canvasContainerURI + CANVASPREFIX +
                resourceID;
    }

    public String getCanvasContainerURI(Map<String, BagInfoField> map) {
        ContainerIRIResolver containerIRIResolver;
        containerIRIResolver = ContainerIRIResolver.resolve()
                .map(map)
                .baseURIKey("FedoraBaseURI")
                .collectionRootKey("CollectionRoot")
                .collectionKey("CollectionID")
                .objektIDKey("ObjektID")
                .containerKey("IIIFCanvasContainer")
                .build();
        return containerIRIResolver.render();
    }
}