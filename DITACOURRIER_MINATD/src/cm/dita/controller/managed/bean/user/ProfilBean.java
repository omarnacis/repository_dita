package cm.dita.controller.managed.bean.user;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
//import javax.swing.JOptionPane;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import cm.dita.beans.Sexe;
import cm.dita.constant.IConstance;
import cm.dita.entities.Espace;
import cm.dita.entities.Typespersonnel;
import cm.dita.entities.user.InfosPersonne;
import cm.dita.entities.user.User;
import cm.dita.service.domaine.inter.IMouchardRessourceService;
import cm.dita.service.domaine.inter.user.IUserService;
import cm.dita.utils.Messages;

@ManagedBean(name = "profilBean")
@SessionScoped
public class ProfilBean implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
			//Spring User Service is injected...
			@ManagedProperty(value="#{userService}")
			IUserService userService;
			
			@ManagedProperty(value="#{mouchardRessourceService}")
			private IMouchardRessourceService mouchardRessourceService;
			
	private User user;
	private User user_tmp;
	private String  lastPassword;
	private String lastLogin;
	//POUR CHANGER LES MOTS DE PASSE
		private String  password;
		private String password2;
		private String password3;
	
	
		List<Typespersonnel> listType;
		List<Sexe> listSexe;
	
	// modification du profil    
    public void profilEvent(ActionEvent actionEvent){
    	
    	
    		UserDetails user_secutity = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		user =userService.findByLogin(user_secutity.getUsername());
    		
	     
         if(this.user!=null){	       
	        
	        setLastPassword(user.getPassword());
	        this.user=new User(userService.findByLogin(user_secutity.getUsername()));
	        
        }
         
         listSexe=Sexe.initialise();
    	
    }
    
    /**
	 * @see mise a jour
	 * 
	 * @param user
	 */
	public void update() {
   	 FacesContext context = FacesContext.getCurrentInstance();
   	try{
		    //verifi si l'user existe
		    	
			    	userService.update(user);    	
			    	mouchardRessourceService.tracage("Modification de son profil par :"+user.getInfosPersonne().getNom()+" "+user.getInfosPersonne().getPrenom(), "modification",user.getDateUseToSortData(), "User");
					
			    	FacesMessage message = Messages.getMessage("messages", "global.gestion.reussi", null);
			    	message.setSeverity(FacesMessage.SEVERITY_INFO);
			        context.addMessage(null, message);
		
   	
   	 }catch(Exception e){
   		 e.printStackTrace();
   		mouchardRessourceService.tracage("Echec modification de son profil par :"+user.getInfosPersonne().getNom()+" "+user.getInfosPersonne().getPrenom(), "modification",user.getDateUseToSortData(), "User");
		
   		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
		    	message.setSeverity(FacesMessage.SEVERITY_WARN);
		        context.addMessage(null, message);
   	 }finally{
   		user = new User();
   		
       
   	 }
   	
	     
   }
	
	 /**
     * 
     * @see Focntion qui modifie le mot de passe
     * 
     * @param ancien mot de passe
     * @param nouveau mot de passe
     */
    
    public void changePass(ActionEvent actionEvent){   
    	
    	 FacesContext context = FacesContext.getCurrentInstance();
    	
    	try{
		    	Md5PasswordEncoder encoder = new Md5PasswordEncoder();
				String cryptedPassword = encoder.encodePassword(password, IConstance.MOT_POUR_CRYPTER);
				
				UserDetails user_secutity = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		    	 user =userService.findByLogin(user_secutity.getUsername());
			      
		      if(user!=null)
			       if(cryptedPassword.equals(user.getPassword())){
			    	   
			    	   cryptedPassword=encoder.encodePassword(password2,IConstance.MOT_POUR_CRYPTER);			    	   
			    	   user.setPassword(cryptedPassword);
			    	   user.setInit_pass(false);
			    	   userService.update(user); // mise a jour de l'utilisateur
			    	   
			    	   mouchardRessourceService.tracage("Modification de son mot de passe par :"+user.getInfosPersonne().getNom()+" "+user.getInfosPersonne().getPrenom(), "modification",user.getDateUseToSortData(), "User");
						
			    	   FacesMessage message = Messages.getMessage("messages", "global.gestion.reussi", null);
				    	message.setSeverity(FacesMessage.SEVERITY_INFO);
				        context.addMessage(null, message);
			    	   
			       }else{
			    	   mouchardRessourceService.tracage("Tentative de modification du mot de passe par :"+user.getInfosPersonne().getNom()+" "+user.getInfosPersonne().getPrenom(), "modification",user.getDateUseToSortData(), "User");
						
			    	   FacesMessage message = Messages.getMessage("messages", "user.password.echec", null);
			 	    	message.setSeverity(FacesMessage.SEVERITY_WARN);
			 	        context.addMessage(null, message);
			       }
		      
		   
    	 }catch(Exception e){
    		 mouchardRessourceService.tracage("Echec modification de son mot de passe par :"+user.getInfosPersonne().getNom()+" "+user.getInfosPersonne().getPrenom(), "modification",user.getDateUseToSortData(), "User");
				
    		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
 	    	message.setSeverity(FacesMessage.SEVERITY_WARN);
 	        context.addMessage(null, message);
      	 }finally{
      		 this.user=null;
      		 password=null;
      		password2=null;
      		password3=null;
      	 }   	
      
    }
    
    /**
 	 * Validation de l'existance 
 	 * @return
 	 */
 	public void validateName(FacesContext context, UIComponent component, Object value) throws ValidatorException {
 		  String valeur = (String) value;
 	
 		 user.setLogin(valeur);
 		if(userService.userExiste(user)){
    		
   		 mouchardRessourceService.tracage("Modification de son profil erronée (Login existant) par :"+user.getInfosPersonne().getNom()+" "+user.getInfosPersonne().getPrenom(), "modification",user.getDateUseToSortData(), "User");
   		
   		FacesMessage message = Messages.getMessage("messages", "user.login.validator", null);
	    	message.setSeverity(FacesMessage.SEVERITY_ERROR);
	        
	        throw new ValidatorException(message);
 		}
 		
 		  
 		}	

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getLastPassword() {
		return lastPassword;
	}

	public void setLastPassword(String lastPassword) {
		this.lastPassword = lastPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public String getPassword3() {
		return password3;
	}

	public void setPassword3(String password3) {
		this.password3 = password3;
	}

	public List<Typespersonnel> getListType() {
		return listType;
	}

	public void setListType(List<Typespersonnel> listType) {
		this.listType = listType;
	}

	public List<Sexe> getListSexe() {
		return listSexe;
	}

	public void setListSexe(List<Sexe> listSexe) {
		this.listSexe = listSexe;
	}

	public IMouchardRessourceService getMouchardRessourceService() {
		return mouchardRessourceService;
	}

	public void setMouchardRessourceService(
			IMouchardRessourceService mouchardRessourceService) {
		this.mouchardRessourceService = mouchardRessourceService;
	}
    
    

}
