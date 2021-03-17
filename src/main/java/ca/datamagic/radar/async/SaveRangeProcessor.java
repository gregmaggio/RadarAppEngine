package ca.datamagic.radar.async;

import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;

import ca.datamagic.radar.dao.RadarImageDAO;
import ca.datamagic.radar.dao.RadarSitesDAO;
import ca.datamagic.radar.dao.SourceImagesDAO;
import ca.datamagic.radar.dto.RadarSiteDTO;
import ca.datamagic.radar.inject.DAOModule;

public class SaveRangeProcessor extends AsyncProcessor {
	private static final Logger logger = Logger.getLogger(SaveRangeProcessor.class.getName());
	private int offset = 0;
	private int limit = 0;
	
	public SaveRangeProcessor(int offset, int limit) {
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
			SourceImagesDAO sourceImagesDAO = new SourceImagesDAO();
			Injector injector = Guice.createInjector(new DAOModule());
			RadarImageDAO radarImageDAO = injector.getInstance(RadarImageDAO.class);
			List<RadarSiteDTO> sites = radarSitesDAO.load();
			for (int ii = this.offset, count = 0; (ii < sites.size()) && (count < this.limit); ii++, count++) {
				if (this.stopped) {
					break;
				}
				RadarSiteDTO site = sites.get(ii);
				String radar = site.getRadarId();				
				logger.info("radar: " + radar);
				List<String> sourceImages = sourceImagesDAO.loadFiles(radar);
				logger.info("sourceImages: " + sourceImages.size());
				if (this.stopped) {
					break;
				}
				int imagesProcessed = 0;
				for (int jj = 0; jj < sourceImages.size(); jj++) {
					if (this.stopped) {
						break;
					}
					String sourceImage = sourceImages.get(jj);
					try {
						radarImageDAO.save(radar, sourceImage);
						imagesProcessed++;
					} catch (Throwable t) {
						logger.warning("Exception saving image " + sourceImage + ". Exception: " + t.getMessage());
					}
				}
				logger.info("imagesProcessed: " + imagesProcessed);
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
