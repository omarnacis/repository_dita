package cm.dita.controller.managed.bean.user.privileges;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

import cm.dita.constant.IConstance;
import cm.dita.entities.user.Group;
import cm.dita.entities.user.Role;
import cm.dita.entities.user.RoleGroup;
import cm.dita.service.domaine.inter.IMouchardRessourceService;
import cm.dita.service.domaine.inter.user.IGroupService;
import cm.dita.service.domaine.inter.user.IRoleGroupService;
import cm.dita.service.domaine.inter.user.IRoleService;
import cm.dita.utils.Messages;

@ManagedBean(name = "roleBean")
@SessionScoped
public class RoleBean  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = Logger.getLogger(RoleBean.class);
	
	@ManagedProperty(value="#{roleService}")
	IRoleService roleService;
	
	@ManagedProperty(value="#{roleGroupService}")
	IRoleGroupService roleGroupService;
	
	@ManagedProperty(value="#{groupService}")
	IGroupService groupService;
	
	@ManagedProperty(value="#{mouchardRessourceService}")
	 private IMouchardRessourceService mouchardRessourceService;
	
	private List<Role> roleList = new ArrayList<Role>();
	private Role role;
	private Role role_edit ;
	private List<Role> filteredRole=new ArrayList<Role>();
	private RoleDataModel roleListDataModel;		
	private Role[]  selectedRole;
	
	private List<Group>  selectedGroups;
	private List<Group> groupsList;
	private GroupDataModel groupListDataModel;
	
	
	

	/**
	 * Initialisation des valeurs
	 * @param
	 * @return
	 * 
	 */
	@PostConstruct    
	    public void init(){
		this.roleList=roleService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});			
		 this.roleListDataModel= new RoleDataModel(roleList);	
		 mouchardRessourceService.tracage("Liste des roles", "listing",null, "Groupe");
		
	    }
	
	
	//CLICK SUR LES BUTTONS ajout et EDIT
 	public void ajoutEvent(ActionEvent actionEvent) {		 		
    	role = new Role();			    	
    	SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2);    	
    	role.setDateCreation(sdf.format(new Date()));
    	
    }
 	
 	/**
 	 * 
 	 * @param id de l'role a charger
 	 * @return l'role choisi
 	 */
 	
 	public void editEvent(Role role) { 		
 	    //this.role_edit = roleService.load(role.getIdentifier());
 	   this.role= new Role(roleService.load(role.getIdentifier()));
 	   this.selectedGroups=groupService.listOfGroup4Role(this.role); 	
 	 this.groupListDataModel= new GroupDataModel(this.selectedGroups);
        
     }
