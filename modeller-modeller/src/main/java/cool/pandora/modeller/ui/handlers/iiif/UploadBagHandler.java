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

import static java.util.Collections.singletonMap;
import static java.util.Optional.ofNullable;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;


import cool.pandora.modeller.ldpclient.HttpClient9;
import cool.pandora.modeller.bag.BagInfoField;
import cool.pandora.modeller.bag.BaggerFileEntity;
import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.Progress;
import cool.pandora.modeller.ui.handlers.common.IIIFObjectURI;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.jpanel.iiif.UploadBagFrame;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import gov.loc.repository.bagit.impl.AbstractBagConstants;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;
import javax.swing.AbstractAction;
import org.apache.commons.rdf.api.IRI;
import org.apache.commons.rdf.jena.JenaRDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Upload Bag Handler.
 *
 * @author Christopher Johnson
 */
public class UploadBagHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(UploadBagHandler.class);
    private static final long serialVersionUID = 1L;
    private final BagView bagView;
    private final HttpClient9 client = new HttpClient9();
    private static final JenaRDF rdf = new JenaRDF();

    /**
     * UploadBagHandler.
     *
     * @param bagView BagView
     */
    public UploadBagHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
    }

    @Override
    public void execute() {
        final String message = ApplicationContextUtil.getMessage("bag.message.fileuploaded");
        final DefaultBag bag = bagView.getBag();
        final List<String> payload = bag.getPayloadPaths();
        final Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        final String basePath = AbstractBagConstants.DATA_DIRECTORY;
        final Path rootDir = bagView.getBagRootPath().toPath();
        for (final String filePath : payload) {
            final String filename = BaggerFileEntity.removeBasePath(basePath, filePath);
            URI uri = IIIFObjectURI.getDestinationURI(map, filename);
            final IRI identifier = rdf.createIRI(Objects.requireNonNull(uri).toString());
            final Path absoluteFilePath = rootDir.resolve(filePath);
            final File resourceFile = absoluteFilePath.toFile();
            final String hash = bag.;
            try {
                final InputStream targetStream = new FileInputStream(resourceFile);
                final FileTypeMap mimes = FileTypeMap.getDefaultFileTypeMap();
                ((MimetypesFileTypeMap) mimes).addMimeTypes("application/xml xml XML");
                final String contentType = mimes.getContentType(resourceFile);
                final Map<String, String> metadata = singletonMap(
                        CONTENT_TYPE, ofNullable(contentType)
                                .orElse(APPLICATION_OCTET_STREAM));
                client.putStream(identifier, targetStream, metadata);
                ApplicationContextUtil.addConsoleMessage(message + " " + identifier);
            } catch (InterruptedException | IOException | URISyntaxException e) {
                e.printStackTrace();
            }

        }
        bagView.getControl().invalidate();
    }

    void openUploadBagFrame() {
        final DefaultBag bag = bagView.getBag();
        final UploadBagFrame uploadBagFrame =
                new UploadBagFrame(bagView, bagView.getPropertyMessage("bag.frame" + ".upload"));
        uploadBagFrame.setBag(bag);
        uploadBagFrame.setVisible(true);
    }
}