package cm.dita.service.domaine.inter;

import java.util.Collection;
import java.util.List;

import cm.dita.service.generic.IServiceBase;
import cm.dita.entities.Bordereau;
import cm.dita.entities.EspaceCourrier;
import cm.dita.entities.user.User;

public interface IEspaceCourrierRessourceService extends IServiceBase<EspaceCourrier> {
	
	public List<EspaceCourrier> listCourrierParEspace(int espace);
	public List<EspaceCourrier> listCourrierParEspacefiltre(int espace, int iduser);
	public List<EspaceCourrier> listCourrierUser(User user);
	public List<EspaceCourrier> findByBordereauId(Integer bordereauId);
	public List<EspaceCourrier> findByCourrierId(String courrier);
	public List<EspaceCourrier> listCourrierParEspacetraite(int espace);
	public List<EspaceCourrier> listCourrierSearch(int espace, int iduser,String chaine) ;
	public Integer getCountCourrierParEspace(int espace);
	public void transferMany(EspaceCourrier eCourrierT, EspaceCourrier EcDetransfert);
	public String fabriqueNumBordereau(Bordereau bordereau, String dateCourante);
	public Bordereau getBordereauDebutTransfert(User usercreate, String dateCreate, Collection<EspaceCourrier> espaceCourrierCollection);
	
	 
	public List<EspaceCourrier> listCourrierOfBordereau(Bordereau bordereau) ;

}
