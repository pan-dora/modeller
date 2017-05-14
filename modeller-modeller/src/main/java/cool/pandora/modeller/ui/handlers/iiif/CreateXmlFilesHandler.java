package cool.pandora.modeller.ui.handlers.iiif;

import cool.pandora.modeller.ModellerClient;
import cool.pandora.modeller.ModellerClientFailedException;
import cool.pandora.modeller.ProfileOptions;
import cool.pandora.modeller.XmlFileWriter;
import cool.pandora.modeller.bag.BagInfoField;
import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.Progress;
import cool.pandora.modeller.ui.handlers.common.IIIFObjectURI;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.jpanel.iiif.CreateXmlFilesFrame;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import cool.pandora.modeller.ui.util.URIResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.xml.bind.JAXBException;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

/**
 * Create Xml Files Handler
 *
 * @author Christopher Johnson
 */
public class CreateXmlFilesHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(CreateXmlFilesHandler.class);
    private static final long serialVersionUID = 1L;
    private final BagView bagView;

    /**
     * @param bagView BagView
     */
    public CreateXmlFilesHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        openCreateXmlFilesFrame();
    }

    @Override
    public void execute() {
        final String message = ApplicationContextUtil.getMessage("bag.message.xmlfilecreated");
        final DefaultBag bag = bagView.getBag();
        final Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        final ResourceIdentifierList idList = new ResourceIdentifierList(bagView);
        final ArrayList<String> resourceIDList = idList.getResourceIdentifierList();
        final String collectionId =
                URIResolver.ContainerURIResolverNormal.getMapValue(map, ProfileOptions.COLLECTION_ID_KEY);
        final String objektId = URIResolver.ContainerURIResolverNormal.getMapValue(map, ProfileOptions.OBJEKT_ID_KEY);
        for (final String resourceId : resourceIDList) {
            ByteArrayOutputStream resourceFile = null;
            try {
                resourceFile = getXmlOutputStream(collectionId, objektId, resourceId);
            } catch (final JAXBException e) {
                e.printStackTrace();
            }
            assert resourceFile != null;
            final ByteArrayInputStream in = new ByteArrayInputStream(resourceFile.toByteArray());
            final String filename = resourceId + ".xml";
            final String contentType = "application/xml";
            final URI destinationURI = IIIFObjectURI.getDestinationURI(map, filename);
            try {
                ModellerClient.doStreamPut(destinationURI, in, contentType);
                ApplicationContextUtil.addConsoleMessage(message + " " + destinationURI);
            } catch (final ModellerClientFailedException e) {
                ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
    }

    void openCreateXmlFilesFrame() {
        final DefaultBag bag = bagView.getBag();
        final CreateXmlFilesFrame createXmlFilesFrame =
                new CreateXmlFilesFrame(bagView, bagView.getPropertyMessage("bag" + ".frame.xmlfiles"));
        createXmlFilesFrame.setBag(bag);
        createXmlFilesFrame.setVisible(true);
    }

    private static ByteArrayOutputStream getXmlOutputStream(final String collectionId, final String objektId,
                                                            final String resourceId) throws JAXBException {
        final ByteArrayOutputStream xmlFileWriter;
        xmlFileWriter =
                XmlFileWriter.write().collectionId(collectionId).objektId(objektId).resourceId(resourceId).build();
        return xmlFileWriter;
    }
}