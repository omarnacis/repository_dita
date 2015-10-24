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
@Table(name = "dita_statuts")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Statuts.findAll", query = "SELECT s FROM Statuts s"),
    @NamedQuery(name = "Statuts.findById", query = "SELECT s FROM Statuts s WHERE s.id = :id"),
    @NamedQuery(name = "Statuts.findByStatutdesignation", query = "SELECT s FROM Statuts s WHERE s.statutdesignation = :statutdesignation")})
public class Statuts extends OMBase  {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)
    @Column(name = "statutdesignation")
    private String statutdesignation;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "statut")
    private Collection<EspaceCourrier> espaceCourrierCollection;

    public Statuts() {
    }

    public Statuts(Integer id) {
        this.id = id;
    }

    public Statuts(Integer id, String statutdesignation) {
        this.id = id;
        this.statutdesignation = statutdesignation;
    }

    public Integer getId() {
        return id;
    }
    
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatutdesignation() {
        return statutdesignation;
    }

    public void setStatutdesignation(String statutdesignation) {
        this.statutdesignation = statutdesignation;
    }

    
 

    public Collection<EspaceCourrier> getEspaceCourrierCollection() {
		return espaceCourrierCollection;
	}

	public void setEspaceCourrierCollection(
			Collection<EspaceCourrier> espaceCourrierCollection) {
		this.espaceCourrierCollection = espaceCourrierCollection;
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
        if (!(object instanceof Statuts)) {
            return false;
        }
        Statuts other = (Statuts) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    
}
