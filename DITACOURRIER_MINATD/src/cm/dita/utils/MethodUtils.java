/**
 * 
 */
package cm.dita.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;


/*import javax.annotation.sql.DataSourceDefinitions;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;*/
import javax.faces.model.SelectItem;
import javax.persistence.Transient;
//import javax.servlet.http.HttpServletRequest;












//import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
//import org.apache.commons.collections.CollectionUtils;
//import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;
//import org.springframework.security.authentication.encoding.Md5PasswordEncoder;





import cm.dita.entities.Bordereau;
import cm.dita.entities.EspaceCourrier;
import cm.dita.entities.user.User;
//import cm.dita.constant.IConstance;
//import cm.dita.entities.User;
import cm.dita.exception.ApplicationException;
import cm.dita.annotation.AsignedIdEntity;





/**
 * @author bertrand
 * 
 */
public class MethodUtils {

	private MethodUtils() {
		super();
	}

	/**
	 * Cette mï¿½thode doit ï¿½tre appelï¿½e avec une liste non nulle
	 * 
	 * @param listString
	 * @return
	 */
	public static String[] listToArray(List listString) {

		if (listString == null)
			return null;

		String[] arrayString = new String[listString.size()];
		for (int i = 0; i < listString.size(); i++) {
			arrayString[i] = new String((String) listString.get(i));
		}

		return arrayString;

	}

	/**
	 * 
	 * @param setString
	 * @return
	 */
	public static String[] convertSetToArray(Set<String> setString) {
	
		if (setString == null)
			return null;

		String[] arrayString = new String[setString.size()];

		Iterator<String> itSetString = setString.iterator();
		int i = 0;
		while (itSetString.hasNext()) {
			arrayString[i] = new String(itSetString.next());
			i = i + 1;
		}
		return arrayString;

	}

	/**
	 * 
	 * @param set
	 * @return
	 */
	public static List<String> convertSetToListString(Set<String> set) {

		if (set == null)
			return null;
		Iterator<String> itSet = set.iterator();
		List<String> listString = new ArrayList<String>();
		while (itSet.hasNext()) {
			listString.add(new String(itSet.next()));
		}

		return listString;

	}
	
	public static List<Object> convertSetToListObject(Set<Object> set) {

		if (set == null)
			return null;
		Iterator<Object> itSet = set.iterator();
		List<Object> listObject = new ArrayList<Object>();
		while (itSet.hasNext()) {
			listObject.add(itSet.next());
		}

		return listObject;

	}

	/***
	 * Retourne -1 si value n'est pas trouvï¿½e dans array
	 * 
	 * @param value
	 * @param array
	 * @return
	 */
	public static int findIndexStringInArray(String value, String[] array) {
		int index = -1;
		if (array == null || array.length == 0)
			return index;

		for (int i = 0; i < array.length; i++) {
			if (array[i].equalsIgnoreCase(value)) {
				index = i;
				break;

			}
		}
		return index;

	}

	public static boolean stringIsNullOrBlank(String str) {

		if (str == null || str.equalsIgnoreCase("") || str.equalsIgnoreCase("null"))
			return true;
		else
			return false;
	}

	public static String toUpperCaseFirstCaractar(String str) {

		String firstCarac = str.substring(0, 1);
		firstCarac = firstCarac.toUpperCase();
		return firstCarac.concat(str.substring(1));
	}

