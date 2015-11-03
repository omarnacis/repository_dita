/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cm.dita.entities;

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


import cm.dita.entities.user.User;
import cm.dita.object.model.OMBase;

/**
 *
 * @author Bessala
 */
@Entity
@Table(name = "dita_courriers")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Courriers.findAll", query = "SELECT c FROM Courriers c"),
    @NamedQuery(name = "Courriers.findById", query = "SELECT c FROM Courriers c WHERE c.id = :id"),
    @NamedQuery(name = "Courriers.findByRefid", query = "SELECT c FROM Courriers c WHERE c.refid = :refid"),
    @NamedQuery(name = "Courriers.findByCourobjet", query = "SELECT c FROM Courriers c WHERE c.courobjet = :courobjet"),
    @NamedQuery(name = "Courriers.findByCourdate", query = "SELECT c FROM Courriers c WHERE c.courdate = :courdate"),
    @NamedQuery(name = "Courriers.findByUsercreate", query = "SELECT c FROM Courriers c WHERE c.usercreate = :usercreate"),
    @NamedQuery(name = "Courriers.findByCourdatenreg", query = "SELECT c FROM Courriers c WHERE c.courdatenreg = :courdatenreg"),
    @NamedQuery(name = "Courriers.findByCourdatemodif", query = "SELECT c FROM Courriers c WHERE c.courdatemodif = :courdatemodif"),
    @NamedQuery(name = "Courriers.findByConfidentiel", query = "SELECT c FROM Courriers c WHERE c.confidentiel = :confidentiel"),
    @NamedQuery(name = "Courriers.findByEspaceOfUserConnect", query = "SELECT c FROM Courriers c WHERE c.idespacecourantducour = :idespacecourantducour AND c.delate = false" ),
    @NamedQuery(name = "Courriers.findByUsercreateIdAndDateUseToSortData", query = "SELECT c FROM Courriers c WHERE c.dateUseToSortData = :dateUserToSortData AND c.usercreate.id = :userCreateId")})

public class Courriers extends OMBase {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
 
    @Column(name = "refid", nullable=true, unique=true)
    private String refid;
    @Basic(optional = false)
 
   // @Size(min = 1, max = 255)
    @Column(name = "courobjet")
    private String courobjet;
    @Basic(optional = false)
 
    //date de dépot du courrier
    @Column(name = "courdate", nullable=true)
    @Temporal(TemporalType.DATE)
    private Date courdate;
    
    @Basic(optional = false)
 
     //@Size(min = 1, max = 65535)
    @Column(name = "courmots")
    private String courmots;
   
    @JoinColumn(name = "usercreateid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User usercreate;
    
    @Column(name = "courdatenreg",nullable=false)    
    private String courdatenreg;
    
    @JoinColumn(name = "lastuserupdateid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User lastUserUpdate;
    
    @Column(name = "courdatemodif")   
    private String courdatemodif;
 
    @Column(name = "idstatutcourantducour", nullable=false)
    private Integer idstatutcourantducour;
    
    @Column(name = "idespacecourantducour", nullable=false)
    private Integer idespacecourantducour;
    

    

    
    @Column(name = "courobservation",length=2000)
    private String courobservation;
    
    @Column(name = "confidentiel")
    private boolean confidentiel;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "courrier")
    private Collection<EspaceCourrier> espaceCourrierCollection;
    
      
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "courrier")
    private Collection<Piecesjointes> piecesjointesCollection;
    
    //Attribut de celui qui envoie le courrier
    @JoinColumn(name = "usersenderid", referencedColumnName = "id", nullable=true)
    @ManyToOne(optional = true)
    private User usersender;
    
