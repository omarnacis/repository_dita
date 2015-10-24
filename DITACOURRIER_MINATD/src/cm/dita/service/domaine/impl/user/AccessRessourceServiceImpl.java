/**
 * 
 */
package cm.dita.service.domaine.impl.user;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityTransaction;

import org.hibernate.Transaction;
import org.springframework.transaction.annotation.Transactional;

import cm.dita.dao.domaine.inter.user.IAccessRessourceDao;
import cm.dita.entities.user.AccessRessource;
import cm.dita.entities.user.Group;
import cm.dita.entities.user.User;
import cm.dita.exception.ApplicationException;
import cm.dita.service.domaine.inter.user.IAccessRessourceService;
import cm.dita.service.generic.ServiceBaseImpl;



/**
 * @author bruno
 *
 */
public class AccessRessourceServiceImpl extends
		ServiceBaseImpl<AccessRessource> implements IAccessRessourceService,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IAccessRessourceDao dao;
	
	/* (non-Javadoc)
	 * @see cm.socogel.service.generic.IServiceBase#getDao()
	 */
	@Override
	public IAccessRessourceDao getDao() {
		// TODO Auto-generated method stub
		return dao;
	}

	public void setDao(IAccessRessourceDao dao) {
		this.dao = dao;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return getDao().getCount();
	}	
	
	@Transactional
	public void saveAllRessource(List<AccessRessource> list) throws ApplicationException{		
			
			
			try {
				for(AccessRessource ressource : list){	
					
					getDao().save(ressource);
				}
				//getDao().getCurrentSession().flush();//new
			    //getDao().closeSession();//new
			} catch (Exception e) {
				
				throw new ApplicationException("Problème lors du persist de la liste de "+getDao().getOMClass().getSimpleName(), e, 11);
			}
			
		
	}

	@Override
	public List<AccessRessource> listOfAccess4Group(Group group) {
		 Map<String, Object > mapParameter = new HashMap<String, Object>();
	   		mapParameter.put("identifier", group.getIdGroup()); 
	   		try {			
				return getDao().executeNameQueryAndGetListResult("group.Access", mapParameter);
				
			} catch (Exception e) {
						
				//e.printStackTrace();
				throw new ApplicationException("Impossible de delete les groupes du role :", e, 2);
				
			}	
	}
	
	/**
	 * @see Liste des acces d'un utilisateur
	 * @param utilisateur
	 * @return liste des ses access
	 */
	
	
	public List<AccessRessource> listAccess2User(User user) throws ApplicationException{		
		 Map<String, Object > mapParameter = new HashMap<String, Object>();
   		mapParameter.put("identifier", user.getId()); 
   		try {			
			return getDao().executeNameQueryAndGetListResult("User.access", mapParameter);
			
		} catch (Exception e) {
					
			//e.printStackTrace();
			throw new ApplicationException("Impossible de delete les groupes du role :", e, 2);
			
		}
		
	}

}
