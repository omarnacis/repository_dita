package cm.dita.controller.managed.bean.preferences;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
//import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import cm.dita.beans.ApplicationBean;
import cm.dita.constant.IConstance;
import cm.dita.controller.managed.bean.user.privileges.RessourcesDataModel;
import cm.dita.entities.Courriers;
import cm.dita.entities.Image;
import cm.dita.entities.Preferences;
import cm.dita.entities.user.AccessRessource;
import cm.dita.service.domaine.inter.ICourriersRessourceService;
import cm.dita.service.domaine.inter.IImageService;
import cm.dita.service.domaine.inter.IMouchardRessourceService;
import cm.dita.service.domaine.inter.IPreferencesRessourceService;
import cm.dita.service.domaine.inter.user.IAccessRessourceService;
import cm.dita.utils.Messages;
import cm.dita.utils.MethodUtils;
import cm.dita.utils.VigenereCipher;

@ManagedBean(name="preferenceBean")
@SessionScoped
public class PreferencesBean  implements Serializable{
	
	/**
	 * 
	 * 
	 */
	private static final long serialVersionUID = 1L;
		//Spring User Service is injected...
		@ManagedProperty(value="#{preferencesRessourceService}")
		private IPreferencesRessourceService preferencesRessourceService;
		
		private static final Logger LOG = Logger.getLogger(PreferencesBean.class);
		
		@ManagedProperty(value="#{preferencesRessourceService}")
		IPreferencesRessourceService preferenceService;
		
		@ManagedProperty(value="#{IImageService}")
		IImageService mageService;
		
		@ManagedProperty(value="#{mouchardRessourceService}")
		private IMouchardRessourceService mouchardRessourceService;
		
		@ManagedProperty(value="#{applicationBean}")
	    private ApplicationBean applicationScopeBean;
		
		private List<Preferences> preferenceList = new ArrayList<Preferences>();
		private Preferences preference;
		private List<Preferences> filteredPreference=new ArrayList<Preferences>();
		private PreferenceDataModel preferenceListDataModel;		
		private Preferences[]  selectedPreference;
		private String[]  formatDate;
		private UploadedFile file;
		private boolean is_text;
		private Date date;
		
		private int mois;
		private int jour;
		private int heure;
		
