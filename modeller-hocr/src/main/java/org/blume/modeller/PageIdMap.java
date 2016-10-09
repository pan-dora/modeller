package org.blume.modeller;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;

public class PageIdMap {
    private Map<String, String> pageIdMap;

    public PageIdMap(Map<String, String> pageIdMap) {
        this.pageIdMap = pageIdMap;
    }

    public static PageIdMap.PageIdMapBuilder init() {
        return new PageIdMap.PageIdMapBuilder();
    }

    public Map<String, String> render() {
        return this.pageIdMap;
    }

    public static class PageIdMapBuilder {

        private List<String> pageIdList;

        PageIdMapBuilder() {}

        public PageIdMap.PageIdMapBuilder pageIdList(List<String> pageIdList) {
            this.pageIdList = pageIdList;
            return this;
        }

        public Map<String, String> getPageIdMap(List<String> pageIdList) {
            Iterator<String> i1 = pageIdList.iterator();
            Iterator<String> i2 = pageIdList.iterator();
            Map<String,String> pageIdMap = new LinkedHashMap<>();
            while (i1.hasNext() && i2.hasNext()) {
                String pageId = StringUtils.substringAfter(i1.next(), "_");
                pageIdMap.put(pageId , i2.next());
            }
            return pageIdMap;
        }

        public PageIdMap build() throws IOException {
            Map<String, String> pageIdMap = getPageIdMap(this.pageIdList);
            return new PageIdMap(pageIdMap);
        }

    }

}
