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

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import cm.dita.entities.user.User;


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
    private User user;
    private boolean notAccountExpired;
    private boolean dSesionCourante_plusResenteQue_dDerniereSesion;
	

	//Initailisation des elements de la bean
	@PostConstruct
	public void init(){
		try {
			notAccountExpired = true;//par defaut, un compte n'a pas de durée de validité et les date de debut et de fin sont vides
			user = new User();//initialisation de l'utilisateur
			user.setEnabled(true);//par defaut un utilisateur est actif
			nbLoginSuccessifEchoue = 0;//initialisation du nombre de tentative de connection avec echec
			dSesionCourante_plusResenteQue_dDerniereSesion = true;//la date de la session courante doit être plus ressente que la date de la derniere session
				
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


	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}


	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}


	/**
	 * @return the notAccountExpired
	 */
	public boolean isNotAccountExpired() {
		return notAccountExpired;
	}


	/**
	 * @param notAccountExpired the notAccountExpired to set
	 */
	public void setNotAccountExpired(boolean notAccountExpired) {
		this.notAccountExpired = notAccountExpired;
	}


	/**
	 * @return the dSesionCourante_plusResenteQue_dDerniereSesion
	 */
	public boolean isdSesionCourante_plusResenteQue_dDerniereSesion() {
		return dSesionCourante_plusResenteQue_dDerniereSesion;
	}


	/**
	 * @param dSesionCourante_plusResenteQue_dDerniereSesion the dSesionCourante_plusResenteQue_dDerniereSesion to set
	 */
	public void setdSesionCourante_plusResenteQue_dDerniereSesion(
			boolean dSesionCourante_plusResenteQue_dDerniereSesion) {
		this.dSesionCourante_plusResenteQue_dDerniereSesion = dSesionCourante_plusResenteQue_dDerniereSesion;
	}


	

	
}

