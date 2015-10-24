/**
 * 
 */
package cm.dita.service.domaine.inter.user;

import java.util.List;

import cm.dita.entities.user.Group;
import cm.dita.entities.user.Role;
import cm.dita.entities.user.RoleGroup;
import cm.dita.exception.ApplicationException;
import cm.dita.service.generic.IServiceBase;



/**
 * @author bruno
 *
 */
public interface IGroupService extends IServiceBase<Group> {

	void saveAllRessourcesOfOneGroup(Group group) throws ApplicationException;
	public List<Group> listOfGroup4Role(Role role);
	public void deleteVersusDesabled(Group group) throws ApplicationException;
	public  boolean userExiste(Group group);
}
