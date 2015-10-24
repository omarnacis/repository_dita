package cm.dita.service.domaine.impl;

import java.io.Serializable;


import cm.dita.service.generic.ServiceBaseImpl;
import cm.dita.dao.domaine.inter.IActivitesDao;
import cm.dita.dao.generic.IDaoBase;
import cm.dita.entities.Activites;
import cm.dita.service.domaine.inter.IActivitesRessourceService;

/**
 * @author Omar Nacis
 *
 */

public class ActivitesRessourceServiceImpl  extends ServiceBaseImpl<Activites>  implements IActivitesRessourceService{
	
	private static final long serialVersionUID = 1L;
	private IActivitesDao dao;
	/**
	 * @return the dao
	 */
	public IActivitesDao getDao() {
		return dao;
	}
	/**
	 * @param dao the dao to set
	 */
	public void setDao(IActivitesDao dao) {
		this.dao = dao;
	}

}
