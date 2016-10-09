package org.blume.modeller;

import org.apache.http.client.utils.URIBuilder;

import java.net.URI;

public class CanvasRegionURI {
    public static CanvasRegionURI.CanvasRegionURIBuilder regionuri() {
        return new CanvasRegionURI.CanvasRegionURIBuilder();
    }

    public static class CanvasRegionURIBuilder {
        private String region;
        private String canvasUri;

        public CanvasRegionURI.CanvasRegionURIBuilder region(String region) {
            this.region = region;
            return this;
        }

        public CanvasRegionURI.CanvasRegionURIBuilder canvasURI(String canvasUri) {
            this.canvasUri = canvasUri;
            return this;
        }

        public String build() {
            return this.canvasUri + "#xywh=" + this.region;
        }
    }
}
