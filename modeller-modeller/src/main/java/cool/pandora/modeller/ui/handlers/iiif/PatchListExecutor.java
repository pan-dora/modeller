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

import cool.pandora.modeller.ui.jpanel.base.BagView;
import org.springframework.richclient.command.support.AbstractActionCommandExecutor;

/**
 * Patch List Executor.
 *
 * @author Christopher Johnson
 */
public class PatchListExecutor extends AbstractActionCommandExecutor {
    BagView bagView;

    /**
     * PatchListExecutor.
     *
     * @param bagView BagView
     */
    public PatchListExecutor(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void execute() {
        bagView.patchListHandler.openPatchListFrame();
    }

}
