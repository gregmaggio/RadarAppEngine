/**
 * 
 */
package ca.datamagic.radar.async;

import org.junit.Test;

import ca.datamagic.radar.testing.BaseTester;

/**
 * @author Greg
 *
 */
public class SaveProcessorTester extends BaseTester {

	@Test
	public void test1() throws Exception {
		SaveProcessor processor = new SaveProcessor("TDCA");
		processor.run();
	}
}
