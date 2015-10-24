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
@Table(name = "dita_typescourriers")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Typescourriers.findAll", query = "SELECT t FROM Typescourriers t"),
    @NamedQuery(name = "Typescourriers.findById", query = "SELECT t FROM Typescourriers t WHERE t.id = :id"),
    @NamedQuery(name = "Typescourriers.findByTypecourdesignation", query = "SELECT t FROM Typescourriers t WHERE t.typecourdesignation = :typecourdesignation")})
public class Typescourriers extends OMBase {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    
    @Column(name = "typecourdesignation")
    private String typecourdesignation;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "typecourrier")
    private Collection<Courriers> courriersCollection;

    public Typescourriers() {
    }

    public Typescourriers(Integer id) {
        this.id = id;
    }

    public Typescourriers(Integer id, String typecourdesignation) {
        this.id = id;
        this.typecourdesignation = typecourdesignation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypecourdesignation() {
        return typecourdesignation;
    }

    public void setTypecourdesignation(String typecourdesignation) {
        this.typecourdesignation = typecourdesignation;
    }

    
    public Collection<Courriers> getCourriersCollection() {
        return courriersCollection;
    }

    public void setCourriersCollection(Collection<Courriers> courriersCollection) {
        this.courriersCollection = courriersCollection;
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
        if (!(object instanceof Typescourriers)) {
            return false;
        }
        Typescourriers other = (Typescourriers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    
}
