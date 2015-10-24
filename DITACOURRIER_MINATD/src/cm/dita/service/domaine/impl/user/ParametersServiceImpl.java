/**
 * 
 */
package cm.dita.service.domaine.impl.user;

import java.util.List;




import javax.persistence.EntityTransaction;

import org.springframework.transaction.annotation.Transactional;

import cm.dita.dao.domaine.inter.user.IParametersDao;
import cm.dita.entities.user.Parameters;
import cm.dita.exception.ApplicationException;
import cm.dita.service.domaine.inter.user.IParametersService;
import cm.dita.service.generic.ServiceBaseImpl;



/**
 * @author bruno
 *
 */
public class ParametersServiceImpl extends ServiceBaseImpl<Parameters>
		implements IParametersService {

	private IParametersDao dao;
	/* (non-Javadoc)
	 * @see cm.socogel.service.generic.IServiceBase#getDao()
	 */
	@Override
	public IParametersDao getDao() {
		// TODO Auto-generated method stub
		return dao;
	}
	public void setDao(IParametersDao dao) {
		this.dao = dao;
	}
	@Override
	public Parameters getParameterByName(String nameParameter) {
		// TODO Auto-generated method stub
		return getDao().getParameterByName(nameParameter);
	}

	
	@Override
	public int getCount() throws ApplicationException {
		// TODO Auto-generated method stub
		return getDao().getCount();
	}
	
	@Transactional
	public void saveAllParametres(List<Parameters> listParameters) throws ApplicationException{
		

		
		try {
			for(Parameters parameters : listParameters){					
				getDao().save(parameters);
			}
		
		} catch (Exception e) {
		
			throw new ApplicationException("Probl�me lors du persist de la liste de "+getDao().getOMClass().getSimpleName(), e, 11);
		}
	}

}
