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
@Table(name = "dita_typespiecesjointes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Typespiecesjointes.findAll", query = "SELECT t FROM Typespiecesjointes t"),
    @NamedQuery(name = "Typespiecesjointes.findByTypepieceid", query = "SELECT t FROM Typespiecesjointes t WHERE t.typepieceid = :typepieceid"),
    @NamedQuery(name = "Typespiecesjointes.findByTypepiecedesignation", query = "SELECT t FROM Typespiecesjointes t WHERE t.typepiecedesignation = :typepiecedesignation")})
public class Typespiecesjointes extends OMBase {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "typepieceid")
    private Integer typepieceid;
    @Basic(optional = false)
   
    @Column(name = "typepiecedesignation")
    private String typepiecedesignation;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "typespiecesjointesTypepieceid")
    private Collection<Piecesjointes> piecesjointesCollection;

    public Typespiecesjointes() {
    }

    public Typespiecesjointes(Integer typepieceid) {
        this.typepieceid = typepieceid;
    }

    public Typespiecesjointes(Integer typepieceid, String typepiecedesignation) {
        this.typepieceid = typepieceid;
        this.typepiecedesignation = typepiecedesignation;
    }

    public Integer getTypepieceid() {
        return typepieceid;
    }

    public void setTypepieceid(Integer typepieceid) {
        this.typepieceid = typepieceid;
    }

    public String getTypepiecedesignation() {
        return typepiecedesignation;
    }

    public void setTypepiecedesignation(String typepiecedesignation) {
        this.typepiecedesignation = typepiecedesignation;
    }

    
    public Collection<Piecesjointes> getPiecesjointesCollection() {
        return piecesjointesCollection;
    }

    public void setPiecesjointesCollection(Collection<Piecesjointes> piecesjointesCollection) {
        this.piecesjointesCollection = piecesjointesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (typepieceid != null ? typepieceid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Typespiecesjointes)) {
            return false;
        }
        Typespiecesjointes other = (Typespiecesjointes) object;
        if ((this.typepieceid == null && other.typepieceid != null) || (this.typepieceid != null && !this.typepieceid.equals(other.typepieceid))) {
            return false;
        }
        return true;
    }

    
}
