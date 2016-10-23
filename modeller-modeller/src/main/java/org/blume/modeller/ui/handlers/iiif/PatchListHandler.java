package org.blume.modeller.ui.handlers.iiif;

import org.apache.jena.rdf.model.RDFNode;
import org.blume.modeller.*;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.common.uri.IIIFPredicates;
import org.blume.modeller.ui.Progress;
import org.blume.modeller.ui.handlers.common.IIIFObjectURI;
import org.blume.modeller.ui.handlers.common.TextObjectURI;
import org.blume.modeller.ui.handlers.common.TextSequenceMetadata;
import org.blume.modeller.ui.jpanel.base.BagView;
import org.blume.modeller.ui.jpanel.iiif.PatchListFrame;
import org.blume.modeller.ui.util.ApplicationContextUtil;
import org.blume.modeller.util.ResourceList;
import org.blume.modeller.util.ResourceObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.net.URI;
import java.util.*;

import static org.apache.commons.lang.StringUtils.substringAfter;
import static org.apache.commons.lang.StringUtils.substringBefore;
import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

public class PatchListHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(PatchListHandler.class);
    private static final long serialVersionUID = 1L;
    private BagView bagView;

    public PatchListHandler(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void execute() {
        String message = ApplicationContextUtil.getMessage("bag.message.listpatched");
        DefaultBag bag = bagView.getBag();
        Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        URI resourceContainerURI = TextObjectURI.getWordContainerURI(map);
        URI listContainerURI = IIIFObjectURI.getListContainerURI(map);
        String listServiceBaseURI = bag.getListServiceBaseURI();
        ResourceList listList = new ResourceList(listContainerURI);
        ArrayList<String> listsList = listList.getResourceList();
        ResourceList resourceList = new ResourceList(resourceContainerURI);
        ArrayList<String> resourcesList = resourceList.getResourceList();
        String collectionPredicate = IIIFPredicates.OTHER_CONTENT;
        InputStream rdfBody;
        Map<String, List<String>> pageResourcesMap = getPageResourcesMap(listsList, resourcesList);
        Map<String, String> resourceTargetMap = getResourceTargetMap(resourcesList);
        for (String listURI : listsList) {
            rdfBody = TextSequenceMetadata.getListSequenceMetadata(pageResourcesMap, listURI, resourceTargetMap,
                    collectionPredicate, listServiceBaseURI);
            URI destinationURI = URI.create(listURI);
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

    private Map<String, String> getResourceTargetMap(List<String> resourcesList) {
        Map<String, String> wordTargetMap = new HashMap<>();
        for (String resource : resourcesList) {
            RDFNode target = getResourceTarget(resource);
            if (target != null) {
                wordTargetMap.put(resource, target.toString());
            }
        }
        return wordTargetMap;
    }

    private Map<String, List<String>> getPageResourcesMap(ArrayList<String> listsList, ArrayList<String>
            resourcesList) {
        Map<String, List<String>> pageResourcesMap = new HashMap<>();
        for (String list : listsList) {
            ArrayList<String> rl = new ArrayList<>();
            for (String resource : resourcesList) {
                String var1 = substringAfter(list, "list/");
                String var2 = leftPad(substringBefore(substringAfter(resource, "word/"), "_"), 3, "0");
                if (Objects.equals(var1, var2)) {
                    rl.add(resource);
                }
            }
            pageResourcesMap.put(list, rl);
        }
        return pageResourcesMap;
    }

    void openPatchListFrame() {
        DefaultBag bag = bagView.getBag();
        PatchListFrame patchListFrame = new PatchListFrame(bagView,
                bagView.getPropertyMessage("bag.frame.patch.list"));
        patchListFrame.setBag(bag);
        patchListFrame.setVisible(true);
    }

    private RDFNode getResourceTarget(String resourceURI) {
        ResourceObjectNode resourceObjectNode = ResourceObjectNode.init()
                .resourceURI(resourceURI)
                .resourceProperty(IIIFPredicates.ON)
                .build();
        ArrayList<RDFNode> resourceTarget = resourceObjectNode.render();
        if (resourceTarget.isEmpty()) {
            return null;
        }
        return resourceTarget.get(0);
    }
}
