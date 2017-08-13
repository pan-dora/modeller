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

import cool.pandora.modeller.bag.BaggerFileEntity;
import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.jpanel.base.BagView;

import gov.loc.repository.bagit.impl.AbstractBagConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Resource Identifier List.
 *
 * @author Christopher Johnson
 */
class ResourceIdentifierList {
    private final BagView bagView;

    /**
     * ResourceIdentifierList.
     *
     * @param bagView BagView
     */
    ResourceIdentifierList(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    ArrayList<String> getResourceIdentifierList() {
        final DefaultBag bag = bagView.getBag();
        final List<String> payload = bag.getPayloadPaths();
        final ArrayList<String> identifierList = new ArrayList<>();
        final String basePath = AbstractBagConstants.DATA_DIRECTORY;
        for (final String filePath : payload) {
            final String filename = BaggerFileEntity.removeBasePath(basePath, filePath);
            final String resourceID = BaggerFileEntity.removeFileExtension(filename);
            identifierList.add(resourceID);
        }
        identifierList.sort(String.CASE_INSENSITIVE_ORDER);
        return identifierList;
    }

}
