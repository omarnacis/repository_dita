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
import javax.swing.JOptionPane;












import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cm.dita.constant.IConstance;
import cm.dita.dao.domaine.inter.user.IGroupDao;
import cm.dita.dao.domaine.inter.user.IRoleDao;
import cm.dita.dao.domaine.inter.user.IRoleGroupDao;
import cm.dita.dao.domaine.inter.user.IRoleUserDao;
import cm.dita.entities.user.AccessRessource;
import cm.dita.entities.user.Group;
import cm.dita.entities.user.Role;
import cm.dita.entities.user.RoleGroup;
import cm.dita.entities.user.User;
import cm.dita.exception.ApplicationException;
import cm.dita.service.domaine.inter.user.IRoleService;
import cm.dita.service.generic.ServiceBaseImpl;


/**
 * @author bruno
 *
 */

public class RoleServiceImpl extends ServiceBaseImpl<Role> implements IRoleService{

	private IRoleDao dao;
	private IRoleGroupDao roleGroupDao;
	private IRoleUserDao roleUserDao;
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return getDao().getCount();
	}
	

	public  boolean userExiste(Role role){
    	
		   Map<String , Object> map=new Hashtable<String,Object>();
			map.put("nomRole", role.getRoleName().toLowerCase());
			Role r= dao.executeNameQuery("role.findByName", map);
			
	    		if(r == null) // n'existe pas
	    			return false;
	    		else //existe
	    			
	    			if(r.getIdentifier().equals(role.getIdentifier()))// appartient au meme utilisateur
	    				return false;
	    			return true;
	    	
	    }

	@Transactional
	public void saveRoleWithGroups(Role role) throws ApplicationException {
	
	
		try {			
			
						
			for(RoleGroup roleGroup: role.getGroups()){
				//JOptionPane.showMessageDialog(null, roleGroup.getIdCompositeRoleGroup().getGroupId()+" "+roleGroup.getIdCompositeRoleGroup().getRoleId());
				roleGroupDao.save(roleGroup);
			}
			//getDao().update(role);
		
		} catch (Exception e) {
				
			e.printStackTrace();
			throw new ApplicationException("impossible de persiter la suite d'entité relative a role groups", e, 2);
			
		}
		
	}
	
	@Transactional
	public  void deleteGroupRole(String identifier){
    	
   	 Map<String, Object > mapParameter = new HashMap<String, Object>();
   		mapParameter.put("identifier", identifier); 
   		try {			
			roleGroupDao.executeNameQueryUpdate("role.deleteGroup", mapParameter);
			
		} catch (Exception e) {
					
			//e.printStackTrace();
			throw new ApplicationException("Impossible de delete les groupes du role :", e, 2);
			
		}
   		
   	
   }
	
	@Override
	public List<Role> listOfRole4User(User user) {
		 Map<String, Object > mapParameter = new HashMap<String, Object>();
	   		mapParameter.put("identifier", user.getId()); 
	   		try {			
				return getDao().executeNameQueryAndGetListResult("User.roles", mapParameter);
				
			} catch (Exception e) {
						
				//e.printStackTrace();
				throw new ApplicationException("Impossible de delete les groupes du role :", e, 2);
				
			}
		
	}
	
	@Transactional
	public void deleteVersusDesabled(Role role) throws ApplicationException {
		// TODO Auto-generated method stub
		getDao().deleteVersusDesabled(role, IConstance.FIELD_DELETE);
   //SUPPRIMER la relation dans la table d'asociation	role et user
		Query query =  getDao().getCurrentSession().createQuery(" delete from  RoleUser ru where ru.role.identifier = :id");
		query.setParameter("id", role.getIdentifier());			
		query.executeUpdate();
  //suppresion dans la table d'association role et groupe
		Query query2 =  getDao().getCurrentSession().createQuery(" delete from  RoleGroup rg where rg.role.identifier = :id");
		query2.setParameter("id", role.getIdentifier());			
		query2.executeUpdate();
		
	}
	
	
	@Override
	public IRoleDao getDao() {		
		return dao;
	}

	public void setDao(IRoleDao dao) {
		this.dao = dao;
	}

	public IRoleGroupDao getRoleGroupDao() {
		return roleGroupDao;
	}

	public void setRoleGroupDao(IRoleGroupDao roleGroupDao) {
		this.roleGroupDao = roleGroupDao;
	}

	public IRoleUserDao getRoleUserDao() {
		return roleUserDao;
	}

	public void setRoleUserDao(IRoleUserDao roleUserDao) {
		this.roleUserDao = roleUserDao;
	}
	
	


	
		
}
