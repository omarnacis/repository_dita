/**
 * 
 */
package cm.core.csv.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author guy.peupie
 *
 */
public class CsvFileReaderImpl implements ICsvFileReader {

	public final static char DEFAULT_SEPARATOR = ',';
	private char separator = DEFAULT_SEPARATOR;
	private File file;
	private List<String> lines;
	private List<String[]> data;
	private String[] titles;
	private List<Map<String, String>> mappedData;
	public final static List<Character> AVAILABLE_SEPARATORS = Collections.unmodifiableList(new ArrayList<Character>( Arrays.asList(',', ';', '\t', '|')));
	
	
	public CsvFileReaderImpl(File file) {
		this(file, DEFAULT_SEPARATOR);		
	}
	
	//constructeur avec s�parateur sp�cifier
	public CsvFileReaderImpl(File file, char separator) {

		if(file == null) {
			throw new IllegalArgumentException("Le fichier file ne peut pas �tre null");
			}
			this.file =file;
			if(!isValidSeparator(separator)) {
			throw new IllegalArgumentException("Le s�parateur sp�cifi� n'est pas pris en charge.");
			}
			this.separator =separator;
		
		// Init
		init();
	}


	// new version ki ne prend en compte la ligne de titre et ki stocke les
	// donn�es dans une map
	private void init() {
		try {
			lines = CsvFileHelper.readFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		data = new ArrayList<String[]>(lines.size());
		String regex = new Character(separator).toString();
		boolean first = true;
		for (String line : lines) {
			// Suppression des espaces de fin de ligne
			line = line.trim();
			// On saute les lignes vides
			if (line.length() == 0) {
				continue;
			}
			// On saute les lignes de commentaire
			if (line.startsWith("#")) {
				continue;
			}
			String[] oneData = line.split(regex);
			if (first) {
//				for(String str: oneData){
//					str.trim();
//					str.replaceAll(" ", "");
//					System.out.println(" toto"+str);
//				}
				titles = oneData;
				first = false;
			} else {
				data.add(oneData);
			}
		}
		
		mapData();
	}

	private void mapData() {
		mappedData = new ArrayList<Map<String, String>>(data.size());
		final int titlesLength = titles.length;
		for (String[] oneData : data) {
			final Map<String, String> map = new HashMap<String, String>();
			for (int i = 0; i < titlesLength; i++) {
				final String key = titles[i];
				if(oneData.length >= i+1  ){
					final String value = oneData[i];
					map.put(key, value);
				}
			}
			mappedData.add(map);
		}
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public List<String> getLines() {
		return lines;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}

	public List<String[]> getData() {
		return data;
	}

	public void setData(List<String[]> data) {
		this.data = data;
	}

	

	public void setMappedData(List<Map<String, String>> mappedData) {
		this.mappedData = mappedData;
	}
	
	
	public char getSeparator() {
		return separator;
	}

	public void setSeparator(char separator) {
		this.separator = separator;
	}

	
	@Override
	public String[] getTitles() {
		return titles;
	}

	@Override
	public List<Map<String, String>> getMappedData() {		
		return mappedData;
	}

	private boolean isValidSeparator(char separator) {
		return AVAILABLE_SEPARATORS.contains(separator);
		}
}
