package cool.pandora.modeller.ui.handlers.text;

import org.apache.commons.lang.StringUtils;
import cool.pandora.modeller.ModellerClient;
import cool.pandora.modeller.ModellerClientFailedException;
import cool.pandora.modeller.DocManifestBuilder;
import cool.pandora.modeller.hOCRData;
import cool.pandora.modeller.bag.BagInfoField;
import cool.pandora.modeller.bag.impl.DefaultBag;
import cool.pandora.modeller.ui.Progress;
import cool.pandora.modeller.ui.handlers.common.TextObjectURI;
import cool.pandora.modeller.ui.jpanel.base.BagView;
import cool.pandora.modeller.ui.jpanel.text.CreateLinesFrame;
import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;
import static cool.pandora.modeller.DocManifestBuilder.getLineIdList;

/**
 * Create Lines Handler
 *
 * @author Christopher Johnson
 */
public class CreateLinesHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(CreateLinesHandler.class);
    private static final long serialVersionUID = 1L;
    private final BagView bagView;

    /**
     * @param bagView BagView
     */
    public CreateLinesHandler(final BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        openCreateLinesFrame();
    }

    @Override
    public void execute() {
        final String message = ApplicationContextUtil.getMessage("bag.message.linecreated");
        final DefaultBag bag = bagView.getBag();
        final Map<String, BagInfoField> map = bag.getInfo().getFieldMap();
        final String url = bag.gethOCRResource();
        List<String> lineIdList = null;
        try {
            final hOCRData hocr = DocManifestBuilder.gethOCRProjectionFromURL(url);
            lineIdList = getLineIdList(hocr);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        assert lineIdList != null;
        for (String resourceID : lineIdList) {
            resourceID = StringUtils.substringAfter(resourceID, "_");
            final URI lineObjectURI = TextObjectURI.getLineObjectURI(map, resourceID);
            try {
                ModellerClient.doPut(lineObjectURI);
                ApplicationContextUtil.addConsoleMessage(message + " " + lineObjectURI);
            } catch (final ModellerClientFailedException e) {
                ApplicationContextUtil.addConsoleMessage(getMessage(e));
            }
        }
        bagView.getControl().invalidate();
    }

    void openCreateLinesFrame() {
        final DefaultBag bag = bagView.getBag();
        final CreateLinesFrame createLinesFrame =
                new CreateLinesFrame(bagView, bagView.getPropertyMessage("bag.frame" + ".lines"));
        createLinesFrame.setBag(bag);
        createLinesFrame.setVisible(true);
    }
}