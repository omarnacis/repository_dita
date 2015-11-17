package cm.dita.beans;

import java.awt.HeadlessException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.swing.JOptionPane;

import cm.dita.constant.IConstance;
import cm.dita.entities.Preferences;
import cm.dita.service.domaine.inter.IPreferencesRessourceService;

@ManagedBean(eager = true,name = "applicationBean")
@ApplicationScoped
public class ApplicationBean implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ManagedProperty(value="#{preferencesRessourceService}")
	private IPreferencesRessourceService preferencesRessourceService;
	
	
	public String PARAMETER_DATE_FORMAT_2;
	public String NBRE_LIGNE_DATATABLE_DEFAULT;
	public String PAGINATION_TABLE;
	public String LOGO;
	public String IMG_FOND;
	public String TELEPHONE;
	public String EMAIL;
	public String ADRESSE;
	public String DOSSIER_JOINTE;
	public String DOSSIER_STYLE;
	public String DUREE_COURRIER;
	public String COMPLEXE_PASSWORD;
	
	public String NOM_ENTREPRISE;
	public String SERVEUR_SMTP;
	public String PORT_SERVEUR_SMTP;
	public String ADRESSE_COMPTE_ENVOI_MAIL;
	public String PWD_COMPTE_ENVOI_MAIL;
	
	public String OBJET_MAIL;
	public String CORPS_MAIL;
	
	private boolean PRODUCT_OK;
	public int PRODUCT_OK_2;
	
	
	boolean is_text_logo;
	
	private List<Preferences> preferenceList = new ArrayList<Preferences>();
	
	public ApplicationBean(){
		
		
	}
	
	@PostConstruct
	public void init(){
		/*
		 //on garde dans le registre
		java.util.prefs.Preferences lPrf = java.util.prefs.Preferences.userRoot().node( "reg_cour/key" );		
		PRODUCT_OK=lPrf.getBoolean("My_Key",false);
		if(PRODUCT_OK)
			PRODUCT_OK_2=1;
		else
			PRODUCT_OK_2=2;*/
		PARAMETER_DATE_FORMAT_2=preferencesRessourceService.getParameterByName("PARAMETER_DATE_FORMAT_2").getPreferenceValue();
		LOGO=preferencesRessourceService.getParameterByName("LOGO").getPreferenceValue();
		IMG_FOND=preferencesRessourceService.getParameterByName("IMG_FOND").getPreferenceValue();		
		TELEPHONE=preferencesRessourceService.getParameterByName("TELEPHONE").getPreferenceValue();
		ADRESSE=preferencesRessourceService.getParameterByName("ADRESSE").getPreferenceValue();
		DUREE_COURRIER=preferencesRessourceService.getParameterByName("DUREE_COURRIER").getPreferenceValue();
		EMAIL=preferencesRessourceService.getParameterByName("EMAIL").getPreferenceValue();
		COMPLEXE_PASSWORD=preferencesRessourceService.getParameterByName("COMPLEXE_PASSWORD").getPreferenceValue();
		
		NOM_ENTREPRISE=preferencesRessourceService.getParameterByName("NOM_ENTREPRISE").getPreferenceValue();
		SERVEUR_SMTP=preferencesRessourceService.getParameterByName("SERVEUR_SMTP").getPreferenceValue();
		PORT_SERVEUR_SMTP=preferencesRessourceService.getParameterByName("PORT_SERVEUR_SMTP").getPreferenceValue();
		ADRESSE_COMPTE_ENVOI_MAIL=preferencesRessourceService.getParameterByName("ADRESSE_COMPTE_ENVOI_MAIL").getPreferenceValue();
		PWD_COMPTE_ENVOI_MAIL=preferencesRessourceService.getParameterByName("PWD_COMPTE_ENVOI_MAIL").getPreferenceValue();
		
		OBJET_MAIL=preferencesRessourceService.getParameterByName("OBJET_MAIL").getPreferenceValue();
		CORPS_MAIL=preferencesRessourceService.getParameterByName("CORPS_MAIL").getPreferenceValue();
		
		
	}
	
	public void valid(boolean value){		
		PRODUCT_OK=value;	
		//JOptionPane.showMessageDialog(null, "val = "+PRODUCT_OK);
	}

	public void configuration(String parameter,String value){
		
		switch (parameter) {
		case "PARAMETER_DATE_FORMAT_2":
			PARAMETER_DATE_FORMAT_2=value;
			break;
		case "NBRE_LIGNE_DATATABLE_DEFAULT":
			NBRE_LIGNE_DATATABLE_DEFAULT=value;
			break;
		case "PAGINATION_TABLE":
			PAGINATION_TABLE=value;
			break;
			
		case "LOGO":
			LOGO=value;
			break;
			
		case "IMG_FOND":
			IMG_FOND=value;
			break;
		
		case "TELEPHONE":
			TELEPHONE=value;
			break;
		
		case "EMAIL":
			EMAIL=value;
			break;
			
		case "ADRESSE":
			ADRESSE=value;
			break;
		
		case "DUREE_COURRIER":
			DUREE_COURRIER=value;
			break;
			
		case "COMPLEXE_PASSWORD":
			COMPLEXE_PASSWORD=value;
			break;
		
		case "NOM_ENTREPRISE":
			NOM_ENTREPRISE=value;
			break;
			
		case "SERVEUR_SMTP":
			SERVEUR_SMTP=value;
			break;
		
		case "PORT_SERVEUR_SMTP":
			PORT_SERVEUR_SMTP=value;
			break;
			
		case "ADRESSE_COMPTE_ENVOI_MAIL":
			ADRESSE_COMPTE_ENVOI_MAIL=value;
			break;
		
		case "PWD_COMPTE_ENVOI_MAIL":
			PWD_COMPTE_ENVOI_MAIL=value;
			break;
			
		case "OBJET_MAIL":
			OBJET_MAIL=value;
			break;
			
		case "CORPS_MAIL":
			CORPS_MAIL=value;
			break;
		
		default:
			break;
		}
		
		
	}

	public IPreferencesRessourceService getPreferencesRessourceService() {
		return preferencesRessourceService;
	}



	public void setPreferencesRessourceService(
			IPreferencesRessourceService preferencesRessourceService) {
		this.preferencesRessourceService = preferencesRessourceService;
	}

	public List<Preferences> getPreferenceList() {
		return preferenceList;
	}

	public void setPreferenceList(List<Preferences> preferenceList) {
		this.preferenceList = preferenceList;
	}

	public String getPARAMETER_DATE_FORMAT_2() {
		return PARAMETER_DATE_FORMAT_2;
	}

	public void setPARAMETER_DATE_FORMAT_2(String pARAMETER_DATE_FORMAT_2) {
		PARAMETER_DATE_FORMAT_2 = pARAMETER_DATE_FORMAT_2;
	}

	public String getNBRE_LIGNE_DATATABLE_DEFAULT() {
		return NBRE_LIGNE_DATATABLE_DEFAULT;
	}

	public void setNBRE_LIGNE_DATATABLE_DEFAULT(String nBRE_LIGNE_DATATABLE_DEFAULT) {
		NBRE_LIGNE_DATATABLE_DEFAULT = nBRE_LIGNE_DATATABLE_DEFAULT;
	}

	public String getPAGINATION_TABLE() {
		return PAGINATION_TABLE;
	}

	public void setPAGINATION_TABLE(String pAGINATION_TABLE) {
		PAGINATION_TABLE = pAGINATION_TABLE;
	}

	public String getLOGO() {
		return LOGO;
	}

	public void setLOGO(String lOGO) {
		LOGO = lOGO;
	}

	public String getTELEPHONE() {
		return TELEPHONE;
	}

	public void setTELEPHONE(String tELEPHONE) {
		TELEPHONE = tELEPHONE;
	}

	public String getEMAIL() {
		return EMAIL;
	}

	public void setEMAIL(String eMAIL) {
		EMAIL = eMAIL;
	}

	public String getADRESSE() {
		return ADRESSE;
	}

	public void setADRESSE(String aDRESSE) {
		ADRESSE = aDRESSE;
	}

	public String getDOSSIER_JOINTE() {
		return DOSSIER_JOINTE;
	}

	public void setDOSSIER_JOINTE(String dOSSIER_JOINTE) {
		DOSSIER_JOINTE = dOSSIER_JOINTE;
	}

	public String getDOSSIER_STYLE() {
		return DOSSIER_STYLE;
	}

	public void setDOSSIER_STYLE(String dOSSIER_STYLE) {
		DOSSIER_STYLE = dOSSIER_STYLE;
	}

	public String getIMG_FOND() {
		return IMG_FOND;
	}

	public void setIMG_FOND(String iMG_FOND) {
		IMG_FOND = iMG_FOND;
	}

	public boolean isIs_text_logo() {
		return is_text_logo;
	}

	public void setIs_text_logo(boolean is_text_logo) {
		this.is_text_logo = is_text_logo;
	}

	public String getDUREE_COURRIER() {
		return DUREE_COURRIER;
	}

	public void setDUREE_COURRIER(String dUREE_COURRIER) {
		DUREE_COURRIER = dUREE_COURRIER;
	}

	public String getCOMPLEXE_PASSWORD() {
		return COMPLEXE_PASSWORD;
	}

	public void setCOMPLEXE_PASSWORD(String cOMPLEXE_PASSWORD) {
		COMPLEXE_PASSWORD = cOMPLEXE_PASSWORD;
	}
	

	public String getSERVEUR_SMTP() {
		return SERVEUR_SMTP;
	}

	public void setSERVEUR_SMTP(String sERVEUR_SMTP) {
		SERVEUR_SMTP = sERVEUR_SMTP;
	}

	public String getPORT_SERVEUR_SMTP() {
		return PORT_SERVEUR_SMTP;
	}

	public void setPORT_SERVEUR_SMTP(String pORT_SERVEUR_SMTP) {
		PORT_SERVEUR_SMTP = pORT_SERVEUR_SMTP;
	}


	/**
	 * @return the aDRESSE_COMPTE_ENVOI_MAIL
	 */
	public String getADRESSE_COMPTE_ENVOI_MAIL() {
		return ADRESSE_COMPTE_ENVOI_MAIL;
	}

	/**
	 * @param aDRESSE_COMPTE_ENVOI_MAIL the aDRESSE_COMPTE_ENVOI_MAIL to set
	 */
	public void setADRESSE_COMPTE_ENVOI_MAIL(String aDRESSE_COMPTE_ENVOI_MAIL) {
		ADRESSE_COMPTE_ENVOI_MAIL = aDRESSE_COMPTE_ENVOI_MAIL;
	}

	/**
	 * @return the pWD_COMPTE_ENVOI_MAIL
	 */
	public String getPWD_COMPTE_ENVOI_MAIL() {
		return PWD_COMPTE_ENVOI_MAIL;
	}

	/**
	 * @param pWD_COMPTE_ENVOI_MAIL the pWD_COMPTE_ENVOI_MAIL to set
	 */
	public void setPWD_COMPTE_ENVOI_MAIL(String pWD_COMPTE_ENVOI_MAIL) {
		PWD_COMPTE_ENVOI_MAIL = pWD_COMPTE_ENVOI_MAIL;
	}

	/**
	 * @return the nOM_ENTREPRISE
	 */
	public String getNOM_ENTREPRISE() {
		return NOM_ENTREPRISE;
	}

	/**
	 * @param nOM_ENTREPRISE the nOM_ENTREPRISE to set
	 */
	public void setNOM_ENTREPRISE(String nOM_ENTREPRISE) {
		NOM_ENTREPRISE = nOM_ENTREPRISE;
	}

	public String getOBJET_MAIL() {
		return OBJET_MAIL;
	}

	public void setOBJET_MAIL(String oBJET_MAIL) {
		OBJET_MAIL = oBJET_MAIL;
	}

	public String getCORPS_MAIL() {
		return CORPS_MAIL;
	}

	public void setCORPS_MAIL(String cORPS_MAIL) {
		CORPS_MAIL = cORPS_MAIL;
	}

	public boolean isPRODUCT_OK() {
		return PRODUCT_OK;
	}

	public void setPRODUCT_OK(boolean pRODUCT_OK) {
		if(PRODUCT_OK)
			PRODUCT_OK_2=1;
		else
			PRODUCT_OK_2=2;
		PRODUCT_OK = pRODUCT_OK;
	}

	public int getPRODUCT_OK_2() {
		return PRODUCT_OK_2;
	}

	public void setPRODUCT_OK_2(int pRODUCT_OK_2) {
		PRODUCT_OK_2 = pRODUCT_OK_2;
	}

	
  
	

	
	
	
	

}
