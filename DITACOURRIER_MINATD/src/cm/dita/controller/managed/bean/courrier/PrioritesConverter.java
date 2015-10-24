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

import cm.dita.entities.Priorites;

import cm.dita.service.domaine.inter.IPrioritesRessourceService;


import cm.dita.utils.ApplicationContextHolder;

/**
 * @author Technipedia
 *
 */
@FacesConverter("prioritesConverter")
public class PrioritesConverter  implements Converter {
	
	//pour utiliser le contexte de spring à partir de n'importe où
	protected ApplicationContext springAppContext = null;
	
	//Spring User Service is injected...	
	private IPrioritesRessourceService prioritesRessourceService;


	/**
	 * @return the prioritesRessourceService
	 */
	public IPrioritesRessourceService getPrioritesRessourceService() {
		return prioritesRessourceService;
	}

	/**
	 * @param prioritesRessourceService the prioritesRessourceService to set
	 */
	public void setPrioritesRessourceService(
			IPrioritesRessourceService prioritesRessourceService) {
		this.prioritesRessourceService = prioritesRessourceService;
	}

	@Override
	/**
	 * cette méthode est appelée à la construction de la liste des suggestions pour disposer 
	 * du code user au format texte pour chaque objet User retourné par la méthode 
	 * CourrierControllerBean.completeUsersCorrespondant(String query)
	 */
	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		
		if(value != null && value.trim().length() > 0) {
            try {
            	springAppContext = ApplicationContextHolder.getContext();
            	prioritesRessourceService = (IPrioritesRessourceService)springAppContext.getBean("prioritesRessourceService");
                return prioritesRessourceService.load(Integer.parseInt(value));
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
	 * elle est appelée après validation du formulaire d'ajout de courrier qui est un overlay dans courrier.xhtml (clic du bouton Ajouter). 
	 * Elle permet d'obtenir l'objet User à partir du code au format texte du user sélectionné dans 
	 * le composant et ainsi, d'initialiser la propriété selectedUserCorrespondant du managed bean CourrierControllerBean
	 */
	public String getAsString(FacesContext fc, UIComponent uic, Object object) {
	    if(object != null) {
            return String.valueOf(((Priorites) object).getId());
        }
        else {
            return null;
        }
	}

}
