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
@Table(name = "dita_piecesjointesTransf")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PiecesJointeTransf.findAll", query = "SELECT e FROM PiecesJointeTransf e"),
    @NamedQuery(name = "PiecesJointeTransf.findById", query = "SELECT e FROM PiecesJointeTransf e WHERE e.id = :id")})
public class PiecesJointeTransf extends OMBase {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
       
    @JoinColumn(name = "piecejointeid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Piecesjointes piecejointe;
    
    @JoinColumn(name = "transmissionid", referencedColumnName = "id", nullable=true)
    @ManyToOne(optional = true)
    private EspaceCourrier transmission;
        
	public PiecesJointeTransf() {
    }

    public PiecesJointeTransf(Long id) {
        this.id = id;
    }

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the transmission
	 */
	public EspaceCourrier getTransmission() {
		return transmission;
	}

	/**
	 * @param transmission the transmission to set
	 */
	public void setTransmission(EspaceCourrier transmission) {
		this.transmission = transmission;
	}

	/**
	 * @return the piecejointe
	 */
	public Piecesjointes getPiecejointe() {
		return piecejointe;
	}

	/**
	 * @param piecejointe the piecejointe to set
	 */
	public void setPiecejointe(Piecesjointes piecejointe) {
		this.piecejointe = piecejointe;
	}
    
    

}
