/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cm.dita.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import cm.dita.entities.user.User;
import cm.dita.object.model.OMBase;
/**
 *
 * @author Bessala
 */
@Entity
@Table(name = "dita_activites")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Activites.findAll", query = "SELECT a FROM Activites a"),
    @NamedQuery(name = "Activites.findById", query = "SELECT a FROM Activites a WHERE a.id = :id"),
    @NamedQuery(name = "Activites.findByLibelle", query = "SELECT a FROM Activites a WHERE a.libelle = :libelle")})
public class Activites extends OMBase {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "libelle")
    private String libelle;
    
    @JoinColumn(name = "usercreateid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userCreate;
    
    @Column(name = "datecreate",nullable=false)    
    private String dateCreate;
    
    @JoinColumn(name = "lastuserupdateid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User lastUserUpdate;
    
    @Column(name = "lastdatemodif")   
    private String lastDatemodif;

    public Activites() {
    }

    public Activites(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
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
        if (!(object instanceof Activites)) {
            return false;
        }
        Activites other = (Activites) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

	/**
	 * @return the userCreate
	 */
	public User getUserCreate() {
		return userCreate;
	}

	/**
	 * @param userCreate the userCreate to set
	 */
	public void setUserCreate(User userCreate) {
		this.userCreate = userCreate;
	}

	/**
	 * @return the dateCreate
	 */
	public String getDateCreate() {
		return dateCreate;
	}

	/**
	 * @param dateCreate the dateCreate to set
	 */
	public void setDateCreate(String dateCreate) {
		this.dateCreate = dateCreate;
	}

	/**
	 * @return the lastUserUpdate
	 */
	public User getLastUserUpdate() {
		return lastUserUpdate;
	}

	/**
	 * @param lastUserUpdate the lastUserUpdate to set
	 */
	public void setLastUserUpdate(User lastUserUpdate) {
		this.lastUserUpdate = lastUserUpdate;
	}

	/**
	 * @return the lastDatemodif
	 */
	public String getLastDatemodif() {
		return lastDatemodif;
	}

	/**
	 * @param lastDatemodif the lastDatemodif to set
	 */
	public void setLastDatemodif(String lastDatemodif) {
		this.lastDatemodif = lastDatemodif;
	}

    
    
}
