/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.dita.service.domaine.impl.user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;










import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import cm.dita.constant.IConstance;
import cm.dita.constant.ISessionConstant;
import cm.dita.dao.domaine.inter.IMouchardDao;
import cm.dita.dao.domaine.inter.user.IAccessRessourceDao;
import cm.dita.dao.domaine.inter.user.IUserDao;
import cm.dita.entities.Mouchard;
import cm.dita.entities.Personne;
import cm.dita.entities.user.AccessRessource;
import cm.dita.entities.user.RoleUser;
import cm.dita.entities.user.User;
import cm.dita.exception.ApplicationException;
import cm.dita.service.domaine.inter.user.IAccessRessourceService;
import cm.dita.service.domaine.inter.user.IUserService;
import cm.dita.service.generic.ServiceBaseImpl;
import cm.dita.utils.Messages;
//import cm.dita.utils.OperRolesSpringSecurity;



/**
 * UserDetailsService est une classe de spring security
 * @author bruno
 */
public class UserServiceImpl extends ServiceBaseImpl< User> implements IUserService , UserDetailsService {
	private static final Logger LOG = Logger.getLogger(UserServiceImpl.class);
   
	private IUserDao dao;
    private IAccessRessourceDao accessRessourceDao;
    private IMouchardDao mouchardDao;
    //private IAccesProjet_roles0_projetDao accesprojet_roles0_projetDao;
    
    public static Map<String,Object> countries;
	static{
		countries = new LinkedHashMap<String,Object>();
		countries.put("en", Locale.ENGLISH); //label, value
		countries.put("fr", Locale.FRENCH);
	}
    
       /**
     * l'encodeur de mot de passe injecte par spring
     */
    private PasswordEncoder passEncoder;
 
    /**
     * Objet qui permet de faire un "salt" sur le mot de passe avant de <br/>
     * le hasher. Cela ajout un niveau de sécurité en rendant plus difficiles
     * les attaque<br/>
     * par dictionnaire.
     */
    private SaltSource saltSource;
    

    @Override
    public IUserDao getDao() {
        return dao;
    }

    public void setDao(IUserDao dao) {
        this.dao = dao;
    }
    
    


	@Override
	public void saveUser(User user) throws ApplicationException {
	
    	try {   
    		dao.save(user);
    		   
    	} catch (Exception e) {
			
			throw new ApplicationException("impossible de persiter l'entit�t� ["+getDao().getOMClass().getSimpleName()+ "] en base", e, 2);
		}
	}

	/*@Override
	public User update(User entity) {
		// TODO Auto-generated method stub
		return null;
	}*/
	
// verifie l'existance d'un login
	@Override
   public  boolean userExiste(User user){    	
	   Map<String , Object> map=new Hashtable<String,Object>();
		map.put("login", user.getLogin().toLowerCase());
		User user1= dao.executeNameQuery("User.findByLogin", map);
		//JOptionPane.showMessageDialog(null, user1.getId()==user.getId());
    		if(user1 == null) // n'existe pas
    			return false;
    		else //existe
    			if(user1.getId()==user.getId())// appartient au meme utilisateur
    				return false;
    		
    			return true;
    	
    }
   
   @Override
	public int getCount() {
		// TODO Auto-generated method stub
		return getDao().getCount();
	}

	/**
	 * @see Retourne un utilisateur connaissant sont login
	 * @param login 
	 * @return User
	 * 
	 */
	public User findByLogin(String login) {
		
			Map<String , Object> map=new Hashtable<String,Object>();
			map.put("login", login);
			
			return getDao().executeNameQuery("User.findByLogin", map);
			
	}
	
	
	public User findByIdPersonne(Personne personne) {
		
		Map<String , Object> map=new Hashtable<String,Object>();
		map.put("persid", personne.getPersid());
		
		return getDao().executeNameQuery("User.findByIdPersonne", map);
		
}
	
