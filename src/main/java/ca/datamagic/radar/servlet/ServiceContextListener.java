/**
 * 
 */
package ca.datamagic.radar.servlet;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import ca.datamagic.radar.dao.BaseDAO;

/**
 * @author Greg
 *
 */
public class ServiceContextListener implements ServletContextListener {
	private static final Logger logger = Logger.getLogger(ServiceContextListener.class.getName());
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			String keyFile = sce.getServletContext().getInitParameter("keyFile");
			String realPath = sce.getServletContext().getRealPath("/");
			String keyPath = MessageFormat.format("{0}/WEB-INF/{1}", realPath, keyFile);
			BaseDAO.setKeyFile(keyPath);
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "Throwable", t);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}
}
