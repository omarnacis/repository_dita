/**
 * 
 */
package cm.dita.controller.managed.bean.sessionControl;



import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
//import javax.swing.JOptionPane;

import org.apache.log4j.Logger;


/**
 * @author Omar Nacis
 *
 */
@ManagedBean(name="sessionControlControllerBean")
@SessionScoped
public class SessionControlControllerBean  implements Serializable{
	
	/**
	 * 
	 */

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(SessionControlControllerBean.class);
	private static final long serialVersionUID = 1L;
	
	//Nombre de tentative de connexion avec echec
    private Integer nbLoginSuccessifEchoue;
	

	//Initailisation des elements de la bean
	@PostConstruct
	public void init(){
		try {
			//JOptionPane.showMessageDialog(null,  "QUELQQUE CHOSES "+nbLoginSuccessifEchoue);
			nbLoginSuccessifEchoue = 0;
				
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}		
	}


	/**
	 * @return the nbLoginSuccessifEchoue
	 */
	public Integer getNbLoginSuccessifEchoue() {
		return nbLoginSuccessifEchoue;
	}


	/**
	 * @param nbLoginSuccessifEchoue the nbLoginSuccessifEchoue to set
	 */
	public void setNbLoginSuccessifEchoue(Integer nbLoginSuccessifEchoue) {
		this.nbLoginSuccessifEchoue = nbLoginSuccessifEchoue;
	}


	
}

