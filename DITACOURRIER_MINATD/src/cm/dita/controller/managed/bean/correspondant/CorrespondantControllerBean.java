package cm.dita.controller.managed.bean.correspondant;

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




import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import cm.dita.constant.IConstance;
import cm.dita.controller.managed.bean.user.privileges.RessourcesDataModel;
import cm.dita.entities.Correspondant;
import cm.dita.entities.user.User;
import cm.dita.service.domaine.inter.ICorrespondantRessourceService;
import cm.dita.service.domaine.inter.IMouchardRessourceService;
import cm.dita.service.domaine.inter.user.IUserService;
import cm.dita.utils.Messages;



@ManagedBean(name = "correspondantControllerBean")
@SessionScoped
public class CorrespondantControllerBean implements Serializable{
	
	/**
	 * 
	 */
	
	private static final Logger LOG = Logger.getLogger(CorrespondantControllerBean.class);
	private static final long serialVersionUID = 1L;



		private List<Correspondant> correspondantList = new ArrayList<Correspondant>();
		private Correspondant correspondant;
		private List<Correspondant> filteredCorrespondants=new ArrayList<Correspondant>();
		private CorrespondantDataModel correspondantListDataModel;		
		private Correspondant[]  selectedCorrespondants;
		
		private int operation;		
		RessourcesDataModel ressourceListDataModel;		
	    private User userconnected;
	    
		//Spring Correspondant Service is injected...
		@ManagedProperty(value="#{correspondantRessourceService}")
		ICorrespondantRessourceService correspondantRessourceService;
	
		@ManagedProperty(value="#{mouchardRessourceService}")
		private IMouchardRessourceService mouchardRessourceService;
		
		//Spring User Service is injected...
		@ManagedProperty(value="#{userService}")
		private IUserService userService;   
 
		public CorrespondantControllerBean() {
    	correspondant = new Correspondant();
    	
    }
    
