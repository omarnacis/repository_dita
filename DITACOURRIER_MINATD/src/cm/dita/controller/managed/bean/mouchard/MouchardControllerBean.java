package cm.dita.controller.managed.bean.mouchard;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
//import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import cm.dita.constant.IConstance;
import cm.dita.entities.Mouchard;
import cm.dita.service.domaine.inter.IMouchardRessourceService;
@ManagedBean(name="mouchardControllerBean")
@ViewScoped
public class MouchardControllerBean  implements Serializable{
	
	private static final Logger LOG = Logger.getLogger(MouchardControllerBean.class);
	private static final long serialVersionUID = 1L;
	
	
	private List<Mouchard> listMouchard;
	private MouchardDataModel listMouchardMeta;
	private String mouchardPrintDate;
	
	@ManagedProperty(value="#{mouchardRessourceService}")
	private IMouchardRessourceService mouchardRessourceService;
	public MouchardControllerBean(){
		
	}
	
	@PostConstruct
	public void init(){
		
		    java.util.Date aujourdhui = new java.util.Date();	    	
	    	SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_HHmmss");    	
	    	mouchardPrintDate = sdf.format(aujourdhui);
		
		listMouchard=mouchardRessourceService.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
		//JOptionPane.showMessageDialog(null, "testetst"+listMouchard);
		
		setListMouchardMeta(new MouchardDataModel(listMouchard));
		
	}

	/**
	 * @return the listMouchardMeta
	 */
	public MouchardDataModel getListMouchardMeta() {
		return listMouchardMeta;
	}

	/**
	 * @param listMouchardMeta the listMouchardMeta to set
	 */
	public void setListMouchardMeta(MouchardDataModel listMouchardMeta) {
		this.listMouchardMeta = listMouchardMeta;
	}

	/**
	 * @return the mouchardRessourceService
	 */
	public IMouchardRessourceService getMouchardRessourceService() {
		return mouchardRessourceService;
	}

	/**
	 * @param mouchardRessourceService the mouchardRessourceService to set
	 */
	public void setMouchardRessourceService(
			IMouchardRessourceService mouchardRessourceService) {
		this.mouchardRessourceService = mouchardRessourceService;
	}

	/**
	 * @return the listMouchard
	 */
	public List<Mouchard> getListMouchard() {
		return listMouchard;
	}

	/**
	 * @param listMouchard the listMouchard to set
	 */
	public void setListMouchard(List<Mouchard> listMouchard) {
		this.listMouchard = listMouchard;
	}

	/**
	 * @return the mouchardPrintDate
	 */
	public String getMouchardPrintDate() {
		return mouchardPrintDate;
	}

	/**
	 * @param mouchardPrintDate the mouchardPrintDate to set
	 */
	public void setMouchardPrintDate(String mouchardPrintDate) {
		this.mouchardPrintDate = mouchardPrintDate;
	}
	
	
	
}
