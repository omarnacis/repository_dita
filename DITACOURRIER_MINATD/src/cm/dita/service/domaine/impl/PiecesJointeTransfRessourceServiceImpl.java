package cm.dita.service.domaine.impl;




import cm.dita.service.generic.ServiceBaseImpl;

import cm.dita.dao.domaine.inter.IPiecesJointeTransfDao;

import cm.dita.entities.PiecesJointeTransf;

import cm.dita.service.domaine.inter.IPiecesJointeTransfRessourceService;

/**
 * @author Omar Nacis
 *
 */

public class PiecesJointeTransfRessourceServiceImpl  extends ServiceBaseImpl<PiecesJointeTransf>  implements IPiecesJointeTransfRessourceService{
	
	private static final long serialVersionUID = 1L;
	private IPiecesJointeTransfDao dao;
	/**
	 * @return the dao
	 */
	public IPiecesJointeTransfDao getDao() {
		return dao;
	}
	/**
	 * @param dao the dao to set
	 */
	public void setDao(IPiecesJointeTransfDao dao) {
		this.dao = dao;
	}

}