    //Attribut de jointure
    @JoinColumn(name = "typecourrierid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Typescourriers typecourrier;
    
    @JoinColumn(name = "prioriteid", referencedColumnName = "id", nullable=true)
    @ManyToOne(optional = true)
    private Priorites priorite;
    
    @JoinColumn(name = "userreceiverid", referencedColumnName = "id", updatable = false, nullable=true)
    @ManyToOne(optional = true)
    private User userreceiver;
    
    @JoinColumn(name = "espacedestfid", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Espace espacedestf;
    
    //Attribut de celui qui envoie le courrier
    @JoinColumn(name = "correspondantsenderid", referencedColumnName = "id", nullable=true)
    @ManyToOne(optional = true)
    private Correspondant correspondantsender;
    
    @JoinColumn(name = "correspondantreceiverid", referencedColumnName = "id", updatable = false, nullable=true)
    @ManyToOne(optional = true)
    private Correspondant correspondantreceiver;

    @Column(name = "courrierparentid", nullable=true)
    private Integer courrierparentid;
    
    @Column(name = "courrierracineid", nullable=true)
    private Integer courrierracineid;
    
    public Courriers() {
    }

    public Courriers(Integer id) {
        this.id = id;
    }

    public Courriers(Integer id, String refid, String courobjet, Date courdate, String courmots, User usercreate, String courdatenreg) {
        this.id = id;
        this.refid = refid;
        this.courobjet = courobjet;
        this.courdate = courdate;
        this.courmots = courmots;
        this.usercreate = usercreate;
        this.courdatenreg = courdatenreg;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRefid() {
        return refid;
    }

    public void setRefid(String refid) {
        this.refid = refid;
    }

    public String getCourobjet() {
        return courobjet;
    }

    public void setCourobjet(String courobjet) {
        this.courobjet = courobjet;
    }    

    public String getCourmots() {
        return courmots;
    }

    public void setCourmots(String courmots) {
        this.courmots = courmots;
    }

    public String getCourobservation() {
        return courobservation;
    }

    public void setCourobservation(String courobservation) {
        this.courobservation = courobservation;
    }

  
    
    public boolean isConfidentiel() {
		return confidentiel;
	}

	public void setConfidentiel(boolean confidentiel) {
		this.confidentiel = confidentiel;
	}

	public Collection<Piecesjointes> getPiecesjointesCollection() {
        return piecesjointesCollection;
    }

    public void setPiecesjointesCollection(Collection<Piecesjointes> piecesjointesCollection) {
        this.piecesjointesCollection = piecesjointesCollection;
    }

    public User getUsersender() {
        return usersender;
    }

    public void setUsersender(User usersender) {
        this.usersender = usersender;
    }

    public Typescourriers getTypecourrier() {
        return typecourrier;
    }

    public void setTypecourrier(Typescourriers typecourrier) {
        this.typecourrier = typecourrier;
    }

    
    public Priorites getPriorite() {
        return priorite;
    }


    public User getUserreceiver() {
		return userreceiver;
	}

	public void setUserreceiver(User userreceiver) {
		this.userreceiver = userreceiver;
	}

	public void setPriorite(Priorites priorite) {
		this.priorite = priorite;
	}

	public User getUsercreate() {
		return usercreate;
	}

	public void setUsercreate(User usercreate) {
		this.usercreate = usercreate;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
	

    public Espace getEspacedestf() {
		return espacedestf;
	}

	public void setEspacedestf(Espace espacedestf) {
		this.espacedestf = espacedestf;
	}

	
	@Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Courriers)) {
            return false;
        }
        Courriers other = (Courriers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    

    public Collection<EspaceCourrier> getEspaceCourrierCollection() {
		return espaceCourrierCollection;
	}

	public void setEspaceCourrierCollection(
			Collection<EspaceCourrier> espaceCourrierCollection) {
		this.espaceCourrierCollection = espaceCourrierCollection;
	}

	/**
	 * @return the idstatutcourantducour
	 */
	public Integer getIdstatutcourantducour() {
		return idstatutcourantducour;
	}

	/**
	 * @param idstatutcourantducour the idstatutcourantducour to set
	 */
	public void setIdstatutcourantducour(Integer idstatutcourantducour) {
		this.idstatutcourantducour = idstatutcourantducour;
	}

	/**
	 * @return the idespacecourantducour
	 */
	public Integer getIdespacecourantducour() {
		return idespacecourantducour;
	}

	/**
	 * @param idespacecourantducour the idespacecourantducour to set
	 */
	public void setIdespacecourantducour(Integer idespacecourantducour) {
		this.idespacecourantducour = idespacecourantducour;
	}

	@Override
    public String toString() {
        return "cm.dita.entities.Courriers[ id=" + id + " ]";
    }

	/**
	 * @return the correspondantsender
	 */
	public Correspondant getCorrespondantsender() {
		return correspondantsender;
	}

	/**
	 * @param correspondantsender the correspondantsender to set
	 */
	public void setCorrespondantsender(Correspondant correspondantsender) {
		this.correspondantsender = correspondantsender;
	}

	/**
	 * @return the correspondantreceiver
	 */
	public Correspondant getCorrespondantreceiver() {
		return correspondantreceiver;
	}

	/**
	 * @param correspondantreceiver the correspondantreceiver to set
	 */
	public void setCorrespondantreceiver(Correspondant correspondantreceiver) {
		this.correspondantreceiver = correspondantreceiver;
	}

	/**
	 * @return the courrierparentid
	 */
	public Integer getCourrierparentid() {
		return courrierparentid;
	}

	/**
	 * @param courrierparentid the courrierparentid to set
	 */
	public void setCourrierparentid(Integer courrierparentid) {
		this.courrierparentid = courrierparentid;
	}

	/**
	 * @return the courrierracineid
	 */
	public Integer getCourrierracineid() {
		return courrierracineid;
	}

	/**
	 * @param courrierracineid the courrierracineid to set
	 */
	public void setCourrierracineid(Integer courrierracineid) {
		this.courrierracineid = courrierracineid;
	}


	/**
	 * @return the courdate
	 */
	public Date getCourdate() {
		return courdate;
	}

	/**
	 * @param courdate the courdate to set
	 */
	public void setCourdate(Date courdate) {
		this.courdate = courdate;
	}

	/**
	 * @return the courdatenreg
	 */
	public String getCourdatenreg() {
		return courdatenreg;
	}

	/**
	 * @param courdatenreg the courdatenreg to set
	 */
	public void setCourdatenreg(String courdatenreg) {
		this.courdatenreg = courdatenreg;
	}

	/**
	 * @return the courdatemodif
	 */
	public String getCourdatemodif() {
		return courdatemodif;
	}

	/**
	 * @param courdatemodif the courdatemodif to set
	 */
	public void setCourdatemodif(String courdatemodif) {
		this.courdatemodif = courdatemodif;
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
    
	
	
}
