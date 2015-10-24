package cm.dita.service.domaine.impl;

import java.io.Serializable;

import cm.dita.service.generic.ServiceBaseImpl;
import cm.dita.dao.domaine.inter.ITypespersonnelDao;
import cm.dita.dao.domaine.inter.ITypespiecesjointesDao;
import cm.dita.dao.generic.IDaoBase;
import cm.dita.entities.Typespiecesjointes;
import cm.dita.service.domaine.inter.ITypespiecesjointesRessourceService;

/**
 * @author Omar Nacis
 *
 */

public class TypespiecesjointesRessourceServiceImpl  extends ServiceBaseImpl<Typespiecesjointes>  implements ITypespiecesjointesRessourceService{
	
	private static final long serialVersionUID = 1L;
	private ITypespiecesjointesDao dao;
	/**
	 * @return the dao
	 */
	public ITypespiecesjointesDao getDao() {
		return dao;
	}
	/**
	 * @param dao the dao to set
	 */
	public void setDao(ITypespiecesjointesDao dao) {
		this.dao = dao;
	}


}
