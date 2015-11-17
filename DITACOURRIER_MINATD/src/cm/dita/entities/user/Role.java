/**
 * 
 */
package cm.dita.entities.user;
/**
 * 
 */


import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import cm.dita.listener.EntiteListener;
import cm.dita.object.model.OMBase;
import cm.dita.annotation.AsignedIdEntity;


/**
 * @author bruno
 *
 */
@NamedQueries({@NamedQuery(name = "role.findByName", query = "select r  from Role r where LOWER(r.roleName) = :nomRole and r.delate='false'"
                + ""),
                @NamedQuery(name = "role.deleteGroup", query = "delete from RoleGroup rg where rg.role.identifier = :identifier"
                        + ""),
                @NamedQuery(name = "role.Groups", query = "select g from RoleGroup rg left join  rg.group g where rg.role.identifier = :identifier"
                        + "")
})
//
@Entity
@Table(name="dita_role")

public class Role extends OMBase {

    /**
        * 
        */
    private static final long serialVersionUID = 1L;

    
   // @AsignedIdEntity(name = "ROLE")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identifier", unique = true, nullable = false)
    private Integer identifier;
	
    
    @Column(name = "role_name")
    private String roleName;
    
    @Column(name = "dateCreation")
    private String dateCreation;

   @OneToMany(mappedBy="role",cascade={CascadeType.REMOVE},fetch=FetchType.LAZY)
    private Set<RoleUser> users = new HashSet<RoleUser>();

   
   @OneToMany(mappedBy="role",cascade={CascadeType.REMOVE,CascadeType.PERSIST},fetch=FetchType.LAZY)
   private Set<RoleGroup> groups = new HashSet<RoleGroup>();
   
 
   
    public Role() {
    }
    
    


    public Role(Role role) {
		//super();
    	this.identifier=role.getIdentifier();
		this.roleName = role.roleName;
		this.dateCreation = role.dateCreation;
		this.users = role.users;
		this.groups = role.groups;
		this.setDateCreate(role.getDateCreate());
		this.setIdLastUserUpdate(role.getIdLastUserUpdate());
		this.setIdUserCreate(role.getIdUserCreate());
		this.setIdLastUserUpdate(role.getIdLastUserUpdate());
	}




	public String getRoleName() {
            return roleName;
    }

    public void setRoleName(String roleName) {
            this.roleName = roleName;
    }      

    public Role(String roleName) {
        this.roleName = roleName;
    }

    public Set<RoleUser> getUsers() {
        return users;
    }

    public void setUsers(Set<RoleUser> users) {
        this.users = users;
    }


    
	public Integer getIdentifier() {
		return identifier;
	}


	public void setIdentifier(Integer identifier) {
		this.identifier = identifier;
	}
	
	


	public Set<RoleGroup> getGroups() {
		return groups;
	}


	public void setGroups(Set<RoleGroup> groups) {
		this.groups = groups;
	}

	

	

	public String getDateCreation() {
		return dateCreation;
	}


	public void setDateCreation(String dateCreation) {
		this.dateCreation = dateCreation;
	}


	@Override
	public String toString() {
		return "Role [roleName=" + roleName + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((identifier == null) ? 0 : identifier.hashCode());
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
		Role other = (Role) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}	
    
	
	
    
}

