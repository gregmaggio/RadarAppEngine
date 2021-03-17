/**
 * 
 */
package ca.datamagic.radar.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Greg
 *
 */
public abstract class BaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String origin = request.getHeader("Origin");
		addCorsHeaders(origin, response);
		response.setHeader("Content-Length", "0");
		response.setHeader("Content-Type", "text/plain");
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	protected static void addCorsHeaders(String origin, HttpServletResponse response) {
		if ((origin != null) && (origin.length() > 0)) {
			origin = origin.toLowerCase();
			if ((origin.indexOf("localhost") > -1) || (origin.compareToIgnoreCase("https://api-project-378578942759.ue.r.appspot.com") == 0)) {
				response.setHeader("Access-Control-Allow-Origin", origin);
				response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
				response.setHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization, X-Request-With, location");
				response.setHeader("Access-Control-Allow-Credentials", "true");
			}
		}
	}
}
