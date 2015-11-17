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
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

import cm.dita.constant.IConstance;
import cm.dita.entities.user.AccessRessource;
import cm.dita.entities.user.Group;
import cm.dita.entities.user.GroupAccessRessource;
import cm.dita.entities.user.Role;
import cm.dita.service.domaine.inter.IMouchardRessourceService;
import cm.dita.service.domaine.inter.user.IAccessRessourceService;
import cm.dita.service.domaine.inter.user.IGroupAccessRessourceService;
import cm.dita.service.domaine.inter.user.IGroupService;
import cm.dita.service.domaine.inter.user.IRoleGroupService;
import cm.dita.utils.Messages;

@ManagedBean(name = "groupBean")
@SessionScoped
public class GroupBean implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = Logger.getLogger(GroupBean.class);
	
	@ManagedProperty(value="#{groupService}")
	IGroupService groupService;
	
	@ManagedProperty(value="#{groupAccessRessourceService}")
	IGroupAccessRessourceService groupAccessRessourceService;
	
	@ManagedProperty(value="#{roleGroupService}")
	IRoleGroupService roleGroupService;
	
	@ManagedProperty(value="#{accessRessourceService}")
	IAccessRessourceService accessRessourceService;
	
	@ManagedProperty(value="#{mouchardRessourceService}")
	 private IMouchardRessourceService mouchardRessourceService;
	
	private List<Group> groupList = new ArrayList<Group>();
	private Group group;
	private Group group_edit;
	private List<Group> filteredGroup=new ArrayList<Group>();
	private GroupDataModel groupListDataModel;		
	private Group[]  selectedGroup;
	
	private List<AccessRessource>  selectedRessources;
	private List<AccessRessource> ressourcesList;
	private RessourcesDataModel ressourcesListDataModel;
	private List<AccessRessource> listRessoureBlocs;
	
	
	

	/**
	 * Initialisation des valeurs
	 * @param
	 * @return
	 * 
	 */
	@PostConstruct    
	    public void init(){
		this.groupList=groupService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});			
		 this.groupListDataModel= new GroupDataModel(groupList);
		 mouchardRessourceService.tracage("Liste des groupes", "listing",null, "Groupe");
		
	    }
	
	
	//CLICK SUR LES BUTTONS ajout et EDIT
 	public void ajoutEvent(ActionEvent actionEvent) {		 		
    	group = new Group();			    	
    	SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2);    	
    	group.setDateCreation(sdf.format(new Date()));
    	
    	this.listRessoureBlocs= accessRessourceService.listBlocAccess();
 	    for(int i=0;i<listRessoureBlocs.size();i++){
 	    	listRessoureBlocs.get(i).setListRessourceDuBloc(accessRessourceService.listAccessDuBloc(listRessoureBlocs.get(i).getCode_bloc()));
 	    	//listRessoureBlocs.get(i).getSelectRessourceDuBloc().clear();
 	    }
    	
    }
 	
 	/**
 	 * 
 	 * @param id de l'group a charger
 	 * @return l'group choisi
 	 */
 	
 	public void editEvent(Group group) { 	
 	    this.group=new Group(groupService.load(group.getIdGroup()));
 	 //this.selectedRessources=accessRessourceService.listOfAccess4Group(this.group); 	
 	 //this.ressourcesListDataModel= new RessourcesDataModel(this.selectedRessources);
 	   this.listRessoureBlocs= accessRessourceService.listBlocAccess();
	    for(int i=0;i<listRessoureBlocs.size();i++){
	    	listRessoureBlocs.get(i).setListRessourceDuBloc(accessRessourceService.listAccessDuBloc(listRessoureBlocs.get(i).getCode_bloc()));
	    	listRessoureBlocs.get(i).setSelectRessourceDuBloc(accessRessourceService.listAccessDuBlocSelect(this.group.getIdGroup(), listRessoureBlocs.get(i).getCode_bloc(),1));
	    	
	    }  
        
     }
 	
 	
 	public void viewEvent(Group group) { 	
 	    this.group=new Group(groupService.load(group.getIdGroup()));
 	 //this.selectedRessources=accessRessourceService.listOfAccess4Group(this.group); 	
 	 //this.ressourcesListDataModel= new RessourcesDataModel(this.selectedRessources);
 	   this.listRessoureBlocs= accessRessourceService.listBlocAccess();
	    for(int i=0;i<listRessoureBlocs.size();i++){
	    	listRessoureBlocs.get(i).setListRessourceDuBloc(accessRessourceService.listAccessDuBloc(listRessoureBlocs.get(i).getCode_bloc()));
	    	listRessoureBlocs.get(i).setSelectRessourceDuBloc(accessRessourceService.listAccessDuBlocSelect(this.group.getIdGroup(), listRessoureBlocs.get(i).getCode_bloc(),2));
	    	
	    }  
        
     }
 	
 	public void deleteEvent(Group group) { 	
 	 	
 	    this.group=group;
 	 this.selectedRessources=accessRessourceService.listOfAccess4Group(this.group); 	
 	 this.ressourcesListDataModel= new RessourcesDataModel(this.selectedRessources);
        
     }
