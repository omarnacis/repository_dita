/**
 * 
 */
package cm.core.csv.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author guy.peupie
 *
 */
public interface ICsvFileWriter {
	
	/**
	 * 
	 * @param mappedData
	 * @throws IOException
	 */
	void write(List<Map<String, String>>mappedData) throws IOException;
	
	/**
	 * 
	 * @param mappedData
	 * @param titles
	 * @throws IOException
	 */
	void write(List<Map<String, String>>mappedData, String[] titles) throws IOException;
}
