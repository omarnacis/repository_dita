/**
 * 
 */
package cm.dita.dao.domaine.impl.user;


import cm.dita.dao.domaine.inter.user.IAccessRessourceDao;
import cm.dita.dao.generic.DaoBaseImpl;
import cm.dita.entities.user.AccessRessource;


/**
 * @author bruno
 *
 */
public class AccessRessourceDaoImpl extends DaoBaseImpl<AccessRessource>
		implements IAccessRessourceDao {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int getCount(){
		return ((Long)(getCurrentSession().createQuery("select count(*) from AccessRessource").getResultList().get(0))).intValue();
	}
	

}
