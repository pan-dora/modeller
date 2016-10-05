package org.blume.modeller.ui.handlers.iiif;

import org.blume.modeller.ModellerClient;
import org.blume.modeller.ModellerClientFailedException;
import org.blume.modeller.ProfileOptions;
import org.blume.modeller.XmlFileWriter;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.ui.Progress;
import org.blume.modeller.ui.handlers.base.SaveBagHandler;
import org.blume.modeller.ui.jpanel.BagView;
import org.blume.modeller.ui.jpanel.CreateXmlFilesFrame;
import org.blume.modeller.ui.util.ApplicationContextUtil;
import org.blume.modeller.ui.util.URIResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.*;
import javax.xml.bind.JAXBException;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

public class CreateXmlFilesHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(SaveBagHandler.class);
    private static final long serialVersionUID = 1L;
    private BagView bagView;

    public CreateXmlFilesHandler(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        openCreateXmlFilesFrame();
    }

    @Override
    public void execute() {
        String message = ApplicationContextUtil.getMessage("bag.message.xmlfilecreated");
        DefaultBag bag = bagView.getBag();
        Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        ResourceIdentifierList idList = new ResourceIdentifierList(bagView);
        ArrayList<String> resourceIDList = idList.getResourceIdentifierList();
        ModellerClient client = new ModellerClient();
        String collectionId = URIResolver.ContainerURIResolverNormal.getMapValue(map, ProfileOptions.COLLECTION_ID_KEY);
        String objektId = URIResolver.ContainerURIResolverNormal.getMapValue(map, ProfileOptions.OBJEKT_ID_KEY);
        for (String resourceId : resourceIDList) {
            ByteArrayOutputStream resourceFile = null;
            try {
                resourceFile = getXmlOutputStream(collectionId, objektId, resourceId);
            } catch (JAXBException e) {
                e.printStackTrace();
            }
            assert resourceFile != null;
            ByteArrayInputStream in = new ByteArrayInputStream(resourceFile.toByteArray());
            String filename = resourceId + ".xml";
            String contentType = "application/xml";
            URI destinationURI = getDestinationURI(map, filename);
            try {
                client.doStreamPut(destinationURI, in, contentType);
                ApplicationContextUtil.addConsoleMessage(message + " " + destinationURI);
            } catch (ModellerClientFailedException e) {
                ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
    }

    void openCreateXmlFilesFrame() {
        DefaultBag bag = bagView.getBag();
        CreateXmlFilesFrame createXmlFilesFrame = new CreateXmlFilesFrame(bagView, bagView.getPropertyMessage("bag.frame.xmlfiles"));
        createXmlFilesFrame.setBag(bag);
        createXmlFilesFrame.setVisible(true);
    }

    private ByteArrayOutputStream getXmlOutputStream(String collectionId, String objektId, String resourceId) throws JAXBException {
        ByteArrayOutputStream xmlFileWriter;
        xmlFileWriter = XmlFileWriter.write()
                .collectionId(collectionId)
                .objektId(objektId)
                .resourceId(resourceId)
                .build();
        return xmlFileWriter;
    }

    private URI getDestinationURI(Map<String, BagInfoField> map, String filename) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(ProfileOptions.RESOURCE_CONTAINER_KEY)
                    .resource(filename)
                    .pathType(5)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    public URI getResourceContainerURI(Map<String, BagInfoField> map) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(ProfileOptions.RESOURCE_CONTAINER_KEY)
                    .pathType(4)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

}