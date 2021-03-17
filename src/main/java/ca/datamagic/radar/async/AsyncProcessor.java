/**
 * 
 */
package ca.datamagic.radar.async;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * @author Greg
 *
 */
public abstract class AsyncProcessor implements Runnable {
	protected static SimpleDateFormat utcDateFormat = null;
	//yyyy-MM-dd'T'HH:mm:ssZ
	protected boolean running = false;
	protected boolean started = false;
	protected boolean stopped = false;
	protected double percentComplete = 0.0;
	protected String startTime = null;
	protected String endTime = null;
	protected String runningTime = null;
	
	static {
		utcDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		utcDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
	
	public AsyncProcessor() {
		AsyncProcessors.add(this);
	}
	
	protected void setStartTime() {		
		this.startTime = utcDateFormat.format(Calendar.getInstance().getTime());
	}
	
	protected void setEndTime() {		
		this.endTime = utcDateFormat.format(Calendar.getInstance().getTime());
	}
	
	public void start() {
		if (!this.running) {
			if (!this.started) {
				(new Thread(this)).start();
				this.started = true;
			}
		}
	}
	
	public void stop() {
		if (this.running) {
			this.stopped = true;
		}
	}
	
	public boolean isRunning() {
		return this.running;
	}

	public boolean isStarted() {
		return this.started;
	}

	public boolean isStopped() {
		return this.stopped;
	}

	public double percentComplete() {
		return this.percentComplete;
	}

	public String getStartTime() {
		return this.startTime;
	}
	
	public String getEndTime() {
		return this.endTime;
	}
	
	public String getRunningTime() {
		return this.runningTime;
	}
}
