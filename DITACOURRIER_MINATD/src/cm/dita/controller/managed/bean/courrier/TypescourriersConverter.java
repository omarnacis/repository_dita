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

import cm.dita.entities.Typescourriers;
import cm.dita.service.domaine.inter.ITypescourriersRessourceService;
import cm.dita.utils.ApplicationContextHolder;

/**
 * @author Technipedia
 *
 */
@FacesConverter("typescourriersConverter")//le nom de la face à utiliser dans la vue
public class TypescourriersConverter  implements Converter {
	
	//pour utiliser le contexte de spring à partir de n'importe où
	protected ApplicationContext springAppContext = null;
	
	//Spring User Service is injected...	
	private ITypescourriersRessourceService typescourriersRessourceService;

	/**
	 * @return the typescourriersRessourceService
	 */
	public ITypescourriersRessourceService getTypescourriersRessourceService() {
		return typescourriersRessourceService;
	}

	/**
	 * @param typescourriersRessourceService the typescourriersRessourceService to set
	 */
	public void setTypescourriersRessourceService(
			ITypescourriersRessourceService typescourriersRessourceService) {
		this.typescourriersRessourceService = typescourriersRessourceService;
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
            	typescourriersRessourceService = (ITypescourriersRessourceService)springAppContext.getBean("typescourriersRessourceService");
                return typescourriersRessourceService.load(Integer.parseInt(value));
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid typescourriers."));
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
            return String.valueOf(((Typescourriers) object).getId());
        }
        else {
            return null;
        }
	}
}
