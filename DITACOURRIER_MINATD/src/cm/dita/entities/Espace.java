/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cm.dita.entities;

import java.util.Collection;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.primefaces.model.TreeNode;

import cm.dita.entities.user.User;
import cm.dita.object.model.OMBase;


/**
 *
 * @author Bessala
 */
@Entity
@Table(name = "dita_espace")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Espace.findAll", query = "SELECT e FROM Espace e"),
    @NamedQuery(name = "Espace.findById", query = "SELECT e FROM Espace e WHERE e.id = :id"),
   // @NamedQuery(name = "Espace.findByUserConnect", query = "SELECT e FROM Espace e, Projet pr,  PersonneHasProjets p, Personne pers WHERE e = pr.espace AND pr.projid = p.projet AND p.personne = pers.persid AND pers.user = :user AND e.delate = false"),
    @NamedQuery(name = "Espace.findByNomespace", query = "SELECT e FROM Espace e WHERE e.nomespace = :nomespace")})

public class Espace extends OMBase{
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)   
    @Column(name = "Nomespace")
    private String nomespace;
    
    @Column(name = "dateCreation")
    private String dateCreation;
    
    @Column(name="used")
    private boolean used;
    
   
    @Column(columnDefinition="TEXT",nullable = false)
    private String hierachie;
    
   /* @OneToMany(cascade = CascadeType.ALL, mappedBy = "espace")
    private Collection<User> userCollection;*/
    
	    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "espace")
    private Collection<Espace> espaceCollection;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "espace_idParent", nullable = true)
    private Espace espace;
    
    @Transient
    private TreeNode root;
    
    
    public Espace() {
    }

    public Espace(Integer id) {
        this.id = id;
    }

    public Espace(Integer id, String nomespace) {
        this.id = id;
        this.nomespace = nomespace;
    }
    

    public Espace(Espace espace) {
		super();
		this.id = espace.id;
		this.nomespace = espace.nomespace;
		this.dateCreation = espace.dateCreation;
		//this.userCollection = espace.userCollection;
		this.espace=espace.espace;
		this.hierachie=espace.hierachie;
		
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomespace() {
        return nomespace;
    }

    public void setNomespace(String nomespace) {
        this.nomespace = nomespace;
    }

    
  

    public String getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(String dateCreation) {
		this.dateCreation = dateCreation;
	}
	
	

	public String getHierachie() {
		return hierachie;
	}

	public void setHierachie(String hierachie) {
		this.hierachie = hierachie;
	}
	
	

	public Collection<Espace> getEspaceCollection() {
		return espaceCollection;
	}

	public void setEspaceCollection(Collection<Espace> espaceCollection) {
		this.espaceCollection = espaceCollection;
	}
	
	

	public Espace getEspace() {
		return espace;
	}

	public void setEspace(Espace espace) {
		this.espace = espace;
	}
	
	

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
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
        if (!(object instanceof Espace)) {
            return false;
        }
        Espace other = (Espace) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    

	@Override
    public String toString() {
        return "cm.dita.entities.Espace[ id=" + id + " ]";
    }

	/**
	 * @return the used
	 */
	public boolean isUsed() {
		return used;
	}

	/**
	 * @param used the used to set
	 */
	public void setUsed(boolean used) {
		this.used = used;
	}

 
	
}
