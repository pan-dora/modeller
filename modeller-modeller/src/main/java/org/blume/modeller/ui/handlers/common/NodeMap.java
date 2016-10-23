package org.blume.modeller.ui.handlers.common;

import org.apache.commons.lang.StringUtils;
import org.blume.modeller.CanvasPageMap;
import org.blume.modeller.DocManifestBuilder;
import org.blume.modeller.PageIdMap;
import org.blume.modeller.hOCRData;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.blume.modeller.DocManifestBuilder.*;

public class NodeMap {

    public static Map<String, String> getBBoxMap(hOCRData hocr, List<String> pageIdList) {
        Map<String, String> bboxMap = new HashMap<>();
        for (String pageId : pageIdList) {
            String bbox = DocManifestBuilder.getBboxForId(hocr, pageId);
            bboxMap.put(pageId, bbox);
        }
        return bboxMap;
    }

    public static Map<String, String> getBBoxAreaMap(hOCRData hocr, List<String> areaIdList) {
        Map<String, String> bboxMap = new HashMap<>();
        for (String areaId : areaIdList) {
            String bbox = DocManifestBuilder.getBboxForId(hocr, areaId);
            bboxMap.put(areaId, bbox);
        }
        return bboxMap;
    }

    public static Map<String, String> getBBoxLineMap(hOCRData hocr, List<String> lineIdList) {
        Map<String, String> bboxMap = new HashMap<>();
        for (String lineId : lineIdList) {
            String bbox = DocManifestBuilder.getBboxForId(hocr, lineId);
            bboxMap.put(lineId, bbox);
        }
        return bboxMap;
    }

    public static Map<String, String> getBBoxWordMap(hOCRData hocr, List<String> wordIdList) {
        Map<String, String> bboxMap = new HashMap<>();
        for (String wordId : wordIdList) {
            String bbox = DocManifestBuilder.getBboxForId(hocr, wordId);
            bboxMap.put(wordId, bbox);
        }
        return bboxMap;
    }

    public static Map<String, String> getCharWordMap(hOCRData hocr, List<String> wordIdList) {
        Map<String, String> charMap = new HashMap<>();
        for (String wordId : wordIdList) {
            String chars = DocManifestBuilder.getCharsForId(hocr, wordId);
            charMap.put(wordId, chars);
        }
        return charMap;
    }

    public static Map<String, String> getCanvasPageMap(List<String> pageIdList, URI canvasContainerURI) {
        try {
            CanvasPageMap canvasPageMap;
            canvasPageMap = CanvasPageMap.init()
                    .canvasContainerURI(canvasContainerURI)
                    .pageIdList(pageIdList)
                    .build();
            return canvasPageMap.render();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, String> getPageIdMap(List<String> pageIdList) {
        try {
            PageIdMap pageIdMap;
            pageIdMap = PageIdMap.init()
                    .pageIdList(pageIdList)
                    .build();
            return pageIdMap.render();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, List<String>> getAreaIdMap(hOCRData hocr, List<String> pageIdList) {
        Map<String, List<String>> nodemap = new HashMap<>();
        List<String> areaIdList;
        for (String pageId : pageIdList) {
            areaIdList = getAreaIdListforPage(hocr, pageId);
            for (int i = 0; i < areaIdList.size(); i++) {
                String areaId = StringUtils.substringAfter(areaIdList.get(i), "_");
                areaIdList.set(i, areaId);
            }
            pageId = StringUtils.substringAfter(pageId, "_");
            nodemap.put(pageId, areaIdList);
        }
        return nodemap;
    }

    public static Map<String, List<String>> getLineIdMap(hOCRData hocr, List<String> areaIdList) {
        Map<String, List<String>> nodemap = new HashMap<>();
        List<String> lineIdList;
        for (String areaId : areaIdList) {
            lineIdList = getLineIdListforArea(hocr, areaId);
            for (int i = 0; i < lineIdList.size(); i++) {
                String lineId = StringUtils.substringAfter(lineIdList.get(i), "_");
                lineIdList.set(i, lineId);
            }
            areaId = StringUtils.substringAfter(areaId, "_");
            nodemap.put(areaId, lineIdList);
        }
        return nodemap;
    }

    public static Map<String, List<String>> getWordIdMap(hOCRData hocr, List<String> lineIdList) {
        Map<String, List<String>> nodemap = new HashMap<>();
        List<String> wordIdList;
        for (String lineId : lineIdList) {
            wordIdList = getWordIdListforLine(hocr, lineId);
            for (int i = 0; i < wordIdList.size(); i++) {
                String wordId = StringUtils.substringAfter(wordIdList.get(i), "_");
                wordIdList.set(i, wordId);
            }
            lineId = StringUtils.substringAfter(lineId, "_");
            nodemap.put(lineId, wordIdList);
        }
        return nodemap;
    }

    public static Map<String, List<String>> getWordsForPageMap(hOCRData hocr, List<String> pageIdList) {
        Map<String, List<String>> nodemap = new HashMap<>();
        List<String> wordIdList;
        for (String pageId : pageIdList) {
            wordIdList = getWordIdListforPage(hocr, pageId);
            for (int i = 0; i < wordIdList.size(); i++) {
                String wordId = StringUtils.substringAfter(wordIdList.get(i), "_");
                wordIdList.set(i, wordId);
            }
            pageId = StringUtils.substringAfter(pageId, "_");
            nodemap.put(pageId, wordIdList);
        }
        return nodemap;
    }
}
