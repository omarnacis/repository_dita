/**
 * 
 */
package cm.dita.dao.domaine.inter.user;

import cm.dita.dao.generic.IDaoBase;
import cm.dita.entities.user.User;


/**
 * @author bruno
 *
 */
public interface IUserDao  extends IDaoBase<User>{
	
	 /**
     * methode utils�e par spring s�curity
     * @param username
     * @return 
     */
    User loadByUserName(String username);
    
    public int getCount();

}
