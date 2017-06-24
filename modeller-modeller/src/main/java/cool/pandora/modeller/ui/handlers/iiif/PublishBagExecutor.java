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

import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

/**
 * Publish Bag Executor
 *
 * @author Christopher Johnson
 */
public class PublishBagExecutor extends AbstractActionCommandExecutor {
    private final BagView bagView;

    /**
     * @param bagView BagView
     */
    public PublishBagExecutor(final BagView bagView) {
        super();
        setEnabled(true);
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        final DefaultBag bag = bagView.getBag();
        final String message = ApplicationContextUtil.getMessage("bag.message.bagpublished");
        final String baglabel = bag.getName();
        try {
            bagView.createDefaultContainersHandler.execute();
            bagView.uploadBagHandler.execute();
            bagView.patchResourceHandler.execute();
            bagView.createListsHandler.execute();
            bagView.createCanvasesHandler.execute();
            bagView.patchCanvasHandler.execute();
            bagView.createSequencesHandler.execute();
            bagView.patchSequenceHandler.execute();
            bagView.patchManifestHandler.execute();
            ApplicationContextUtil.addConsoleMessage(message + " " + baglabel);
        } catch (final Exception e) {
            ApplicationContextUtil.addConsoleMessage(getMessage(e));
        }
    }
}
