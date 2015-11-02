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
//import javax.swing.JOptionPane;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import cm.dita.beans.ApplicationBean;
import cm.dita.beans.Sexe;
import cm.dita.constant.IConstance;
import cm.dita.constant.ISessionConstant;
import cm.dita.controller.managed.bean.user.privileges.GroupDataModel;
import cm.dita.controller.managed.bean.user.privileges.RessourcesDataModel;
import cm.dita.controller.managed.bean.user.privileges.RoleDataModel;
import cm.dita.entities.Espace;
import cm.dita.entities.Typespersonnel;
import cm.dita.entities.user.InfosPersonne;
import cm.dita.entities.user.Role;
import cm.dita.entities.user.User;
import cm.dita.service.domaine.inter.IEspaceRessourceService;
import cm.dita.service.domaine.inter.IMouchardRessourceService;
import cm.dita.service.domaine.inter.ITypespersonnelRessourceService;
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
		 
		 @ManagedProperty(value="#{typespersonnelRessourceService}")
		 private ITypespersonnelRessourceService typeService = null;
		 
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
		private InfosPersonne infosuser;
		private User[]  selectedUsers;
	
		private String  lastPassword;
	//POUR CHANGER LES MOTS DE PASSE
		private String  password;
		private String password2;
		private String password3;
	
	
		List<Espace> listEspace;
		List<Typespersonnel> listType;
		List<Sexe> listSexe;
		private Integer espace_id;
		private Integer type_id;
		private int operation;
		
		List<Role> listRole;
		List<Role>seletedRoles;
		RoleDataModel roleListDataModel;
		
		RessourcesDataModel ressourceListDataModel;
		
		private boolean limite_validite_compte;
   
 
		public UserBean() {
    	user = new User();
    	
    }
    
		
   @PostConstruct    
    public void init(){
	   limite_validite_compte = false;
	   userList=userService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});		
	   userListDataModel= new UserDataModel(userList);
	   listSexe=Sexe.initialise();
	   mouchardRessourceService.tracage("Liste des utilisateurs", "listing",null, "User");
	 
    }
 
    public void ajoutEvent(ActionEvent actionEvent) {
    	limite_validite_compte = false;
    	operation=1; //ajout
    	espace_id=0;
		type_id=0;
    	user = new User();
    	infosuser =new InfosPersonne();    	
    	
    	SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2);    	
    	user.setDateCreation(sdf.format(new Date()));
    	user.setEnabled(true);
    	
    	listEspace=espaceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
    	listType=typeService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
 
    }
    
    public void afficheSelectDatesValidite(){
    	limite_validite_compte &= true;
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
 
    
    public void editEvent(int id) {
        
    	operation=2; //edition
     // user = new User();
      	infosuser =new InfosPersonne();     
    	
        this.user =new User(userService.load(id));       
        infosuser=this.user.getInfosPersonne();
        setLastPassword(this.user.getPassword());//garde le mot de passe pour ne pas le modififer
        espace_id=this.user.getEspace().getId();
        //type_id=this.user.getTypespersonnel().getTypepersid();        
        listEspace=espaceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
    	listType=typeService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
    	
    	//pour initialiser la zone la zone d'affichage de date de debut de validité et date de fin de validité
    	if(user.getDate_debut_validite() != null)
    		limite_validite_compte = true;
    	else
    		limite_validite_compte = false;
    	
    	this.roleListDataModel= new RoleDataModel(roleService.listOfRole4User(this.user));//role de l'utilisateur
    	this.ressourceListDataModel =new RessourcesDataModel(accessRessourceService.listAccess2User(this.user));
    
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
		    	
			    	Md5PasswordEncoder encoder = new Md5PasswordEncoder();
					String cryptedPassword = encoder.encodePassword(user.getPassword(), IConstance.MOT_POUR_CRYPTER);
					user.setPassword(cryptedPassword);
			    	
					//user.setEnabled(IConstance.ENABLE_UTILISATEUR_OFF);
					
					JOptionPane.showMessageDialog(null,  "debut de validite"+user.getDate_debut_validite()+"  Fin de validite"+user.getDate_fin_validite());
					
					user.setInfosPersonne(infosuser);					
					user.setEspace(espaceService.load(this.espace_id));
				//	user.setTypespersonnel(typeService.load(this.type_id));
					user.setAutorithies(false);
					user.setInit_pass(true);
					//user.setLangue("fr");
			    	this.user=userService.save(user);	
			    	
			    	
			    	
			    //	passwordService.save(pwd);
			    	init();//mise ï¿½ jour de la liste
			    	limite_validite_compte = false;
			    	
			    	//HttpServletRequest req=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
			    	/*LOG.info("Ajout du compte \\("+user.getIdentifier()+"\\)"+user.getInfosuser().getNomRaisonSocial()+
			        		" "+user.getInfosuser().getPrenom()+" Login\\:"+user.getLogin()+" par\\: "
			        		+((User)req.getSession().getAttribute(ISessionConstant.SS_USER)).getInfosuser().getNomRaisonSocial());
			    	*/
			    	mouchardRessourceService.tracage("Ajout de l'utilisateur  "+user.getInfosPersonne().getNom()+" "+user.getInfosPersonne().getPrenom()+"("+user.getLogin()+") ", "ajout",user.getDateUseToSortData(), "User");
					
			    	FacesMessage message = Messages.getMessage("messages", "global.gestion.reussi", null);
			    	message.setSeverity(FacesMessage.SEVERITY_INFO);
			        context.addMessage(null, message);
			       // requestContext.execute("msgDlg.show()");	
			        
    	
    	 }catch(Exception e){
    		 e.printStackTrace();
    		 mouchardRessourceService.tracage("Echec de l'ajout de l'utilisateur  "+user.getInfosPersonne().getNom()+" "+user.getInfosPersonne().getPrenom()+"("+user.getLogin()+") ", "ajout",user.getDateUseToSortData(), "User");
				
    		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
		    	message.setSeverity(FacesMessage.SEVERITY_WARN);
		        context.addMessage(null, message);
    	 }finally{
    		 
    		 espace_id=0;
     		type_id=0;
 	        user = new User();
 	        infosuser= new InfosPersonne();
 	       limite_validite_compte = false;
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
		    		
			    	
		    		user.setEspace(espaceService.load(this.espace_id));
				//	user.setTypespersonnel(typeService.load(this.type_id));
			    	userService.update(user);
			    	init();//mise ï¿½ jour de la liste
			    	limite_validite_compte = false;
			    	mouchardRessourceService.tracage("Modification de l'utilisateur "+user.getInfosPersonne().getNom()+" "+user.getInfosPersonne().getPrenom()+"("+user.getLogin()+") ", "modification",user.getDateUseToSortData(), "User");
					
			    	FacesMessage message = Messages.getMessage("messages", "global.gestion.reussi", null);
			    	message.setSeverity(FacesMessage.SEVERITY_INFO);
			        context.addMessage(null, message);
		    
   	
   	 }catch(Exception e){
   		 e.printStackTrace();
   		mouchardRessourceService.tracage("Eche de modification de l'utilisateur "+user.getInfosPersonne().getNom()+" "+user.getInfosPersonne().getPrenom()+"("+user.getLogin()+") ", "modification",user.getDateUseToSortData(), "User");
		
   		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
		    	message.setSeverity(FacesMessage.SEVERITY_WARN);
		        context.addMessage(null, message);
   	 }finally{
   		user = new User();
        infosuser= new InfosPersonne();
        limite_validite_compte = false;
   	 }
   	
	     
   }
   
 
    public void delete(ActionEvent actionEvent) {
    	
    	 FacesContext context = FacesContext.getCurrentInstance();
    	 RequestContext requestContext = RequestContext.getCurrentInstance();
       try{
    	  
    	 /*  if(selectedUsers.length>0)
	    	   for(int i=0;i<selectedUsers.length;i++){	
	    		  user=selectedUsers[i];
	    		   userService.deleteVersusDesabled(selectedUsers[i], IConstance.FIELD_DELETE);
	    		   mouchardRessourceService.tracage("Suppression de l'utilisateur "+selectedUsers[i].getInfosPersonne().getNom()+" "+selectedUsers[i].getInfosPersonne().getPrenom()+"("+selectedUsers[i].getLogin()+") ", "suppression",selectedUsers[i].getDateUseToSortData(), "User");
					
		    	}
    	   else{   */ 	 
		    		userService.deleteVersusDesabled(user, IConstance.FIELD_DELETE);
		    		 mouchardRessourceService.tracage("Suppression de l'utilisateur "+user.getInfosPersonne().getNom()+" "+user.getInfosPersonne().getPrenom()+"("+user.getLogin()+") ", "suppression",user.getDateUseToSortData(), "User");
						
    	   
		    	init();//mise Ã  jour de la liste
		    	
		    	 FacesMessage message = Messages.getMessage("messages", "global.gestion.delete", null);
			    	message.setSeverity(FacesMessage.SEVERITY_INFO);
			        context.addMessage(null, message);				    	
		        requestContext.execute("deleteDialog.hide()");	
    	  
    	   
       }catch(Exception e){
    	   e.printStackTrace();
    	   mouchardRessourceService.tracage("Echec de suppression de l'utilisateur "+user.getInfosPersonne().getNom()+" "+user.getInfosPersonne().getPrenom()+"("+user.getLogin()+") ", "suppression",user.getDateUseToSortData(), "User");
			
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
	  	 mouchardRessourceService.tracage("Reinitialisation du mot de passe de:"+user.getInfosPersonne().getNom()+" "+user.getInfosPersonne().getPrenom(), "modification",user.getDateUseToSortData(), "User");
			
	  	 FacesMessage message = Messages.getMessage("messages", "global.gestion.reussi", null);
	  	 message.setSeverity(FacesMessage.SEVERITY_INFO);
	     context.addMessage(null, message);	
    }catch(Exception e){
    	mouchardRessourceService.tracage("Echec de reinitialisation du mot de passe de:"+user.getInfosPersonne().getNom()+" "+user.getInfosPersonne().getPrenom(), "modification",user.getDateUseToSortData(), "User");
		
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
    
    public void AddRole2UserEvent(User user) { 		
 	    this.user = user; 	  
 	   this.roleListDataModel= new RoleDataModel(roleService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{}));
 	   this.seletedRoles=roleService.listOfRole4User(this.user); 	  
     }
    
    /**
 	 * @see Ajoute un role a un utilisateur
 	 * @param list role
 	 * @param user
 	 * @return le role associe au user
 	 */
 	
 	public void AddRole2User(){ 		
 		 FacesContext context = FacesContext.getCurrentInstance();
 		
       try{
    	   //this.role.getGroups().clear();
    	   if(this.seletedRoles.size()>0){    		   
    		  roleUserService.saveRole2User(this.user, seletedRoles);     		  	
	    	 	
    	   }else{
    		  
    		  roleUserService.deleteRole2User(this.user);
    		   
    	   }
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
 
 	
 		 if(userService.userExiste(user)){ 
 			
 			mouchardRessourceService.tracage("Tentative de modification de l'utilisateur(login existant) "+"("+user.getLogin()+") ", "modification",user.getDateUseToSortData(), "User");			
    		FacesMessage message = Messages.getMessage("messages", "user.login.validator", null);
	    	message.setSeverity(FacesMessage.SEVERITY_ERROR);	
	    	
 	    	 throw new ValidatorException(message);
 	    	
 		 }
 		
 		  
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

	public InfosPersonne getInfosuser() {
		return infosuser;
	}

	public void setInfosuser(InfosPersonne infosuser) {
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

	public ITypespersonnelRessourceService getTypeService() {
		return typeService;
	}

	public void setTypeService(ITypespersonnelRessourceService typeService) {
		this.typeService = typeService;
	}

	public List<Espace> getListEspace() {
		return listEspace;
	}

	public void setListEspace(List<Espace> listEspace) {
		this.listEspace = listEspace;
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


	/**
	 * @return the limite_validite_compte
	 */
	public boolean isLimite_validite_compte() {
		return limite_validite_compte;
	}


	/**
	 * @param limite_validite_compte the limite_validite_compte to set
	 */
	public void setLimite_validite_compte(boolean limite_validite_compte) {
		this.limite_validite_compte = limite_validite_compte;
	}

	
	
	
	




	 
    
}