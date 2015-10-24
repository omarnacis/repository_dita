package cm.dita.controller.managed.bean.statistiques;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.PieChartModel;

import cm.dita.constant.IConstance;
import cm.dita.controller.managed.bean.espacecourrier.EspaceCourrierControllerBean;
import cm.dita.entities.Courriers;
import cm.dita.entities.Espace;
import cm.dita.entities.EspaceCourrier;
import cm.dita.service.domaine.inter.ICourriersRessourceService;
import cm.dita.service.domaine.inter.IEspaceCourrierRessourceService;
import cm.dita.service.domaine.inter.IEspaceRessourceService;

@ManagedBean(name="statistiqueControllerBean")
@ViewScoped
public class StatistiqueControllerBean implements Serializable {
	/**
	 * 
	 */
	private static final Logger LOG = Logger.getLogger(StatistiqueControllerBean.class);
	private static final long serialVersionUID = 1L;
	
	private List<Courriers> listCourriersTotal;
	private int nbCourrierTE=0;
	private int nbCourrierTS=0;
	private int nbCourrierTI=0;
	private PieChartModel model;
	
	private int nbCourrierTER=0;
	private int nbCourrierENC=0;
	private int nbCourrierTRA=0;
	private int nbCourrierREG=0;
	
	private int nbCourrierTERE=0;
	private int nbCourrierENCE=0;
	private int nbCourrierTRAE=0;
	private int nbCourrierREGE=0;
	private PieChartModel model2;
	private PieChartModel modelE;
	private String idEspaceCourant;
	private List<EspaceCourrier> listecourriers;
	
	
	private List <Espace> listeEspace = new ArrayList<Espace>();
	
	@ManagedProperty(value="#{courriersRessourceService}")
	private ICourriersRessourceService courriersRessourceService;
	
	//Spring User Service is injected...
	@ManagedProperty(value="#{espaceCourrierRessourceService}")
	private IEspaceCourrierRessourceService espaceCourrierRessourceService;
	
	@ManagedProperty(value="#{espaceRessourceService}")
	private IEspaceRessourceService espaceRessourceService;

	
	@PostConstruct
	public void init(){
		listCourriersTotal=courriersRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
		stattotalEIS(listCourriersTotal);
		statTotalTraitement();
		constructChart();
		constructChartTraitement();
		constructChartTraitementE();
		
		listeEspace=espaceRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
	
	}
	public String showStatistique(){
		listCourriersTotal=courriersRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
		stattotalEIS(listCourriersTotal);
		statTotalTraitement();
		constructChart();
		constructChartTraitement();
		constructChartTraitementE();
		
		listeEspace=espaceRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
		return "bon";
	}
	
	public void constructChart(){
		model= new PieChartModel();
		model.set("Courriers Entrant", nbCourrierTE);
		model.set("Courriers Interne", nbCourrierTI);
		model.set("Courriers Sortant", nbCourrierTS);
		
		}
	
	public void stattotalEIS(List<Courriers> courriers){
		for(int i = 0; i < courriers.size(); i++)
	 	{
			if(courriers.get(i).getTypecourrier().getId()==1){
				nbCourrierTE++;
				
			}else if (courriers.get(i).getTypecourrier().getId()==2){
				nbCourrierTI++;
			}else if(courriers.get(i).getTypecourrier().getId()==3){
				nbCourrierTS++;
			}
	 	}
		
	}
	
	public void constructChartTraitement()
	{
		model2= new PieChartModel();
		model2.set("Courriers Termin�s", nbCourrierTER);
		model2.set("Courriers En cours", nbCourrierENC);
		model2.set("Courriers Rejet�s", nbCourrierREG);
		
		
		
	}
	public void constructChartTraitementE()
	{
		modelE= new PieChartModel();
		modelE.set("Courriers Termin�s", nbCourrierTERE);
		modelE.set("Courriers En cours", nbCourrierENCE);
		modelE.set("Courriers Trait�s", nbCourrierTRAE);
		modelE.set("Courriers Rejet�s", nbCourrierREGE);
		
				
	}
	
	public void statTotalTraitement(){
		listCourriersTotal=courriersRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
		for(int i = 0; i < listCourriersTotal.size(); i++)
	 	{
			if(listCourriersTotal.get(i).getIdstatutcourantducour()==1){
				nbCourrierTER++;
				
			}else if (listCourriersTotal.get(i).getIdstatutcourantducour()==2){
				nbCourrierENC++;
			}else if(listCourriersTotal.get(i).getIdstatutcourantducour()==3){
				nbCourrierTRA++;
			}else if(listCourriersTotal.get(i).getIdstatutcourantducour()==4){
				nbCourrierREG++;
			}
	 	}
		
	}
	
	public void ChangeListenerChart(){
		listecourriers=espaceCourrierRessourceService.listCourrierParEspace(Integer.parseInt(idEspaceCourant));
		nbCourrierTERE=0;
		nbCourrierENCE=0;
		nbCourrierTRAE=0;
		nbCourrierREGE=0;
		
		for(int i = 0; i < listecourriers.size(); i++)
	 	{
			if(listecourriers.get(i).getStatut().getId()==1){
				nbCourrierTERE++;
			 }else if (listecourriers.get(i).getStatut().getId()==2){
				nbCourrierENCE++;
			}else if(listecourriers.get(i).getStatut().getId()==3){
				nbCourrierTRAE++;
			}else{
				nbCourrierREGE++;
			}
			
	 	}
		
		constructChartTraitementE();
		
	}
	
	public void itemSelect(ItemSelectEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item selected",
               "Item Index: " + event.getItemIndex() + ", Series Index:" + event.getSeriesIndex());
         
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
	/**
	 * @return the listCourriersTotal
	 */
	public List<Courriers> getListCourriersTotal() {
		return listCourriersTotal;
	}

