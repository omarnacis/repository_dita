package cm.dita.dao.domaine.inter;

import java.util.List;

import cm.dita.dao.generic.IDaoBase;
import cm.dita.entities.Courriers;
import cm.dita.entities.user.User;

public interface ICourriersDao extends IDaoBase <Courriers> {
	
	public List<Courriers> listCourrierLater(User userConnect,Long date_limit);
	public List<Courriers> listCourrierSearch(String chaine);
}
