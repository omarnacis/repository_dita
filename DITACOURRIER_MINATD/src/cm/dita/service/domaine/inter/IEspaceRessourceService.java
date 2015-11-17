package cm.dita.service.domaine.inter;

import cm.dita.service.generic.IServiceBase;
import cm.dita.entities.Espace;

public interface IEspaceRessourceService extends IServiceBase<Espace>{

	public  boolean userExiste(Espace espace);
}
