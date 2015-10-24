package cm.dita.controller.managed.bean.courrier;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;

import cm.dita.entities.Courriers;
import cm.dita.entities.EspaceCourrier;
import cm.dita.service.domaine.inter.ICourriersRessourceService;
import cm.dita.service.domaine.inter.IEspaceCourrierRessourceService;

@ManagedBean(name="suiviESControllerBean")
@ViewScoped
public class SuiviESControllerBean implements Serializable{
	private static final Logger LOG = Logger.getLogger(SuiviESControllerBean.class);
	private static final long serialVersionUID = 1L;
	private List<Courriers> listeSuiviCourrier;
	private String reference;
		
	@ManagedProperty(value="#{courriersRessourceService}")
	private ICourriersRessourceService courriersRessourceService;
		
	public void suivi(String courrier){
		reference=courrier;
		listeSuiviCourrier=courriersRessourceService.getCourriersRelated(courrier);
		/*for(Courriers cour: courrierList)
			if(cour.getRefid()==courrier)
				courrierList.remove(cour);
	listeSuiviCourrier=courrierList;*/
	}

	/**
	 * @return the listeSuiviCourrier
	 */
	public List<Courriers> getListeSuiviCourrier() {
		return listeSuiviCourrier;
	}

	/**
	 * @param listeSuiviCourrier the listeSuiviCourrier to set
	 */
	public void setListeSuiviCourrier(List<Courriers> listeSuiviCourrier) {
		this.listeSuiviCourrier = listeSuiviCourrier;
	}

	/**
	 * @return the courriersRessourceService
	 */
	public ICourriersRessourceService getCourriersRessourceService() {
		return courriersRessourceService;
	}

	/**
	 * @param courriersRessourceService the courriersRessourceService to set
	 */
	public void setCourriersRessourceService(
			ICourriersRessourceService courriersRessourceService) {
		this.courriersRessourceService = courriersRessourceService;
	}

	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}
	
}
