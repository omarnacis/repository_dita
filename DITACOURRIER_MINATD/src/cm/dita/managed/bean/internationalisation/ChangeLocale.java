/**
 * 
 */
package cm.dita.managed.bean.internationalisation;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import cm.dita.entities.user.User;
import cm.dita.service.domaine.inter.user.IUserService;

/**
 * @author bertrand
 *
 */
@ManagedBean
@SessionScoped
public class ChangeLocale  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//locale de la page
	private String locale = "fr";
	private String locale_2 = "en";
	private String lang = "Francais";
	
	@ManagedProperty(value="#{userService}")
	IUserService userService;
	

	public ChangeLocale() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void changeLocale(String local){
		locale_2= locale;
		locale = local;
		Locale locale=new Locale(local);
		FacesContext.getCurrentInstance()
		.getViewRoot().setLocale(locale);
		
		UserDetails user_secutity = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user =userService.findByLogin(user_secutity.getUsername());
		user.setLangue(local);
		userService.update(user);
	}
	
	public String setFrenshLocale(){
		locale = "fr";
		lang = "Francais";
		locale_2 ="en";
		return null;
	}
	
	
	public String setEnglishLocale(){
		locale = "en";
		lang = "English";
		locale_2 ="fr";
		return null;
	}
	
	
	
	
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}



	public String getLocale_2() {
		return locale_2;
	}



	public void setLocale_2(String locale_2) {
		this.locale_2 = locale_2;
	}



	public String getLang() {
		return lang;
	}



	public void setLang(String lang) {
		this.lang = lang;
	}

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	
 
	
	
}
