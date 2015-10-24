package cm.dita.utils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

/**
 * <p>Title: ORMUtils</p>
 * 
 * <p>
 * Util class containing several methods used in Hibernate session and beans's manipulations.
 * </p>
 * @author Huseyin Ozveren 
 */
public class ORMUtils {

	public static <T> T initializeAndUnproxy(T entity) {
		if (entity == null) {
			return null;
		}

		if (entity instanceof HibernateProxy) {
			Hibernate.initialize(entity);
			entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
		}		
			
		return entity;
	}
	
	

	
	/**
	 * Call a lazy method of corresponding the path p_getter.
	 * 
	 * @param p_objet
	 *            object that contains the method
	 * @param p_getter
	 *            path of getter separate by "." <br/>
	 *            Ex: user.adresse.rue => getUser().getAdresse().getRue()
	 * @return value of the object corresponding of the path in p_objet
	 * @throws TechnicalException
	 */
	private static Object callLazyMethod(Object p_objet, String p_getter)
			throws Exception {
		Object result = null;
		if (p_objet != null && !"".equals(p_getter)) {
			try {
				String[] getTab = p_getter.split("\\.");
				String listSousGetter = "";

				/*
				 * mettre en majuscule le premiere lettre et ajouter get Ex:
				 * utilisateur => getUtilisateur
				 */
				String currentGetter = getTab[0].substring(0, 1);
				currentGetter = "get" + currentGetter.toUpperCase();
				currentGetter += getTab[0].substring(1);

				for (int i = 1; i < getTab.length; i++) {
					listSousGetter += (i == 1 ? "" : ".") + getTab[i];
				}

				Method method = p_objet.getClass().getMethod(currentGetter,
						new Class[] {});
				result = method.invoke(p_objet, new Object[] {});
				if (result != null) {
					if (result instanceof Collection) {
						Collection collection = (Collection) result;
						for (Iterator iter = collection.iterator(); iter
								.hasNext();) {
							Object object = iter.next();
							callLazyMethod(object, listSousGetter);
						}
					} else {
						result.toString();
						callLazyMethod(result, listSousGetter);
					}
				}
			} catch (Exception e) {
				throw new Exception(p_objet.getClass().getName() +"  Erreur d'initialisation",e);
			}
		}
		return result;
	}

	/**
	 * Initializate some attribut of an object
	 * 
	 * @param p_objetToInit
	 * @param p_listGetterToInit
	 * @throws TechnicalException
	 */
	public static void initLazyGetters(Object p_objetToInit,
			String[] p_listGetterToInit) throws Exception {
		if (p_objetToInit != null && p_listGetterToInit != null) {
			for (int i = 0; i < p_listGetterToInit.length; i++) {
				callLazyMethod(p_objetToInit, p_listGetterToInit[i]);
			}
		}
	}

	

}
