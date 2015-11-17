/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cm.dita.entities;

import cm.dita.entities.user.User;
import cm.dita.object.model.OMBase;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Bessala
 */
@Entity
@Table(name = "dita_espace_courrier")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EspaceCourrier.findAll", query = "SELECT e FROM EspaceCourrier e"),
    @NamedQuery(name = "EspaceCourrier.findById", query = "SELECT e FROM EspaceCourrier e WHERE e.id = :id"),
    @NamedQuery(name = "EspaceCourrier.findByEspacePrecedent", query = "SELECT e FROM EspaceCourrier e WHERE e.espacePrecedent = :espacePrecedent"),
    @NamedQuery(name = "EspaceCourrier.findByDatearrive", query = "SELECT e FROM EspaceCourrier e WHERE e.datearrive = :datearrive"),
    @NamedQuery(name = "EspaceCourrier.findByDatesortie", query = "SELECT e FROM EspaceCourrier e WHERE e.datesortie = :datesortie"),
    @NamedQuery(name = "EspaceCourrier.findByEspaceDestination", query = "SELECT e FROM EspaceCourrier e WHERE e.espacedestination = :espacedestination"),
    @NamedQuery(name = "EspaceCourrier.findByBordereauId", query = "SELECT ec FROM EspaceCourrier ec WHERE ec.bordereau.id = :bordereauid and ec.delate = 'false'")})
public class EspaceCourrier extends OMBase {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    
    @Basic(optional = false)
    @JoinColumn(name = "espaceprecedentid", referencedColumnName = "id", insertable = true, updatable = true )
    @ManyToOne(optional = true)
    private Espace espacePrecedent;    
    
    @Column(name = "datearrive", nullable=true, updatable=true)
    private String datearrive;
      
    @Column(name = "datesortie", nullable=true, updatable=true)
    private String datesortie; 
    
    @Column(name = "observation", nullable=true, updatable=true)
    private String observation; 
    
    @JoinColumn(name = "espacedestinationtid", referencedColumnName = "id", nullable=true,updatable=true)
    @ManyToOne(optional = true)
    private Espace espacedestination;
    
    @JoinColumn(name = "usercreateid_Courrier", referencedColumnName = "id", nullable=true)
    @ManyToOne(optional = true)
    private User usercreate;
    
    @JoinColumn(name = "userreceiverid", referencedColumnName = "id", nullable=true)
    @ManyToOne(optional = true)
    private User userreceiver;
    
    @JoinColumn(name = "espaceid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Espace espace;
    
    @JoinColumn(name = "bordereauid", referencedColumnName = "id", nullable=true)
    @ManyToOne(optional = true)
    private Bordereau bordereau;
    
    @JoinColumn(name = "courrierid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Courriers courrier;
    
    @JoinColumn(name = "statutid", referencedColumnName = "id", updatable=true)
    @ManyToOne(optional = false)
    private Statuts statut;
     
    @Column(name = "recu", nullable=true)
    private boolean recu;
    
    @Column(name = "confidentialite", nullable=true)
    private boolean confidentialite=false;
    
    @Column(name = "receptionDate", nullable=true)
    private String recepationDate;
    
    @Column(name = "espaceEnterieur", nullable=true)
    private Long espaceEnterieur;
    
    @Column(name = "observationreReception", nullable=true)
    private String observeReceiver;
    
    
	public EspaceCourrier() {
    }

    public EspaceCourrier(Long id) {
        this.id = id;
    }

    public EspaceCourrier(Long id, Espace espacePrecedent, String datearrive, String datesortie, Espace espacedestination) {
        this.id = id;
        this.espacePrecedent = espacePrecedent;
        this.datearrive = datearrive;
        this.datesortie = datesortie;
        this.espacedestination = espacedestination;
    }
    
    
	public Statuts getStatut() {
        return statut;
    }

    public void setStatut(Statuts statut) {
        this.statut = statut;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Espace getEspacePrecedent() {
        return espacePrecedent;
    }

    public void setEspacePrecedent(Espace espacePrecedent) {
        this.espacePrecedent = espacePrecedent;
    }

    public String getDatearrive() {
        return datearrive;
    }

    public void setDatearrive(String datearrive) {
        this.datearrive = datearrive;
    }

    public String getDatesortie() {
        return datesortie;
    }

    public void setDatesortie(String datesortie) {
        this.datesortie = datesortie;
    }

    public Espace getEspacedestination() {
        return espacedestination;
    }

    public void setEspacedestination(Espace espacedestination) {
        this.espacedestination = espacedestination;
    }

   
    public User getUserreceiver() {
        return userreceiver;
    }

    public void setUserreceiver(User userreceiver) {
        this.userreceiver = userreceiver;
    }

    public Espace getEspace() {
        return espace;
    }

    public void setEspace(Espace espace) {
        this.espace = espace;
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
        if (!(object instanceof EspaceCourrier)) {
            return false;
        }
        EspaceCourrier other = (EspaceCourrier) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    /**
	 * @return the usercreate
	 */
	public User getUsercreate() {
		return usercreate;
	}

	/**
	 * @param usercreate the usercreate to set
	 */
	public void setUsercreate(User usercreate) {
		this.usercreate = usercreate;
	}

	
	/**
	 * @return the courrier
	 */
	public Courriers getCourrier() {
		return courrier;
	}

	/**
	 * @param courrier the courrier to set
	 */
	public void setCourrier(Courriers courrier) {
		this.courrier = courrier;
	}
	
	


	/**
	 * @return the bordereau
	 */
	public Bordereau getBordereau() {
		return bordereau;
	}

	/**
	 * @param bordereau the bordereau to set
	 */
	public void setBordereau(Bordereau bordereau) {
		this.bordereau = bordereau;
	}
	

	/**
	 * @return the observation
	 */
	public String getObservation() {
		return observation;
	}

	/**
	 * @param observation the observation to set
	 */
	public void setObservation(String observation) {
		this.observation = observation;
	}
	

	/**
	 * @return the recu
	 */
	public boolean isRecu() {
		return recu;
	}

	/**
	 * @param recu the recu to set
	 */
	public void setRecu(boolean recu) {
		this.recu = recu;
	}

	/**
	 * @return the recepationDate
	 */
	public String getRecepationDate() {
		return recepationDate;
	}

	/**
	 * @param recepationDate the recepationDate to set
	 */
	public void setRecepationDate(String recepationDate) {
		this.recepationDate = recepationDate;
	}

	/**
	 * @return the espaceEnterieur
	 */
	public Long getEspaceEnterieur() {
		return espaceEnterieur;
	}

	/**
	 * @param espaceEnterieur the espaceEnterieur to set
	 */
	public void setEspaceEnterieur(Long espaceEnterieur) {
		this.espaceEnterieur = espaceEnterieur;
	}

	/**
	 * @return the observeReceiver
	 */
	public String getObserveReceiver() {
		return observeReceiver;
	}

	/**
	 * @param observeReceiver the observeReceiver to set
	 */
	public void setObserveReceiver(String observeReceiver) {
		this.observeReceiver = observeReceiver;
	}
	

	/**
	 * @return the confidentialite
	 */
	public boolean isConfidentialite() {
		return confidentialite;
	}

	/**
	 * @param confidentialite the confidentialite to set
	 */
	public void setConfidentialite(boolean confidentialite) {
		this.confidentialite = confidentialite;
	}

	@Override
    public String toString() {
        return "cm.dita.entities.EspaceCourrier[ id=" + id + " ]";
    }
    
}
