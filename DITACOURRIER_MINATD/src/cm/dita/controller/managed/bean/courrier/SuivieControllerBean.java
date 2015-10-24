package cm.dita.controller.managed.bean.courrier;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;


//import javax.swing.JOptionPane;

import org.apache.log4j.Logger;








import cm.dita.constant.IConstance;
import cm.dita.controller.managed.bean.espacecourrier.EspaceCourrierDataModel;
import cm.dita.entities.Courriers;
import cm.dita.entities.EspaceCourrier;
import cm.dita.service.domaine.inter.ICourriersRessourceService;
import cm.dita.service.domaine.inter.IEspaceCourrierRessourceService;


@ManagedBean(name="suivieControllerBean")
@ViewScoped
public class SuivieControllerBean implements Serializable{
	private static final Logger LOG = Logger.getLogger(SuivieControllerBean.class);
	private static final long serialVersionUID = 1L;
	
	private List<EspaceCourrier> listeSuiviCourrier;
	private List<Courriers> listeCourriers;
	private EspaceCourrierDataModel listEspaceCourrierMeta;
	private EspaceCourrier espacecourriercourant;
	private String idcourrier;
	//Spring User Service is injected...
		@ManagedProperty(value="#{espaceCourrierRessourceService}")
		private IEspaceCourrierRessourceService espaceCourrierRessourceService;

		@ManagedProperty(value="#{courriersRessourceService}")
		private ICourriersRessourceService courriersRessourceService;
		
		@PostConstruct
		public void init(){
			
			listeCourriers=courriersRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
			listeSuiviCourrier=espaceCourrierRessourceService.findByCourrierId(idcourrier);
			setListEspaceCourrierMeta(new EspaceCourrierDataModel(listeSuiviCourrier));
		}
		
			public void suivreCourrier(String idcourrier){
									
			listeSuiviCourrier=espaceCourrierRessourceService.findByCourrierId(idcourrier);
				}

		public void rechercher(){
			Courriers identifiant=courriersRessourceService.load(Integer.parseInt(idcourrier));
			idcourrier=identifiant.getRefid();
			listeSuiviCourrier=espaceCourrierRessourceService.findByCourrierId(idcourrier);
			setListEspaceCourrierMeta(new EspaceCourrierDataModel(listeSuiviCourrier));
		
			}
		public void editObservation(Long id){
			
			espacecourriercourant=espaceCourrierRessourceService.load(id);
		}
		/**
		 * @return the listeSuiviCourrier
		 */
		public List<EspaceCourrier> getListeSuiviCourrier() {
			return listeSuiviCourrier;
		}

		/**
		 * @param listeSuiviCourrier the listeSuiviCourrier to set
		 */
		public void setListeSuiviCourrier(List<EspaceCourrier> listeSuiviCourrier) {
			this.listeSuiviCourrier = listeSuiviCourrier;
		}

		/**
		 * @return the idcourrier
		 */
		public String getIdcourrier() {
			return idcourrier;
		}

		/**
		 * @param idcourrier the idcourrier to set
		 */
		public void setIdcourrier(String idcourrier) {
			this.idcourrier = idcourrier;
		}

		/**
		 * @return the espaceCourrierRessourceService
		 */
		public IEspaceCourrierRessourceService getEspaceCourrierRessourceService() {
			return espaceCourrierRessourceService;
		}

		/**
		 * @param espaceCourrierRessourceService the espaceCourrierRessourceService to set
		 */
		public void setEspaceCourrierRessourceService(
				IEspaceCourrierRessourceService espaceCourrierRessourceService) {
			this.espaceCourrierRessourceService = espaceCourrierRessourceService;
		}

		/**
		 * @return the listEspaceCourrierMeta
		 */
		public EspaceCourrierDataModel getListEspaceCourrierMeta() {
			return listEspaceCourrierMeta;
		}

		/**
		 * @param listEspaceCourrierMeta the listEspaceCourrierMeta to set
		 */
		public void setListEspaceCourrierMeta(
				EspaceCourrierDataModel listEspaceCourrierMeta) {
			this.listEspaceCourrierMeta = listEspaceCourrierMeta;
		}

		/**
		 * @return the listeCourriers
		 */
		public List<Courriers> getListeCourriers() {
			return listeCourriers;
		}

		/**
		 * @param listeCourriers the listeCourriers to set
		 */
		public void setListeCourriers(List<Courriers> listeCourriers) {
			this.listeCourriers = listeCourriers;
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
		 * @return the espacecourriercourant
		 */
		public EspaceCourrier getEspacecourriercourant() {
			return espacecourriercourant;
		}

		/**
		 * @param espacecourriercourant the espacecourriercourant to set
		 */
		public void setEspacecourriercourant(EspaceCourrier espacecourriercourant) {
			this.espacecourriercourant = espacecourriercourant;
		}
		

}
