/**
 * 
 */
package ca.datamagic.radar.util;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Greg
 *
 */
public final class XmlUtils {
	public static final String getText(Node node) {
        if (node.hasChildNodes()) {
            NodeList childNodes = node.getChildNodes();
            if (childNodes.getLength() > 0) {
                Node child = childNodes.item(0);
                if ((child.getNodeType() == Node.CDATA_SECTION_NODE) ||
                    (child.getNodeType() == Node.TEXT_NODE)) {
                    return child.getNodeValue();
                }
            }
        }
        return null;
    }
}
