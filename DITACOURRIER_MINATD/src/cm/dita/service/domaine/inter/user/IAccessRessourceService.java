/**
 * 
 */
package cm.dita.service.domaine.inter.user;

import java.util.List;

import cm.dita.entities.user.AccessRessource;
import cm.dita.entities.user.Group;
import cm.dita.entities.user.User;
import cm.dita.exception.ApplicationException;
import cm.dita.service.generic.IServiceBase;



/**
 * @author bruno
 *
 */
public interface IAccessRessourceService extends IServiceBase<AccessRessource>  {

	int getCount();
	
	void saveAllRessource(List<AccessRessource> list) throws ApplicationException;
	public List<AccessRessource> listOfAccess4Group(Group group);
	public List<AccessRessource> listAccess2User(User user)throws ApplicationException;
	public List<AccessRessource> listBlocAccess()throws ApplicationException;
	public List<AccessRessource> listAccessDuBloc(int code_bloc)throws ApplicationException;
	public List<String> listAccessDuBlocSelect(int idgroupe,int code_bloc,int colone)throws ApplicationException;
}
