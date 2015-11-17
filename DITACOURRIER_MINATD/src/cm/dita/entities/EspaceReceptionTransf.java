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
@Table(name = "dita_espace_reception_transf")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EspaceReceptionTransf.findAll", query = "SELECT e FROM EspaceReceptionTransf e"),
    @NamedQuery(name = "EspaceReceptionTransf.findById", query = "SELECT e FROM EspaceReceptionTransf e WHERE e.id = :id")})
public class EspaceReceptionTransf extends OMBase {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "reponsecotation", nullable=true, updatable=true)
    private String reponseCotation; 
    
    @Column(name = "datereponse", nullable=true, updatable=true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateReponse; 
       
    @JoinColumn(name = "espaceid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Espace espace;
    
    @JoinColumn(name = "transmissionid", referencedColumnName = "id", nullable=true)
    @ManyToOne(optional = true)
    private EspaceCourrier transmission;
        
	public EspaceReceptionTransf() {
    }

    public EspaceReceptionTransf(Long id) {
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
	 * @return the reponseCotation
	 */
	public String getReponseCotation() {
		return reponseCotation;
	}

	/**
	 * @param reponseCotation the reponseCotation to set
	 */
	public void setReponseCotation(String reponseCotation) {
		this.reponseCotation = reponseCotation;
	}

	/**
	 * @return the dateReponse
	 */
	public Date getDateReponse() {
		return dateReponse;
	}

	/**
	 * @param dateReponse the dateReponse to set
	 */
	public void setDateReponse(Date dateReponse) {
		this.dateReponse = dateReponse;
	}

	/**
	 * @return the espace
	 */
	public Espace getEspace() {
		return espace;
	}

	/**
	 * @param espace the espace to set
	 */
	public void setEspace(Espace espace) {
		this.espace = espace;
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
    
    

}
