/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cm.dita.entities;


import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import cm.dita.object.model.OMBase;

/**
 *
 * @author Bessala
 */
@Entity
@Table(name = "dita_piecesjointes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Piecesjointes.findAll", query = "SELECT p FROM Piecesjointes p"),
    @NamedQuery(name = "Piecesjointes.findByPieceid", query = "SELECT p FROM Piecesjointes p WHERE p.pieceid = :pieceid"),
    @NamedQuery(name = "Piecesjointes.findByPersid", query = "SELECT p FROM Piecesjointes p WHERE p.persid = :persid"),
    @NamedQuery(name = "Piecesjointes.findByPiecetitre", query = "SELECT p FROM Piecesjointes p WHERE p.piecetitre = :piecetitre"),
    @NamedQuery(name = "Piecesjointes.findByTypepieceid", query = "SELECT p FROM Piecesjointes p WHERE p.typepieceid = :typepieceid"),
    @NamedQuery(name = "Piecesjointes.findByPiecedate", query = "SELECT p FROM Piecesjointes p WHERE p.piecedate = :piecedate"),
    @NamedQuery(name = "Piecesjointes.findByPieceetat", query = "SELECT p FROM Piecesjointes p WHERE p.pieceetat = :pieceetat"),
    @NamedQuery(name = "Piecesjointes.findByCourrierid", query = "SELECT p FROM Piecesjointes p WHERE p.courrier.id = :courrierid and p.delate = false"),
    @NamedQuery(name = "Piecesjointes.findByPersid2", query = "SELECT p FROM Piecesjointes p WHERE p.persid2 = :persid2")})
public class Piecesjointes extends OMBase {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pieceid")
    private Integer pieceid;
    @Basic(optional = false)
    
    @Column(name = "persid")
    private int persid;
    @Basic(optional = true)
   
    
    @Column(name = "piecetitre", nullable=true)
    private String piecetitre;
    @Basic(optional = true)
    
  
   
    @Column(name = "piecedescription", nullable=true)
    private String piecedescription;
    @Basic(optional = false)
   
    @Column(name = "piecefichier")
    private String piecefichier;
    
    @Basic(optional = true)
   
    @Column(name = "typepieceid", nullable=true)
    private int typepieceid;
    @Basic(optional = false)


    @Column(name = "piecedate")
   /* @Temporal(TemporalType.DATE)*/
    private String piecedate;
    @Basic(optional = false)


    @Column(name = "pieceetat", nullable=true)
    private short pieceetat;
    @Basic(optional = true)

    @Column(name = "persid2", nullable=true)
    private String persid2;
    @JoinColumn(name = "typespiecesjointes_typepieceid", referencedColumnName = "typepieceid", nullable=true)
    @ManyToOne(optional = true)
    private Typespiecesjointes typespiecesjointesTypepieceid;
    
    @JoinColumn(name = "courrierid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Courriers courrier;

    public Piecesjointes() {
    }

    public Piecesjointes(Integer pieceid) {
        this.pieceid = pieceid;
    }

    public Piecesjointes(Integer pieceid, int persid, String piecetitre, String piecedescription, String piecefichier, int typepieceid, String piecedate, short pieceetat, String persid2) {
        this.pieceid = pieceid;
        this.persid = persid;
        this.piecetitre = piecetitre;
        this.piecedescription = piecedescription;
        this.piecefichier = piecefichier;
        this.typepieceid = typepieceid;
        this.piecedate = piecedate;
        this.pieceetat = pieceetat;
        this.persid2 = persid2;
    }

    public Integer getPieceid() {
        return pieceid;
    }

    public void setPieceid(Integer pieceid) {
        this.pieceid = pieceid;
    }

    public int getPersid() {
        return persid;
    }

    public void setPersid(int persid) {
        this.persid = persid;
    }

    public String getPiecetitre() {
        return piecetitre;
    }

    public void setPiecetitre(String piecetitre) {
        this.piecetitre = piecetitre;
    }

    public String getPiecedescription() {
        return piecedescription;
    }

    public void setPiecedescription(String piecedescription) {
        this.piecedescription = piecedescription;
    }

    public String getPiecefichier() {
        return piecefichier;
    }

    public void setPiecefichier(String piecefichier) {
        this.piecefichier = piecefichier;
    }

    public int getTypepieceid() {
        return typepieceid;
    }

    public void setTypepieceid(int typepieceid) {
        this.typepieceid = typepieceid;
    }

 

    public String getPiecedate() {
		return piecedate;
	}

	public void setPiecedate(String piecedate) {
		this.piecedate = piecedate;
	}

	public short getPieceetat() {
        return pieceetat;
    }

    public void setPieceetat(short pieceetat) {
        this.pieceetat = pieceetat;
    }

    public String getPersid2() {
        return persid2;
    }

    public void setPersid2(String persid2) {
        this.persid2 = persid2;
    }

    public Typespiecesjointes getTypespiecesjointesTypepieceid() {
        return typespiecesjointesTypepieceid;
    }

    public void setTypespiecesjointesTypepieceid(Typespiecesjointes typespiecesjointesTypepieceid) {
        this.typespiecesjointesTypepieceid = typespiecesjointesTypepieceid;
    }

    public Courriers getCourrier() {
        return courrier;
    }

    public void setCourrier(Courriers courrier) {
        this.courrier = courrier;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pieceid != null ? pieceid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Piecesjointes)) {
            return false;
        }
        Piecesjointes other = (Piecesjointes) object;
        if ((this.pieceid == null && other.pieceid != null) || (this.pieceid != null && !this.pieceid.equals(other.pieceid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cm.dita.entities.Piecesjointes[ pieceid=" + pieceid + " ]";
    }
    
}
