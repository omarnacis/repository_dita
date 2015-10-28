/**
 * 
 */
package cm.dita.entities.user;


/**
 * 
 */


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import cm.dita.object.model.OMBase;
import cm.dita.annotation.AsignedIdEntity;
import cm.dita.entities.Courriers;
import cm.dita.entities.Espace;
import cm.dita.entities.Typespersonnel;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;






/**
 * @author bertrand
 *
 */

@NamedQueries({ @NamedQuery(name = "User.find", query = "select u  from User u , Role  r, RoleUser ru where ru.user.id = u.id and ru.role.identifier = r.identifier and u.login = :login"
        + "") ,        
        		@NamedQuery(name = "User.findByLogin", query = "select u  from User u where LOWER(u.login) = :login and u.delate='false'"
                + ""),
                @NamedQuery(name = "User.getAllAccessForOneRole", query = "select access  from AccessRessource access,Role r,RoleUser ru,User u,RoleGroup rg,GroupAccessRessource ga,Group g"
                		+ " where u.id = :idUser  and ru.user.id = u.id and r.identifier = ru.role.identifier and rg.role.identifier = r.identifier"
                		+ " and rg.group.idGroup = g.idGroup and  ga.group.idGroup = g.idGroup and access.idRessource = ga.accessRessource.idRessource"
                        + ""),
	            @NamedQuery(name = "User.roles", query = "select r  from RoleUser ru left join  ru.role r where ru.user.id = :identifier"
	                    + ""),
                @NamedQuery(name = "User.access", query = "select a from RoleUser ru, RoleGroup rg, GroupAccessRessource ga, AccessRessource a "
                		+ " where ru.role.identifier = rg.role.identifier and rg.group.idGroup = ga.group.idGroup "
                		+ " and ga.accessRessource.idRessource = a.idRessource and ru.user.id = :identifier "
	                    + "")
})
@Entity
@Table(name = "dita_user")
public class User  extends OMBase{
   
    private static final long serialVersionUID = 1L;
    
   // @AsignedIdEntity(name = "USER")
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;
	    
    @Column(name = "date_enable", nullable =true )
    private String date_enable;
    
    @Column(name = "ip_last_connection", nullable =true )
    private String ip_last_connection;
    
    /**
     * The login of the user, used for authentication
     */
    @Column(name = "login",unique=true)
    private String login;
    /**
     * The password of the user, used for authentication
     */
    @Column(name = "password")
    private String password;
    
    /**
     * The creation date of the user
     */
    @Column(name = "creationDate")
    private String dateCreation;
    
    /*@Column(name = "datecreation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datecreation;*/
    
    @Column(name = "enabled")// si true l'user ets bloqu� et ne peut se connect� sinon il peut se connect�
    private boolean enabled;
    
  @Column(name="autorithies", nullable =true ) //pour avoir le max de privileges en super administrateur(=1)
    boolean autorithies;
  
  @Column(name="langue", nullable =true ) 
    private String langue;
  
  @Column(name="isInit") //Permet de savoir si le mot de passe vient d'etre initialisé 
  private boolean Init_pass;
    
    
    @Transient
    private String password1;
    
    @Embedded
    private InfosPersonne infosPersonne;
    
    public User(){
    	
    }
    
    
	
	public User(User u) {
		this.id=u.id;
		this.login = u.login;
		this.password = u.password;
		this.dateCreation = u.dateCreation;
		this.enabled = u.enabled;
		this.autorithies = u.autorithies;
		this.langue = u.langue;
		this.Init_pass = u.Init_pass;
		this.password1 = u.password1;
		this.infosPersonne = u.infosPersonne;
		this.roles = u.roles;
		this.espace = u.espace;
		this.courriersCollection = u.courriersCollection;
	}
	
	



	@OneToMany(mappedBy="user" , cascade={CascadeType.REMOVE}, fetch=FetchType.LAZY)
    private Set<RoleUser> roles = new HashSet<>();
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "espace_id", nullable = false)
    private Espace espace;
    /*
    @JoinColumn(name = "types_id",  nullable = false)
    @ManyToOne(fetch=FetchType.LAZY)
    private Typespersonnel typespersonnel;*/
    
    @OneToMany( mappedBy = "usersender")
    private Collection<Courriers> courriersCollection;
    
	public String getLogin() {
            return login;
    }
    public void setLogin(String login) {
            this.login = login;
    }
    public String getPassword() {
            return password;
    }
    public void setPassword(String password) {
            this.password = password;
    }
   
   
    public String getDateCreation() {
		return dateCreation;
	}
	public void setDateCreation(String dateCreation) {
		this.dateCreation = dateCreation;
	}
	public Set<RoleUser> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleUser> roles) {
        this.roles = roles;
    }

    
    
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public InfosPersonne getInfosPersonne() {
		return infosPersonne;
	}
	public void setInfosPersonne(InfosPersonne infosPersonne) {
		this.infosPersonne = infosPersonne;
	}
	
	public String getPassword1() {
		return password1;
	}
	public void setPassword1(String password1) {
		this.password1 = password1;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	public Espace getEspace() {
		return espace;
	}
	public void setEspace(Espace espace) {
		this.espace = espace;
	}
	
	public Collection<Courriers> getCourriersCollection() {
		return courriersCollection;
	}
	public void setCourriersCollection(Collection<Courriers> courriersCollection) {
		this.courriersCollection = courriersCollection;
	}
	public boolean isAutorithies() {
		return autorithies;
	}
	public void setAutorithies(boolean autorithies) {
		this.autorithies = autorithies;
	}
	public String getLangue() {
		return langue;
	}
	public void setLangue(String langue) {
		this.langue = langue;
	}
	public boolean isInit_pass() {
		return Init_pass;
	}
	public void setInit_pass(boolean init_pass) {
		Init_pass = init_pass;
	}


	/**
	 * @return the date_enable
	 */
	public String getDate_enable() {
		return date_enable;
	}


	/**
	 * @param date_enable the date_enable to set
	 */
	public void setDate_enable(String date_enable) {
		this.date_enable = date_enable;
	}

	
	/**
	 * @return the ip_last_connection
	 */
	public String getIp_last_connection() {
		return ip_last_connection;
	}



	/**
	 * @param ip_last_connection the ip_last_connection to set
	 */
	public void setIp_last_connection(String ip_last_connection) {
		this.ip_last_connection = ip_last_connection;
	}
	
	
}
