/**
 * 
 */
package cm.core.csv.utils;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author guy.peupie
 *
 */
public interface ICsvFileReader {

	/**
	 * 
	 * @return recup�re le ficher sur disque
	 */
	File getFile();
	
	/**
	 * 
	 * @return la liste des lignes du fichier csv
	 */
	List<String[] > getData();
	
	/**
	 * 
	 * @return la liste des titres
	 */
	String[] getTitles();
	
	/**
	 * recup�re les donn�es sous forme d'une map
	 * @return
	 */
	List<Map<String,String>> getMappedData();
}
