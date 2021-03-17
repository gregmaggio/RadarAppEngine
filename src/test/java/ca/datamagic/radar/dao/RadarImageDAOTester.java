package ca.datamagic.radar.dao;

import java.util.List;
import java.util.logging.Logger;

import org.junit.Test;

import com.google.gson.Gson;

import ca.datamagic.radar.testing.BaseTester;

public class RadarImageDAOTester extends BaseTester {
	private static final Logger logger = Logger.getLogger(RadarImageDAOTester.class.getName());
	
	@Test
	public void test1() throws Exception {
		/*
		String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64( 
           auth.getBytes(Charset.forName("US-ASCII")) );
        String authHeader = "Basic " + new String( encodedAuth );
        set( "Authorization", authHeader );
        */
		RadarImageDAO dao = new RadarImageDAO();
		//ca-datamagic-radar/KAKQ
		dao.clean("KAMX");
		//logger.info("AccessToken: " + dao.getCredentials().getAccessToken().getTokenValue());
		//dao.save("KABR", "https://mrms.ncep.noaa.gov/data/RIDGEII/L3/KABR/CREF/KABR_L3_CREF_20210104_133200.tif.gz");
		//dao.save("KABR", "https://mrms.ncep.noaa.gov/data/RIDGEII/L3/KABR/CREF/KABR_L3_CREF_20210104_133200.tif.gz");
		//dao.save("KABR", "https://mrms.ncep.noaa.gov/data/RIDGEII/L3/KABR/CREF/KABR_L3_CREF_20210109_145120.tif.gz");
		//List<String> files = dao.list("KABR");
		//Gson gson = new Gson();
		//logger.info("files: " + gson.toJson(files));
	}
}
