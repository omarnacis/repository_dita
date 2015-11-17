/**
 * 
 */
package cm.dita.service.domaine.impl.user;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;















import javax.faces.context.FacesContext;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cm.dita.constant.IConstance;
import cm.dita.constant.ISessionConstant;
import cm.dita.dao.domaine.inter.IMouchardDao;
import cm.dita.dao.domaine.inter.user.IRoleGroupDao;
import cm.dita.entities.Mouchard;
import cm.dita.entities.user.Group;
import cm.dita.entities.user.Role;
import cm.dita.entities.user.RoleGroup;
import cm.dita.entities.user.User;
import cm.dita.exception.ApplicationException;
import cm.dita.service.domaine.inter.user.IRoleGroupService;
import cm.dita.service.generic.ServiceBaseImpl;


/**
 * @author bruno
 *
 */
public class RoleGroupServiceImpl extends ServiceBaseImpl<RoleGroup> implements
		IRoleGroupService {

	
	private IRoleGroupDao dao;
	private IMouchardDao mouchardDao;
	/* (non-Javadoc)
	 * @see cm.socogel.service.generic.IServiceBase#getDao()
	 */
	@Override
	public IRoleGroupDao getDao() {
		// TODO Auto-generated method stub
		return dao;
	}
	public void setDao(IRoleGroupDao dao) {
		this.dao = dao;
	}
	
	@Override
	public void deleteAllGroupOfRole(String identifier)
			throws ApplicationException {
		EntityTransaction tr = getDao().getCurrentSession().getTransaction();
		try {			
			Query query =  getDao().getCurrentSession().createQuery(" select grpRole from  RoleGroup grpRole where grpRole.role.identifier = :idRole");
			query.setParameter("idRole", identifier);
			
			List<RoleGroup> listResult = query.getResultList();
			
			if(!listResult.isEmpty()){
				for(RoleGroup roleGroup: listResult){
					getDao().delete(roleGroup);
				}
			}
			
			tr.commit();
			getDao().getCurrentSession().flush();
		} catch (Exception e) {
			tr.rollback();
			throw new ApplicationException("Problème lors de la suppression de la liste de "+getDao().getOMClass().getSimpleName(), e, 11);
		}	
	}
	
	
	@Override
	public void saveAll(Set<RoleGroup> groups) throws ApplicationException {
		EntityTransaction tr = getDao().getCurrentSession().getTransaction();
		try {
			for(RoleGroup roleGroup : groups){	
				
				getDao().save(roleGroup);
			}
			tr.commit();
		} catch (Exception e) {
			tr.rollback();
			throw new ApplicationException("Problème lors du persist de la liste de "+getDao().getOMClass().getSimpleName(), e, 11);
		}
		
	}
	
	/**
	 * @see Enregistre les groupes d'un role
	 * @param role
	 * @param groupes
	 * 
	 */
	@Transactional //(propagation=Propagation.NOT_SUPPORTED,rollbackFor=Exception.class)
	public void deleteGroup2Role(Role role) {
		// TODO Auto-generated method stub
		
		Group group=new Group();
		   HttpServletRequest req=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	    	HttpSession httpSession = req.getSession();
	    	httpSession.getAttribute(ISessionConstant.SS_USER);
		
		 Map<String, Object > mapParameter = new HashMap<String, Object>();
	   	 mapParameter.put("idRole", role.getIdentifier()); 		
		
	   	 
		try{
			Query query =  getDao().getCurrentSession().createQuery(" select grpRole from  RoleGroup grpRole where grpRole.role.identifier = :idRole");
			query.setParameter("idRole", role.getIdentifier());			
			List<RoleGroup> listResult = query.getResultList();			
			if(!listResult.isEmpty()){
				for(RoleGroup roleGroup: listResult){
					group=roleGroup.getGroup();
					getDao().delete(roleGroup);
					
					SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2); 
		 			 Mouchard mouchard = new Mouchard();
		 			 mouchard.setDelate(false);
		 			 mouchard.setMouchardTache("Retrait du groupe :"+group.getGroupName()+"("
		 					+group.getIdGroup()+") du rôle "+role.getRoleName()+" "
		 					+"("+role.getIdentifier()+")" );
		 			 mouchard.setMouchardUserCode((User) httpSession.getAttribute(ISessionConstant.SS_USER));
		 			 mouchard.setMouchardDate(sdf.format(new Date()));
		 			
		 			 mouchard.setEntite_name("RoleGroup");
		 			 mouchard.setOperation("suppression");
		 			 mouchard.setReference_date(role.getDateUseToSortData());
		 			 mouchardDao.save(mouchard);
				}
			}
			
		}catch(Exception e){
			SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2); 
			 Mouchard mouchard = new Mouchard();
			 mouchard.setDelate(false);
			 mouchard.setMouchardTache("Echec retrait du groupe :"+group.getGroupName()+"("
					+group.getIdGroup()+") du rôle "+role.getRoleName()+" "
					+"("+role.getIdentifier()+")" );
			 mouchard.setMouchardUserCode((User) httpSession.getAttribute(ISessionConstant.SS_USER));
			 mouchard.setMouchardDate(sdf.format(new Date()));
			
			 mouchard.setEntite_name("RoleGroup");
			 mouchard.setOperation("suppression");
			 mouchard.setReference_date(role.getDateUseToSortData());
			 mouchardDao.save(mouchard);
			//e.printStackTrace();
		}
		
	}
	@Transactional //(propagation=Propagation.NOT_SUPPORTED,rollbackFor=Exception.class)
	public void saveGroup2Role(Role role, List<Group> seletedGroups) {
		   Group group=new Group();
		   HttpServletRequest req=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	    	HttpSession httpSession = req.getSession();
	    	httpSession.getAttribute(ISessionConstant.SS_USER);
		try{
			//SUPPRIMER LES ANCIENS GROUPES	
			Query query =  getDao().getCurrentSession().createQuery(" select grpRole from  RoleGroup grpRole where grpRole.role.identifier = :idRole");
			query.setParameter("idRole", role.getIdentifier());			
			List<RoleGroup> listResult = query.getResultList();			
			if(!listResult.isEmpty()){
				for(RoleGroup roleGroup: listResult){
					group=roleGroup.getGroup();
					getDao().delete(roleGroup);
					
					SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2); 
		 			 Mouchard mouchard = new Mouchard();
		 			 mouchard.setDelate(false);
		 			 mouchard.setMouchardTache("Retrait du groupe :"+group.getGroupName()+"("
		 					+group.getIdGroup()+") du rôle "+role.getRoleName()+" "
		 					+"("+role.getIdentifier()+")" );
		 			 mouchard.setMouchardUserCode((User) httpSession.getAttribute(ISessionConstant.SS_USER));
		 			 mouchard.setMouchardDate(sdf.format(new Date()));
		 			
		 			 mouchard.setEntite_name("RoleGroup");
		 			 mouchard.setOperation("suppression");
		 			 mouchard.setReference_date(role.getDateUseToSortData());
		 			 mouchardDao.save(mouchard);
				}
			}
			
			
			for(int i=0;i<seletedGroups.size();i++){
				group=seletedGroups.get(i);
	    		  getDao().save(new RoleGroup(role, seletedGroups.get(i)));
	    		  
	    		  SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2); 
		 			 Mouchard mouchard = new Mouchard();
		 			 mouchard.setDelate(false);
		 			 mouchard.setMouchardTache("Attribution du groupe :"+group.getGroupName()+"("
		 					+group.getIdGroup()+") au rôle "+role.getRoleName()+" "
		 					+"("+role.getIdentifier()+")" );
		 			 mouchard.setMouchardUserCode((User) httpSession.getAttribute(ISessionConstant.SS_USER));
		 			 mouchard.setMouchardDate(sdf.format(new Date()));
		 			
		 			 mouchard.setEntite_name("RoleGroup");
		 			 mouchard.setOperation("suppression");
		 			 mouchard.setReference_date(role.getDateUseToSortData());
		 			 mouchardDao.save(mouchard);
	    	   }
			
		}catch(Exception e){
			
			SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2); 
			 Mouchard mouchard = new Mouchard();
			 mouchard.setDelate(false);
			 mouchard.setMouchardTache("Echec attribution ou retrait du groupe :"+group.getGroupName()+"("
					+group.getIdGroup()+") au rôle "+role.getRoleName()+" "
					+"("+role.getIdentifier()+")" );
			 mouchard.setMouchardUserCode((User) httpSession.getAttribute(ISessionConstant.SS_USER));
			 mouchard.setMouchardDate(sdf.format(new Date()));
			
			 mouchard.setEntite_name("RoleGroup");
			 mouchard.setOperation("suppression");
			 mouchard.setReference_date(role.getDateUseToSortData());
			 mouchardDao.save(mouchard);
			//e.printStackTrace();
		}
		
	}
	public IMouchardDao getMouchardDao() {
		return mouchardDao;
	}
	public void setMouchardDao(IMouchardDao mouchardDao) {
		this.mouchardDao = mouchardDao;
	}
	
	

}
