package cm.dita.service.domaine.impl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.springframework.transaction.annotation.Transactional;

import cm.dita.service.generic.ServiceBaseImpl;
import cm.dita.constant.IConstance;
import cm.dita.dao.domaine.inter.IBordereauDao;
import cm.dita.dao.domaine.inter.ICourriersDao;
import cm.dita.dao.domaine.inter.IEspaceCourrierDao;
import cm.dita.dao.domaine.inter.IEspaceDao;
import cm.dita.dao.domaine.inter.IMouchardDao;
import cm.dita.dao.domaine.inter.IStatutsDao;
import cm.dita.dao.domaine.inter.user.IUserDao;
import cm.dita.dao.generic.IDaoBase;
import cm.dita.entities.EspaceCourrier;
import cm.dita.entities.Bordereau;
import cm.dita.entities.user.User;
import cm.dita.service.domaine.inter.IEspaceCourrierRessourceService;

/**
 * @author Omar Nacis
 *
 */
@Transactional
public class EspaceCourrierRessourceServiceImpl  extends ServiceBaseImpl<EspaceCourrier>  implements IEspaceCourrierRessourceService{
	
	private static final long serialVersionUID = 1L;
	private IEspaceCourrierDao dao;
	private ICourriersDao courriersDao;
	private IBordereauDao bordereauDao;
	private IStatutsDao statutsDao;
	private IEspaceDao espaceDao;
	private IMouchardDao mouchardDao;
	private IUserDao userDao;
	
	@Override
	public List<EspaceCourrier> listCourrierParEspacefiltre(int espace, int iduser) {
		// TODO Auto-generated method
		//getDao().getCurrentSession().flush();
		List<EspaceCourrier> ecourriers = getDao().getCurrentSession().createQuery("select e from EspaceCourrier e where e.espace.id=:espace AND e.delate='false' AND e.statut.id<>3 and (e.userreceiver=null or e.userreceiver.id=:iduser) order by dateUseToSortData desc").setParameter("espace", espace).setParameter("iduser", iduser).getResultList();
   		return ecourriers;
   	/*	and e.userreceiver=null or e.userreceiver.id:=iduser*/
	}
	
	public List<EspaceCourrier> listCourrierSearch(int espace, int iduser,String chaine) {
		// TODO Auto-generated method
		chaine="%"+chaine+"%";
		List<EspaceCourrier> ecourriers = getDao().getCurrentSession().createQuery("select e from EspaceCourrier e , Courriers c where e.courrier.id=c.id AND e.espace.id=:espace AND e.delate='false' AND e.statut.id<>3 and (e.userreceiver=null or e.userreceiver.id=:iduser)"
				+ " AND courmots LIKE :chaine").setParameter("espace", espace).setParameter("iduser", iduser).setParameter("chaine", chaine).getResultList();
   		return ecourriers;
   	/*	and e.userreceiver=null or e.userreceiver.id:=iduser*/
	}
	
	@Override
	public List<EspaceCourrier> listCourrierParEspace(int espace) {
		// TODO Auto-generated method
		List<EspaceCourrier> ecourriers = getDao().getCurrentSession().createQuery("select e from EspaceCourrier e where e.espace.id=:espace and e.delate='false' order by dateUseToSortData desc").setParameter("espace", espace).getResultList();
   		return ecourriers;
	}
	
	@Override
	public Integer getCountCourrierParEspace(int espace) {
		// TODO Auto-generated method
		return ((Long) dao.getCurrentSession().createQuery("select count(*) from EspaceCourrier e where e.espace.id=:espace and e.delate='false'").setParameter("espace", espace).getResultList().get(0)).intValue();
   		
	}
	
	
	@Override
	public List<EspaceCourrier> listCourrierParEspacetraite(int espace) {
		// TODO Auto-generated method
		List<EspaceCourrier> ecourriers = getDao().getCurrentSession().createQuery("select e from EspaceCourrier e where e.espace.id=:espace and e.delate='false' and e.statut.id=3 order by dateUseToSortData desc").setParameter("espace", espace).getResultList();
   		return ecourriers;
	}
	@Override
	public List<EspaceCourrier> listCourrierUser(User user) {
		// TODO Auto-generated method
		List<EspaceCourrier> usercourrier= dao.getCurrentSession().createQuery("select e from EspaceCourrier e where e.usercreate=:user and e.delate='false'").setParameter("user",user).getResultList();
   		return usercourrier;
	}
	/**
	 * *retourne la liste des espacecourrier sachant l'idBordereau
	 */
	public List<EspaceCourrier> findByBordereauId(Integer bordereauId) {
		// TODO Auto-generated method
    	Map<String , Object> parameters = new HashMap<String, Object>();
		parameters.put("bordereauid", bordereauId);
		List<EspaceCourrier> espaceCourriers = getDao().executeNameQueryAndGetListResult("EspaceCourrier.findByBordereauId", parameters);
		
		if(espaceCourriers != null && espaceCourriers.size()> 0){
			 
			 return espaceCourriers;
		 }
		 
		 return null;
	}
	@Override
	
