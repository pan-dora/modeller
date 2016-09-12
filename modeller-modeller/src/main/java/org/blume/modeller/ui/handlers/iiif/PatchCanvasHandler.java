package org.blume.modeller.ui.handlers.iiif;

import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import javax.swing.AbstractAction;

import org.apache.commons.io.IOUtils;
import org.blume.modeller.ModellerClientFailedException;
import org.blume.modeller.ProfileOptions;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.common.uri.FedoraPrefixes;
import org.blume.modeller.templates.CanvasScope;
import org.blume.modeller.templates.MetadataTemplate;
import org.blume.modeller.ui.jpanel.PatchCanvasFrame;
import org.blume.modeller.ui.util.URIResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.ui.jpanel.BagView;
import org.blume.modeller.ui.Progress;
import org.blume.modeller.ui.util.ApplicationContextUtil;
import org.blume.modeller.ModellerClient;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeXml;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

public class PatchCanvasHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(PatchCanvasHandler.class);
    private static final long serialVersionUID = 1L;
    private BagView bagView;

    public PatchCanvasHandler(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void execute() {
        String message = ApplicationContextUtil.getMessage("bag.message.canvaspatched");
        DefaultBag bag = bagView.getBag();
        Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        URI resourceContainerURI = getResourceContainerURI(map);
        URI canvasContainerURI = getCanvasContainerURI(map);
        URI ListContainerURI = getListContainerURI(map);
        ResourceList canvasList = new ResourceList(canvasContainerURI);
        ArrayList<String> canvasesList = canvasList.getResourceList();
        ResourceList resourceList = new ResourceList(resourceContainerURI);
        ArrayList<String> resourcesList = resourceList.getResourceList();
        ResourceList listList = new ResourceList(ListContainerURI);
        ArrayList<String> listsList = listList.getResourceList();
        Iterator<String> i1 = canvasesList.iterator();
        Iterator<String> i2 = resourcesList.iterator();
        Iterator<String> i3 = canvasesList.iterator();
        Iterator<String> i4 = listsList.iterator();
        Map<String,String> canvasResourceMap = new LinkedHashMap<>();
        Map<String,String> canvasListMap = new LinkedHashMap<>();
        while (i1.hasNext() && i2.hasNext()) {
            canvasResourceMap.put(i1.next(), i2.next());
        }
        while (i3.hasNext() && i4.hasNext()) {
            canvasListMap.put(i3.next(), i4.next());
        }

        InputStream rdfBody;
        for (String canvasURI : canvasesList) {
            rdfBody = getCanvasMetadata(canvasResourceMap.get(canvasURI), canvasListMap.get(canvasURI));
            URI destinationURI = URI.create(canvasURI);
            ModellerClient client = new ModellerClient();
            try {
                client.doPatch(destinationURI, rdfBody);
                ApplicationContextUtil.addConsoleMessage(message + " " + destinationURI);
            } catch (ModellerClientFailedException e) {
                ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
        bagView.getControl().invalidate();
    }

    void openPatchCanvasFrame() {
        DefaultBag bag = bagView.getBag();
        PatchCanvasFrame patchCanvasFrame = new PatchCanvasFrame(bagView,
                bagView.getPropertyMessage("bag.frame.patch.canvas"));
        patchCanvasFrame.setBag(bag);
        patchCanvasFrame.setVisible(true);
    }

    public URI getCanvasContainerURI(Map<String, BagInfoField> map) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(ProfileOptions.CANVAS_CONTAINER_KEY)
                    .pathType(4)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    private URI getResourceContainerURI(Map<String, BagInfoField> map)  {
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

    private URI getListContainerURI(Map<String, BagInfoField> map)  {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(ProfileOptions.LIST_CONTAINER_KEY)
                    .pathType(4)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    private InputStream getCanvasMetadata(String resourceURI, String listURI) {

        MetadataTemplate metadataTemplate;
        List<CanvasScope.Prefix> prefixes = Arrays.asList(
                new CanvasScope.Prefix(FedoraPrefixes.RDFS),
                new CanvasScope.Prefix(FedoraPrefixes.MODE));

        CanvasScope scope = new CanvasScope()
                .fedoraPrefixes(prefixes)
                .resourceURI(resourceURI)
                .listURI(listURI)
                .canvasLabel("test")
                .canvasHeight(3000)
                .canvasWidth(2000);

        metadataTemplate = MetadataTemplate.template()
                .template("template/sparql-update-canvas.mustache")
                .scope(scope)
                .throwExceptionOnFailure()
                .build();

        String metadata = unescapeXml(metadataTemplate.render());
        System.out.println(metadata);
        return IOUtils.toInputStream(metadata, UTF_8 );
    }

}
