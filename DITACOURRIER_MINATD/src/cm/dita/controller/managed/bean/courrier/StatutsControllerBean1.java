package cm.dita.controller.managed.bean.courrier;

import java.io.Serializable;

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
public class StatutsControllerBean1 implements Serializable{

	private static final Logger LOG = Logger.getLogger(StatutsControllerBean1.class);
	private static final long serialVersionUID = 1L;
	
	private String designation;

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

public void ajoutEvent() {
System.out.println("=======================================");
//JOptionPane.showMessageDialog(null, "rr");
Statuts statuts = new Statuts();
statuts.setStatutdesignation(designation);
statuts.setDelate(true);
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
