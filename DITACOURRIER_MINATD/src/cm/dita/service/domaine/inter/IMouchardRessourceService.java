package cm.dita.service.domaine.inter;

import cm.dita.service.generic.IServiceBase;
import cm.dita.entities.Mouchard;
import cm.dita.entities.user.User;

public interface IMouchardRessourceService extends IServiceBase<Mouchard> {
	
	public void tracage(String message, User userconnected);
	public void tracage(String message ,String operation,Long ref_date,String entite);
	void mouchard(String message, String operation, Long ref_date,
			String entite, long code_id, String valeur);

}
