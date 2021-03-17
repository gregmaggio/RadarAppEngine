/**
 * 
 */
package ca.datamagic.radar.dto;

/**
 * @author Greg
 *
 */
public class ImageDTO {
	private Integer width = null;
    private Integer height = null;
    private int[] pixels = null;

    public Integer getWidth() {
        return this.width;
    }

    public Integer getHeight() {
        return this.height;
    }

    public int[] getPixels() {
        return this.pixels;
    }

    public void setWidth(Integer newVal) {
    	this.width = newVal;
    }

    public void setHeight(Integer newVal) {
    	this.height = newVal;
    }

    public void setPixels(int[] newVal) {
    	this.pixels = newVal;
    }
}
