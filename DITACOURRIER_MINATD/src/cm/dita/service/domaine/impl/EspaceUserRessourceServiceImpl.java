package cm.dita.service.domaine.impl;

import java.io.Serializable;


import cm.dita.service.generic.ServiceBaseImpl;
import cm.dita.dao.domaine.inter.IEspaceUserDao;
import cm.dita.dao.generic.IDaoBase;
import cm.dita.entities.EspaceUser;
import cm.dita.service.domaine.inter.IEspaceUserRessourceService;

/**
 * @author Omar Nacis
 *
 */

public class EspaceUserRessourceServiceImpl  extends ServiceBaseImpl<EspaceUser>  implements IEspaceUserRessourceService{
	
	private static final long serialVersionUID = 1L;
	private IEspaceUserDao dao;
	/**
	 * @return the dao
	 */
	public IEspaceUserDao getDao() {
		return dao;
	}
	/**
	 * @param dao the dao to set
	 */
	public void setDao(IEspaceUserDao dao) {
		this.dao = dao;
	}

}
