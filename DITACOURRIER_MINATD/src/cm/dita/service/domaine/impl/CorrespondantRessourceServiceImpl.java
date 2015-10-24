/**
 * 
 */
package cm.dita.service.domaine.impl;


import java.util.Hashtable;
import java.util.Map;

import cm.dita.dao.domaine.inter.ICorrespondantDao;
import cm.dita.entities.Correspondant;
import cm.dita.entities.user.User;
import cm.dita.service.domaine.inter.ICorrespondantRessourceService;
import cm.dita.service.generic.ServiceBaseImpl;

/**
 * @author Omar Nacis
 *
 */
public class CorrespondantRessourceServiceImpl extends ServiceBaseImpl<Correspondant>  implements ICorrespondantRessourceService{
	
	private static final long serialVersionUID = 1L;
	private ICorrespondantDao dao;
	/**
	 * @return the dao
	 */
	public ICorrespondantDao getDao() {
		return dao;
	}
	/**
	 * @param dao the dao to set
	 */
	public void setDao(ICorrespondantDao dao) {
		this.dao = dao;
	}
	
	// verifie l'existance d'un nom
    
	   public  boolean correspondantExisteWithName(Correspondant correspondant){
	    	
		   Map<String , Object> map=new Hashtable<String,Object>();
			map.put("nom", correspondant.getNom().toLowerCase());
			Correspondant correspondant1= dao.executeNameQuery("Correspondant.findByNom", map);
	    		if(correspondant1 == null) // n'existe pas
	    			return false;
	    		else //existe
	    			if(correspondant1.getId().equals(correspondant.getId()))// appartient au meme correspondant
	    				return false;
	    			return true;
	    	
	    }
	
	
}
