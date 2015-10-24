package cm.dita.controller.managed.bean.courrier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
//import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import cm.dita.entities.Courriers;
import cm.dita.entities.Statuts;
import cm.dita.service.domaine.inter.ICourriersRessourceService;
import cm.dita.service.domaine.inter.IStatutsRessourceService;


@ManagedBean
@ViewScoped
public class StatutsControllerBean implements Serializable{

	private static final Logger LOG = Logger.getLogger(StatutsControllerBean.class);
	private static final long serialVersionUID = 1L;
	
	private List<Statuts> listeStatuts = new ArrayList<Statuts>();
	private String designation;

	
	public List<Statuts> getListeStatuts() {
		return listeStatuts;
	}

	public void setListeStatuts(List<Statuts> listeStatuts) {
		this.listeStatuts = listeStatuts;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}
	//Spring User Service is injected...
	@ManagedProperty(value="#{statutsRessourceServiceImpl}")
	IStatutsRessourceService statutsRessourceServiceImpl;
	
// juste pour tester l'affichage d'une valeur
	public String submit(){
		return null;
	}

	//liste les status
	public void getListeStatuts1(){
		this.listeStatuts = statutsRessourceServiceImpl.list();
	}
	
	public void ajoutEvent() {
		System.out.println("=======================================");
		//JOptionPane.showMessageDialog(null, "rr");
		Statuts statuts = new Statuts();
		statuts.setStatutdesignation(designation);
		statuts.setDelate(false);
		statutsRessourceServiceImpl.save(statuts);	
	}

	public IStatutsRessourceService getStatutsRessourceServiceImpl() {
		return statutsRessourceServiceImpl;
	}
	
	public void setStatutsRessourceServiceImpl(
			IStatutsRessourceService statutsRessourceServiceImpl) {
		this.statutsRessourceServiceImpl = statutsRessourceServiceImpl;
	}
		
	
}
