/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.dita.service.domaine.inter.user;

import java.util.List;

import cm.dita.entities.EspaceCourrier;
import cm.dita.entities.user.User;
import cm.dita.service.generic.IServiceBase;



/**
 *
 * @author bertrand
 */
public interface IUserService  extends IServiceBase<User>{
	
	public void saveUser(User user);
	public List<User> listUserParEspace(int espace);
	public User getUser(int id);
	//public User getUserByLogin(String login);
	public boolean userExiste(User user);
	public int getCount();
	User findByLogin(String login);
	public void updatePasswordAdmin(String password);
	public Integer getCountUserParEspace(int espace);
    
}
