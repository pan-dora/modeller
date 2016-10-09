package org.blume.modeller.ui.handlers.text;

import org.apache.commons.lang.StringUtils;
import org.blume.modeller.*;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.ui.Progress;
import org.blume.modeller.ui.jpanel.base.BagView;
import org.blume.modeller.ui.jpanel.text.CreateWordsFrame;
import org.blume.modeller.ui.util.ApplicationContextUtil;
import org.blume.modeller.ui.util.URIResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;
import static org.blume.modeller.DocManifestBuilder.getWordIdList;

public class CreateWordsHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(CreateWordsHandler.class);
    private static final long serialVersionUID = 1L;
    private BagView bagView;

    public CreateWordsHandler(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(ActionEvent e) { openCreateWordsFrame(); }

    @Override
    public void execute() {
        String message = ApplicationContextUtil.getMessage("bag.message.wordcreated");
        DefaultBag bag = bagView.getBag();
        Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        ModellerClient client = new ModellerClient();
        String url = bag.gethOCRResource();
        List<String> wordIdList = null;
        try {
            hOCRData hocr = DocManifestBuilder.gethOCRProjectionFromURL(url);
            wordIdList = getWordIdList(hocr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert wordIdList != null;
        for (String resourceID : wordIdList) {
            resourceID = StringUtils.substringAfter(resourceID,"_");
            URI wordObjectURI = getWordObjectURI(map, resourceID);
            try {
                client.doPut(wordObjectURI);
                ApplicationContextUtil.addConsoleMessage(message + " " + wordObjectURI);
            } catch (ModellerClientFailedException e) {
                    ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
        bagView.getControl().invalidate();
    }

    void openCreateWordsFrame() {
        DefaultBag bag = bagView.getBag();
        CreateWordsFrame createWordsFrame = new CreateWordsFrame(bagView, bagView.getPropertyMessage("bag.frame.words"));
        createWordsFrame.setBag(bag);
        createWordsFrame.setVisible(true);
    }

    public URI getWordContainerURI(Map<String, BagInfoField> map) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(ProfileOptions.TEXT_WORD_CONTAINER_KEY)
                    .pathType(4)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    private URI getWordObjectURI(Map<String, BagInfoField> map, String resourceID) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(ProfileOptions.TEXT_WORD_CONTAINER_KEY)
                    .resource(resourceID)
                    .pathType(5)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }
}