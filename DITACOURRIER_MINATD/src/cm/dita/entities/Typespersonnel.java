/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cm.dita.entities;

import cm.dita.entities.user.User;
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
@Table(name = "dita_typespersonnel")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Typespersonnel.findAll", query = "SELECT t FROM Typespersonnel t"),
    @NamedQuery(name = "Typespersonnel.findByTypepersid", query = "SELECT t FROM Typespersonnel t WHERE t.typepersid = :typepersid"),
    @NamedQuery(name = "Typespersonnel.findByTypepersdesignation", query = "SELECT t FROM Typespersonnel t WHERE t.typepersdesignation = :typepersdesignation")})
public class Typespersonnel extends OMBase {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "typepersid")
    private Integer typepersid;
   // @Basic(optional = false)
    
    @Column(name = "typepersdesignation")
    private String typepersdesignation;
   /* @OneToMany(cascade = CascadeType.ALL, mappedBy = "typespersonnel")
   
    private Collection<User> userCollection;*/

    public Typespersonnel() {
    }

    public Typespersonnel(Integer typepersid) {
        this.typepersid = typepersid;
    }

    public Typespersonnel(Integer typepersid, String typepersdesignation) {
        this.typepersid = typepersid;
        this.typepersdesignation = typepersdesignation;
    }

    public Integer getTypepersid() {
        return typepersid;
    }

    public void setTypepersid(Integer typepersid) {
        this.typepersid = typepersid;
    }

    public String getTypepersdesignation() {
        return typepersdesignation;
    }

    public void setTypepersdesignation(String typepersdesignation) {
        this.typepersdesignation = typepersdesignation;
    }

   
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (typepersid != null ? typepersid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Typespersonnel)) {
            return false;
        }
        Typespersonnel other = (Typespersonnel) object;
        if ((this.typepersid == null && other.typepersid != null) || (this.typepersid != null && !this.typepersid.equals(other.typepersid))) {
            return false;
        }
        return true;
    }

   
}
