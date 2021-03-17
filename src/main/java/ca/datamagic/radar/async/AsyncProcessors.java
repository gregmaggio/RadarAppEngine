/**
 * 
 */
package ca.datamagic.radar.async;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Greg
 *
 */
public class AsyncProcessors {
	private static final List<AsyncProcessor> processors = new ArrayList<AsyncProcessor>();
	
	public static synchronized void add(AsyncProcessor processor) {
		processors.add(processor);
	}
	
	public static synchronized void remove(AsyncProcessor processor) {
		processors.remove(processor);
	}
	
	public static synchronized List<AsyncProcessor> getProcessors() {
		return processors;
	}
}
