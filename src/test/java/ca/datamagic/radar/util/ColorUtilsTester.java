package ca.datamagic.radar.util;

import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;

public class ColorUtilsTester {
	private static final Logger logger = Logger.getLogger(ColorUtilsTester.class.getName());
	
	@Test
	public void test1() {
		int red = 45;
		int green = 23;
		int blue = 123;
		int alpha = 1;
		int pixel = ColorUtils.rgba(red, green, blue, alpha);
		logger.info("pixel: " + pixel);
		int red2 = ColorUtils.red(pixel);
		logger.info("red2: " + red2);
		int green2 = ColorUtils.green(pixel);
		logger.info("green2: " + green2);
		int blue2 = ColorUtils.blue(pixel);
		logger.info("blue2: " + blue2);
		int alpha2 = ColorUtils.alpha(pixel);
		logger.info("alpha2: " + alpha2);
		Assert.assertEquals(red, red2);
		Assert.assertEquals(green, green2);
		Assert.assertEquals(blue, blue2);
		Assert.assertEquals(alpha, alpha2);
	}

}
