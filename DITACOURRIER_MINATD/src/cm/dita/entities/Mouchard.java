/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cm.dita.entities;

import cm.dita.entities.user.User;
import cm.dita.object.model.OMBase;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Bessala
 */
@Entity
@Table(name = "dita_mouchard")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Mouchard.findAll", query = "SELECT m FROM Mouchard m"),
    @NamedQuery(name = "Mouchard.findByMouchardId", query = "SELECT m FROM Mouchard m WHERE m.mouchardId = :mouchardId"),
    @NamedQuery(name = "Mouchard.findByMouchardTache", query = "SELECT m FROM Mouchard m WHERE m.mouchardTache = :mouchardTache"),
    @NamedQuery(name = "Mouchard.findByMouchardDate", query = "SELECT m FROM Mouchard m WHERE m.mouchardDate = :mouchardDate"),
    @NamedQuery(name = "Mouchard.findByMouchardUserCode", query = "SELECT m FROM Mouchard m WHERE m.user = :user")})
public class Mouchard extends OMBase {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "mouchard_id")
    private Long mouchardId;
 
    @Column(name = "mouchard_tache")
    @Lob
    private String mouchardTache;
    
    @Column(name = "mouchard_date")
    private String mouchardDate;
    
    @JoinColumn(name = "userid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User user;
    
    @Column(name = "operation", nullable=true)
    private String operation;
    
    @Column(name = "reference_date", nullable=true)
    private Long reference_date;
    
    @Column(name = "entite_name", nullable=true)
    private String entite_name;
   
    @Column(name="code_id",nullable=true, columnDefinition="bigint default '0'")
    private long code_id;
    
    @Column(nullable=true)
    @Lob
	private String valeur;
    
   
    public Mouchard() {
    }

   
    public Mouchard(Long mouchardId) {
        this.mouchardId = mouchardId;
        
    }

    public Long getMouchardId() {
        return mouchardId;
    }

    public void setMouchardId(Long mouchardId) {
        this.mouchardId = mouchardId;
    }

    public String getMouchardTache() {
        return mouchardTache;
    }

    public void setMouchardTache(String mouchardTache) {
        this.mouchardTache = mouchardTache;
    }

    public String getMouchardDate() {
        return mouchardDate;
    }

    public void setMouchardDate(String mouchardDate) {
        this.mouchardDate = mouchardDate;
    }

    public User getMouchardUserCode() {
        return user;
    }

    public void setMouchardUserCode(User user) {
        this.user = user;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mouchardId != null ? mouchardId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mouchard)) {
            return false;
        }
        Mouchard other = (Mouchard) object;
        if ((this.mouchardId == null && other.mouchardId != null) || (this.mouchardId != null && !this.mouchardId.equals(other.mouchardId))) {
            return false;
        }
        return true;
    }
    
    


	public String getOperation() {
		return operation;
	}


	public void setOperation(String operation) {
		this.operation = operation;
	}


	public Long getReference_date() {
		return reference_date;
	}


	public void setReference_date(Long reference_date) {
		this.reference_date = reference_date;
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


	public String getEntite_name() {
		return entite_name;
	}


	public void setEntite_name(String entite_name) {
		this.entite_name = entite_name;
	}


	public long getCode_id() {
		return code_id;
	}


	public void setCode_id(long code_id) {
		this.code_id = code_id;
	}


	public String getValeur() {
		return valeur;
	}


	public void setValeur(String valeur) {
		this.valeur = valeur;
	}
	
	
	
	

}
