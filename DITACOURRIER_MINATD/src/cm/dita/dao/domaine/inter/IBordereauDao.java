package cm.dita.dao.domaine.inter;

import java.util.List;

import cm.dita.dao.generic.IDaoBase;
import cm.dita.entities.Bordereau;
import cm.dita.entities.Espace;
import cm.dita.entities.user.User;

public interface IBordereauDao extends IDaoBase<Bordereau>{
	
	public List<Bordereau> getListBordereauByUsercreateIdAndDateUseToSortData(User userCreate, Long dateUserToSortData);
	public Bordereau getBordereauByUsercreateIdAndDateUseToSortData(User userCreate, Long dateUserToSortData);
	public List<Bordereau> getListBordereauByEspace(Espace espace);
	public List<Bordereau> getListBordereauByUserCreate(User usercreate);
	public List<Bordereau> getListBordereauByUserUpdate(User userupdate);
	public Bordereau findByNumbordereau(String numbordereau);
	public List<Bordereau> getListBordereauByEspaceAndTraite(Espace espace, Boolean estTraite);

}
