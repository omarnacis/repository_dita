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
@Table(name = "dita_bordereau")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bordereau.findAll", query = "SELECT b FROM Bordereau b"),
    @NamedQuery(name = "Bordereau.findById", query = "SELECT b FROM Bordereau b WHERE b.id = :id"),
    @NamedQuery(name = "Bordereau.findByCheminPj", query = "SELECT b FROM Bordereau b WHERE b.cheminPj = :cheminPj"),
    @NamedQuery(name = "Bordereau.findByEstTraite", query = "SELECT b FROM Bordereau b WHERE b.estTraite = :estTraite"),
    @NamedQuery(name = "Bordereau.findByDateCreation", query = "SELECT b FROM Bordereau b WHERE b.dateCreation = :dateCreation"),
    @NamedQuery(name = "Bordereau.findByNumbordereau", query = "SELECT b FROM Bordereau b WHERE b.numbordereau = :numbordereau"),
    @NamedQuery(name = "Bordereau.findByEspaceid", query = "SELECT b FROM Bordereau b WHERE b.espacebordereauid = :espacebordereauid AND b.delate = 'false'"),
    @NamedQuery(name = "Bordereau.findByEspaceAndTraite", query = "SELECT b FROM Bordereau b WHERE b.espacebordereauid = :espacebordereauid AND b.estTraite = :estTraite AND b.delate = 'false'"),
    @NamedQuery(name = "Bordereau.findByUsercreateid", query = "SELECT b FROM Bordereau b WHERE b.usercreateid = :usercreateid AND b.delate = 'false'"),
    @NamedQuery(name = "Bordereau.findByUserupdateid", query = "SELECT b FROM Bordereau b WHERE b.userupdateid = :userupdateid AND b.delate = 'false'"),
    @NamedQuery(name = "Bordereau.findByUsercreateIdAndDateUseToSortData", query = "SELECT b FROM Bordereau b WHERE b.dateUseToSortData = :dateUserToSortData AND b.usercreateid = :userCreateId")})
public class Bordereau extends OMBase {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "cheminPj")
    private String cheminPj;
    
    @Column(name = "est_traite")
    private boolean estTraite;
    
    @Column(name = "datecreation")   
    private String dateCreation;
    
    @Column(name = "datetraitement")   
    private String datetraitement;
    
    @Column(name = "usercreateid_bordereau", nullable=false)
    private Integer usercreateid;
    
    @Column(name = "userupdateid", nullable=true)
    private Integer userupdateid;
    
    @Column(name = "espacebordereauid", nullable=true)
    private Integer espacebordereauid;
    
    @javax.persistence.Transient//transient estutiliser pour emp�cher la colonne d'etre cr�er dans la BD
    private Espace espacebordereau;
    
    @Column(name = "numbordereau")
    private String numbordereau;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bordereau")
    private Collection<EspaceCourrier> espaceCourrierCollection;

    public Bordereau() {
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



	public String getCheminPj() {
        return cheminPj;
    }

    public void setCheminPj(String cheminPj) {
        this.cheminPj = cheminPj;
    }

  

    /**
	 * @return the estTraite
	 */
	public boolean isEstTraite() {
		return estTraite;
	}



	/**
	 * @param estTraite the estTraite to set
	 */
	public void setEstTraite(boolean estTraite) {
		this.estTraite = estTraite;
	}



	public String getNumbordereau() {
        return numbordereau;
    }

    public void setNumbordereau(String numbordereau) {
        this.numbordereau = numbordereau;
    }
    
    
   
    /*public Collection<EspaceCourrier> getEspaceCourrierCollection() {
        return espaceCourrierCollection;
    }

    public void setEspaceCourrierCollection(Collection<EspaceCourrier> espaceCourrierCollection) {
        this.espaceCourrierCollection = espaceCourrierCollection;
    }*/

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
	 * @return the datetraitement
	 */
	public String getDatetraitement() {
		return datetraitement;
	}

	/**
	 * @param datetraitement the datetraitement to set
	 */
	public void setDatetraitement(String datetraitement) {
		this.datetraitement = datetraitement;
	}

	/**
	 * @return the usercreateid
	 */
	public Integer getUsercreateid() {
		return usercreateid;
	}

	/**
	 * @param usercreateid the usercreateid to set
	 */
	public void setUsercreateid(Integer usercreateid) {
		this.usercreateid = usercreateid;
	}

	/**
	 * @return the userupdateid
	 */
	public Integer getUserupdateid() {
		return userupdateid;
	}

	/**
	 * @param userupdateid the userupdateid to set
	 */
	public void setUserupdateid(Integer userupdateid) {
		this.userupdateid = userupdateid;
	}



	/**
	 * @return the espacebordereauid
	 */
	public Integer getEspacebordereauid() {
		return espacebordereauid;
	}

	/**
	 * @param espacebordereauid the espacebordereauid to set
	 */
	public void setEspacebordereauid(Integer espacebordereauid) {
		this.espacebordereauid = espacebordereauid;
	}


	/**
	 * @return the espaceCourrierCollection
	 */
	public Collection<EspaceCourrier> getEspaceCourrierCollection() {
		return espaceCourrierCollection;
	}

	/**
	 * @param espaceCourrierCollection the espaceCourrierCollection to set
	 */
	public void setEspaceCourrierCollection(
			Collection<EspaceCourrier> espaceCourrierCollection) {
		this.espaceCourrierCollection = espaceCourrierCollection;
	}
	
	
	/**
	 * @return the espacebordereau
	 */
	public Espace getEspacebordereau() {
		return espacebordereau;
	}

	/**
	 * @param espacebordereau the espacebordereau to set
	 */
	public void setEspacebordereau(Espace espacebordereau) {
		this.espacebordereau = espacebordereau;
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
        if (!(object instanceof Bordereau)) {
            return false;
        }
        Bordereau other = (Bordereau) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

	@Override
    public String toString() {
        return "cm.dita.entities.Bordereau[ id=" + id + " ]";
    }
    
}
