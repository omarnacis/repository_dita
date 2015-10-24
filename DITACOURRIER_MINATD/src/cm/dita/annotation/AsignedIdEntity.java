/**
 * 
 */
package cm.dita.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author bertrand
 *
 */
@Documented
@Retention( RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AsignedIdEntity{
	
	/** Nom de la propri�t� li�e � cet �l�ment. */
	String name () ;
	/** Valeur si l'�l�ment est absent (optionnel / "" par d�faut). */
	//String missing() default "default";

}
