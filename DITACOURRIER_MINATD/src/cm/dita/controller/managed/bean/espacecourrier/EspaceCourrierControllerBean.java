package cm.dita.controller.managed.bean.espacecourrier;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;



//import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import cm.dita.beans.ApplicationBean;
import cm.dita.constant.IConstance;
import cm.dita.controller.managed.bean.courrier.CourrierControllerBean;
import cm.dita.controller.managed.bean.espacecourrier.EspaceCourrierDataModel;
import cm.dita.entities.Alarmes;
import cm.dita.entities.Courriers;
import cm.dita.entities.Espace;
import cm.dita.entities.EspaceCourrier;
import cm.dita.entities.Bordereau;
import cm.dita.entities.Piecesjointes;
//import cm.dita.entities.Mouchard;
import cm.dita.entities.Typescourriers;
import cm.dita.entities.user.User;
import cm.dita.service.domaine.inter.IAlarmesRessourceService;
import cm.dita.service.domaine.inter.IBordereauRessourceService;
import cm.dita.service.domaine.inter.ICourriersRessourceService;
import cm.dita.service.domaine.inter.IEspaceCourrierRessourceService;
import cm.dita.service.domaine.inter.IEspaceRessourceService;
import cm.dita.service.domaine.inter.IMouchardRessourceService;
import cm.dita.service.domaine.inter.IPiecesjointesRessourceService;
import cm.dita.service.domaine.inter.IStatutsRessourceService;
import cm.dita.service.domaine.inter.user.IUserService;
import cm.dita.utils.Messages;
import cm.dita.utils.MethodUtils;



@ManagedBean(name="espaceCourrierControllerBean")
@SessionScoped
public class EspaceCourrierControllerBean implements Serializable {

	/**
	 * 
	 */
	private static final Logger LOG = Logger.getLogger(EspaceCourrierControllerBean.class);
	private static final long serialVersionUID = 1L;
	
	private Courriers courrier;
	private List<Courriers> listCourriers;
	private List<EspaceCourrier> listecourriers;
	private List<EspaceCourrier> listecourrierstraite;
	private List<EspaceCourrier> listecourriersfilter;
	private List<EspaceCourrier> listeCourriersUser;
	private List<EspaceCourrier> espacecourrierList;
	private EspaceCourrier espaceCourrierP;
	private String rejetText="";
	private String courrierObservation;
	private List<Piecesjointes> listePiecejointes;
	private boolean estTermine;
	private String observeReceiver;
	
	
	//Valeur pour signifier que le courrier passe ï¿½ ï¿½tat terminï¿½
	private Boolean endvalue;
	
	private String userLoad;
	private boolean confidentialite;
	private EspaceCourrierDataModel listEspaceCourrierMeta;
	private EspaceCourrierDataModel listEspaceCourrierUserMeta;
	private Piecesjointes piece;
	
	private EspaceCourrier espacecourrier;
	private EspaceCourrier EcDetransfert;
	private EspaceCourrier espacecourriercourant;
	
	private List <User> listeUser = new ArrayList<User>();
	private User userconnected;
	
	private List <Bordereau> listeBordereau;
	private String idBordereau;
	
	private Courriers[]  selectedCourriers;
	private List <Espace> listeEspace = new ArrayList<Espace>();
	private Espace espacecourant;
	
	private String idEspaceTransfert;
	private String idEspacecourant;
	private String comboEspace;
	private Collection <EspaceCourrier> listeEspacecourrierBord;
	
	
	private List<Typescourriers> listeTypescourriers = new ArrayList<Typescourriers>();
	private Typescourriers[] selectedTypescourriers;
	private Typescourriers typescourriersSelected;
	private List<SelectItem> lesStatuts = new ArrayList<SelectItem>(Arrays.asList(new SelectItem("","Tous"),new SelectItem("Terminé","Terminé"),new SelectItem("Rejeté","Rejeté"),new SelectItem("En cours","En cours")));
	private List<SelectItem> estrecu = new ArrayList<SelectItem>(Arrays.asList(new SelectItem("","Tous"),new SelectItem("true","OUI"),new SelectItem("false","NON")));
	/*@ManagedProperty(value="#{mouchardAction}")
	private MouchardAction mouchardAction;
	*/
	
	//Spring Espace service is injected..
	@ManagedProperty(value="#{espaceRessourceService}")
	private IEspaceRessourceService espaceRessourceService;
	
	//Spring injection des piecesjointes
	 @ManagedProperty(value="#{piecesjointesRessourceService}")
	 private IPiecesjointesRessourceService piecesjointesRessourceService;

	//Spring User Service is injected...
			/**
			 * 
			 * @author Bessala
			 * @category injection du service courrier
			 */
	@ManagedProperty(value="#{courriersRessourceService}")
	private ICourriersRessourceService courriersRessourceService;
	
