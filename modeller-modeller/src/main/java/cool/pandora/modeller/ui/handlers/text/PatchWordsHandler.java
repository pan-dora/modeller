package cool.pandora.modeller.ui.handlers.text;

import org.apache.commons.io.IOUtils;
import cool.pandora.modeller.ModellerClient;
import cool.pandora.modeller.ModellerClientFailedException;
import cool.pandora.modeller.DocManifestBuilder;
import cool.pandora.modeller.hOCRData;
import cool.pandora.modeller.CanvasRegionURI;
import cool.pandora.modeller.Region;
import cool.pandora.modeller.bag.BagInfoField;
import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.common.uri.FedoraPrefixes;
import cool.pandora.modeller.common.uri.IIIFPrefixes;
import cool.pandora.modeller.templates.MetadataTemplate;
import cool.pandora.modeller.templates.WordScope;
import cool.pandora.modeller.ui.Progress;
import cool.pandora.modeller.ui.handlers.common.IIIFObjectURI;
import cool.pandora.modeller.ui.handlers.common.NodeMap;
import cool.pandora.modeller.ui.handlers.common.TextObjectURI;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.jpanel.text.PatchWordsFrame;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.AbstractAction;
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
import static cool.pandora.modeller.DocManifestBuilder.getPageIdList;
import static cool.pandora.modeller.DocManifestBuilder.getWordIdList;
import static cool.pandora.modeller.ui.handlers.common.NodeMap.getCanvasPageMap;

/**
 * Patch Words Handler
 *
 * @author Christopher Johnson
 */
public class PatchWordsHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(PatchWordsHandler.class);
    private static final long serialVersionUID = 1L;
    private final BagView bagView;

    /**
     * @param bagView BagView
     */
    public PatchWordsHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        openPatchWordsFrame();
    }

    @Override
    public void execute() {
        final String message = ApplicationContextUtil.getMessage("bag.message.wordpatched");
        final DefaultBag bag = bagView.getBag();
        final Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        final URI canvasContainerURI = IIIFObjectURI.getCanvasContainerURI(map);
        final URI wordContainerURI = TextObjectURI.getWordContainerURI(map);
        final String url = bag.gethOCRResource();

        List<String> pageIdList = null;
        List<String> wordIdList = null;
        Map<String, String> bboxmap = null;
        Map<String, String> charmap = null;
        final Map<String, String> canvasPageMap;

        InputStream rdfBody;

        try {
            final hOCRData hocr = DocManifestBuilder.gethOCRProjectionFromURL(url);
            pageIdList = getPageIdList(hocr);
            wordIdList = getWordIdList(hocr);
            bboxmap = NodeMap.getBBoxWordMap(hocr, wordIdList);
            charmap = NodeMap.getCharWordMap(hocr, wordIdList);
        } catch (final IOException e) {
            e.printStackTrace();
        }

        canvasPageMap = getCanvasPageMap(pageIdList, canvasContainerURI);

        if (wordIdList != null) {
            for (final String wordId : wordIdList) {
                final String subWordId = substringAfter(wordId, "_");
                final URI wordObjectURI = TextObjectURI.getWordObjectURI(map, subWordId);
                String canvasURI = null;
                if (canvasPageMap != null) {
                    canvasURI = canvasPageMap.get(substringBefore(subWordId, "_"));
                }
                final String bbox = bboxmap.get(wordId);
                final String chars = charmap.get(wordId);
                final String region = Region.region().bbox(bbox).build();
                final String canvasRegionURI = CanvasRegionURI.regionuri().region(region).canvasURI(canvasURI).build();
                if (canvasRegionURI != null & chars != null & wordContainerURI != null) {
                    rdfBody = getWordMetadata(canvasRegionURI, wordContainerURI.toString(), chars);
                    try {
                        ModellerClient.doPatch(wordObjectURI, rdfBody);
                        ApplicationContextUtil.addConsoleMessage(message + " " + wordObjectURI);
                    } catch (final ModellerClientFailedException e) {
                        ApplicationContextUtil.addConsoleMessage(getMessage(e));
                    }
                }
            }
        }
        bagView.getControl().invalidate();
    }

    void openPatchWordsFrame() {
        final DefaultBag bag = bagView.getBag();
        final PatchWordsFrame patchWordsFrame =
                new PatchWordsFrame(bagView, bagView.getPropertyMessage("bag.frame" + ".patch" + ".words"));
        patchWordsFrame.setBag(bag);
        patchWordsFrame.setVisible(true);
    }

    /**
     * @param canvasRegionURI  String
     * @param wordContainerURI String
     * @param chars            String
     * @return InputStream
     */
    private static InputStream getWordMetadata(final String canvasRegionURI, final String wordContainerURI,
                                               final String chars) {
        final MetadataTemplate metadataTemplate;
        final List<WordScope.Prefix> prefixes =
                Arrays.asList(new WordScope.Prefix(FedoraPrefixes.RDFS), new WordScope.Prefix(FedoraPrefixes.MODE),
                        new WordScope.Prefix(IIIFPrefixes.OA), new WordScope.Prefix(IIIFPrefixes.CNT),
                        new WordScope.Prefix(IIIFPrefixes.SC), new WordScope.Prefix(IIIFPrefixes.DCTYPES));

        final WordScope scope = new WordScope().fedoraPrefixes(prefixes).canvasURI(canvasRegionURI)
                .resourceContainerURI(wordContainerURI).chars(chars.replace("\"", "\\\""));

        metadataTemplate = MetadataTemplate.template().template("template/sparql-update-word.mustache").scope(scope)
                .throwExceptionOnFailure().build();

        final String metadata = unescapeXml(metadataTemplate.render());
        return IOUtils.toInputStream(metadata, UTF_8);
    }
}