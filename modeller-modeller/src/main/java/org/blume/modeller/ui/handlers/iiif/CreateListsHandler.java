package org.blume.modeller.ui.handlers.iiif;

import org.blume.modeller.ModellerClient;
import org.blume.modeller.ModellerClientFailedException;
import org.blume.modeller.ProfileOptions;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.ui.Progress;
import org.blume.modeller.ui.handlers.base.SaveBagHandler;
import org.blume.modeller.ui.jpanel.BagView;
import org.blume.modeller.ui.jpanel.CreateListsFrame;
import org.blume.modeller.ui.util.ApplicationContextUtil;
import org.blume.modeller.ui.util.ContainerIRIResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.*;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;
import static org.blume.modeller.common.uri.FedoraResources.LISTPREFIX;

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
        Map<String, BagInfoField> map = bag.getInfo().getFieldMap();

        ModellerClient client = new ModellerClient();
        String listContainerURI = getListContainerURI(map);
        String listID = bag.getListID();
        String listObjectURI = getListObjectURI(listContainerURI, listID );
        try {
            client.doPut(listObjectURI);
            ApplicationContextUtil.addConsoleMessage(message + " " + listObjectURI);
        } catch (ModellerClientFailedException e) {
            ApplicationContextUtil.addConsoleMessage(getMessage(e));
        }
        bagView.getControl().invalidate();
    }

    void openCreateListsFrame() {
        DefaultBag bag = bagView.getBag();
        CreateListsFrame createListsFrame = new CreateListsFrame(bagView, bagView.getPropertyMessage("bag.frame.list"));
        createListsFrame.setBag(bag);
        createListsFrame.setVisible(true);
    }

    private String getListObjectURI(String ListContainerURI, String ListID) {
        return ListContainerURI + LISTPREFIX +
                ListID;
    }

    public String getListContainerURI(Map<String, BagInfoField> map) {
        ContainerIRIResolver containerIRIResolver;
        containerIRIResolver = ContainerIRIResolver.resolve()
                .map(map)
                .baseURIKey(ProfileOptions.FEDORA_BASE_KEY)
                .collectionRootKey(ProfileOptions.COLLECTION_ROOT_KEY)
                .collectionKey(ProfileOptions.COLLECTION_ID_KEY)
                .objektIDKey(ProfileOptions.OBJEKT_ID_KEY)
                .containerKey(ProfileOptions.LIST_CONTAINER_KEY)
                .build();
        return containerIRIResolver.render();
    }
}
