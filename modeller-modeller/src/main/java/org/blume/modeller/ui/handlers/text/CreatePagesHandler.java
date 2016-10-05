package org.blume.modeller.ui.handlers.text;

import org.apache.commons.lang.StringUtils;
import org.blume.modeller.*;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.ui.Progress;
import org.blume.modeller.ui.handlers.base.SaveBagHandler;
import org.blume.modeller.ui.jpanel.BagView;
import org.blume.modeller.ui.jpanel.CreatePagesFrame;
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
import static org.blume.modeller.DocManifestBuilder.getPageIdList;

public class CreatePagesHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(SaveBagHandler.class);
    private static final long serialVersionUID = 1L;
    private BagView bagView;

    public CreatePagesHandler(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(ActionEvent e) { openCreatePagesFrame(); }

    @Override
    public void execute() {
        String message = ApplicationContextUtil.getMessage("bag.message.pagecreated");
        DefaultBag bag = bagView.getBag();
        Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        ModellerClient client = new ModellerClient();
        String url = bag.gethOCRResource();
        List<String> pageIdList = null;
        try {
            hOCRData hocr = DocManifestBuilder.gethOCRProjectionFromURL(url);
            pageIdList = getPageIdList(hocr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert pageIdList != null;
        for (String resourceID : pageIdList) {
            resourceID = StringUtils.substringAfter(resourceID,"_");
            URI pageObjectURI = getPageObjectURI(map, resourceID);
            try {
                client.doPut(pageObjectURI);
                ApplicationContextUtil.addConsoleMessage(message + " " + pageObjectURI);
            } catch (ModellerClientFailedException e) {
                    ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
        bagView.getControl().invalidate();
    }

    void openCreatePagesFrame() {
        DefaultBag bag = bagView.getBag();
        CreatePagesFrame createPagesFrame = new CreatePagesFrame(bagView, bagView.getPropertyMessage("bag.frame.pages"));
        createPagesFrame.setBag(bag);
        createPagesFrame.setVisible(true);
    }

    public URI getPageContainerURI(Map<String, BagInfoField> map) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(ProfileOptions.TEXT_PAGE_CONTAINER_KEY)
                    .pathType(4)
                    .build();
            return uriResolver.render();
        } catch (URISyntaxException e) {
            log.debug(e.getMessage());
        }
        return null;
    }

    private URI getPageObjectURI(Map<String, BagInfoField> map, String resourceID) {
        URIResolver uriResolver;
        try {
            uriResolver = URIResolver.resolve()
                    .map(map)
                    .containerKey(ProfileOptions.TEXT_PAGE_CONTAINER_KEY)
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