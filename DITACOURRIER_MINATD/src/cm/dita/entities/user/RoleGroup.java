/**
 * 
 */
package cm.dita.entities.user;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cm.dita.object.model.OMBase;

/**
 * @author bruno
 *
 */
@Entity
@Table(name = "dita_role_group")
public class RoleGroup extends OMBase {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id   
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_roleGroup", unique = true, nullable = false)
	private Integer idRoleGroup;
	
	@ManyToOne
    @JoinColumn(name="role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name="group_id")
    private Group group;

	

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public RoleGroup(Role role, Group group) {
		super();
		this.setRole(role);
		this.setGroup(group);
	}

	public RoleGroup() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
    

}
