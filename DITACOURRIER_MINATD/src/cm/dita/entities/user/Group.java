/**
 * 
 */
package cm.dita.entities.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import cm.dita.object.model.OMBase;
import cm.dita.annotation.AsignedIdEntity;
import cm.dita.constant.IConstance;

/**
 * @author bruno
 *
 */


@NamedQueries({ @NamedQuery(name = "group.findByName", query = "select g  from Group g  where LOWER(g.groupName) = :nom and g.delate='false'"
        + ""),@NamedQuery(name = "find.groupOfRole", query = "select g  from Group g , RoleGroup rg where rg.role.identifier = :idRole"
                + " and g.idGroup = rg.group.idGroup"),
        @NamedQuery(name = "group.Access", query = "select a from GroupAccessRessource ga left join  ga.accessRessource a where ga.group.idGroup = :identifier "
        		+ " and ga."+IConstance.FIELD_DELETE+"='false'"
                + "")
})
@Entity
@Table(name = "dita_group")
public class Group extends OMBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//@AsignedIdEntity(name = "GRP")
    @Id   
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_group", unique = true, nullable = false)
	private Integer idGroup;

	@Column(name = "group_name")
	private String groupName;
	
	@Column(name = "dateCreation")
	private String dateCreation;
	
	 public Group(){
		 
	 }
	
	 public Group(Group group) {
		super();
		this.idGroup=group.getIdGroup();
		this.groupName = group.groupName;
		this.dateCreation = group.dateCreation;
		this.roles = group.roles;
		this.ressources = group.ressources;
		this.listAccessOfgroup = group.listAccessOfgroup;
		this.setDateCreate(group.getDateCreate());
		this.setIdLastUserUpdate(group.getIdLastUserUpdate());
		this.setIdUserCreate(group.getIdUserCreate());
		this.setIdLastUserUpdate(group.getIdLastUserUpdate());
		
		
	}

	@OneToMany(mappedBy="group",cascade={CascadeType.REMOVE})
	 private Set<RoleGroup> roles = new HashSet<RoleGroup>();

	 
	 @OneToMany(mappedBy="group",cascade={CascadeType.REMOVE})
	 private Set<GroupAccessRessource> ressources = new HashSet<GroupAccessRessource>();
	 
	
	 
	 
	 
	 
	 /**
	  * cette propriy� permet de contenir la liste des noms d'acces du pr�sent groupe 
	  */
	 @Transient
	 private List<String> listAccessOfgroup = new ArrayList<String>();
	 
	

	public Integer getIdGroup() {
		return idGroup;
	}

	public void setIdGroup(Integer idGroup) {
		this.idGroup = idGroup;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Set<RoleGroup> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleGroup> roles) {
		this.roles = roles;
	}

	public Set<GroupAccessRessource> getRessources() {
		return ressources;
	}

	public void setRessources(Set<GroupAccessRessource> ressources) {
		this.ressources = ressources;
	}

	public List<String> getListAccessOfgroup() {
		return listAccessOfgroup;
	}

	public void setListAccessOfgroup(List<String> listAccessOfgroup) {
		this.listAccessOfgroup = listAccessOfgroup;
	}

	public String getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(String dateCreation) {
		this.dateCreation = dateCreation;
	}

		
	
}
