package cool.pandora.modeller;

/**
 * CanvasRegionURI
 *
 * @author Christopher Johnson
 */
public class CanvasRegionURI {

    private CanvasRegionURI() {

    }
    /**
     *
     * @return CanvasRegionURIBuilder
     */
    public static CanvasRegionURI.CanvasRegionURIBuilder regionuri() {
        return new CanvasRegionURI.CanvasRegionURIBuilder();
    }

    public static class CanvasRegionURIBuilder {
        private String region;
        private String canvasUri;

        /**
         *
         * @param region String
         * @return this
         */
        public CanvasRegionURI.CanvasRegionURIBuilder region(final String region) {
            this.region = region;
            return this;
        }

        /**
         *
         * @param canvasUri String
         * @return this
         */
        public CanvasRegionURI.CanvasRegionURIBuilder canvasURI(final String canvasUri) {
            this.canvasUri = canvasUri;
            return this;
        }

        /**
         *
         * @return String
         */
        public String build() {
            return this.canvasUri + "#xywh=" + this.region;
        }
    }
}
