/**
 * 
 */
package cm.dita.service.domaine.inter.user;

import java.util.List;

import cm.dita.entities.user.Role;
import cm.dita.entities.user.RoleGroup;
import cm.dita.entities.user.User;
import cm.dita.exception.ApplicationException;
import cm.dita.service.generic.IServiceBase;



/**
 * @author bruno
 *
 */
public interface IRoleService extends IServiceBase<Role>{

	int getCount();

	void saveRoleWithGroups(Role role) throws ApplicationException;
	void deleteGroupRole(String identifier);
	public  boolean userExiste(Role role);
	List<Role> listOfRole4User(User user);
	public void deleteVersusDesabled(Role role) throws ApplicationException;
	
}