	@ManagedProperty(value="#{userService}")
	private IUserService userService;
			
	@ManagedProperty(value="#{statutsRessourceService}")
	private IStatutsRessourceService statutsRessourceService;
	
	//Spring User Service is injected...
	@ManagedProperty(value="#{espaceCourrierRessourceService}")
	private IEspaceCourrierRessourceService espaceCourrierRessourceService;
	
	//Spring User Service is injected...
	@ManagedProperty(value="#{bordereauRessourceService}")
	private IBordereauRessourceService bordereauRessourceService;
			
	@ManagedProperty(value="#{mouchardRessourceService}")
	private IMouchardRessourceService mouchardRessourceService;
	
	@ManagedProperty(value="#{courrierControllerBean}")
    private CourrierControllerBean initialisation;
	
	@ManagedProperty(value="#{alarmesRessourceService}")
	private IAlarmesRessourceService alarmesRessourceService;
	
	@ManagedProperty(value="#{applicationBean}")
    private ApplicationBean applicationScopeBean;
	/**
	 * permet de lister l'ensemble des courriers en cours de traitement dans l'espace courant de 
	 * l'utilisateur
	 * @param 
	 */
	
	
	@PostConstruct
	public void init(){
		
	try {
			//Cette portion permet de charger les courriers de l'utilisateur connectï¿½
		
		
			UserDetails user_secutity = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			userconnected =userService.findByLogin(user_secutity.getUsername());
			idEspacecourant=userconnected.getEspace().getId().toString();
		 // liste les espaces courrier 'les transmissions de courriers"
		    listeEspace=espaceRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
		 // Liste les courriers qui ont été traités dans l'espace
            listecourrierstraite=espaceCourrierRessourceService.listCourrierParEspacetraite(userconnected.getEspace().getId());
		    listecourriers=espaceCourrierRessourceService.listCourrierParEspacefiltre(Integer.parseInt(idEspacecourant), userconnected.getId());
		  
		   //listCourriers=courriersRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
		   setListEspaceCourrierMeta(new EspaceCourrierDataModel(listecourriers));
		 
		   //Selectionner la liste des bordereaux
		   listeBordereau=bordereauRessourceService.getListBordereauByEspaceAndTraite(userconnected.getEspace(), false);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		 
		
		
	}
	public void EnvoiMail(Courriers cour){
		
				    
			     String nomPrenomExpediteur = "";
			     
			     
			     String adresseExpediteur = "";
			     
			     Alarmes alarme1=new Alarmes();
				 
				 if(cour.getTypecourrier().getId() == 1){//courrier entrant
					 
					 nomPrenomExpediteur = cour.getCorrespondantsender().getNom();
					 adresseExpediteur = cour.getCorrespondantsender().getMailAddress();
					 
					 
				 }
				 else
					 if(cour.getTypecourrier().getId() == 2){//courrier interne
						 
						 nomPrenomExpediteur = cour.getUsersender().getInfosPersonne().getNom().concat(" ")+cour.getUsersender().getInfosPersonne().getPrenom();
						 adresseExpediteur = cour.getUsersender().getInfosPersonne().getMailAddress();
						 
						 					 
					 }
					 else
						 if(cour.getTypecourrier().getId() == 3){//courrier sortant
							 
							 nomPrenomExpediteur = cour.getUsersender().getInfosPersonne().getNom().concat(" ")+cour.getUsersender().getInfosPersonne().getPrenom();
							 adresseExpediteur = cour.getUsersender().getInfosPersonne().getMailAddress();
							 
							
						 }
			        
				 
                    if(nomPrenomExpediteur.length()>0 && adresseExpediteur.length()>0){//l'expediteur est renseigné
                    	
                    	alarme1.setEmail(adresseExpediteur);
                    	alarme1.setAlarmdate(new java.util.Date());
                    	alarme1.setAlarmetat(false);
                    	alarme1.setAlarmtype(1);
                    	alarme1.setNbrEssai(0);
                    	
                    	if(cour.getUserreceiver()!=null)
                    	alarme1.setUser_recv(cour.getUserreceiver().getId());
                    	//OMAR     alarme1.setUser_sender(cour.getUsersender().getId());
                    	alarme1.setUser_id(cour.getUsercreate().getId());
                    	alarme1.setObjet("Rejet du courrier de référence"+" "+cour.getRefid());
                    	alarme1.setCorps(applicationScopeBean.getCORPS_MAIL()+"<br> <br> Référence du courrier :"+cour.getRefid()+"a été rejeté pour les raisons suivantes"
                    	+cour.getCourobservation()+"<br><br> "+applicationScopeBean.getNOM_ENTREPRISE()+"<br><br>"+applicationScopeBean.getADRESSE()+"<br> "+applicationScopeBean.getTELEPHONE());
                    	alarmesRessourceService.save(alarme1);
    			           			        
                    }
               
	        
		// }
	}
	// listeners
	public void ChangeListener(ValueChangeEvent event){
	  
	  // on rï¿½cupï¿½re la valeur postï¿½e par l'espace lors de sa selection
	  comboEspace=(String)event.getNewValue();
	  // on rend la rï¿½ponse car on veut court-circuiter les validations
	  FacesContext.getCurrentInstance().renderResponse();
	  
	  listeUser=userService.listUserParEspace(Integer.parseInt(comboEspace));
	}
	
	public void listebordereaux(){
		 listeBordereau=bordereauRessourceService.getListBordereauByEspaceAndTraite(userconnected.getEspace(), false);
	}
	
	 public void delete(ActionEvent actionEvent) {
	    	
    	 FacesContext context = FacesContext.getCurrentInstance();
    	 RequestContext requestContext = RequestContext.getCurrentInstance();
       try{
    	  
    	   if(!espacecourrierList.isEmpty()){
	    	   for(int i=0;i<espacecourrierList.size();i++){	
	    		  
	    		   	if(espacecourrierList.get(i).isRecu() && espacecourrierList.get(i).getStatut().getId()!=1){
	    		    espaceCourrierRessourceService.deleteVersusDesabled(espacecourrierList.get(i), IConstance.FIELD_DELETE);
	    		    if(espacecourrierList.get(i).getStatut().getId()==2||espacecourrierList.get(i).getStatut().getId()==4)
	    		    	courriersRessourceService.deleteVersusDesabled(espacecourrierList.get(i).getCourrier(), IConstance.FIELD_DELETE);
		    	
		    	    FacesMessage message = Messages.getMessage("messages", "global.gestion.delete", null);
			    	message.setSeverity(FacesMessage.SEVERITY_INFO);
			        context.addMessage(null, message);				    	
			        mouchardRessourceService.tracage("Supprimer un ou plusieurs courrier", userconnected);
	    		   	}
    	   }
	    	   }else{
	    		   context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"","Vous devez selection au moins un courrier avant de le supprimer"));
	    	   }
    	   
       }catch(Exception e){
    	   e.printStackTrace();
    	    FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
	    	message.setSeverity(FacesMessage.SEVERITY_WARN);
	        context.addMessage(null, message);
  	 }finally{
  		 
  		 //Permet de mettre a jour la liste des courriers de la table des courrier
  		espacecourrierList.clear();
  		initialisation.init();
  		}
 	   
  	 }
 
	
	/**
	 * fonction permettant de rejetter un courrier
	 */
	public void rejeter(){
		FacesContext context = FacesContext.getCurrentInstance();
		
		try{
			
			if(!listecourriersfilter.isEmpty()){
			
			 for(int i = 0; i < listecourriersfilter.size(); i++)
			 	{
				    listecourriersfilter.get(i).setStatut(statutsRessourceService.load(4));
				 	listecourriersfilter.get(i).getCourrier().setCourobservation(rejetText);
					listecourriersfilter.get(i).setObservation(rejetText);
					listecourriersfilter.get(i).getCourrier().setIdstatutcourantducour(4);
					espaceCourrierRessourceService.save(listecourriersfilter.get(i));
					EnvoiMail(listecourriersfilter.get(i).getCourrier());
		 
			 	}
			   	 this.init();
			 	context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"","Courrier(s) rejeté(s)"));
			   	mouchardRessourceService.tracage("Rejeter au moins un courrier", userconnected);
			} else{
				 context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"","Impossible de supprimer. Soit vous n'avez sélectionné aucun courrier, soit un ou plusieurs courrier(s) non recu ont été sélectionnés"));
				 mouchardRessourceService.tracage("Rejet sans selection de courriers", userconnected); 
			}
			   
					}catch(Exception e){
						 e.printStackTrace();
						
				mouchardRessourceService.tracage("A echouer lors du reject d'un courrier", userconnected); 
				
			
		}finally{
			listecourriersfilter.clear();
			listecourriers=espaceCourrierRessourceService.listCourrierParEspacefiltre(Integer.parseInt(idEspacecourant), userconnected.getId());
			rejetText="";
			initialisation.init();
			   //listCourriers=courriersRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
			setListEspaceCourrierMeta(new EspaceCourrierDataModel(listecourriers));
		}
	}
	
	//permet de créer un bordereau
	public void create() {
    	FacesContext context = FacesContext.getCurrentInstance();
    	try {
			
           	SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2);    	
           	
     		Bordereau bordereau = new Bordereau();
     		bordereau = MethodUtils.getBordereauDebutTransfert(userconnected, sdf.format(new Date()), null); 		
     		bordereauRessourceService.save(bordereau);
    		 
     		Bordereau bordereau2 =bordereauRessourceService.getBordereauByUsercreateIdAndDateUseToSortData(userconnected, bordereau.getDateUseToSortData());
     		
     		SimpleDateFormat formater1 = null;			
    		formater1 = new SimpleDateFormat("ddMMyyyy");
     		String numbordereau = MethodUtils.fabriqueNumBordereau(bordereau2, formater1.format(new java.util.Date()));
     		bordereau2.setNumbordereau(numbordereau);
     		
     		bordereauRessourceService.save(bordereau2);
     		
     		init();//mise Ã  jour de la liste
     		listeBordereau=bordereauRessourceService.getListBordereauByEspaceAndTraite(userconnected.getEspace(), false);
     		FacesMessage message = Messages.getMessage("messages", "info.succes.bordereau.add", null);
     		//FacesMessage message = Messages.getMessage("messages", "info.succes.bordereau.add"+numbordereau, null);
		    	message.setSeverity(FacesMessage.SEVERITY_INFO);
		        context.addMessage(null, message);
    		
		} catch (Exception e) {
	    	 FacesMessage message = Messages.getMessage("messages", "info.echec.bordereau.add", null);
		    	message.setSeverity(FacesMessage.SEVERITY_WARN);
		        context.addMessage(null, message);
			// TODO: handle exception
		}	
   }
	
	
	//prend uniquement les courriers qui sont en cours
	public void updateSelected(){
		/*FacesContext context = FacesContext.getCurrentInstance();
		boolean testSelected=false;*/
		listeEspace=espaceRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
		listecourriersfilter=new ArrayList<EspaceCourrier>();
		for(int i = 0; i < espacecourrierList.size(); i++)
	 	{
			if(espacecourrierList.get(i).getStatut().getId()!=1 && espacecourrierList.get(i).getStatut().getId()!=3 && espacecourrierList.get(i).isRecu()) {
	        		/*testSelected=true;*/
				  	listecourriersfilter.add(espacecourrierList.get(i));
	        	
	        }
		 	
	 	}
		 

	}
	public void updateSelectedRejet(){
		FacesContext context = FacesContext.getCurrentInstance();
		boolean testSelected=false;
		listecourriersfilter=new ArrayList<EspaceCourrier>();
		for(int i = 0; i < espacecourrierList.size(); i++)
	 	{
			if(espacecourrierList.get(i).getStatut().getId()!=1 && espacecourrierList.get(i).getStatut().getId()!=3 && espacecourrierList.get(i).getStatut().getId()!=4 && espacecourrierList.get(i).isRecu()) {
	        	/*testSelected=true;*/
				  	listecourriersfilter.add(espacecourrierList.get(i));
	        	
	        }else
	        {
	        	testSelected=true;
	        }
		 	
	 	}
		 
		 if(testSelected){
		 FacesMessage message = Messages.getMessage("messages", "global.gestion.notselected", null);
		 message.setSeverity(FacesMessage.SEVERITY_INFO);
		 context.addMessage(null, message);
		 }
	}
	public void viewEvent(Long value){
		
		
		espacecourriercourant=espaceCourrierRessourceService.load(value);
		
		//permet d'actualiser la liste de pieces jointes
		initialisation.addAndViewPJEvent(espacecourriercourant.getCourrier().getId());
		estTermine=true;
		if(espacecourriercourant.getStatut().getId()==1 || espacecourriercourant.getStatut().getId()==3 )
			estTermine=false;
		else
			estTermine=true;
	
		if (espacecourriercourant.getCourrier().getUserreceiver()!=null && espacecourriercourant.getCourrier().isConfidentiel() && (espacecourriercourant.getCourrier().getUserreceiver().getId()==userconnected.getId()||espacecourriercourant.getUsercreate().getId()==userconnected.getId()))
		{
			confidentialite=false;
			mouchardRessourceService.tracage("A visualiser son courrier confidentiel", userconnected);
		}else if( espacecourriercourant.getCourrier().isConfidentiel() && (espacecourriercourant.getCourrier().getCorrespondantreceiver()!=null && espacecourriercourant.getUsercreate().getId()==userconnected.getId())){
			confidentialite=false;
			mouchardRessourceService.tracage("A visualiser son courrier confidentiel", userconnected);
		}
		
		else{
		confidentialite=espacecourriercourant.getCourrier().isConfidentiel();
			mouchardRessourceService.tracage("Visualiser interface courrier", userconnected);
		}	
		//mouchardAction.controller("Visualiser interface courrier");
		listePiecejointes=(List<Piecesjointes>)espacecourriercourant.getCourrier().getPiecesjointesCollection();
	}
	
