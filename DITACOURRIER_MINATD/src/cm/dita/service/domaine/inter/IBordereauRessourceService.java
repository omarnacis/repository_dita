package cm.dita.service.domaine.inter;

import java.util.List;

import cm.dita.service.generic.IServiceBase;
import cm.dita.entities.Bordereau;
import cm.dita.entities.Courriers;
import cm.dita.entities.Espace;
import cm.dita.entities.user.User;

public interface IBordereauRessourceService extends IServiceBase<Bordereau>{

	public List<Bordereau> getListBordereauByUsercreateIdAndDateUseToSortData(User userCreate, Long dateUserToSortData);
	
	public Bordereau getBordereauByUsercreateIdAndDateUseToSortData(User userCreate, Long dateUserToSortData);

	public List<Bordereau> getListBordereauByEspace(Espace espace);
	
	public List<Bordereau> getListBordereauByUserCreate(User usercreate);
	
	public List<Bordereau> getListBordereauByUserUpdate(User userupdate);
	
	public Bordereau findByNumbordereau(String numbordereau);
	
	public List<Bordereau> getListBordereauByEspaceAndTraite(Espace espace, Boolean estTraite);
}
