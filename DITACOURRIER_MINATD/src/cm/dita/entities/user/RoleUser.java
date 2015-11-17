/**
 * 
 */
package cm.dita.entities.user;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cm.dita.object.model.IOM;
import cm.dita.object.model.OMBase;



/**
 * @author bruno
 * 
 * 
 */
@Entity
@Table(name = "dita_role_user")
public class RoleUser extends OMBase{
	
    /**
        * 
        */
    private static final long serialVersionUID = 1L;



    @Id   
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_roleUser", unique = true, nullable = false)
	private Integer idRoleUser;

    // JPA ne doit pas gere cette cle car c'est le programme ki initialise cette derniere
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="role_id")
    private Role role;
    

    public RoleUser() {
    }

    public User getUser() {
            return user;
    }

    public void setUser(User user) {
            this.user = user;
    }

    public Role getRole() {
            return role;
    }

    public void setRole(Role role) {
            this.role = role;
    }



    public RoleUser(User user, Role role) {
            super();
            this.setUser(user);
            this.setRole(role);
    }

    
    
    @Override
    public String toString() {
            return "Role_User [id=" + idRoleUser + "]";
    }
    
    
}
