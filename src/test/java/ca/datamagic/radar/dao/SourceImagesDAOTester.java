/**
 * 
 */
package ca.datamagic.radar.dao;

import java.util.List;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import ca.datamagic.radar.testing.BaseTester;

/**
 * @author Greg
 *
 */
public class SourceImagesDAOTester extends BaseTester {
	private static final Logger logger = Logger.getLogger(SourceImagesDAOTester.class.getName());
	
	@Test
	public void test1() throws Exception {
		SourceImagesDAO dao = new SourceImagesDAO();
		List<String> files = dao.loadFiles("KABR");
		Assert.assertNotNull(files);
		Gson gson = new Gson();
		logger.info("files: " + gson.toJson(files));
	}

}
