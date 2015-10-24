/**
 * 
 */
package cm.dita.dao.domaine.impl.user;

import java.util.Hashtable;
import java.util.Map;

import javax.persistence.Query;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import cm.dita.dao.domaine.inter.user.IUserDao;
import cm.dita.dao.generic.DaoBaseImpl;
import cm.dita.entities.user.User;



/**
 * @author bruno
 *
 */
public class UserDaoImpl extends DaoBaseImpl<User> implements IUserDao {

	 @Override
	    public User loadByUserName(String username) {
	        DetachedCriteria criteria = DetachedCriteria.forClass(getOMClass());
	        criteria.add(Restrictions.eq("login", username));
	        
	        User user =null;// getEntityManager()..eexecuteObjectCriteria(criteria);
	        
	           
	        return user;
	    }
	 
	

	
	
	@Override
	public int getCount(){
		return ((Long)(getCurrentSession().createQuery("select count(*) from User").getResultList().get(0))).intValue();
	}


}
