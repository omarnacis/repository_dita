/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cm.dita.entities;

import cm.dita.object.model.OMBase;
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


/**
 *
 * @author Bessala
 */
@Entity
@Table(name = "dita_priorites")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Priorites.findAll", query = "SELECT p FROM Priorites p"),
    @NamedQuery(name = "Priorites.findById", query = "SELECT p FROM Priorites p WHERE p.id = :id"),
    @NamedQuery(name = "Priorites.findByPriodesignation", query = "SELECT p FROM Priorites p WHERE p.priodesignation = :priodesignation")})
public class Priorites extends OMBase {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "prioriteordre", unique=true)
    private Integer prioriteordre;
    
    @Basic(optional = false)    
    @Column(name = "priodesignation")
    private String priodesignation;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "priorite")
    private Collection<Courriers> courriersCollection;

    public Priorites() {
    }

    public Priorites(Integer id) {
        this.id = id;
    }

    public Priorites(Integer id, String priodesignation) {
        this.id = id;
        this.priodesignation = priodesignation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPriodesignation() {
        return priodesignation;
    }

    public void setPriodesignation(String priodesignation) {
        this.priodesignation = priodesignation;
    }

    
    public Collection<Courriers> getCourriersCollection() {
        return courriersCollection;
    }

    public void setCourriersCollection(Collection<Courriers> courriersCollection) {
        this.courriersCollection = courriersCollection;
    }

    
    /**
	 * @return the prioriteordre
	 */
	public Integer getPrioriteordre() {
		return prioriteordre;
	}

	/**
	 * @param prioriteordre the prioriteordre to set
	 */
	public void setPrioriteordre(Integer prioriteordre) {
		this.prioriteordre = prioriteordre;
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
        if (!(object instanceof Priorites)) {
            return false;
        }
        Priorites other = (Priorites) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

	public void setPriodesignation(int i) {
		// TODO Auto-generated method stub
		
	}

    
}
