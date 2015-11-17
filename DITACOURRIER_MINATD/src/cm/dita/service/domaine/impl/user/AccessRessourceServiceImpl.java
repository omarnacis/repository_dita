/**
 * 
 */
package cm.dita.service.domaine.impl.user;

import java.io.Serializable;
import java.util.ArrayList;
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
				
				throw new ApplicationException("Probl�me lors du persist de la liste de "+getDao().getOMClass().getSimpleName(), e, 11);
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
	
	/**
	 * les des priviléges groupés
	 * 
	 * @return liste des groupes de privilèges natif 
	 */
	public List<AccessRessource> listBlocAccess()throws ApplicationException{
		
		 Map<String, Object > mapParameter = new HashMap<String, Object>();
	   		//mapParameter.put("identifier", user.getId()); 
	   		try {			
				return getDao().executeNameQueryAndGetListResult("access.list_bloc", mapParameter);
				
			} catch (Exception e) {						
				//e.printStackTrace();
				throw new ApplicationException("Echec de lecture de la liste des bloc de privilèges natif", e, 2);
				
			}
		
	}
	
	
	/**
	 * les des priviléges d'un bloc
	 * @param code du bloc
	 * @return liste des privilèges d'un bloc
	 */
	public List<AccessRessource> listAccessDuBloc(int code_bloc)throws ApplicationException{
		
		 Map<String, Object > mapParameter = new HashMap<String, Object>();
	   		mapParameter.put("code_bloc",code_bloc); 
	   		try {			
				return getDao().executeNameQueryAndGetListResult("access.list_access_du_bloc", mapParameter);
				
			} catch (Exception e) {						
				//e.printStackTrace();
				throw new ApplicationException("Echec de lecture des privilèges du bloc :"+code_bloc, e, 2);
				
			}
		
	}
	/**
	 * Cette fonction retourne la liste d'un bloc appartenant à un groupe
	 * @param id du groupe a trouver les privilèges
	 * @param bloc d'appartenance des privilèges
	 * @return liste des privilèges appartenant au groupe 
	 * @throws ApplicationException
	 */
	
	
	public List<String> listAccessDuBlocSelect(int idgroupe,int code_bloc,int colone)throws ApplicationException{	
		List<String> lister=new ArrayList<String>();
		 Map<String, Object > mapParameter = new HashMap<String, Object>();
	   		mapParameter.put("code_bloc",code_bloc); 
	   		mapParameter.put("idgroupe",idgroupe); 
	   		try {			
				List<AccessRessource> liste= getDao().executeNameQueryAndGetListResult("access.list_access_du_bloc_select", mapParameter);
				for(AccessRessource access:liste){
					if(colone==1)
						lister.add(String.valueOf(access.getIdRessource()));
					else
						lister.add(String.valueOf(access.getRessourceDetail()));
				}
				return lister;
			} catch (Exception e) {						
				//e.printStackTrace();
				throw new ApplicationException("Echec de lecture des privilèges du bloc :"+code_bloc+" attribuer au groupe "+idgroupe, e, 2);
				
			}
		
	}

}
