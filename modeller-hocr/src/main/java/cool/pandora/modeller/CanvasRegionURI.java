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

/**
 * CanvasRegionURI.
 *
 * @author Christopher Johnson
 */
public class CanvasRegionURI {

    private CanvasRegionURI() {

    }

    /**
     * regionuri.
     *
     * @return CanvasRegionURIBuilder
     */
    public static CanvasRegionURI.CanvasRegionURIBuilder regionuri() {
        return new CanvasRegionURI.CanvasRegionURIBuilder();
    }

    /**
     * CanvasRegionURIBuilder.
     */
    public static class CanvasRegionURIBuilder {
        private String region;
        private String canvasUri;

        /**
         * region.
         *
         * @param region String
         * @return this
         */
        public CanvasRegionURI.CanvasRegionURIBuilder region(final String region) {
            this.region = region;
            return this;
        }

        /**
         * canvasURI.
         *
         * @param canvasUri String
         * @return this
         */
        public CanvasRegionURI.CanvasRegionURIBuilder canvasURI(final String canvasUri) {
            this.canvasUri = canvasUri;
            return this;
        }

        /**
         * build.
         *
         * @return String
         */
        public String build() {
            return this.canvasUri + "#xywh=" + this.region;
        }
    }
}
