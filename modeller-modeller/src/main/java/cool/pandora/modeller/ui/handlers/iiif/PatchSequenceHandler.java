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

import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;

import org.apache.commons.io.IOUtils;
import cool.pandora.modeller.ModellerClientFailedException;
import cool.pandora.modeller.ProfileOptions;
import cool.pandora.modeller.bag.BagInfoField;
import cool.pandora.modeller.common.uri.FedoraPrefixes;
import cool.pandora.modeller.templates.CollectionScope;
import cool.pandora.modeller.templates.MetadataTemplate;
import cool.pandora.modeller.ui.handlers.common.IIIFObjectURI;
import cool.pandora.modeller.ui.jpanel.iiif.PatchSequenceFrame;
import cool.pandora.modeller.ui.util.URIResolver;
import cool.pandora.modeller.util.RDFCollectionWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.Progress;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import cool.pandora.modeller.ModellerClient;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeXml;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

/**
 * Patch Sequence Handler
 *
 * @author Christopher Johnson
 */
public class PatchSequenceHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(PatchSequenceHandler.class);
    private static final long serialVersionUID = 1L;
    private final BagView bagView;

    /**
     * @param bagView BagView
     */
    public PatchSequenceHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
    }

    @Override
    public void execute() {
        final String message = ApplicationContextUtil.getMessage("bag.message.sequencepatched");
        final DefaultBag bag = bagView.getBag();
        final Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        final ResourceIdentifierList idList = new ResourceIdentifierList(bagView);
        final ArrayList<String> resourceIDList = idList.getResourceIdentifierList();
        final InputStream rdfBody;
        final String collectionPredicate = "http://iiif.io/api/presentation/2#hasCanvases";
        final URI canvasContainerIRI = IIIFObjectURI.getCanvasContainerURI(map);
        //TODO Lookup IDs from Created Sequences and provide selection box in Frame
        //final String sequenceID = bag.getSequenceID();
        rdfBody = getSequenceMetadata(resourceIDList, collectionPredicate, canvasContainerIRI);
        // "normal" is static sequence ID value for testing
        final URI destinationURI = getDestinationURI(map);
        try {
            ModellerClient.doPatch(destinationURI, rdfBody);
            ApplicationContextUtil.addConsoleMessage(message + " " + destinationURI);
        } catch (final ModellerClientFailedException e) {
            ApplicationContextUtil.addConsoleMessage(getMessage(e));
        }
        bagView.getControl().invalidate();
    }

    void openPatchSequenceFrame() {
        final DefaultBag bag = bagView.getBag();
        final PatchSequenceFrame patchSequencesFrame =
                new PatchSequenceFrame(bagView, bagView.getPropertyMessage("bag.frame.patch.sequence"));
        patchSequencesFrame.setBag(bag);
        patchSequencesFrame.setVisible(true);
    }

    private static URI getDestinationURI(final Map<String, BagInfoField> map) {
        final URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve().map(map).containerKey(ProfileOptions.SEQUENCE_CONTAINER_KEY)
                    .resource("normal").pathType(5).build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    private static InputStream getSequenceMetadata(final ArrayList<String> resourceIDList,
                                                   final String collectionPredicate, final URI resourceContainerIRI) {
        final RDFCollectionWriter collectionWriter;
        collectionWriter =
                RDFCollectionWriter.collection().idList(resourceIDList).collectionPredicate(collectionPredicate)
                        .resourceContainerIRI(resourceContainerIRI.toString()).build();

        final String collection = collectionWriter.render();
        final MetadataTemplate metadataTemplate;
        final List<CollectionScope.Prefix> prefixes = Arrays.asList(new CollectionScope.Prefix(FedoraPrefixes.RDFS),
                new CollectionScope.Prefix(FedoraPrefixes.MODE));

        final CollectionScope scope = new CollectionScope().fedoraPrefixes(prefixes).sequenceGraph(collection);

        metadataTemplate = MetadataTemplate.template().template("template/sparql-update-seq.mustache").scope(scope)
                .throwExceptionOnFailure().build();

        final String metadata = unescapeXml(metadataTemplate.render());
        return IOUtils.toInputStream(metadata, UTF_8);
    }

}