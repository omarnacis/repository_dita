package cm.dita.service.domaine.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;








import javax.persistence.Query;

import cm.dita.service.generic.ServiceBaseImpl;
import cm.dita.dao.domaine.inter.ICourriersDao;
import cm.dita.entities.Courriers;
import cm.dita.entities.user.User;
import cm.dita.service.domaine.inter.ICourriersRessourceService;

/**
 * @author Omar Nacis
 *
 */

/**
 * @author Omar Nacis
 *
 */
public class CourriersRessourceServiceImpl  extends ServiceBaseImpl<Courriers>  implements ICourriersRessourceService{
	
	private static final long serialVersionUID = 1L;
	private ICourriersDao dao;
	
	public ICourriersDao getDao() {
		return dao;
	}
	public void setDao(ICourriersDao dao) {
		this.dao = dao;
	}
	

   
/**
 * Prend l'id du createur et le temps ecroul� depuis jusqu'au dernier enregistrement et retourne la liste des courriers
 * @author Omar Nacis
 * @param int long
 * @return List<Courriers>
 */
	public List<Courriers> getListCourrierByUsercreateIdAndDateUseToSortData(User userCreate, Long dateUserToSortData) {
		// TODO Auto-generated method
    	Map<String , Object> parameters = new HashMap<String, Object>();
		parameters.put("dateUserToSortData", dateUserToSortData);
		parameters.put("userCreateId", userCreate.getId());
		//getDao().getCurrentSession().flush(); 
		//List<Courriers> courriers = getDao().getCurrentSession().createQuery("select c from Courriers c where c.dateUseToSortData=:dateUserToSortData and c.usercreate.id=:userCreateId").setParameter("espace", espace).getResultList();
		List<Courriers> courriers = getDao().executeNameQueryAndGetListResult("Courriers.findByUsercreateIdAndDateUseToSortData", parameters);
		
		if(courriers != null && courriers.size()> 0){
			 
			 return courriers;
		 }
		 
		 return null;
	}
	
	
	/**
	 * 
	 * @param userCreateId
	 * @param dateUserToSortData
	 * @return le dernier courrier enregistr� par cet utilisateur
	 */
	public Courriers getCourrierByUsercreateIdAndDateUseToSortData(User userCreate, Long dateUserToSortData) {
		
		//List<Courriers> courriers = getDao().getCurrentSession().createQuery("select c from Courriers c where c.dateUseToSortData=:dateUserToSortData and c.usercreate.id=:userCreateId").setParameter("espace", espace).getResultList();
		List<Courriers> courriers = getListCourrierByUsercreateIdAndDateUseToSortData(userCreate, dateUserToSortData);
			
		if(courriers != null && courriers.size()> 0){
			 
			 return courriers.get(courriers.size()-1);
		 }
		 
		 return null;
	}
	
/**
 * Retourne la liste des courriers de l'espace de l'utilisateur connect�
 * @param userConnect
 * @return
 */
	public List<Courriers> getCourrierByEspaceOfUserConnect(User userConnect) {
		// TODO Auto-generated method
    	Map<String , Object> parameters = new HashMap<String, Object>();
		parameters.put("idespacecourantducour", userConnect.getEspace().getId());
		
		List<Courriers> courriers = getDao().executeNameQueryAndGetListResult("Courriers.findByEspaceOfUserConnect", parameters);
		
		if(courriers != null && courriers.size()> 0){
			 
			 return courriers;
		 }
		 
		 return null;
	}
	
	
	public List<Courriers> listCourrierLater(User userConnect,Long date_limit) {
		// TODO Auto-generated method
    	/*Map<String , Object> parameters = new HashMap<String, Object>();
		parameters.put("limit_date", date_limit);
		Long date_current=System.currentTimeMillis();
		
		Query query=dao.getCurrentSession().createQuery("select c from Courriers c where (c.dateUseToSortData-:date_current) > :limit_date");
		query.setParameter("limit_date", date_limit);
		*/
		
		return dao.listCourrierLater(userConnect, date_limit);
	}
	
	@Override
	public List<Courriers> listCourrierSearch(String chaine) {
		// TODO Auto-generated method stub
		return dao.listCourrierSearch(chaine);
	}
	
	@Override
	public List<Courriers> getCourriersRelated(String courrierParent) {
		List<Courriers> courrierList = new ArrayList<Courriers>();
		// TODO Auto-generated method stub
		List<Courriers> courrier = getDao().getCurrentSession().createQuery("select e from Courriers e where e.refid=:courrierParent").setParameter("courrierParent", courrierParent).getResultList();
		
		if(courrier.get(0).getCourrierracineid()!=null){
			int racine=courrier.get(0).getCourrierracineid();
			courrierList=getDao().getCurrentSession().createQuery("select e from Courriers e where e.refid<>:courrierParent and (e.courrierracineid=:racine or e.id=:racine)").setParameter("racine", racine).setParameter("courrierParent", courrierParent).getResultList();
		 
		}else{
			int racine=courrier.get(0).getId();
			courrierList=getDao().getCurrentSession().createQuery("select e from Courriers e where e.courrierracineid=:racine").setParameter("racine", racine).getResultList();
			//courrierList.add(courrier.get(0));
		}
		return courrierList;
	}
	

	public Courriers findById(Integer idCourrier) {
		// TODO Auto-generated method
    	Map<String , Object> parameters = new HashMap<String, Object>();
		parameters.put("id", idCourrier);
		
		List<Courriers> courriers = getDao().executeNameQueryAndGetListResult("Courriers.findById", parameters);
		
		if(courriers != null && courriers.size()> 0){
			 
			 return courriers.get(0);
		 }
		 
		 return null;
	}
    

}
