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

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * PageIdMap
 *
 * @author Christopher Johnson
 */
public class PageIdMap {
    private final Map<String, String> pageIdMap;

    /**
     *
     * @param pageIdMap Map
     */
    PageIdMap(final Map<String, String> pageIdMap) {
        this.pageIdMap = pageIdMap;
    }

    /**
     *
     * @return PageIdMapBuilder
     */
    public static PageIdMap.PageIdMapBuilder init() {
        return new PageIdMap.PageIdMapBuilder();
    }

    /**
     *
     * @return Map
     */
    public Map<String, String> render() {
        return this.pageIdMap;
    }

    public static class PageIdMapBuilder {

        private List<String> pageIdList;

        PageIdMapBuilder() {
        }

        /**
         *
         * @param pageIdList List
         * @return this
         */
        public PageIdMap.PageIdMapBuilder pageIdList(final List<String> pageIdList) {
            this.pageIdList = pageIdList;
            return this;
        }

        /**
         *
         * @param pageIdList List
         * @return pageIdMap
         */
        static Map<String, String> getPageIdMap(final List<String> pageIdList) {
            final Iterator<String> i1 = pageIdList.iterator();
            final Iterator<String> i2 = pageIdList.iterator();
            final Map<String, String> pageIdMap = new LinkedHashMap<>();
            while (i1.hasNext() && i2.hasNext()) {
                final String pageId = StringUtils.substringAfter(i1.next(), "_");
                pageIdMap.put(pageId, i2.next());
            }
            return pageIdMap;
        }

        /**
         *
         * @return PageIdMap
         * @throws IOException Exception
         */
        public PageIdMap build() throws IOException {
            final Map<String, String> pageIdMap = getPageIdMap(this.pageIdList);
            return new PageIdMap(pageIdMap);
        }

    }

}
