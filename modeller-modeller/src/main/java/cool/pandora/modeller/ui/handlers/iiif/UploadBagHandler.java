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
import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;

import gov.loc.repository.bagit.impl.AbstractBagConstants;
import cool.pandora.modeller.ModellerClientFailedException;
import cool.pandora.modeller.bag.BagInfoField;
import cool.pandora.modeller.bag.BaggerFileEntity;
import cool.pandora.modeller.ui.handlers.common.IIIFObjectURI;
import cool.pandora.modeller.util.ImageIOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.Progress;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import cool.pandora.modeller.ui.jpanel.iiif.UploadBagFrame;
import cool.pandora.modeller.ModellerClient;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

/**
 * Upload Bag Handler
 *
 * @author Christopher Johnson
 */
public class UploadBagHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(UploadBagHandler.class);
    private static final long serialVersionUID = 1L;
    private final BagView bagView;

    /**
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
            final URI destinationURI = IIIFObjectURI.getDestinationURI(map, filename);
            final Path absoluteFilePath = rootDir.resolve(filePath);
            final File resourceFile = absoluteFilePath.toFile();
            final String contentType = ImageIOUtil.getImageMIMEType(resourceFile);
            try {
                ModellerClient.doBinaryPut(destinationURI, resourceFile, contentType);
                ApplicationContextUtil.addConsoleMessage(message + " " + destinationURI);
            } catch (final ModellerClientFailedException e) {
                ApplicationContextUtil.addConsoleMessage(getMessage(e));
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