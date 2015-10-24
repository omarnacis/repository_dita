/**
 * 
 */
package cm.dita.service.domaine.impl.user;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityTransaction;







import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import cm.dita.constant.IConstance;
import cm.dita.dao.domaine.inter.user.IGroupAccessRessourceDao;
import cm.dita.dao.domaine.inter.user.IGroupDao;
import cm.dita.dao.domaine.inter.user.IRoleGroupDao;
import cm.dita.entities.user.Group;
import cm.dita.entities.user.GroupAccessRessource;
import cm.dita.entities.user.Role;
import cm.dita.exception.ApplicationException;
import cm.dita.service.domaine.inter.user.IGroupService;
import cm.dita.service.generic.ServiceBaseImpl;


/**
 * @author bruno
 *
 */
public class GroupServiceImpl extends ServiceBaseImpl<Group> implements
		IGroupService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IGroupDao dao;
	private IGroupAccessRessourceDao groupAccessRessourceDao;
	private IRoleGroupDao roleGroupDao;
	/* (non-Javadoc)
	 * @see cm.socogel.service.generic.IServiceBase#getDao()
	 */
	
	public  boolean userExiste(Group group){
    	
		   Map<String , Object> map=new Hashtable<String,Object>();
			map.put("nom", group.getGroupName().toLowerCase());
			Group g= dao.executeNameQuery("group.findByName", map);
	    		if(g == null) // n'existe pas
	    			return false;
	    		else //existe
	    			if(g.getIdGroup().equals(group.getIdGroup()))// appartient au meme utilisateur
	    				return false;
	    			return true;
	    	
	    }
	
	@Override
	public void saveAllRessourcesOfOneGroup(Group group)
			throws ApplicationException {
		EntityTransaction tr = getDao().getCurrentSession().getTransaction();
		try {			
				
			getDao().save(group);
			
			
			for(GroupAccessRessource groupAccessRessource: group.getRessources()){
				groupAccessRessourceDao.save(groupAccessRessource);
			}
			tr.commit();			
			getDao().getCurrentSession().flush();
		} catch (Exception e) {
			tr.rollback();			
			//e.printStackTrace();
			throw new ApplicationException("impossible de persiter la suite d'entit� relative � la facturation d'un fournisseur en base", e, 2);
			
		}
		
	}
	@Override
	public List<Group> listOfGroup4Role(Role role) {
		 Map<String, Object > mapParameter = new HashMap<String, Object>();
	   		mapParameter.put("identifier", role.getIdentifier()); 
	   		try {			
				return getDao().executeNameQueryAndGetListResult("role.Groups", mapParameter);
				
			} catch (Exception e) {
						
				//e.printStackTrace();
				throw new ApplicationException("Impossible de delete les groupes du role :", e, 2);
				
			}
		
	}
	@Transactional
	public void deleteVersusDesabled(Group group) throws ApplicationException {
		// TODO Auto-generated method stub
		getDao().deleteVersusDesabled(group, IConstance.FIELD_DELETE);
   //SUPPRIMER la relation dans la table d'aasociation	group et ressource
		//Query query =  getDao().getCurrentSession().createQuery(" update from  GroupAccessRessource ga  set "+IConstance.FIELD_DELETE+"='true' where ga.group.idGroup = :idgroupe");
		Query query =  getDao().getCurrentSession().createQuery(" delete from  GroupAccessRessource ga  where ga.group.idGroup = :idgroupe");
		
		query.setParameter("idgroupe", group.getIdGroup());			
		query.executeUpdate();
  //suppresion dans la table d'association role et groupe
		//Query query2 =  getDao().getCurrentSession().createQuery(" update from  RoleGroup rg set "+IConstance.FIELD_DELETE+"='true'  where rg.group.idGroup = :idgroupe");
		Query query2 =  getDao().getCurrentSession().createQuery(" delete from  RoleGroup rg  where rg.group.idGroup = :idgroupe");
		
		query2.setParameter("idgroupe", group.getIdGroup());			
		query2.executeUpdate();
		
	}
	
	@Override
	public IGroupDao getDao() {
		// TODO Auto-generated method stub
		return dao;
	}
	public void setDao(IGroupDao dao) {
		this.dao = dao;
	}
	

	public IGroupAccessRessourceDao getGroupAccessRessourceDao() {
		return groupAccessRessourceDao;
	}
	public void setGroupAccessRessourceDao(
			IGroupAccessRessourceDao groupAccessRessourceDao) {
		this.groupAccessRessourceDao = groupAccessRessourceDao;
	}
	public IRoleGroupDao getRoleGroupDao() {
		return roleGroupDao;
	}
	public void setRoleGroupDao(IRoleGroupDao roleGroupDao) {
		this.roleGroupDao = roleGroupDao;
	}
	
	
	
}
