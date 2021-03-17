/**
 * 
 */
package ca.datamagic.radar.dao;

/**
 * @author Greg
 *
 */
public abstract class BaseDAO {
	private static String keyFile = null;

	public static synchronized String getKeyFile() {
		return keyFile;
	}

	public static synchronized void setKeyFile(String keyFile) {
		BaseDAO.keyFile = keyFile;
	}
}
