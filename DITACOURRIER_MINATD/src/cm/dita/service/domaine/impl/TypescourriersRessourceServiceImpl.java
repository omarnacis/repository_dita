package cm.dita.service.domaine.impl;

import java.io.Serializable;

import cm.dita.service.generic.ServiceBaseImpl;
import cm.dita.dao.domaine.inter.IStatutsDao;
import cm.dita.dao.domaine.inter.ITypescourriersDao;
import cm.dita.dao.generic.IDaoBase;
import cm.dita.entities.Typescourriers;
import cm.dita.service.domaine.inter.ITypescourriersRessourceService;

/**
 * @author Omar Nacis
 *
 */

public class TypescourriersRessourceServiceImpl  extends ServiceBaseImpl<Typescourriers>  implements ITypescourriersRessourceService{
	
	private static final long serialVersionUID = 1L;
	private ITypescourriersDao dao;
	/**
	 * @return the dao
	 */
	public ITypescourriersDao getDao() {
		return dao;
	}
	/**
	 * @param dao the dao to set
	 */
	public void setDao(ITypescourriersDao dao) {
		this.dao = dao;
	}
	


}
