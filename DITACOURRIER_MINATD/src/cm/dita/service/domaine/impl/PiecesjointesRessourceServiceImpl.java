package cm.dita.service.domaine.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cm.dita.service.generic.ServiceBaseImpl;
import cm.dita.dao.domaine.inter.IMouchardDao;
import cm.dita.dao.domaine.inter.IPiecesjointesDao;
import cm.dita.dao.generic.IDaoBase;
import cm.dita.entities.Courriers;
import cm.dita.entities.Piecesjointes;
import cm.dita.service.domaine.inter.IPiecesjointesRessourceService;

/**
 * @author Omar Nacis
 *
 */

public class PiecesjointesRessourceServiceImpl  extends ServiceBaseImpl<Piecesjointes>  implements IPiecesjointesRessourceService{
	
	private static final long serialVersionUID = 1L;
	private IPiecesjointesDao dao;
	/**
	 * @return the dao
	 */
	public IPiecesjointesDao getDao() {
		return dao;
	}
	/**
	 * @param dao the dao to set
	 */
	public void setDao(IPiecesjointesDao dao) {
		this.dao = dao;
	}
	
	public List<Piecesjointes> pieceJointes2Courrier(Courriers courrier){
		
		return dao.getCurrentSession().createQuery("select p  from Piecesjointes p where p.courrier.id = :id").setParameter("id", courrier.getId()).getResultList();
		//getResultList().get(0)).intValue();
		
	}
	
	public List<Piecesjointes> ListPieceJointesNonSupprimeDunCourrier(Courriers courrier){
		// TODO Auto-generated method
    	Map<String , Object> parameters = new HashMap<String, Object>();
		parameters.put("courrierid", courrier.getId());
		
		
		List<Piecesjointes> piecesjointes = getDao().executeNameQueryAndGetListResult("Piecesjointes.findByCourrierid", parameters);
		
		if(piecesjointes != null && piecesjointes.size()> 0){
			 
			 return piecesjointes;
		 }
		 
		 return null;
	}
	
	

}
