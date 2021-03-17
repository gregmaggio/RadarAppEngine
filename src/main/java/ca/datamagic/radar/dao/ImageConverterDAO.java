/**
 * 
 */
package ca.datamagic.radar.dao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import ar.com.hjg.pngj.ImageInfo;
import ar.com.hjg.pngj.ImageLineHelper;
import ar.com.hjg.pngj.ImageLineInt;
import ar.com.hjg.pngj.PngWriter;
import ca.datamagic.radar.dto.ImageDTO;
import ca.datamagic.radar.util.ColorUtils;

/**
 * @author Greg
 *
 */
public class ImageConverterDAO extends BaseDAO {
	public byte[] convertToPNG(ImageDTO image) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		int width = image.getWidth();
		int height = image.getHeight();
		int[] allPixels = image.getPixels();
		ImageInfo imageInfo = new ImageInfo(width, height, 8, true);
		PngWriter pngWriter = new PngWriter(outputStream, imageInfo);
		for (int row = 0; row < height; row++) {
			ImageLineInt imageLine = new ImageLineInt(imageInfo);
			for (int col = 0; col < width; col++) {
				int red = ColorUtils.red(allPixels[row * width + col]);
				int green = ColorUtils.green(allPixels[row * width + col]);
				int blue = ColorUtils.blue(allPixels[row * width + col]);
				int alpha = ColorUtils.alpha(allPixels[row * width + col]);
				ImageLineHelper.setPixelRGBA8(imageLine, col, red, green, blue, alpha);
			}
			pngWriter.writeRow(imageLine);
		}
		pngWriter.end();
		outputStream.flush();
		outputStream.close();
		return outputStream.toByteArray();
	}
}
