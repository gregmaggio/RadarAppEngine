package ca.datamagic.radar.dao;

import java.util.List;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import ca.datamagic.radar.dto.RadarSiteDTO;
import ca.datamagic.radar.testing.BaseTester;

public class RadarSitesDAOTester extends BaseTester {
	private static final Logger logger = Logger.getLogger(RadarSitesDAOTester.class.getName());
	
	@Test
	public void test1() throws Exception {
		RadarSitesDAO dao = new RadarSitesDAO();
		List<RadarSiteDTO> sites = dao.load();
		Assert.assertNotNull(sites);
		logger.info("sites: " + sites.size());
		Gson gson = new Gson();
		logger.info("sites: " + gson.toJson(sites));
	}
}
