/**
 * 
 */
package cm.dita.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import cm.dita.object.model.OMBase;

/**
 * @author Omar Nacis
 *
 */

@NamedQueries({
    @NamedQuery(name = "Correspondant.findAll", query = "SELECT c FROM Correspondant c"),
    @NamedQuery(name = "Correspondant.findById", query = "SELECT c FROM Correspondant c WHERE c.id = :id"),
    @NamedQuery(name = "Correspondant.findByNom", query = "SELECT c FROM Correspondant c WHERE LOWER(c.nom) = :nom  AND c.delate = false")})

@Entity
@Table(name = "dita_correspondant")
public class Correspondant extends OMBase{
   
    private static final long serialVersionUID = 1L;
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
    
	@Column(name = "nom_raison_social")
	private String nom;
	
    /**
     * The creation date of the user
     */
    @Column(name = "creationDate")
    private String dateCreation;
    
	/**
     * The mail address of the user
     */
    @Column(name = "mailAddress" , nullable = true)
    private String mailAddress;

    @Column(name = "telephone",  nullable = true)
    private String telephone;
    
    

	public Correspondant(Correspondant co) {
		super();
		this.id = co.id;
		this.nom = co.nom;
		this.dateCreation = co.dateCreation;
		this.mailAddress = co.mailAddress;
		this.telephone = co.telephone;
		
		this.setDateCreate(co.getDateCreate());
		this.setIdLastUserUpdate(co.getIdLastUserUpdate());
		this.setIdUserCreate(co.getIdUserCreate());
		this.setIdLastUserUpdate(co.getIdLastUserUpdate());
	}
	
	public Correspondant() {
		
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * @return the dateCreation
	 */
	public String getDateCreation() {
		return dateCreation;
	}

	/**
	 * @param dateCreation the dateCreation to set
	 */
	public void setDateCreation(String dateCreation) {
		this.dateCreation = dateCreation;
	}

	/**
	 * @return the mailAddress
	 */
	public String getMailAddress() {
		return mailAddress;
	}

	/**
	 * @param mailAddress the mailAddress to set
	 */
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * @param telephone the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Correspondant)) {
            return false;
        }
        Correspondant other = (Correspondant) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
}
