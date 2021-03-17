/**
 * 
 */
package ca.datamagic.radar.dom;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ca.datamagic.radar.dto.RadarSiteDTO;
import ca.datamagic.radar.util.XmlUtils;

/**
 * @author Greg
 *
 */
public class RadarSiteHandler {
	private List<RadarSiteDTO> sites = null;

    public List<RadarSiteDTO> parse(String xml) throws Exception {
        return parse(new ByteArrayInputStream(xml.getBytes()));
    }

    public List<RadarSiteDTO> parse(InputStream inputStream) throws Exception {
        this.sites = new ArrayList<RadarSiteDTO>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(false);
        factory.setIgnoringComments(true);

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(inputStream);

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        xPath.setNamespaceContext(new NamespaceResolver(document));
        XPathExpression radarSiteExpression = xPath.compile("//" + RadarSiteDTO.NodeName);
        NodeList radarSiteNodeList = (NodeList)radarSiteExpression.evaluate(document, XPathConstants.NODESET);
        for (int ii = 0; ii < radarSiteNodeList.getLength(); ii++) {
            Element radarSiteElement = (Element)radarSiteNodeList.item(ii);
            String radarId = findChildElementValue(radarSiteElement, RadarSiteDTO.RadarIdNode);
            String wfoId = findChildElementValue(radarSiteElement, RadarSiteDTO.WfoIdNode);
            String name = findChildElementValue(radarSiteElement, RadarSiteDTO.NameNode);
            String latitude = findChildElementValue(radarSiteElement, RadarSiteDTO.LatNode);
            String longitude = findChildElementValue(radarSiteElement, RadarSiteDTO.LonNode);
            String elevation = findChildElementValue(radarSiteElement, RadarSiteDTO.ElevationNode);
            RadarSiteDTO dto = new RadarSiteDTO();
            dto.setRadarId(radarId);
            dto.setWfoId(wfoId);
            dto.setName(name);
            if ((latitude != null) && (latitude.length() > 0)) {
            	dto.setLatitude(new Double(latitude));
            }
            if ((longitude != null) && (longitude.length() > 0)) {
            	dto.setLongitude(new Double(longitude));
            }
            if ((elevation != null) && (elevation.length() > 0)) {
            	dto.setElevation(new Double(elevation));
            }
            if ((radarId != null) && (radarId.length() > 0) &&
            	(wfoId != null) && (wfoId.length() > 0)) {
            	this.sites.add(dto);
            }
        }
        return this.sites;
    }

    private static String findChildElementValue(Element element, String nodeName) {
        if (element.hasChildNodes()) {
            for (int ii = 0; ii < element.getChildNodes().getLength(); ii++) {
                if (element.getChildNodes().item(ii).getNodeName().compareToIgnoreCase(nodeName) == 0) {
                    return XmlUtils.getText(element.getChildNodes().item(ii));
                }
            }
        }
        return null;
    }

    public class NamespaceResolver implements NamespaceContext {
        //Store the source document to search the namespaces
        private Document sourceDocument;

        public NamespaceResolver(Document sourceDocument) {
            this.sourceDocument = sourceDocument;
        }

        //The lookup for the namespace uris is delegated to the stored document.
        public String getNamespaceURI(String prefix) {
            if (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
                return this.sourceDocument.lookupNamespaceURI(null);
            } else {
                return this.sourceDocument.lookupNamespaceURI(prefix);
            }
        }

        public String getPrefix(String namespaceURI) {
            return this.sourceDocument.lookupPrefix(namespaceURI);
        }

        @SuppressWarnings("rawtypes")
        public Iterator getPrefixes(String namespaceURI) {
            return null;
        }
    }
}
