package cm.dita.controller.managed.bean.user;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
//import javax.swing.JOptionPane;

import javax.swing.JOptionPane;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import cm.dita.constant.IConstance;
import cm.dita.constant.ISessionConstant;
import cm.dita.entities.Espace;
import cm.dita.entities.user.User;
import cm.dita.service.domaine.inter.IEspaceRessourceService;
import cm.dita.service.domaine.inter.IMouchardRessourceService;
import cm.dita.service.domaine.inter.user.IUserService;
import cm.dita.utils.VigenereCipher;

@ManagedBean
@SessionScoped
//@RequestScoped
public class LoginBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	
	private String username;
    private String password;
    
    
    
    
	private UserDetails user_secutity;
    private User userconnected;
	private Espace espaceCourrant = new Espace();
    private boolean afficheStatDashbord = false; //on affiche les statistiques si l'utilisateur connect� � le ROLE_ADMIN
    private String libelleRoleAdmin = "ROLE_ADMIN";
    
  
    
	//Spring Espace Service is injected...
	@ManagedProperty(value="#{espaceRessourceService}")
	IEspaceRessourceService espaceRessourceService;
    
    @ManagedProperty(value="#{userService}")
	IUserService userService;
   
    @ManagedProperty(value="#{mouchardRessourceService}")
	private IMouchardRessourceService mouchardRessourceService;
	 
	
	private User user;
	
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
        	/*while(f.hasNext())
        JOptionPane.showMessageDialog(null,  f.next().getDetail());
        	
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
    		mouchardRessourceService.tracage("D�connexion de "+useTmp.getInfosPersonne().getPersnom()+" "+useTmp.getInfosPersonne().getPersprenom()+"("
					+useTmp.getLogin()+") du syst�me", "d�connexion",useTmp.getDateUseToSortData(), "User");
    	
    	 ExternalContext context = FacesContext.getCurrentInstance()
                 .getExternalContext();
   
         RequestDispatcher dispatcher = ((ServletRequest) context.getRequest())
                 .getRequestDispatcher("/j_spring_security_logout");
  
         dispatcher.forward((ServletRequest) context.getRequest(),
                 (ServletResponse) context.getResponse());
  
         FacesContext.getCurrentInstance().responseComplete();
         
         
    	}catch(Exception e){
    		
    	/*	mouchardRessourceService.tracage("Echec déconnexion de "+useTmp.getInfosPersonne().getNom()+" "+useTmp.getInfosPersonne().getPrenom()+"("
					+useTmp.getLogin()+") dans le système", "déconnexion",useTmp.getDateUseToSortData(), "User");*/
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
	 * @return the user_secutity
	 */
	public UserDetails getUser_secutity() {
		return user_secutity;
	}

	/**
	 * @param user_secutity the user_secutity to set
	 */
	public void setUser_secutity(UserDetails user_secutity) {
		this.user_secutity = user_secutity;
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
	 * @return the espaceCourrant
	 */
	public Espace getEspaceCourrant() {
		return espaceCourrant;
	}

	/**
	 * @param espaceCourrant the espaceCourrant to set
	 */
	public void setEspaceCourrant(Espace espaceCourrant) {
		this.espaceCourrant = espaceCourrant;
	}

	/**
	 * @return the afficheStatDashbord
	 */
	public boolean isAfficheStatDashbord() {
		return afficheStatDashbord;
	}

	/**
	 * @param afficheStatDashbord the afficheStatDashbord to set
	 */
	public void setAfficheStatDashbord(boolean afficheStatDashbord) {
		this.afficheStatDashbord = afficheStatDashbord;
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
	 * @return the libelleRoleAdmin
	 */
	public String getLibelleRoleAdmin() {
		return libelleRoleAdmin;
	}

	/**
	 * @param libelleRoleAdmin the libelleRoleAdmin to set
	 */
	public void setLibelleRoleAdmin(String libelleRoleAdmin) {
		this.libelleRoleAdmin = libelleRoleAdmin;
	}


}
