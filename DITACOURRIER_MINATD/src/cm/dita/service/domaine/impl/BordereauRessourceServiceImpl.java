package cm.dita.service.domaine.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cm.dita.service.generic.ServiceBaseImpl;
import cm.dita.dao.domaine.inter.IBordereauDao;
import cm.dita.entities.Bordereau;
import cm.dita.entities.Espace;
import cm.dita.entities.user.User;
import cm.dita.service.domaine.inter.IBordereauRessourceService;

/**
 * @author Omar Nacis
 *
 */

/**
 * @author Technipedia
 *
 */
public class BordereauRessourceServiceImpl  extends ServiceBaseImpl<Bordereau>  implements IBordereauRessourceService{
	
	private static final long serialVersionUID = 1L;
	private IBordereauDao dao;
	

	/**
	 * Prend l'id du createur et le temps ecroul� depuis jusqu'au dernier enregistrement et retourne la liste des bordereaus
	 * @author Omar Nacis
	 * @param int long
	 * @return List<Bordereau>
	 */
		public List<Bordereau> getListBordereauByUsercreateIdAndDateUseToSortData(User userCreate, Long dateUserToSortData) {
			
			 return getDao().getListBordereauByUsercreateIdAndDateUseToSortData(userCreate,dateUserToSortData);
		}
		
		/**
		 * 
		 * @param userCreateId
		 * @param dateUserToSortData
		 * @return le dernier bordereaus enregistr� par cet utilisateur
		 */
		public Bordereau getBordereauByUsercreateIdAndDateUseToSortData(User userCreate, Long dateUserToSortData) {
			
			
			 return dao.getBordereauByUsercreateIdAndDateUseToSortData(userCreate,dateUserToSortData);
		}
		
		
		/**
		 * 
		 */
		public List<Bordereau> getListBordereauByEspace(Espace espace) {
			
			 return dao.getListBordereauByEspace(espace);
		}
		
		/**
		 * 
		 */
		public List<Bordereau> getListBordereauByUserCreate(User usercreate) {
			
			 return dao.getListBordereauByUserCreate(usercreate);
		}
		
		/**
		 * 
		 */
		public List<Bordereau> getListBordereauByUserUpdate(User userupdate) {
			
			 return dao.getListBordereauByUserUpdate(userupdate);
		}
		
		/**
		 * retourne un bordereau sachant son numero
		 * @param numbordereau
		 * @return Bordereau
		 */
		public Bordereau findByNumbordereau(String numbordereau) {
						 
			 return dao.findByNumbordereau(numbordereau);
		}
		

		/**
		 * Returne la liste des bordereaux actifs d'un espace en fonction de son traitement trait�
		 * @param espace
		 * @param estTraite
		 * @return List<Bordereau>
		 */
		public List<Bordereau> getListBordereauByEspaceAndTraite(Espace espace, Boolean estTraite) {
			
			 return dao.getListBordereauByEspaceAndTraite(espace,estTraite);
		}
		
		
		
		/**
		 * @return the dao
		 */
		public IBordereauDao getDao() {
			return dao;
		}
		/**
		 * @param dao the dao to set
		 */
		public void setDao(IBordereauDao dao) {
			this.dao = dao;
		}

}
