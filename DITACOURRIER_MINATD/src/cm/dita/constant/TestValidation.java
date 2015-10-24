package cm.dita.constant;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.swing.JOptionPane;


// classe juste pour les testes
@ManagedBean
@SessionScoped
public class TestValidation implements Serializable{
	
	/**
	 * omar
	 */
	private static final long serialVersionUID = 1L;
    
	private String courobjet;
	private String courmot;
	private String courobservation;
	
	public String getCourobjet() {
		return courobjet;
	}
	public void setCourobjet(String courobjet) {
		this.courobjet = courobjet;
	}
	public String getCourmot() {
		return courmot;
	}
	public void setCourmot(String courmot) {
		this.courmot = courmot;
	}
	public String getCourobservation() {
		return courobservation;
	}
	public void setCourobservation(String courobservation) {
		this.courobservation = courobservation;
	}
	
	public String submit(){
		JOptionPane.showMessageDialog(null, "gsdfhgf");
		return null;
	}		  
	
}
