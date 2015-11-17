/**
 * 
 */
package cm.dita.service.domaine.impl.user;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.transaction.annotation.Transactional;

import cm.dita.constant.IConstance;
import cm.dita.constant.ISessionConstant;
import cm.dita.dao.domaine.inter.IMouchardDao;
import cm.dita.dao.domaine.inter.user.IGroupAccessRessourceDao;
import cm.dita.entities.Mouchard;
import cm.dita.entities.user.AccessRessource;
import cm.dita.entities.user.Group;
import cm.dita.entities.user.GroupAccessRessource;
import cm.dita.entities.user.Role;
import cm.dita.entities.user.RoleGroup;
import cm.dita.entities.user.User;
import cm.dita.exception.ApplicationException;
import cm.dita.service.domaine.inter.user.IGroupAccessRessourceService;
import cm.dita.service.generic.ServiceBaseImpl;



/**
 * @author bruno
 *
 */
public class GroupAccessRessourceServiceImpl extends
		ServiceBaseImpl<GroupAccessRessource> implements
		IGroupAccessRessourceService {

	
	private IGroupAccessRessourceDao dao;
	private IMouchardDao mouchardDao;
	/* (non-Javadoc)
	 * @see cm.socogel.service.generic.IServiceBase#getDao()
	 */
	@Override
	public IGroupAccessRessourceDao getDao() {
		// TODO Auto-generated method stub
		return dao;
	}
	public void setDao(IGroupAccessRessourceDao dao) {
		this.dao = dao;
	}
	
	@Override
	public void saveAll(Set<GroupAccessRessource> groupAccessRessources)
			throws ApplicationException {
		EntityTransaction tr = getDao().getCurrentSession().getTransaction();
		
		try {
			
			for(GroupAccessRessource groupAccessRessource : groupAccessRessources){				
				getDao().save(groupAccessRessource);
			}
			tr.commit();
		} catch (Exception e) {
			tr.rollback();
			throw new ApplicationException("Problème lors du persist de la liste de "+getDao().getOMClass().getSimpleName(), e, 11);
		}
		
	}
	
	
	
	@Transactional //(propagation=Propagation.NOT_SUPPORTED,rollbackFor=Exception.class)
	public void saveAcess2Group(Group group, List<AccessRessource> seletedAccess) {
		
			AccessRessource access=new AccessRessource();
		   HttpServletRequest req=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	    	HttpSession httpSession = req.getSession();
	    	httpSession.getAttribute(ISessionConstant.SS_USER);
		
		try{
			
			
			//SUPPRIMER LES ANCIENS GROUPES	
			Query query =  getDao().getCurrentSession().createQuery(" select ga from  GroupAccessRessource ga where ga.group.idGroup = :idgroupe");
			query.setParameter("idgroupe", group.getIdGroup());			
			List<GroupAccessRessource> listResult = query.getResultList();	
			group.getRessources().removeAll(listResult);
			if(!listResult.isEmpty()){
				for(GroupAccessRessource accessGroup: listResult){
					access=accessGroup.getAccessRessource();
					getDao().delete(accessGroup);
					
					SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2); 
		 			 Mouchard mouchard = new Mouchard();
		 			 mouchard.setDelate(false);
		 			 mouchard.setMouchardTache("Retrait du privilège :"+accessGroup.getAccessRessource().getRessourceDetail()+"("
		 					+accessGroup.getAccessRessource().getIdRessource()+") du groupe "+group.getGroupName()+" "
		 					+"("+group.getIdGroup()+")" );
		 			 mouchard.setMouchardUserCode((User) httpSession.getAttribute(ISessionConstant.SS_USER));
		 			 mouchard.setMouchardDate(sdf.format(new Date()));		 			
		 			 mouchard.setEntite_name("GroupeAccess");
		 			 mouchard.setOperation("suppression");
		 			 mouchard.setReference_date(group.getDateUseToSortData());
		 			 mouchardDao.save(mouchard);
				}
			}
			
			
			for(int i=0;i<seletedAccess.size();i++){
	    		  getDao().save(new GroupAccessRessource(seletedAccess.get(i),group));
	    		  access=seletedAccess.get(i);
	    		  
	    		  	SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2); 
		 			 Mouchard mouchard = new Mouchard();
		 			 mouchard.setDelate(false);
		 			 mouchard.setMouchardTache("Attribution du privilège :"+seletedAccess.get(i).getRessourceDetail()+"("
		 					+seletedAccess.get(i).getIdRessource()+") au groupe "+group.getGroupName()+" "
		 					+"("+group.getIdGroup()+")" );
		 			 mouchard.setMouchardUserCode((User) httpSession.getAttribute(ISessionConstant.SS_USER));
		 			 mouchard.setMouchardDate(sdf.format(new Date()));		 			
		 			 mouchard.setEntite_name("GroupeAccess");
		 			 mouchard.setOperation("ajout");
		 			 mouchard.setReference_date(group.getDateUseToSortData());
		 			 mouchardDao.save(mouchard);
	    	   }
			
		}catch(Exception e){
			
			SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2); 
			 Mouchard mouchard = new Mouchard();
			 mouchard.setDelate(false);
			 mouchard.setMouchardTache("Echec attribution ou retrait du privilège :"+access.getRessourceDetail()+"("
					+access.getIdRessource()+") du groupe "+group.getGroupName()+" "
					+"("+group.getIdGroup()+")" );
			 mouchard.setMouchardUserCode((User) httpSession.getAttribute(ISessionConstant.SS_USER));
			 mouchard.setMouchardDate(sdf.format(new Date()));		 			
			 mouchard.setEntite_name("GroupeAccess");
			 mouchard.setOperation("ajout");
			 mouchard.setReference_date(group.getDateUseToSortData());
			 mouchardDao.save(mouchard);
	
			//e.printStackTrace();
		}
		
	}
	@Transactional
	public void deleteAccess2Group(Group group) {
		// TODO Auto-generated method stub
		AccessRessource access=new AccessRessource();
		   HttpServletRequest req=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	    	HttpSession httpSession = req.getSession();
	    	httpSession.getAttribute(ISessionConstant.SS_USER);
		
		try {		
			Query query =  getDao().getCurrentSession().createQuery(" select gr from  GroupAccessRessource gr where gr.group.idGroup = :idGroup");
			query.setParameter("idGroup", group.getIdGroup());
			
			List<GroupAccessRessource> listResult = query.getResultList();
			
			if(!listResult.isEmpty()){
				for(GroupAccessRessource accessRessource: listResult){
					access=accessRessource.getAccessRessource();
					getDao().delete(accessRessource);
					
					SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2); 
					 Mouchard mouchard = new Mouchard();
					 mouchard.setDelate(false);
					 mouchard.setMouchardTache("Retrait du privilège :"+access.getRessourceDetail()+"("
							+access.getIdRessource()+") du groupe "+group.getGroupName()+" "
							+"("+group.getIdGroup()+")" );
					 mouchard.setMouchardUserCode((User) httpSession.getAttribute(ISessionConstant.SS_USER));
					 mouchard.setMouchardDate(sdf.format(new Date()));		 			
					 mouchard.setEntite_name("GroupeAccess");
					 mouchard.setOperation("ajout");
					 mouchard.setReference_date(group.getDateUseToSortData());
					 mouchardDao.save(mouchard);
				}
			}		
	}catch(Exception e){
		
		SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2); 
		 Mouchard mouchard = new Mouchard();
		 mouchard.setDelate(false);
		 mouchard.setMouchardTache("Echec retrait du privilège :"+access.getRessourceDetail()+"("
				+access.getIdRessource()+") du groupe "+group.getGroupName()+" "
				+"("+group.getIdGroup()+")" );
		 mouchard.setMouchardUserCode((User) httpSession.getAttribute(ISessionConstant.SS_USER));
		 mouchard.setMouchardDate(sdf.format(new Date()));		 			
		 mouchard.setEntite_name("GroupeAccess");
		 mouchard.setOperation("ajout");
		 mouchard.setReference_date(group.getDateUseToSortData());
		 mouchardDao.save(mouchard);
		
	}
	}
	public IMouchardDao getMouchardDao() {
		return mouchardDao;
	}
	public void setMouchardDao(IMouchardDao mouchardDao) {
		this.mouchardDao = mouchardDao;
	}
	
	

}
