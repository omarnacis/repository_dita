package cm.dita.service.domaine.inter;

import cm.dita.service.generic.IServiceBase;
import cm.dita.entities.Preferences;
import cm.dita.exception.ApplicationException;
/**
 * methode pour la mise a jour d'une pr�f�rence juste pour le test
 * @author Bessala
 *@param 
 */
public interface IPreferencesRessourceService extends IServiceBase<Preferences> {

	
	public int getCount() throws ApplicationException;
	public Preferences getParameterByName(String nom)
			throws ApplicationException ;
}

