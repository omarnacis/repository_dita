package cm.dita.controller.managed.bean.user;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;




import javax.faces.validator.ValidatorException;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import cm.dita.beans.ApplicationBean;
import cm.dita.beans.Sexe;
import cm.dita.constant.IConstance;
import cm.dita.controller.managed.bean.user.privileges.RessourcesDataModel;
import cm.dita.controller.managed.bean.user.privileges.RoleDataModel;
import cm.dita.entities.Espace;
import cm.dita.entities.Personne;
import cm.dita.entities.user.InfosPersonne;
import cm.dita.entities.user.Role;
import cm.dita.entities.user.User;
import cm.dita.service.domaine.inter.IEspaceRessourceService;
import cm.dita.service.domaine.inter.IMouchardRessourceService;
import cm.dita.service.domaine.inter.user.IAccessRessourceService;
import cm.dita.service.domaine.inter.user.IRoleService;
import cm.dita.service.domaine.inter.user.IRoleUserService;
import cm.dita.service.domaine.inter.user.IUserService;
import cm.dita.utils.Messages;



@ManagedBean(name = "userBean")
@SessionScoped
public class UserBean implements Serializable{
	
	/**
	 * 
	 */
	
	private static final Logger LOG = Logger.getLogger(UserBean.class);
	private static final long serialVersionUID = 1L;

		//Spring User Service is injected...
		@ManagedProperty(value="#{userService}")
		IUserService userService;
		 
		 @ManagedProperty(value="#{roleService}")
		 private IRoleService roleService = null;
		 
		 @ManagedProperty(value="#{roleUserService}")
		 private IRoleUserService roleUserService = null;
		 
		 @ManagedProperty(value="#{espaceRessourceService}")
		 private IEspaceRessourceService espaceService = null;
		 
		
		 @ManagedProperty(value="#{accessRessourceService}")
			IAccessRessourceService accessRessourceService;
		 
		 @ManagedProperty(value="#{applicationBean}")
		    private ApplicationBean applicationBean;
		 
		 @ManagedProperty(value="#{mouchardRessourceService}")
		 private IMouchardRessourceService mouchardRessourceService;
		 
		
		 
		
		
		private List<User> userList = new ArrayList<User>();
		private User user;
		private List<User> filteredUsers=new ArrayList<User>();
		private UserDataModel userListDataModel;
		private Personne infosuser;
		private User[]  selectedUsers;
	
		private String  lastPassword;
	//POUR CHANGER LES MOTS DE PASSE
		private String  password;
		private String password2;
		private String password3;
	
	
		List<Espace> listEspace;
		
		List<Sexe> listSexe;
		private Integer espace_id;
		private Integer type_id;
		private int operation;
		
		List<Role> listRole;
		List<Role>seletedRoles;
		RoleDataModel roleListDataModel;
		