		/**
		 * Initialisation des valeurs
		 * @param
		 * @return
		 * 
		 */
		@PostConstruct    
		    public void init(){
			//JOptionPane.showMessageDialog(null, "preference");
			//file = new U
			this.preferenceList=preferenceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});			
			 this.preferenceListDataModel= new PreferenceDataModel(preferenceList);	
			
		    }
		
		
		//CLICK SUR LES BUTTONS ajout et EDIT
	 	public void ajoutEvent(ActionEvent actionEvent) {		 		
	    	preference = new Preferences();			    	
	    	SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2);    	
	    	preference.setDateCreation(sdf.format(new Date()));
	    	
	    }
	 	
	 	/**
	 	 * 
	 	 * @param id de l'preference a charger
	 	 * @return l'preference choisi
	 	 */
	 	
	 	public void editEvent(Preferences preference) {		
	 	    this.preference = preferenceService.load(preference.getIdentifier());
	 	   is_text=false;
	 	   // this.formatDate=IConstance.FORMAT_DATE;
	 	  
	     }
	/**
	* Ajoute un preference
	* @param preference
	* @return preference
	*/
	 	
	 	public void create(ActionEvent actionEvent) {
	    	 FacesContext context = FacesContext.getCurrentInstance();
	    	try{	
	    		if(preference.getPreferenceName().equalsIgnoreCase("LOGO")){   			
	    			
	    				if(this.file!=null){
	    				applicationScopeBean.setIs_text_logo(false);
		    			Image image=new Image();
		    			image.setImage(this.file.getContents());
				    	image.setType(2);
				    	image.setName(this.file.getFileName());
				    	try{
				    	mageService.deleteAndSAve(image);
				    	}catch(Exception e){
				    		
				    	}finally{
				    	String defaultSystemDirFileUpload  = FacesContext.getCurrentInstance().getExternalContext().getRealPath("");	    			
		    			  defaultSystemDirFileUpload=defaultSystemDirFileUpload+File.separator+"uplaoded"+File.separator+"style";
		    			 MethodUtils.copyFile(defaultSystemDirFileUpload+File.separator, this.file.getFileName(), this.file.getInputstream());
		    			 //preference.setPreferenceValue("/uplaoded/style/"+this.file.getFileName()); 
		    			 preference.setPreferenceValue(File.separator+"uplaoded"+File.separator+"style"+File.separator+this.file.getFileName()); 
			    			
		    			 applicationScopeBean.configuration(preference.getPreferenceName(), preference.getPreferenceValue());
				    	}
	    				}else
	    					applicationScopeBean.setIs_text_logo(true);
	    			
	    		}
	    		
	    		if(preference.getPreferenceName().equalsIgnoreCase("IMG_FOND")){ 			
	    			
    				if(this.file!=null){
    				applicationScopeBean.setIs_text_logo(false);
	    			Image image=new Image();
	    			image.setImage(this.file.getContents());
			    	image.setType(3);
			    	image.setName(this.file.getFileName());
			    	try{
				    	mageService.deleteAndSAve(image);
				    	}catch(Exception e){
				    		
				    	}finally{
				    	String defaultSystemDirFileUpload  = FacesContext.getCurrentInstance().getExternalContext().getRealPath("");	    			
		    			  defaultSystemDirFileUpload=defaultSystemDirFileUpload+File.separator+"uplaoded"+File.separator+"style";
		    			 MethodUtils.copyFile(defaultSystemDirFileUpload+File.separator, this.file.getFileName(), this.file.getInputstream());
		    			 preference.setPreferenceValue(File.separator+"uplaoded"+File.separator+"style"+File.separator+this.file.getFileName()); 
		    			 applicationScopeBean.configuration(preference.getPreferenceName(), preference.getPreferenceValue());
				    	}
	    	
    				}else
    					applicationScopeBean.setIs_text_logo(true);
    			
    		}
	    		
	    		if(preference.getPreferenceName().equalsIgnoreCase("PWD_COMPTE_ENVOI_MAIL")){  
	    		
	    			VigenereCipher vc = new VigenereCipher();
	    			String  input = vc.encrypt(preference.getPreferenceValue());	    			
	    	        preference.setPreferenceValue(input);
	    		
	    		}
	    		
	    		if(preference.getPreferenceName().equalsIgnoreCase("DUREE_COURRIER")){   			
	    			
    			//	JOptionPane.showMessageDialog(null, preference.getPreferenceValue());
	    		}
	    		
	    		preferenceService.save(preference);
		        init();//mise � jour de la liste			    	
		        applicationScopeBean.configuration(preference.getPreferenceName(), preference.getPreferenceValue());
			    
		        mouchardRessourceService.tracage("Modification de la preference :"+preference.getPreferenceName()+" par : "+preference.getPreferenceValue(), "modification",preference.getDateUseToSortData(), "Preferences");
				 
		        
		        FacesMessage message = Messages.getMessage("messages", "global.gestion.reussi", null);
		    	message.setSeverity(FacesMessage.SEVERITY_INFO);
		        context.addMessage(null, message);
	    	
	    	 }catch(Exception e){
	    		 e.printStackTrace();
	    		 mouchardRessourceService.tracage("Echec de modification de la preference :"+preference.getPreferenceName()+" par : "+preference.getPreferenceValue(), "modification",preference.getDateUseToSortData(), "Preferences");
					
	    		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
			    	message.setSeverity(FacesMessage.SEVERITY_WARN);
			        context.addMessage(null, message);
	    	 }finally{
	    		 preference = new Preferences();
	    		 this.file=null;
	    	 }
		       
		     
	    }
	 	
	 	 public void handleFileUpload(FileUploadEvent event) { 
	 		// JOptionPane.showMessageDialog(null, event.getFile().getFileName());
	        this.file=event.getFile();
	     }  
	 	
	 	
	 /**
	  * Mise à jour d'un preference
	  * @param preference
	  * @return preference
	  */
	 	
	 	 public void edition(ActionEvent actionEvent) {
	    	 FacesContext context = FacesContext.getCurrentInstance();
	    	
	    	 try{
	    		 Image image=new Image();
	    		 
	    		 	preferenceService.update(preference);
	    		 	
	    		 	image.setImage(file.getContents());
			    	image.setType(2);
			    	mageService.save(image);
	    		 	
			    	init();//mise à jour de la liste
			    	
			    	
			    		preference = new Preferences();
			    	FacesMessage message = Messages.getMessage("messages", "global.gestion.modifier", null);
			    	message.setSeverity(FacesMessage.SEVERITY_INFO);
			        context.addMessage(null, message);
				  
	    	 }catch(Exception e){
	     		 
	    		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
			    	message.setSeverity(FacesMessage.SEVERITY_WARN);
			        context.addMessage(null, message);
	     	 }
	    }
	 	 
	 	/**
	 	 * Supprime un preference
	 	 * @param Preference a supprimer
	 	 *@return void
	 	 */
	 	 
	 	public void delete(ActionEvent actionEvent) {
	    	
	    	 FacesContext context = FacesContext.getCurrentInstance();
	    	 RequestContext requestContext = RequestContext.getCurrentInstance();
	       try{
	    	   
	    	   //Cas selectiond
	    	  
	    	   if(selectedPreference.length>0)
		    	   for(int i=0;i<selectedPreference.length;i++){				    		
			    		//preferenceService.deleteVersusDesabled(selectedPreference[i], IConstance.FIELD_DELETE);
			    		preferenceService.deleteVersusDesabled(selectedPreference[i], IConstance.FIELD_DELETE);
			    		
			    	}
	    	 
	    	   if(preference!=null)		    	
	    		   //preferenceService.deleteVersusDesabled(preference, IConstance.FIELD_DELETE);
	    		   preferenceService.deleteVersusDesabled(preference, IConstance.FIELD_DELETE);
	    	   
	    	   selectedPreference=null;
	    	   preference=null;
			    	init();//mise à jour de la liste
			    	
			    	 FacesMessage message = Messages.getMessage("messages", "global.gestion.delete", null);
				    	message.setSeverity(FacesMessage.SEVERITY_INFO);
				        context.addMessage(null, message);				    	
			        requestContext.execute("deleteDialog.hide()");	    	  
	    	   
	       }catch(Exception e){
	  		 e.printStackTrace();
	  		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
		    	message.setSeverity(FacesMessage.SEVERITY_WARN);
		        context.addMessage(null, message);
	  	 }
	 
	    }


		public IPreferencesRessourceService getPreferencesRessourceService() {
			return preferencesRessourceService;
		}


		public void setPreferencesRessourceService(
				IPreferencesRessourceService preferencesRessourceService) {
			this.preferencesRessourceService = preferencesRessourceService;
		}


		public IPreferencesRessourceService getPreferenceService() {
			return preferenceService;
		}


		public void setPreferenceService(IPreferencesRessourceService preferenceService) {
			this.preferenceService = preferenceService;
		}


		public List<Preferences> getPreferenceList() {
			return preferenceList;
		}


		public void setPreferenceList(List<Preferences> preferenceList) {
			this.preferenceList = preferenceList;
		}


		public Preferences getPreference() {
			return preference;
		}


		public void setPreference(Preferences preference) {
			this.preference = preference;
		}


		public List<Preferences> getFilteredPreference() {
			return filteredPreference;
		}


		public void setFilteredPreference(List<Preferences> filteredPreference) {
			this.filteredPreference = filteredPreference;
		}


		public PreferenceDataModel getPreferenceListDataModel() {
			return preferenceListDataModel;
		}


		public void setPreferenceListDataModel(
				PreferenceDataModel preferenceListDataModel) {
			this.preferenceListDataModel = preferenceListDataModel;
		}


		public Preferences[] getSelectedPreference() {
			return selectedPreference;
		}


		public void setSelectedPreference(Preferences[] selectedPreference) {
			this.selectedPreference = selectedPreference;
		}


		public ApplicationBean getApplicationScopeBean() {
			return applicationScopeBean;
		}


		public void setApplicationScopeBean(ApplicationBean applicationScopeBean) {
			this.applicationScopeBean = applicationScopeBean;
		}


		public String[] getFormatDate() {
			return formatDate;
		}


		public void setFormatDate(String[] formatDate) {
			this.formatDate = formatDate;
		}


		public UploadedFile getFile() {
			return file;
		}


		public void setFile(UploadedFile file) {
			this.file = file;
		}


		public IImageService getMageService() {
			return mageService;
		}


		public void setMageService(IImageService mageService) {
			this.mageService = mageService;
		}


		public boolean isIs_text() {
			return is_text;
		}


		public void setIs_text(boolean is_text) {
			this.is_text = is_text;
		}


		public Date getDate() {
			return date;
		}


		public void setDate(Date date) {
			this.date = date;
		}


		public int getMois() {
			return mois;
		}


		public void setMois(int mois) {
			this.mois = mois;
		}


		public int getJour() {
			return jour;
		}


		public void setJour(int jour) {
			this.jour = jour;
		}


		public int getHeure() {
			return heure;
		}


		public void setHeure(int heure) {
			this.heure = heure;
		}


		public IMouchardRessourceService getMouchardRessourceService() {
			return mouchardRessourceService;
		}


		public void setMouchardRessourceService(
				IMouchardRessourceService mouchardRessourceService) {
			this.mouchardRessourceService = mouchardRessourceService;
		}
		
		
	 	
	 	
		
		 

}
