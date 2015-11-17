package cm.dita.controller.managed.bean.courrier;




import java.io.File;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;


import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ArrayList;
import java.util.List;























import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
//import javax.swing.JOptionPane;




import cm.dita.beans.ApplicationBean;
import cm.dita.constant.IConstance;
import cm.dita.controller.managed.bean.espacecourrier.EspaceCourrierDataModel;
import cm.dita.entities.Alarmes;
import cm.dita.entities.Correspondant;
import cm.dita.entities.Courriers;
import cm.dita.entities.Espace;
import cm.dita.entities.EspaceCourrier;
import cm.dita.entities.Piecesjointes;
import cm.dita.entities.Priorites;
import cm.dita.entities.Statuts;
import cm.dita.entities.Typescourriers;
import cm.dita.entities.user.User;
import cm.dita.service.domaine.inter.IAlarmesRessourceService;
import cm.dita.service.domaine.inter.ICorrespondantRessourceService;
import cm.dita.service.domaine.inter.ICourriersRessourceService;
import cm.dita.service.domaine.inter.IEspaceCourrierRessourceService;
import cm.dita.service.domaine.inter.IEspaceRessourceService;
import cm.dita.service.domaine.inter.IImageService;
import cm.dita.service.domaine.inter.IMouchardRessourceService;
import cm.dita.service.domaine.inter.IPiecesjointesRessourceService;
import cm.dita.service.domaine.inter.IPrioritesRessourceService;
import cm.dita.service.domaine.inter.IStatutsRessourceService;
import cm.dita.service.domaine.inter.ITypescourriersRessourceService;
import cm.dita.service.domaine.inter.user.IUserService;
import cm.dita.utils.Messages;
import cm.dita.utils.MethodUtils;



@ManagedBean(name="courrierControllerBean")
@SessionScoped
public class CourrierControllerBean  implements Serializable{
	

	private static final Logger LOG = Logger.getLogger(CourrierControllerBean.class);
	private static final long serialVersionUID = 1L;
	
	  		


		private Courriers courrier;
		private List<Courriers> listCourriers;
		private List<Courriers> listCourriersEspace;
		private CourrierDataModel listCourriersMeta;
		private Courriers[]  selectedCourriers;
		
		private List<EspaceCourrier> espaceCourriers;
		private List<Piecesjointes> piecesjointes;
		
		//pour les cles etrangeres
		private List<Statuts> listeStatuts = new ArrayList<Statuts>();
		private Statuts[] selectedStatuts;
		private Statuts statutsSelected;
		
		private List<Priorites> listePriorites = new ArrayList<Priorites>();
		private Priorites[] selectedPriorites;
		private Priorites prioritesSelected;
		private int nombreDePriorite;
		
		private List<Typescourriers> listeTypescourriers = new ArrayList<Typescourriers>();
		private Typescourriers[] selectedTypescourriers;
		private Typescourriers typescourriersSelected;
		
		private List<User> listeUser = new ArrayList<User>();
		private User[] selectedUsers;
		private User userSelected;
		
		private String idEspacecourant;
		private List<EspaceCourrier> listecourriers;
		private List<UploadedFile> files;
		//private List<UploadedFile> files1;
		
		private List<EspaceCourrier> listeEspaceCourrier = new ArrayList<EspaceCourrier>();
		
		private List<Piecesjointes> listePiecesjointes;

		
		private boolean skip;//pour le confirme au niveau des onglets
		
	    private List<Typescourriers> listTypescourriers = new ArrayList<Typescourriers>();//pour recuperer les utilisateurs correspondants dans la liste de selection
    
	    private EspaceCourrierDataModel listEspaceCourrierMeta;
	    
	    private List<Priorites> listPriorites = new ArrayList<Priorites>();//pour recuperer les priorites dans la liste de selection
	   
	    
	    private Boolean orderActivationExpediteurTbUser = true;//utilis� pour activer la liste de selection des exp�diteurs
	    
	    private Boolean orderActivationExpediteurTbCorresp = true;
	    
	    private Boolean orderActivationUserRecepteur = true;//utilis� pour activer la liste de selection des exp�diteurs
		
        private User userconnected;
        
		private int operation;//1 pour Ajout et 2 pour Modification        
        
        private int userrecieverid;
        private int correspondantrecieverid;
        
		private Correspondant correspondant;
		
		 private String numeroEnregistrement;
		 
	    private Integer idPieceJointeToDelete;	
	   
	    private String parametre_de_date_format_2 = IConstance.PARAMETER_DATE_FORMAT_2;
	    
	    private long taille_max_de_un_fichier_uploade = 10000000;
        
//******************************************POUR LES LISTE DE SELCTION*********************************************************

  		private List<User> listExpediteursUser = new ArrayList<User>();

  		private List<Correspondant> listExpediteursCorrespondant = new ArrayList<Correspondant>();

	    private List<Espace> listEspaces = new ArrayList<Espace>();//pour recuperer les espaces dans la liste de selection
  		
  		private List<User> listRecepteursUser = new ArrayList<User>();

  		private List<Correspondant> listRecepteursCorrespondant = new ArrayList<Correspondant>();
//******************************************FIN POUR LES LISTE DE SELCTION*********************************************************

//*************************************************POUR ACTIVER ET DESACTIVER L'AFFICHAGE DE CERTAINS COMPOSANTS DANS LA VUE***************************************************************************
        private Boolean orderAfficheCpExpediteurTbUtil = true;

        private Boolean orderAfficheCpExpediteurTbCorresp = true;

        private Boolean orderAfficheCpDestinationTbEspace = true;

        private Boolean orderAfficheCpRecepteurTbUtil = true;

        private Boolean orderAfficheCpRecepteurTbCorresp = true;
//*************************************************POUR ACTIVER ET DESACTIVER L'AFFICHAGE DE CERTAINS COMPOSANTS DANS LA VUE***************************************************************************
  
//***********************************ENVOI DE MAIL*******************************************************************
//***********************************FIN ENVOI DE MAIL*******************************************************************
        /*
         * POUR LA RECHERCHE DANS LES COURRIERS
         * 
         */
          private String search;
          private List<Courriers> listSearch;
        
        
		//Spring User Service is injected...
		@ManagedProperty(value="#{userService}")
		private IUserService userService;
		
		//Spring Statu Service is injected...
		@ManagedProperty(value="#{statutsRessourceService}")
		private IStatutsRessourceService statutsRessourceService;
		
		//Spring User Service is injected...
		@ManagedProperty(value="#{typescourriersRessourceService}")
		private ITypescourriersRessourceService typescourriersRessourceService;
		
		//Spring User Service is injected...
		@ManagedProperty(value="#{espaceCourrierRessourceService}")
		private IEspaceCourrierRessourceService espaceCourrierRessourceService;
		
		//Spring User Service is injected...
		@ManagedProperty(value="#{espaceRessourceService}")
		private IEspaceRessourceService espaceRessourceService;
		
		//Spring User Service is injected...
		@ManagedProperty(value="#{prioritesRessourceService}")
		private IPrioritesRessourceService prioritesRessourceService;
		
		//Spring User Service is injected...
		@ManagedProperty(value="#{courriersRessourceService}")
		private ICourriersRessourceService courriersRessourceService;		
		
		//Spring User Service is injected...
		@ManagedProperty(value="#{piecesjointesRessourceService}")
		private IPiecesjointesRessourceService piecesjointesRessourceService;
		
		//Spring Correspondant Service is injected...
		@ManagedProperty(value="#{correspondantRessourceService}")
		ICorrespondantRessourceService correspondantRessourceService;
		
		@ManagedProperty(value="#{alarmesRessourceService}")
		IAlarmesRessourceService alarmesRessourceService;
		
		@ManagedProperty(value="#{IImageService}")
		IImageService mageService;

		@ManagedProperty(value="#{applicationBean}")
	    private ApplicationBean applicationScopeBean;
		
		@ManagedProperty(value="#{mouchardRessourceService}")
		private IMouchardRessourceService mouchardRessourceService;
		

		@PostConstruct
		public void init(){
			
		try {
			
			this.courrier=new Courriers();
			courrier.setConfidentiel(false);
			correspondant = new Correspondant();
				//Cette portion permet de charger les courriers de l'utilisateur connect�
			
			
				UserDetails user_secutity = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				userconnected =userService.findByLogin(user_secutity.getUsername());
				
				//recuperer le nombre de priorit�
				nombreDePriorite = prioritesRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{}).size();

				
				if(userconnected!=null){
					idEspacecourant=userconnected.getEspace().getId().toString();
					
					}
		
			 
				// Lister les courriers selon les
			   listecourriers=espaceCourrierRessourceService.listCourrierParEspacefiltre(Integer.parseInt(idEspacecourant),userconnected.getId());
			   //listecourriers=espaceCourrierRessourceService.listCourrierParEspace(Integer.parseInt(idEspacecourant));
			   listCourriers=courriersRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
			   listCourriersEspace = courriersRessourceService.getCourrierByEspaceOfUserConnect(userconnected);
			   setListEspaceCourrierMeta(new EspaceCourrierDataModel(listecourriers));
			   mouchardRessourceService.tracage("Affiche la liste des courriers de son espace ", userconnected);
			   //setListCourriersMeta(new CourrierDataModel(listCourriers));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}		
			
		}

		/**
		 * le click sur le bouton ajout reinitialise l'objet courrier avant affichage du formulaire d'ajout
		 */
		public void ajoutEvent() {
			//JOptionPane.showMessageDialog(null, "hhsdjfs");
			
			operation = 1;//1 pour l'enregistrement de courrier
			
			files=new ArrayList<UploadedFile>();
			
			numeroEnregistrement = new String();
			
			listTypescourriers = typescourriersRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
			listPriorites = prioritesRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
			courrier=new Courriers();
			courrier.setConfidentiel(false);	
			SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2);
			java.util.Date date=new java.util.Date();
			courrier.setCourdate(date);
			piecesjointes = new ArrayList<Piecesjointes>();			
			espaceCourriers = new ArrayList<EspaceCourrier>();
			
//*************************************************POUR ACTIVER ET DESACTIVER L'AFFICHAGE DE CERTAINS COMPOSANTS DANS LA VUE***************************************************************************
	        orderAfficheCpExpediteurTbUtil = false;
	        orderAfficheCpExpediteurTbCorresp = false;
	        orderAfficheCpDestinationTbEspace = false;
	        orderAfficheCpRecepteurTbUtil = false;
	        orderAfficheCpRecepteurTbCorresp = false;
//*************************************************POUR ACTIVER ET DESACTIVER L'AFFICHAGE DE CERTAINS COMPOSANTS DANS LA VUE***************************************************************************
	  
//******************************************POUR LES LISTE DE SELCTION*********************************************************
	        listExpediteursUser = userService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
	        listExpediteursCorrespondant = correspondantRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
	        listEspaces = espaceRessourceService.listVersusEnabled(IConstance.FIELD_DELETE,  new String[]{});
	        listRecepteursUser = new ArrayList<User>();
	        listRecepteursCorrespondant = correspondantRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
//******************************************FIN POUR LES LISTE DE SELCTION*********************************************************

			
	    }
		