public void viewConf(Long value){
		
		
		espacecourriercourant=espaceCourrierRessourceService.load(value);
		
		//permet d'actualiser la liste de pieces jointes
		
		if (espacecourriercourant.getCourrier().getUserreceiver()!=null && espacecourriercourant.getCourrier().isConfidentiel() && (espacecourriercourant.getCourrier().getUserreceiver().getId()==userconnected.getId()||espacecourriercourant.getUsercreate().getId()==userconnected.getId()))
		{
			confidentialite=false;
			mouchardRessourceService.tracage("A visualiser son courrier confidentiel", userconnected);
		}else if( espacecourriercourant.getCourrier().isConfidentiel() && (espacecourriercourant.getCourrier().getCorrespondantreceiver()!=null && espacecourriercourant.getUsercreate().getId()==userconnected.getId())){
			confidentialite=false;
			mouchardRessourceService.tracage("A visualiser son courrier confidentiel", userconnected);
		}
		
		else{
		confidentialite=espacecourriercourant.getCourrier().isConfidentiel();
			mouchardRessourceService.tracage("Visualiser interface courrier", userconnected);
		}	
		
	}
	
	//Permet de charger le courrier avec l'id 
	public void editObservation(Long id){
		
		espacecourriercourant=espaceCourrierRessourceService.load(id);
		
	}
	
	public StreamedContent prepDownload(int id) throws Exception {
		piece =piecesjointesRessourceService.load(id);
		StreamedContent download=new DefaultStreamedContent();
		    File file = new File(piece.getPiecefichier());
		    InputStream input = new FileInputStream(file);
		    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		    download=new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName());
		    System.out.println("PREP = " + download.getName());
		return download;
		}
	
	/**
	 * fonction permettant de transferer les documents d'un espace Ã© un autre
	 * @param idespace
	 */
	
	public void manyTransfert(){
		
		//mise ï¿½ jour du courrier de l'espace courant ï¿½ transferer
		
		
		 FacesContext context = FacesContext.getCurrentInstance();
		 if(userLoad=="") userLoad="0";
		 
		  try{
			 if(!listecourriersfilter.isEmpty() && idEspaceTransfert!="" && (Integer.parseInt(idEspaceTransfert)!=Integer.parseInt(idEspacecourant)) &&(Integer.parseInt(userLoad)!=userconnected.getId())){
				 
				 SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2); 
				    EspaceCourrier eCourrierT= listecourriersfilter.get(0);
						
				    eCourrierT.setStatut(statutsRessourceService.load(3));
					eCourrierT.setDatesortie(sdf.format(new Date()));
					eCourrierT.setObservation(courrierObservation);
					eCourrierT.getCourrier().setCourobservation(courrierObservation);
					eCourrierT.setEspacedestination(espaceRessourceService.load(Integer.parseInt(idEspaceTransfert)));
					eCourrierT.setRecu(false);
					if(idBordereau!="")
					eCourrierT.setBordereau(bordereauRessourceService.load(Integer.parseInt(idBordereau)));
						//JOptionPane.showMessageDialog(null, eCourrierT.getBordereau().getNumbordereau());
					EspaceCourrier EcDetransfert= new EspaceCourrier();
					  
					EcDetransfert.setCourrier(courriersRessourceService.load(eCourrierT.getCourrier().getId()));
					EcDetransfert.setDelate(false);
					EcDetransfert.setEspaceEnterieur(eCourrierT.getId());
					EcDetransfert.setDatearrive(sdf.format(new Date()));
					EcDetransfert.setRecu(false);
				    EcDetransfert.setEspace(espaceRessourceService.load(Integer.parseInt(idEspaceTransfert)));
				    EcDetransfert.setUsercreate(userconnected);
				    EcDetransfert.setEspacePrecedent(espaceRessourceService.load(eCourrierT.getEspace().getId()));
				    EcDetransfert.setStatut(statutsRessourceService.load(2));
				    EcDetransfert.getCourrier().setIdstatutcourantducour(statutsRessourceService.load(2).getId());
				    EcDetransfert.getCourrier().setIdespacecourantducour(Integer.parseInt(idEspaceTransfert));
				    
				    if (userLoad!="0")
				    {
				  	  EcDetransfert.setUserreceiver(userService.load(Integer.parseInt(userLoad)));
				    }
					 
				 espaceCourrierRessourceService.transferMany(eCourrierT, EcDetransfert);
				 
						 		
			 		mouchardRessourceService.tracage("Transferer ou plusieurs courriers", userconnected);
			 		initialisation.init();
			 		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"","Le transfert a été bien éffectué"));
	    	 		
			 } else {
				 if (listecourriersfilter.isEmpty())
					 context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"","Une erreur est survenue lors du transfert du courrier. La liste des courriers est vide"));
				 else if (Integer.parseInt(idEspaceTransfert)==Integer.parseInt(idEspacecourant) && userLoad=="")
				 context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"","Une erreur est survenue lors du transfert du courrier. Vous devez choisir un utilisateur pour transferer dans le meme espace"));
				 else if(idEspaceTransfert=="")
					 context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"","Une erreur est survenue lors du transfert du courrier. Vous devez choisir un espace pour effectuer le transfert"));
				 else if(Integer.parseInt(userLoad)==userconnected.getId())
					 context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"","Une erreur est survenue lors du transfert du courrier. Vous êtes entrain de vous retransférer le même courrier"));
			 }
			 
		 }catch(Exception e){
			 e.printStackTrace();
			 context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"","Une erreur est survenue lors du transfert du courrier"));
			 
		 }finally{
			 listecourriersfilter.clear();
			espacecourrierList =null;
				idEspaceTransfert="";
				courrierObservation="";
				userLoad="";
				idBordereau="";
			}
		
		
		
	}
	
	public void transfertView(Long id){
		 EspaceCourrier espaceC=new EspaceCourrier();
		 listecourriersfilter = new ArrayList<EspaceCourrier>();
		 espaceC=espaceCourrierRessourceService.load(id);
		 if (listecourriersfilter!=null)
		 listecourriersfilter.clear();
		 listecourriersfilter.add(espaceC);
		 listeEspace=espaceRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
		 listeBordereau=bordereauRessourceService.getListBordereauByEspaceAndTraite(userconnected.getEspace(), false);
		 
		
		
	}
	
	
	public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Un courrier selectionné", ((EspaceCourrier) event.getObject()).getId().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        espacecourrier=(EspaceCourrier)event.getObject();
        
        //Definition de la date de sortie d'un courrier d'un espace Ã© l'autre
        SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2);    	
    	espacecourrier.setDatesortie(sdf.format(new Date()));
       // JOptionPane.showMessageDialog(null, ""+espacecourrier.getCourrier().getRefid()+"");
    }
	 
	//Permet de changer le statut de reception d'un courrier récemment envoyé
	public void changeReceptionStatut(){
		
		//JOptionPane.showMessageDialog(null,espacecourrier.isRecu());
        SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2);
        //JOptionPane.showMessageDialog(null,espacecourrier.getObservation());
        espaceCourrierP = espaceCourrierRessourceService.load(espacecourriercourant.getEspaceEnterieur());
        espaceCourrierP.setObserveReceiver(observeReceiver);
        espaceCourrierP.setRecu(true);
        
        if (espacecourriercourant.getCourrier().getUserreceiver()!=null && espacecourriercourant.getCourrier().isConfidentiel() && (espacecourriercourant.getCourrier().getUserreceiver().getId()==userconnected.getId()||espacecourriercourant.getUsercreate().getId()==userconnected.getId()))
		{
			//confidentialite=false;
			espacecourriercourant.setConfidentialite(false);
			
		}else if( espacecourriercourant.getCourrier().isConfidentiel() && (espacecourriercourant.getCourrier().getCorrespondantreceiver()!=null && espacecourriercourant.getUsercreate().getId()==userconnected.getId())){
			//confidentialite=false;
			espacecourriercourant.setConfidentialite(false);
			}
		
		else{
			espacecourriercourant.setConfidentialite(espacecourriercourant.getCourrier().isConfidentiel());
			
		}	
        
        //défini la date de receptio du courrier
        espaceCourrierP.setRecepationDate(sdf.format(new Date()));
        espacecourriercourant.setRecu(true);
        
        espaceCourrierRessourceService.save(espaceCourrierP);
        espaceCourrierRessourceService.save(espacecourriercourant);
        initialisation.init();
	}
	 public void editCourrier(ActionEvent actionEvent) {    
		 FacesContext context = FacesContext.getCurrentInstance();
		 try{
			 
			 courriersRessourceService.update(courrier); 
	    	 this.init();
	    	 
	    	 context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"","Courrier modifiÃ©e"));
		 }catch(Exception e){
			 e.printStackTrace();
			 context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"","Echec"));
			 
		 }
	 
	}
	 
	 
	 public void updatetransfere(){
		 
		 listecourrierstraite=espaceCourrierRessourceService.listCourrierParEspacetraite(Integer.parseInt(idEspacecourant));
		 
	 }
	 public void fermerView(){
			if(endvalue){
				//met le courrier qui se trouve dans un espace Ã© l'ï¿½tat terminï¿½
				espacecourriercourant.setStatut(statutsRessourceService.load(1));
				
				//on place le statut courant du courrier 
				espacecourriercourant.getCourrier().setIdstatutcourantducour(1);
				
				espaceCourrierRessourceService.save(espacecourriercourant);
				
				
				init();
				mouchardRessourceService.tracage("A terminÃ© le courrier "+espacecourriercourant.getCourrier().getRefid(), userconnected);
				endvalue=false;
				//endvalue=false;
			}
		}
	 public void afficherNumero(){
		 init();
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
	 * @return the espacecourrier
	 */
	public EspaceCourrier getEspacecourrier() {
		return espacecourrier;
	}
	/**
	 * @param espacecourrier the espacecourrier to set
	 */
	public void setEspacecourrier(EspaceCourrier espacecourrier) {
		this.espacecourrier = espacecourrier;
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
	 * @return the listeEspace
	 */
	public List<Espace> getListeEspace() {
		return listeEspace;
	}
	/**
	 * @param listeEspace the listeEspace to set
	 */
	public void setListeEspace(List<Espace> listeEspace) {
		this.listeEspace = listeEspace;
	}
	/**
	 * @return the espacecourant
	 */
	public Espace getEspacecourant() {
		return espacecourant;
	}
	/**
	 * @param espacecourant the espacecourant to set
	 */
	public void setEspacecourant(Espace espacecourant) {
		this.espacecourant = espacecourant;
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
	 * @return the lesStatuts
	 */
	public List<SelectItem> getLesStatuts() {
		return lesStatuts;
	}
	/**
	 * @param lesStatuts the lesStatuts to set
	 */
	public void setLesStatuts(List<SelectItem> lesStatuts) {
		this.lesStatuts = lesStatuts;
	}
	/**
	 * @return the idEspaceTransfert
	 */
	public String getIdEspaceTransfert() {
		return idEspaceTransfert;
	}
	/**
	 * @param idEspaceTransfert the idEspaceTransfert to set
	 */
	public void setIdEspaceTransfert(String idEspaceTransfert) {
		this.idEspaceTransfert = idEspaceTransfert;
	}
	/**
	 * @return the comboEspace
	 */
	public String getComboEspace() {
		return comboEspace;
	}
	/**
	 * @param comboEspace the comboEspace to set
	 */
	public void setComboEspace(String comboEspace) {
		this.comboEspace = comboEspace;
	}
	/**
	 * @return the userLoad
	 */
	public String getUserLoad() {
		return userLoad;
	}
	/**
	 * @param userLoad the userLoad to set
	 */
	public void setUserLoad(String userLoad) {
		this.userLoad = userLoad;
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
	 * @return the ecDetransfert
	 */
	public EspaceCourrier getEcDetransfert() {
		return EcDetransfert;
	}
	/**
	 * @param ecDetransfert the ecDetransfert to set
	 */
	public void setEcDetransfert(EspaceCourrier ecDetransfert) {
		EcDetransfert = ecDetransfert;
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
	 * @return the rejetText
	 */
	public String getRejetText() {
		return rejetText;
	}
	/**
	 * @param rejetText the rejetText to set
	 */
	public void setRejetText(String rejetText) {
		this.rejetText = rejetText;
	}
	/**
	 * @return the listeCourriersUser
	 */
	public List<EspaceCourrier> getListeCourriersUser() {
		return listeCourriersUser;
	}
	/**
	 * @param listeCourriersUser the listeCourriersUser to set
	 */
	public void setListeCourriersUser(List<EspaceCourrier> listeCourriersUser) {
		this.listeCourriersUser = listeCourriersUser;
	}
	/**
	 * @return the listEspaceCourrierUserMeta
	 */
	public EspaceCourrierDataModel getListEspaceCourrierUserMeta() {
		return listEspaceCourrierUserMeta;
	}
	/**
	 * @param listEspaceCourrierUserMeta the listEspaceCourrierUserMeta to set
	 */
	public void setListEspaceCourrierUserMeta(
			EspaceCourrierDataModel listEspaceCourrierUserMeta) {
		this.listEspaceCourrierUserMeta = listEspaceCourrierUserMeta;
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
	 * @return the espacecourriercourant
	 */
	public EspaceCourrier getEspacecourriercourant() {
		return espacecourriercourant;
	}
	/**
	 * @param espacecourriercourant the espacecourriercourant to set
	 */
	public void setEspacecourriercourant(EspaceCourrier espacecourriercourant) {
		this.espacecourriercourant = espacecourriercourant;
	}
	/**
	 * @return the courrierObservation
	 */
	public String getCourrierObservation() {
		return courrierObservation;
	}
	/**
	 * @param courrierObservation the courrierObservation to set
	 */
	public void setCourrierObservation(String courrierObservation) {
		this.courrierObservation = courrierObservation;
	}
	/**
	 * @return the espacecourrierList
	 */
	public List<EspaceCourrier> getEspacecourrierList() {
		return espacecourrierList;
	}
	/**
	 * @param espacecourrierList the espacecourrierList to set
	 */
	public void setEspacecourrierList(List<EspaceCourrier> espacecourrierList) {
		this.espacecourrierList = espacecourrierList;
	}
	/**
	 * @return the confidentialite
	 */
	public boolean getConfidentialite() {
		return confidentialite;
	}
	/**
	 * @param confidentialite the confidentialite to set
	 */
	public void setConfidentialite(boolean confidentialite) {
		this.confidentialite = confidentialite;
	}
	/**
	 * @return the endvalue
	 */
	public Boolean getEndvalue() {
		return endvalue;
	}
	/**
	 * @param endvalue the endvalue to set
	 */
	public void setEndvalue(Boolean endvalue) {
		this.endvalue = endvalue;
	}
	/**
	 * @return the listeEspacecourrierBord
	 */
	public Collection<EspaceCourrier> getListeEspacecourrierBord() {
		return listeEspacecourrierBord;
	}
	/**
	 * @param listeEspacecourrierBord the listeEspacecourrierBord to set
	 */
	public void setListeEspacecourrierBord(
			Collection<EspaceCourrier> listeEspacecourrierBord) {
		this.listeEspacecourrierBord = listeEspacecourrierBord;
	}
	/**
	 * @return the bordereauRessourceService
	 */
	public IBordereauRessourceService getBordereauRessourceService() {
		return bordereauRessourceService;
	}
	/**
	 * @param bordereauRessourceService the bordereauRessourceService to set
	 */
	public void setBordereauRessourceService(
			IBordereauRessourceService bordereauRessourceService) {
		this.bordereauRessourceService = bordereauRessourceService;
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
	 * @return the listecourriersfilter
	 */
	public List<EspaceCourrier> getListecourriersfilter() {
		return listecourriersfilter;
	}
	/**
	 * @param listecourriersfilter the listecourriersfilter to set
	 */
	public void setListecourriersfilter(List<EspaceCourrier> listecourriersfilter) {
		this.listecourriersfilter = listecourriersfilter;
	}
	/**
	 * @return the listecourrierstraite
	 */
	public List<EspaceCourrier> getListecourrierstraite() {
		return listecourrierstraite;
	}
	/**
	 * @param listecourrierstraite the listecourrierstraite to set
	 */
	public void setListecourrierstraite(List<EspaceCourrier> listecourrierstraite) {
		this.listecourrierstraite = listecourrierstraite;
	}
	/**
	 * @return the initialisation
	 */
	public CourrierControllerBean getInitialisation() {
		return initialisation;
	}
	/**
	 * @param initialisation the initialisation to set
	 */
	public void setInitialisation(CourrierControllerBean initialisation) {
		this.initialisation = initialisation;
	}
	/**
	 * @return the listePiecejointes
	 */
	public List<Piecesjointes> getListePiecejointes() {
		return listePiecejointes;
	}
	/**
	 * @param listePiecejointes the listePiecejointes to set
	 */
	public void setListePiecejointes(List<Piecesjointes> listePiecejointes) {
		this.listePiecejointes = listePiecejointes;
	}
	/**
	 * @return the piece
	 */
	public Piecesjointes getPiece() {
		return piece;
	}
	/**
	 * @param piece the piece to set
	 */
	public void setPiece(Piecesjointes piece) {
		this.piece = piece;
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
	 * @return the listeBordereau
	 */
	public List<Bordereau> getListeBordereau() {
		return listeBordereau;
	}
	/**
	 * @param listeBordereau the listeBordereau to set
	 */
	public void setListeBordereau(List<Bordereau> listeBordereau) {
		this.listeBordereau = listeBordereau;
	}
	/**
	 * @return the idBordereau
	 */
	public String getIdBordereau() {
		return idBordereau;
	}
	/**
	 * @param idBordereau the idBordereau to set
	 */
	public void setIdBordereau(String idBordereau) {
		this.idBordereau = idBordereau;
	}
	/**
	 * @return the estTermine
	 */
	public boolean isEstTermine() {
		return estTermine;
	}
	/**
	 * @param estTermine the estTermine to set
	 */
	public void setEstTermine(boolean estTermine) {
		this.estTermine = estTermine;
	}
	/**
	 * @return the alarmesRessourceService
	 */
	public IAlarmesRessourceService getAlarmesRessourceService() {
		return alarmesRessourceService;
	}
	/**
	 * @param alarmesRessourceService the alarmesRessourceService to set
	 */
	public void setAlarmesRessourceService(
			IAlarmesRessourceService alarmesRessourceService) {
		this.alarmesRessourceService = alarmesRessourceService;
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
	/**
	 * @return the espaceCourrierP
	 */
	public EspaceCourrier getEspaceCourrierP() {
		return espaceCourrierP;
	}
	/**
	 * @param espaceCourrierP the espaceCourrierP to set
	 */
	public void setEspaceCourrierP(EspaceCourrier espaceCourrierP) {
		this.espaceCourrierP = espaceCourrierP;
	}
	/**
	 * @return the observeReceiver
	 */
	public String getObserveReceiver() {
		return observeReceiver;
	}
	/**
	 * @param observeReceiver the observeReceiver to set
	 */
	public void setObserveReceiver(String observeReceiver) {
		this.observeReceiver = observeReceiver;
	}
	/**
	 * @return the estrecu
	 */
	public List<SelectItem> getEstrecu() {
		return estrecu;
	}
	/**
	 * @param estrecu the estrecu to set
	 */
	public void setEstrecu(List<SelectItem> estrecu) {
		this.estrecu = estrecu;
	}
	

}
