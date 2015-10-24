/**
 * 
 */
package cm.dita.dao.domaine.impl.user;

import java.util.List;




import javax.persistence.Query;

import cm.dita.dao.domaine.inter.user.IParametersDao;
import cm.dita.dao.generic.DaoBaseImpl;
import cm.dita.entities.user.Parameters;
import cm.dita.exception.ApplicationException;



/**
 * @author bruno
 *
 */
public class ParametersDaoImpl extends DaoBaseImpl<Parameters> implements
		IParametersDao {

	@Override
	public Parameters getParameterByName(String nom)
			throws ApplicationException {
		Query query =  getEntityManager().createNamedQuery("select p from Parameters p where p.parameterName = :name");
		query.setParameter("name", nom);
		
		List list = query.getResultList();
		
		if(list != null && !list.isEmpty()){
			return  (Parameters) list.get(0);
		}		
		return null;
	}

	
	@Override
	public int getCount() 
		throws ApplicationException {		
			return ((Long)(getCurrentSession().createQuery("select count(*) from Parameters").getResultList().get(0))).intValue();
	}
	

}
