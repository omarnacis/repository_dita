package cm.dita.service.domaine.impl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import cm.dita.service.generic.ServiceBaseImpl;
import cm.dita.constant.IConstance;
import cm.dita.constant.ISessionConstant;
import cm.dita.dao.domaine.inter.IMouchardDao;
import cm.dita.dao.generic.IDaoBase;
import cm.dita.entities.Mouchard;
import cm.dita.entities.user.User;
import cm.dita.service.domaine.inter.IMouchardRessourceService;
import cm.dita.service.domaine.inter.user.IUserService;

/**
 * @author Omar Nacis
 *
 */

public class MouchardRessourceServiceImpl  extends ServiceBaseImpl<Mouchard>  implements IMouchardRessourceService{
	
	private static final long serialVersionUID = 1L;
	private IMouchardDao dao;
	
	@ManagedProperty(value="#{userService}")
	private IUserService userService;
	/**
	 * @return the dao
	 */
	public IMouchardDao getDao() {
		return dao;
	}
	/**
	 * @param dao the dao to set
	 */
	public void setDao(IMouchardDao dao) {
		this.dao = dao;
	}
	
	//@Transactional
	public void tracage(String message , User userconnected) {
		// TODO Auto-generated method stub
		try {
			 SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2); 
			 Mouchard mouchard = new Mouchard();
			 mouchard.setDelate(false);
			 mouchard.setMouchardTache(message);
			 mouchard.setMouchardUserCode(userconnected);
			 mouchard.setMouchardDate(sdf.format(new Date()));
			 dao.save(mouchard);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}		
	}
	
	//@Transactional(noRollbackFor=Exception.class)
	public void tracage(String message ,String operation,Long ref_date,String entite) {
		// TODO Auto-generated method stub
		try {
				 HttpServletRequest req=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		    	 HttpSession httpSession = req.getSession();
		    	 httpSession.getAttribute(ISessionConstant.SS_USER);
			
				 SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2); 
				 Mouchard mouchard = new Mouchard();
				 mouchard.setDelate(false);
				 mouchard.setMouchardTache(message);
				 mouchard.setMouchardUserCode((User) httpSession.getAttribute(ISessionConstant.SS_USER));
				 mouchard.setMouchardDate(sdf.format(new Date()));
				 mouchard.setEntite_name(entite);
				 mouchard.setOperation(operation);
				 mouchard.setReference_date(ref_date);
				 dao.save(mouchard);
			
		} catch ( Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}		
	}
	
	@Override
	public void mouchard(String message, String operation, Long ref_date,
			String entite,long code_id,String valeur) {
		 HttpServletRequest req=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
	    	HttpSession httpSession = req.getSession();
	    	httpSession.getAttribute(ISessionConstant.SS_USER);
		
		 SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2); 
		 Mouchard mouchard = new Mouchard();
		 mouchard.setDelate(false);
		 mouchard.setMouchardTache(message);
		 mouchard.setMouchardUserCode((User) httpSession.getAttribute(ISessionConstant.SS_USER));
		 mouchard.setMouchardDate(sdf.format(new Date()));
		 mouchard.setValeur(valeur);
		 mouchard.setOperation(operation);
		 mouchard.setReference_date(ref_date);
		 mouchard.setEntite_name(entite);
		 mouchard.setCode_id(code_id);
		
		 dao.save(mouchard);
		
	}

}