	public List<EspaceCourrier> findByCourrierId(String idcourrier) {
		List<EspaceCourrier> listcourriersuivie=dao.getCurrentSession().createQuery("select e from EspaceCourrier e where e.courrier.refid=:idcourrier and e.delate='false' order by dateUseToSortData desc").setParameter("idcourrier",idcourrier).getResultList();
   		return listcourriersuivie;
		
	}
	
	@Override
	
	public void transferMany(EspaceCourrier eCourrierT, EspaceCourrier EcDetransfert) {
		// TODO Auto-generated method stub
		
		   	dao.save(EcDetransfert);
			dao.save(eCourrierT);
	
	
		
	}

	@Override
	public String fabriqueNumBordereau(Bordereau bordereau, String dateCourante) {
		// TODO Auto-generated method stub
		return "B"+bordereau.getId()+dateCourante;
	}

	@Override
	public Bordereau getBordereauDebutTransfert(User usercreate,
			String dateCreate,
			Collection<EspaceCourrier> espaceCourrierCollection) {
		// TODO Auto-generated method stub
		Bordereau bordereau = new Bordereau();
    	bordereau.setDelate(false);
    	bordereau.setCheminPj("Non renseigné");
    	bordereau.setDateCreation(dateCreate);     	
    	bordereau.setEspacebordereauid(usercreate.getEspace().getId());
    	bordereau.setUsercreateid(usercreate.getId());
    	bordereau.setUserupdateid(usercreate.getId());
    	bordereau.setEstTraite(false);
    	bordereau.setEspaceCourrierCollection(espaceCourrierCollection);
    	bordereau.setNumbordereau("Non renseigné");
    	
    	return bordereau;
	}
	/**
	 * Retourne liste des courriers d'un bordereau
	 * @param bordereau
	 * @return liste espaceCourrier
	 * 
	 */
	public List<EspaceCourrier> listCourrierOfBordereau(Bordereau bordereau) {
		// TODO Auto-generated method stub
		List<EspaceCourrier> bordereaucourriers= dao.getCurrentSession().createQuery("select e from EspaceCourrier e where e.bordereau.id=:id and e.delate='false'").setParameter("id",bordereau.getId()).getResultList();
   		return bordereaucourriers;
	}
	
	
	/**
	 * @return the dao
	 */
	public IEspaceCourrierDao getDao() {
		return dao;
	}
	/**
	 * @param dao the dao to set
	 */
	public void setDao(IEspaceCourrierDao dao) {
		this.dao = dao;
	}

	

	/**
	 * @return the courriersDao
	 */
	public ICourriersDao getCourriersDao() {
		return courriersDao;
	}

	/**
	 * @param courriersDao the courriersDao to set
	 */
	public void setCourriersDao(ICourriersDao courriersDao) {
		this.courriersDao = courriersDao;
	}

	public IBordereauDao getBordereauDao() {
		return bordereauDao;
	}

	public void setBordereauDao(IBordereauDao bordereauDao) {
		this.bordereauDao = bordereauDao;
	}

	/**
	 * @return the statutsDao
	 */
	public IStatutsDao getStatutsDao() {
		return statutsDao;
	}

	/**
	 * @param statutsDao the statutsDao to set
	 */
	public void setStatutsDao(IStatutsDao statutsDao) {
		this.statutsDao = statutsDao;
	}

	/**
	 * @return the espaceDao
	 */
	public IEspaceDao getEspaceDao() {
		return espaceDao;
	}

	/**
	 * @param espaceDao the espaceDao to set
	 */
	public void setEspaceDao(IEspaceDao espaceDao) {
		this.espaceDao = espaceDao;
	}

	/**
	 * @return the mouchardDao
	 */
	public IMouchardDao getMouchardDao() {
		return mouchardDao;
	}

	/**
	 * @param mouchardDao the mouchardDao to set
	 */
	public void setMouchardDao(IMouchardDao mouchardDao) {
		this.mouchardDao = mouchardDao;
	}

	/**
	 * @return the userDao
	 */
	public IUserDao getUserDao() {
		return userDao;
	}

	/**
	 * @param userDao the userDao to set
	 */
	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	

	
	
	
}
