package org.blume.modeller.ui.handlers.text;

import org.apache.commons.lang.StringUtils;
import org.blume.modeller.*;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.ui.Progress;
import org.blume.modeller.ui.handlers.common.TextObjectURI;
import org.blume.modeller.ui.jpanel.base.BagView;
import org.blume.modeller.ui.jpanel.text.CreateAreasFrame;
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
import static org.blume.modeller.DocManifestBuilder.getAreaIdList;

public class CreateAreasHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(CreateAreasHandler.class);
    private static final long serialVersionUID = 1L;
    private BagView bagView;

    public CreateAreasHandler(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(ActionEvent e) { openCreateAreasFrame(); }

    @Override
    public void execute() {
        String message = ApplicationContextUtil.getMessage("bag.message.areacreated");
        DefaultBag bag = bagView.getBag();
        Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        ModellerClient client = new ModellerClient();
        String url = bag.gethOCRResource();
        List<String> areaIdList = null;
        try {
            hOCRData hocr = DocManifestBuilder.gethOCRProjectionFromURL(url);
            areaIdList = getAreaIdList(hocr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert areaIdList != null;
        for (String resourceID : areaIdList) {
            resourceID = StringUtils.substringAfter(resourceID,"_");
            URI areaObjectURI = TextObjectURI.getAreaObjectURI(map, resourceID);
            try {
                client.doPut(areaObjectURI);
                ApplicationContextUtil.addConsoleMessage(message + " " + areaObjectURI);
            } catch (ModellerClientFailedException e) {
                    ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
        bagView.getControl().invalidate();
    }

    void openCreateAreasFrame() {
        DefaultBag bag = bagView.getBag();
        CreateAreasFrame createAreasFrame = new CreateAreasFrame(bagView, bagView.getPropertyMessage("bag.frame.areas"));
        createAreasFrame.setBag(bag);
        createAreasFrame.setVisible(true);
    }
}