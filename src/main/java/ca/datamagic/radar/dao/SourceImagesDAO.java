/**
 * 
 */
package ca.datamagic.radar.dao;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ca.datamagic.radar.util.IOUtils;

/**
 * @author Greg
 *
 */
public class SourceImagesDAO extends BaseDAO {
	private static final Logger logger = Logger.getLogger(SourceImagesDAO.class.getName());
	private static final int DEFAULT_IMAGES = 15;
	
	public List<String> loadFiles(String radar) throws IOException {
		return loadFiles(radar, DEFAULT_IMAGES);
	}
	
	public List<String> loadFiles(String radar, int number) throws IOException {
		HttpsURLConnection connection = null;
		try {
			String urlSpec = MessageFormat.format("https://mrms.ncep.noaa.gov/data/RIDGEII/L3/{0}/CREF/", radar);
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
            Document document = Jsoup.parse(responseText);
            Elements anchors = document.getElementsByTag("a");
            List<String> files = new ArrayList<String>();
            if (anchors != null) {
            	for (int ii = anchors.size() - 1; ii > -1; ii--) {
            		Element anchor = anchors.get(ii);
            		if (anchor.hasAttr("href")) {
            			String href = anchor.attr("href");
            			if ((href != null) && (href.length() > 0)) {
            				if (href.contains(".tif.gz")) {
            					files.add(MessageFormat.format("{0}{1}", urlSpec, href));
            				}
            			}
            		}
            		if (files.size() >= number) {
            			break;
            		}
            	}
            }
            Collections.sort(files);
            return files;
		} finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
	}
}
