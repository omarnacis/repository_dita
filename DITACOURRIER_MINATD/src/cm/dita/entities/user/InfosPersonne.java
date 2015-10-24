/**
 * 
 */
package cm.dita.entities.user;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * @author bertrand
 *
 */
@Embeddable
public class InfosPersonne  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "nom_raison_social")
	private String nom;
	
	@Column(name = "prenom")
	private String prenom;
	
	@Column(name = "date_naissance")
	private Date dateNaissance;
	
	@Column(name = "profession")
	private String profession;
	
	@Column(name = "statut_matrimonial")
	private String statutMatrimonial;
	
	@Column(name = "localisation")
	private String localisation;
	
	@Column(name="sexe")
	private String sexe;
	
	/**
     * The mail address of the user
     */
    @Column(name = "mailAddress" , nullable = true)
    private String mailAddress;

    @Column(name = "telephone",  nullable = true)
    private String telephone;
    
   
	

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	

	public Date getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getStatutMatrimonial() {
		return statutMatrimonial;
	}

	public void setStatutMatrimonial(String statutMatrimonial) {
		this.statutMatrimonial = statutMatrimonial;
	}

	public String getLocalisation() {
		return localisation;
	}

	public void setLocalisation(String localisation) {
		this.localisation = localisation;
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getSexe() {
		return sexe;
	}

	public void setSexe(String sexe) {
		this.sexe = sexe;
	}
	
	


}
