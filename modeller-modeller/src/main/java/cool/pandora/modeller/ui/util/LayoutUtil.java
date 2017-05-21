package cool.pandora.modeller.ui.util;

import java.awt.GridBagConstraints;

/**
 * LayoutUtil
 *
 * @author loc.gov
 */
public class LayoutUtil {

    private LayoutUtil() {
    }

    /**
     * @param x      int
     * @param y      int
     * @param w      int
     * @param h      int
     * @param wx     int
     * @param wy     int
     * @param fill   int
     * @param anchor int
     */
    public static GridBagConstraints buildGridBagConstraints(final int x, final int y, final int w, final int h,
                                                             final int wx, final int wy, final int fill,
                                                             final int anchor) {
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x; // start cell in a row
        gbc.gridy = y; // start cell in a column
        gbc.gridwidth = w; // how many column does the control occupy in the row
        gbc.gridheight = h; // how many column does the control occupy in the
        // column
        gbc.weightx = wx; // relative horizontal size
        gbc.weighty = wy; // relative vertical size
        gbc.fill = fill; // the way how the control fills cells
        gbc.anchor = anchor; // alignment

        return gbc;
    }

}
