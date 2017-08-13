/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cool.pandora.modeller.ui.handlers.iiif;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

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

import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create Xml Files Handler.
 *
 * @author Christopher Johnson
 */
public class CreateXmlFilesHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(CreateXmlFilesHandler.class);
    private static final long serialVersionUID = 1L;
    private final BagView bagView;

    /**
     * CreateXmlFilesHandler.
     *
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
                URIResolver.ContainerURIResolverNormal.getMapValue(map, ProfileOptions
                        .COLLECTION_ID_KEY);
        final String objektId = URIResolver.ContainerURIResolverNormal.getMapValue(map,
                ProfileOptions.OBJEKT_ID_KEY);
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
                new CreateXmlFilesFrame(bagView, bagView.getPropertyMessage("bag" + ".frame"
                        + ".xmlfiles"));
        createXmlFilesFrame.setBag(bag);
        createXmlFilesFrame.setVisible(true);
    }

    private static ByteArrayOutputStream getXmlOutputStream(final String collectionId, final
    String objektId,
                                                            final String resourceId) throws
            JAXBException {
        final ByteArrayOutputStream xmlFileWriter;
        xmlFileWriter =
                XmlFileWriter.write().collectionId(collectionId).objektId(objektId).resourceId(
                        resourceId).build();
        return xmlFileWriter;
    }
}