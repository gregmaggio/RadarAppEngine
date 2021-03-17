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

import ca.datamagic.radar.async.AsyncProcessor;
import ca.datamagic.radar.async.AsyncProcessors;
import ca.datamagic.radar.async.CleanProcessor;
import ca.datamagic.radar.async.CleanRangeProcessor;
import ca.datamagic.radar.async.SaveAllProcessor;
import ca.datamagic.radar.async.SaveProcessor;
import ca.datamagic.radar.async.SaveRangeProcessor;

/**
 * @author Greg
 *
 */
public class CronAPIServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(CronAPIServlet.class.getName());
	private static final Pattern cleanPattern = Pattern.compile("/clean/(?<radar>\\w+)", Pattern.CASE_INSENSITIVE);
	private static final Pattern cleanRangePattern = Pattern.compile("/clean/(?<offset>\\d+)/(?<limit>\\d+)", Pattern.CASE_INSENSITIVE);
	private static final Pattern savePattern = Pattern.compile("/save/(?<radar>\\w+)", Pattern.CASE_INSENSITIVE);
	private static final Pattern saveRangePattern = Pattern.compile("/save/(?<offset>\\d+)/(?<limit>\\d+)", Pattern.CASE_INSENSITIVE);
	private static final Pattern saveAllPattern = Pattern.compile("/saveAll", Pattern.CASE_INSENSITIVE);	
	private static final Pattern asyncPattern = Pattern.compile("/async", Pattern.CASE_INSENSITIVE);
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String origin = request.getHeader("Origin");
			logger.info("origin: " + origin);
			String pathInfo = request.getPathInfo();
			logger.info("pathInfo: " + pathInfo);			
			Matcher cleanMatcher = cleanPattern.matcher(pathInfo);
			if (cleanMatcher.matches()) {
				logger.info("clean");
				String radar = cleanMatcher.group("radar");
				logger.info("radar: " + radar);
				CleanProcessor async = new CleanProcessor(radar);
				async.start();
				addCorsHeaders(origin, response);
				response.setStatus(204);
				return;
			}
			Matcher cleanRangeMatcher = cleanRangePattern.matcher(pathInfo);
			if (cleanRangeMatcher.matches()) {
				logger.info("cleanRange");
				int offset = Integer.parseInt(cleanRangeMatcher.group("offset"));
				int limit = Integer.parseInt(cleanRangeMatcher.group("limit"));
				logger.info("offset: " + offset);
				logger.info("limit: " + limit);
				CleanRangeProcessor async = new CleanRangeProcessor(offset, limit);
				async.start();
				addCorsHeaders(origin, response);
				response.setStatus(204);
				return;
			}
			Matcher saveMatcher = savePattern.matcher(pathInfo);
			if (saveMatcher.matches()) {
				logger.info("save");
				String radar = saveMatcher.group("radar");
				logger.info("radar: " + radar);
				SaveProcessor async = new SaveProcessor(radar);
				async.start();
				addCorsHeaders(origin, response);
				response.setStatus(204);
				return;
			}
			Matcher saveRangeMatcher = saveRangePattern.matcher(pathInfo);
			if (saveRangeMatcher.matches()) {
				logger.info("saveRange");
				int offset = Integer.parseInt(saveRangeMatcher.group("offset"));
				int limit = Integer.parseInt(saveRangeMatcher.group("limit"));
				logger.info("offset: " + offset);
				logger.info("limit: " + limit);
				SaveRangeProcessor async = new SaveRangeProcessor(offset, limit);
				async.start();
				addCorsHeaders(origin, response);
				response.setStatus(204);
				return;
			}
			Matcher saveAllMatcher = saveAllPattern.matcher(pathInfo);
			if (saveAllMatcher.matches()) {
				logger.info("saveAll");
				SaveAllProcessor async = new SaveAllProcessor();
				async.start();
				addCorsHeaders(origin, response);
				response.setStatus(204);
				return;
			}
			Matcher asyncMatcher = asyncPattern.matcher(pathInfo);
			if (asyncMatcher.matches()) {
				logger.info("async");
				List<AsyncProcessor> processors = AsyncProcessors.getProcessors();
				Gson gson = new Gson();
				String json = gson.toJson(processors);
				addCorsHeaders(origin, response);
				response.setContentType("application/json");
				response.getWriter().println(json);
				return;
			}
			response.sendError(403);
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "Throwable", t);
			throw new IOException("Exception", t);
		}
	}
}
