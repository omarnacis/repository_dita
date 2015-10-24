/**
 * 
 */
package cm.dita.controller.managed.bean.courrier;

import javax.faces.application.FacesMessage;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;



import org.springframework.context.ApplicationContext;



import cm.dita.utils.ApplicationContextHolder;
import cm.dita.entities.user.User;
import cm.dita.service.domaine.inter.user.IUserService;

/**
 * @author Technipedia
 *
 */
@FacesConverter("userConverter")
public class UserConverter implements Converter {
	
	//pour utiliser le contexte de spring � partir de n'importe o�
	protected ApplicationContext springAppContext = null;
	
	//Spring User Service is injected...	
	private IUserService userService;




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

	@Override
	/**
	 * cette m�thode est appel�e � la construction de la liste des suggestions pour disposer 
	 * du code user au format texte pour chaque objet User retourn� par la m�thode 
	 * CourrierControllerBean.completeUsersCorrespondant(String query)
	 */
	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		
		if(value != null && value.trim().length() > 0) {
            try {
            	springAppContext = ApplicationContextHolder.getContext();
        		userService = (IUserService)springAppContext.getBean("userService");
                return userService.load(Integer.parseInt(value));
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid user."));
            }
        }
        else {
            return null;
        }
	}

	@Override
	/**
	 * elle est appel�e apr�s validation du formulaire d'ajout de courrier qui est un overlay dans courrier.xhtml (clic du bouton Ajouter). 
	 * Elle permet d'obtenir l'objet User � partir du code au format texte du user s�lectionn� dans 
	 * le composant et ainsi, d'initialiser la propri�t� selectedUserCorrespondant du managed bean CourrierControllerBean
	 */
	public String getAsString(FacesContext fc, UIComponent uic, Object object) {
	    if(object != null) {
            return String.valueOf(((User) object).getId());
        }
        else {
            return null;
        }
	}

}
