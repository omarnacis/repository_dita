/**
 * 
 */
package cm.dita.dao.domaine.inter.user;

import cm.dita.dao.generic.IDaoBase;
import cm.dita.entities.user.AccessRessource;


/**
 * @author bertrand
 *
 */
public interface IAccessRessourceDao extends IDaoBase<AccessRessource> {

	public int getCount();
}

