package cm.dita.controller.managed.bean.user;

import java.io.Serializable;


import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
//import javax.swing.JOptionPane;



import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import cm.dita.constant.IConstance;
import cm.dita.constant.ISessionConstant;
import cm.dita.controller.managed.bean.sessionControl.SessionControlControllerBean;
import cm.dita.entities.user.User;
import cm.dita.service.domaine.inter.IMouchardRessourceService;
import cm.dita.service.domaine.inter.user.IUserService;

import cm.dita.utils.VigenereCipher;

@ManagedBean
@RequestScoped
public class LoginBean implements Serializable{

	//pour utiliser le contexte de spring ï¿½ partir de n'importe oï¿½
	protected ApplicationContext springAppContext = null;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String username;
    private String password;
    
    //private Date dateCourante = new Date();
    private String dateCourante =  (new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2)).format(new Date()) ; 
    
    @ManagedProperty(value="#{userService}")
	IUserService userService;
    
    @ManagedProperty(value="#{sessionControlControllerBean}")
	private SessionControlControllerBean sessionControlControllerBean;
    
    @ManagedProperty(value="#{mouchardRessourceService}")
	private IMouchardRessourceService mouchardRessourceService;
    
//    private SessionControlControllerBean sessionControlControllerBean;
	 
	
	private User user = new User();
	
	private String codeTrans;
 
    public LoginBean() {
    }
    
 
    public String doLogin() throws IOException, ServletException {
    	
    	    	
        ExternalContext context = FacesContext.getCurrentInstance()
                .getExternalContext();
  
        RequestDispatcher dispatcher = ((ServletRequest) context.getRequest())
                .getRequestDispatcher("/j_spring_security_check?j_username=" + username
                                + "&j_password=" + password);
 
        dispatcher.forward((ServletRequest) context.getRequest(),
                (ServletResponse) context.getResponse());
 
        FacesContext.getCurrentInstance().responseComplete();
       // for(FacesMessage f: )
        	Iterator<FacesMessage> f=FacesContext.getCurrentInstance().getMessages();
        	
       //Accéder à la javabean placé dans le contexte de spring
        	
        	//springAppContext = ApplicationContextHolder.getContext();
        	//sessionControlControllerBean = (SessionControlControllerBean)springAppContext.getBean("sessionControlControllerBean");
        	//pour incrementer le nombre de tentative de connection successif avec echec
        	sessionControlControllerBean.setNbLoginSuccessifEchoue(sessionControlControllerBean.getNbLoginSuccessifEchoue()+1);
        		
        	if(sessionControlControllerBean.getNbLoginSuccessifEchoue() > 3){//le nombre de tentative de connection a excedé 2, nous dvons automatiquement desactiver cet utilisateur et l'inviter à consulter un administrateur
	        	User userTenteConnexion = new User();
	        	userTenteConnexion.setLogin(username.trim());
	        	if(userService.userExiste(userTenteConnexion)){
	        		userTenteConnexion = userService.findByLogin(username.trim());
	        		userTenteConnexion.setEnabled(false);//bloquer l'utilisateur ce qui entraine une modification dans son enregistrement
	        		userTenteConnexion.setDate_enable(dateCourante);
	        		//user = userService.update(userTenteConnexion);//enregistrer la modification
	        		sessionControlControllerBean.setNbLoginSuccessifEchoue(0);//reinitialiser le compteur du nombre de tentative de connection successif avec echec
	        		mouchardRessourceService.tracage("Déxactivation de "+userTenteConnexion.getInfosPersonne().getNom()+" "+userTenteConnexion.getInfosPersonne().getPrenom()+"("
							+userTenteConnexion.getLogin()+") du système pour cause de tentative multiple de connexion sans succes!", "déxactivation Automatique",userTenteConnexion.getDateUseToSortData(), "User");
	        	}
	        	
        	}
        	
        	/**
        	 * POUR POUVOIR RECUPERER LE enabled de l'utilisateur 
        	 */
        	user.setLogin(username);
        	if(userService.userExiste(user))
        		sessionControlControllerBean.setUser(userService.findByLogin(username.trim()));
        	
        	//JOptionPane.showMessageDialog(null,  "nbconnect  = "+sessionControlControllerBean.getNbLoginSuccessifEchoue()+"    utilisateur : = "+sessionControlControllerBean.getUser().getLogin()+ "  STATUS =  " +sessionControlControllerBean.getUser().isEnabled());
        	//System.out.println("************************************************** nbsession "+sessionControlControllerBean.getNbLoginSuccessifEchoue());  
        	
        	/*
        	 * Recupérer le nom et l'adresse ip reseau de la machine cliente 
        	 */
            String nomHote ;
            String adresseIPLocale = null ;

            try{
               InetAddress inetadr = InetAddress.getLocalHost();
               //nom de machine
               nomHote = (String) inetadr.getHostName();
               System.out.println("Nom de la machine = "+nomHote );
               //adresse ip sur le réseau
               adresseIPLocale = (String) inetadr.getHostAddress();
               System.out.println("Adresse IP locale = "+adresseIPLocale );
       
            } catch (UnknownHostException e) {
                   e.printStackTrace();
            }
            
            
            /**
             * Mettre à jour les informations de connection du client
             */
        	
        	try {
        		UserDetails user_secutity = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    			User userconnected =userService.findByLogin(username.trim());
            	//if(userconnected != null)
    			//JOptionPane.showMessageDialog(null,  "utilisateur connecté"+user_secutity.getUsername());
    			if(user_secutity != null){    				
    				userconnected.setIp_last_connection(adresseIPLocale);
	        		userService.update(userconnected);//enregistrer la modification
            		sessionControlControllerBean.setNbLoginSuccessifEchoue(0);
            		mouchardRessourceService.tracage("Connexion de "+userconnected.getInfosPersonne().getNom()+" "+userconnected.getInfosPersonne().getPrenom()+"("
						+userconnected.getLogin()+") au système avec succes!", "Connexion",userconnected.getDateUseToSortData(), "User");
            		//JOptionPane.showMessageDialog(null,  "utilisateur connecté"+userconnected.getId());
            		//JOptionPane.showMessageDialog(null,  "nbconnect"+sessionControlControllerBean.getNbLoginSuccessifEchoue());}
    			}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
        	/*
        	while(f.hasNext())
        JOptionPane.showMessageDialog(null,  "==========="+f.next().getDetail());
        		
        	FacesMessage message = Messages.getMessage("messages", "dsfdsgds", null);
    		message.setSeverity(FacesMessage.SEVERITY_ERROR);
    		FacesContext.getCurrentInstance().addMessage(null, message);*/
        	
        // It's OK to return null here because Faces is just going to exit.
      
        return null;
    }
    
    
    
    public void logout() {
    	HttpServletRequest req=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	HttpSession httpSession = req.getSession();
    	httpSession.getAttribute(ISessionConstant.SS_USER);
	User useTmp=(User) httpSession.getAttribute(ISessionConstant.SS_USER);
	
    	try{
    		mouchardRessourceService.tracage("Déconnexion de "+useTmp.getInfosPersonne().getNom()+" "+useTmp.getInfosPersonne().getPrenom()+"("
					+useTmp.getLogin()+") du système", "déconnexion",useTmp.getDateUseToSortData(), "User");
    	
    	 ExternalContext context = FacesContext.getCurrentInstance()
                 .getExternalContext();
   
         RequestDispatcher dispatcher = ((ServletRequest) context.getRequest())
                 .getRequestDispatcher("/j_spring_security_logout");
  
         dispatcher.forward((ServletRequest) context.getRequest(),
                 (ServletResponse) context.getResponse());
  
         FacesContext.getCurrentInstance().responseComplete();
         
         
    	}catch(Exception e){
    		
    	/*	mouchardRessourceService.tracage("Echec dÃ©connexion de "+useTmp.getInfosPersonne().getNom()+" "+useTmp.getInfosPersonne().getPrenom()+"("
					+useTmp.getLogin()+") dans le systÃ¨me", "dÃ©connexion",useTmp.getDateUseToSortData(), "User");*/
    	}finally{
    		useTmp=null;
    	}
		
		/*try {
			
			((HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse()).sendRedirect(req.getContextPath()+"/j_spring_security_logout");
			
		} catch (IOException e) {}*/
	 }
    
    
    public void codeTRans(){
    	VigenereCipher vi=new VigenereCipher();
    	Long code=System.currentTimeMillis();
    	codeTrans=vi.encrypt(String.valueOf(code));
    }
 
    public String getPassword() {
        return password;
    }
 
    public String getUsername() {
        return username;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
 
    public void setUsername(String username) {
        this.username = username;
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

	public String getCodeTrans() {
		return codeTrans;
	}

	public void setCodeTrans(String codeTrans) {
		this.codeTrans = codeTrans;
	}

	public IMouchardRessourceService getMouchardRessourceService() {
		return mouchardRessourceService;
	}

	public void setMouchardRessourceService(
			IMouchardRessourceService mouchardRessourceService) {
		this.mouchardRessourceService = mouchardRessourceService;
	}

	/**
	 * @return the springAppContext
	 */
	public ApplicationContext getSpringAppContext() {
		return springAppContext;
	}

	/**
	 * @param springAppContext the springAppContext to set
	 */
	public void setSpringAppContext(ApplicationContext springAppContext) {
		this.springAppContext = springAppContext;
	}

	/**
	 * @return the sessionControlControllerBean
	 */
	public SessionControlControllerBean getSessionControlControllerBean() {
		return sessionControlControllerBean;
	}

	/**
	 * @param sessionControlControllerBean the sessionControlControllerBean to set
	 */
	public void setSessionControlControllerBean(
			SessionControlControllerBean sessionControlControllerBean) {
		this.sessionControlControllerBean = sessionControlControllerBean;
	}

	/**
	 * @return the dateCourante
	 */
	public String getDateCourante() {
		return dateCourante;
	}

	/**
	 * @param dateCourante the dateCourante to set
	 */
	public void setDateCourante(String dateCourante) {
		this.dateCourante = dateCourante;
	}

	
	
	

}
