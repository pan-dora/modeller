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

import static org.apache.commons.lang.StringUtils.substringAfter;
import static org.apache.commons.lang.StringUtils.substringBefore;
import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

import cool.pandora.modeller.ModellerClient;
import cool.pandora.modeller.ModellerClientFailedException;
import cool.pandora.modeller.bag.BagInfoField;
import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.common.uri.IIIFPredicates;
import cool.pandora.modeller.ui.Progress;
import cool.pandora.modeller.ui.handlers.common.IIIFObjectURI;
import cool.pandora.modeller.ui.handlers.common.TextObjectURI;
import cool.pandora.modeller.ui.handlers.common.TextSequenceMetadata;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.jpanel.iiif.PatchListFrame;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import cool.pandora.modeller.util.ResourceList;
import cool.pandora.modeller.util.ResourceObjectNode;
import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.swing.AbstractAction;
import org.apache.jena.rdf.model.RDFNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Patch List Handler.
 *
 * @author Christopher Johnson
 */
public class PatchListHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(PatchListHandler.class);
    private static final long serialVersionUID = 1L;
    private final BagView bagView;

    /**
     * PatchListHandler.
     *
     * @param bagView BagView
     */
    public PatchListHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    private static Map<String, String> getResourceTargetMap(final List<String> resourcesList) {
        final Map<String, String> wordTargetMap = new HashMap<>();
        for (final String resource : resourcesList) {
            final RDFNode target = getResourceTarget(resource);
            if (target != null) {
                wordTargetMap.put(resource, target.toString());
            }
        }
        return wordTargetMap;
    }

    private static Map<String, List<String>> getPageResourcesMap(final ArrayList<String> listsList,
                                                                 final ArrayList<String>
                                                                         resourcesList) {
        final Map<String, List<String>> pageResourcesMap = new HashMap<>();
        for (final String list : listsList) {
            final ArrayList<String> rl = new ArrayList<>();
            for (final String resource : resourcesList) {
                final String var1 = substringAfter(list, "list/");
                final String var2 =
                        leftPad(substringBefore(substringAfter(resource, "word/"), "_"), 3, "0");
                if (Objects.equals(var1, var2)) {
                    rl.add(resource);
                }
            }
            pageResourcesMap.put(list, rl);
        }
        return pageResourcesMap;
    }

    private static RDFNode getResourceTarget(final String resourceURI) {
        final ResourceObjectNode resourceObjectNode =
                ResourceObjectNode.init().resourceURI(resourceURI)
                        .resourceProperty(IIIFPredicates.ON).build();
        final ArrayList<RDFNode> resourceTarget = resourceObjectNode.render();
        if (resourceTarget.isEmpty()) {
            return null;
        }
        return resourceTarget.get(0);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
    }

    @Override
    public void execute() {
        final String message = ApplicationContextUtil.getMessage("bag.message.listpatched");
        final DefaultBag bag = bagView.getBag();
        final Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        final URI resourceContainerURI = TextObjectURI.getWordContainerURI(map);
        final URI listContainerURI = IIIFObjectURI.getListContainerURI(map);
        final String listServiceBaseURI = bag.getListServiceBaseURI();
        final ResourceList listList = new ResourceList(listContainerURI);
        final ArrayList<String> listsList = listList.getResourceList();
        final ResourceList resourceList = new ResourceList(resourceContainerURI);
        final ArrayList<String> resourcesList = resourceList.getResourceList();
        final String collectionPredicate = IIIFPredicates.OTHER_CONTENT;
        InputStream rdfBody;
        final Map<String, List<String>> pageResourcesMap =
                getPageResourcesMap(listsList, resourcesList);
        final Map<String, String> resourceTargetMap = getResourceTargetMap(resourcesList);
        for (final String listURI : listsList) {
            rdfBody = TextSequenceMetadata
                    .getListSequenceMetadata(pageResourcesMap, listURI, resourceTargetMap,
                            collectionPredicate, listServiceBaseURI);
            final URI destinationURI = URI.create(listURI);
            try {
                ModellerClient.doPatch(destinationURI, rdfBody);
                ApplicationContextUtil.addConsoleMessage(message + " " + destinationURI);
            } catch (final ModellerClientFailedException e) {
                ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
        bagView.getControl().invalidate();
    }

    void openPatchListFrame() {
        final DefaultBag bag = bagView.getBag();
        final PatchListFrame patchListFrame =
                new PatchListFrame(bagView, bagView.getPropertyMessage("bag.frame.patch.list"));
        patchListFrame.setBag(bag);
        patchListFrame.setVisible(true);
    }
}
