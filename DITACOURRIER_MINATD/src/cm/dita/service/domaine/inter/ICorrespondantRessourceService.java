/**
 * 
 */
package cm.dita.service.domaine.inter;

import cm.dita.entities.Correspondant;
import cm.dita.service.generic.IServiceBase;

/**
 * @author Omar Nacis
 *
 */
public interface ICorrespondantRessourceService  extends IServiceBase<Correspondant> {
	
	   public  boolean correspondantExisteWithName(Correspondant correspondant);
	
}
