/**
 * 
 */
package ca.datamagic.radar.async;

import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

import ca.datamagic.radar.dao.RadarImageDAO;
import ca.datamagic.radar.dao.SourceImagesDAO;

/**
 * @author Greg
 *
 */
public class SaveProcessor extends AsyncProcessor {
	private static final Logger logger = Logger.getLogger(SaveProcessor.class.getName());
	private String radar = null;
	
	public SaveProcessor(String radar) {
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
			SourceImagesDAO sourceImagesDAO = new SourceImagesDAO();
			RadarImageDAO radarImageDAO = new RadarImageDAO();
			logger.info("radar: " + this.radar);
			List<String> sourceImages = sourceImagesDAO.loadFiles(this.radar);
			logger.info("sourceImages: " + sourceImages.size());
			int imagesProcessed = 0;
			for (int ii = 0; ii < sourceImages.size(); ii++) {
				if (this.stopped) {
					break;
				}
				String sourceImage = sourceImages.get(ii);
				try {
					radarImageDAO.save(radar, sourceImage);
					imagesProcessed++;
				} catch (Throwable t) {
					logger.warning("Exception saving image " + sourceImage + ". Exception: " + t.getMessage());
				}
				this.percentComplete = (double)ii / (double)sourceImages.size() * 100.0;
				long runningTime = System.currentTimeMillis() - startTime;
				Duration duration = Duration.ofMillis(runningTime);
				this.runningTime = duration.toString();
			}
			logger.info("imagesProcessed: " + imagesProcessed);
		} catch (Throwable t) {
			logger.severe("Throwable: " + t.getMessage());
		}
		this.running = false;
		this.started = false;
		this.stopped = false;
		this.setEndTime();
	}
}
