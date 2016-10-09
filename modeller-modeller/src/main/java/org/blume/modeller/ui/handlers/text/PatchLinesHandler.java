package org.blume.modeller.ui.handlers.text;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.blume.modeller.*;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.common.uri.FedoraPrefixes;
import org.blume.modeller.templates.CollectionScope;
import org.blume.modeller.templates.MetadataTemplate;
import org.blume.modeller.ui.Progress;
import org.blume.modeller.ui.handlers.common.TextObjectURI;
import org.blume.modeller.ui.jpanel.base.BagView;
import org.blume.modeller.ui.jpanel.text.PatchLinesFrame;
import org.blume.modeller.ui.util.ApplicationContextUtil;
import org.blume.modeller.util.RDFCollectionWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeXml;
import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;
import static org.blume.modeller.DocManifestBuilder.*;

public class PatchLinesHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(PatchLinesHandler.class);
    private static final long serialVersionUID = 1L;
    private BagView bagView;

    public PatchLinesHandler(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        openPatchLinesFrame();
    }

    @Override
    public void execute() {
        String message = ApplicationContextUtil.getMessage("bag.message.linepatched");
        DefaultBag bag = bagView.getBag();
        Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        ModellerClient client = new ModellerClient();
        URI wordContainerIRI = TextObjectURI.getWordContainerURI(map);
        String collectionPredicate = "http://iiif.io/api/text#hasWords";

        String url = bag.gethOCRResource();

        List<String> lineIdList;
        Map<String, List<String>> nodemap = null;
        InputStream rdfBody;

        try {
            hOCRData hocr = DocManifestBuilder.gethOCRProjectionFromURL(url);
            lineIdList = getLineIdList(hocr);
            nodemap = getLineIdMap(hocr, lineIdList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert nodemap != null;
        List<String> lineKeyList = new ArrayList<>(nodemap.keySet());

        for (String lineId : lineKeyList) {
            URI lineObjectURI = TextObjectURI.getLineObjectURI(map, lineId);
            rdfBody = getWordSequenceMetadata(nodemap, lineId, collectionPredicate, wordContainerIRI);
            try {
                client.doPatch(lineObjectURI, rdfBody);
                ApplicationContextUtil.addConsoleMessage(message + " " + lineObjectURI);
            } catch (ModellerClientFailedException e) {
                ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
        bagView.getControl().invalidate();
    }

    private Map<String, List<String>> getLineIdMap(hOCRData hocr, List<String> lineIdList) {
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

    void openPatchLinesFrame() {
        DefaultBag bag = bagView.getBag();
        PatchLinesFrame patchLinesFrame = new PatchLinesFrame(bagView, bagView.getPropertyMessage("bag.frame.patch.lines"));
        patchLinesFrame.setBag(bag);
        patchLinesFrame.setVisible(true);
    }

    private InputStream getWordSequenceMetadata(Map<String, List<String>> resourceIDList, String pageId,
                                                String collectionPredicate, URI resourceContainerIRI) {
        ArrayList<String> idList = new ArrayList<>(resourceIDList.get(pageId));
        RDFCollectionWriter collectionWriter;
        collectionWriter = RDFCollectionWriter.collection()
                .idList(idList)
                .collectionPredicate(collectionPredicate)
                .resourceContainerIRI(resourceContainerIRI.toString())
                .build();

        String collection = collectionWriter.render();
        MetadataTemplate metadataTemplate;
        List<CollectionScope.Prefix> prefixes = Arrays.asList(
                new CollectionScope.Prefix(FedoraPrefixes.RDFS),
                new CollectionScope.Prefix(FedoraPrefixes.MODE));

        CollectionScope scope = new CollectionScope()
                .fedoraPrefixes(prefixes)
                .sequenceGraph(collection);

        metadataTemplate = MetadataTemplate.template()
                .template("template/sparql-update-seq.mustache")
                .scope(scope)
                .throwExceptionOnFailure()
                .build();

        String metadata = unescapeXml(metadataTemplate.render());
        return IOUtils.toInputStream(metadata, UTF_8);
    }
}