package cm.dita.controller.managed.bean.user;

import java.io.Serializable;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;


import org.primefaces.context.RequestContext;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import cm.dita.constant.IConstance;
import cm.dita.service.domaine.inter.IMouchardRessourceService;
import cm.dita.service.domaine.inter.user.IUserService;
import cm.dita.utils.KeyGen;
import cm.dita.utils.Messages;
import cm.dita.utils.VigenereCipher;

@ManagedBean(name="forgetPassBean")
@ViewScoped
public class ForgetPassBean  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value="#{userService}")
	IUserService userService;
	
	@ManagedProperty(value="#{mouchardRessourceService}")
	private IMouchardRessourceService mouchardRessourceService;
	
	private String codeTrans;
	private String valeur;
	private String password;
	private String password2;
	int code;
	private static String CARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
	@PostConstruct
	public void getCodeTRans(){
		VigenereCipher vi=new VigenereCipher();
    	
    	KeyGen kegen=new KeyGen();
    	
    	Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < 16; j++) {
			//sb.append(CARS.charAt(random.nextInt(TAILLE_CLE)));
			sb.append(CARS.charAt(random.nextInt(CARS.length())));
		}
		
		//JOptionPane.showMessageDialog(null, kegen.code(sb.toString()));
    	this.code=kegen.code(sb.toString());
    	System.out.println("CODE == "+vi.encrypt(String.valueOf(this.code)));
    	Long code=System.currentTimeMillis();
    	codeTrans=vi.encrypt(String.valueOf(code)).toUpperCase();
    	
    }
	
	public void valid(){
		VigenereCipher vi=new VigenereCipher();
		FacesContext context = FacesContext.getCurrentInstance();
   	 RequestContext requestContext = RequestContext.getCurrentInstance();
		this.valeur=vi.decrypt(this.valeur);
		try{
			if(Integer.parseInt(this.valeur)==this.code){	
				codeTrans="";
				requestContext.execute("initPassDialog.show()");
			}else{
				FacesMessage message = Messages.getMessage("messages", "global.code.saisi.error", null);
		    	message.setSeverity(FacesMessage.SEVERITY_INFO);
		        context.addMessage(null, message);
			}
		}catch(Exception e){
			FacesMessage message = Messages.getMessage("messages", "global.code.saisi.error", null);
	    	message.setSeverity(FacesMessage.SEVERITY_INFO);
	        context.addMessage(null, message);
		}
	}
	
	public String changePass(){
		FacesContext context = FacesContext.getCurrentInstance();
	   	 RequestContext requestContext = RequestContext.getCurrentInstance();
		if(Integer.parseInt(this.valeur)==this.code){
			Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			
			try{
				
				String cryptedPassword = encoder.encodePassword(this.password, IConstance.MOT_POUR_CRYPTER);
				
				
				userService.updatePasswordAdmin(cryptedPassword);
				this.valeur="";
				this.code=-1;
				this.password="";
				requestContext.execute("initPassDialog.hide()");
				
				 mouchardRessourceService.tracage("Modification du mot de passe administrateur", "modification",null, "User");
					
				
				FacesMessage message = Messages.getMessage("messages", "global.gestion.reussi", null);
		    	message.setSeverity(FacesMessage.SEVERITY_INFO);
		        context.addMessage(null, message);
		       // return "ok";
			}catch(Exception e){
				 mouchardRessourceService.tracage("Echec modification du mot de passe administrateur", "modification",null, "User");
					
				 FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
			    	message.setSeverity(FacesMessage.SEVERITY_WARN);
			        context.addMessage(null, message);
			}
			
		}
		
		return null;
	}


	public String getCodeTrans() {
		return codeTrans;
	}


	public void setCodeTrans(String codeTrans) {
		this.codeTrans = codeTrans;
	}

	public String getValeur() {
		return valeur;
	}

	public void setValeur(String valeur) {
		this.valeur = valeur;
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

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
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
	
	
	
	
	
	

}
