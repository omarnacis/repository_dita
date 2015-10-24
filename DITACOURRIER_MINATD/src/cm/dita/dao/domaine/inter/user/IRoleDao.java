/**
 * 
 */
package cm.dita.dao.domaine.inter.user;

import cm.dita.dao.generic.IDaoBase;
import cm.dita.entities.user.Role;


/**
 * @author bertrand
 *
 */
public interface IRoleDao  extends IDaoBase<Role>{

	public int getCount();
}
