package cm.dita.controller.managed.bean.user.espace;

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
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
//import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import cm.dita.service.domaine.inter.IEspaceCourrierRessourceService;
import cm.dita.service.domaine.inter.IEspaceRessourceService;
import cm.dita.service.domaine.inter.IMouchardRessourceService;
import cm.dita.service.domaine.inter.IStatutsRessourceService;
import cm.dita.service.domaine.inter.ITypescourriersRessourceService;
import cm.dita.service.domaine.inter.user.IUserService;
import cm.dita.utils.Messages;
import cm.dita.constant.IConstance;
import cm.dita.constant.ISessionConstant;
import cm.dita.entities.Espace;
import cm.dita.entities.user.User;



@ManagedBean(name = "espaceBean")
@SessionScoped
public class EspaceBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = Logger.getLogger(EspaceBean.class);
	
		//Spring Espace Service is injected...
		@ManagedProperty(value="#{espaceRessourceService}")
		IEspaceRessourceService espaceRessourceService;
		
		@ManagedProperty(value="#{userService}")
		IUserService userService;
		
		@ManagedProperty(value="#{espaceCourrierRessourceService}")
		private IEspaceCourrierRessourceService espaceCourrierRessourceService;
		
		@ManagedProperty(value="#{mouchardRessourceService}")
		private IMouchardRessourceService mouchardRessourceService;
		
		private List<Espace> espaceList = new ArrayList<Espace>();
		private Espace espace;
		private List<Espace> filteredEspace=new ArrayList<Espace>();
		private EspaceDataModel espaceListDataModel;		
		private Espace[]  selectedEspace;
		//private User 
		
		/**
		 * Initialisation des valeurs
		 * @param
		 * @return
		 * 
		 */
		@PostConstruct    
		    public void init(){
			this.espaceList=espaceRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});			
			 this.espaceListDataModel= new EspaceDataModel(espaceList);	
			// mouchardRessourceService.tracage("Liste des espaces", "listing",null, "Espace");
		       
			
		    }
		
		//POUR LE POLL DE  MISE A JOUR
			
		 	public void updateByPoll(ActionEvent actionEvent){ 		
		 		init();//mise � jour de la liste	 		
		 	}
		//CLICK SUR LES BUTTONS ajout et EDIT
		 	public void ajoutEvent(ActionEvent actionEvent) {		 		
		    	espace = new Espace();			    	
		    	SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2);    	
		    	espace.setDateCreation(sdf.format(new Date()));
		    	
		    }
		 	
		 	/**
		 	 * 
		 	 * @param id de l'espace a charger
		 	 * @return l'espace choisi
		 	 */
		 	
		 	public void editEvent(Espace espace) {
		 		
		 	    this.espace = new Espace(espaceRessourceService.load(espace.getId()));
		 	    
		     }
		 	
		 	public void detailsEvent(Espace espace) {
		 		
		 	    this.espace = espaceRessourceService.load(espace.getId());
		 	   mouchardRessourceService.tracage("Visualisation de l'espace ("+espace.getNomespace()+") ", "visualisation",espace.getDateUseToSortData(), "Espace");
			    
		        
		     }
	/**
	 * Ajoute un espace
	 * @param espace
	 * @return espace
	 */
		 	
		 	public void create(ActionEvent actionEvent) {
		    	 FacesContext context = FacesContext.getCurrentInstance();
		    	try{	
		    		
			    		espaceRessourceService.save(espace);	
				        init();//mise � jour de la liste	 	       
				        
				        mouchardRessourceService.tracage("Ajout de l'espace ("+espace.getNomespace()+") ", "ajout",espace.getDateUseToSortData(), "Espace");
				        
				        FacesMessage message = Messages.getMessage("messages", "global.gestion.reussi", null);
				    	message.setSeverity(FacesMessage.SEVERITY_INFO);
				        context.addMessage(null, message);
			    
		    	 }catch(Exception e){
		    		 e.printStackTrace();
		    		 mouchardRessourceService.tracage("Echec de l'ajout de l'espace ("+espace.getNomespace()+") ", "ajout",espace.getDateUseToSortData(), "Espace");
		    		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
				    	message.setSeverity(FacesMessage.SEVERITY_WARN);
				        context.addMessage(null, message);
		    	 }		    	  
			        espace = new Espace();
			     
		    }
		 	
		 	
		 /**
		  * Mise à jour d'un espace
		  * @param espace
		  * @return espace
		  */
		 	
		 	 public void edition(ActionEvent actionEvent) {
		    	 FacesContext context = FacesContext.getCurrentInstance();
		    	
		    	 try{
		    		 if(espaceRessourceService.userExiste(espace)){
				    		mouchardRessourceService.tracage("Tentative de modfication de l'espace (nom existant) "+espace.getNomespace(), "modification",espace.getDateUseToSortData(), "Espace");
							
				    		FacesMessage message = Messages.getMessage("messages", "espace.nom.existe", null);
					    	message.setSeverity(FacesMessage.SEVERITY_ERROR);
					        context.addMessage(null, message);
				    	}else{
			    		 	espaceRessourceService.update(espace);
					    	init();//mise � jour de la liste
					    	mouchardRessourceService.tracage("Modification de l'espace ("+espace.getNomespace()+") ", "modification",espace.getDateUseToSortData(), "Espace");
						       
					    	espace = new Espace();
					    	FacesMessage message = Messages.getMessage("messages", "global.gestion.modifier", null);
					    	message.setSeverity(FacesMessage.SEVERITY_INFO);
					        context.addMessage(null, message);
				    	}
					  
		    	 }catch(Exception e){
		    		 mouchardRessourceService.tracage(" Echec modification de l'espace ("+espace.getNomespace()+") ", "modification",espace.getDateUseToSortData(), "Espace");
					   
		    		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
				    	message.setSeverity(FacesMessage.SEVERITY_WARN);
				        context.addMessage(null, message);
		     	 }
		    }
		 	 
		 	/**
		 	 * Supprime un espace
		 	 * @param Espace a supprimer
		 	 *@return void
		 	 */
		 	 
		 	public void delete(ActionEvent actionEvent) {
		    	
		    	 FacesContext context = FacesContext.getCurrentInstance();
		    	 RequestContext requestContext = RequestContext.getCurrentInstance();
		       try{
		    	   
		    	   //Cas selectiond
		    	  
		    	  /* if(selectedEspace.length>0)
			    	   for(int i=0;i<selectedEspace.length;i++){
			    		   //JOptionPane.showMessageDialog(null, selectedEspace[i].getNomespace());
				    		espaceRessourceService.deleteVersusDesabled(selectedEspace[i], IConstance.FIELD_DELETE);				    		
				    	}
		    	   else{
			    	   if(espace!=null){
			    		   if(userService.getCountUserParEspace(espace.getId())>0 &&(espaceCourrierRessourceService.getCountCourrierParEspace(espace.getId())>0) )
			    		   
			    	   	}else
			    	   		espaceRessourceService.deleteVersusDesabled(espace, IConstance.FIELD_DELETE);
			    	   }
		    	   }*/
		    	       
			    		   if((userService.getCountUserParEspace(espace.getId())>0)||(espaceCourrierRessourceService.getCountCourrierParEspace(espace.getId())>0) ){
			    			   mouchardRessourceService.tracage(" Erreur de suppression de l'espace ("+espace.getNomespace()+") ", "suppression",espace.getDateUseToSortData(), "Espace");
							    	
			    			   FacesMessage message = Messages.getMessage("messages", "espace.echec.delete", null);
						    	message.setSeverity(FacesMessage.SEVERITY_WARN);
						        context.addMessage(null, message);
			    	   	}else{
			    	   		espaceRessourceService.deleteVersusDesabled(espace, IConstance.FIELD_DELETE);
			    	   		mouchardRessourceService.tracage("Suppression de l'espace ("+espace.getNomespace()+") ", "suppression",espace.getDateUseToSortData(), "Espace");
						    
			    	   		selectedEspace=null;
					    	   espace=null;
							    	init();//mise à jour de la liste
							    	
							    	 FacesMessage message = Messages.getMessage("messages", "global.gestion.delete", null);
								    	message.setSeverity(FacesMessage.SEVERITY_INFO);
								        context.addMessage(null, message);				    	
							        requestContext.execute("deleteDialog.hide()");	
			    	   
			    	   	}
		    	       	  
		    	   
		       }catch(Exception e){
		  		 e.printStackTrace();
		  		mouchardRessourceService.tracage("Echec de suppression de l'espace ("+espace.getNomespace()+") ", "suppression",espace.getDateUseToSortData(), "Espace");
			    
		  		 	FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
			    	message.setSeverity(FacesMessage.SEVERITY_WARN);
			        context.addMessage(null, message);
		  	 }
		 
		    }
		 	
		 /**
		  * Supprime une liste d'espaces
		  * @param espaceList : liste de  l'espace a supprimer
		  * @return 
		  */
		 	
		 
		 	  // modification du profil    
		    public void profilEvent(ActionEvent actionEvent){
		    
		    }

		    /**
		 	 * Validation de l'existance 
		 	 * @return
		 	 */
		 	public void validateName(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		 		  String valeur = (String) value;
		 	
		 		 espace.setNomespace(valeur);
		 		 if(espaceRessourceService.userExiste(espace)){
		 			mouchardRessourceService.tracage("Tentative d'ajout de l'espace (nom existant) "+espace.getNomespace(), "ajout",espace.getDateUseToSortData(), "Espace");
					
		    		FacesMessage message = Messages.getMessage("messages", "espace.nom.existe", null);
			    	message.setSeverity(FacesMessage.SEVERITY_ERROR);
			        
		 	    	 throw new ValidatorException(message);
		 		 }
		 		
		 		  
		 		}		    
		 	
		
		public IEspaceRessourceService getEspaceRessourceService() {
			return espaceRessourceService;
		}
		public void setEspaceRessourceService(
				IEspaceRessourceService espaceRessourceService) {
			this.espaceRessourceService = espaceRessourceService;
		}
		public List<Espace> getEspaceList() {
			return espaceList;
		}
		public void setEspaceList(List<Espace> espaceList) {
			this.espaceList = espaceList;
		}
		public Espace getEspace() {
			return espace;
		}
		public void setEspace(Espace espace) {
			this.espace = espace;
		}
		public List<Espace> getFilteredEspace() {
			return filteredEspace;
		}
		public void setFilteredEspace(List<Espace> filteredEspace) {
			this.filteredEspace = filteredEspace;
		}
		public EspaceDataModel getEspaceListDataModel() {			
			return espaceListDataModel;
		}
		public void setEspaceListDataModel(EspaceDataModel espaceListDataModel) {
			this.espaceListDataModel = espaceListDataModel;
		}
		public Espace[] getSelectedEspace() {
			return selectedEspace;
		}
		public void setSelectedEspace(Espace[] selectedEspace) {
			this.selectedEspace = selectedEspace;
		}

		public IUserService getUserService() {
			return userService;
		}

		public void setUserService(IUserService userService) {
			this.userService = userService;
		}

		public IEspaceCourrierRessourceService getEspaceCourrierRessourceService() {
			return espaceCourrierRessourceService;
		}

		public void setEspaceCourrierRessourceService(
				IEspaceCourrierRessourceService espaceCourrierRessourceService) {
			this.espaceCourrierRessourceService = espaceCourrierRessourceService;
		}

		public IMouchardRessourceService getMouchardRessourceService() {
			return mouchardRessourceService;
		}

		public void setMouchardRessourceService(
				IMouchardRessourceService mouchardRessourceService) {
			this.mouchardRessourceService = mouchardRessourceService;
		}
		
		
		
		
		
		
}
