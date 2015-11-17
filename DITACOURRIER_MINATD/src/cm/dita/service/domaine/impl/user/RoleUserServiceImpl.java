/**
 * 
 */
package cm.dita.service.domaine.impl.user;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;

import org.springframework.transaction.annotation.Transactional;

import cm.dita.constant.IConstance;
import cm.dita.constant.ISessionConstant;
import cm.dita.dao.domaine.inter.IMouchardDao;
import cm.dita.dao.domaine.inter.user.IRoleUserDao;
import cm.dita.entities.Mouchard;
import cm.dita.entities.user.GroupAccessRessource;
import cm.dita.entities.user.Role;
import cm.dita.entities.user.RoleUser;
import cm.dita.entities.user.User;
import cm.dita.exception.ApplicationException;
import cm.dita.service.domaine.inter.user.IRoleService;
import cm.dita.service.domaine.inter.user.IRoleUserService;
import cm.dita.service.generic.ServiceBaseImpl;



/**
 * @author bruno
 *
 */
public class RoleUserServiceImpl extends  ServiceBaseImpl<RoleUser> implements 
		IRoleUserService {
	
		private IRoleUserDao dao;
		private IMouchardDao mouchardDao;
	
	 	@Override
	    public  IRoleUserDao getDao(){
	 		return dao;
	 	}
	 	
	 	
	 
	    public void setDao(IRoleUserDao dao) {
			this.dao = dao;
		}



		@Override
		public void saveAll(Set<RoleUser> roles) throws ApplicationException {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void deleteAllRoleOfUser(String identifier)
				throws ApplicationException {
			// TODO Auto-generated method stub
			
		}



		@Transactional
		public void saveRole2User(User user, List<Role> seletedRole) {
			// TODO Auto-generated method stub
			   Role role=new Role();
			   HttpServletRequest req=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		    	HttpSession httpSession = req.getSession();
		    	httpSession.getAttribute(ISessionConstant.SS_USER);
			try{
				//SUPPRIMER LES ANCIENS GROUPES	
				Query query =  getDao().getCurrentSession().createQuery(" select ru from  RoleUser ru  where ru.user.id = :iduser");
				query.setParameter("iduser", user.getId());			
				List<RoleUser> listResult = query.getResultList();	
				
				if(!listResult.isEmpty()){
					for(RoleUser roleUser: listResult){
						role=roleUser.getRole();
						getDao().delete(roleUser);
						
						SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2); 
			 			 Mouchard mouchard = new Mouchard();
			 			 mouchard.setDelate(false);
			 			 mouchard.setMouchardTache("Retrait du rôle :"+roleUser.getRole().getRoleName()+"("
			 					+roleUser.getRole().getIdentifier()+") à l'utilisateur "+user.getInfosPersonne().getNom()+" "
			 					+user.getInfosPersonne().getPrenom()+"("+user.getLogin()+")" );
			 			 mouchard.setMouchardUserCode((User) httpSession.getAttribute(ISessionConstant.SS_USER));
			 			 mouchard.setMouchardDate(sdf.format(new Date()));
			 			
			 			 mouchard.setEntite_name("RoleUser");
			 			 mouchard.setOperation("suppression");
			 			 mouchard.setReference_date(user.getDateUseToSortData());
			 			 mouchardDao.save(mouchard);
					}
				}
				
				
				for(int i=0;i<seletedRole.size();i++){
		    		  getDao().save(new RoleUser(user,seletedRole.get(i)));
		    		   role=seletedRole.get(i);
		    		  
		    		   SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2); 
		 			 Mouchard mouchard = new Mouchard();
		 			 mouchard.setDelate(false);
		 			 mouchard.setMouchardTache("Attribution du rôle :"+seletedRole.get(i).getRoleName()+"("
		 					+seletedRole.get(i).getIdentifier()+") à l'utlisateur "+user.getInfosPersonne().getNom()+" "
		 					+user.getInfosPersonne().getPrenom()+"("+user.getLogin()+")" );
		 			mouchard.setMouchardUserCode((User) httpSession.getAttribute(ISessionConstant.SS_USER));
		 			 mouchard.setMouchardDate(sdf.format(new Date()));
		 			 mouchard.setEntite_name("RoleUser");
		 			 mouchard.setOperation("ajout");
		 			 mouchard.setReference_date(user.getDateUseToSortData());
		 			 mouchardDao.save(mouchard);
		    	   }
				
			}catch(Exception e){
				
				SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2); 
	 			 Mouchard mouchard = new Mouchard();
	 			 mouchard.setDelate(false);
	 			 mouchard.setMouchardTache("Echec d'Attribution ou retrait du rôle :"+role.getRoleName()+"("
	 					+role.getIdentifier()+") à  l'utlisateur "+user.getInfosPersonne().getNom()+" "
	 					+user.getInfosPersonne().getPrenom()+"("+user.getLogin()+")" );
	 			mouchard.setMouchardUserCode((User) httpSession.getAttribute(ISessionConstant.SS_USER));
	 			 mouchard.setMouchardDate(sdf.format(new Date()));
	 			 mouchard.setEntite_name("RoleUser");
	 			 mouchard.setOperation("ajout");
	 			 mouchard.setReference_date(user.getDateUseToSortData());
	 			 mouchardDao.save(mouchard);
				//e.printStackTrace();
			}
			
		}



		@Transactional
		public void deleteRole2User(User user) {
			// TODO Auto-generated method stub
			Role role =new Role();
			HttpServletRequest req=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	    	HttpSession httpSession = req.getSession();
	    	httpSession.getAttribute(ISessionConstant.SS_USER);
			
			try{
				//SUPPRIMER LES ANCIENS GROUPES	
				Query query =  getDao().getCurrentSession().createQuery(" select ru from  RoleUser ru where ru.user.id = :iduser");
				query.setParameter("iduser", user.getId());			
				List<RoleUser> listResult = query.getResultList();			
				if(!listResult.isEmpty()){
					for(RoleUser roleUser: listResult){
						 role = roleUser.getRole();
						getDao().delete(roleUser);
						
						SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2); 
			 			 Mouchard mouchard = new Mouchard();
			 			 mouchard.setDelate(false);
			 			 mouchard.setMouchardTache("Retrait du rôle :"+role.getRoleName()+"("
			 					+role.getIdentifier()+") à  l'utilisateur "+user.getInfosPersonne().getNom()+" "
			 					+user.getInfosPersonne().getPrenom()+"("+user.getLogin()+")" );
			 			 mouchard.setMouchardUserCode((User) httpSession.getAttribute(ISessionConstant.SS_USER));
			 			 mouchard.setMouchardDate(sdf.format(new Date()));
			 			
			 			 mouchard.setEntite_name("RoleUser");
			 			 mouchard.setOperation("suppression");
			 			 mouchard.setReference_date(user.getDateUseToSortData());
			 			 mouchardDao.save(mouchard);
					}
				}
				
				
			}catch(Exception e){
				
				SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2); 
	 			 Mouchard mouchard = new Mouchard();
	 			 mouchard.setDelate(false);
	 			 mouchard.setMouchardTache("Echec Retrait du rôle: "+role.getRoleName()+"("
	 					+role.getIdentifier()+") à l'utilisateur "+user.getInfosPersonne().getNom()+" "
	 					+user.getInfosPersonne().getPrenom()+"("+user.getLogin()+")" );
	 			 mouchard.setMouchardUserCode((User) httpSession.getAttribute(ISessionConstant.SS_USER));
	 			 mouchard.setMouchardDate(sdf.format(new Date()));
	 			
	 			 mouchard.setEntite_name("RoleUser");
	 			 mouchard.setOperation("suppression");
	 			 mouchard.setReference_date(user.getDateUseToSortData());
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
