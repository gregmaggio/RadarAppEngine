/**
 * 
 */
package ca.datamagic.radar.dto;

/**
 * @author Greg
 *
 */
public class RadarSiteDTO {
	public static final String NodeName = "nws:radar_sites";
    public static final String RadarIdNode = "nws:rda_id";
    public static final String WfoIdNode = "nws:wfo_id";
    public static final String NameNode = "nws:name";
    public static final String LatNode = "nws:lat";
    public static final String LonNode = "nws:lon";
    public static final String ElevationNode = "nws:eqp_elv";
    private String radarId = null;
    private String wfoId = null;
    private String name = null;
    private Double latitude = null;
    private Double longitude = null;
    private Double elevation = null;
    
    public String getRadarId() {
        return this.radarId;
    }

    public String getWfoId() {
        return this.wfoId;
    }

    public String getName() {
        return this.name;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public Double getElevation() {
        return this.elevation;
    }

    public void setRadarId(String newVal) {
        this.radarId = newVal;
    }

    public void setWfoId(String newVal) {
        this.wfoId = newVal;
    }

    public void setName(String newVal) {
        this.name = newVal;
    }

    public void setLatitude(Double newVal) {
        this.latitude = newVal;
    }

    public void setLongitude(Double newVal) {
        this.longitude = newVal;
    }

    public void setElevation(Double newVal) {
        this.elevation = newVal;
    }
    
    @Override
    public String toString() {
    	StringBuilder builder = new StringBuilder();
    	builder.append(((this.radarId != null) ? this.radarId : ""));
    	builder.append(",");
    	builder.append(((this.wfoId != null) ? this.wfoId : ""));
    	builder.append(",");
    	builder.append(((this.name != null) ? this.name : ""));
    	builder.append(",");
    	builder.append(((this.latitude != null) ? Double.toString(this.latitude) : ""));
    	builder.append(",");
    	builder.append(((this.longitude != null) ? Double.toString(this.longitude) : ""));
    	builder.append(",");
    	builder.append(((this.elevation != null) ? Double.toString(this.elevation) : ""));
    	return builder.toString();
    }
}
