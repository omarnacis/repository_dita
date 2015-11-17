/**
 * 
 */
package cm.dita.utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

/**
 * @author bertrand
 *
 */
public class ReflectionUtils {

	private ReflectionUtils() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * retourne le field correspondant � un nom donn� dans une classe
	 * @param object
	 * @param fieldName
	 * @return
	 */
//	public static Field getField(Object object,String fieldName){		
//		Class objClass = object.getClass();	
//		Field field = null;
//		try {
//			field =  objClass.getDeclaredField(fieldName);		
//			
//			return field;// � refaire avec la recursivit� pour tous les cas 
//		} catch (NoSuchFieldException e ) {
//			if(field == null){
//				try {
//					field = objClass.getSuperclass().getDeclaredField(fieldName);
//				} catch (NoSuchFieldException | SecurityException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
//			
//			return field;
//			}	
//		  catch (SecurityException e) {
//			e.printStackTrace();
//			return null;
//			}	
//		
//	}
//	
	
	public static Field getField(Class objClass,String fieldName){		
		//Class objClass = object.getClass();	
		
		Field field = null;
		field = getFieldOnClass(objClass,fieldName);
		
		if(field != null){
			return field;
		}else{
			Class superClass = objClass.getSuperclass();
			if(superClass != null){
				return getField(superClass,fieldName);
			}else{
				return null;
			}
		}	
		
	}
	
	
	private static Field getFieldOnClass(Class  clss, String fieldName){
		if(!MethodUtils.stringIsNullOrBlank(fieldName)){
			for(Field field : clss.getDeclaredFields()){
				if(field.getName().equalsIgnoreCase(fieldName)){
					return field;
				}
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
	public static Method getGetter(Field field, Class classObj) {
		String name = "get" + field.getName();
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

	

	public static String getGetterMethode(Object object,String fieldName){		
		Field field = getField(object.getClass(), fieldName);
		return getGetter(field, object.getClass()).getName();
	}
	
	/**
	 * 
	 * @param object
	 * @param field
	 * @return
	 */
	public static Method getGetterOfField(Object object, Field field){
	
		String fieldName = field.getName();
		
		String currentGetter = fieldName.substring(0, 1);
		currentGetter = "get" + currentGetter.toUpperCase();
		currentGetter += fieldName.substring(1);
		
		Method method = null;
		try {
			method = object.getClass().getMethod(currentGetter,	new Class[] {});
		} catch (NoSuchMethodException e) {			
			e.printStackTrace();
		} catch (SecurityException e) {			
			e.printStackTrace();
		}
		return method;		
	}
	
	
	/**
	 * 
	 * @param class1
	 * @return
	 */
	public static Field getIdentifierOM(Class class1){
		
		for(Field  field : class1.getDeclaredFields()){			
			Id annotatedElement = field.getAnnotation(Id.class);
			if(annotatedElement != null){
				return field;
			}			
		}
		return null;
	}
	
	
	public static Serializable getValueOfIdOM(Object object){
		
		Field field = getIdentifierOM(object.getClass());
		if(field != null){
			
			Method method = getGetterOfField(object, field);
				if(method != null){
					try {
						return (Serializable) method.invoke(object, new Object[] {});
					} catch (IllegalAccessException e) {			
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}else{
					//metre les log ici
					return null;
				}
		}
		return null;
	}
	
	
	/**
	 * recup�re la liste des fields de type String d'une classe ne prend pas en compte les fields de la super classe a voir 
	 * plutard
	 * @param listNameAttributObjectToGetFieldInCurrentClass  ici on a la liste des attributs complexe dont on recuperera les fields
	 * @param clazz
	 * @return
	 */
	public static List<String> getListStringFieldOfClass(Class clazz, List<String> listNameAttributObjectToGetFieldInCurrentClass,String parent){
		
		List<String> listNameField = new ArrayList<String>();
		for(Field field : clazz.getDeclaredFields()){
			if(field.getType().getName().contains("String")){
			  if(!MethodUtils.isTransientField(field)){//si le champ n'est pas transient
				//System.out.println(" Type de field : "+ field.getName() +"  "+ parent+field.getName());				
				listNameField.add(parent+field.getName());
			  }
			}else if(!MethodUtils.isSimpleType(field) && !MethodUtils.isDateOrByteType(field) ){
					if(!MethodUtils.isList(field) && listNameAttributObjectToGetFieldInCurrentClass!= null){//cas ou on a une prop de type complexe qui n'est pas une liste					
						parent = field.getName()+".";
					 if(listNameAttributObjectToGetFieldInCurrentClass.contains(field.getName())){
						Class classOfAttribut = MethodUtils.getClassForName(field.getType().getName());						
						listNameField.addAll(getListStringFieldOfClass(classOfAttribut,listNameAttributObjectToGetFieldInCurrentClass,parent));
					 }
					}
			}
		}
		return listNameField;
	}
	
}
