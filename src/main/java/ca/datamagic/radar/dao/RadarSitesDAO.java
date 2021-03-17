/**
 * 
 */
package ca.datamagic.radar.dao;

import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import ca.datamagic.radar.dom.RadarSiteHandler;
import ca.datamagic.radar.dto.RadarSiteDTO;
import ca.datamagic.radar.util.IOUtils;

/**
 * @author Greg
 *
 */
public class RadarSitesDAO extends BaseDAO {
	private static final Logger logger = Logger.getLogger(RadarSitesDAO.class.getName());
    private static final String urlSpec = "https://opengeo.ncep.noaa.gov/geoserver/nws/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=nws:radar_sites";
    
    public List<RadarSiteDTO> load() throws Exception {
        HttpsURLConnection connection = null;
        try {
            URL url = new URL(urlSpec);
            connection = (HttpsURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000);
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Cache-Control", "max-age=0");
            connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
            connection.setRequestProperty("User-Agent", "(datamagic.ca,gregorymaggio@gmail.com)");
            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            connection.setRequestProperty("Sec-Fetch-Site", "none");
            connection.setRequestProperty("Sec-Fetch-Mode", "navigate");
            connection.setRequestProperty("Sec-Fetch-User", "?1");
            connection.setRequestProperty("Sec-Fetch-Dest", "document");
            connection.connect();
            String responseText = IOUtils.readEntireStream(connection.getInputStream());
            logger.info("responseLength: " + responseText.length());
            logger.info("responseText: " + responseText);
            RadarSiteHandler handler = new RadarSiteHandler();
            return handler.parse(responseText);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
