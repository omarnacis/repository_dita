package cm.dita.service.domaine.impl;

import java.io.Serializable;

import cm.dita.service.generic.ServiceBaseImpl;
import cm.dita.dao.domaine.inter.IPreferencesDao;
import cm.dita.dao.domaine.inter.IPrioritesDao;
import cm.dita.dao.generic.IDaoBase;
import cm.dita.entities.Priorites;
import cm.dita.service.domaine.inter.IPrioritesRessourceService;

/**
 * @author Omar Nacis
 *
 */

public class PrioritesRessourceServiceImpl  extends ServiceBaseImpl<Priorites>  implements IPrioritesRessourceService{
	
	private static final long serialVersionUID = 1L;
	private IPrioritesDao dao;
	/**
	 * @return the dao
	 */
	public IPrioritesDao getDao() {
		return dao;
	}
	/**
	 * @param dao the dao to set
	 */
	public void setDao(IPrioritesDao dao) {
		this.dao = dao;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return getDao().getCount();
	}
}
