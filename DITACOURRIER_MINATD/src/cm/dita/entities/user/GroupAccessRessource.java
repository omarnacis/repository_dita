/**
 * 
 */
package cm.dita.entities.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
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
import javax.swing.JOptionPane;

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
	
	
	@Embeddable
	public static class Id implements Serializable {
	
		// composantes de la clé composite	
		// pointe sur une Personne
	
		@Column(name = "GROUP_ID")		
		private int groupId;
		
		// pointe sur une Activite
	
		@Column(name = "ACCESSRESSOURCE_ID")		
		private int accessRessourceId;

		public int getGroupId() {
			return groupId;
		}

		public void setGroupId(int groupId) {
			this.groupId = groupId;
		}

		public int getAccessRessourceId() {
			return accessRessourceId;
		}

		public void setAccessRessourceId(int accessRessourceId) {
			this.accessRessourceId = accessRessourceId;
		}
		
		
		// constructeurs	
		
		// getters et setters
		
	
		}
	

	@EmbeddedId
	private Id id = new Id();
	// relation principale PersonneActivite (many) -> Personne (one)
	// implémentée par la clé étrangère : personneId (PersonneActivite (many) -> Personne (one)
	// personneId est en même temps élément de la clé primaire composite
	// JPA ne doit pas gérer cette clé étrangère (insertable = false, updatable = false) car c'est
	
	@ManyToOne
	@JoinColumn(name = "GROUP_ID", insertable = false, updatable = false)
	private Group group;
	
	@ManyToOne
	@JoinColumn(name = "ACCESSRESSOURCE_ID", insertable = false, updatable = false)
	private AccessRessource accessRessource;
	
	


	/*@Id   
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_groupAcess", unique = true, nullable = false)
	private Integer idGroupAcess;*/
	

	/*@ManyToOne
    @JoinColumn(name="access_ressource_id")
    private AccessRessource accessRessource;*/

   /* @ManyToOne
    @JoinColumn(name="group_id")
    private Group group;*/

	
	public AccessRessource getAccessRessource() {
		return accessRessource;
	}

	/*public Integer getIdGroupAcess() {
		return idGroupAcess;
	}

	public void setIdGroupAcess(Integer idGroupAcess) {
		this.idGroupAcess = idGroupAcess;
	}*/

	public void setAccessRessource(AccessRessource accessRessource) {
		this.accessRessource = accessRessource;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	
	
	/*public GroupAccessRessource(AccessRessource accessRessource, Group group) {
		super();
	  //JOptionPane.showMessageDialog(null, group.getIdGroup()+" "+accessRessource.getIdRessource());
		this.setGroup(group);
		this.setAccessRessource(accessRessource);
	
	}*/
	
	

	public Id getId() {
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public GroupAccessRessource() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	 public GroupAccessRessource(Group g, AccessRessource a) {	
	// les clés étrangères sont fixées par l'application
		 //JOptionPane.showMessageDialog(null, g.getIdGroup());
		 getId().setGroupId(g.getIdGroup());	
		 getId().setAccessRessourceId(a.getIdRessource());	
	// associations bidirectionnelles
		 this.setGroup(g);	
		 this.setAccessRessource(a);	
		 g.getRessources().add(this);	
		 a.getGroups().add(this);
	 }
	
	
	
}
