/**
 * 
 */
package cm.dita.entities.user;

import java.util.HashSet;
import java.util.Set;

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

import cm.dita.object.model.OMBase;
import cm.dita.annotation.AsignedIdEntity;

/**
 * @author bruno
 *
 */

@NamedQueries({ @NamedQuery(name = "find.accessGroup", query = "select res  from AccessRessource res, GroupAccessRessource gar  where  gar.group.idGroup = :groupId "
		+ "and res.idRessource = gar.accessRessource.idRessource"
        + "")
})
@Entity
@Table(name = "dita_access_ressource")
public class AccessRessource extends OMBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//@AsignedIdEntity(name = "ARS")
    @Id    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ressource", unique = true, nullable = false)
	private Integer idRessource;

	@Column(name = "ressource_name")
	private String ressourceName;
	
	@Column(name = "ressource_datail")
	private String ressourceDetail;

	@OneToMany(mappedBy="accessRessource",cascade={CascadeType.REMOVE} )
	private Set<GroupAccessRessource> groups = new HashSet<GroupAccessRessource>();

	
	
	public Integer getIdRessource() {
		return idRessource;
	}

	public void setIdRessource(Integer idRessource) {
		this.idRessource = idRessource;
	}

	public String getRessourceName() {
		return ressourceName;
	}

	public void setRessourceName(String ressourceName) {
		this.ressourceName = ressourceName;
	}

	public Set<GroupAccessRessource> getGroups() {
		return groups;
	}

	public void setGroups(Set<GroupAccessRessource> groups) {
		this.groups = groups;
	}

	
	public String getRessourceDetail() {
		return ressourceDetail;
	}

	public void setRessourceDetail(String ressourceDetail) {
		this.ressourceDetail = ressourceDetail;
	}

	public AccessRessource(String detailRessource, String ressourceName) {
		super();
		this.ressourceDetail = detailRessource;
		this.ressourceName = ressourceName;
	}

	public AccessRessource() {
		super();		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((idRessource == null) ? 0 : idRessource.hashCode());
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
		AccessRessource other = (AccessRessource) obj;
		if (idRessource == null) {
			if (other.idRessource != null)
				return false;
		} else if (!idRessource.equals(other.idRessource))
			return false;
		return true;
	}

	
		
}
