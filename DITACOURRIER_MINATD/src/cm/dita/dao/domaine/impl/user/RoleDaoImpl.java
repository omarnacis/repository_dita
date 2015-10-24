/**
 * 
 */
package cm.dita.dao.domaine.impl.user;

import cm.dita.dao.domaine.inter.user.IRoleDao;
import cm.dita.dao.generic.DaoBaseImpl;
import cm.dita.entities.user.Role;


/**
 * @author bruno
 *
 */
public class RoleDaoImpl extends DaoBaseImpl<Role> implements IRoleDao{

	
	@Override
	public int getCount(){
		return ((Long)(getCurrentSession().createQuery("select count(*) from Role").getResultList().get(0))).intValue();
	}
}
