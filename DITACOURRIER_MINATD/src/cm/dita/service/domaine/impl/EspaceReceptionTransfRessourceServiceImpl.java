package cm.dita.service.domaine.impl;

import java.io.Serializable;





import cm.dita.service.generic.ServiceBaseImpl;

import cm.dita.dao.domaine.inter.IEspaceReceptionTransfDao;

import cm.dita.entities.EspaceReceptionTransf;

import cm.dita.service.domaine.inter.IEspaceReceptionTransfRessourceService;

/**
 * @author Omar Nacis
 *
 */

public class EspaceReceptionTransfRessourceServiceImpl  extends ServiceBaseImpl<EspaceReceptionTransf>  implements IEspaceReceptionTransfRessourceService{
	
	private static final long serialVersionUID = 1L;
	private IEspaceReceptionTransfDao dao;
	/**
	 * @return the dao
	 */
	public IEspaceReceptionTransfDao getDao() {
		return dao;
	}
	/**
	 * @param dao the dao to set
	 */
	public void setDao(IEspaceReceptionTransfDao dao) {
		this.dao = dao;
	}

}
