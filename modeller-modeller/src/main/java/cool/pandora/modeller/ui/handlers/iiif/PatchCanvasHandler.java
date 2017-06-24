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
import java.util.Map;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Arrays;

import javax.swing.AbstractAction;

import org.apache.commons.io.IOUtils;
import cool.pandora.modeller.ModellerClientFailedException;
import cool.pandora.modeller.bag.BagInfoField;
import cool.pandora.modeller.common.uri.FedoraPrefixes;
import cool.pandora.modeller.common.uri.FedoraResources;
import cool.pandora.modeller.common.uri.IIIFPredicates;
import cool.pandora.modeller.common.uri.IIIFPrefixes;
import cool.pandora.modeller.templates.CanvasScope;
import cool.pandora.modeller.templates.MetadataTemplate;
import cool.pandora.modeller.ui.handlers.common.IIIFObjectURI;
import cool.pandora.modeller.ui.jpanel.iiif.PatchCanvasFrame;
import cool.pandora.modeller.util.ResourceList;
import cool.pandora.modeller.util.ResourceIntegerValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.Progress;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import cool.pandora.modeller.ModellerClient;

import static org.apache.commons.lang.StringUtils.substringAfter;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeXml;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

/**
 * Patch Canvas Handler
 *
 * @author Christopher Johnson
 */
public class PatchCanvasHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(PatchCanvasHandler.class);
    private static final long serialVersionUID = 1L;
    private final BagView bagView;

    /**
     * @param bagView BagView
     */
    public PatchCanvasHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
    }

    @Override
    public void execute() {
        final String message = ApplicationContextUtil.getMessage("bag.message.canvaspatched");
        final DefaultBag bag = bagView.getBag();
        final Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        final URI resourceContainerURI = IIIFObjectURI.getResourceContainerURI(map);
        final URI canvasContainerURI = IIIFObjectURI.getCanvasContainerURI(map);
        final URI ListContainerURI = IIIFObjectURI.getListContainerURI(map);
        final ResourceList canvasList = new ResourceList(canvasContainerURI);
        final ArrayList<String> canvasesList = canvasList.getResourceList();
        final ResourceList resourceList = new ResourceList(resourceContainerURI);
        final ArrayList<String> resourcesList = resourceList.getResourceList();
        final ResourceList listList = new ResourceList(ListContainerURI);
        final ArrayList<String> listsList = listList.getResourceList();
        final Iterator<String> i1 = canvasesList.iterator();
        final Iterator<String> i2 = resourcesList.iterator();
        final Iterator<String> i3 = canvasesList.iterator();
        final Iterator<String> i4 = listsList.iterator();
        final Map<String, String> canvasResourceMap = new LinkedHashMap<>();
        final Map<String, String> canvasListMap = new LinkedHashMap<>();
        while (i1.hasNext() && i2.hasNext()) {
            canvasResourceMap.put(i1.next(), i2.next());
        }
        while (i3.hasNext() && i4.hasNext()) {
            canvasListMap.put(i3.next(), i4.next());
        }

        InputStream rdfBody;
        for (final String canvasURI : canvasesList) {
            rdfBody = getCanvasMetadata(canvasResourceMap.get(canvasURI), canvasListMap.get(canvasURI));
            final URI destinationURI = URI.create(canvasURI);
            try {
                ModellerClient.doPatch(destinationURI, rdfBody);
                ApplicationContextUtil.addConsoleMessage(message + " " + destinationURI);
            } catch (final ModellerClientFailedException e) {
                ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
        bagView.getControl().invalidate();
    }

    void openPatchCanvasFrame() {
        final DefaultBag bag = bagView.getBag();
        final PatchCanvasFrame patchCanvasFrame =
                new PatchCanvasFrame(bagView, bagView.getPropertyMessage("bag.frame.patch.canvas"));
        patchCanvasFrame.setBag(bag);
        patchCanvasFrame.setVisible(true);
    }


    private static InputStream getCanvasMetadata(final String resourceURI, final String listURI) {

        final MetadataTemplate metadataTemplate;
        final List<CanvasScope.Prefix> prefixes =
                Arrays.asList(new CanvasScope.Prefix(FedoraPrefixes.RDFS), new CanvasScope.Prefix(FedoraPrefixes.MODE),
                        new CanvasScope.Prefix(IIIFPrefixes.SC), new CanvasScope.Prefix(IIIFPrefixes.OA),
                        new CanvasScope.Prefix(IIIFPrefixes.EXIF));

        final ResourceIntegerValue resourceHeight =
                ResourceIntegerValue.init().resourceURI(resourceURI + FedoraResources.FCRMETADATA)
                        .resourceProperty(IIIFPredicates.HEIGHT).build();
        final int resheight = resourceHeight.render().get(0);

        final ResourceIntegerValue resourceWidth =
                ResourceIntegerValue.init().resourceURI(resourceURI + FedoraResources.FCRMETADATA)
                        .resourceProperty(IIIFPredicates.WIDTH).build();
        final int reswidth = resourceWidth.render().get(0);

        final String canvasLabel = substringAfter(listURI, "list/");
        final CanvasScope scope = new CanvasScope().fedoraPrefixes(prefixes).resourceURI(resourceURI).listURI(listURI)
                .canvasLabel(canvasLabel).canvasHeight(resheight).canvasWidth(reswidth);

        metadataTemplate = MetadataTemplate.template().template("template/sparql-update-canvas.mustache").scope(scope)
                .throwExceptionOnFailure().build();

        final String metadata = unescapeXml(metadataTemplate.render());
        return IOUtils.toInputStream(metadata, UTF_8);
    }

}
