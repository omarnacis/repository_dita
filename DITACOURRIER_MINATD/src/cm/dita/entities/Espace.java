/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cm.dita.entities;

import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


import javax.xml.bind.annotation.XmlRootElement;

import cm.dita.entities.user.User;
import cm.dita.object.model.OMBase;


/**
 *
 * @author Bessala
 */
@Entity
@Table(name = "dita_espace")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Espace.findAll", query = "SELECT e FROM Espace e"),
    @NamedQuery(name = "Espace.findById", query = "SELECT e FROM Espace e WHERE e.id = :id"),
    @NamedQuery(name = "Espace.findByNomespace", query = "SELECT e FROM Espace e WHERE LOWER(e.nomespace) = :nomespace AND e.delate='false'")})
public class Espace extends OMBase{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)   
    @Column(name = "Nomespace")
    private String nomespace;
    
    @Column(name = "dateCreation")
    private String dateCreation;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "espace")
    private Collection<User> userCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "espace")
    private Collection<EspaceCourrier> espaceCourrierCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "espacedestf")
    private Collection<Courriers> CourrierCollection;
    
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "espacePrecedent")
    private Collection<EspaceCourrier> CourrierPrecedentCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "espacedestination")
    private Collection<EspaceCourrier> CourrierDestinationCollection;
    
    public Espace() {
    }

    public Espace(Integer id) {
        this.id = id;
    }

    public Espace(Integer id, String nomespace) {
        this.id = id;
        this.nomespace = nomespace;
    }
    

    public Espace(Espace espace) {
		super();
		this.id = espace.id;
		this.nomespace = espace.nomespace;
		this.dateCreation = espace.dateCreation;
		this.userCollection = espace.userCollection;
		this.espaceCourrierCollection = espace.espaceCourrierCollection;
		CourrierCollection = espace.CourrierCollection;
		CourrierPrecedentCollection = espace.CourrierPrecedentCollection;
		CourrierDestinationCollection = espace.CourrierDestinationCollection;
		
		this.setDateCreate(espace.getDateCreate());
		this.setIdLastUserUpdate(espace.getIdLastUserUpdate());
		this.setIdUserCreate(espace.getIdUserCreate());
		this.setIdLastUserUpdate(espace.getIdLastUserUpdate());
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomespace() {
        return nomespace;
    }

    public void setNomespace(String nomespace) {
        this.nomespace = nomespace;
    }

    
    public Collection<User> getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(Collection<User> userCollection) {
        this.userCollection = userCollection;
    }

    
    public Collection<EspaceCourrier> getEspaceCourrierCollection() {
        return espaceCourrierCollection;
    }

    public void setEspaceCourrierCollection(Collection<EspaceCourrier> espaceCourrierCollection) {
        this.espaceCourrierCollection = espaceCourrierCollection;
    }
    
    

    public String getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(String dateCreation) {
		this.dateCreation = dateCreation;
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
        if (!(object instanceof Espace)) {
            return false;
        }
        Espace other = (Espace) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    

    /**
	 * @return the courrierCollection
	 */
	public Collection<Courriers> getCourrierCollection() {
		return CourrierCollection;
	}

	/**
	 * @param courrierCollection the courrierCollection to set
	 */
	public void setCourrierCollection(Collection<Courriers> courrierCollection) {
		CourrierCollection = courrierCollection;
	}

	
	/**
	 * @return the courrierPrecedentCollection
	 */
	public Collection<EspaceCourrier> getCourrierPrecedentCollection() {
		return CourrierPrecedentCollection;
	}

	/**
	 * @param courrierPrecedentCollection the courrierPrecedentCollection to set
	 */
	public void setCourrierPrecedentCollection(
			Collection<EspaceCourrier> courrierPrecedentCollection) {
		CourrierPrecedentCollection = courrierPrecedentCollection;
	}

	@Override
    public String toString() {
        return "cm.dita.entities.Espace[ id=" + id + " ]";
    }
    
}
