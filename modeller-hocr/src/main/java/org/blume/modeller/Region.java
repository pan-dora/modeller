package org.blume.modeller;

public class Region {
    protected Region() {};

    public static Region.RegionBuilder region() {
        return new Region.RegionBuilder();
    }

    public static class RegionBuilder {
        private String bbox;

        public Region.RegionBuilder bbox(String bbox) {
            this.bbox = bbox;
            return this;
        }

        public String build() {
            String[] parts = this.bbox.split("\\s+");
            int x1 = Integer.parseInt(parts[0]);
            int y1 = Integer.parseInt(parts[1]);
            int x2 = Integer.parseInt(parts[2]);
            int y2 = Integer.parseInt(parts[3]);
            int w = x2 - x1;
            int h = y2 -y1;
            return x1 + "," + y1 + "," + w + "," + h;
        }
    }

}
