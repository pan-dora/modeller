package cool.pandora.modeller.ui.jpanel.base;

import cool.pandora.modeller.ui.util.ApplicationContextUtil;
import org.springframework.richclient.application.PageComponentContext;
import org.springframework.richclient.application.support.AbstractView;

import javax.swing.JPanel;
import javax.swing.JComponent;
import java.awt.BorderLayout;

/**
 * ProfilesView
 *
 * @author gov.loc
 */
public class ProfilesView extends AbstractView {

    @Override
    protected JComponent createControl() {
        final JPanel bagViewPanel = new JPanel(new BorderLayout(2, 2));
        final InfoFormsPane infoPanel = ApplicationContextUtil.getBagView().infoInputPane;
        bagViewPanel.add(infoPanel);
        return bagViewPanel;
    }

    @Override
    protected void registerLocalCommandExecutors(final PageComponentContext context) {
        context.register("startCommand", ApplicationContextUtil.getBagView().startExecutor);
        context.register("openCommand", ApplicationContextUtil.getBagView().openExecutor);
        context.register("createBagInPlaceCommand", ApplicationContextUtil.getBagView().createBagInPlaceExecutor);
        context.register("clearCommand", ApplicationContextUtil.getBagView().clearExecutor);
        context.register("validateCommand", ApplicationContextUtil.getBagView().validateExecutor);
        context.register("completeCommand", ApplicationContextUtil.getBagView().completeExecutor);
        context.register("addDataCommand", ApplicationContextUtil.getBagView().addDataExecutor);
        context.register("saveBagCommand", ApplicationContextUtil.getBagView().saveBagExecutor);
        context.register("saveBagAsCommand", ApplicationContextUtil.getBagView().saveBagAsExecutor);
    }
}