	/**
	 * Change le mot de passe de l'administrateur
	 * @param password
	 */
	public void updatePasswordAdmin(String password) {
		
	Query query =getDao().getCurrentSession().createQuery("update User u set u.password = :pass where u.autorithies = 'true'").setParameter("pass", password);
	getDao().getCurrentSession().flush();
	query.executeUpdate();
		
}
	
	@Override
	public Integer getCountUserParEspace(int espace) {
		// TODO Auto-generated method
		return ((Long) dao.getCurrentSession().createQuery("select count(*) from User u where u.espace.id=:espace and u.delate='false'").setParameter("espace", espace).getResultList().get(0)).intValue();
   		
	}
	
	
	/**
	 * @see Redefinition de le methode de connection de springSecurity
	 * @return
	 */
	
	 @SuppressWarnings("deprecation")
		@Override
	    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String login) throws UsernameNotFoundException, DataAccessException {
		 
		 FacesContext context = FacesContext.getCurrentInstance();
	    	List<GrantedAuthorityImpl> authorities = new ArrayList<GrantedAuthorityImpl>();	
	    	
	    	Map<String , Object> map=new Hashtable<String,Object>();
			map.put("login", login);
			User user=null;
			try{
				user=dao.executeNameQuery("User.findByLogin", map);
			}catch(Exception e){
				HttpServletRequest req=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		    	HttpSession httpSession = req.getSession(true);
		    	httpSession.setAttribute("error", 0);
			}
			
	    	if(user == null){//cas ou l'utilisateur de login entr� n'existe pas en base
	    		
	    		HttpServletRequest req=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		    	HttpSession httpSession = req.getSession(true);
		    	httpSession.setAttribute("error", 1);
	    		
	    		/*FacesMessage message = Messages.getMessage("messages", "user.connexion.inconu", null);
	    		message.setSeverity(FacesMessage.SEVERITY_ERROR);
	   	     	context.addMessage("msgErreur", message); */
	   	     	
	   	     SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2); 
			 Mouchard mouchard = new Mouchard();
			 mouchard.setDelate(false);
			 mouchard.setMouchardTache("Tentative de connexion avec le login :"+login+" dans le système");
			 mouchard.setMouchardUserCode(null);
			 mouchard.setMouchardDate(sdf.format(new Date()));
			 mouchard.setEntite_name("user");
			 mouchard.setOperation("connexion");
			 mouchard.setReference_date(null);
			 mouchardDao.save(mouchard);
	   	    
	   	     	return null;
	    	}
	    	
	    
	    	if(!user.isEnabled()){//si l'utilisateur est desactiv
	    		HttpServletRequest req=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		    	HttpSession httpSession = req.getSession(true);
		    	httpSession.setAttribute("error", 2);
	    		//request.getSession().setAttribute("t", );
	    		FacesMessage message = Messages.getMessage("messages", "user.connexion.etat", null);
	    		message.setSeverity(FacesMessage.SEVERITY_ERROR);
	   	     	context.addMessage(null, message);
	   	     	
	   	    // throw new LockedException(error);
	   	     	
	   	     SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2); 
			 Mouchard mouchard = new Mouchard();
			 mouchard.setDelate(false);
			 mouchard.setMouchardTache("Tentative de connexion  "+user.getInfosPersonne().getPersnom()+" "+user.getInfosPersonne().getPersprenom()+"("
					+user.getLogin()+") dans le systéme avec un compte desactiv�");
			 mouchard.setMouchardUserCode(null);
			 mouchard.setMouchardDate(sdf.format(new Date()));
			 mouchard.setEntite_name("user");
			 mouchard.setOperation("connexion");
			 mouchard.setReference_date(user.getDateUseToSortData());
			 mouchardDao.save(mouchard);
	   	     	
	   	     	return null;
			   
	    	}
	    	
	    	Map<String , Object> parameters = new HashMap<String, Object>();
			parameters.put("idUser", user.getId());
	    
			//recup�ration des roles de l'user
	    	 try{ 
	    		if(user!=null){
		    		if(user.isAutorithies()){ //JE SUIS LE SUPER ADMIN
		    			authorities.add(new GrantedAuthorityImpl("ROLE_ADMIN")) ;
		    		}else{
			    		 Map<String, Object > mapParameter = new HashMap<String, Object>();
			    	   		mapParameter.put("identifier", user.getId()); 
			    	   		
			    	   //recup�ration et chargement de ses privil�ges sur l'application		
			    		 List<AccessRessource> accessRessources= accessRessourceDao.executeNameQueryAndGetListResult("User.access", mapParameter);
			    		 for(AccessRessource ressource : accessRessources){
					    		authorities.add(new GrantedAuthorityImpl(ressource.getRessourceName()));		    		 
					    	  }
			    		 
			    		
		    		}
		    		
		    		
	    		}
	    		
	    		 
	    		// authorities.add(new GrantedAuthorityImpl("ROLE_ADMIN")) ;

	    	 }catch(Exception e){
	    		 HttpServletRequest req=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			    	HttpSession httpSession = req.getSession(true);
			    	httpSession.setAttribute("error", 0);
	    	 }
		
	    	  Collection<? extends GrantedAuthority> grantedAuthority;    	  
	    	  grantedAuthority = authorities;
	    	
	    	org.springframework.security.core.userdetails.User userNew=  new org.springframework.security.core.userdetails.User(login, user.getPassword(), user.isEnabled(), true, true, true,
	      			grantedAuthority); 
	    	
	    	
		    	HttpServletRequest req=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		    	HttpSession httpSession = req.getSession(true);
		    	user.setEspaceCourantId(1);//par defaut tout utilisateur se retrouve dans l'espace initial de l'application qui est consid�r� ici coe l'espace par defaut
		    	httpSession.setAttribute(ISessionConstant.SS_USER, user);
		    	//JOptionPane.showMessageDialog(null, user.getInfosPersonne().getSrc_img()+" "+ user.getInfosPersonne().getPersnom());
		    	
		    	SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2); 
				 Mouchard mouchard = new Mouchard();
				 mouchard.setDelate(false);
				 mouchard.setMouchardTache("Connexion de "+user.getInfosPersonne().getPersnom()+" "+user.getInfosPersonne().getPersprenom()+"("
						+user.getLogin()+") dans le syst�me");
				 mouchard.setMouchardUserCode((User) httpSession.getAttribute(ISessionConstant.SS_USER));
				 mouchard.setMouchardDate(sdf.format(new Date()));
				 mouchard.setEntite_name("user");
				 mouchard.setOperation("connexion");
				 mouchard.setReference_date(user.getDateUseToSortData());
				
					 mouchardDao.save(mouchard);
				
	    	    	
	    	return userNew;
	    }

	
	
	
	public PasswordEncoder getPassEncoder() {
		return passEncoder;
	}

	public SaltSource getSaltSource() {
		return saltSource;
	}

	
    
    public void setPassEncoder(PasswordEncoder passEncoder) {
        this.passEncoder = passEncoder;
    }
 
    public void setSaltSource(SaltSource saltSource) {
        this.saltSource = saltSource;
    }   
    

	public IAccessRessourceDao getAccessRessourceDao() {
		return accessRessourceDao;
	}

	public void setAccessRessourceDao(IAccessRessourceDao accessRessourceDao) {
		this.accessRessourceDao = accessRessourceDao;
	}
	
	

	
	public IMouchardDao getMouchardDao() {
		return mouchardDao;
	}

	public void setMouchardDao(IMouchardDao mouchardDao) {
		this.mouchardDao = mouchardDao;
	}

	@Override
	public User getUser(int id) {
		// TODO Auto-generated method stub
		return null;
	}   
	@Override
	public List<User> listUserParEspace(int espace) {
		// TODO Auto-generated method stub
		List<User> listUser = getDao().getCurrentSession().createQuery("select e from User e where e.espace.id=:espace and e.delate='false'").setParameter("espace", espace).getResultList();
   		return listUser;
	}

	

 
}
