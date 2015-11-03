package cm.dita.controller.managed.bean.mouchard;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
//import javax.swing.JOptionPane;


import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

import cm.dita.constant.IConstance;
import cm.dita.controller.managed.bean.user.privileges.RessourcesDataModel;
import cm.dita.controller.managed.bean.user.privileges.RoleDataModel;
import cm.dita.entities.Mouchard;
import cm.dita.entities.user.InfosPersonne;
import cm.dita.entities.user.User;
import cm.dita.service.domaine.inter.IMouchardRessourceService;
import cm.dita.utils.Messages;
@ManagedBean(name="mouchardControllerBean")
@ViewScoped
public class MouchardControllerBean  implements Serializable{
	
	private static final Logger LOG = Logger.getLogger(MouchardControllerBean.class);
	private static final long serialVersionUID = 1L;
	
	
	private List<Mouchard> listMouchard;
	private MouchardDataModel listMouchardMeta;
	private String mouchardPrintDate;
	
	private Mouchard mouchard;
	
	@ManagedProperty(value="#{mouchardRessourceService}")
	private IMouchardRessourceService mouchardRessourceService;
	
	public MouchardControllerBean(){
		this.mouchard = new Mouchard();
	}
	
	@PostConstruct
	public void init(){
		
		    java.util.Date aujourdhui = new java.util.Date();	    	
	    	SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_HHmmss");    	
	    	mouchardPrintDate = sdf.format(aujourdhui);
		
		listMouchard=mouchardRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
		//JOptionPane.showMessageDialog(null, "testetst"+listMouchard);
		
		setListMouchardMeta(new MouchardDataModel(listMouchard));
		
	}
	
    public void editEvent(int id) {
         
        this.mouchard = mouchardRessourceService.load(id);       

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
    	  mouchardRessourceService.deleteVersusDesabled(mouchard, IConstance.FIELD_DELETE);
		  //  		 mouchardRessourceService.tracage("Suppression Volontaire Administrateur du l'utilisateur "+user.getInfosPersonne().getNom()+" "+user.getInfosPersonne().getPrenom()+"("+user.getLogin()+") ", "suppression",user.getDateUseToSortData(), "User");
						
   	   
		    	init();//mise à jour de la liste
		    	
		    	 FacesMessage message = Messages.getMessage("messages", "global.gestion.delete", null);
			    	message.setSeverity(FacesMessage.SEVERITY_INFO);
			        context.addMessage(null, message);				    	
		        requestContext.execute("deleteDialog.hide()");	
   	  
   	   
      }catch(Exception e){
   	   e.printStackTrace();
   	  // mouchardRessourceService.tracage("Echec de suppression de l'utilisateur "+user.getInfosPersonne().getNom()+" "+user.getInfosPersonne().getPrenom()+"("+user.getLogin()+") ", "suppression",user.getDateUseToSortData(), "User");
			
   	   FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
	    	message.setSeverity(FacesMessage.SEVERITY_WARN);
	        context.addMessage(null, message);
 	 }finally{
 		
 		mouchard=null;
	  
 	 }

   }

	/**
	 * @return the listMouchardMeta
	 */
	public MouchardDataModel getListMouchardMeta() {
		return listMouchardMeta;
	}

	/**
	 * @param listMouchardMeta the listMouchardMeta to set
	 */
	public void setListMouchardMeta(MouchardDataModel listMouchardMeta) {
		this.listMouchardMeta = listMouchardMeta;
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
	 * @return the listMouchard
	 */
	public List<Mouchard> getListMouchard() {
		return listMouchard;
	}

	/**
	 * @param listMouchard the listMouchard to set
	 */
	public void setListMouchard(List<Mouchard> listMouchard) {
		this.listMouchard = listMouchard;
	}

	/**
	 * @return the mouchardPrintDate
	 */
	public String getMouchardPrintDate() {
		return mouchardPrintDate;
	}

	/**
	 * @param mouchardPrintDate the mouchardPrintDate to set
	 */
	public void setMouchardPrintDate(String mouchardPrintDate) {
		this.mouchardPrintDate = mouchardPrintDate;
	}

	/**
	 * @return the mouchard
	 */
	public Mouchard getMouchard() {
		return mouchard;
	}

	/**
	 * @param mouchard the mouchard to set
	 */
	public void setMouchard(Mouchard mouchard) {
		this.mouchard = mouchard;
	}
	
	
	
}
