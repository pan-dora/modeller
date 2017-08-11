/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cool.pandora.modeller.ui;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextPane;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BagTextPane.
 *
 * @author gov.loc
 */
public class BagTextPane extends JTextPane {
    private static final long serialVersionUID = -505900021814525136L;

    protected static final Logger log = LoggerFactory.getLogger(BagTextPane.class);

    private StyledDocument document;
    private String message = "";

    /**
     * BagTextPane.
     *
     * @param message String
     */
    BagTextPane(final String message) {
        super();

        this.message = message;
        this.buildDocument();
        this.setStyledDocument(document);
        this.setAutoscrolls(true);
        this.setEditable(false);
        final Color textBackground = new Color(240, 240, 240);
        this.setBackground(textBackground);
    }

    /**
     * setMessage.
     *
     * @param message String
     */
    public void setMessage(final String message) {
        this.message = message;
        this.buildDocument();
        this.setStyledDocument(document);
    }

    /**
     * getMessage.
     *
     * @return message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * buildDocument.
     */
    private void buildDocument() {
        final StyleContext context = new StyleContext();
        document = new DefaultStyledDocument(context);

        final Style style = context.getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setAlignment(style, StyleConstants.ALIGN_LEFT);
        StyleConstants.setFontSize(style, 14);
        StyleConstants.setSpaceAbove(style, 4);
        StyleConstants.setSpaceBelow(style, 4);
        // Insert content
        try {
            document.insertString(document.getLength(), message, style);
        } catch (final BadLocationException badLocationException) {
            log.error(badLocationException.getMessage());
        }

        final SimpleAttributeSet attributes = new SimpleAttributeSet();
        StyleConstants.setBold(attributes, true);
        StyleConstants.setItalic(attributes, true);

        // Third style for icon/component
        final Style labelStyle = context.getStyle(StyleContext.DEFAULT_STYLE);

        final Icon icon = new ImageIcon("Computer.gif");
        final JLabel label = new JLabel(icon);
        StyleConstants.setComponent(labelStyle, label);
    }
}