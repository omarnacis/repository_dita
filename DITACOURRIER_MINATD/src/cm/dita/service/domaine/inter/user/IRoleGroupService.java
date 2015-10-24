/**
 * 
 */
package cm.dita.service.domaine.inter.user;

import java.util.List;
import java.util.Set;

import cm.dita.entities.user.Group;
import cm.dita.entities.user.Role;
import cm.dita.entities.user.RoleGroup;
import cm.dita.exception.ApplicationException;
import cm.dita.service.generic.IServiceBase;



/**
 * @author bruno
 *
 */
public interface IRoleGroupService extends IServiceBase<RoleGroup> {

	void deleteAllGroupOfRole(String identifier) throws ApplicationException;

	void saveAll(Set<RoleGroup> groups)throws ApplicationException;
	
	void saveGroup2Role(Role role,List<Group> seletedGroups);
	public void deleteGroup2Role(Role role);

}
