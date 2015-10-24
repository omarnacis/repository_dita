package cm.dita.dao.domaine.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cm.dita.dao.domaine.inter.IBordereauDao;
import cm.dita.dao.generic.DaoBaseImpl;
import cm.dita.entities.Bordereau;
import cm.dita.entities.Espace;
import cm.dita.entities.user.User;

public class BordereauDaoImpl extends DaoBaseImpl<Bordereau> implements IBordereauDao{

			
	

	/**
	 * Prend l'id du createur et le temps ecroul� depuis jusqu'au dernier enregistrement et retourne la liste des bordereaus
	 * @author Omar Nacis
	 * @param int long
	 * @return List<Bordereau>
	 */
		public List<Bordereau> getListBordereauByUsercreateIdAndDateUseToSortData(User userCreate, Long dateUserToSortData) {
			// TODO Auto-generated method
	    	Map<String , Object> parameters = new HashMap<String, Object>();
			parameters.put("dateUserToSortData", dateUserToSortData);
			parameters.put("userCreateId", userCreate.getId());
			//getDao().getCurrentSession().flush(); 
			//List<Courriers> courriers = getDao().getCurrentSession().createQuery("select c from Courriers c where c.dateUseToSortData=:dateUserToSortData and c.usercreate.id=:userCreateId").setParameter("espace", espace).getResultList();
			List<Bordereau> bordereaus = executeNameQueryAndGetListResult("Bordereau.findByUsercreateIdAndDateUseToSortData", parameters);
			
			if(bordereaus != null && bordereaus.size()> 0){
				 
				 return bordereaus;
			 }
			 
			 return null;
		}
		
		/**
		 * 
		 * @param userCreateId
		 * @param dateUserToSortData
		 * @return le dernier bordereaus enregistr� par cet utilisateur
		 */
		public Bordereau getBordereauByUsercreateIdAndDateUseToSortData(User userCreate, Long dateUserToSortData) {
			
			//List<Courriers> courriers = getDao().getCurrentSession().createQuery("select c from Courriers c where c.dateUseToSortData=:dateUserToSortData and c.usercreate.id=:userCreateId").setParameter("espace", espace).getResultList();
			List<Bordereau> bordereaus = getListBordereauByUsercreateIdAndDateUseToSortData(userCreate, dateUserToSortData);
				
			if(bordereaus != null && bordereaus.size()> 0){
				 
				 return bordereaus.get(bordereaus.size()-1);
			 }
			 
			 return null;
		}
		
		
		/**
		 * 
		 */
		public List<Bordereau> getListBordereauByEspace(Espace espace) {
			// TODO Auto-generated method
	    	Map<String , Object> parameters = new HashMap<String, Object>();
			parameters.put("espacebordereauid", espace.getId());
			
			//getDao().getCurrentSession().flush(); 
			//List<Courriers> courriers = getDao().getCurrentSession().createQuery("select c from Courriers c where c.dateUseToSortData=:dateUserToSortData and c.usercreate.id=:userCreateId").setParameter("espace", espace).getResultList();
			List<Bordereau> bordereaus = executeNameQueryAndGetListResult("Bordereau.findByEspaceid", parameters);
			
			if(bordereaus != null && bordereaus.size()> 0){
				 
				 return bordereaus;
			 }
			 
			 return null;
		}
		
		/**
		 * 
		 */
		public List<Bordereau> getListBordereauByUserCreate(User usercreate) {
			// TODO Auto-generated method
	    	Map<String , Object> parameters = new HashMap<String, Object>();
			parameters.put("usercreateid", usercreate.getId());
			
			//getDao().getCurrentSession().flush(); 
			//List<Courriers> courriers = getDao().getCurrentSession().createQuery("select c from Courriers c where c.dateUseToSortData=:dateUserToSortData and c.usercreate.id=:userCreateId").setParameter("espace", espace).getResultList();
			List<Bordereau> bordereaus = executeNameQueryAndGetListResult("Bordereau.findByUsercreateid", parameters);
			
			if(bordereaus != null && bordereaus.size()> 0){
				 
				 return bordereaus;
			 }
			 
			 return null;
		}
		
		/**
		 * 
		 */
		public List<Bordereau> getListBordereauByUserUpdate(User userupdate) {
			// TODO Auto-generated method
	    	Map<String , Object> parameters = new HashMap<String, Object>();
			parameters.put("userupdateid", userupdate.getId());
			
			//getDao().getCurrentSession().flush(); 
			//List<Courriers> courriers = getDao().getCurrentSession().createQuery("select c from Courriers c where c.dateUseToSortData=:dateUserToSortData and c.usercreate.id=:userCreateId").setParameter("espace", espace).getResultList();
			List<Bordereau> bordereaus = executeNameQueryAndGetListResult("Bordereau.findByUserupdateid", parameters);
			
			if(bordereaus != null && bordereaus.size()> 0){
				 
				 return bordereaus;
			 }
			 
			 return null;
		}
		
		/**
		 * retourne un bordereau sachant son numero
		 * @param numbordereau
		 * @return Bordereau
		 */
		public Bordereau findByNumbordereau(String numbordereau) {
			// TODO Auto-generated method
	    	Map<String , Object> parameters = new HashMap<String, Object>();
			parameters.put("numbordereau", numbordereau);
		    List<Bordereau> bordereaux =executeNameQueryAndGetListResult("Bordereau.findByNumbordereau", parameters);
			
			if(bordereaux != null && bordereaux.size()> 0){
				 
				 return bordereaux.get(0);
			 }
			 
			 return null;
		}
		

		/**
		 * Returne la liste des bordereaux actifs d'un espace en fonction de son traitement trait�
		 * @param espace
		 * @param estTraite
		 * @return List<Bordereau>
		 */
		public List<Bordereau> getListBordereauByEspaceAndTraite(Espace espace, Boolean estTraite) {
			// TODO Auto-generated method
	    	Map<String , Object> parameters = new HashMap<String, Object>();
			parameters.put("espacebordereauid", espace.getId());
			parameters.put("estTraite", estTraite);
			List<Bordereau> bordereaus = executeNameQueryAndGetListResult("Bordereau.findByEspaceAndTraite", parameters);
			
			if(bordereaus != null && bordereaus.size()> 0){
				 
				 return bordereaus;
			 }
			 
			 return null;
		}



}
