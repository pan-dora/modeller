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

package cool.pandora.modeller;

import cool.pandora.modeller.util.ResourceList;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * CanvasPageMap.
 *
 * @author Christopher Johnson
 */
public class CanvasPageMap {
    private final Map<String, String> canvasPageMap;

    /**
     * CanvasPageMap.
     *
     * @param canvasPageMap Map
     */
    CanvasPageMap(final Map<String, String> canvasPageMap) {
        this.canvasPageMap = canvasPageMap;
    }

    /**
     * init.
     *
     * @return CanvasPageMapBuilder
     */
    public static CanvasPageMap.CanvasPageMapBuilder init() {
        return new CanvasPageMap.CanvasPageMapBuilder();
    }

    /**
     * render.
     *
     * @return canvasPageMap
     */
    public Map<String, String> render() {
        return this.canvasPageMap;
    }

    /**
     * CanvasPageMapBuilder.
     */
    public static class CanvasPageMapBuilder {

        private List<String> pageIdList;
        private URI canvasContainerURI;

        CanvasPageMapBuilder() {
        }

        /**
         * pageIdList.
         *
         * @param pageIdList List
         * @return this
         */
        public CanvasPageMap.CanvasPageMapBuilder pageIdList(final List<String> pageIdList) {
            this.pageIdList = pageIdList;
            return this;
        }

        /**
         * canvasContainerURI.
         *
         * @param canvasContainerURI URI
         * @return this
         */
        public CanvasPageMap.CanvasPageMapBuilder canvasContainerURI(final URI canvasContainerURI) {
            this.canvasContainerURI = canvasContainerURI;
            return this;
        }

        /**
         * getMap.
         *
         * @param pageIdList List
         * @param canvasContainerURI URI
         * @return canvasPageMap
         */
        static Map<String, String> getMap(final List<String> pageIdList, final URI
                canvasContainerURI) {
            final ResourceList canvasList = new ResourceList(canvasContainerURI);
            final ArrayList<String> canvasesList = canvasList.getResourceList();
            final Iterator<String> i1 = pageIdList.iterator();
            final Iterator<String> i2 = canvasesList.iterator();
            final Map<String, String> canvasPageMap = new LinkedHashMap<>();
            while (i1.hasNext() && i2.hasNext()) {
                final String pageId = StringUtils.substringAfter(i1.next(), "_");
                canvasPageMap.put(pageId, i2.next());
            }
            return canvasPageMap;
        }

        /**
         * build.
         *
         * @return CanvasPageMap
         * @throws IOException Exception
         */
        public CanvasPageMap build() throws IOException {
            final Map<String, String> canvasPageMap = getMap(this.pageIdList, this
                    .canvasContainerURI);
            return new CanvasPageMap(canvasPageMap);
        }

    }

}
