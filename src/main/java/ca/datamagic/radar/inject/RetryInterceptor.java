/**
 * 
 */
package ca.datamagic.radar.inject;

import java.util.logging.Logger;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Greg
 *
 */
public class RetryInterceptor implements MethodInterceptor {
	private static final Logger logger = Logger.getLogger(RetryInterceptor.class.getName());
	private static final int MAX_TRIES = 5;
	private static final int RETRY_TIMEOUT = 250;
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		for (int ii = 0; ii < MAX_TRIES; ii++) {
			try {
				return invocation.proceed();
			} catch (Throwable t) {
				logger.warning("Method failed. Retrying. Error: " + t.getMessage());
				Thread.sleep(RETRY_TIMEOUT);
			}
		}
		return null;
	}

}
