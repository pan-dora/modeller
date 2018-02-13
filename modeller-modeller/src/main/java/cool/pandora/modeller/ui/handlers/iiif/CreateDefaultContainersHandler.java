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

import cool.pandora.modeller.ldpclient.HttpClient9;
import cool.pandora.modeller.ProfileOptions;
import cool.pandora.modeller.bag.BagInfoField;
import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.Progress;
import cool.pandora.modeller.ui.handlers.base.SaveBagHandler;
import cool.pandora.modeller.ui.handlers.common.IIIFObjectURI;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.jpanel.iiif.CreateDefaultContainersFrame;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import javax.swing.AbstractAction;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Create Default Containers Handler.
 *
 * @author Christopher Johnson
 */
public class CreateDefaultContainersHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(SaveBagHandler.class);
    private static final long serialVersionUID = 1L;
    private final BagView bagView;
    private final HttpClient9 client = new HttpClient9();

    /**
     * CreateDefaultContainersHandler.
     *
     * @param bagView BagView
     */
    public CreateDefaultContainersHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
    }

    @Override
    public void execute() {
        final String message = ApplicationContextUtil.getMessage("bag.message.containercreated");
        final DefaultBag bag = bagView.getBag();
        final Map<String, BagInfoField> map = bag.getInfo().getFieldMap();

        final URI collectionIDURI = IIIFObjectURI.getCollectionIdURI(map);
        final URI objektURI = IIIFObjectURI.getObjektURI(map);

        try {
            URI collParent = new URI(StringUtils.substringBeforeLast(
                    Objects.requireNonNull(collectionIDURI).toString(),"/"));
            String collSlug = new File(Objects.requireNonNull(collectionIDURI).getPath()).getName();
            client.doCreateDirectContainer(collParent, collSlug);
        } catch (IOException | URISyntaxException | InterruptedException e) {
            ApplicationContextUtil.addConsoleMessage(getMessage(e));
        }

        try {
            URI objParent = new URI(StringUtils.substringBeforeLast(
                    Objects.requireNonNull(objektURI).toString(),"/"));
            String objSlug = new File(Objects.requireNonNull(objektURI).getPath()).getName();
            client.doCreateDirectContainer(objParent, objSlug);
        } catch (IOException | URISyntaxException | InterruptedException e) {
            ApplicationContextUtil.addConsoleMessage(getMessage(e));
        }

        final String[] Containers;
        final String[] IIIFContainers = new String[]{ProfileOptions.RESOURCE_CONTAINER_KEY,
                ProfileOptions.MANIFEST_RESOURCE_LABEL, ProfileOptions.SEQUENCE_CONTAINER_KEY,
                ProfileOptions.RANGE_CONTAINER_KEY, ProfileOptions.CANVAS_CONTAINER_KEY,
                ProfileOptions.LIST_CONTAINER_KEY, ProfileOptions.LAYER_CONTAINER_KEY};

        final String[] TextContainers = new String[]{ProfileOptions.TEXT_PAGE_CONTAINER_KEY,
                ProfileOptions.TEXT_AREA_CONTAINER_KEY, ProfileOptions.TEXT_LINE_CONTAINER_KEY,
                ProfileOptions.TEXT_WORD_CONTAINER_KEY};

        if (bag.hasText()) {
            Containers = Stream.concat(Arrays.stream(IIIFContainers), Arrays.stream(TextContainers))
                    .toArray(String[]::new);
        } else {
            Containers = IIIFContainers;
        }

        for (final String containerKey : Containers) {
            final URI containerURI = IIIFObjectURI.buildContainerURI(map, containerKey);
            try {
                URI parent = new URI(StringUtils.substringBeforeLast(
                        Objects.requireNonNull(containerURI).toString(),"/"));
                String slug = new File(Objects.requireNonNull(containerURI).getPath()).getName();
                client.doCreateDirectContainer(parent, slug);
                ApplicationContextUtil.addConsoleMessage(message + " " + containerURI);
            } catch (final IOException | URISyntaxException | InterruptedException e) {
                ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
        bagView.getControl().invalidate();
    }

    void openCreateDefaultContainersFrame() {
        final DefaultBag bag = bagView.getBag();
        final CreateDefaultContainersFrame createDefaultContainersFrame =
                new CreateDefaultContainersFrame(bagView,
                        bagView.getPropertyMessage("bag.frame" + ".put"));
        createDefaultContainersFrame.setBag(bag);
        createDefaultContainersFrame.setVisible(true);
    }
}