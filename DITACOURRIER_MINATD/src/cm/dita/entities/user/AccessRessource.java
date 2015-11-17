/**
 * 
 */
package cm.dita.entities.user;

import java.util.HashSet;
import java.util.List;
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
import javax.persistence.Transient;

import cm.dita.object.model.OMBase;
import cm.dita.annotation.AsignedIdEntity;

/**
 * @author bruno
 *
 */


@NamedQueries({ @NamedQuery(name = "find.accessGroup", query = "select res  from AccessRessource res, GroupAccessRessource gar  where  gar.group.idGroup = :groupId "
		+ "and res.idRessource = gar.accessRessource.idRessource"),
		@NamedQuery(name = "access.list_bloc", query = "select res  from AccessRessource res where  res.code_action = 0"),
		@NamedQuery(name = "access.list_access_du_bloc", query = "select res  from AccessRessource res where  res.code_bloc =:code_bloc and res.code_action !=0"),
		@NamedQuery(name = "access.list_access_du_bloc_select", query = "select res  from AccessRessource res , GroupAccessRessource gares where res.idRessource=gares.accessRessource.idRessource and gares.group.idGroup=:idgroupe and (res.code_bloc =:code_bloc and res.code_action !=0)")
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
	
	@Column(name = "code_bloc")
	private int code_bloc;
	@Column(name = "code_action")
	private int code_action;
	
	@Transient
	private List<AccessRessource> listRessourceDuBloc;
	
	@Transient
	private List<String> selectRessourceDuBloc;

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
	

	public int getCode_bloc() {
		return code_bloc;
	}

	public void setCode_bloc(int code_bloc) {
		this.code_bloc = code_bloc;
	}

	public int getCode_action() {
		return code_action;
	}

	public void setCode_action(int code_action) {
		this.code_action = code_action;
	}

	public List<AccessRessource> getListRessourceDuBloc() {
		return listRessourceDuBloc;
	}

	public void setListRessourceDuBloc(List<AccessRessource> listRessourceDuBloc) {
		this.listRessourceDuBloc = listRessourceDuBloc;
	}
	
	
	
	
	public List<String> getSelectRessourceDuBloc() {
		return selectRessourceDuBloc;
	}

	public void setSelectRessourceDuBloc(List<String> selectRessourceDuBloc) {
		this.selectRessourceDuBloc = selectRessourceDuBloc;
	}

	public AccessRessource(Integer idRessource ) {
		super();
		this.idRessource = idRessource;
		
	}
	
	public AccessRessource(String detailRessource, String ressourceName) {
		super();
		this.ressourceDetail = detailRessource;
		this.ressourceName = ressourceName;
	}
	
	public AccessRessource(Object detailRessource, Object ressourceName,String bloc,String action) {
		super();
		this.ressourceDetail = (String) detailRessource;
		this.ressourceName = (String)ressourceName;
		this.code_bloc=Integer.parseInt(bloc);
		this.code_action=Integer.parseInt(action);
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
	@Override
	public String toString() {
		return "AccessRessource [id_Ressource=" + idRessource
				+ ", Nom ressource=" + ressourceName + ", detail sur la ressource="
				+ ressourceDetail + ", liste groupe ayant la ressource=" + groups + 
				" , code bloc="+code_bloc+" , code action ="+code_action+"]";
	}
	
	
	

	
		
}
