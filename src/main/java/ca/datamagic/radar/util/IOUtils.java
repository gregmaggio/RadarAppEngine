/**
 * 
 */
package ca.datamagic.radar.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

/**
 * @author Greg
 *
 */
public final class IOUtils {
	private static Logger logger = Logger.getLogger(IOUtils.class.getName());
    private static final int BUFFER_SIZE = 4096;
    
	public static String readEntireStream(InputStream inputStream) throws IOException {
        StringBuffer textBuffer = new StringBuffer();
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = 0;
        while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) > 0) {
            textBuffer.append(new String(buffer, 0, bytesRead));
        }
        return textBuffer.toString();
    }

    public static void closeQuietly(InputStream inputStream) {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (Throwable t) {
            logger.warning(t.getMessage());
        }
    }

    public static void closeQuietly(OutputStream outputStream) {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (Throwable t) {
            logger.warning(t.getMessage());
        }
    }
}
