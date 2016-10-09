package org.blume.modeller.ui.handlers.text;

import org.apache.commons.lang.StringUtils;
import org.blume.modeller.*;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.ui.Progress;
import org.blume.modeller.ui.handlers.common.TextObjectURI;
import org.blume.modeller.ui.jpanel.base.BagView;
import org.blume.modeller.ui.jpanel.text.CreatePagesFrame;
import org.blume.modeller.ui.util.ApplicationContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;
import static org.blume.modeller.DocManifestBuilder.getPageIdList;

public class CreatePagesHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(CreatePagesHandler.class);
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
            URI pageObjectURI = TextObjectURI.getPageObjectURI(map, resourceID);
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
}