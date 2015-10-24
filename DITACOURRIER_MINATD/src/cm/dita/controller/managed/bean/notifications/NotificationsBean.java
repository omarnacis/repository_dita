package cm.dita.controller.managed.bean.notifications;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
//import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import cm.dita.beans.ApplicationBean;
import cm.dita.controller.managed.bean.user.UserBean;
import cm.dita.entities.Courriers;
import cm.dita.entities.user.User;
import cm.dita.service.domaine.inter.ICourriersRessourceService;
import cm.dita.service.domaine.inter.IEspaceRessourceService;
import cm.dita.service.domaine.inter.user.IUserService;


@ManagedBean(name = "notificationBean")
@SessionScoped
public class NotificationsBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(NotificationsBean.class);
	
	private List<Courriers> listCourriers;
	
	//Spring Espace service is injected..
		@ManagedProperty(value="#{espaceRessourceService}")
		private IEspaceRessourceService espaceRessourceService;

		
		@ManagedProperty(value="#{courriersRessourceService}")
		private ICourriersRessourceService courriersRessourceService;
		
		@ManagedProperty(value="#{userService}")
		private IUserService userService;
		
		@ManagedProperty(value="#{applicationBean}")
	    private ApplicationBean applicationScopeBean;
		
		
		@PostConstruct
		public void courrierEnRetard(){
			UserDetails user_secutity = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		User user =userService.findByLogin(user_secutity.getUsername());
    		Long d=Long.parseLong(applicationScopeBean.DUREE_COURRIER)*24*60*60*1000;
    		//d=(Long)14308555117426;// 1430855517426;
    		//Integer f=new Integer()
    		//JOptionPane.showMessageDialog(null, new Date(Long.parseLong("1431061383709")));
    		//JOptionPane.showMessageDialog(null, new Date(System.currentTimeMillis()));
    		
    		//JOptionPane.showMessageDialog(null,(System.currentTimeMillis()>Long.parseLong("14308555117426"))+"  "+(System.currentTimeMillis()-Long.parseLong("14308555117426")));
    		listCourriers=courriersRessourceService.listCourrierLater(user,d);
			//JOptionPane.showMessageDialog(null, listCourriers.size()+"  "+d+"  "+Long.parseLong(applicationScopeBean.DUREE_COURRIER));
		}
		
		
		
		
		
		
		
		

		public List<Courriers> getListCourriers() {
			return listCourriers;
		}

		public void setListCourriers(List<Courriers> listCourriers) {
			this.listCourriers = listCourriers;
		}

		public IEspaceRessourceService getEspaceRessourceService() {
			return espaceRessourceService;
		}

		public void setEspaceRessourceService(
				IEspaceRessourceService espaceRessourceService) {
			this.espaceRessourceService = espaceRessourceService;
		}

		public ICourriersRessourceService getCourriersRessourceService() {
			return courriersRessourceService;
		}

		public void setCourriersRessourceService(
				ICourriersRessourceService courriersRessourceService) {
			this.courriersRessourceService = courriersRessourceService;
		}

		public IUserService getUserService() {
			return userService;
		}

		public void setUserService(IUserService userService) {
			this.userService = userService;
		}

		public ApplicationBean getApplicationScopeBean() {
			return applicationScopeBean;
		}

		public void setApplicationScopeBean(ApplicationBean applicationScopeBean) {
			this.applicationScopeBean = applicationScopeBean;
		}
		
		
		
		
		
}
