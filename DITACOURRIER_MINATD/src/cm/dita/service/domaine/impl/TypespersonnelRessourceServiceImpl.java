package cm.dita.service.domaine.impl;

import java.io.Serializable;

import cm.dita.service.generic.ServiceBaseImpl;
import cm.dita.dao.domaine.inter.ITypescourriersDao;
import cm.dita.dao.domaine.inter.ITypespersonnelDao;
import cm.dita.dao.generic.IDaoBase;
import cm.dita.entities.Typespersonnel;
import cm.dita.service.domaine.inter.ITypespersonnelRessourceService;

/**
 * @author Omar Nacis
 *
 */

public class TypespersonnelRessourceServiceImpl  extends ServiceBaseImpl<Typespersonnel>  implements ITypespersonnelRessourceService{
	
	private static final long serialVersionUID = 1L;
	private ITypespersonnelDao dao;
	/**
	 * @return the dao
	 */
	public ITypespersonnelDao getDao() {
		return dao;
	}
	/**
	 * @param dao the dao to set
	 */
	public void setDao(ITypespersonnelDao dao) {
		this.dao = dao;
	}


}
