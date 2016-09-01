package org.blume.modeller.ui.handlers.iiif;

import org.blume.modeller.ModellerClient;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.ui.Progress;
import org.blume.modeller.ui.handlers.base.SaveBagHandler;
import org.blume.modeller.ui.jpanel.BagView;
import org.blume.modeller.ui.jpanel.CreateListsFrame;
import org.blume.modeller.ui.util.ApplicationContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.swing.*;


public class CreateListsHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(SaveBagHandler.class);
    private static final long serialVersionUID = 1L;
    private BagView bagView;

    public CreateListsHandler(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(ActionEvent e) { openCreateListsFrame(); }

    @Override
    public void execute() {
        String message = ApplicationContextUtil.getMessage("bag.message.listcreated");
        DefaultBag bag = bagView.getBag();
        HashMap<String, BagInfoField> map = bag.getInfo().getFieldMap();

        ModellerClient client = new ModellerClient();
        String listContainerURI = getListContainerURI(map);
        String listID = bag.getListID();
        String listObjectURI = getListObjectURI(listContainerURI, listID );
        try {
            client.doPut(listObjectURI);
        } finally {
            ApplicationContextUtil.addConsoleMessage(message + " " + listObjectURI);
        }
        bagView.getControl().invalidate();
    }

    public void openCreateListsFrame() {
        DefaultBag bag = bagView.getBag();
        CreateListsFrame createListsFrame = new CreateListsFrame(bagView, bagView.getPropertyMessage("bag.frame.list"));
        createListsFrame.setBag(bag);
        createListsFrame.setVisible(true);
    }

    public String getListObjectURI(String ListContainerURI, String ListID) {
        return ListContainerURI +
                ListID;
    }

    public String getMapValue(HashMap<String, BagInfoField> map, String key) {
        BagInfoField IIIFResourceContainer = map.get(key);
        return IIIFResourceContainer.getValue();
    }

    public String buildContainerURI(String objektURI, String container) {
        return objektURI +
                container;
    }

    public String getListContainerURI(HashMap<String, BagInfoField> map) {
        BagInfoField baseURI = map.get("FedoraBaseURI");
        BagInfoField collectionRoot = map.get("CollectionRoot");
        BagInfoField objektID = map.get("ObjektID");
        BagInfoField IIIFListContainer = map.get("IIIFListContainer");
        return baseURI.getValue() +
                collectionRoot.getValue() +
                objektID.getValue() +
                IIIFListContainer.getValue();
    }
}
