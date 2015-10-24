/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cm.dita.entities;

import cm.dita.object.model.OMBase;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Bessala
 */
@Entity
@Table(name = "dita_preferences")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Preferences.findAll", query = "SELECT p FROM Preferences p"),
    @NamedQuery(name = "Preferences.findByidentifier", query = "SELECT p FROM Preferences p WHERE p.identifier = :identifier"),
    @NamedQuery(name = "Preferences.findByName", query = "SELECT p FROM Preferences p WHERE p.preferenceName = :name")
    
})

public class Preferences extends OMBase {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "identifier")
    private Integer identifier;
    
	@Column(name = "preference_name")
	private String preferenceName;
	
	@Column(name = "preference_value")
	private String preferenceValue;
	
	@Column(name = "description",length=2000)
	private String description;
	
	@Column(name = "dateCreation")
	private String dateCreation;
	
	
	//public Prefernces(String preferenceName,String preferenceValue,String description)

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (identifier != null ? identifier.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Preferences)) {
            return false;
        }
        Preferences other = (Preferences) object;
        if ((this.identifier == null && other.identifier != null) || (this.identifier != null && !this.identifier.equals(other.identifier))) {
            return false;
        }
        return true;
    }
    /*public String toString() {
        return "entities.Statuts[ statutid=" + identifier + " ]";
    }*/

	public Integer getIdentifier() {
		return identifier;
	}

	public void setIdentifier(Integer identifier) {
		this.identifier = identifier;
	}

	

	public String getPreferenceName() {
		return preferenceName;
	}

	public void setPreferenceName(String preferenceName) {
		this.preferenceName = preferenceName;
	}

	public String getPreferenceValue() {
		return preferenceValue;
	}

	public void setPreferenceValue(String preferenceValue) {
		this.preferenceValue = preferenceValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(String dateCreation) {
		this.dateCreation = dateCreation;
	}
    
    
}
