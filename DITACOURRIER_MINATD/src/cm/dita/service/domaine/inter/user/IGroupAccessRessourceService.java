/**
 * 
 */
package cm.dita.service.domaine.inter.user;

import java.util.List;
import java.util.Set;

import cm.dita.entities.user.AccessRessource;
import cm.dita.entities.user.Group;
import cm.dita.entities.user.GroupAccessRessource;
import cm.dita.exception.ApplicationException;
import cm.dita.service.generic.IServiceBase;



/**
 * @author bruno
 *
 */
public interface IGroupAccessRessourceService extends
		IServiceBase<GroupAccessRessource> {
	
	public void createGroupAndAccess(Group group, List<AccessRessource> listRessoureBlocs);
	public void updateGroupAndAccess(Group group, List<AccessRessource> listRessoureBlocs);

	/*void saveAll(Set<GroupAccessRessource> groupAccessRessources)
			throws ApplicationException;

	public void saveAcess2Group(Group group, List<AccessRessource> seletedAccess);
	public void deleteAccess2Group(Group group);*/
	
	//public void saveAll(Set<GroupAccessRessource> groupAccessRessources) throws ApplicationException ;

	//public void deleteAllAccessGroup(String idGroup) throws ApplicationException;
	
	
}
