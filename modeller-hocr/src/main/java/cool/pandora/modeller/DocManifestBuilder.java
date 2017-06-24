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

import cool.pandora.modeller.util.RDFCollectionWriter;
import org.apache.commons.lang3.StringUtils;
import org.xmlbeam.XBProjector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DocManifestBuilder
 *
 * @author Christopher Johnson
 */
public class DocManifestBuilder {

    private DocManifestBuilder() {
    }

    /**
     *
     * @param url String
     * @return XBProjector
     * @throws IOException Exception
     */
    public static hOCRData gethOCRProjectionFromURL(final String url) throws IOException {
        return new XBProjector(new OmitDTDXMLFactoriesConfig()).io().url(url).read(hOCRData.class);
    }

    /**
     *
     * @param descList List
     * @param hocr hOCRData
     * @return valueMap
     */
    private static Map<String, Object> buildValueMap(final List<String> descList, final hOCRData hocr) {
        final Map<String, Object> valueMap = new HashMap<>();
        for (final String descId : descList) {
            final Object oNode = hocr.getTitleForId(descId);
            valueMap.put(descId, oNode);
        }
        return valueMap;
    }

    /**
     *
     * @param hocr hOCRData
     * @return areaMap
     */
    public static Map getAreaMapForhOCRResource(final hOCRData hocr) {
        final List<String> cAreaIdList = hocr.getCAreaNodeId();
        final Map<String, Object> areaMap = new HashMap<>();
        for (final String cAreaId : cAreaIdList) {
            final List<String> descList = hocr.getCAreaIdDescIds(cAreaId);
            areaMap.put(cAreaId, buildValueMap(descList, hocr));
        }
        return areaMap;
    }

    /**
     *
     * @param hocr hOCRData
     * @param resourceURI String
     * @return collection
     * @throws IOException Exception
     */
    static String getAreaRDFSequenceForhOCRResource(final hOCRData hocr, final String resourceURI) throws IOException {
        final List<String> cAreaIdList = hocr.getCAreaNodeId();
        final RDFCollectionWriter collectionWriter;
        final String collectionPredicate = "http://iiif.io/api/text#hasAreas";
        collectionWriter = RDFCollectionWriter.collection().idList(cAreaIdList).collectionPredicate(collectionPredicate)
                .resourceContainerIRI(resourceURI).build();
        return collectionWriter.render();
    }

    /**
     *
     * @param hocr hOCRData
     * @return PageNodeId
     */
    public static List<String> getPageIdList(final hOCRData hocr) {
        return hocr.getPageNodeId();
    }

    /**
     *
     * @param hocr hOCRData
     * @return CAreaNodeId
     */
    public static List<String> getAreaIdList(final hOCRData hocr) {
        return hocr.getCAreaNodeId();
    }

    /**
     *
     * @param hocr hOCRData
     * @return LineNodeId
     */
    public static List<String> getLineIdList(final hOCRData hocr) {
        return hocr.getLineNodeId();
    }

    /**
     *
     * @param hocr hOCRData
     * @return WordNodeId
     */
    public static List<String> getWordIdList(final hOCRData hocr) {
        return hocr.getWordNodeId();
    }

    /**
     *
     * @param hocr hOCRData
     * @param id String
     * @return CAreasforPage
     */
    public static List<String> getAreaIdListforPage(final hOCRData hocr, final String id) {
        return hocr.getCAreasforPage(id);
    }

    /**
     *
     * @param hocr hOCRData
     * @param id String
     * @return LinesforArea
     */
    public static List<String> getLineIdListforArea(final hOCRData hocr, final String id) {
        return hocr.getLinesforArea(id);
    }

    /**
     *
     * @param hocr hOCRData
     * @param id String
     * @return TitleForId
     */
    public static String getBboxForId(final hOCRData hocr, final String id) {
        return StringUtils.substringBefore(StringUtils.substringAfter(hocr.getTitleForId(id), "bbox "), ";");
    }

    /**
     *
     * @param hocr hOCRData
     * @param id String
     * @return CharsForId
     */
    public static String getCharsForId(final hOCRData hocr, final String id) {
        return hocr.getCharsForId(id);
    }

    /**
     *
     * @param hocr hOCRData
     * @param id String
     * @return WordsforLine
     */
    public static List<String> getWordIdListforLine(final hOCRData hocr, final String id) {
        return hocr.getWordsforLine(id);
    }

    /**
     *
     * @param hocr hOCRData
     * @param id String
     * @return WordsforPage
     */
    public static List<String> getWordIdListforPage(final hOCRData hocr, final String id) {
        return hocr.getWordsforPage(id);
    }

    /**
     *
     * @param hocr File
     * @return hocr
     * @throws JAXBException Exception
     */
    private static ByteArrayOutputStream marshal(final File hocr) throws JAXBException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final JAXBContext jaxbContext = JAXBContext.newInstance(File.class);
        final Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(hocr, out);
        return out;
    }
}