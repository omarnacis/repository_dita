/**
 * 
 */
package cm.dita.service.domaine.inter.user;

import java.util.List;
import java.util.Set;

import cm.dita.entities.user.AccessRessource;
import cm.dita.entities.user.Group;
import cm.dita.entities.user.Role;
import cm.dita.entities.user.RoleUser;
import cm.dita.entities.user.User;
import cm.dita.exception.ApplicationException;
import cm.dita.service.generic.IServiceBase;



/**
 * @author bertrand
 *
 */
public interface IRoleUserService extends IServiceBase<RoleUser> {

	void saveAll(Set<RoleUser> roles) throws ApplicationException;

	void deleteAllRoleOfUser(String identifier) throws ApplicationException;
	
	public void saveRole2User(User user, List<Role> seletedRole);
	public void deleteRole2User(User user);

}
