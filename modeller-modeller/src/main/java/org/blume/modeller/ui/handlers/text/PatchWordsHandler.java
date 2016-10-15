package org.blume.modeller.ui.handlers.text;

import org.apache.commons.io.IOUtils;
import org.blume.modeller.*;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.common.uri.FedoraPrefixes;
import org.blume.modeller.common.uri.IIIFPrefixes;
import org.blume.modeller.templates.MetadataTemplate;
import org.blume.modeller.templates.WordScope;
import org.blume.modeller.ui.Progress;
import org.blume.modeller.ui.handlers.common.IIIFObjectURI;
import org.blume.modeller.ui.handlers.common.NodeMap;
import org.blume.modeller.ui.handlers.common.TextObjectURI;
import org.blume.modeller.ui.jpanel.base.BagView;
import org.blume.modeller.ui.jpanel.text.PatchWordsFrame;
import org.blume.modeller.ui.util.ApplicationContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang.StringUtils.substringAfter;
import static org.apache.commons.lang.StringUtils.substringBefore;
import static org.apache.commons.lang3.StringEscapeUtils.unescapeXml;
import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;
import static org.blume.modeller.DocManifestBuilder.getPageIdList;
import static org.blume.modeller.DocManifestBuilder.getWordIdList;
import static org.blume.modeller.ui.handlers.common.NodeMap.getCanvasPageMap;
import static org.blume.modeller.ui.handlers.common.NodeMap.getPageIdMap;

public class PatchWordsHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(PatchWordsHandler.class);
    private static final long serialVersionUID = 1L;
    private BagView bagView;

    public PatchWordsHandler(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        openPatchWordsFrame();
    }

    @Override
    public void execute() {
        String message = ApplicationContextUtil.getMessage("bag.message.wordpatched");
        DefaultBag bag = bagView.getBag();
        Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        ModellerClient client = new ModellerClient();
        URI canvasContainerURI = IIIFObjectURI.getCanvasContainerURI(map);
        URI wordContainerURI = TextObjectURI.getWordContainerURI(map);
        String url = bag.gethOCRResource();

        List<String> pageIdList = null;
        List<String> wordIdList= null;
        Map<String, String> bboxmap = null;
        Map<String, String> charmap = null;
        Map<String, String> canvasPageMap;
        Map<String, String> pageIdMap;

        InputStream rdfBody;

        try {
            hOCRData hocr = DocManifestBuilder.gethOCRProjectionFromURL(url);
            pageIdList = getPageIdList(hocr);
            wordIdList = getWordIdList(hocr);
            bboxmap = NodeMap.getBBoxWordMap(hocr, wordIdList);
            charmap = NodeMap.getCharWordMap(hocr, wordIdList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        canvasPageMap = getCanvasPageMap(pageIdList, canvasContainerURI);
        pageIdMap = getPageIdMap(pageIdList);
        assert wordIdList != null;

        for (String wordId : wordIdList) {
            String subWordId = substringAfter(wordId, "_");
            URI wordObjectURI = TextObjectURI.getWordObjectURI(map, subWordId);
            assert canvasPageMap != null;
            String canvasURI = canvasPageMap.get(substringBefore(subWordId, "_"));
            assert pageIdMap != null;
            String bbox = bboxmap.get(wordId);
            String chars = charmap.get(wordId);
            String region = Region.region()
                    .bbox(bbox)
                    .build();
            String canvasRegionURI = CanvasRegionURI.regionuri()
                    .region(region)
                    .canvasURI(canvasURI)
                    .build();
            if (canvasRegionURI != null & chars !=null) {
                rdfBody = getWordMetadata(canvasRegionURI, wordContainerURI.toString(), chars);
                try {
                    client.doPatch(wordObjectURI, rdfBody);
                    ApplicationContextUtil.addConsoleMessage(message + " " + wordObjectURI);
                } catch (ModellerClientFailedException e) {
                    ApplicationContextUtil.addConsoleMessage(getMessage(e));
                }
            }
        }
        bagView.getControl().invalidate();
    }

    void openPatchWordsFrame() {
        DefaultBag bag = bagView.getBag();
        PatchWordsFrame patchWordsFrame = new PatchWordsFrame(bagView, bagView.getPropertyMessage("bag.frame.patch" +
                ".words"));
        patchWordsFrame.setBag(bag);
        patchWordsFrame.setVisible(true);
    }

    private InputStream getWordMetadata(String canvasRegionURI, String wordContainerURI, String chars) {
        MetadataTemplate metadataTemplate;
        List<WordScope .Prefix> prefixes = Arrays.asList(
                new WordScope.Prefix(FedoraPrefixes.RDFS),
                new WordScope.Prefix(FedoraPrefixes.MODE),
                new WordScope.Prefix(IIIFPrefixes.OA),
                new WordScope.Prefix(IIIFPrefixes.CNT),
                new WordScope.Prefix(IIIFPrefixes.SC),
                new WordScope.Prefix(IIIFPrefixes.DCTYPES));

        WordScope scope = new WordScope ()
                .fedoraPrefixes(prefixes)
                .canvasURI(canvasRegionURI)
                .resourceContainerURI(wordContainerURI)
                .chars(chars);

        metadataTemplate = MetadataTemplate.template()
                .template("template/sparql-update-word.mustache")
                .scope(scope)
                .throwExceptionOnFailure()
                .build();

        String metadata = unescapeXml(metadataTemplate.render());
        return IOUtils.toInputStream(metadata, UTF_8 );
    }
}