   @PostConstruct    
    public void init(){
    
		UserDetails user_secutity = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		userconnected =userService.findByLogin(user_secutity.getUsername());
		
	   correspondantList=correspondantRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});		
	   correspondantListDataModel= new CorrespondantDataModel(correspondantList);	   
	   mouchardRessourceService.tracage("Liste les correspondants", userconnected);
   	   
    	
    }
 
    public void ajoutEvent(ActionEvent actionEvent) {
    	
    	operation=1; //ajout
    	correspondant = new Correspondant();
    
    	SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2);    	
    	correspondant.setDateCreation(sdf.format(new Date()));

    }
    
    public void deleteMultipleEvent() {
    	
		FacesContext context = FacesContext.getCurrentInstance();
	   	RequestContext requestContext = RequestContext.getCurrentInstance();
	   	
 	   if(selectedCorrespondants.length>0){
 		  requestContext.execute("deleteDialog.show()");
	   }
 	   else{
 		   mouchardRessourceService.tracage("Tentative de suppression de correspondant sans sélection préalable ", userconnected);
   		   FacesMessage message = Messages.getMessage("messages", "correspondant.suppression.vide", null);
	    	message.setSeverity(FacesMessage.SEVERITY_ERROR);
	        context.addMessage(null, message);
 	   }
    }
  
    // modification du profil    
    public void profilEvent(ActionEvent actionEvent){
    	
    	operation=3;//profil
    		
    }
 
    
    public void editEvent(int id) {
        
    	operation=2; //edition   
    	
        this.correspondant = new Correspondant(correspondantRessourceService.load(id));       

    }
 
    
    /**
     * @see fonction d'ajout d'un utilisateur
     * @param correspondant
     * 		prend en entree un utilisateur
     * 
     * @return vide
     */
    

	public void create() {
    	 FacesContext context = FacesContext.getCurrentInstance();
    	try{
    		
		    		correspondant.setDelate(false);
			    	this.correspondant=correspondantRessourceService.save(correspondant);	
			    	JOptionPane.showMessageDialog(null, "  enregistre le mouchard");
			    	mouchardRessourceService.tracage("a ajouté le correspondant "+correspondant.getNom(), userconnected);
			    	
			  
			    	init();//mise ï¿½ jour de la liste
			    
			    	FacesMessage message = Messages.getMessage("messages", "global.gestion.reussi", null);
			    	message.setSeverity(FacesMessage.SEVERITY_INFO);
			        context.addMessage(null, message);
		  
    	 }catch(Exception e){
    		 e.printStackTrace();
    		 mouchardRessourceService.tracage("Tentative et echec d'ajout du correspondant ", userconnected);
    		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
		    	message.setSeverity(FacesMessage.SEVERITY_WARN);
		        context.addMessage(null, message);
    	 }finally{
    		 
    	
 	        correspondant = new Correspondant();
 	        
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
	 * @param correspondant
	 */
	public void update() {
   	 FacesContext context = FacesContext.getCurrentInstance();
   	try{
   		
			    	correspondantRessourceService.update(correspondant);
			    	init();//mise ï¿½ jour de la liste
			    	mouchardRessourceService.tracage("Modification d'un correspondant "+correspondant.getNom(), userconnected);
			    	FacesMessage message = Messages.getMessage("messages", "global.gestion.reussi", null);
			    	message.setSeverity(FacesMessage.SEVERITY_INFO);
			        context.addMessage(null, message);
    	
   	 }catch(Exception e){
   		 e.printStackTrace();
   		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
		    	message.setSeverity(FacesMessage.SEVERITY_WARN);
		        context.addMessage(null, message);
   	 }finally{
   		correspondant = new Correspondant();
        
   	 }
   	
	     
   }
   
 
    public void delete(ActionEvent actionEvent) {
    	
    	 FacesContext context = FacesContext.getCurrentInstance();
    	 RequestContext requestContext = RequestContext.getCurrentInstance();
       try{
    	  
    	   if(selectedCorrespondants.length>0)
	    	   for(int i=0;i<selectedCorrespondants.length;i++){	
	    		  
	    		   correspondantRessourceService.deleteVersusDesabled(selectedCorrespondants[i], IConstance.FIELD_DELETE);
	    		   mouchardRessourceService.tracage("Suppression du correspondant "+selectedCorrespondants[i].getNom(), userconnected);
		    	}
    	   else   { 	 
		    		correspondantRessourceService.deleteVersusDesabled(correspondant, IConstance.FIELD_DELETE);
		    		mouchardRessourceService.tracage("Suppression du correspondant "+correspondant.getNom(), userconnected);
    	   }
    	
		    	init();//mise Ã  jour de la liste
		    	
		    	 FacesMessage message = Messages.getMessage("messages", "global.gestion.delete", null);
			    	message.setSeverity(FacesMessage.SEVERITY_INFO);
			        context.addMessage(null, message);				    	
		        requestContext.execute("deleteDialog.hide()");	
    	  
    	   
       }catch(Exception e){
    	   e.printStackTrace();
    	   FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
	    	message.setSeverity(FacesMessage.SEVERITY_WARN);
	        context.addMessage(null, message);
  	 }finally{
  		selectedCorrespondants=null;
 	   correspondant=null;
 
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
 		 correspondant.setNom(valeur);
 		 if(correspondantRessourceService.correspondantExisteWithName(correspondant)){
 			 mouchardRessourceService.tracage("Tentative de modification du correspondant(nom complet existant) "+correspondant.getNom(), "modification",correspondant.getDateUseToSortData(), "Correspondant");
			
    		FacesMessage message = Messages.getMessage("messages", "correspondant.nom.validator", null);
	    	message.setSeverity(FacesMessage.SEVERITY_ERROR);	      
 	    	 throw new ValidatorException(message);
 		 }
 		
 		  
 		}

	/**
	 * @return the correspondantRessourceService
	 */
	public ICorrespondantRessourceService getCorrespondantRessourceService() {
		return correspondantRessourceService;
	}

	/**
	 * @param correspondantRessourceService the correspondantRessourceService to set
	 */
	public void setCorrespondantRessourceService(
			ICorrespondantRessourceService correspondantRessourceService) {
		this.correspondantRessourceService = correspondantRessourceService;
	}

	/**
	 * @return the correspondantList
	 */
	public List<Correspondant> getCorrespondantList() {
		return correspondantList;
	}

	/**
	 * @param correspondantList the correspondantList to set
	 */
	public void setCorrespondantList(List<Correspondant> correspondantList) {
		this.correspondantList = correspondantList;
	}

	/**
	 * @return the correspondant
	 */
	public Correspondant getCorrespondant() {
		return correspondant;
	}

	/**
	 * @param correspondant the correspondant to set
	 */
	public void setCorrespondant(Correspondant correspondant) {
		this.correspondant = correspondant;
	}

	/**
	 * @return the filteredCorrespondants
	 */
	public List<Correspondant> getFilteredCorrespondants() {
		return filteredCorrespondants;
	}

	/**
	 * @param filteredCorrespondants the filteredCorrespondants to set
	 */
	public void setFilteredCorrespondants(List<Correspondant> filteredCorrespondants) {
		this.filteredCorrespondants = filteredCorrespondants;
	}

	/**
	 * @return the correspondantListDataModel
	 */
	public CorrespondantDataModel getCorrespondantListDataModel() {
		return correspondantListDataModel;
	}

	/**
	 * @param correspondantListDataModel the correspondantListDataModel to set
	 */
	public void setCorrespondantListDataModel(
			CorrespondantDataModel correspondantListDataModel) {
		this.correspondantListDataModel = correspondantListDataModel;
	}

	/**
	 * @return the selectedCorrespondants
	 */
	public Correspondant[] getSelectedCorrespondants() {
		return selectedCorrespondants;
	}

	/**
	 * @param selectedCorrespondants the selectedCorrespondants to set
	 */
	public void setSelectedCorrespondants(Correspondant[] selectedCorrespondants) {
		this.selectedCorrespondants = selectedCorrespondants;
	}

	/**
	 * @return the operation
	 */
	public int getOperation() {
		return operation;
	}

	/**
	 * @param operation the operation to set
	 */
	public void setOperation(int operation) {
		this.operation = operation;
	}

	/**
	 * @return the ressourceListDataModel
	 */
	public RessourcesDataModel getRessourceListDataModel() {
		return ressourceListDataModel;
	}

	/**
	 * @param ressourceListDataModel the ressourceListDataModel to set
	 */
	public void setRessourceListDataModel(RessourcesDataModel ressourceListDataModel) {
		this.ressourceListDataModel = ressourceListDataModel;
	}

	/**
	 * @return the userconnected
	 */
	public User getUserconnected() {
		return userconnected;
	}

	/**
	 * @param userconnected the userconnected to set
	 */
	public void setUserconnected(User userconnected) {
		this.userconnected = userconnected;
	}

	/**
	 * @return the mouchardRessourceService
	 */
	public IMouchardRessourceService getMouchardRessourceService() {
		return mouchardRessourceService;
	}

	/**
	 * @param mouchardRessourceService the mouchardRessourceService to set
	 */
	public void setMouchardRessourceService(
			IMouchardRessourceService mouchardRessourceService) {
		this.mouchardRessourceService = mouchardRessourceService;
	}

	/**
	 * @return the userService
	 */
	public IUserService getUserService() {
		return userService;
	}

	/**
	 * @param userService the userService to set
	 */
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
    

    

    
}