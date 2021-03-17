/**
 * 
 */
package ca.datamagic.radar.async;

import java.time.Duration;
import java.util.logging.Logger;

import ca.datamagic.radar.dao.RadarImageDAO;

/**
 * @author Greg
 *
 */
public class CleanProcessor extends AsyncProcessor {
	private static final Logger logger = Logger.getLogger(CleanProcessor.class.getName());
	private String radar = null;
	
	public CleanProcessor(String radar) {
		super();
		this.radar = radar;
	}
	
	@Override
	public void run() {
		this.running = true;
		this.setStartTime();
		try {
			this.percentComplete = 0.0;
			long startTime = System.currentTimeMillis();
			RadarImageDAO dao = new RadarImageDAO();
			logger.info("radar: " + this.radar);
			int deleted = dao.clean(this.radar);
			logger.info("deleted: " + deleted);
			this.percentComplete = 100.0;
			long runningTime = System.currentTimeMillis() - startTime;
			Duration duration = Duration.ofMillis(runningTime);
			this.runningTime = duration.toString();
		} catch (Throwable t) {
			logger.severe("Throwable: " + t.getMessage());
		}
		this.running = false;
		this.started = false;
		this.stopped = false;
		this.setEndTime();
	}
}
