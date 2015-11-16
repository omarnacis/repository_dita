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
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
//import javax.swing.JOptionPane;



import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import cm.dita.service.domaine.inter.IEspaceRessourceService;
import cm.dita.service.domaine.inter.IMouchardRessourceService;
import cm.dita.service.domaine.inter.user.IUserService;
import cm.dita.utils.Messages;

import cm.dita.constant.IConstance;
import cm.dita.constant.ISessionConstant;
import cm.dita.controller.managed.bean.user.LoginBean;
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
	
	    // LoginBean loginBean;
	
		//Spring Espace Service is injected...
		@ManagedProperty(value="#{espaceRessourceService}")
		IEspaceRessourceService espaceRessourceService;
		
		@ManagedProperty(value="#{userService}")
		IUserService userService;
		
		
		@ManagedProperty(value="#{mouchardRessourceService}")
		private IMouchardRessourceService mouchardRessourceService;
		
		private List<Espace> espaceList = new ArrayList<Espace>();
		private Espace espace;
		private Espace espaceParent;
		private List<Espace> filteredEspace=new ArrayList<Espace>();
		private EspaceDataModel espaceListDataModel;		
		private Espace[]  selectedEspace;
		
		private TreeNode root;
		private String val;
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
			 
			 espaceParent=new Espace();
			 espace=new Espace();
			
		    }
		
		public void onNodeExpand(NodeExpandEvent event) {  
	         DefaultTreeNode parent = (DefaultTreeNode) event.getTreeNode();	         
	         parent.getChildren().clear();
	         List<Espace> liste=espaceRessourceService.getMyEspace(((Espace) event.getTreeNode().getData()));	         
	         for(Espace espace:liste){	
	        	 TreeNode node = new DefaultTreeNode(espace, parent); 
		    		if(espaceRessourceService.getMyEspace(espace).size()>0)
		    			new DefaultTreeNode(new Espace(), node);
		    	}	        
	    }

	   
	    
	    public void onRowToggle(ToggleEvent event) {
	    		TreeNode root = new DefaultTreeNode(((Espace)event.getData()), null);  
	    	for(Espace espace: espaceRessourceService.getMyEspace(((Espace)event.getData()))){
	    		TreeNode node = new DefaultTreeNode(espace, root); 
	    		if(espaceRessourceService.getMyEspace(espace).size()>0)
	    			new DefaultTreeNode(new Espace(), node);
	    	}
		   	((Espace)event.getData()).setRoot(root); 	
			
		}
	    
	    public void onRowSelect(SelectEvent event) {
	        //FacesMessage msg = new FacesMessage("Car Selected", ((Car) event.getObject()).getId());
	        //FacesContext.getCurrentInstance().addMessage(null, msg);
	    	//JOptionPane.showMessageDialog(null, ((Espace) event.getObject()).getId());
	    }
		
		//CLICK SUR LES BUTTONS ajout et EDIT
		 	public void ajoutEvent(ActionEvent actionEvent) {	
		 		this.espaceList=espaceRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});	
		    	espace = new Espace();
		    	espace.setUsed(false);
		    	espaceParent=new Espace();///espaceParent.setId(0);
		    	SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2);    	
		    	espace.setDateCreation(sdf.format(new Date()));
		    	
		    }
		 	
		 	/**
		 	 * 
		 	 * @param id de l'espace a charger
		 	 * @return l'espace choisi
		 	 */
		 	
		 	public void editEvent(Espace espace) {
		 		this.espaceList=espaceRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});	
		 	    this.espace = new Espace(espaceRessourceService.load(espace.getId()));
		 	   espaceParent=new Espace();
		 	   if(this.espace.getEspace()!=null)
		 		  
		 		   espaceParent.setId(this.espace.getEspace().getId());
		 	   else
		 		   espaceParent.setId(0);;
		 	    
		     }
		 	
		 	public void detailsEvent(Espace espace) {
		 		
		 	    this.espace = espaceRessourceService.load(espace.getId());
		 	   mouchardRessourceService.tracage("Visualisation de la fonction ("+espace.getNomespace()+") ", "visualisation",espace.getDateUseToSortData(), "Espace");
			  
		        
		     }
	/**
	 * Ajoute un espace
	 * @param espace
	 * @return espace
	 */
		 	
		 	public void create(ActionEvent actionEvent) {
		    	 FacesContext context = FacesContext.getCurrentInstance();
		    	try{	
		    		
		    		//espaceParent=espaceRessourceService.load(espaceParent.getId());
		    		/*if(espaceParent!=null)
		    			espace.setHierachie(espaceParent.getHierachie()+"*"+espaceParent.getId());*/
		    		//JOptionPane.showMessageDialog(null, espaceParent);
		    		if(espaceParent.getId()!=0){
		    			espaceParent=espaceRessourceService.load(espaceParent.getId());
		    			espaceRessourceService.updateHierachie(espaceParent.getHierachie()+"*"+espaceParent.getId(),espace);
		    			espace.setHierachie(espaceParent.getHierachie()+"*"+espaceParent.getId());
		    			espace.setEspace(espaceParent);
		    		}else{
		    			espace.setHierachie("0");
		    			espace.setEspace(null);
		    		}
		    			/*if(espaceParent.getHierachie()!=null)
		    				espace.setHierachie(espaceParent.getHierachie()+"*"+espaceParent.getId());
		    			else*/
		    			//JOptionPane.showMessageDialog(null, espace.getDateCreation());
		    			
			    		espaceRessourceService.save(espace);	
				        init();//mise � jour de la liste	 	       
				        /*loginBean.setListeEspace(OperRolesSpringSecurity.lesEspacesFonctionDuRole(loginBean.getLibelleRoleAdmin()));
				        String lesEspaces = "";
				        for(Espace e :loginBean.getListeEspace()){
				        	lesEspaces += e.getNomespace()+"\n"; 
				        }
				        JOptionPane.showMessageDialog(null, "******************les espaces******************\n"+lesEspaces);*/
				        
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
			        espaceParent=new Espace();
			     
		    }
		 	
		 	
		 /**
		  * Mise à jour d'un espace
		  * @param espace
		  * @return espace
		  */
		 	
		 	 public void edition(ActionEvent actionEvent) {
		    	 FacesContext context = FacesContext.getCurrentInstance();
		    	
		    	 try{
		    		
				    		if(espaceParent.getId()==0){
				    			espaceParent.setHierachie("0");
				    			espaceParent=null;
				    		//JOptionPane.showMessageDialog(null, "Construction0");
				    		}
				    		else
				    		{
				    			espaceParent=espaceRessourceService.load(espaceParent.getId());
				    			//JOptionPane.showMessageDialog(null, espaceParent);	
				    		}
				    		if(espaceParent!=null){
				    			//if()
				    			JOptionPane.showMessageDialog(null, espaceParent);
				    			espaceRessourceService.updateHierachie(espaceParent.getHierachie()+"*"+espaceParent.getId(),espace);
				    			JOptionPane.showMessageDialog(null, espaceParent);
				    			espace.setHierachie(espaceParent.getHierachie()+"*"+espaceParent.getId());
				    			JOptionPane.showMessageDialog(null, "Construction2");
				    			if(espaceParent.getId()==0)
				    				espace.setEspace(null);
				    			else
				    				espace.setEspace(espaceParent);
				    		}else{
				    			JOptionPane.showMessageDialog(null, "Construction3");
				    			espace.setHierachie("0");
				    			espace.setEspace(null);
				    		}
				    		
			    		 	espaceRessourceService.update(espace);
					    	init();//mise � jour de la liste
					    	mouchardRessourceService.tracage("Modification de la fonction ("+espace.getNomespace()+") ", "modification",espace.getDateUseToSortData(), "Espace");
						       
					    	espace = new Espace();
					    	 espaceParent=new Espace();
					    	FacesMessage message = Messages.getMessage("messages", "global.gestion.modifier", null);
					    	message.setSeverity(FacesMessage.SEVERITY_INFO);
					        context.addMessage(null, message);
				    	
					  
		    	 }catch(Exception e){
		    		 mouchardRessourceService.tracage(" Echec modification de la fonction ("+espace.getNomespace()+") ", "modification",espace.getDateUseToSortData(), "Espace");
					   
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
		    	       
			    		  
			    	   		espaceRessourceService.deleteVersusDesabled(espace, IConstance.FIELD_DELETE);
			    	   		mouchardRessourceService.tracage("Suppression de la fonction ("+espace.getNomespace()+") ", "suppression",espace.getDateUseToSortData(), "Espace");
						    
			    	   		selectedEspace=null;
					    	   espace=null;
							    	init();//mise à jour de la liste
							    	
							    	 FacesMessage message = Messages.getMessage("messages", "global.gestion.delete", null);
								    	message.setSeverity(FacesMessage.SEVERITY_INFO);
								        context.addMessage(null, message);				    	
							         requestContext.execute("PF('deleteDialog').hide()");	
			    	    
		    	   
		       }catch(Exception e){
		  		 e.printStackTrace();
		  		mouchardRessourceService.tracage("Echec de suppression de la fonction ("+espace.getNomespace()+") ", "suppression",espace.getDateUseToSortData(), "Espace");
			    
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
		 			mouchardRessourceService.tracage("Tentative d'ajout de la fonction (nom existant) "+espace.getNomespace(), "ajout",espace.getDateUseToSortData(), "Espace");
					
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

		

		public IMouchardRessourceService getMouchardRessourceService() {
			return mouchardRessourceService;
		}

		public void setMouchardRessourceService(
				IMouchardRessourceService mouchardRessourceService) {
			this.mouchardRessourceService = mouchardRessourceService;
		}

		public Espace getEspaceParent() {
			return espaceParent;
		}

		public void setEspaceParent(Espace espaceParent) {
			this.espaceParent = espaceParent;
		}

		public TreeNode getRoot() {
			return root;
		}

		public void setRoot(TreeNode root) {
			this.root = root;
		}

		public String getVal() {
			return val;
		}

		public void setVal(String val) {
			this.val = val;
		}


		
		
}
