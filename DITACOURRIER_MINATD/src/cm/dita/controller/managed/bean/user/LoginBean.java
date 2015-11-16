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
import cm.dita.utils.DateManipulation;
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
    
    public void bloqueUser(User user){
    	
    	user.setEnabled(false);//bloquer l'utilisateur ce qui entraine une modification dans son enregistrement
    	user.setDate_desabled(dateCourante);
		userService.update(user);//enregistrer la modification
		
    }
    
 
    public String doLogin() throws IOException, ServletException {
    	
    	    	
        ExternalContext context = FacesContext.getCurrentInstance()
                .getExternalContext();
        
        /**
         * DEVERROUILAGE DU COMPTE VERROUILLE ET DONT LA PENALITE DE VERROUILLAGE EST DEPASSEE
         * Avant de demarrer la connection, si le compte est verrouillé, il a une date_fin_validité et que date_fin_validité  > datecourante
         * Il a une periode de validité non expirée, il s'agit d'un verouillage avec une durée de  verrouillage
         * Si sa durée de verrouillage est depassée, le debloquer 
         */
        
        User u = new User();
        u.setLogin(username.trim());//je mets au moins son nom d'utilisateur pour me rassurer que je peux verifier son existance
        String valeurExactDuLoginSaisi = username.trim();
    	if(userService.userExiste(u)){    //me rassurer que l'utilisateur est bien dans la bd
    		
    			u = userService.findByLogin(username.trim());//on recupère l'utilisateur sachant son login
    		    long dureeVerrouillage = 0;//initialisation de la duréée de verrouillage    		   
    			
    			if(u.isAutorithies()) dureeVerrouillage = 10000;//la durée de verrouilllage de l'administrateur dans le code
    			else
    				dureeVerrouillage = 5000;//la durée de verrouillage des autres comptes venant des préférences.
    			
    			if(   u.isEnabled() == false /*le compte est verrouillé*/
    			   && u.getDate_desabled() != null//l'utilisateur avait été verrouillé en précisant la date de verouillage
    			   && DateManipulation.differenceEnJourEntre2Dates(u.getDate_desabled(),dateCourante) > 0 /*la difference entre la date courante et la date de dernier verouillage est positif*/
    			   && (DateManipulation.differenceEnJourEntre2Dates(u.getDate_desabled(),dateCourante) - dureeVerrouillage >= 0)){/*la durée de verrouillage est depassée*/
    				
    				u.setEnabled(true);//deverrouiller le compte
        			userService.update(u);//faire la mise a jour des modifications en bd
    			}
    			
    	
    	
    	      
       // if(u != null && u.getId() != null)
        	//JOptionPane.showMessageDialog(null,  "DATE COURANTE : "+dateCourante+"  DERNIERE CONNECTION : "+u.getDate_derniere_session()+"            DIFFERENCE ENTRE LES DEUX DATES : " +DateManipulation.differenceEnJourEntre2Dates(u.getDate_derniere_session(),dateCourante));
        
        /**
         * POUR SE RASSURER QUE LA DATE DU SYSTEME N'A PAS ETE MODIFIEE AVANT CONNECTION SINON je provoque une erreur depuis le code EN MODIFIANT VOLONTAIREMENT LE username pour empêcher la connection
         */
        if(   u.getDate_derniere_session() != null /*la date de dernière session existe*/
           && DateManipulation.differenceEnJourEntre2Dates(u.getDate_derniere_session(),dateCourante) < 0){//la datecourante doit être plus grande que la date de dernière session sinon la date du système a été modifié par le dba
        	username += dateCourante;//auquel cas, je modifie volontairement le nom d'utilisateur pour provoquer l'echec de connection et ainsi nous serons automatiquement redirigé vers la page d'echec connection et le message d'erreur sera pris en compte
        		         
	         sessionControlControllerBean.setdSesionCourante_plusResenteQue_dDerniereSesion(false);//je met cette valeur pour controller l'affichage du message dans la page de login
        	
        }
        else
        	/**
        	 * POUR CONTROLLER LES COMPTES LIMITES DANS LE TEMPS AU CAS OU IL EST LIMITE DANS LE TEMPS, je provoque une erreur depuis le code EN MODIFIANT VOLONTAIREMENT LE username pour empêcher la connection
        	 */
        
        	{
        		//un compte est expiré si sa validité est limitée dans le temps et la date courante est > à la date de fin de validité auquel cas il faut bloquer le compte lorsqu'il y'a tentative de connection
        		if(u.getDate_fin_validite() != null && DateManipulation.differenceEnJourEntre2Dates(u.getDate_fin_validite(),dateCourante) > 0){
        			sessionControlControllerBean.setNotAccountExpired(false);//sert a selectionner le message d'erreur qui sera affiché sur la vue
        			username += dateCourante;//je modifie volontairement le nom d'utilisateur pour provoquer l'echec de connection et ainsi nous serons automatiquement redirigé vers la page d'echec connection et le message d'erreur sera pris en compte
        		}
        		
        	}
        	
        	sessionControlControllerBean.setUser(userService.findByLogin(valeurExactDuLoginSaisi.trim()));
    	}
    	
    	
    	/**
    	 * PARTIE DE SOUMISSION Du username et du password à spring security afin qu'il effectue les controlles
    	 */
        RequestDispatcher dispatcher = null;
        dispatcher = ((ServletRequest) context.getRequest()).getRequestDispatcher("/j_spring_security_check?j_username=" + username + "&j_password=" + password);
        dispatcher.forward((ServletRequest) context.getRequest(), (ServletResponse) context.getResponse()); 
        FacesContext.getCurrentInstance().responseComplete();
       // for(FacesMessage f: )
        	Iterator<FacesMessage> f=FacesContext.getCurrentInstance().getMessages();
        	
        	
      /**
       * POUR VERROUILLER LE COMPTE DE L'UTILISATEUR LORSQUE LE NOMBRE DE TENTATIVE DE CONNECTION AVEC ECHEC EST DEPASSE
       */
        	User u1 = new User();
        	u1.setLogin(valeurExactDuLoginSaisi);
        	//JOptionPane.showMessageDialog(null,  "1. JE SUIS LA AVEC previous  = "+sessionControlControllerBean.getPreviousLogin()+"  UTILISATEUR = "+userService.userExiste(u)+" utilisateurID =" +u.getId() );
        	
        	if(userService.userExiste(u1)){ //l'utilisteur existe dans la bd, incrementer le compteur de nombre d'echec ou alors repositionner à 1
        		//JOptionPane.showMessageDialog(null,  "2. *****JE SUIS LA AVEC previous  = "+sessionControlControllerBean.getPreviousLogin() );
		        	if(   sessionControlControllerBean.getPreviousLogin() != null /*le precedent login est non vide*/
		        	   && sessionControlControllerBean.getPreviousLogin().length()>0 /*le precedent login contient au moins un caractère*/
		        	   && sessionControlControllerBean.getPreviousLogin().equalsIgnoreCase(valeurExactDuLoginSaisi.trim())){//le precedent login est egale au login courant
		        		
		        		//JOptionPane.showMessageDialog(null,  "JE SUIS LA AVEC nbconnect  = "+sessionControlControllerBean.getNbLoginSuccessifEchoue() );
		        		sessionControlControllerBean.setNbLoginSuccessifEchoue(sessionControlControllerBean.getNbLoginSuccessifEchoue()+1);//on incremente le compteur d'echec
		        		
		        	}
		        	else
		        		
		        			sessionControlControllerBean.setNbLoginSuccessifEchoue(1);//on suppose qu'il s'agit de la première tentative et que le login est bien dans la bd
		        		        	
        	}
        		else{//l'utilisateur n'existe pas, c'est l'occasion de reinitialiser le nombre de compteur d'echec
        			//JOptionPane.showMessageDialog(null,  "3. *****JE SUIS LA AVEC previous  = "+sessionControlControllerBean.getPreviousLogin() );
        			sessionControlControllerBean.setNbLoginSuccessifEchoue(0);//reinitialiser le compteur du nombre de tentative de connection successif avec echec parceque le login a changé
        		}
        	//JOptionPane.showMessageDialog(null,  "nbconnect  = "+sessionControlControllerBean.getNbLoginSuccessifEchoue()+"    utilisateur : = "+valeurExactDuLoginSaisi+ "  PREVIOUS =  "+sessionControlControllerBean.getPreviousLogin() );
        	sessionControlControllerBean.setPreviousLogin(valeurExactDuLoginSaisi);//on remplace le precedent login par le nouveau
        	
        		        	
        	if(sessionControlControllerBean.getNbLoginSuccessifEchoue() > 3){//le nombre de tentative de connection a excedé 3, nous dvons automatiquement desactiver cet utilisateur et l'inviter à consulter un administrateur
	        	User userTenteConnexion = new User();
	        	userTenteConnexion.setLogin(valeurExactDuLoginSaisi.trim());
	        	if(userService.userExiste(userTenteConnexion)){	        		
	        		
	        		userTenteConnexion = userService.findByLogin(valeurExactDuLoginSaisi);
	        		bloqueUser(userTenteConnexion);
	        		
	        		sessionControlControllerBean.setNbLoginSuccessifEchoue(0);//reinitialiser le compteur du nombre de tentative de connection successif avec echec
	        		mouchardRessourceService.tracage("Déxactivation de "+userTenteConnexion.getInfosPersonne().getNom()+" "+userTenteConnexion.getInfosPersonne().getPrenom()+"("
							+userTenteConnexion.getLogin()+") du système pour cause de tentative multiple de connexion sans succes!", "déxactivation Automatique",userTenteConnexion.getDateUseToSortData(), "User");
	        	}
	        	
        	}
        	
        	
        	if(userService.userExiste(u)){
        		sessionControlControllerBean.setUser(userService.findByLogin(valeurExactDuLoginSaisi.trim()));
        	}
        	
        	
        	//JOptionPane.showMessageDialog(null,  "nbconnect  = "+sessionControlControllerBean.getNbLoginSuccessifEchoue()+"    utilisateur : = "+sessionControlControllerBean.getUser().getLogin()+ "  STATUS =  " +sessionControlControllerBean.getUser().isEnabled());
        	//System.out.println("************************************************** nbsession "+sessionControlControllerBean.getNbLoginSuccessifEchoue());  
        	
        	/**
        	 * 
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
               System.out.println("Adresse IP locale cliente = "+adresseIPLocale );
       
            } catch (UnknownHostException e) {
                   e.printStackTrace();
            }
            
            
            /**
             * Mettre à jour les informations de connection du client une fois connecté et chargé dans le contexte de spring security
             * Ceci ne se fait que pour les utilisateurs connecté
             */
        	
        	try {
        		
	        		if(userService.userExiste(u)){
	        			
	        			UserDetails user_secutity = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        			User userconnected =userService.findByLogin(valeurExactDuLoginSaisi);
	                	//if(userconnected != null)
	        			//JOptionPane.showMessageDialog(null,  "utilisateur connecté"+user_secutity.getUsername());
	        			if(user_secutity != null){    				
	        				userconnected.setIp_derniere_session(adresseIPLocale);//l'adresse ip de dernière session
	        				userconnected.setDate_derniere_session(dateCourante);//la date de dernière session
	        				
	    	        		userService.update(userconnected);//enregistrer la modification
	    	        		sessionControlControllerBean.setPreviousLogin(new String(""));//on reinitialise le conteneur de precedent login
	                		sessionControlControllerBean.setNbLoginSuccessifEchoue(0);
	                		mouchardRessourceService.tracage("Connexion de "+userconnected.getInfosPersonne().getNom()+" "+userconnected.getInfosPersonne().getPrenom()+"("
	    						+userconnected.getLogin()+") au système avec succes!", "Connexion",userconnected.getDateUseToSortData(), "User");
	                		//JOptionPane.showMessageDialog(null,  "utilisateur connecté"+userconnected.getId());
	                		//JOptionPane.showMessageDialog(null,  "nbconnect"+sessionControlControllerBean.getNbLoginSuccessifEchoue());}
	        		}
    			
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
	//useTmp.setDate_derniere_session(dateCourante);//si l'utilisateur se deconnecte normalement alors on remplace sa date et heure de connnection par celle de deconnexion
	//userService.update(useTmp);//enregistrer la modification
	
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