	/**
	 * @param listCourriersTotal the listCourriersTotal to set
	 */
	public void setListCourriersTotal(List<Courriers> listCourriersTotal) {
		this.listCourriersTotal = listCourriersTotal;
	}

	/**
	 * @return the nbCourrierTE
	 */
	public int getNbCourrierTE() {
		return nbCourrierTE;
	}

	/**
	 * @param nbCourrierTE the nbCourrierTE to set
	 */
	public void setNbCourrierTE(int nbCourrierTE) {
		this.nbCourrierTE = nbCourrierTE;
	}

	/**
	 * @return the nbCourrierTS
	 */
	public int getNbCourrierTS() {
		return nbCourrierTS;
	}

	/**
	 * @param nbCourrierTS the nbCourrierTS to set
	 */
	public void setNbCourrierTS(int nbCourrierTS) {
		this.nbCourrierTS = nbCourrierTS;
	}

	/**
	 * @return the nbCourrierTI
	 */
	public int getNbCourrierTI() {
		return nbCourrierTI;
	}

	/**
	 * @param nbCourrierTI the nbCourrierTI to set
	 */
	public void setNbCourrierTI(int nbCourrierTI) {
		this.nbCourrierTI = nbCourrierTI;
	}

	/**
	 * @return the model
	 */
	public PieChartModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(PieChartModel model) {
		this.model = model;
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
	 * @return the nbCourrierTER
	 */
	public int getNbCourrierTER() {
		return nbCourrierTER;
	}
	/**
	 * @param nbCourrierTER the nbCourrierTER to set
	 */
	public void setNbCourrierTER(int nbCourrierTER) {
		this.nbCourrierTER = nbCourrierTER;
	}
	/**
	 * @return the nbCourrierENC
	 */
	public int getNbCourrierENC() {
		return nbCourrierENC;
	}
	/**
	 * @param nbCourrierENC the nbCourrierENC to set
	 */
	public void setNbCourrierENC(int nbCourrierENC) {
		this.nbCourrierENC = nbCourrierENC;
	}
	/**
	 * @return the nbCourrierTRA
	 */
	public int getNbCourrierTRA() {
		return nbCourrierTRA;
	}
	/**
	 * @param nbCourrierTRA the nbCourrierTRA to set
	 */
	public void setNbCourrierTRA(int nbCourrierTRA) {
		this.nbCourrierTRA = nbCourrierTRA;
	}
	/**
	 * @return the nbCourrierREG
	 */
	public int getNbCourrierREG() {
		return nbCourrierREG;
	}
	/**
	 * @param nbCourrierREG the nbCourrierREG to set
	 */
	public void setNbCourrierREG(int nbCourrierREG) {
		this.nbCourrierREG = nbCourrierREG;
	}
	/**
	 * @return the model2
	 */
	public PieChartModel getModel2() {
		return model2;
	}
	/**
	 * @param model2 the model2 to set
	 */
	public void setModel2(PieChartModel model2) {
		this.model2 = model2;
	}
	/**
	 * @return the nbCourrierTERE
	 */
	public int getNbCourrierTERE() {
		return nbCourrierTERE;
	}
	/**
	 * @param nbCourrierTERE the nbCourrierTERE to set
	 */
	public void setNbCourrierTERE(int nbCourrierTERE) {
		this.nbCourrierTERE = nbCourrierTERE;
	}
	/**
	 * @return the nbCourrierENCE
	 */
	public int getNbCourrierENCE() {
		return nbCourrierENCE;
	}
	/**
	 * @param nbCourrierENCE the nbCourrierENCE to set
	 */
	public void setNbCourrierENCE(int nbCourrierENCE) {
		this.nbCourrierENCE = nbCourrierENCE;
	}
	/**
	 * @return the nbCourrierTRAE
	 */
	public int getNbCourrierTRAE() {
		return nbCourrierTRAE;
	}
	/**
	 * @param nbCourrierTRAE the nbCourrierTRAE to set
	 */
	public void setNbCourrierTRAE(int nbCourrierTRAE) {
		this.nbCourrierTRAE = nbCourrierTRAE;
	}
	/**
	 * @return the nbCourrierREGE
	 */
	public int getNbCourrierREGE() {
		return nbCourrierREGE;
	}
	/**
	 * @param nbCourrierREGE the nbCourrierREGE to set
	 */
	public void setNbCourrierREGE(int nbCourrierREGE) {
		this.nbCourrierREGE = nbCourrierREGE;
	}
	/**
	 * @return the modelE
	 */
	public PieChartModel getModelE() {
		return modelE;
	}
	/**
	 * @param modelE the modelE to set
	 */
	public void setModelE(PieChartModel modelE) {
		this.modelE = modelE;
	}
	/**
	 * @return the idEspaceCourant
	 */
	public String getIdEspaceCourant() {
		return idEspaceCourant;
	}
	/**
	 * @param idEspaceCourant the idEspaceCourant to set
	 */
	public void setIdEspaceCourant(String idEspaceCourant) {
		this.idEspaceCourant = idEspaceCourant;
	}
	/**
	 * @return the listecourriers
	 */
	public List<EspaceCourrier> getListecourriers() {
		return listecourriers;
	}
	/**
	 * @param listecourriers the listecourriers to set
	 */
	public void setListecourriers(List<EspaceCourrier> listecourriers) {
		this.listecourriers = listecourriers;
	}
	/**
	 * @return the listeEspace
	 */
	public List<Espace> getListeEspace() {
		return listeEspace;
	}
	/**
	 * @param listeEspace the listeEspace to set
	 */
	public void setListeEspace(List<Espace> listeEspace) {
		this.listeEspace = listeEspace;
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
	
}
