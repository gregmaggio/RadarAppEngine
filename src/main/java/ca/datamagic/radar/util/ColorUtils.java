/**
 * 
 */
package ca.datamagic.radar.util;

/**
 * @author Greg
 *
 */
public final class ColorUtils {
	public static final int rgba(int red, int green, int blue, int alpha) {
		red = (red << 24 & 0xFF000000);
		green = (green << 16 & 0x00FF0000);
		blue = (blue << 8 & 0x0000FF00);
		alpha = (alpha & 0x000000FF);
		return (red | green | blue | alpha);
	}
	
	public static final int red(int pixel) {
		return (pixel >> 24 & 0xFF);
	}
	
	public static final int green(int pixel) {
		return (pixel >> 16 & 0xFF);
	}
	
	public static final int blue(int pixel) {
		return (pixel >> 8 & 0xFF);
	}
	
	public static final int alpha(int pixel) {
		return (pixel & 0xFF);
	}
}
