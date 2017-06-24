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
 * Region
 *
 * @author Christopher Johnson
 */
public class Region {
    protected Region() {
    }

    /**
     *
     * @return RegionBuilder
     */
    public static Region.RegionBuilder region() {
        return new Region.RegionBuilder();
    }

    public static class RegionBuilder {
        private String bbox;

        /**
         *
         * @param bbox String
         * @return this
         */
        public Region.RegionBuilder bbox(final String bbox) {
            this.bbox = bbox;
            return this;
        }

        /**
         *
         * @return String
         */
        public String build() {
            final String[] parts = this.bbox.split("\\s+");
            final int x1 = Integer.parseInt(parts[0]);
            final int y1 = Integer.parseInt(parts[1]);
            final int x2 = Integer.parseInt(parts[2]);
            final int y2 = Integer.parseInt(parts[3]);
            final int w = x2 - x1;
            final int h = y2 - y1;
            return x1 + "," + y1 + "," + w + "," + h;
        }
    }

}
