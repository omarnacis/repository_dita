package cm.dita.service.domaine.inter;

import java.util.List;

import cm.dita.service.generic.IServiceBase;
import cm.dita.entities.Espace;
import cm.dita.entities.user.User;

public interface IEspaceRessourceService extends IServiceBase<Espace>{

	public  boolean userExiste(Espace espace);
	public List<Espace> getMyEspace(Espace espace);
	
	public void updateHierachie(String new_hie,Espace espace);
	public List<Espace> listeFonctionNonAttribue();
}
