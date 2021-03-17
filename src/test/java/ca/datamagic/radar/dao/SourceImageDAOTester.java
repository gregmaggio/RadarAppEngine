package ca.datamagic.radar.dao;

import java.io.FileOutputStream;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import ca.datamagic.radar.dto.ImageDTO;

public class SourceImageDAOTester {
	private static final Logger logger = Logger.getLogger(SourceImageDAOTester.class.getName());
	
	@Test
	public void test1() throws Exception {
		SourceImageDAO dao = new SourceImageDAO();
		ImageDTO image = dao.downloadUnzipConvert("https://mrms.ncep.noaa.gov/data/RIDGEII/L3/KABR/CREF/KABR_L3_CREF_20210104_133200.tif.gz");
		Assert.assertNotNull(image);
		Gson gson = new Gson();
		logger.info("image: " + gson.toJson(image));
		
		ImageConverterDAO converterDAO = new ImageConverterDAO();
		byte[] bytes = converterDAO.convertToPNG(image);
		FileOutputStream fileOutputStream = new FileOutputStream("C:/Temp/KABR_L3_CREF_20210104_133200.png");
		fileOutputStream.write(bytes);
		fileOutputStream.flush();
		fileOutputStream.close();		
	}

}
