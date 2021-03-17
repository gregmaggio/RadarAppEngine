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
public class SaveImageCountProcessorTester extends BaseTester {
	@Test
	public void test1() throws Exception {
		SaveImageCountProcessor processor = new SaveImageCountProcessor();
		processor.run();
	}
}
