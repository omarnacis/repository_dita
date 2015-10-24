package cm.dita.utils;

import org.springframework.beans.BeansException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class ApplicationContextHolder implements ApplicationContextAware {
 
	/** Contexte Spring qui sera injecte par Spring directement */
	private static ApplicationContext context = null;
 

	/**
	* M�thode de ApplicationContextAware, qui sera appell�e automatiquement par le conteneur
	*/
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		context = ctx;
	}
 
 
	/**
	 * Methode statique pour r�cup�rer le contexte
	 */
	public static ApplicationContext getContext() {
		return context;
	}
 
}

