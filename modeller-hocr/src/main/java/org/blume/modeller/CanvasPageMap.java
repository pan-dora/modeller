package org.blume.modeller;

import org.apache.commons.lang3.StringUtils;
import org.blume.modeller.util.ResourceList;

import java.io.IOException;
import java.net.URI;
import java.util.*;

public class CanvasPageMap {
    private Map<String, String> canvasPageMap;

    public CanvasPageMap(Map<String, String> canvasPageMap) {
        this.canvasPageMap = canvasPageMap;
    }

    public static CanvasPageMap.CanvasPageMapBuilder init() {
        return new CanvasPageMap.CanvasPageMapBuilder();
    }

    public Map<String, String> render() {
        return this.canvasPageMap;
    }

    public static class CanvasPageMapBuilder {

        private List<String> pageIdList;
        private URI canvasContainerURI;

        CanvasPageMapBuilder() {}

        public CanvasPageMap.CanvasPageMapBuilder pageIdList(List<String> pageIdList) {
            this.pageIdList = pageIdList;
            return this;
        }

        public CanvasPageMap.CanvasPageMapBuilder canvasContainerURI(URI canvasContainerURI) {
            this.canvasContainerURI = canvasContainerURI;
            return this;
        }

        public Map<String, String> getMap(List<String> pageIdList, URI canvasContainerURI) {
            ResourceList canvasList = new ResourceList(canvasContainerURI);
            ArrayList<String> canvasesList = canvasList.getResourceList();
            Iterator<String> i1 = pageIdList.iterator();
            Iterator<String> i2 = canvasesList.iterator();
            Map<String,String> canvasPageMap = new LinkedHashMap<>();
            while (i1.hasNext() && i2.hasNext()) {
                String pageId = StringUtils.substringAfter(i1.next(), "_");
                canvasPageMap.put(pageId , i2.next());
            }
            return canvasPageMap;
        }

        public Map<String, String> getPageIdMap(List<String> pageIdList) {
            Iterator<String> i1 = pageIdList.iterator();
            Map<String,String> pageIdMap = new LinkedHashMap<>();
            while (i1.hasNext()) {
                String pageId = StringUtils.substringAfter(i1.next(), "_");
                pageIdMap.put(pageId , i1.next());
            }
            return pageIdMap;
        }

        public CanvasPageMap build() throws IOException {
            Map<String, String> canvasPageMap = getMap(this.pageIdList, this.canvasContainerURI);
            return new CanvasPageMap(canvasPageMap);
        }

   }

}
