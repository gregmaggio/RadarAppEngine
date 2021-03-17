/**
 * 
 */
package ca.datamagic.radar.servlet;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import ca.datamagic.radar.dao.RadarImageDAO;
import ca.datamagic.radar.dao.RadarSitesDAO;
import ca.datamagic.radar.dao.SourceImagesDAO;
import ca.datamagic.radar.dto.RadarSiteDTO;

/**
 * @author Greg
 *
 */
public class ServiceAPIServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ServiceAPIServlet.class.getName());
	private static final Pattern sitesPattern = Pattern.compile("/sites", Pattern.CASE_INSENSITIVE);
	private static final Pattern sourceImagesPattern = Pattern.compile("/sourceImages/(?<radar>\\w+)", Pattern.CASE_INSENSITIVE);	
	private static final Pattern listPattern = Pattern.compile("/list/(?<radar>\\w+)", Pattern.CASE_INSENSITIVE);
	private static final Pattern loadPattern = Pattern.compile("/load/(?<radar>\\w+)/(?<file>(\\w|\\x5F|\\d|\\x2E)+)", Pattern.CASE_INSENSITIVE);
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String origin = request.getHeader("Origin");
			logger.info("origin: " + origin);
			String pathInfo = request.getPathInfo();
			logger.info("pathInfo: " + pathInfo);			
			Matcher sitesMatcher = sitesPattern.matcher(pathInfo);
			if (sitesMatcher.matches()) {
				logger.info("sites");
				RadarSitesDAO dao = new RadarSitesDAO();
				List<RadarSiteDTO> sites = dao.load();
				String accept = request.getHeader("accept");
				addCorsHeaders(origin, response);
				if ((accept != null) && (accept.compareToIgnoreCase("text/csv") == 0)) {
					StringBuilder builder = new StringBuilder();
					builder.append("radarId,wfoId,name,latitude,longitude,elevation");			    	
			    	for (int ii = 0; ii < sites.size(); ii++) {
			    		builder.append("\r\n");
			    		builder.append(sites.get(ii).toString());
			    	}
			    	response.setContentType("text/csv");
					response.getWriter().println(builder.toString());
				} else {
					Gson gson = new Gson();
					String json = gson.toJson(sites);
					response.setContentType("application/json");
					response.getWriter().println(json);
				}
				return;
			}
			Matcher sourceImagesMatcher = sourceImagesPattern.matcher(pathInfo);
			if (sourceImagesMatcher.matches()) {
				logger.info("sourceImages");
				String radar = sourceImagesMatcher.group("radar");
				logger.info("radar: " + radar);
				SourceImagesDAO dao = new SourceImagesDAO();
				List<String> files = dao.loadFiles(radar);
				Gson gson = new Gson();
				String json = gson.toJson(files);
				addCorsHeaders(origin, response);
				response.setContentType("application/json");
				response.getWriter().println(json);
				return;
			}
			Matcher listMatcher = listPattern.matcher(pathInfo);
			if (listMatcher.matches()) {
				logger.info("list");
				String radar = listMatcher.group("radar");
				logger.info("radar: " + radar);
				RadarImageDAO dao = new RadarImageDAO();
				List<String> files = dao.list(radar);
				Gson gson = new Gson();
				String json = gson.toJson(files);
				addCorsHeaders(origin, response);
				response.setContentType("application/json");
				response.getWriter().println(json);
				return;
			}
			Matcher loadMatcher = loadPattern.matcher(pathInfo);
			if (loadMatcher.matches()) {
				logger.info("load");
				String radar = loadMatcher.group("radar");
				String file = loadMatcher.group("file");
				logger.info("radar: " + radar);
				logger.info("file: " + file);
				RadarImageDAO dao = new RadarImageDAO();
				byte[] imageBytes = dao.load(radar, file);
				response.setContentType("image/png");
				response.getOutputStream().write(imageBytes);
				return;
			}
			response.sendError(403);
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "Throwable", t);
			throw new IOException("Exception", t);
		}
	}
}
