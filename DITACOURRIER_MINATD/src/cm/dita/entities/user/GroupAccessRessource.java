/**
 * 
 */
package cm.dita.entities.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import cm.dita.object.model.OMBase;

/**
 * @author bruno
 *
 */

@NamedQueries({ @NamedQuery(name = "GroupAccess.delete", query = "delete  from GroupAccessRessource gr  where gr.group.idGroup = :idGroup"
        + "") })

@Entity
@Table(name = "dita_group_access_ressource")
public class GroupAccessRessource extends OMBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id   
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_groupAcess", unique = true, nullable = false)
	private Integer idGroupAcess;
	

	@ManyToOne
    @JoinColumn(name="access_ressource_id")
    private AccessRessource accessRessource;

    @ManyToOne
    @JoinColumn(name="group_id")
    private Group group;

	
	public AccessRessource getAccessRessource() {
		return accessRessource;
	}

	public Integer getIdGroupAcess() {
		return idGroupAcess;
	}

	public void setIdGroupAcess(Integer idGroupAcess) {
		this.idGroupAcess = idGroupAcess;
	}

	public void setAccessRessource(AccessRessource accessRessource) {
		this.accessRessource = accessRessource;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public GroupAccessRessource(AccessRessource accessRessource, Group group) {
		super();
	  
		this.setGroup(group);
		this.setAccessRessource(accessRessource);
	
	}
	
	

	public GroupAccessRessource() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
