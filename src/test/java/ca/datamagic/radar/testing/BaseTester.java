/**
 * 
 */
package ca.datamagic.radar.testing;

import org.junit.BeforeClass;

import ca.datamagic.radar.dao.BaseDAO;

/**
 * @author Greg
 *
 */
public abstract class BaseTester {
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		BaseDAO.setKeyFile("src/main/webapp/WEB-INF/appkey.json");
	}
}
