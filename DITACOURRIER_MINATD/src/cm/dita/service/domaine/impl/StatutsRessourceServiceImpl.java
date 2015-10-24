package cm.dita.service.domaine.impl;

import java.io.Serializable;

import cm.dita.service.generic.ServiceBaseImpl;
import cm.dita.dao.domaine.inter.IStatutsDao;
import cm.dita.dao.generic.IDaoBase;
import cm.dita.entities.Statuts;
import cm.dita.service.domaine.inter.IStatutsRessourceService;

/**
 * @author Omar Nacis
 *
 */

public class StatutsRessourceServiceImpl  extends ServiceBaseImpl<Statuts>  implements IStatutsRessourceService{
	
	private static final long serialVersionUID = 1L;
	private IStatutsDao dao;
	public IStatutsDao getDao() {
		return dao;
	}
	public void setDao(IStatutsDao dao) {
		this.dao = dao;
	}
	
	


	    

}