	/**
	 * injecter la valeur de la proprietï¿½ de l'annotation apposï¿½e sur un
	 * attribut ï¿½ l'attribut en question
	 * 
	 * @param pObjectInstance
	 */
	public static void initialise(Object pObjectInstance) {
		// On rï¿½cupï¿½re la classe de l'objet.
		Class<?> ClassInstance = pObjectInstance.getClass();

		// Pour tous les champs de l'objet :
		for (Field f : ClassInstance.getDeclaredFields()) {

			AsignedIdEntity annot = f.getAnnotation(AsignedIdEntity.class);

			if (annot != null) {
				// On recupï¿½re la valeur dans le Properties
				String value = annot.name();
				// Si la propriï¿½tï¿½ n'existe pas on met la valeur par defaut:
				if (value == null) {
					// value = annot.missing();
				}

				try {
					// Si on n'a pas accï¿½s aux champs, on force son
					// accessibilitï¿½
					// Cela permet de modifier les champs firendly, protected et
					// private :
					boolean pAccessible = f.isAccessible();
					if (!pAccessible)
						f.setAccessible(true);
					String idGenerate = value + "_" + getString() + ""
							+ System.currentTimeMillis();
					// System.out.println(" *************idGenerï¿½**************"+idGenerate);
					f.set(pObjectInstance, idGenerate);
				} catch (Exception lException) {
					// throw new PropertyManagerException(
					// "Impossible d'assigner le champ '" +
					// value + "'.", lException );
					lException.printStackTrace();
				}
			} // fin property!=null
		}
		
		try {
			Field fieldDateTime =  ReflectionUtils.getField(ClassInstance, "dateUseToSortData");			
			if(fieldDateTime != null){
				boolean pAccessible = fieldDateTime.isAccessible();
				if (!pAccessible)
					fieldDateTime.setAccessible(true);
				fieldDateTime.set(pObjectInstance, System.currentTimeMillis());
			}
		
		} catch (SecurityException e) {			
			e.printStackTrace();
		} catch (IllegalArgumentException e) {			
			e.printStackTrace();
		} catch (IllegalAccessException e) {			
			e.printStackTrace();
		}
		
	}
	
	
	public static boolean isDateOrByteType(Field field){
		if (((field.getType().getName().contains("Date")
				|| (field.getType().getName().contains("byte"))
				|| (field.getType().getName().contains("Byte"))))) {
			return true;
		}
		return false;
	}