/**
* Ajoute un group
* @param group
* @return group
*/
 	
 	public void create(ActionEvent actionEvent) {
    	 FacesContext context = FacesContext.getCurrentInstance();
    	try{	     	   
    		int code=(int) groupService.saveReturnID(group);
    		group.setIdGroup(code);  
    		groupAccessRessourceService.createGroupAndAccess(this.group,this.listRessoureBlocs);   		
	     	 mouchardRessourceService.mouchard("Ajout du groupe :"+this.group.getIdGroup(), "ajout",this.group.getDateUseToSortData(), "Groupe",this.group.getIdGroup(),this.group.toString());
				
		        init();//mise � jour de la liste			    	
		        
		        FacesMessage message = Messages.getMessage("messages", "global.gestion.reussi", null);
		    	message.setSeverity(FacesMessage.SEVERITY_INFO);
		        context.addMessage(null, message);
	   
    	
    	 }catch(Exception e){
    		 e.printStackTrace();
    		 mouchardRessourceService.mouchard("Echec ajout du groupe :"+this.group.getIdGroup(), "ajout",this.group.getDateUseToSortData(), "Groupe",this.group.getIdGroup(),this.group.toString());
 			
    		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
		    	message.setSeverity(FacesMessage.SEVERITY_WARN);
		        context.addMessage(null, message);
    	 }		    	  
	        group = new Group();
	     
    }
 	/**
 	 * Modification des infrmations et des privilèges d'un groupe
 	 * @param groupe
 	 */
 	
 	public void update(ActionEvent actionEvent) {
   	 FacesContext context = FacesContext.getCurrentInstance();
   	try{	     	   
	     	  groupAccessRessourceService.updateGroupAndAccess(this.group,this.listRessoureBlocs);   		
	     	 mouchardRessourceService.mouchard("Modification du groupe :"+this.group.getIdGroup(), "Modification",this.group.getDateUseToSortData(), "Groupe",this.group.getIdGroup(),this.group.toString());
				
		        init();//mise � jour de la liste			    	
		        
		        FacesMessage message = Messages.getMessage("messages", "global.gestion.reussi", null);
		    	message.setSeverity(FacesMessage.SEVERITY_INFO);
		        context.addMessage(null, message);
	   
   	
   	 }catch(Exception e){
   		 e.printStackTrace();
   		 mouchardRessourceService.mouchard("Echec de modification du groupe :"+this.group.getIdGroup(), "Modification",this.group.getDateUseToSortData(), "Groupe",this.group.getIdGroup(),this.group.toString());
			
   		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
		    	message.setSeverity(FacesMessage.SEVERITY_WARN);
		        context.addMessage(null, message);
   	 }		    	  
	        group = new Group();
	     
   }
 	
 	
 /**
  * Mise à jour d'un group
  * @param group
  * @return group
  */
 	
 	 public void edition(ActionEvent actionEvent) {
    	 FacesContext context = FacesContext.getCurrentInstance();
    	
    	 try{
    		 
    		 	groupService.update(group);
		    	init();//mise � jour de la liste
		    	mouchardRessourceService.tracage("Modification du groupe "+group.getGroupName()+" ("+group.getIdGroup()+")", "modification",group.getDateUseToSortData(), "Groupe");
	 			
		    	FacesMessage message = Messages.getMessage("messages", "global.gestion.modifier", null);
		    	message.setSeverity(FacesMessage.SEVERITY_INFO);
		        context.addMessage(null, message);
 	    
			  
    	 }catch(Exception e){
    		 mouchardRessourceService.tracage("Echec modification du groupe "+group.getGroupName()+" ("+group.getIdGroup()+")", "modification",group.getDateUseToSortData(), "Groupe");
	 			 
    		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
		    	message.setSeverity(FacesMessage.SEVERITY_WARN);
		        context.addMessage(null, message);
     	 }finally{
     		group = new Group();
     	 }
    }
 	 
 	/**
 	 * Supprime un group
 	 * @param Group a supprimer
 	 *@return void
 	 */
 	 
 	public void delete(ActionEvent actionEvent) {
    	
    	 FacesContext context = FacesContext.getCurrentInstance();
    	 RequestContext requestContext = RequestContext.getCurrentInstance();
       try{
    	   
    	   if((roleGroupService.getCountRoleOfGroup(group.getIdGroup())>0)){
			   mouchardRessourceService.tracage(" Erreur de suppression du groupe ("+group.getGroupName()+") ", "suppression",group.getDateUseToSortData(), "Groupe");
			    	
			   FacesMessage message = Messages.getMessage("messages", "group.echec.delete", null);
		    	message.setSeverity(FacesMessage.SEVERITY_WARN);
		        context.addMessage(null, message);
	   	}else{
    		   groupService.deleteVersusDesabled(group);
    	   mouchardRessourceService.tracage("Suppression du groupe "+group.getGroupName()+" ("+group.getIdGroup()+")", "suppression",group.getDateUseToSortData(), "Groupe");
			
    	   init();//mise à jour de la liste
	    	
	    	 FacesMessage message = Messages.getMessage("messages", "global.gestion.delete", null);
		    	message.setSeverity(FacesMessage.SEVERITY_INFO);
		        context.addMessage(null, message);				    	
	        requestContext.execute("PF('deleteDialog').hide()");	 
	   	}
    	   
		    	   	  
    	   
       }catch(Exception e){
  		 e.printStackTrace();
  		mouchardRessourceService.tracage("Echec de suppression du groupe "+group.getGroupName()+" ("+group.getIdGroup()+")", "suppression",group.getDateUseToSortData(), "Groupe");
			
  		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
	    	message.setSeverity(FacesMessage.SEVERITY_WARN);
	        context.addMessage(null, message);
  	 }finally{
  		selectedGroup=null;
 	   group=new Group();
  	 }
 
    }
 	
 	public void AddRessources2GroupEvent(Group group) { 		
 	    this.group = group;
 	    this.ressourcesList=accessRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
 	   this.ressourcesListDataModel= new RessourcesDataModel(this.ressourcesList);
 	    this.selectedRessources=accessRessourceService.listOfAccess4Group(this.group);
 	    this.listRessoureBlocs= accessRessourceService.listBlocAccess();
 	    for(int i=0;i<listRessoureBlocs.size();i++){
 	    	listRessoureBlocs.get(i).setListRessourceDuBloc(accessRessourceService.listAccessDuBloc(listRessoureBlocs.get(i).getCode_bloc()));
 	    }
 	    //JOptionPane.showMessageDialog(null, this.listRessoureBlocs.size());
 	 
        
     }
 	
 	
 	/**
 	 * @see Ajoute un groupe a un group
 	 * @param list groupe
 	 * @param group
 	 * @return le group associe au groupe
 	 */
 	
 	public void AddGroup2Access(){ 		
 		 FacesContext context = FacesContext.getCurrentInstance();
 		RequestContext requestContext = RequestContext.getCurrentInstance();
       try{
    	  // JOptionPane.showMessageDialog(null, selectedRessources.size());
    	   selectedRessources.clear();
    	   for(int i=0;i<listRessoureBlocs.size();i++){    		  
    		  for(int j=0;j<listRessoureBlocs.get(i).getSelectRessourceDuBloc().size();j++)  
    			  selectedRessources.add(new AccessRessource(Integer.parseInt(listRessoureBlocs.get(i).getSelectRessourceDuBloc().get(j))));
    		   listRessoureBlocs.get(i).getSelectRessourceDuBloc().clear();
    	   }
    	   
    	   if(selectedRessources.size()>0){   
       		   requestContext.execute("PF('addDialogAccess').show()");
       		   
       	   }else{
       		   
       		   requestContext.execute("PF('deleteDialogAccess').show()");
       		  
       		   
       	   }
       	   	  
    	   
       }catch(Exception e){
  		 e.printStackTrace();
  		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
	    	message.setSeverity(FacesMessage.SEVERITY_WARN);
	        context.addMessage(null, message);
  	 }finally{
  		 
  		//this.selectedRessources=null;
  		//this.group=null;
  	 }
 		
 	}
 	
 	
 	
 	/*public void addAcess(ActionEvent actionEvent){
 		FacesContext context = FacesContext.getCurrentInstance();
 		RequestContext requestContext = RequestContext.getCurrentInstance();
 		try{
 			
 			 groupAccessRessourceService.saveAcess2Group(this.group,this.selectedRessources);		    	  
 		   FacesMessage message = Messages.getMessage("messages", "global.gestion.reussi", null);
	    	message.setSeverity(FacesMessage.SEVERITY_INFO);
	        context.addMessage(null, message);
	       
 		}catch(Exception e){
 			e.printStackTrace();
 	  		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
 		    	message.setSeverity(FacesMessage.SEVERITY_WARN);
 		        context.addMessage(null, message);
 		}finally{
 			this.selectedRessources=null;
 	  		this.group=null;
 		}
 		
 	}*/
 	
 	/**
 	 * Suppression de tous les groupes du role
 	 */
 	
 	/*public void deleteAcess(ActionEvent actionEvent){
 		FacesContext context = FacesContext.getCurrentInstance();
 		RequestContext requestContext = RequestContext.getCurrentInstance();
 		try{
 			
 			 groupAccessRessourceService.deleteAccess2Group(this.group);
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
 			this.selectedRessources=null;
 	  		this.group=null;
 		}
 		
 	}*/
 	
 	/**
 	 * Validation de l'existance 
 	 * @return
 	 */
 	public void validateName(FacesContext context, UIComponent component, Object value) throws ValidatorException {
 		  String valeur = (String) value;
 		
 		 group.setGroupName(valeur);
 		 if(groupService.userExiste(group)){
 			mouchardRessourceService.tracage("Tentative d'ajout du groupe (nom existant) "+group.getGroupName(), "ajout",group.getDateUseToSortData(), "Groupe");
			
    		FacesMessage message = Messages.getMessage("messages", "group.nom.existe", null);
 	    	message.setSeverity(FacesMessage.SEVERITY_ERROR);
 	    	 throw new ValidatorException(message);
 		 }
 		 
 		  
 		}
	
	

	public IGroupService getGroupService() {
		return groupService;
	}


	public IGroupAccessRessourceService getGroupAccessRessourceService() {
		return groupAccessRessourceService;
	}


	public IAccessRessourceService getAccessRessourceService() {
		return accessRessourceService;
	}


	public List<AccessRessource> getSelectedRessources() {
		return selectedRessources;
	}


	public List<AccessRessource> getRessourcesList() {
		return ressourcesList;
	}


	public RessourcesDataModel getRessourcesListDataModel() {
		return ressourcesListDataModel;
	}


	public void setGroupService(IGroupService groupService) {
		this.groupService = groupService;
	}


	public void setGroupAccessRessourceService(
			IGroupAccessRessourceService groupAccessRessourceService) {
		this.groupAccessRessourceService = groupAccessRessourceService;
	}


	public void setAccessRessourceService(
			IAccessRessourceService accessRessourceService) {
		this.accessRessourceService = accessRessourceService;
	}


	public void setSelectedRessources(List<AccessRessource> selectedRessources) {
		this.selectedRessources = selectedRessources;
	}


	public void setRessourcesList(List<AccessRessource> ressourcesList) {
		this.ressourcesList = ressourcesList;
	}


	public void setRessourcesListDataModel(
			RessourcesDataModel ressourcesListDataModel) {
		this.ressourcesListDataModel = ressourcesListDataModel;
	}


	public List<Group> getGroupList() {
		return groupList;
	}


	public void setGroupList(List<Group> groupList) {
		this.groupList = groupList;
	}


	public Group getGroup() {
		return group;
	}


	public void setGroup(Group group) {
		this.group = group;
	}


	public List<Group> getFilteredGroup() {
		return filteredGroup;
	}


	public void setFilteredGroup(List<Group> filteredGroup) {
		this.filteredGroup = filteredGroup;
	}




	public List<AccessRessource> getListRessoureBlocs() {
		return listRessoureBlocs;
	}


	public void setListRessoureBlocs(List<AccessRessource> listRessoureBlocs) {
		this.listRessoureBlocs = listRessoureBlocs;
	}


	public Group[] getSelectedGroup() {
		return selectedGroup;
	}


	public void setSelectedGroup(Group[] selectedGroup) {
		this.selectedGroup = selectedGroup;
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


	public IRoleGroupService getRoleGroupService() {
		return roleGroupService;
	}


	public void setRoleGroupService(IRoleGroupService roleGroupService) {
		this.roleGroupService = roleGroupService;
	}
	
	
	
	

}
