package cm.dita.controller.managed.bean.user;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.swing.JOptionPane;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import cm.dita.beans.Sexe;
import cm.dita.constant.IConstance;
import cm.dita.entities.Espace;
import cm.dita.entities.Image;
import cm.dita.entities.Personne;
import cm.dita.entities.user.InfosPersonne;
import cm.dita.entities.user.User;
import cm.dita.service.domaine.inter.IImageService;
import cm.dita.service.domaine.inter.IMouchardRessourceService;
import cm.dita.service.domaine.inter.user.IUserService;
import cm.dita.utils.Messages;
import cm.dita.utils.MethodUtils;

@ManagedBean(name = "profilBean")
@SessionScoped
public class ProfilBean implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
			//Spring User Service is injected...
	@ManagedProperty(value="#{userService}")
	IUserService userService;
	
	@ManagedProperty(value="#{mouchardRessourceService}")
	private IMouchardRessourceService mouchardRessourceService;
	
	@ManagedProperty(value="#{IImageService}")
	IImageService imageService;
			
	private User user;
	private String  lastPassword;
	private UploadedFile file;
	private int idCat;
	private int idSrv;
//POUR CHANGER LES MintOTS DE PASSE
	private String  password;
	private String password2;
	private String password3;
	private boolean skip;
	private boolean changeImage;
	private Personne personne;


	List<Sexe> listSexe;
	
	public ProfilBean(){
		user=new User();
		
	}
	
	// modification du profil    
    public void profilEvent(ActionEvent actionEvent){
    	
    	
    		UserDetails user_secutity = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		user =userService.findByLogin(user_secutity.getUsername());
    			
         if(this.user!=null){       	 
        	
        	 
	        setLastPassword(user.getPassword());
	       this.personne=this.user.getInfosPersonne();
	       this.changeImage=false;
	        
	       this.user=new User(userService.findByLogin(user_secutity.getUsername()));
	       // this.user=userService.findByLogin(user_secutity.getUsername());
	        
        }
         
        // listSexe=Sexe.initialise();
    	
    }
    
    public void photoEvent(Personne personne){
 		this.personne=personne;
 		
 	}
    
    /**
	 * @see mise a jour
	 * 
	 * @param user
	 */
	public void update() {
   	 FacesContext context = FacesContext.getCurrentInstance();
   	
   	try{
		   		String ext="";
		   		int id=personne.getPersid();
		   		if(this.changeImage){
				try{
		    		Image image=new Image();
		    		image.setImage(this.file.getContents());
		    		
		        	image.setType(2);
		        	ext = FilenameUtils.getExtension(this.file.getFileName());
		        	image.setName("profil_"+id+"."+ext);
		        	image.setIdEntite(id);
		        	imageService.deleteAndSAveByImg(image);
		        	
		        	String defaultSystemDirFileUpload  = FacesContext.getCurrentInstance().getExternalContext().getRealPath("");	    			
					  defaultSystemDirFileUpload=defaultSystemDirFileUpload+File.separator+"uplaoded"+File.separator+"profil";
					 MethodUtils.copyFile(defaultSystemDirFileUpload+File.separator, "profil_"+id+"."+ext, this.file.getInputstream());
					 
					 personne.setSrc_img(File.separator+"uplaoded"+File.separator+"profil"+File.separator+"profil_"+id+"."+ext);
					// personneRessourceService.update(personne);
		        	
		    	}catch(Exception e){
		    		//e.printStackTrace();
		    		
		    	}finally{
			    }	
		   		}
   				
	
    		
    		user.setInfosPersonne(personne);
    		personne.setUser(user);
    		userService.update(user);    	
	    	mouchardRessourceService.tracage("Modification de son profil par :"+user.getInfosPersonne().getPersnom()+" "+user.getInfosPersonne().getPersprenom(), "modification",user.getDateUseToSortData(), "User");
			
	    	FacesMessage message = Messages.getMessage("messages", "global.gestion.reussi", null);
	    	message.setSeverity(FacesMessage.SEVERITY_INFO);
	        context.addMessage(null, message);
		
   	
   	 }catch(Exception e){
   		 e.printStackTrace();
   		mouchardRessourceService.tracage("Echec modification de son profil par :"+user.getInfosPersonne().getPersnom()+" "+user.getInfosPersonne().getPersprenom(), "modification",user.getDateUseToSortData(), "User");
		
   		 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
		    	message.setSeverity(FacesMessage.SEVERITY_WARN);
		        context.addMessage(null, message);
   	 }finally{
   		user = new User();
   		this.personne=new Personne();
   		this.changeImage=false;
   		this.file=null;
   		
       
   	 }
   	
	     
   }
	
	public void handleFileUpload(FileUploadEvent event) { 
 		// JOptionPane.showMessageDialog(null, event.getFile().getFileName());
        this.file=event.getFile();
     }  
	
	 /**
     * 
     * @see Focntion qui modifie le mot de passe
     * 
     * @param ancien mot de passe
     * @param nouveau mot de passe
     */
    
    public void changePass(ActionEvent actionEvent){   
    	 //JOptionPane.showMessageDialog(null, "pz");
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
			    	   user.setInit_pass(false);
			    	   userService.update(user); // mise a jour de l'utilisateur
			    	   
			    	   mouchardRessourceService.tracage("Modification de son mot de passe par :"+user.getInfosPersonne().getPersnom()+" "+user.getInfosPersonne().getPersprenom(), "modification",user.getDateUseToSortData(), "User");
						
			    	   FacesMessage message = Messages.getMessage("messages", "global.gestion.reussi", null);
				    	message.setSeverity(FacesMessage.SEVERITY_INFO);
				        context.addMessage(null, message);
			    	   
			       }else{
			    	   mouchardRessourceService.tracage("Tentative de modification du mot de passe par :"+user.getInfosPersonne().getPersnom()+" "+user.getInfosPersonne().getPersprenom(), "modification",user.getDateUseToSortData(), "User");
						
			    	   FacesMessage message = Messages.getMessage("messages", "user.password.echec", null);
			 	    	message.setSeverity(FacesMessage.SEVERITY_WARN);
			 	        context.addMessage(null, message);
			       }
		      
		   
    	 }catch(Exception e){
    		 mouchardRessourceService.tracage("Echec modification de son mot de passe par :"+user.getInfosPersonne().getPersnom()+" "+user.getInfosPersonne().getPersprenom(), "modification",user.getDateUseToSortData(), "User");
				
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
 	 * Validation de l'existance 
 	 * @return
 	 */
 	public void validateName(FacesContext context, UIComponent component, Object value) throws ValidatorException {
 		  String valeur = (String) value;
 	
 		 user.setLogin(valeur);
 		if(userService.userExiste(user)){
    		
   		 mouchardRessourceService.tracage("Modification de son profil erronée (Login existant) par :"+user.getInfosPersonne().getPersnom()+" "+user.getInfosPersonne().getPersprenom(), "modification",user.getDateUseToSortData(), "User");
   		
   		FacesMessage message = Messages.getMessage("messages", "user.login.validator", null);
	    	message.setSeverity(FacesMessage.SEVERITY_ERROR);
	        
	        throw new ValidatorException(message);
 		}
 		
 		  
 		}	
 	
 	 public String onFlowProcess(FlowEvent event) {
	    	
	        if(skip) {
	            skip = false;   //reset in case user goes back
	            return "confirm_profil";
	        }
	        else {
	            return event.getNewStep();
	        }
	    }

 	public void addMessage() {
	      
	    }

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getLastPassword() {
		return lastPassword;
	}

	public void setLastPassword(String lastPassword) {
		this.lastPassword = lastPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	
	public List<Sexe> getListSexe() {
		return listSexe;
	}

	public void setListSexe(List<Sexe> listSexe) {
		this.listSexe = listSexe;
	}

	public IMouchardRessourceService getMouchardRessourceService() {
		return mouchardRessourceService;
	}

	public void setMouchardRessourceService(
			IMouchardRessourceService mouchardRessourceService) {
		this.mouchardRessourceService = mouchardRessourceService;
	}

	
	public void setImageService(IImageService imageService) {
		this.imageService = imageService;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public int getIdCat() {
		return idCat;
	}

	public void setIdCat(int idCat) {
		this.idCat = idCat;
	}

	public int getIdSrv() {
		return idSrv;
	}

	public void setIdSrv(int idSrv) {
		this.idSrv = idSrv;
	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public Personne getPersonne() {
		return personne;
	}

	public void setPersonne(Personne personne) {
		this.personne = personne;
	}

	public boolean isChangeImage() {
		return changeImage;
	}

	public void setChangeImage(boolean changeImage) {
		this.changeImage = changeImage;
	}
    
	
	
	
    

}
