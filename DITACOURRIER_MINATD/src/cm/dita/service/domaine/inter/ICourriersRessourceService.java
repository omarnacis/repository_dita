package cm.dita.service.domaine.inter;

import java.util.List;

import cm.dita.service.generic.IServiceBase;
import cm.dita.entities.Courriers;
import cm.dita.entities.user.User;

public interface ICourriersRessourceService extends IServiceBase <Courriers> {

	public List<Courriers> getListCourrierByUsercreateIdAndDateUseToSortData(User userCreate, Long dateUserToSortData);
	
	public Courriers getCourrierByUsercreateIdAndDateUseToSortData(User userCreate, Long dateUserToSortData);

	public List<Courriers> getCourrierByEspaceOfUserConnect(User userConnect);
	public List<Courriers> listCourrierLater(User userConnect,Long date_limit);
	public List<Courriers> getCourriersRelated(String courrierParent);
	public List<Courriers> listCourrierSearch(String chaine);
	public Courriers findById(Integer idCourrier);
}