/**
 * ****************************************************************************************************************************************
 * Pour controller la fin du processus que offre le wizrd pour l'ajout de courrier jusqu'� la confirmation		
 * @param event
 * @return
 */
	    public String onFlowProcess(FlowEvent event) {
	        if(skip) {
	            skip = false;   //reset in case user goes back
	            return "confirm";
	        }
	        else {
	            return event.getNewStep();
	        }
	    }
	    
	    /**
	     * Pour la selection dans l'autocompletion
	     * @param event
	     */
	    public void onItemSelect(SelectEvent event) {
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Item Selected", event.getObject().toString()));
	    }
	    
	    

	    
		/**
	     * 
	     * @param query
	     * @return la liste des objets de courrier qui contiennent la chaine query ils sont utilis�s pour l'autocompletion
	     */
	    public List<String> completeObjetCourrier(String query) {
	        //List<Courriers> allCourrier = courriersRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
	        List<String> filteredObjetsCourrier = new ArrayList<String>();
	        if(listCourriers != null && listCourriers.size()>0) { 
		        for (Courriers courrier : listCourriers) {
		        	
		            if(courrier.getCourobjet().contains(query)/*.startsWith(query)*/) {
		            	filteredObjetsCourrier.add(courrier.getCourobjet());
		            }
		        }
	        }
	         
	        return filteredObjetsCourrier;
	    }
	    
		/**
	     * 
	     * @param query
	     * @return la liste des objets de courrier qui contiennent la chaine query ils sont utilis�s pour l'autocompletion
	     */
	  /*  public List<Courriers> completeRefidCourrier(String query) {

	        //List<Courriers> allCourrier = courriersRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
	        List<Courriers> filteredObjetsCourrier = new ArrayList<Courriers>();
	        
	        if(listCourriers != null && listCourriers.size()>0) { 
		    	//JOptionPane.showMessageDialog(null, "reference : " +allCourrier.size());
		        for (Courriers courrier : listCourriers) {
		        	
		            if(courrier.getRefid().toLowerCase().contains(query)) {
		            	filteredObjetsCourrier.add(courrier);
		            }
		            
		        }
	        }
	         
	        return filteredObjetsCourrier;
	    }*/
	    
		/**
	     * 
	     * @param query
	     * @return la liste des mots cl�s de recherche courrier qui contiennent la chaine query ils sont utilis�s pour l'autocompletion
	     */
	    public List<String> completeMotsClesRechCourrier(String query) {
	       // List<Courriers> allCourrier = courriersRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
	        List<String> filteredObjetsCourrier = new ArrayList<String>();
	        if(listCourriers != null && listCourriers.size()>0) {
		        for (Courriers courrier : listCourriers) {		        	
		            if(courrier.getCourmots().contains(query)/*.startsWith(query)*/) {
		            	filteredObjetsCourrier.add(courrier.getCourmots());
		            }
		        }
	        }
	         
	        return filteredObjetsCourrier;
	    }
	    


	    
	    /**
	     * Active l'affichage de la yone des expediteurs en chergeant sa liste de selection 
	     * @author Omar Nacis
	     */
	    public void afficheTypeExpediteur() {
	    	//JOptionPane.showMessageDialog(null, "je suis la");
	    	if(listRecepteursUser != null)
	    		listRecepteursUser.clear();
	    	
	    	if(courrier.getTypecourrier() != null){//le type de courrier n'est pas vide
	    		
/*		    	List<User> allUser = new ArrayList<User>();
		    	allUser.addAll(userService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{}));
		    	allUser.addAll(userService.listVersusDesabled(IConstance.FIELD_DELETE, new String[]{}));	    	
	    	*/
		    		    	
		        	//si le courrier est entrant, l'exp�diteur est un correspondant
		        	 if(courrier.getTypecourrier().getId() == 1 ){ 
		        		 /*for(User u : allUser ){
		        			 if(u.getTypespersonnel().getTypepersid() == 1)
		        				 listRecepteursUser.add(u);
		        		 }*/
		        		 if(courrier.getEspacedestf() != null) {
		        			 this.listRecepteursUser= userService.listUserParEspace(courrier.getEspacedestf().getId());
		        			 orderActivationUserRecepteur = false;//on active le composant de selection des exp�diteurs
		        		 }
		        		 
		        		orderAfficheCpExpediteurTbUtil = false;
		     	        orderAfficheCpExpediteurTbCorresp = true;
		     	        orderAfficheCpDestinationTbEspace = true;
		     	        orderAfficheCpRecepteurTbUtil = true;
		     	        orderAfficheCpRecepteurTbCorresp = false;
		        		 
		        	 }
		        	 else//si le courrier est interne, l'exp�diteur est un personnel interne ainsi que le recepteur
		        		 if(courrier.getTypecourrier().getId() == 2){ 
		        			 /*for(User u : allUser ){
			        			 if(u.getTypespersonnel().getTypepersid() == 2)
			        				 listRecepteursUser.add(u);
			        		 }*/
		        			 if(courrier.getEspacedestf() != null){
		        				 this.listRecepteursUser= userService.listUserParEspace(courrier.getEspacedestf().getId());
		        				 orderActivationUserRecepteur = false;//on active le composant de selection des exp�diteurs
		        			 }       			 
		        			    orderAfficheCpExpediteurTbUtil = true;
		        		        orderAfficheCpExpediteurTbCorresp = false;
		        		        orderAfficheCpDestinationTbEspace = true;
		        		        orderAfficheCpRecepteurTbUtil = true;
		        		        orderAfficheCpRecepteurTbCorresp = false;
		        			 
			        	 }
		        		 else
		        			 if( courrier.getTypecourrier().getId() == 3){
		        				 
					                orderActivationUserRecepteur = true;//on active le composant de selection des exp�diteurs
					        		
		        				    orderAfficheCpExpediteurTbUtil = true;
		        			        orderAfficheCpExpediteurTbCorresp = false;
		        			        orderAfficheCpDestinationTbEspace = false;
		        			        orderAfficheCpRecepteurTbUtil = false;
		        			        orderAfficheCpRecepteurTbCorresp = true;
		        			 }
	        	
	        }
	    	else {
	    		
	    		orderActivationUserRecepteur = true;
	    		
        		orderAfficheCpExpediteurTbUtil = false;
     	        orderAfficheCpExpediteurTbCorresp = false;
     	        orderAfficheCpDestinationTbEspace = false;
     	        orderAfficheCpRecepteurTbUtil = false;
     	        orderAfficheCpRecepteurTbCorresp = false;
	    	}
	    }


		/**
	     * 
	     * @param query
	     * @return la liste des utilisateurs dont le nom debute par la chaine query
	     * @author Omar Nacis
	     */
	    public void afficheUsersRecepteur() {
	    	if(listRecepteursUser != null)
	    		listRecepteursUser.clear();
	    	
	    	if(courrier.getEspacedestf() != null){//l'espace de destination finale n'est pas vide	    		
	          
	    		//on charge la liste des utilisateurs en fonction de l'espace
	        	  // this.listRecepteursUser = (List<User>)espaceRessourceService.load(courrier.getEspacedestf().getId()).getUserCollection();          
	        	   this.listRecepteursUser= userService.listUserParEspace(courrier.getEspacedestf().getId());
	        	   orderActivationUserRecepteur = false;//on active le composant de selection des recepteuurs finaux s'ils existent
	        	 
	           }	          
	           else 	
	        	   
	        	   orderActivationUserRecepteur = true;
	    	      
	    }
	    
	    /**
	     * 
	     */
	    public void ajoutEventCorrespondant(ActionEvent actionEvent) {	    	


	    	correspondant = new Correspondant();	    	
					
			 java.util.Date aujourdhui = new java.util.Date();
	    	
	    	SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2);    	
	    	correspondant.setDateCreation(sdf.format(aujourdhui));
	    	
	    	//JOptionPane.showMessageDialog(null, "la date :"+correspondant.getDateCreation());
          
	    	
	 
	    }	    
	    
	    /**
	     * @see fonction d'ajout d'un utilisateur
	     * @param correspondant
	     * 		prend en entree un utilisateur
	     * 
	     * @return vide
	     */
		public void createCorrespond() {
	    	 FacesContext context = FacesContext.getCurrentInstance();
	    	
	    	try{
			   
	    		//verifi si le correspondant existe
		    	if(correspondantRessourceService.correspondantExisteWithName(correspondant)){
		    		mouchardRessourceService.tracage("Tentative d'ajout du correspondant(nom complet existant) "+correspondant.getNom(), "ajout",correspondant.getDateUseToSortData(), "Correspondant");
					
		    		FacesMessage message = Messages.getMessage("messages", "correspondant.nom.validator", null);
			    	message.setSeverity(FacesMessage.SEVERITY_ERROR);
			        context.addMessage(null, message);
		    	}else{		
	    		        correspondant.setDelate(false);
	    		        java.util.Date aujourdhui = new java.util.Date();
	    		    	
	    		    	SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2);    	
	    		    	correspondant.setDateCreation(sdf.format(aujourdhui));
				    	this.correspondant=correspondantRessourceService.save(correspondant);	
				    	
				    	
				  
				    	//init();//mise � jour de la liste
				    	this.listRecepteursCorrespondant = correspondantRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
				    	this.listExpediteursCorrespondant=this.listRecepteursCorrespondant;
				    	FacesMessage message = Messages.getMessage("messages", "global.gestion.reussi", null);
				    	message.setSeverity(FacesMessage.SEVERITY_INFO);
				        context.addMessage(null, message);
				        mouchardRessourceService.tracage("Enregistrement du correspondant "+correspondant.getNom(), userconnected);
		    	}
	    	
	    	 }catch(Exception e){
	    		 e.printStackTrace();
	    		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
			    	message.setSeverity(FacesMessage.SEVERITY_WARN);
			        context.addMessage(null, message);
			        mouchardRessourceService.tracage("Echec d'enregistrement du correspondant "+correspondant.getNom(), userconnected);
	    	 }finally{
	    		 
	    	
	    		 correspondant = new Correspondant();
	 	        
	    	 }
	    		
	    }
	    
	    
	    private UploadedFile file;
	    
	    
	    /**
		 * @return the file
		 */
		public UploadedFile getFile() {
			return file;
		}

		/**
		 * @param file the file to set
		 */
		public void setFile(UploadedFile file) {
			this.file = file;
		}

		public void upload() {
	        if(file != null) {
	            FacesMessage message = new FacesMessage("Succesful", file.getFileName() + " is uploaded.");
	            FacesContext.getCurrentInstance().addMessage(null, message);
	        }
	    }
		
		/**
		 * 
		 * @param event
		 * @return nothing
		 */
	    public void handleFileUpload(FileUploadEvent fileUploadEvent) {
	        FacesMessage message = new FacesMessage("Succesful", fileUploadEvent.getFile().getFileName() + " is uploaded.");
	        FacesContext.getCurrentInstance().addMessage(null, message);	       
	        files.add(fileUploadEvent.getFile());
	        
	    }
	    
	    /**
	     * Copie un fichier du tempon in de nom fileName dans le r�pertoire de chemin cheminRepStore
	     * @param cheminRepStore
	     * @param fileName
	     * @param in
	     * @author Technipedia
	     */
		public void copyFile(String cheminRepStore, String fileName, InputStream in) {
			try {
				// write the inputStream to a FileOutputStream
				OutputStream out = new FileOutputStream(new File(cheminRepStore+fileName));
				int read = 0;
				byte[] bytes = new byte[1024];
				while ((read = in.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				in.close();
				out.flush();
				out.close();
				System.out.println("New file created!");
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		
		
		/**
		 * 
		 * @param fichier
		 * supprimer un fichier tempon sachant son pointeur
		 */
		public void dropTempFile(UploadedFile fichier){
			 try {
				    
				 	files.remove(fichier);
				 	
				  }
				  catch (Exception e) {
				   System.out.println(e.toString());
				  } 
		}
		
		
		/**
		 * 
		 * @param indexFichier
		 * supprimer un fichier tempon sachant son index
		 */
		public void dropTempFileByIndex(int indexFichier){
			 try {
				    
				 	files.remove(indexFichier);				 	

				  }
				  catch (Exception e) {
				   System.out.println(e.toString());
				  } 
		}
		
		
		public void envoiIdPieceJointe(Integer idPieceJointe){
			this.idPieceJointeToDelete = idPieceJointe;
		}
		
		
		public void dropExistFile(Integer piecejointeId){
			 try {
				 
				 	Piecesjointes pj = new Piecesjointes();
				 	//JOptionPane.showMessageDialog(null, "piece jointe  :  "+piecejointeId);
				 	pj = piecesjointesRessourceService.load(piecejointeId);
				    File file = null;
				    if((file = new File(pj.getPiecefichier())) != null){
				    	piecesjointesRessourceService.deleteVersusDesabled(pj, IConstance.FIELD_DELETE);
				    	mouchardRessourceService.tracage("Suppression de la pi�ce jointe : "+pj.getPiecetitre()+" du courrier de numero d'enregistrement : "+courrier.getRefid(), userconnected);
				    }
				    piecesjointes = piecesjointesRessourceService.ListPieceJointesNonSupprimeDunCourrier(courrier);//.ListPieceJointesDunCourrierNonSupprime(courrier);	
					
				    this.idPieceJointeToDelete = null;
				    //JOptionPane.showMessageDialog(null, "piece jointe  :  "+piecesjointes.size());
				  }
				  catch (Exception e) {
				   System.out.println(e.toString());
				   this.idPieceJointeToDelete = null;
				  } 
		}
		
		
		
		/**
		 * 
		 * @param listeFichiers
		 * @param nomSousRep
		 * Enregistre les pieces jointes dans la destination pr�cis�e(sous repertoire du r�pertoire d'upload des fichiers)
		 */
		public List<Piecesjointes> enregPiecesJointes(List<UploadedFile> listeFichiersPJ, String cheminCompletSousRepStockagePJ, String nomSousRepStorePJCourrierCourant, String dateCreate, Courriers courrier, User userCreate){
			 try {
				 List<Piecesjointes> pj = new ArrayList<Piecesjointes>();
				 //JOptionPane.showMessageDialog(null, "no,bre de piece jointe :  "+listeFichiersPJ.size());
			     for(UploadedFile uploadFile : listeFichiersPJ){
			    	 
			    	 
			    	 	/*Image image=new Image();
		    			image.setImage(uploadFile.getContents());
				    	image.setType(1);
				    	image.setIdCourrier(courrier.getId());
				    	image.setName(nomSousRepStorePJCourrierCourant+File.separator+uploadFile.getFileName());
				    	mageService.deleteAndSAve(image);*/
				    	//String defaulhtSystemDirFileUpload  = FacesContext.getCurrentInstance().getExternalContext().getRealPath("");	    			
		    			//  defaultSystemDirFileUpload=defaultSystemDirFileUpload+File.separator+"uplaoded"+File.separator+"jointes";
		    			 MethodUtils.copyFile(cheminCompletSousRepStockagePJ, uploadFile.getFileName(), uploadFile.getInputstream());
		    		
		    			 mouchardRessourceService.tracage("Ajout de la pi�ce jointe : "+uploadFile.getFileName() +" au courrier de num�ro d'enreistrement : " +courrier.getRefid() , userconnected);
			    	 
			    	// copyFile(cheminCompletSousRepStockagePJ, uploadFile.getFileName(), uploadFile.getInputstream());
			    	 //perssitance des pi�ces jointes dans la BD
		    			 //JOptionPane.showMessageDialog(null, "Ajout de la pi�ce jointe : "+uploadFile.getFileName() +" au courrier de num�ro d'enreistrement : " +courrier.getRefid());
					 
			    	 Piecesjointes pieceJointe = new Piecesjointes();
			    	 pieceJointe.setDelate(false);
			    	 pieceJointe.setPersid(userCreate.getId());
			    	 pieceJointe.setPiecedate(dateCreate);
			    	 pieceJointe.setPiecefichier(cheminCompletSousRepStockagePJ+uploadFile.getFileName());
			    	 pieceJointe.setPiecetitre(uploadFile.getFileName());
			    	 pieceJointe.setPiecedescription(""+uploadFile.getSize());
			    	 pieceJointe.setCourrier(courrier);
			    	 pj.add(pieceJointe);
			    	 //piecesjointesRessourceService.save(pieceJointe);				    	
			     }
			     
			     return pj;
			     
			  }
			  catch (Exception e) {
				  
			   System.out.println(e.toString());
			   return null;
			   
			  } 
			
		}
		

		
		/**
		 * L'ajout proprement dit d'un courrier dans l bd
		 */
		@Transactional
	    public void ajoutCourrier(FileUploadEvent fileUploadEvent) {   
	    	FacesContext context = FacesContext.getCurrentInstance();
	    	RequestContext requestContext = RequestContext.getCurrentInstance();
		 try{  
			 if(courrier != null){
			     UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			   			
				 Statuts statutEnCours = statutsRessourceService.load(2);//il s'agit du statut de libelle "en cours" � eviter de supprimer ou modifier 
				// User utilisateurCourant = userService.findByLogin(user.getUsername());//.findByLogin(user.getUsername());//Povisoire car il faudra prendre l'utilisateur courant 
				 Espace espaceCourant = userconnected.getEspace();// en fonction de l'utilisateur courant nous avons l'espace courant
				 
				//recup�ration de la date courante java et fabrication de la date Sql 		 
				 SimpleDateFormat formater = null;			
				 java.util.Date aujourdhui = new java.util.Date();			
				 formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");			
				 String dateAujourdhuiSql = formater.format(aujourdhui); 	
				  
				 courrier.setDelate(false);
				 courrier.setCourdatemodif(dateAujourdhuiSql);
				 courrier.setCourdatenreg(dateAujourdhuiSql);					 
				 courrier.setRefid("RAS");//doit �tre g�n�r� suivant une nomenclature
				 courrier.setUsercreate(userconnected);
				 courrier.setLastUserUpdate(userconnected);
				 courrier.setIdespacecourantducour(espaceCourant.getId());
				 courrier.setIdstatutcourantducour(statutEnCours.getId());
				 //courrier.setUsersender(utilisateurCourant);
				 
						 if( courrier.getCourrierparentid() != null && courrier.getCourrierparentid() > 0 ){//enegistrer l'id du courrier racine							 
							
								
								 Courriers  courrierParent =  courriersRessourceService.load(courrier.getCourrierparentid());//le parent
								 
								 courrier.setCourrierparentid(courrierParent.getId());
								 
								 if( courrierParent.getCourrierparentid() != null &&  courrierParent.getCourrierparentid() > 0){
									 
									 courrier.setCourrierracineid(courrierParent.getCourrierracineid());
									 
								 }
								 else{
									 
									 courrier.setCourrierracineid(courrierParent.getId());
									 
								 }						
								
						 }
						 else
								courrier.setCourrierparentid(courrier.getCourrierracineid());//en sachant que la racine est null le parent le sera aussi
							
						 
				 User userReceiver= new User();
				 if(this.userrecieverid>0){
					 userReceiver.setId(this.userrecieverid);
					 courrier.setUserreceiver(userReceiver);
				 }
				 
				 Correspondant correspondantReceiver= new Correspondant();
				 if(this.correspondantrecieverid>0){
					 correspondantReceiver.setId(this.correspondantrecieverid);
					 courrier.setCorrespondantreceiver(correspondantReceiver);
				 }		     
				 
				 
				 //Enregistrement de l'espace courrier	
			     EspaceCourrier espaceCourrier = new EspaceCourrier();
				 espaceCourrier.setDelate(false);
				 espaceCourrier.setDatearrive(dateAujourdhuiSql);
				 //espaceCourrier.setDatesortie(Date.valueOf(dateAujourdhuiSql));//pendant un transfert, date de sortie est null
				 espaceCourrier.setEspace(espaceCourant);
				 espaceCourrier.setEspacedestination(espaceCourant);
				 espaceCourrier.setEspacePrecedent(espaceCourant);
				 espaceCourrier.setRecu(true);
				 //espaceCourrier.setBordereau("0");
				 espaceCourrier.setStatut(statutEnCours);
				 espaceCourrier.setUsercreate(userconnected);
				 espaceCourrier.setObservation(courrier.getCourobservation());
				 espaceCourrier.setUserreceiver(userconnected);//lors de l'enregistrement, le courrier est transf�r� de chez moi vers chez moi
				 espaceCourrier.setCourrier(courrier);
				 espaceCourriers.add(espaceCourrier);
				
				 courrier.setEspaceCourrierCollection(espaceCourriers);			 
				 courriersRessourceService.save(courrier);
				 
				 Courriers courrierEnreg = courriersRessourceService.getCourrierByUsercreateIdAndDateUseToSortData(userconnected,  courrier.getDateUseToSortData());
				
				//recup�ration de la date courante java pourfabrication du numero d'enregistrement 		 
				 SimpleDateFormat formater1 = null;					
				 formater1 = new SimpleDateFormat("MMyyyy");			
				 String moisEtAnneedateAujourdhui = formater1.format(aujourdhui);
				 
			     //fabrique le num�ro d'enregistrement qui est contitu� de C+code_du_courrier+date et modifie le courrier
				 courrierEnreg.setRefid("C"+courrierEnreg.getId()+moisEtAnneedateAujourdhui);
				/* 
				 //Enregitrement des pieces jointes
				 HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();			 
			     //chemin complet du repertoire du projet courant d�ploy�
			     String defaultSystemDirFileUpload  = request.getSession().getServletContext().getRealPath("/");
			     System.out.println("chemin complet du repertoire du projet d�ploy� :"+ defaultSystemDirFileUpload);
			     
				 //Separateur des dossiers du syst�me
		         String separateurRepSyst =  File.separator;
		         
		         //Chemin fix� par moi du r�petoire des fichier upload�
		         String destinationDirFileUpload = defaultSystemDirFileUpload.concat("ressourcesUpload");		     
			     System.out.println("chemin complet du repertoire des fichiers joints :"+ destinationDirFileUpload);*/
			     
			     String defaultSystemDirFileUpload  = FacesContext.getCurrentInstance().getExternalContext().getRealPath("");	    			
   			     defaultSystemDirFileUpload=defaultSystemDirFileUpload+File.separator+"uplaoded"+File.separator+"jointes";
			     
			     //Chemin complet du sous repertoire d'enregistrement des pi�ces jointes de ce courrier � completer
			     String nomSousRepStorePJCourrierCourant = courrierEnreg.getRefid()+"PJ";// le nom  du sous-repertoire qui contient les pi�ces jointes du courrier, il est compos� du mot courrrier suvi du num�ro d'enregistrement du courrier et se termine par PJ
			     String cheminCompletSousRepStockagePJ = defaultSystemDirFileUpload+File.separator+nomSousRepStorePJCourrierCourant;//utilisation rovisoire doit fabriquer le chemin coomplet du reperrtoire de stockage et le remplacer
  			     //enregistrement des chemins des fichiers joints dans la table
			    
			     //cr�er le sous repertoire des pieces jointes � uploader pour le courrier
			     File pointeurSousRepPJ = new File(cheminCompletSousRepStockagePJ);
			     if (pointeurSousRepPJ.exists()) {
			            System.out.println("Le dossier existe d�j� : " + pointeurSousRepPJ.getAbsolutePath());
			        } else {
			            if (pointeurSousRepPJ.mkdir()) {
			                System.out.println("Ajout du dossier : " + pointeurSousRepPJ.getAbsolutePath());
			            } else {
			                System.out.println("Echec sur le dossier : " + pointeurSousRepPJ.getAbsolutePath());
			            }
			        }
			     cheminCompletSousRepStockagePJ += File.separator; 
			     //enregistre les fichiers joints dans la repertoire physique
			     //JOptionPane.showMessageDialog(null, cheminCompletSousRepStockagePJ);
				piecesjointes = enregPiecesJointes(files, cheminCompletSousRepStockagePJ,nomSousRepStorePJCourrierCourant, dateAujourdhuiSql, courrierEnreg, userconnected);
				//JOptionPane.showMessageDialog(null, "nbpj : "+piecesjointes.size());
				
				courrierEnreg.setPiecesjointesCollection(piecesjointes);
				
			     //vider la liste des fichiers joins salut le bess
			     this.files = null;
				 
				 //modification du courrier
				 courriersRessourceService.save(courrierEnreg);				
				 
				 numeroEnregistrement = courrierEnreg.getRefid();
				 
				 mouchardRessourceService.tracage("Enregistrement du courrier de num�ro : "+numeroEnregistrement, userconnected);
				 
				 //Envoi des mails � l'exp�diteur et au recepteur final s'il est p�cis�
			     //final MailSender mail2 = new MailSender("smtp.mail.yahoo.com", 465,"omarnacis@yahoo.fr", "mot_de_passe", true);
				
				 EnvoiMail(courrierEnreg);
			        

				 
				 //reinitialise la vue en rechargeant la nouvelle liste
		    	 this.init();
		    	 
				 this.courrier=new Courriers();
				 this.piecesjointes=null;
				 this.espaceCourriers=null;
				// this.numeroEnregistrement=null;
		    	 
				 //requestContext.execute("dialogShowRefidCourrier.show()");
		    	 context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"","Courrier ajout� avec pour numero d'enregistrement : "+courrierEnreg.getRefid()));
			 }
		 }catch(Exception e){
			 e.printStackTrace();
			 mouchardRessourceService.tracage("tentative et echec d'enregistrement d'un courrier ", userconnected);
			 context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"","Echec"));
			 
		 }finally{
			 this.courrier=new Courriers();
			 this.piecesjointes=null;
			 this.espaceCourriers=null;
			 this.numeroEnregistrement=null;
		 }
	 
	  }
		
		
	public void EnvoiMail(Courriers cour){
			
			/* String serveur = applicationScopeBean.getSERVEUR_SMTP();
			 String port = applicationScopeBean.getPORT_SERVEUR_SMTP();
			 String adresse_envoi = applicationScopeBean.getADRESSE_COMPTE_ENVOI_MAIL();
			 String pwd = applicationScopeBean.getPWD_COMPTE_ENVOI_MAIL();
			 String nom_entreprise = applicationScopeBean.getNOM_ENTREPRISE();*/
			 
		//	 if((serveur != null) && (port != null) && (adresse_envoi != null) && (pwd != null)){
			 
				     //final MailSender mail2 = new MailSender("smtp.mail.yahoo.com", 465,"omarnacis@yahoo.fr", "mot_de_passe", true);
				    // final MailSender mail2 = new MailSender(serveur, Integer.parseInt(port), adresse_envoi, pwd, true);
				    // final MailMessage msg = new MailMessage();
				  //   final MailMessage msg1 = new MailMessage();
				     
				    
				     String nomPrenomExpediteur = "";
				     String nomPrenomRecepteur = "";
				     
				     String adresseExpediteur = "";
				     String adresseRecepteur = "";
				     
				     Alarmes alarme=new Alarmes();
				     Alarmes alarme1=new Alarmes();
					 
					 if(cour.getTypecourrier().getId() == 1){//courrier entrant
						 
						 nomPrenomExpediteur = cour.getCorrespondantsender().getNom();
						 adresseExpediteur = cour.getCorrespondantsender().getMailAddress();
						 
						 if(cour.getUserreceiver() != null){
							 nomPrenomRecepteur = cour.getUserreceiver().getInfosPersonne().getPersnom().concat(" ")+cour.getUserreceiver().getInfosPersonne().getPersprenom();
							 adresseRecepteur = cour.getUserreceiver().getInfosPersonne().getPersemail();
						 }
					 }
					 else
						 if(cour.getTypecourrier().getId() == 2){//courrier interne
							 
							 nomPrenomExpediteur = cour.getUsersender().getInfosPersonne().getPersnom().concat(" ")+cour.getUsersender().getInfosPersonne().getPersprenom();
							 adresseExpediteur = cour.getUsersender().getInfosPersonne().getPersemail();
							 
							 if(cour.getUserreceiver() != null){
								 nomPrenomRecepteur = cour.getUserreceiver().getInfosPersonne().getPersnom().concat(" ")+cour.getUserreceiver().getInfosPersonne().getPersprenom();
								 adresseRecepteur = cour.getUserreceiver().getInfosPersonne().getPersemail();
							 }
						 
						 }
						 else
							 if(cour.getTypecourrier().getId() == 3){//courrier sortant
								 
								 nomPrenomExpediteur = cour.getUsersender().getInfosPersonne().getPersnom().concat(" ")+cour.getUsersender().getInfosPersonne().getPersprenom();
								 adresseExpediteur = cour.getUsersender().getInfosPersonne().getPersemail();
								 
								 if(cour.getCorrespondantreceiver() != null){
									 nomPrenomRecepteur =  cour.getCorrespondantreceiver().getNom();
									 adresseRecepteur = cour.getCorrespondantreceiver().getMailAddress();
								 }
							 }
				        
					 
	                    if(nomPrenomExpediteur.length()>0 && adresseExpediteur.length()>0){//l'expediteur est renseign�
	                    	
	                    	alarme1.setEmail(adresseExpediteur);
	                    	alarme1.setAlarmdate(new java.util.Date());
	                    	alarme1.setAlarmetat(false);
	                    	alarme1.setAlarmtype(1);
	                    	alarme1.setNbrEssai(0);
	                    	if(cour.getUserreceiver()!=null)
	                    	alarme1.setUser_recv(cour.getUserreceiver().getId());
	                    	//OMAR     alarme1.setUser_sender(cour.getUsersender().getId());
	                    	alarme1.setUser_id(cour.getUsercreate().getId());
	                    	alarme1.setObjet(applicationScopeBean.getOBJET_MAIL()+" "+cour.getRefid());
	                    	alarme1.setCorps(applicationScopeBean.getCORPS_MAIL()+" <br> R�f�rence du courrier :"+cour.getRefid()
	                    			+"<br><br> "+applicationScopeBean.getNOM_ENTREPRISE()+"<br><br>"+applicationScopeBean.getADRESSE()+"<br> "+applicationScopeBean.getTELEPHONE());
	                    	alarmesRessourceService.save(alarme1);
	    			        // Message avec texte html + images incluses + pi�ces jointes
	    			      /*  try {
								msg.setFrom(new InternetAddress(adresse_envoi, nom_entreprise));
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	    			       // msg.setTo(adresseExpediteur.trim());
	    			        try {
								msg.setTo(adresseExpediteur.trim());
							} catch (AddressException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	    			        msg.setSubject("Enregistrement du courrier");
	    			        msg.setContent("Votre courrier a été enregistré avec succes dans notre système. \n Votre numéro d'enregistrement est le suivant : "+cour.getRefid(), true);
	    			      //  msg.setAttachmentURL(new String[] { "c:\\fichier.csv", "c:\\fichier1.csv",
	    			            	//"c:\\fichier2.csv", "c:\\numero.csv"});
	    			        try {
								mail2.sendMessage(msg);
							} catch (MessagingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}*/
	    			        
	                    }
	                    
	                    if(nomPrenomRecepteur.length()>0 && adresseRecepteur.length()>0){//le recepteur est renseign�
	                    	
	     			       //message au recepteur final 
	                    	
	                    	alarme.setEmail(adresseRecepteur);
	                    	alarme.setAlarmdate(new java.util.Date());
	                    	alarme.setAlarmetat(false);
	                    	alarme.setAlarmtype(1);
	                    	alarme.setNbrEssai(0);
	                    	if(cour.getUserreceiver()!=null)
	                    	alarme.setUser_recv(cour.getUserreceiver().getId());
	                    	//OMAR     alarme.setUser_sender(cour.getUsersender().getId());
	                    	alarme.setUser_id(cour.getUsercreate().getId());
	                    	alarme.setObjet(applicationScopeBean.getOBJET_MAIL()+" "+cour.getRefid());
	                    	alarme.setCorps(applicationScopeBean.getCORPS_MAIL()+" \n R�f�rence du courrier :"+cour.getRefid()
	                    			+"\n\n "+applicationScopeBean.getNOM_ENTREPRISE()+"\n\n"+applicationScopeBean.getADRESSE()+"\n "+applicationScopeBean.getTELEPHONE());
	                    	
	                    	alarmesRessourceService.save(alarme);
	    			       /* try {
								msg1.setFrom(new InternetAddress(adresse_envoi, nom_entreprise));
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	    			        try {
								msg1.setTo(adresseRecepteur.trim());
							} catch (AddressException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}    			        
	    			        //msg1.setTo("omarnacis@yahoo.fr");
	    			        msg1.setSubject("Envoi Courrier");
	    			        msg1.setContent("Un courrier viens de vous être adressé avec comme numero d'enregistrement : "+cour.getRefid(), true);
	    			      //  msg.setAttachmentURL(new String[] { "c:\\fichier.csv", "c:\\fichier1.csv",
	    			            	//"c:\\fichier2.csv", "c:\\numero.csv"});
	    			        try {
								mail2.sendMessage(msg1);
							} catch (MessagingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}*/
	    			        
	                    }
		        
			// }
		}

