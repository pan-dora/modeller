package org.blume.modeller.ui.handlers.text;

import org.apache.commons.lang.StringUtils;
import org.blume.modeller.*;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.ui.Progress;
import org.blume.modeller.ui.handlers.common.TextObjectURI;
import org.blume.modeller.ui.jpanel.base.BagView;
import org.blume.modeller.ui.jpanel.text.CreateLinesFrame;
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
import static org.blume.modeller.DocManifestBuilder.getLineIdList;

public class CreateLinesHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(CreateLinesHandler.class);
    private static final long serialVersionUID = 1L;
    private BagView bagView;

    public CreateLinesHandler(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(ActionEvent e) { openCreateLinesFrame(); }

    @Override
    public void execute() {
        String message = ApplicationContextUtil.getMessage("bag.message.linecreated");
        DefaultBag bag = bagView.getBag();
        Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        ModellerClient client = new ModellerClient();
        String url = bag.gethOCRResource();
        List<String> lineIdList = null;
        try {
            hOCRData hocr = DocManifestBuilder.gethOCRProjectionFromURL(url);
            lineIdList = getLineIdList(hocr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert lineIdList != null;
        for (String resourceID : lineIdList) {
            resourceID = StringUtils.substringAfter(resourceID,"_");
            URI lineObjectURI = TextObjectURI.getLineObjectURI(map, resourceID);
            try {
                client.doPut(lineObjectURI);
                ApplicationContextUtil.addConsoleMessage(message + " " + lineObjectURI);
            } catch (ModellerClientFailedException e) {
                    ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
        bagView.getControl().invalidate();
    }

    void openCreateLinesFrame() {
        DefaultBag bag = bagView.getBag();
        CreateLinesFrame createLinesFrame = new CreateLinesFrame(bagView, bagView.getPropertyMessage("bag.frame.lines"));
        createLinesFrame.setBag(bag);
        createLinesFrame.setVisible(true);
    }
}