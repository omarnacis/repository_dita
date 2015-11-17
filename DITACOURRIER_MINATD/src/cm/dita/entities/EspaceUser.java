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
@Table(name = "dita_espace_user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EspaceUser.findAll", query = "SELECT e FROM EspaceUser e"),
    @NamedQuery(name = "EspaceUser.findById", query = "SELECT e FROM EspaceUser e WHERE e.id = :id"),
    @NamedQuery(name = "EspaceUser.findByDatearrive", query = "SELECT e FROM EspaceUser e WHERE e.datearrive = :datearrive"),
    @NamedQuery(name = "EspaceUser.findByDatesortie", query = "SELECT e FROM EspaceUser e WHERE e.datesortie = :datesortie")})
public class EspaceUser extends OMBase {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "datearrive", nullable=true, updatable=true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date datearrive;
      
    @Column(name = "datesortie", nullable=true, updatable=true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date datesortie; 
    
    @JoinColumn(name = "espaceid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Espace espace;
    
    @JoinColumn(name = "userid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User user;
  
	public EspaceUser() {
    }

    public EspaceUser(Long id) {
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
	 * @return the datearrive
	 */
	public Date getDatearrive() {
		return datearrive;
	}

	/**
	 * @param datearrive the datearrive to set
	 */
	public void setDatearrive(Date datearrive) {
		this.datearrive = datearrive;
	}

	/**
	 * @return the datesortie
	 */
	public Date getDatesortie() {
		return datesortie;
	}

	/**
	 * @param datesortie the datesortie to set
	 */
	public void setDatesortie(Date datesortie) {
		this.datesortie = datesortie;
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
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}


    
}