/**
 * ***********************************************************************************************************	    
 * @param idCourrier
 */
		
		public StreamedContent prepDownload(int id) throws Exception {
			Piecesjointes piece =piecesjointesRessourceService.load(id);
			StreamedContent download=new DefaultStreamedContent();
			    File file = new File(piece.getPiecefichier());
			    InputStream input = new FileInputStream(file);
			    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			    download=new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName());
			    System.out.println("PREP = " + download.getName());
			return download;
			}
	    
	 //Pour le widget du boutton
	 public void editEvent(Integer idCourrier) {   
			operation = 2;//2 pour la modification du courrier
			//JOptionPane.showMessageDialog(null, "id   : "+idCourrier);
		// FacesContext context = FacesContext.getCurrentInstance();
		 try{
	    	
			    //courrier = courriersRessourceService.findById(idCourrier);//.load(idCourrier);
			    courrier = courriersRessourceService.load(idCourrier);
			 
				//JOptionPane.showMessageDialog(null, "reference du courrier : "+courrier.getRefid());
				files=new ArrayList<UploadedFile>();
				
				listTypescourriers = typescourriersRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
				listPriorites = prioritesRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});						
				piecesjointes = piecesjointesRessourceService.ListPieceJointesNonSupprimeDunCourrier(courrier);//.ListPieceJointesDunCourrierNonSupprime(courrier);	
				/*for(Piecesjointes pj : piecesjointes){
					files.add((UploadedFile)new File(pj.getPiecefichier()));
				}*/
				//JOptionPane.showMessageDialog(null, "Nombre de pces jointe : "+piecesjointes.size());
				espaceCourriers = new ArrayList<EspaceCourrier>();
				
	//*************************************************POUR ACTIVER ET DESACTIVER L'AFFICHAGE DE CERTAINS COMPOSANTS DANS LA VUE***************************************************************************
				if(listRecepteursUser != null)
					listRecepteursUser.clear();
				
		    	if(courrier.getTypecourrier() != null){//le type de courrier n'est pas vide   		
	
			    		    	
			        	//si le courrier est entrant, l'exp�diteur est un correspondant
			        	 if(courrier.getTypecourrier().getId() == 1 ){ 

			        		 if(courrier.getEspacedestf() != null) {
			        			// this.listRecepteursUser = (List<User>)espaceRessourceService.load(courrier.getEspacedestf().getId()).getUserCollection();          
			        			 this.listRecepteursUser= userService.listUserParEspace(courrier.getEspacedestf().getId());
			        			 orderActivationUserRecepteur = false;//on active le composant de selection des exp�diteurs
			        		 }
			        		 
			        		orderAfficheCpExpediteurTbUtil = false;
			     	        orderAfficheCpExpediteurTbCorresp = true;
			     	        orderAfficheCpDestinationTbEspace = true;
			     	        orderAfficheCpRecepteurTbUtil = true;
			     	        orderAfficheCpRecepteurTbCorresp = false;
			        		 
			        	 }
			        	 else//si le courrier est interne, l'exp�diteur est un personnel interne ainsi que le recepteur
			        		 if(courrier.getTypecourrier().getId() == 2){ 

			        			 if(courrier.getEspacedestf() != null){
			        				// this.listRecepteursUser = (List<User>)espaceRessourceService.load(courrier.getEspacedestf().getId()).getUserCollection();          
			        				 this.listRecepteursUser= userService.listUserParEspace(courrier.getEspacedestf().getId());
			        				 orderActivationUserRecepteur = false;//on active le composant de selection des exp�diteurs
			        			 }       			 
			        			    orderAfficheCpExpediteurTbUtil = true;
			        		        orderAfficheCpExpediteurTbCorresp = false;
			        		        orderAfficheCpDestinationTbEspace = true;
			        		        orderAfficheCpRecepteurTbUtil = true;
			        		        orderAfficheCpRecepteurTbCorresp = false;
			        			 
				        	 }
			        		 else
			        			 if( courrier.getTypecourrier().getId() == 3){
			        				 
						                orderActivationUserRecepteur = true;//on active le composant de selection des exp�diteurs
						        		
			        				    orderAfficheCpExpediteurTbUtil = true;
			        			        orderAfficheCpExpediteurTbCorresp = false;
			        			        orderAfficheCpDestinationTbEspace = false;
			        			        orderAfficheCpRecepteurTbUtil = false;
			        			        orderAfficheCpRecepteurTbCorresp = true;
			        			 }
		        	
		        }
		    	else {
		    		
		    		orderActivationUserRecepteur = true;
		    		
	        		orderAfficheCpExpediteurTbUtil = false;
	     	        orderAfficheCpExpediteurTbCorresp = false;
	     	        orderAfficheCpDestinationTbEspace = false;
	     	        orderAfficheCpRecepteurTbUtil = false;
	     	        orderAfficheCpRecepteurTbCorresp = false;
		    	}
	//*************************************************POUR ACTIVER ET DESACTIVER L'AFFICHAGE DE CERTAINS COMPOSANTS DANS LA VUE***************************************************************************
		  
	//******************************************POUR LES LISTE DE SELCTION*********************************************************
		        listExpediteursUser = userService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
		        listExpediteursCorrespondant = correspondantRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
		        listEspaces = espaceRessourceService.listVersusEnabled(IConstance.FIELD_DELETE,  new String[]{});
		        //listRecepteursUser = new ArrayList<User>();
		        listRecepteursCorrespondant = correspondantRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
	//******************************************FIN POUR LES LISTE DE SELCTION*********************************************************
	    	
	    	//this.init();
	    	 
	    	// context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"","Preference ajouter"));
		 }catch(Exception e){
			 e.printStackTrace();
			// context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"","Echec"));
			 
		 }
	 }
	 
	 @Transactional
	 public void editCourrier(FileUploadEvent fileUploadEvent) {    
	    	FacesContext context = FacesContext.getCurrentInstance();
	    	//RequestContext requestContext = RequestContext.getCurrentInstance();
		 try{  
			 if(courrier != null){
			     //UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			   			
				 //Statuts statutEnCours = statutsRessourceService.load(2);//il s'agit du statut de libelle "en cours" � eviter de supprimer ou modifier 
				// User utilisateurCourant = userService.findByLogin(user.getUsername());//.findByLogin(user.getUsername());//Povisoire car il faudra prendre l'utilisateur courant 
				 //Espace espaceCourant = utilisateurCourant.getEspace();// en fonction de l'utilisateur courant nous avons l'espace courant
				 
				//recup�ration de la date courante java et fabrication de la date Sql 		 
				 SimpleDateFormat formater = null;			
				 java.util.Date aujourdhui = new java.util.Date();			
				 formater = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2);			
				 String dateAujourdhuiSql = formater.format(aujourdhui); 	
				
				 courrier.setCourdatemodif(dateAujourdhuiSql);

				 //courrier.setIdespacecourantducour(espaceCourant.getId());
				 //courrier.setIdstatutcourantducour(statutEnCours.getId());
				 //courrier.setUsersender(utilisateurCourant);
				 
				 User userReceiver= new User();
				 if(this.userrecieverid>0){
					 userReceiver.setId(this.userrecieverid);
					 courrier.setUserreceiver(userReceiver);
				 }
				 
				 Correspondant correspondantReceiver= new Correspondant();
				 if(this.correspondantrecieverid>0){
					 correspondantReceiver.setId(this.correspondantrecieverid);
					 courrier.setCorrespondantreceiver(correspondantReceiver);
				 }	
				 
					 if( courrier.getCourrierparentid() != null && courrier.getCourrierparentid() > 0 ){//enegistrer l'id du courrier racine							 
							
							
							 Courriers  courrierParent =  courriersRessourceService.load(courrier.getCourrierparentid());//le parent
							 
							 courrier.setCourrierparentid(courrierParent.getId());
							 
							 if( courrierParent.getCourrierparentid() != null &&  courrierParent.getCourrierparentid() > 0){
								 
								 courrier.setCourrierracineid(courrierParent.getCourrierracineid());
								 
							 }
							 else{
								 
								 courrier.setCourrierracineid(courrierParent.getId());
								 
							 }						
							
					 }
					 else
							courrier.setCourrierparentid(courrier.getCourrierracineid());//en sachant que la racine est null le parent le sera aussi
					
				 
				 //Recup�ration de l'espace courrier	
				 List<EspaceCourrier> listEspaceCourrier = (List<EspaceCourrier>)courrier.getEspaceCourrierCollection();
				 
				 EspaceCourrier espaceCourrier =  listEspaceCourrier.get(0);
				 int index = 0;
				 int indexPremierEspaceCourrier = 0;
				 for(EspaceCourrier ep : listEspaceCourrier){
					 if( espaceCourrier.getDateUseToSortData() > ep.getDateUseToSortData() ){
						 espaceCourrier = ep;
						 indexPremierEspaceCourrier = index;
					 }
					 index +=1;
				 }
				 
				 listEspaceCourrier.get(indexPremierEspaceCourrier).setDatearrive(dateAujourdhuiSql);
				 listEspaceCourrier.get(indexPremierEspaceCourrier).setObservation(courrier.getCourobservation());
				 courrier.setEspaceCourrierCollection(listEspaceCourrier);
				 
				 /*espaceCourrier.setDatearrive(dateAujourdhuiSql);
				 espaceCourrier.setObservation(courrier.getCourobservation());
                 espaceCourrier.setCourrier(courrier);
                 espaceCourrierRessourceService.save(espaceCourrier);*/
                 
				 //courriersRessourceService.save(courrier);
                 

				/* //Separateur des dossiers du syst�me
		         String separateurRepSyst =  File.separator;

			     //Chemin complet du sous repertoire d'enregistrement des pi�ces jointes de ce courrier � completer
			     String nomSousRepStorePJCourrierCourant = courrier.getRefid()+"PJ";// le nom  du sous-repertoire qui contient les pi�ces jointes du courrier, il est compos� du mot courrrier suvi du num�ro d'enregistrement du courrier et se termine par PJ
			     String cheminCompletSousRepStockagePJ = "C:\\rep_omar_test_upload"+separateurRepSyst+nomSousRepStorePJCourrierCourant;//utilisation rovisoire doit fabriquer le chemin coomplet du reperrtoire de stockage et le remplacer
			     //enregistrement des chemins des fichiers joints dans la table
			     */
			     
			     String defaultSystemDirFileUpload  = FacesContext.getCurrentInstance().getExternalContext().getRealPath("");	    			
   			     defaultSystemDirFileUpload=defaultSystemDirFileUpload+File.separator+"uplaoded"+File.separator+"jointes";
			     
			     //Chemin complet du sous repertoire d'enregistrement des pi�ces jointes de ce courrier � completer
			     String nomSousRepStorePJCourrierCourant = courrier.getRefid()+"PJ";// le nom  du sous-repertoire qui contient les pi�ces jointes du courrier, il est compos� du mot courrrier suvi du num�ro d'enregistrement du courrier et se termine par PJ
			     String cheminCompletSousRepStockagePJ = defaultSystemDirFileUpload+File.separator+nomSousRepStorePJCourrierCourant;//utilisation rovisoire doit fabriquer le chemin coomplet du reperrtoire de stockage et le remplacer
			     //enregistrement des chemins des fichiers joints dans la table
			     
			     //cr�er le sous repertoire des pieces jointes � uploader pour le courrier
			     File pointeurSousRepPJ = new File(cheminCompletSousRepStockagePJ);
			     if (pointeurSousRepPJ.exists()) {
			            System.out.println("Le dossier existe d�j� : " + pointeurSousRepPJ.getAbsolutePath());
			        } else {
			            if (pointeurSousRepPJ.mkdir()) {
			                System.out.println("Ajout du dossier : " + pointeurSousRepPJ.getAbsolutePath());
			            } else {
			                System.out.println("Echec sur le dossier : " + pointeurSousRepPJ.getAbsolutePath());
			            }
			        }
			     cheminCompletSousRepStockagePJ += File.separator ; 
			     //enregistre les fichiers joints dans la repertoire physique
				
				piecesjointes = enregPiecesJointes(files, cheminCompletSousRepStockagePJ,nomSousRepStorePJCourrierCourant, dateAujourdhuiSql, courrier, userconnected);
				/*if(piecesjointes != null){
					for(Piecesjointes pj : piecesjointes)
						piecesjointesRessourceService.save(pj);
					courrier.getPiecesjointesCollection().addAll(piecesjointes);
				}*/
				courrier.getPiecesjointesCollection().addAll(piecesjointes);
				//courrierEnreg.setPiecesjointesCollection(piecesjointes);
			     //vider la liste des fichiers joins salut le bess
			     this.files = null;
				 
				 //modification du courrier
				 courriersRessourceService.update(courrier);
				 mouchardRessourceService.tracage("Modification du courrier de num�ro d'enregistrement : "+courrier.getRefid(), userconnected);

				 //reinitialise la vue en rechargeant la nouvelle liste
		    	 this.init();
		    	 
				 this.courrier=new Courriers();
				 this.piecesjointes=null;
				 this.espaceCourriers=null;
				 
		    	 //requestContext.execute("dialogShowRefidCourrier.show()");

		    	 context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"","Courrier modifi� avec succ�s!�"));

		    	
			 }
		    	
			 }catch(Exception e){
				 e.printStackTrace();
				 mouchardRessourceService.tracage("tentaative et echec de modification du courrier de num�ro d'enregistrement : "+courrier.getRefid(), userconnected);
				 context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"","Echec"));
				 
			 }finally{
				 this.courrier=new Courriers();
				 this.piecesjointes=null;
				 this.espaceCourriers=null;
				 this.numeroEnregistrement=null;
			 }
	
	 
	}
	 
	    public void delete(ActionEvent actionEvent) {
	    	
	    	 FacesContext context = FacesContext.getCurrentInstance();
	    	 RequestContext requestContext = RequestContext.getCurrentInstance();
	       /*try{
	    	  
	    	   if(selectedUsers.length>0)
		    	   for(int i=0;i<selectedUsers.length;i++){	
		    		  
		    		   userService.deleteVersusDesabled(selectedUsers[i], IConstance.FIELD_DELETE);
			    		
			    	}else    	 
			    		userService.deleteVersusDesabled(user, IConstance.FIELD_DELETE);
	    	
	    	   
	    	   selectedUsers=null;
	    	   user=null;
	    	   espace_id=0;
	   			type_id=0;
			    	
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
	  	 }*/
	 
	    }
	 
	    /*
	     * Redirection pour actualiser
	     * 
	     */
	    public String showCourrier(){		
			
	    	listecourriers=espaceCourrierRessourceService.listCourrierParEspacefiltre(Integer.parseInt(idEspacecourant),userconnected.getId());
			   //listecourriers=espaceCourrierRessourceService.listCourrierParEspace(Integer.parseInt(idEspacecourant));
			   listCourriers=courriersRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
			   setListEspaceCourrierMeta(new EspaceCourrierDataModel(listecourriers));
			return "ok";
		}
	    
	    
	    
	    public void addAndViewPJEvent(Integer idCourrier){
	    	
	    	piecesjointes = null;
	    	courrier = courriersRessourceService.load(idCourrier);	
	    	
			files=new ArrayList<UploadedFile>();				
			piecesjointes = piecesjointesRessourceService.ListPieceJointesNonSupprimeDunCourrier(courrier);
			
			//JOptionPane.showMessageDialog(null, "Nombre de pi�ces jointe : "+piecesjointes.size()+" et courrier : "+courrier.getRefid());
			
	    }
	    
	    public void addPJ(FileUploadEvent fileUploadEvent){   
	    	FacesContext context = FacesContext.getCurrentInstance();
	    	int nbpjAdd = 0;
	    	try {
	    		
	    		if(files != null){
	    			
			    	//utilisateur courant
				    // UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			   		// User utilisateurCourant = userService.findByLogin(user.getUsername());//.findByLogin(user.getUsername());//Povisoire car il faudra prendre l'utilisateur courant 
					 
			    	
					//recup�ration de la date courante java et fabrication de la date Sql 		 
					 SimpleDateFormat formater = null;
					 java.util.Date aujourdhui = new java.util.Date();			
					 formater = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2);	
					 String dateAujourdhuiSql = formater.format(aujourdhui);
									
					//piecesjointes = piecesjointesRessourceService.ListPieceJointesNonSupprimeDunCourrier(courrier);
					
				     String defaultSystemDirFileUpload  = FacesContext.getCurrentInstance().getExternalContext().getRealPath("");	    			
					 defaultSystemDirFileUpload=defaultSystemDirFileUpload+File.separator+"uplaoded"+File.separator+"jointes";
				     
				     //Chemin complet du sous repertoire d'enregistrement des pi�ces jointes de ce courrier � completer
				     String nomSousRepStorePJCourrierCourant = courrier.getRefid()+"PJ";// le nom  du sous-repertoire qui contient les pi�ces jointes du courrier, il est compos� du mot courrrier suvi du num�ro d'enregistrement du courrier et se termine par PJ
				     String cheminCompletSousRepStockagePJ = defaultSystemDirFileUpload+File.separator+nomSousRepStorePJCourrierCourant;//utilisation rovisoire doit fabriquer le chemin coomplet du reperrtoire de stockage et le remplacer
				     //enregistrement des chemins des fichiers joints dans la table
				     
				     //cr�er le sous repertoire des pieces jointes � uploader pour le courrier
				     File pointeurSousRepPJ = new File(cheminCompletSousRepStockagePJ);
				     if (pointeurSousRepPJ.exists()) {
				            System.out.println("Le dossier existe d�j� : " + pointeurSousRepPJ.getAbsolutePath());
				        } else {
				            if (pointeurSousRepPJ.mkdir()) {
				                System.out.println("Ajout du dossier : " + pointeurSousRepPJ.getAbsolutePath());
				            } else {
				                System.out.println("Echec sur le dossier : " + pointeurSousRepPJ.getAbsolutePath());
				            }
				        }
				     cheminCompletSousRepStockagePJ += File.separator ; 
				     //enregistre les fichiers joints dans la repertoire physique
					
					piecesjointes = enregPiecesJointes(files, cheminCompletSousRepStockagePJ,nomSousRepStorePJCourrierCourant, dateAujourdhuiSql, courrier, userconnected);
					if(piecesjointes != null){
						for(Piecesjointes pj : piecesjointes)
							piecesjointesRessourceService.save(pj);
						//courrier.getPiecesjointesCollection().addAll(piecesjointes);
					}
					
					nbpjAdd = piecesjointes.size();
					
				     //vider la liste des fichiers joins salut le bess
				     this.files = null;
					 
					 //modification du courrier
					 //courriersRessourceService.save(courrier);

					 //reinitialise la vue en rechargeant la nouvelle liste
			    	 this.init();
			    	 
					 this.courrier=new Courriers();
					 this.piecesjointes=null;
					 
					 context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"",nbpjAdd+"pi�ces jointes ajout�es avec succ�s��"));
				
					 nbpjAdd = 0;
	    			
	    		}
	    		else
	    			 context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"","aucune pi�ces jointes charg�e��"));
	
			} catch (Exception e) {
				 e.printStackTrace();
				 this.courrier=new Courriers();
				 this.piecesjointes=null;
				 mouchardRessourceService.tracage("tentative et echec d'ajout de pi�ce jointe au courrier de num�ro d'enregistrement : "+courrier.getRefid(), userconnected);
				 context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"","Echec d'ajout"));
				 
				 
			 }finally{
				 this.courrier=new Courriers();
				 this.piecesjointes=null;
				 nbpjAdd = 0;
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
	 * @return the courrier
	 */
	public Courriers getCourrier() {
		return courrier;
	}

	/**
	 * @param courrier the courrier to set
	 */
	public void setCourrier(Courriers courrier) {
		this.courrier = courrier;
	}

	/**
	 * @return the listCourriers
	 */
	public List<Courriers> getListCourriers() {
		return listCourriers;
	}

	/**
	 * @param listCourriers the listCourriers to set
	 */
	public void setListCourriers(List<Courriers> listCourriers) {
		this.listCourriers = listCourriers;
	}

	/**
	 * @return the listCourriersMeta
	 */
	public CourrierDataModel getListCourriersMeta() {
		return listCourriersMeta;
	}

	/**
	 * @param listCourriersMeta the listCourriersMeta to set
	 */
	public void setListCourriersMeta(CourrierDataModel listCourriersMeta) {
		this.listCourriersMeta = listCourriersMeta;
	}

	/**
	 * @return the selectedCourriers
	 */
	public Courriers[] getSelectedCourriers() {
		return selectedCourriers;
	}

	/**
	 * @param selectedCourriers the selectedCourriers to set
	 */
	public void setSelectedCourriers(Courriers[] selectedCourriers) {
		this.selectedCourriers = selectedCourriers;
	}

	/**
	 * @return the listeStatuts
	 */
	public List<Statuts> getListeStatuts() {
		return listeStatuts;
	}

	/**
	 * @param listeStatuts the listeStatuts to set
	 */
	public void setListeStatuts(List<Statuts> listeStatuts) {
		this.listeStatuts = listeStatuts;
	}

	/**
	 * @return the selectedStatuts
	 */
	public Statuts[] getSelectedStatuts() {
		return selectedStatuts;
	}

	/**
	 * @param selectedStatuts the selectedStatuts to set
	 */
	public void setSelectedStatuts(Statuts[] selectedStatuts) {
		this.selectedStatuts = selectedStatuts;
	}

	/**
	 * @return the statutsSelected
	 */
	public Statuts getStatutsSelected() {
		return statutsSelected;
	}

	/**
	 * @param statutsSelected the statutsSelected to set
	 */
	public void setStatutsSelected(Statuts statutsSelected) {
		this.statutsSelected = statutsSelected;
	}

	/**
	 * @return the listePriorites
	 */
	public List<Priorites> getListePriorites() {
		return listePriorites;
	}

	/**
	 * @param listePriorites the listePriorites to set
	 */
	public void setListePriorites(List<Priorites> listePriorites) {
		this.listePriorites = listePriorites;
	}

	/**
	 * @return the selectedPriorites
	 */
	public Priorites[] getSelectedPriorites() {
		return selectedPriorites;
	}

	/**
	 * @param selectedPriorites the selectedPriorites to set
	 */
	public void setSelectedPriorites(Priorites[] selectedPriorites) {
		this.selectedPriorites = selectedPriorites;
	}

	/**
	 * @return the prioritesSelected
	 */
	public Priorites getPrioritesSelected() {
		return prioritesSelected;
	}

	/**
	 * @param prioritesSelected the prioritesSelected to set
	 */
	public void setPrioritesSelected(Priorites prioritesSelected) {
		this.prioritesSelected = prioritesSelected;
	}

	/**
	 * @return the listeTypescourriers
	 */
	public List<Typescourriers> getListeTypescourriers() {
		return listeTypescourriers;
	}

	/**
	 * @param listeTypescourriers the listeTypescourriers to set
	 */
	public void setListeTypescourriers(List<Typescourriers> listeTypescourriers) {
		this.listeTypescourriers = listeTypescourriers;
	}

	/**
	 * @return the selectedTypescourriers
	 */
	public Typescourriers[] getSelectedTypescourriers() {
		return selectedTypescourriers;
	}

	/**
	 * @param selectedTypescourriers the selectedTypescourriers to set
	 */
	public void setSelectedTypescourriers(Typescourriers[] selectedTypescourriers) {
		this.selectedTypescourriers = selectedTypescourriers;
	}

	/**
	 * @return the typescourriersSelected
	 */
	public Typescourriers getTypescourriersSelected() {
		return typescourriersSelected;
	}

	/**
	 * @param typescourriersSelected the typescourriersSelected to set
	 */
	public void setTypescourriersSelected(Typescourriers typescourriersSelected) {
		this.typescourriersSelected = typescourriersSelected;
	}

	/**
	 * @return the listeUser
	 */
	public List<User> getListeUser() {
		return listeUser;
	}

	/**
	 * @param listeUser the listeUser to set
	 */
	public void setListeUser(List<User> listeUser) {
		this.listeUser = listeUser;
	}

	/**
	 * @return the selectedUsers
	 */
	public User[] getSelectedUsers() {
		return selectedUsers;
	}

	/**
	 * @param selectedUsers the selectedUsers to set
	 */
	public void setSelectedUsers(User[] selectedUsers) {
		this.selectedUsers = selectedUsers;
	}

	/**
	 * @return the userSelected
	 */
	public User getUserSelected() {
		return userSelected;
	}

	/**
	 * @param userSelected the userSelected to set
	 */
	public void setUserSelected(User userSelected) {
		this.userSelected = userSelected;
	}

	/**
	 * @return the listeEspaceCourrier
	 */
	public List<EspaceCourrier> getListeEspaceCourrier() {
		return listeEspaceCourrier;
	}

	/**
	 * @param listeEspaceCourrier the listeEspaceCourrier to set
	 */
	public void setListeEspaceCourrier(List<EspaceCourrier> listeEspaceCourrier) {
		this.listeEspaceCourrier = listeEspaceCourrier;
	}

	

	/**
	 * @return the statutsRessourceService
	 */
	public IStatutsRessourceService getStatutsRessourceService() {
		return statutsRessourceService;
	}

	/**
	 * @param statutsRessourceService the statutsRessourceService to set
	 */
	public void setStatutsRessourceService(
			IStatutsRessourceService statutsRessourceService) {
		this.statutsRessourceService = statutsRessourceService;
	}

	/**
	 * @return the typescourriersRessourceService
	 */
	public ITypescourriersRessourceService getTypescourriersRessourceService() {
		return typescourriersRessourceService;
	}

	/**
	 * @param typescourriersRessourceService the typescourriersRessourceService to set
	 */
	public void setTypescourriersRessourceService(
			ITypescourriersRessourceService typescourriersRessourceService) {
		this.typescourriersRessourceService = typescourriersRessourceService;
	}

	/**
	 * @return the espaceCourrierRessourceService
	 */
	public IEspaceCourrierRessourceService getEspaceCourrierRessourceService() {
		return espaceCourrierRessourceService;
	}

	/**
	 * @param espaceCourrierRessourceService the espaceCourrierRessourceService to set
	 */
	public void setEspaceCourrierRessourceService(
			IEspaceCourrierRessourceService espaceCourrierRessourceService) {
		this.espaceCourrierRessourceService = espaceCourrierRessourceService;
	}

	/**
	 * @return the prioritesRessourceService
	 */
	public IPrioritesRessourceService getPrioritesRessourceService() {
		return prioritesRessourceService;
	}

	/**
	 * @param prioritesRessourceService the prioritesRessourceService to set
	 */
	public void setPrioritesRessourceService(
			IPrioritesRessourceService prioritesRessourceService) {
		this.prioritesRessourceService = prioritesRessourceService;
	}

	/**
	 * @return the courriersRessourceService
	 */
	public ICourriersRessourceService getCourriersRessourceService() {
		return courriersRessourceService;
	}

	/**
	 * @param courriersRessourceService the courriersRessourceService to set
	 */
	public void setCourriersRessourceService(
			ICourriersRessourceService courriersRessourceService) {
		this.courriersRessourceService = courriersRessourceService;
	}

	

	/**
	 * @return the files
	 */
	public List<UploadedFile> getFiles() {
		return files;
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

	/**
	 * @param files the files to set
	 */
	public void setFiles(List<UploadedFile> files) {
		this.files = files;
	}

	/**
	 * @return the skip
	 */
	public boolean isSkip() {
		return skip;
	}

	/**
	 * @param skip the skip to set
	 */
	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	/**
	 * @return the espaceRessourceService
	 */
	public IEspaceRessourceService getEspaceRessourceService() {
		return espaceRessourceService;
	}

	/**
	 * @param espaceRessourceService the espaceRessourceService to set
	 */
	public void setEspaceRessourceService(
			IEspaceRessourceService espaceRessourceService) {
		this.espaceRessourceService = espaceRessourceService;
	}

	/**
	 * @return the piecesjointesRessourceService
	 */
	public IPiecesjointesRessourceService getPiecesjointesRessourceService() {
		return piecesjointesRessourceService;
	}

	/**
	 * @param piecesjointesRessourceService the piecesjointesRessourceService to set
	 */
	public void setPiecesjointesRessourceService(
			IPiecesjointesRessourceService piecesjointesRessourceService) {
		this.piecesjointesRessourceService = piecesjointesRessourceService;
	}

	/**
	 * @return the listePiecesjointes
	 */
	public List<Piecesjointes> getListePiecesjointes() {
		return listePiecesjointes;
	}

	/**
	 * @param listePiecesjointes the listePiecesjointes to set
	 */
	public void setListePiecesjointes(List<Piecesjointes> listePiecesjointes) {
		this.listePiecesjointes = listePiecesjointes;
	}

	/**
	 * @return the listTypescourriers
	 */
	public List<Typescourriers> getListTypescourriers() {
		return listTypescourriers;
	}

	/**
	 * @param listTypescourriers the listTypescourriers to set
	 */
	public void setListTypescourriers(List<Typescourriers> listTypescourriers) {
		this.listTypescourriers = listTypescourriers;
	}



	/**
	 * @return the listPriorites
	 */
	public List<Priorites> getListPriorites() {
		return listPriorites;
	}

	/**
	 * @param listPriorites the listPriorites to set
	 */
	public void setListPriorites(List<Priorites> listPriorites) {
		this.listPriorites = listPriorites;
	}

	/**
	 * @return the espaceCourriers
	 */
	public List<EspaceCourrier> getEspaceCourriers() {
		return espaceCourriers;
	}

	/**
	 * @param espaceCourriers the espaceCourriers to set
	 */
	public void setEspaceCourriers(List<EspaceCourrier> espaceCourriers) {
		this.espaceCourriers = espaceCourriers;
	}

	/**
	 * @return the piecesjointes
	 */
	public List<Piecesjointes> getPiecesjointes() {
		return piecesjointes;
	}

	/**
	 * @param piecesjointes the piecesjointes to set
	 */
	public void setPiecesjointes(List<Piecesjointes> piecesjointes) {
		this.piecesjointes = piecesjointes;
	}

	/**
	 * @return the nombreDePriorite
	 */
	public int getNombreDePriorite() {
		return nombreDePriorite;
	}

	/**
	 * @param nombreDePriorite the nombreDePriorite to set
	 */
	public void setNombreDePriorite(int nombreDePriorite) {
		this.nombreDePriorite = nombreDePriorite;
	}

	/**
	 * @return the idEspacecourant
	 */
	public String getIdEspacecourant() {
		return idEspacecourant;
	}

	/**
	 * @param idEspacecourant the idEspacecourant to set
	 */
	public void setIdEspacecourant(String idEspacecourant) {
		this.idEspacecourant = idEspacecourant;
	}

	/**
	 * @return the listecourriers
	 */
	public List<EspaceCourrier> getListecourriers() {
		return listecourriers;
	}

	/**
	 * @param listecourriers the listecourriers to set
	 */
	public void setListecourriers(List<EspaceCourrier> listecourriers) {
		this.listecourriers = listecourriers;
	}

	/**
	 * @return the listEspaceCourrierMeta
	 */
	public EspaceCourrierDataModel getListEspaceCourrierMeta() {
		return listEspaceCourrierMeta;
	}

	/**
	 * @param listEspaceCourrierMeta the listEspaceCourrierMeta to set
	 */
	public void setListEspaceCourrierMeta(
			EspaceCourrierDataModel listEspaceCourrierMeta) {
		this.listEspaceCourrierMeta = listEspaceCourrierMeta;
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
	 * @return the listEspaces
	 */
	public List<Espace> getListEspaces() {
		return listEspaces;
	}

	/**
	 * @param listEspaces the listEspaces to set
	 */
	public void setListEspaces(List<Espace> listEspaces) {
		this.listEspaces = listEspaces;
	}

	/**
	 * @return the orderActivationUserRecepteur
	 */
	public Boolean getOrderActivationUserRecepteur() {
		return orderActivationUserRecepteur;
	}

	/**
	 * @param orderActivationUserRecepteur the orderActivationUserRecepteur to set
	 */
	public void setOrderActivationUserRecepteur(Boolean orderActivationUserRecepteur) {
		this.orderActivationUserRecepteur = orderActivationUserRecepteur;
	}

	/**
	 * @return the orderActivationExpediteurTbUser
	 */
	public Boolean getOrderActivationExpediteurTbUser() {
		return orderActivationExpediteurTbUser;
	}

	/**
	 * @param orderActivationExpediteurTbUser the orderActivationExpediteurTbUser to set
	 */
	public void setOrderActivationExpediteurTbUser(
			Boolean orderActivationExpediteurTbUser) {
		this.orderActivationExpediteurTbUser = orderActivationExpediteurTbUser;
	}

	/**
	 * @return the orderActivationExpediteurTbCorresp
	 */
	public Boolean getOrderActivationExpediteurTbCorresp() {
		return orderActivationExpediteurTbCorresp;
	}

	/**
	 * @param orderActivationExpediteurTbCorresp the orderActivationExpediteurTbCorresp to set
	 */
	public void setOrderActivationExpediteurTbCorresp(
			Boolean orderActivationExpediteurTbCorresp) {
		this.orderActivationExpediteurTbCorresp = orderActivationExpediteurTbCorresp;
	}

	/**
	 * @return the userrecieverid
	 */
	public int getUserrecieverid() {
		return userrecieverid;
	}

	/**
	 * @param userrecieverid the userrecieverid to set
	 */
	public void setUserrecieverid(int userrecieverid) {
		this.userrecieverid = userrecieverid;
	}

	/**
	 * @return the correspondantrecieverid
	 */
	public int getCorrespondantrecieverid() {
		return correspondantrecieverid;
	}

	/**
	 * @param correspondantrecieverid the correspondantrecieverid to set
	 */
	public void setCorrespondantrecieverid(int correspondantrecieverid) {
		this.correspondantrecieverid = correspondantrecieverid;
	}

	/**
	 * @return the orderAfficheCpExpediteurTbUtil
	 */
	public Boolean getOrderAfficheCpExpediteurTbUtil() {
		return orderAfficheCpExpediteurTbUtil;
	}

	/**
	 * @param orderAfficheCpExpediteurTbUtil the orderAfficheCpExpediteurTbUtil to set
	 */
	public void setOrderAfficheCpExpediteurTbUtil(
			Boolean orderAfficheCpExpediteurTbUtil) {
		this.orderAfficheCpExpediteurTbUtil = orderAfficheCpExpediteurTbUtil;
	}

	/**
	 * @return the orderAfficheCpExpediteurTbCorresp
	 */
	public Boolean getOrderAfficheCpExpediteurTbCorresp() {
		return orderAfficheCpExpediteurTbCorresp;
	}

	/**
	 * @param orderAfficheCpExpediteurTbCorresp the orderAfficheCpExpediteurTbCorresp to set
	 */
	public void setOrderAfficheCpExpediteurTbCorresp(
			Boolean orderAfficheCpExpediteurTbCorresp) {
		this.orderAfficheCpExpediteurTbCorresp = orderAfficheCpExpediteurTbCorresp;
	}

	/**
	 * @return the orderAfficheCpDestinationTbEspace
	 */
	public Boolean getOrderAfficheCpDestinationTbEspace() {
		return orderAfficheCpDestinationTbEspace;
	}

	/**
	 * @param orderAfficheCpDestinationTbEspace the orderAfficheCpDestinationTbEspace to set
	 */
	public void setOrderAfficheCpDestinationTbEspace(
			Boolean orderAfficheCpDestinationTbEspace) {
		this.orderAfficheCpDestinationTbEspace = orderAfficheCpDestinationTbEspace;
	}

	/**
	 * @return the orderAfficheCpRecepteurTbUtil
	 */
	public Boolean getOrderAfficheCpRecepteurTbUtil() {
		return orderAfficheCpRecepteurTbUtil;
	}

	/**
	 * @param orderAfficheCpRecepteurTbUtil the orderAfficheCpRecepteurTbUtil to set
	 */
	public void setOrderAfficheCpRecepteurTbUtil(
			Boolean orderAfficheCpRecepteurTbUtil) {
		this.orderAfficheCpRecepteurTbUtil = orderAfficheCpRecepteurTbUtil;
	}

	/**
	 * @return the orderAfficheCpRecepteurTbCorresp
	 */
	public Boolean getOrderAfficheCpRecepteurTbCorresp() {
		return orderAfficheCpRecepteurTbCorresp;
	}

	/**
	 * @param orderAfficheCpRecepteurTbCorresp the orderAfficheCpRecepteurTbCorresp to set
	 */
	public void setOrderAfficheCpRecepteurTbCorresp(
			Boolean orderAfficheCpRecepteurTbCorresp) {
		this.orderAfficheCpRecepteurTbCorresp = orderAfficheCpRecepteurTbCorresp;
	}

	/**
	 * @return the listExpediteursUser
	 */
	public List<User> getListExpediteursUser() {
		return listExpediteursUser;
	}

	/**
	 * @param listExpediteursUser the listExpediteursUser to set
	 */
	public void setListExpediteursUser(List<User> listExpediteursUser) {
		this.listExpediteursUser = listExpediteursUser;
	}

	/**
	 * @return the listExpediteursCorrespondant
	 */
	public List<Correspondant> getListExpediteursCorrespondant() {
		return listExpediteursCorrespondant;
	}

	/**
	 * @param listExpediteursCorrespondant the listExpediteursCorrespondant to set
	 */
	public void setListExpediteursCorrespondant(
			List<Correspondant> listExpediteursCorrespondant) {
		this.listExpediteursCorrespondant = listExpediteursCorrespondant;
	}

	/**
	 * @return the listRecepteursUser
	 */
	public List<User> getListRecepteursUser() {
		return listRecepteursUser;
	}

	/**
	 * @param listRecepteursUser the listRecepteursUser to set
	 */
	public void setListRecepteursUser(List<User> listRecepteursUser) {
		this.listRecepteursUser = listRecepteursUser;
	}

	/**
	 * @return the listRecepteursCorrespondant
	 */
	public List<Correspondant> getListRecepteursCorrespondant() {
		//JOptionPane.showMessageDialog(null, getListRecepteursCorrespondant().size());
		//listRecepteursCorrespondant = correspondantRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
		//JOptionPane.showMessageDialog(null, listRecepteursCorrespondant.size());
		return listRecepteursCorrespondant;
	}

	/**
	 * @param listRecepteursCorrespondant the listRecepteursCorrespondant to set
	 */
	public void setListRecepteursCorrespondant(
			List<Correspondant> listRecepteursCorrespondant) {
		this.listRecepteursCorrespondant = listRecepteursCorrespondant;
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
	 * @return the numeroEnregistrement
	 */
	public String getNumeroEnregistrement() {
		return numeroEnregistrement;
	}

	/**
	 * @param numeroEnregistrement the numeroEnregistrement to set
	 */
	public void setNumeroEnregistrement(String numeroEnregistrement) {
		this.numeroEnregistrement = numeroEnregistrement;
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

	public IImageService getMageService() {
		return mageService;
	}

	public void setMageService(IImageService mageService) {
		this.mageService = mageService;
	}

	/**
	 * @return the applicationScopeBean
	 */
	public ApplicationBean getApplicationScopeBean() {
		return applicationScopeBean;
	}

	/**
	 * @param applicationScopeBean the applicationScopeBean to set
	 */
	public void setApplicationScopeBean(ApplicationBean applicationScopeBean) {
		this.applicationScopeBean = applicationScopeBean;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public List<Courriers> getListSearch() {
		return listSearch;
	}

	public void setListSearch(List<Courriers> listSearch) {
		this.listSearch = listSearch;
	}

	public IAlarmesRessourceService getAlarmesRessourceService() {
		return alarmesRessourceService;
	}

	public void setAlarmesRessourceService(
			IAlarmesRessourceService alarmesRessourceService) {
		this.alarmesRessourceService = alarmesRessourceService;
	}

	/**
	 * @return the idPieceJointeToDelete
	 */
	public Integer getIdPieceJointeToDelete() {
		return idPieceJointeToDelete;
	}

	/**
	 * @param idPieceJointeToDelete the idPieceJointeToDelete to set
	 */
	public void setIdPieceJointeToDelete(Integer idPieceJointeToDelete) {
		this.idPieceJointeToDelete = idPieceJointeToDelete;
	}

	/**
	 * @return the listCourriersEspace
	 */
	public List<Courriers> getListCourriersEspace() {
		return listCourriersEspace;
	}

	/**
	 * @param listCourriersEspace the listCourriersEspace to set
	 */
	public void setListCourriersEspace(List<Courriers> listCourriersEspace) {
		this.listCourriersEspace = listCourriersEspace;
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
	 * @return the parametre_de_date_format_2
	 */
	public String getParametre_de_date_format_2() {
		return parametre_de_date_format_2;
	}

	/**
	 * @param parametre_de_date_format_2 the parametre_de_date_format_2 to set
	 */
	public void setParametre_de_date_format_2(String parametre_de_date_format_2) {
		this.parametre_de_date_format_2 = parametre_de_date_format_2;
	}

	/**
	 * @return the taille_max_de_un_fichier_uploade
	 */
	public long getTaille_max_de_un_fichier_uploade() {
		return taille_max_de_un_fichier_uploade;
	}

	/**
	 * @param taille_max_de_un_fichier_uploade the taille_max_de_un_fichier_uploade to set
	 */
	public void setTaille_max_de_un_fichier_uploade(
			long taille_max_de_un_fichier_uploade) {
		this.taille_max_de_un_fichier_uploade = taille_max_de_un_fichier_uploade;
	}
	



}