	/**
	 * verifie si une propriï¿½tï¿½ est d'un type primitif
	 * 
	 * @param field
	 * @return
	 */
	public static boolean isSimpleType(Field field) {

		if ((isNumeric(field))
				|| (field.getType().getName().contains("boolean"))
				|| (field.getType().getName().contains("Boolean"))) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param field
	 * @return
	 */
	public static boolean isNumeric(Field field) {

		if ((field.getType().getName().contains("Long"))
				|| (field.getType().getName().contains("long"))
				|| (field.getType().getName().contains("int"))
				|| (field.getType().getName().contains("Integer"))
				|| (field.getType().getName().contains("Double"))
				|| (field.getType().getName().contains("double"))
				|| (field.getType().getName().contains("Float"))
				|| (field.getType().getName().contains("float"))
				|| (field.getType().getName().contains("BigDecimal"))) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param field
	 * @return
	 */
	public static boolean isNumericObject(Object field) {

		if ((field instanceof Long				
				|| (field instanceof Integer)
				|| (field instanceof Double)
				|| (field instanceof Float)
				|| (field instanceof BigDecimal))) {
			return true;
		}
		return false;
	}

	
	/**
	 * setter la valeur d'un field
	 * 
	 * @param object
	 * @param field
	 * @param value
	 */
	public static void setValeurOfField(Object object, Field field, Object value) {
		if (field == null || object == null) {
			return;
		}
		Object valeur = null;
		Method setter = getSetter(field, object.getClass());
		Object paramsObj[] = { value };
		if (setter != null) {
			try {
				valeur = setter.invoke(object, paramsObj); // execution de la
															// methode setter
															// d'un attribut de
															// classe
			} catch (Exception e) {
				// System.err.println(AbstractModelObject.class.getCanonicalName()+".setValeurOfField: exception : "+e.getMessage());
				e.printStackTrace();
			}
		}

	}

	/**
	 * retourne une map donc la clï¿½ est pï¿½ssï¿½e en paramï¿½tre
	 * 
	 * @param keyOfMapElement
	 * @param maps
	 * @return
	 */
	public static Map<String, Integer> getMapOnListMap(String keyOfMapElement,
			List<Map<String, Integer>> maps) {

		for (Map<String, Integer> map : maps) {
			if (map.containsKey(keyOfMapElement)) {
				return map;
			}
		}
		return null;
	}

	/**
	 * getter la valeur d'un field
	 * 
	 * @param field
	 * @param classObj
	 * @return
	 */
	private static Method getSetter(Field field, Class classObj) {
		String name = "set" + field.getName();
		for (Method method : classObj.getMethods()) {
			if (method.getName().equalsIgnoreCase(name)) {
				return method;
			}
		}

		for (Method method : classObj.getSuperclass().getMethods()) {
			if (method.getName().equalsIgnoreCase(name)) {
				return method;
			}
		}
		return null;
	}

	private static int index = 0;

	private static String getString() {
		String str = "ABCDEFGHIJKLMNOPQRSTUVW";
		char[] arrayStr = str.toCharArray();
		synchronized ((Object) index) {
			index++;
		}

		if (index < 23) {
			return arrayStr[index] + "" + index;
		} else {
			return arrayStr[index % 22] + "" + index % 22;
		}
	}

	
	
	
	//CALCUL DU "CMUP"
	
	public static BigDecimal calculCMUP(long last_qte,long new_qte,BigDecimal last_price,BigDecimal new_price){
		
		last_price=last_price.multiply(new BigDecimal(last_qte));
		new_price=new_price.multiply(new BigDecimal(new_qte));
		new_price=new_price.add(last_price);
		long qte = last_qte+new_qte;
		new_price=new_price.divide(new BigDecimal(qte));
		return new_price;
	}
	
	 //Compare le mot de passe saisi et celui de la session pour droit de suppresion
  
   
   /**
	 * sï¿½rialiser un object en un tableau de bytes
	 * @param object
	 * @return
	 * @throws TechnicalException
	 */
	public static byte[] serializeObjectToBytes (Serializable object) throws ApplicationException{
		File file = serializeObjectToFile(object);
		if (file!=null){
			byte[] result = encodeFileToBytes(file.getAbsolutePath());
			file.delete();
			return result;
		}
		return null;
	}

	/**
	 * dï¿½sï¿½rialiser un tableau de bytes, en un object JAVA. notez que ce tableau de bytes doit ï¿½ l'origine provenir de la sï¿½rialisation d'un objet.
	 * @param bytes
	 * @return
	 * @throws TechnicalException
	 */
	public static Object deserializeBytesToObject (byte[] bytes) throws ApplicationException{
		File file = decodeBytesToFile(bytes);
		if (file!=null){
			Object result = deserializeFileToObject(file.getAbsolutePath());
			file.delete();
			return result;
		}
		return null;
	}
	
	

	/**
	 * dï¿½serialiser un fichier un fichier en un object de classe JAVA.
	 * @param urlFile
	 * @return
	 * @throws TechnicalException
	 */
	public static Object deserializeFileToObject (String urlFile) throws ApplicationException{
		File file = new File(urlFile);
		if (!file.exists()){
			return null;
		}

        FileInputStream fis = null;
        ObjectInputStream ois= null;
        
		try {	
            fis = new FileInputStream(urlFile);
            ois= new ObjectInputStream(fis);

            Object object = ois.readObject();

            ois.close();
            fis.close();
            return object;
        } catch (java.io.EOFException ex) {
        	throw new ApplicationException( "requï¿½te-incompatible",12);
        } catch (Exception ex) {
        	ex.printStackTrace();
        	throw new ApplicationException("dï¿½serialisation echouï¿½e : "+ex.getMessage()+" UrlFile : "+urlFile,13);
        }
	}
	

	/**
	 * obtenir un fichier binaire reprï¿½sentant un tableau de bytes.
	 * @param bytes
	 * @return
	 * @throws TechnicalException
	 */
	public static File decodeBytesToFile (byte[] bytes) throws ApplicationException{
		File file = createTempFile();
	    return decodeBytesToFile(bytes, file.getAbsolutePath());
	}

	/**
	 * obtenir un fichier binaire reprï¿½sentant un tableau de bytes.
	 * @param bytes
	 * @return
	 * @throws TechnicalException
	 */
	public static File decodeBytesToFile (byte[] bytes, String destinationFilePath) throws ApplicationException{
		File file = new File(destinationFilePath);
		if (!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		if (!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
	        	e.printStackTrace();
				throw new ApplicationException("dï¿½codage de bytes en fichier ï¿½chouï¿½ , impossible de crï¿½er le fichier: "+e.getMessage(),14);
			}
		}
	    try {
	    	FileOutputStream fileOuputStream = new FileOutputStream(file.getAbsoluteFile()); 
		    fileOuputStream.write(bytes);
		    fileOuputStream.close();	
		    return file;
		} catch (IOException e) {
        	e.printStackTrace();
			throw new ApplicationException("dï¿½codage de tableau de byte en fichier, ï¿½chouï¿½: "+e.getMessage(),14);
		}
	}

	
	
	
	/**
	 * 
	 * @return
	 * @throws TechnicalException
	 */
	private static File createTempFile () throws ApplicationException{
		try {
			File tempFile = createTempFile(""+System.currentTimeMillis(), "tmp");
			return tempFile; 
		} catch (Exception e) {
			throw new ApplicationException("Impossible de crï¿½er un fichier : "+e.getMessage(),1);
		}
	}

	
	/**
	 * crï¿½er un fichier temporaire.
	 * @return
	 */
	public static File createTempFile (String prefix, String suffix) throws ApplicationException{
		if (prefix==null){
			prefix = "";
		}
		if (suffix==null){
			suffix = "tmp";
		}
		try {
			String urlFileTemp = getDirectoryTemp()+"ERP-SOCOGEL-tempFile-"+prefix+System.currentTimeMillis()+"_"+Thread.currentThread().hashCode()+"."+suffix;
			File tempFile = new File(urlFileTemp);
			tempFile.createNewFile();
			return tempFile;
		} catch (Exception e) {
			throw new ApplicationException("Impossible de crï¿½er un fichier : "+e.getMessage(),3);
		}
	}
	
	
	/**
	 * obtenir l'adresse d'un dossier temporaire.
	 * @return
	 */
	public static String getDirectoryTemp (){
		try {
			File tempFile = File.createTempFile("ERP-SOCOGEL-tempFile", ".temp");
			File doc = new File(tempFile.getParentFile().getAbsolutePath()+"/TMP/");
			if (!doc.exists()){
				doc.mkdirs();
			}
			return doc.getAbsolutePath()+"/";
		} catch (Exception e) {
			//log.error("Impossible d'obtenir un dossier temporaire de stockage : "+e.getMessage());
			File doc = new File("c:/temp/");
			if (!doc.exists()){
				doc.mkdirs();
			}
			return doc.getAbsolutePath();
		}
	}
	
	
	
    /**
     * sï¿½rialiser un objet d'une classe vers un fichier.
     */
	public static File serializeObjectToFile (Serializable object) throws ApplicationException{
		File file = createTempFile(object.getClass().getName(), "tmp");
		return serializeObjectToFile(object, file.getAbsolutePath());
	}

    /**
     * sï¿½rialiser un objet d'une classe vers un fichier.
     */
	public static File serializeObjectToFile (Serializable object, String destinationFilePath) throws ApplicationException{
		File file = new File(destinationFilePath);
		if (!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		if (!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				throw new ApplicationException("dï¿½codage de bytes en fichier ï¿½chouï¿½ , impossible de crï¿½er le fichier: "+e.getMessage(),7);
			}
		}
        try{
            FileOutputStream requestFile = new FileOutputStream(file.getAbsoluteFile());
            ObjectOutputStream oos= new ObjectOutputStream(requestFile);
            try {

                oos.writeObject(object); 
                oos.flush();

            }catch (Exception ex){
                try {
                    oos.close();
                } finally {
                    requestFile.close();
                }
            } 
            return file;
        } catch(Exception ex) {
        	ex.printStackTrace();
        	throw new ApplicationException("Serialisation echouï¿½e : "+ex.getMessage(),2);
        }
	}




/**
 * transformer un fichier en tableau de bytes.
 * @param urlFile
 * @return
 * @throws TechnicalException
 */
public static byte[] encodeFileToBytes (String urlFile) throws ApplicationException{
	File file = new File(urlFile);
	if (!file.exists()){
		return null;
	}
    try {
		byte[] bFile = new byte[(int) file.length()];
		FileInputStream fileInputStream;
	    fileInputStream = new FileInputStream(file);
		fileInputStream.read(bFile);
	    fileInputStream.close();
		return bFile;
	} catch (IOException e) {
    	e.printStackTrace();
		throw new ApplicationException("encodage de fichier en byte, ï¿½chouï¿½ : "+e.getMessage(),5);
	}
 }



	
	
	/**
	 * retrouve le nombre de digit sur lequel tient une serie  de chiffres
	 * @return
	 */
	public static	String getGabaritOfLastIndex(String nombre,String gabarit){		
		return gabarit.substring(nombre.length());		
	}

	/**
	 * construction des elements d'un select
	 * @param data
	 * @return
	 */
	public static SelectItem[] createFilterOptions(List<String> data)  {  
        SelectItem[] options = new SelectItem[data.size() + 1];  
  
        options[0] = new SelectItem("", "Select");  
        for(int i = 0; i < data.size(); i++) {  
            options[i + 1] = new SelectItem(data.get(i), data.get(i));  
        }  
  
        return options;  
    }  
	
	
	public static String getClassElementOfList (Field field, Class classContaining){
		Method method = ReflectionUtils.getGetter(field, classContaining);
		String result = method.getGenericReturnType().toString();
		
		if (result.contains("<")){
			result = result.substring(result.indexOf("<")+1);
			result = result.substring(0,result.indexOf(">"));
		}
		return result;
	}
	

	public static boolean isList (Field field){
		if (field==null){
			return false;
		}
		Class cl = field.getType();
		for (Class parent : cl.getInterfaces()){
			if (parent.equals(Collection.class)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param nameClass
	 * @return
	 */
	public static Class getClassForName (String nameClass){

		try {
			final Class<?> classNewObject = Class.forName(nameClass);
			if (classNewObject!=null){
				return classNewObject;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return null;
	}
	
	/**
	 * verifie si une propritï¿½ contient l'annotation @Transient
	 * @param field
	 * @return
	 */
	public static boolean isTransientField(Field field){
		if(field.getAnnotation(Transient.class)!= null){
			return true;
		}else{
			return false;
		}
	}

	
	/**
	 * recupï¿½re un echaine unique utilisable dans le systï¿½me
	 * @return
	 */
	public static String getUniqueString(){
		return getString()+System.currentTimeMillis();
	}
	
	
	public static String genererNUMFacture(){
		String str = System.currentTimeMillis()+"";
		str = getString()+str.substring(5);		
		return str;		
	}
	
	public static List< String> splitString(String str,String separator){
		List< String> lisStrings = new ArrayList<String>();
		StringTokenizer stringTokenizer = new StringTokenizer(str, separator);
		
		while (stringTokenizer.hasMoreElements()) {
			String vakue = (String) stringTokenizer.nextElement();
			lisStrings.add(vakue);
		}
		return lisStrings;
	}
	
	
	
	
	
	/**
	 * 
	 * @param date1
	 * @param date2
	 * @return -1 erreur, 2 egalite   1 date1<date2,  
	 */
	public static int compareDate(String date1,String date2){//dd/MM/yyyy
		
	List listDate1 = splitString(date1+"", "/");
	List listDate2 = splitString(date2+"", "/");
		
		if( Integer.parseInt(listDate1.get(2).toString().trim())>Integer.parseInt(listDate2.get(2).toString().trim())){
			return -1; //erreur car date superieur a date2
		}else if(Integer.parseInt(listDate1.get(2).toString().trim())<Integer.parseInt(listDate2.get(2).toString().trim())){
			return 1;			
		}else if(Integer.parseInt(listDate1.get(2).toString().trim())==Integer.parseInt(listDate2.get(2).toString().trim())){
			if(	Integer.parseInt(listDate1.get(0).toString().trim())<Integer.parseInt(listDate2.get(0).toString().trim())){
				return 1;
			}else if(Integer.parseInt(listDate1.get(0).toString().trim()) > Integer.parseInt(listDate2.get(0).toString().trim())){
				return -1;
			}else if(Integer.parseInt(listDate1.get(0).toString().trim()) == Integer.parseInt(listDate2.get(0).toString().trim())){
					if(Integer.parseInt(listDate1.get(1).toString().trim()) == Integer.parseInt(listDate2.get(1).toString().trim())){
						return 2;
					}else if(Integer.parseInt(listDate1.get(1).toString().trim()) > Integer.parseInt(listDate2.get(1).toString().trim())){						
						return -1;
					}else {
						return 1;
					}
			}
		}
		return -1;
	}
	
	

	public static void copyFile(String cheminRepStore, String fileName, InputStream in) {
		try {
			
		//test exsitence du dossier de sauvegarde
			File pointeurSousRepPJ = new File(cheminRepStore);
		     if (pointeurSousRepPJ.exists()) {
		            System.out.println("Le dossier existe déja : " + pointeurSousRepPJ.getAbsolutePath());
		        } else {
		            if (pointeurSousRepPJ.mkdir()) {
		                System.out.println("Ajout du dossier : " + pointeurSousRepPJ.getAbsolutePath());
		            } else {
		                System.out.println("Echec sur le dossier : " + pointeurSousRepPJ.getAbsolutePath());
		            }
		        }
		     
			// write the inputStream to a FileOutputStream
			OutputStream out = new FileOutputStream(new File(cheminRepStore+fileName));
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			in.close();
			out.flush();
			out.close();
			System.out.println("New file created!");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	
	public static String fabriqueNumBordereau(Bordereau bordereau, String dateCourante){    	
    	
    	return "B"+bordereau.getId()+dateCourante;
    }
	/**
	* Fabrique un objet bordereau des l'initiation du transfert des courriers et le retourne, 
	* il sera utilisï¿½ pour persister tous les espacecourrier d'un transfert de la maniere suivante:
	* l'ajouter comme attribut(setter) de la collection des espacecourrier d'un transfert avant de persister le bordereau
	* bordereau.setEspacecourrierCollection(espaceCourrierCollection)
	* @param usercreate
	* @param dateCreate
	* @param espaceCourrierCollection
	* @return
	*/
	public static Bordereau getBordereauDebutTransfert(User usercreate, String dateCreate, Collection<EspaceCourrier> espaceCourrierCollection){
		//recupï¿½ration de la date courante java et fabrication de la date Sql 		 
				 
		Bordereau bordereau = new Bordereau();
		bordereau.setDelate(false);
		bordereau.setCheminPj("Non renseigné");
		bordereau.setDateCreation(dateCreate);     	
		bordereau.setEspacebordereauid(usercreate.getEspace().getId());
		bordereau.setUsercreateid(usercreate.getId());
		bordereau.setUserupdateid(usercreate.getId());
		bordereau.setEstTraite(false);
		if(espaceCourrierCollection != null)
			bordereau.setEspaceCourrierCollection(espaceCourrierCollection);
		bordereau.setNumbordereau("Non renseigné");
		
		return bordereau;
	}
	

}
