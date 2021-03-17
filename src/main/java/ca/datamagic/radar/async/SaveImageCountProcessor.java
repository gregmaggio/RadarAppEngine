/**
 * 
 */
package ca.datamagic.radar.async;

import java.io.PrintWriter;
import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

import org.apache.geronimo.mail.util.StringBufferOutputStream;

import ca.datamagic.radar.dao.RadarImageDAO;
import ca.datamagic.radar.dao.RadarSitesDAO;
import ca.datamagic.radar.dto.RadarSiteDTO;

/**
 * @author Greg
 *
 */
public class SaveImageCountProcessor extends AsyncProcessor {
	private static final Logger logger = Logger.getLogger(SaveImageCountProcessor.class.getName());
	
	public SaveImageCountProcessor() {
		super();
	}
	
	@Override
	public void run() {
		this.running = true;
		this.setStartTime();
		try {
			this.percentComplete = 0.0;
			long startTime = System.currentTimeMillis();
			RadarSitesDAO radarSitesDAO = new RadarSitesDAO();
			RadarImageDAO radarImageDAO = new RadarImageDAO();
			List<RadarSiteDTO> sites = radarSitesDAO.load();
			StringBuffer buffer = new StringBuffer();
			StringBufferOutputStream output = new StringBufferOutputStream(buffer);
			PrintWriter writer = new PrintWriter(output);
			writer.println("radarId,imageCount");
			for (int ii = 0; ii < sites.size(); ii++) {
				if (this.stopped) {
					break;
				}
				RadarSiteDTO site = sites.get(ii);
				String radar = site.getRadarId();
				logger.info("radar: " + radar);
				List<String> files = radarImageDAO.list(radar);
				logger.info("files: " + files.size());
				writer.println(radar + "," + Integer.toString(files.size()));
				if (this.stopped) {
					break;
				}
				this.percentComplete = (double)(ii + 1) / (double)sites.size() * 100.0;
				long runningTime = System.currentTimeMillis() - startTime;
				Duration duration = Duration.ofMillis(runningTime);
				this.runningTime = duration.toString();
			}
			writer.flush();
			writer.close();
			radarImageDAO.saveImageCounts(buffer.toString());
		} catch (Throwable t) {
			logger.severe("Throwable: " + t.getMessage());
		}
		this.running = false;
		this.started = false;
		this.stopped = false;
		this.setEndTime();
	}
}
