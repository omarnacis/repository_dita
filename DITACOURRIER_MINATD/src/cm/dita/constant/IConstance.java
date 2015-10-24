/**
 * 
 */
package cm.dita.constant;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author mbe
 *
 */

public interface IConstance {

	public String PARAMETER_VERSION_NAME = "VERSION";
	public String PARAMETER_DATE_FORMAT="dd-MM-yyyy HH:mm:ss";
	public String PARAMETER_DATETIME_FORMAT_SQL="yyyy-MM-dd HH:mm:ss";
	public String PARAMETER_DATE_FORMAT_SQL="yyyy-MM-dd";
	public String PARAMETER_DATE_FORMAT_2="EEEE, dd-MM-yyyy HH:mm:ss";
	public String PARAMETER_DATE_FORMAT_3="EEEE, dd-MM-yyyy";
	
	public String FIELD_DELETE ="delate";
	public String FIELD_SORT ="dateUseToSortData";
	
	public String MOT_POUR_CRYPTER ="";
	


	
// content type des diff�rents type de fichiers 
	public String CONTENT_EXCEL_TYPE_FILE = "application/vnd.ms-excel";
	public String CONTENT_CSV_TYPE_FILE = "application/CSV"; 
	public String CONTENT_PDF_TYPE_FILE = "application/pdf";
	
	//commandes pour les diff�rentes extension de fichier
	public String EXTENTION_EXCEL_FILE = ".xls";
	public String EXTENTION_PDF_FILE = ".pdf";
	public String EXTENTION_CSV_FILE = ".csv";
	
	// les diff�rentes types de fichier
	public String EXCEL_TYPE_FILE = "Excel";
	public String CSV_TYPE_FILE = "Csv";
	public String PDF_TYPE_FILE = "pdffile";
	
	//dossier temporaire
	public String TEMP_FOLDER = "TMP/";
	
	public String LIST_LOGS_FILE_NAME =  "LIST_LOGS";
	
	public String[] FORMAT_DATE={"EEEE, dd-MM-yyyy","EEEE, dd-MM-yyyy HH:mm:ss","yyyy-MM-dd","yyyy-MM-dd HH:mm:ss","dd-MM-yyyy HH:mm:ss",
			 "EEEE, dd MMM yyyy HH:mm:ss"};
//	#######################pagination des tableaux######################################
	
	public Integer[] LIST_PAGINATOR = new Integer[]{10,20,40,80};
	
	
//	########################### REPORTING ##########################
	public String PRINT_BORDERAUX = "/REPORTING/bordereau.jrxml";
	public String PRINT_IMAGE_ENTETE = "/REPORTING/coffee.jpg";
	public String EXPORT_FOLDER = "/TMP/";
	
	
		
}
