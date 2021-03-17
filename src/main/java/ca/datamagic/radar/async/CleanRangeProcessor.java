/**
 * 
 */
package ca.datamagic.radar.async;

import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

import ca.datamagic.radar.dao.RadarImageDAO;
import ca.datamagic.radar.dao.RadarSitesDAO;
import ca.datamagic.radar.dto.RadarSiteDTO;

/**
 * @author Greg
 *
 */
public class CleanRangeProcessor extends AsyncProcessor {
	private static final Logger logger = Logger.getLogger(CleanRangeProcessor.class.getName());
	private int offset = 0;
	private int limit = 0;
	
	public CleanRangeProcessor(int offset, int limit) {
		super();
		this.offset = offset;
		this.limit = limit;
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
			for (int ii = this.offset, count = 0; (ii < sites.size()) && (count < this.limit); ii++, count++) {
				if (this.stopped) {
					break;
				}
				RadarSiteDTO site = sites.get(ii);
				String radar = site.getRadarId();
				logger.info("radar: " + radar);
				int deleted = radarImageDAO.clean(radar);
				logger.info("deleted: " + deleted);
				if (this.stopped) {
					break;
				}
				this.percentComplete = (double)count / (double)this.limit * 100.0;
				long runningTime = System.currentTimeMillis() - startTime;
				Duration duration = Duration.ofMillis(runningTime);
				this.runningTime = duration.toString();
			}
		} catch (Throwable t) {
			logger.severe("Throwable: " + t.getMessage());
		}
		this.running = false;
		this.started = false;
		this.stopped = false;
		this.setEndTime();
	}
}