		RessourcesDataModel ressourceListDataModel;
   
 
		public UserBean() {
    	user = new User();
    	
    }
    
		
   @PostConstruct    
    public void init(){
	 
	   userList=userService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});		
	   userListDataModel= new UserDataModel(userList);
	   listSexe=Sexe.initialise();
	   mouchardRessourceService.tracage("Liste des utilisateurs", "listing",null, "User");
	 
    }
 
    public void ajoutEvent(ActionEvent actionEvent) {
    	
    	operation=1; //ajout
    	espace_id=0;
		type_id=0;
    	user = new User();
    	infosuser =new Personne();    	
    	
    	SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2);    	
    	user.setDateCreation(sdf.format(new Date()));
    	user.setEnabled(true);    	
    	listEspace=espaceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
    	
    	 this.roleListDataModel= new RoleDataModel(roleService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{}));
     	  // this.seletedRoles=roleService.listOfRole4User(this.user);
    
 
    }
  
    // modification du profil    
    public void profilEvent(ActionEvent actionEvent){
    	
    	operation=3;//profil
    		UserDetails user_secutity = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		user =userService.findByLogin(user_secutity.getUsername());    		 
	     
         if(this.user!=null){	       
	        this.infosuser=user.getInfosPersonne();
	        setLastPassword(user.getPassword());
        }
         
        
    	
    }
    
    public void viewEvent(int id) {
        
    	operation=2; //edition
     // user = new User();
      	infosuser =new Personne();    
      this.seletedRoles=new ArrayList<Role>();
    	
        this.user =new User(userService.load(id));       
        infosuser=this.user.getInfosPersonne();
        setLastPassword(this.user.getPassword());//garde le mot de passe pour ne pas le modififer
     
    	this.roleListDataModel= new RoleDataModel(roleService.listOfRole4User(this.user));//role de l'utilisateur
    	this.ressourceListDataModel =new RessourcesDataModel(accessRessourceService.listAccess2User(this.user));
    
    }
 
    
    public void editEvent(int id) {
        
    	operation=2; //edition
     // user = new User();
      	infosuser =new Personne();     
    	
        this.user =new User(userService.load(id));       
        infosuser=this.user.getInfosPersonne();
        setLastPassword(this.user.getPassword());//garde le mot de passe pour ne pas le modififer
        
        listEspace=espaceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});    	
   	 	this.roleListDataModel= new RoleDataModel(roleService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{}));
    	 this.seletedRoles= roleService.listOfRole4User(this.user);
     
    
    }
    
    public void deleteEvent(int id){
    	
    	this.user =userService.load(id);
    	
    }
 
    
    /**
     * @see fonction d'ajout d'un utilisateur
     * @param user
     * 		prend en entree un utilisateur
     * 
     * @return vide
     */
    

	public void create() {
    	 FacesContext context = FacesContext.getCurrentInstance();
    	 RequestContext requestContext = RequestContext.getCurrentInstance();
    	try{
		    //verifi si l'user existe
    				//JOptionPane.showMessageDialog(null, 1);
		    		if(this.seletedRoles.size()<=0){    					
		    			//JOptionPane.showMessageDialog(null, 2);
		    	        mouchardRessourceService.tracage("Tentative de creation de compte utilisateur sans preciser les rôles "+"("+user.getLogin()+") ", "modification",user.getDateUseToSortData(), "User");			
		    	        FacesMessage message = Messages.getMessage("messages", "user.role.error", null);
		    	    	message.setSeverity(FacesMessage.SEVERITY_ERROR);	
		    	    	context.addMessage(null, message);
		    	    	//JOptionPane.showMessageDialog(null, 2);
		    	    	//return;
		    		}else{
    		
			    	Md5PasswordEncoder encoder = new Md5PasswordEncoder();
					String cryptedPassword = encoder.encodePassword(user.getPassword(), IConstance.MOT_POUR_CRYPTER);
					user.setPassword(cryptedPassword);
			    	
					//user.setEnabled(IConstance.ENABLE_UTILISATEUR_OFF);
					user.setInfosPersonne(infosuser);			
					
					user.setAutorithies(false);
					user.setInit_pass(true);
					user.setLangue("fr");
					user.setIndex_login(user.getLogin()+System.currentTimeMillis());
					user.setEspace(espaceService.load(espace_id));
			    	int code=(int) userService.saveReturnID(user);	
			    	roleUserService.saveRole2User(userService.load(code), seletedRoles); 		    	
			    				    	
			    //	passwordService.save(pwd);
			    	init();//mise � jour de la liste		    	
			    	
			    	mouchardRessourceService.tracage("Ajout de l'utilisateur  "+user.getInfosPersonne().getPersnom()+" "+user.getInfosPersonne().getPersprenom()+"("+user.getLogin()+") ", "ajout",user.getDateUseToSortData(), "User");
					
			    	FacesMessage message = Messages.getMessage("messages", "global.gestion.reussi", null);
			    	message.setSeverity(FacesMessage.SEVERITY_INFO);
			        context.addMessage(null, message);
			       // requestContext.execute("msgDlg.show()");	
		    		}
		    
    	
    	 }catch(Exception e){
    		 e.printStackTrace();
    		 mouchardRessourceService.tracage("Echec de l'ajout de l'utilisateur  "+user.getInfosPersonne().getPersnom()+" "+user.getInfosPersonne().getPersprenom()+"("+user.getLogin()+") ", "ajout",user.getDateUseToSortData(), "User");
				
    		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
		    	message.setSeverity(FacesMessage.SEVERITY_WARN);
		        context.addMessage(null, message);
    	 }finally{
    		 
    		 espace_id=0;
     		type_id=0;
 	        user = new User();
 	        infosuser= new Personne();
 	       seletedRoles.clear();
    	 }
    		
    }
	
	/**
	 * @see Mise a jour d'un utilisateur
	 * @param utilisateur
	 * 
	 */
	
	/**
	 * @see mise a jour
	 * 
	 * @param user
	 */
	public void update() {
   	 FacesContext context = FacesContext.getCurrentInstance();
   	try{
   		if(this.seletedRoles.size()<=0){    					
			
	        mouchardRessourceService.tracage("Tentative de creation de compte utilisateur sans preciser les rôles "+"("+user.getLogin()+") ", "modification",user.getDateUseToSortData(), "User");			
	        FacesMessage message = Messages.getMessage("messages", "user.role.error", null);
	    	message.setSeverity(FacesMessage.SEVERITY_ERROR);	
	    	context.addMessage(null, message);
	    	
		}else{
		   
	    	user.setEspace(espaceService.load(espace_id));
	    	User u=userService.load(this.user.getId());
	    	u.setLogin(this.user.getLogin());
	    	userService.update(u);	    	
	    	roleUserService.saveRole2User(user, seletedRoles); 
	    	
	    	init();//mise � jour de la liste
	    	mouchardRessourceService.tracage("Modification de l'utilisateur "+user.getInfosPersonne().getPersnom()+" "+user.getInfosPersonne().getPersprenom()+"("+user.getLogin()+") ", "modification",user.getDateUseToSortData(), "User");
			
	    	FacesMessage message = Messages.getMessage("messages", "global.gestion.reussi", null);
	    	message.setSeverity(FacesMessage.SEVERITY_INFO);
	        context.addMessage(null, message);
		}
		    
   	
   	 }catch(Exception e){
   		 e.printStackTrace();
   		mouchardRessourceService.tracage("Eche de modification de l'utilisateur "+user.getInfosPersonne().getPersnom()+" "+user.getInfosPersonne().getPersprenom()+"("+user.getLogin()+") ", "modification",user.getDateUseToSortData(), "User");
		
   		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
		    	message.setSeverity(FacesMessage.SEVERITY_WARN);
		        context.addMessage(null, message);
   	 }finally{
   		user = new User();
        infosuser= new Personne();
        seletedRoles.clear();
   	 }
   	
	     
   }
	/**
	 * Change le status d'un utilisateur.
	 */
	
	
	public void status(int id) {
	   	 FacesContext context = FacesContext.getCurrentInstance();
	   	try{
	   		
	   		user=userService.load(id);
			   if(user.isEnabled())
				   user.setEnabled(false);
			   else
				   user.setEnabled(true);
		    	
		    	userService.update(user);	    	
		    	init();//mise � jour de la liste
		    	mouchardRessourceService.tracage("Change ke status de l'utilisateur "+user.getInfosPersonne().getPersnom()+" "+user.getInfosPersonne().getPersprenom()+"("+user.getLogin()+") ", "modification",user.getDateUseToSortData(), "User");
				
		    	FacesMessage message = Messages.getMessage("messages", "global.gestion.reussi", null);
		    	message.setSeverity(FacesMessage.SEVERITY_INFO);
		        context.addMessage(null, message);
			    
	   	
	   	 }catch(Exception e){
	   		 e.printStackTrace();
	   		mouchardRessourceService.tracage("Eche de modification de l'utilisateur "+user.getInfosPersonne().getPersnom()+" "+user.getInfosPersonne().getPersprenom()+"("+user.getLogin()+") ", "modification",user.getDateUseToSortData(), "User");
			
	   		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
			    	message.setSeverity(FacesMessage.SEVERITY_WARN);
			        context.addMessage(null, message);
	   	 }finally{
	   		user = new User();
	        infosuser= new Personne();
	       // seletedRoles.clear();
	   	 }
	   	
		     
	   }
   
 
    public void delete(ActionEvent actionEvent) {
    	
    	 FacesContext context = FacesContext.getCurrentInstance();
    	 RequestContext requestContext = RequestContext.getCurrentInstance();
       try{
    	  	 
    	   Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			String cryptedPassword = encoder.encodePassword(user.getLogin()+System.currentTimeMillis(), IConstance.MOT_POUR_CRYPTER);
			user.setPassword(cryptedPassword);
    	   
    		 userService.deleteVersusDesabled(user, IConstance.FIELD_DELETE);
    		 mouchardRessourceService.tracage("Suppression de l'utilisateur "+user.getInfosPersonne().getPersnom()+" "+user.getInfosPersonne().getPersprenom()+"("+user.getLogin()+") ", "suppression",user.getDateUseToSortData(), "User");
						
    	   
		    	init();//mise à jour de la liste
		    	
		    	 FacesMessage message = Messages.getMessage("messages", "global.gestion.delete", null);
			    	message.setSeverity(FacesMessage.SEVERITY_INFO);
			        context.addMessage(null, message);				    	
		        requestContext.execute("PF('deleteDialog').hide()");
	  // 	}
    	  
    	   
       }catch(Exception e){
    	   e.printStackTrace();
    	   mouchardRessourceService.tracage("Echec de suppression de l'utilisateur "+user.getInfosPersonne().getPersnom()+" "+user.getInfosPersonne().getPersprenom()+"("+user.getLogin()+") ", "suppression",user.getDateUseToSortData(), "User");
			
    	   FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
	    	message.setSeverity(FacesMessage.SEVERITY_WARN);
	        context.addMessage(null, message);
  	 }finally{
  		selectedUsers=null;
 	   user=null;
 	   espace_id=0;
			type_id=0;
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
			    	 
			    	   userService.update(user); // mise a jour de l'utilisateur

			    	   mouchardRessourceService.tracage("Modification de son mot de passe par :"+user.getInfosPersonne().getPersnom()+" "+user.getInfosPersonne().getPersprenom(), "modification",user.getDateUseToSortData(), "User");
						
			    	   FacesMessage message = Messages.getMessage("messages", "global.gestion.reussi", null);
				    	message.setSeverity(FacesMessage.SEVERITY_INFO);
				        context.addMessage(null, message);
			    	   
			       }else{
			    	   mouchardRessourceService.tracage("Tentative de modification du mot de passe par :"+user.getInfosPersonne().getPersnom()+" "+user.getInfosPersonne().getPersprenom(), "modification",user.getDateUseToSortData(), "User");
						
			    	   FacesMessage message = Messages.getMessage("messages", "user.password.echec", null);
			 	    	message.setSeverity(FacesMessage.SEVERITY_WARN);
			 	        context.addMessage(null, message);
			       }
		      
		   
    	 }catch(Exception e){
    		 mouchardRessourceService.tracage("Echec modification de son mot de passe par :"+user.getInfosPersonne().getPersnom()+" "+user.getInfosPersonne().getPersprenom(), "modification",user.getDateUseToSortData(), "User");
 			
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
    * @see Reinitialise le mot de passe par l'administrateur
    * @param utilisateur
    */
    public void changePassByAdmin(ActionEvent actionEvent){
    	
    	 FacesContext context = FacesContext.getCurrentInstance();
    try{	
	    	Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			String cryptedPassword = encoder.encodePassword(password, IConstance.MOT_POUR_CRYPTER);
			
			 this.user.setPassword(cryptedPassword);
			 this.user.setInit_pass(true);
	  	   userService.update(this.user); // mise a jour de l'utilisateur
	  	 mouchardRessourceService.tracage("Reinitialisation du mot de passe de:"+user.getInfosPersonne().getPersnom()+" "+user.getInfosPersonne().getPersprenom(), "modification",user.getDateUseToSortData(), "User");
			
	  	 FacesMessage message = Messages.getMessage("messages", "global.gestion.reussi", null);
	  	 message.setSeverity(FacesMessage.SEVERITY_INFO);
	     context.addMessage(null, message);	
    }catch(Exception e){
    	mouchardRessourceService.tracage("Echec de reinitialisation du mot de passe de:"+user.getInfosPersonne().getPersnom()+" "+user.getInfosPersonne().getPersprenom(), "modification",user.getDateUseToSortData(), "User");
		
    	 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
	    	message.setSeverity(FacesMessage.SEVERITY_WARN);
	        context.addMessage(null, message);
    }
    }
    
  /**
   * GESTION DES ROLES UTILISATEURS
   * 
   * @return
   */
    
   /* public void AddRole2UserEvent(User user) { 		
 	    this.user = user; 	  
 	   this.roleListDataModel= new RoleDataModel(roleService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{}));
 	   this.seletedRoles=roleService.listOfRole4User(this.user); 	  
     }*/
    
    /**
 	 * @see Ajoute un role a un utilisateur
 	 * @param list role
 	 * @param user
 	 * @return le role associe au user
 	 */
 	
 	public void AddRole2User(){ 		
 		 FacesContext context = FacesContext.getCurrentInstance();
 		RequestContext requestContext = RequestContext.getCurrentInstance();
 		
       try{
    	   //this.role.getGroups().clear();
    	   if(this.seletedRoles.size()>0){    		   
    		  
    		  requestContext.execute("PF('addDialogRole').show()");
	    	 	
    	   }else{
    		   requestContext.execute("PF('deleteDialogRole').show()");
    		  
    		   
    	   }
    	 
       }catch(Exception e){
  		 e.printStackTrace();
  		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
	    	message.setSeverity(FacesMessage.SEVERITY_WARN);
	        context.addMessage(null, message);
  	 }finally{
  		//this.seletedRoles.clear();
  		//this.user=new User();
  	 }
 		
 	}
 	
 	
 	
 	public void addRole(ActionEvent actionEvent){
 		FacesContext context = FacesContext.getCurrentInstance();
 		RequestContext requestContext = RequestContext.getCurrentInstance();
 		try{
 			roleUserService.saveRole2User(this.user, seletedRoles);  		    	  
 		   FacesMessage message = Messages.getMessage("messages", "global.gestion.reussi", null);
	    	message.setSeverity(FacesMessage.SEVERITY_INFO);
	        context.addMessage(null, message);
	       
 		}catch(Exception e){
 			e.printStackTrace();
 	  		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
 		    	message.setSeverity(FacesMessage.SEVERITY_WARN);
 		        context.addMessage(null, message);
 		}finally{
 			this.seletedRoles.clear();
 	  		this.user=new User();
 		}
 		
 	}
 	
 	/**
 	 * Suppression de tous les groupes du role
 	 */
 	
 	public void deleteRole(ActionEvent actionEvent){
 		FacesContext context = FacesContext.getCurrentInstance();
 		RequestContext requestContext = RequestContext.getCurrentInstance();
 		try{
 			
 			roleUserService.deleteRole2User(this.user);
 			//requestContext.execute("deleteDialogGroup.hide()");
 			FacesMessage message = Messages.getMessage("messages", "global.gestion.reussi", null);
	    	message.setSeverity(FacesMessage.SEVERITY_INFO);
	        context.addMessage(null, message);
 		}catch(Exception e){
 			e.printStackTrace();
 	  		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
 		    	message.setSeverity(FacesMessage.SEVERITY_WARN);
 		        context.addMessage(null, message);
 		}finally{
 			this.seletedRoles.clear();
 	  		this.user=new User();
 		}
 		
 	}
 	
 	
 	
 	
 	
 	 /**
 	 * Validation de l'existance 
 	 * @return
 	 */
 	public void validateName(FacesContext context, UIComponent component, Object value) throws ValidatorException {
 		  String valeur = (String) value;
 	
 		 user.setLogin(valeur);
 	/*	
 		JOptionPane.showMessageDialog(null, "d0");
 		if(this.seletedRoles.size()<=0){    					
			JOptionPane.showMessageDialog(null, "d1");
	        mouchardRessourceService.tracage("Tentative de creation de compte utilisateur sans preciser les rôles "+"("+user.getLogin()+") ", "modification",user.getDateUseToSortData(), "User");			
	        FacesMessage message = Messages.getMessage("messages", "user.role.error", null);
	    	message.setSeverity(FacesMessage.SEVERITY_ERROR);	
	    	
 	    	 throw new ValidatorException(message);
		}
 */
 	
 		 if(userService.userExiste(user)){
 			
 			mouchardRessourceService.tracage("Tentative de modification de l'utilisateur(login existant) "+"("+user.getLogin()+") ", "modification",user.getDateUseToSortData(), "User");			
    		FacesMessage message = Messages.getMessage("messages", "user.login.validator", null);
	    	message.setSeverity(FacesMessage.SEVERITY_ERROR);	
	    	
 	    	 throw new ValidatorException(message);
 	    	
 		 }
 		
 		  
 		}
 	
 	public String showUser(){		
		
 		userList=userService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});		
 	   userListDataModel= new UserDataModel(userList);
 	  
		return "ok";
	}
   
 
	public User getuser() {
		return user;
	}

	public void setuser(User user) {
		this.user = user;
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}	

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<User> getFilteredUsers() {
		return filteredUsers;
	}

	public void setFilteredUsers(List<User> filteredUsers) {
		this.filteredUsers = filteredUsers;
	}

	public UserDataModel getUserListDataModel() {
		return userListDataModel;
	}

	public void setUserListDataModel(UserDataModel userListDataModel) {
		this.userListDataModel = userListDataModel;
	}

	public Personne getInfosuser() {
		return infosuser;
	}

	public void setInfosuser(Personne infosuser) {
		this.infosuser = infosuser;
	}

	public User[] getSelectedUsers() {
		return selectedUsers;
	}

	public void setSelectedUsers(User[] selectedUsers) {
		this.selectedUsers = selectedUsers;
	}

	

	public String getLastPassword() {
		return lastPassword;
	}





	public void setLastPassword(String lastPassword) {
		this.lastPassword = lastPassword;
	}

	

	

	
	public IRoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(IRoleService roleService) {
		this.roleService = roleService;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public IEspaceRessourceService getEspaceService() {
		return espaceService;
	}

	public void setEspaceService(IEspaceRessourceService espaceService) {
		this.espaceService = espaceService;
	}

	

	public List<Espace> getListEspace() {
		return listEspace;
	}

	public void setListEspace(List<Espace> listEspace) {
		this.listEspace = listEspace;
	}

	public List<Sexe> getListSexe() {
		return listSexe;
	}

	public void setListSexe(List<Sexe> listSexe) {
		this.listSexe = listSexe;
	}

	public Integer getEspace_id() {
		return espace_id;
	}

	public void setEspace_id(Integer espace_id) {
		this.espace_id = espace_id;
	}

	public Integer getType_id() {
		return type_id;
	}

	public void setType_id(Integer type_id) {
		this.type_id = type_id;
	}

	public int getOperation() {
		return operation;
	}

	public void setOperation(int operation) {
		this.operation = operation;
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

	public IRoleUserService getRoleUserService() {
		return roleUserService;
	}

	public List<Role> getListRole() {
		return listRole;
	}

	public List<Role> getSeletedRoles() {
		return seletedRoles;
	}

	public RoleDataModel getRoleListDataModel() {
		return roleListDataModel;
	}

	public void setRoleUserService(IRoleUserService roleUserService) {
		this.roleUserService = roleUserService;
	}

	public void setListRole(List<Role> listRole) {
		this.listRole = listRole;
	}

	public void setSeletedRoles(List<Role> seletedRoles) {
		this.seletedRoles = seletedRoles;
	}

	public void setRoleListDataModel(RoleDataModel roleListDataModel) {
		this.roleListDataModel = roleListDataModel;
	}

	public IAccessRessourceService getAccessRessourceService() {
		return accessRessourceService;
	}

	public void setAccessRessourceService(
			IAccessRessourceService accessRessourceService) {
		this.accessRessourceService = accessRessourceService;
	}

	public RessourcesDataModel getRessourceListDataModel() {
		return ressourceListDataModel;
	}

	public void setRessourceListDataModel(RessourcesDataModel ressourceListDataModel) {
		this.ressourceListDataModel = ressourceListDataModel;
	}


	public ApplicationBean getApplicationBean() {
		return applicationBean;
	}


	public void setApplicationBean(ApplicationBean applicationBean) {
		this.applicationBean = applicationBean;
	}


	public IMouchardRessourceService getMouchardRessourceService() {
		return mouchardRessourceService;
	}


	public void setMouchardRessourceService(
			IMouchardRessourceService mouchardRessourceService) {
		this.mouchardRessourceService = mouchardRessourceService;
	}


	
	
	




	 
    
}