/**
* Ajoute un role
* @param role
* @return role
*/
 	
 	public void create(ActionEvent actionEvent) {
    	 FacesContext context = FacesContext.getCurrentInstance();
    	try{	
    	
	    		roleService.save(role);	
		        init();//mise ï¿½ jour de la liste			    	
		        mouchardRessourceService.tracage("Ajout du rôle "+role.getRoleName(), "ajout",role.getDateUseToSortData(), "Role");
				
		        FacesMessage message = Messages.getMessage("messages", "global.gestion.reussi", null);
		    	message.setSeverity(FacesMessage.SEVERITY_INFO);
		        context.addMessage(null, message);
	    	
    	
    	 }catch(Exception e){
    		 e.printStackTrace();
    		 mouchardRessourceService.tracage("Echec ajout du rôle "+role.getRoleName(), "ajout",role.getDateUseToSortData(), "Role");
 			
    		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
		    	message.setSeverity(FacesMessage.SEVERITY_WARN);
		        context.addMessage(null, message);
    	 }finally{
    		 role = new Role();
    		 role_edit= null;
    	 }
	        
	     
    }
 	
 	
 /**
  * Mise Ã  jour d'un role
  * @param role
  * @return role
  */
 	
 	 public void edition(ActionEvent actionEvent) {
    	 FacesContext context = FacesContext.getCurrentInstance();
    	
    	 try{
    		
    		 	roleService.update(role);
		    	init();//mise ï¿½ jour de la liste
		    	 mouchardRessourceService.tracage("Modification du rôle "+role.getRoleName(), "modification",role.getDateUseToSortData(), "Role");
					
		    	role = new Role();
		    	FacesMessage message = Messages.getMessage("messages", "global.gestion.modifier", null);
		    	message.setSeverity(FacesMessage.SEVERITY_INFO);
		        context.addMessage(null, message);
 	    //	}
			  
    	 }catch(Exception e){
    		 mouchardRessourceService.tracage("Echec modification du rôle"+role.getRoleName(), "modification",role.getDateUseToSortData(), "Role");
 			
    		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
		    	message.setSeverity(FacesMessage.SEVERITY_WARN);
		        context.addMessage(null, message);
     	 }finally{
     		role = new Role();
     	 }
    }
 	 
 	/**
 	 * Supprime un role
 	 * @param Role a supprimer
 	 *@return void
 	 */
 	 
 	public void delete(ActionEvent actionEvent) {
    	
    	 FacesContext context = FacesContext.getCurrentInstance();
    	 RequestContext requestContext = RequestContext.getCurrentInstance();
       try{
    	   
    	   //Cas selectiond
    	  
    	 /*  if(selectedRole.length>0)
	    	   for(int i=0;i<selectedRole.length;i++){				    		
		    	//	roleService.deleteVersusDesabled(selectedRole[i], IConstance.FIELD_DELETE);
	    		   roleService.deleteVersusDesabled(selectedRole[i]);
	    		   role=selectedRole[i];
	    		   mouchardRessourceService.tracage("Suppression du rÃ´le"+role.getRoleName()+"("+role.getIdentifier()+")", "suppression",role.getDateUseToSortData(), "Role");
	    			
		    		
		    	}
    	   else{*/
    	 
    	  // if(role!=null)		    	
    		  // roleService.deleteVersusDesabled(role, IConstance.FIELD_DELETE);
    		   roleService.deleteVersusDesabled(role);
    	   		mouchardRessourceService.tracage("Suppression du rôle"+role.getRoleName()+"("+role.getIdentifier()+")", "suppression",role.getDateUseToSortData(), "Role");
			
    	  // }
    	   
    	   
		    	init();//mise Ã  jour de la liste
		    	
		    	 FacesMessage message = Messages.getMessage("messages", "global.gestion.delete", null);
			    	message.setSeverity(FacesMessage.SEVERITY_INFO);
			        context.addMessage(null, message);				    	
		        requestContext.execute("PF('deleteDialog').hide()");	    	  
    	   
       }catch(Exception e){
  		 e.printStackTrace();
  		mouchardRessourceService.tracage("Echec de suppression du rôle"+role.getRoleName()+"("+role.getIdentifier()+")", "suppression",role.getDateUseToSortData(), "Role");
		
  		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
	    	message.setSeverity(FacesMessage.SEVERITY_WARN);
	        context.addMessage(null, message);
  	 }finally{
  		selectedRole=null;
 	   role=new Role();
  	 }
 
    }
 	
 	public void AddGroup2RoleEvent(Role role) { 		
 	    this.role = role;
 	    this.groupsList=groupService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
 	   this.groupListDataModel= new GroupDataModel(this.groupsList);
 	   this.selectedGroups=groupService.listOfGroup4Role(this.role);
 	 
        
     }
 	
 	
 	/**
 	 * @see Ajoute un groupe a un role
 	 * @param list groupe
 	 * @param role
 	 * @return le role associe au groupe
 	 */
 	
 	public void AddGroup2Role(){ 		
 		 FacesContext context = FacesContext.getCurrentInstance();
 		RequestContext requestContext = RequestContext.getCurrentInstance();
 		
       try{
    	   //this.role.getGroups().clear(); addDialogGroup.hide()
    	   if(selectedGroups.size()>0){   
    		   requestContext.execute("PF('addDialogGroup').show()");
    		   
    	   }else{
    		   
    		   requestContext.execute("PF('deleteDialogGroup').show()");
    		  
    		   
    	   }
    	   	 
    	   
       }catch(Exception e){
  		 e.printStackTrace();
  		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
	    	message.setSeverity(FacesMessage.SEVERITY_WARN);
	        context.addMessage(null, message);
  	 }finally{
  		
  	 }
 		
 	}
 	
 	public void addGroup(ActionEvent actionEvent){
 		FacesContext context = FacesContext.getCurrentInstance();
 		RequestContext requestContext = RequestContext.getCurrentInstance();
 		try{
 			roleGroupService.saveGroup2Role(this.role, selectedGroups);	    	  
 		   FacesMessage message = Messages.getMessage("messages", "global.gestion.reussi", null);
	    	message.setSeverity(FacesMessage.SEVERITY_INFO);
	        context.addMessage(null, message);
	       
 		}catch(Exception e){
 			e.printStackTrace();
 	  		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
 		    	message.setSeverity(FacesMessage.SEVERITY_WARN);
 		        context.addMessage(null, message);
 		}finally{
 			this.selectedGroups=null;
 	  		this.role=null;
 		}
 		
 	}
 	
 	/**
 	 * Suppression de tous les groupes du role
 	 */
 	
 	public void deleteGroup(ActionEvent actionEvent){
 		FacesContext context = FacesContext.getCurrentInstance();
 		RequestContext requestContext = RequestContext.getCurrentInstance();
 		try{
 			
 			 roleGroupService.deleteGroup2Role(this.role);
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
 			this.selectedGroups=null;
 	  		this.role=null;
 		}
 		
 	}
 	
 	/**
 	 * Validation de l'existance 
 	 * @return
 	 */
 	public void validateName(FacesContext context, UIComponent component, Object value) throws ValidatorException {
 		  String valeur = (String) value;
 		 /*if(this.role_edit!=null)
 			 role.setIdentifier(role_edit.getIdentifier());*/
 		 role.setRoleName(valeur);
 		 if(roleService.userExiste(role)){
 			 mouchardRessourceService.tracage("Tentative d'ajout du rôle (nom existant) "+role.getRoleName(), "ajout",role.getDateUseToSortData(), "Role");
 			//role.setRoleName(nomRole);    			
 			FacesMessage message = Messages.getMessage("messages", "role.nom.existe", null);
 	    	message.setSeverity(FacesMessage.SEVERITY_ERROR);
 	    	 throw new ValidatorException(message);
 		 }
 		
 		  
 		}
	
	
	

	public IRoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(IRoleService roleService) {
		this.roleService = roleService;
	}

	public IRoleGroupService getRoleGroupService() {
		return roleGroupService;
	}

	public void setRoleGroupService(IRoleGroupService roleGroupService) {
		this.roleGroupService = roleGroupService;
	}

	public IGroupService getGroupService() {
		return groupService;
	}

	public void setGroupService(IGroupService groupService) {
		this.groupService = groupService;
	}


	public List<Role> getRoleList() {
		return roleList;
	}


	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}


	public Role getRole() {
		return role;
	}


	public void setRole(Role role) {
		this.role = role;
	}


	public List<Role> getFilteredRole() {
		return filteredRole;
	}


	public void setFilteredRole(List<Role> filteredRole) {
		this.filteredRole = filteredRole;
	}


	public RoleDataModel getRoleListDataModel() {
		return roleListDataModel;
	}


	public void setRoleListDataModel(RoleDataModel roleListDataModel) {
		this.roleListDataModel = roleListDataModel;
	}


	public Role[] getSelectedRole() {
		return selectedRole;
	}


	public void setSelectedRole(Role[] selectedRole) {
		this.selectedRole = selectedRole;
	}


	


	public List<Group> getSelectedGroups() {
		return selectedGroups;
	}


	public void setSelectedGroups(List<Group> selectedGroups) {
		this.selectedGroups = selectedGroups;
	}


	public List<Group> getGroupsList() {
		return groupsList;
	}


	public void setGroupsList(List<Group> groupsList) {
		this.groupsList = groupsList;
	}


	public GroupDataModel getGroupListDataModel() {
		return groupListDataModel;
	}


	public void setGroupListDataModel(GroupDataModel groupListDataModel) {
		this.groupListDataModel = groupListDataModel;
	}


	public IMouchardRessourceService getMouchardRessourceService() {
		return mouchardRessourceService;
	}


	public void setMouchardRessourceService(
			IMouchardRessourceService mouchardRessourceService) {
		this.mouchardRessourceService = mouchardRessourceService;
	}
	
	
	
	
	
	

}
