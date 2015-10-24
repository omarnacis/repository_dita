/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cm.dita.entities;

import cm.dita.object.model.OMBase;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Bessala
 */
@Entity
@Table(name = "dita_alarmes")
@XmlRootElement
/*@NamedQueries({
    @NamedQuery(name = "Alarmes.findAll", query = "SELECT a FROM Alarmes a"),
    @NamedQuery(name = "Alarmes.findById", query = "SELECT a FROM Alarmes a WHERE a.id = :id"),
    @NamedQuery(name = "Alarmes.findByAlarmdate", query = "SELECT a FROM Alarmes a WHERE a.alarmdate = :alarmdate"),
    @NamedQuery(name = "Alarmes.findByAlarmetat", query = "SELECT a FROM Alarmes a WHERE a.alarmetat = :alarmetat"),
    @NamedQuery(name = "Alarmes.findByAlarmtype", query = "SELECT a FROM Alarmes a WHERE a.alarmtype = :alarmtype")})*/
public class Alarmes extends OMBase {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
  
    @Column(name = "alarmdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date alarmdate; //date de l'alarme
    @Basic(optional = false)
  
  
  
    @Column(name = "nbrEssai")
    private int nbrEssai; // nbre d'essai
    @Basic(optional = false)
  
    @Column(name = "userIdSend",nullable=true)
    private int user_sender; //utilisateur qui enregistre le courrier
    @Basic(optional = false)
    
    @Column(name = "userIdRecv",nullable=true)
    private int user_recv; //utilisateur a qui on envoi
    @Basic(optional = false)
    
    @Column(name = "email")
    private String email; //adresse email a envoyé
    @Basic(optional = false)
    
    @Column(name = "objet",length=300)
    private String objet; //objet du mail
    @Basic(optional = false)
    
    @Column(name = "corps",length=2000)
    private String corps; //corps du mail
    @Basic(optional = false)
    
    @Column(name = "user_id")
    private int user_id; //id de l'utilisateur qui enregistre
    @Basic(optional = false)
  
    
    @Column(name = "alarmetat")
    private boolean alarmetat; //Indiquant si l'envoi est ok
    @Basic(optional = false)
  
    @Column(name = "alarmtype")
    private int alarmtype; //type d'alarmes

    public Alarmes() {
    }

    public Alarmes(Integer id) {
        this.id = id;
    }

    public Alarmes(Integer id, Date alarmdate, short alarmetat, int alarmtype) {
        this.id = id;
        this.alarmdate = alarmdate;
        //this.alarmadresse = alarmadresse;
        
        //this.alarmetat = alarmetat;
        this.alarmtype = alarmtype;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getAlarmdate() {
        return alarmdate;
    }

    public void setAlarmdate(Date alarmdate) {
        this.alarmdate = alarmdate;
    }

    

   
    public String getObjet() {
		return objet;
	}

	public void setObjet(String objet) {
		this.objet = objet;
	}

	public String getCorps() {
		return corps;
	}

	public void setCorps(String corps) {
		this.corps = corps;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getNbrEssai() {
		return nbrEssai;
	}

	public void setNbrEssai(int nbrEssai) {
		this.nbrEssai = nbrEssai;
	}

	public int getUser_sender() {
		return user_sender;
	}

	public void setUser_sender(int user_sender) {
		this.user_sender = user_sender;
	}

	public int getUser_recv() {
		return user_recv;
	}

	public void setUser_recv(int user_recv) {
		this.user_recv = user_recv;
	}

	public boolean isAlarmetat() {
		return alarmetat;
	}

	public void setAlarmetat(boolean alarmetat) {
		this.alarmetat = alarmetat;
	}

	public int getAlarmtype() {
        return alarmtype;
    }

    public void setAlarmtype(int alarmtype) {
        this.alarmtype = alarmtype;
    }
    
    

    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
        if (!(object instanceof Alarmes)) {
            return false;
        }
        Alarmes other = (Alarmes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cm.dita.entities.Alarmes[ id=" + id + " ]";
    }
    
}
