/**
 * 
 */
package cm.dita.service.domaine.inter.user;

import java.util.List;

import cm.dita.entities.user.Parameters;
import cm.dita.exception.ApplicationException;
import cm.dita.service.generic.IServiceBase;



/**
 * @author bruno
 *
 */
public interface IParametersService extends IServiceBase<Parameters> {

	Parameters getParameterByName(String nameParameter);
	
	int getCount() throws ApplicationException;
	
	void saveAllParametres(List<Parameters> listParameters) throws ApplicationException;
}
