package org.blume.modeller.ui.handlers.iiif;

import org.blume.modeller.ModellerClient;
import org.blume.modeller.bag.BagInfoField;
import org.blume.modeller.bag.impl.DefaultBag;
import org.blume.modeller.ui.Progress;
import org.blume.modeller.ui.handlers.base.SaveBagHandler;
import org.blume.modeller.ui.jpanel.BagView;
import org.blume.modeller.ui.jpanel.CreateSequencesFrame;
import org.blume.modeller.ui.util.ApplicationContextUtil;
import org.blume.modeller.ui.util.ContainerIRIResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.*;
import static org.blume.modeller.common.uri.FedoraResources.SEQPREFIX;

public class CreateSequencesHandler extends AbstractAction implements Progress {
    protected static final Logger log = LoggerFactory.getLogger(SaveBagHandler.class);
    private static final long serialVersionUID = 1L;
    private BagView bagView;

    public CreateSequencesHandler(BagView bagView) {
        super();
        this.bagView = bagView;
    }

    @Override
    public void actionPerformed(ActionEvent e) { openCreateSequencesFrame(); }

    @Override
    public void execute() {
        String message = ApplicationContextUtil.getMessage("bag.message.sequencecreated");
        DefaultBag bag = bagView.getBag();
        Map<String, BagInfoField> map = bag.getInfo().getFieldMap();

        ModellerClient client = new ModellerClient();
        String sequenceContainerURI = getSequenceContainerURI(map);
        String sequenceID = bag.getSequenceID();
        String sequenceObjectURI = getSequenceObjectURI(sequenceContainerURI, sequenceID );
        try {
            client.doPut(sequenceObjectURI);
        } finally {
            ApplicationContextUtil.addConsoleMessage(message + " " + sequenceObjectURI);
        }
        bagView.getControl().invalidate();
    }

    void openCreateSequencesFrame() {
        DefaultBag bag = bagView.getBag();
        CreateSequencesFrame createSequencesFrame = new CreateSequencesFrame(bagView, bagView.getPropertyMessage("bag.frame.sequence"));
        createSequencesFrame.setBag(bag);
        createSequencesFrame.setVisible(true);
    }

    private String getSequenceObjectURI(String SequenceContainerURI, String SequenceID) {
        return SequenceContainerURI + SEQPREFIX +
                SequenceID;
    }

    public String getSequenceContainerURI(Map<String, BagInfoField> map) {
        ContainerIRIResolver containerIRIResolver;
        containerIRIResolver = ContainerIRIResolver.resolve()
                .map(map)
                .baseURIKey("FedoraBaseURI")
                .collectionRootKey("CollectionRoot")
                .collectionKey("CollectionID")
                .objektIDKey("ObjektID")
                .containerKey("IIIFSequenceContainer")
                .build();
        return containerIRIResolver.render();
    }
}
