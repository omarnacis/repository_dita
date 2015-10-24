/**
 * 
 */
package cm.dita.dao.domaine.inter.user;

import cm.dita.dao.generic.IDaoBase;
import cm.dita.entities.user.Parameters;
import cm.dita.exception.ApplicationException;

/**
 * @author bertrand
 *
 */
public interface IParametersDao extends IDaoBase<Parameters> {

	Parameters getParameterByName(String nom) throws ApplicationException;

	int getCount() throws ApplicationException;
}
