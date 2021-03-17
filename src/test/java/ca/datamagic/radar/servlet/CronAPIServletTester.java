/**
 * 
 */
package ca.datamagic.radar.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import static org.mockito.Mockito.*;

import ca.datamagic.radar.testing.BaseTester;

/**
 * @author Greg
 *
 */
public class CronAPIServletTester extends BaseTester {

	@Test
	public void saveTest() throws Exception {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);		
		when(request.getPathInfo()).thenReturn("/save/KAKQ");
		CronAPIServlet servlet = new CronAPIServlet();
		servlet.doGet(request, response);
	}
	
	@Test
	public void saveAllTest() throws Exception {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);		
		when(request.getPathInfo()).thenReturn("/saveAll");
		CronAPIServlet servlet = new CronAPIServlet();
		servlet.doGet(request, response);
	}
}
