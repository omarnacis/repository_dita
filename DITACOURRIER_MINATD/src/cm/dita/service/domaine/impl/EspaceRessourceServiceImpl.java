package cm.dita.service.domaine.impl;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

import cm.dita.service.generic.ServiceBaseImpl;
import cm.dita.dao.domaine.inter.ICourriersDao;
import cm.dita.dao.domaine.inter.IEspaceCourrierDao;
import cm.dita.dao.domaine.inter.IEspaceDao;
import cm.dita.dao.generic.IDaoBase;
import cm.dita.entities.Espace;
import cm.dita.entities.user.Group;
import cm.dita.service.domaine.inter.IEspaceRessourceService;

/**
 * @author Omar Nacis
 *
 */

public class EspaceRessourceServiceImpl  extends ServiceBaseImpl<Espace>  implements IEspaceRessourceService{
	
	private static final long serialVersionUID = 1L;
	private IEspaceDao dao;
	/**
	 * @return the dao
	 */
	
	public  boolean userExiste(Espace espace){
    	
		   Map<String , Object> map=new Hashtable<String,Object>();
			map.put("nomespace", espace.getNomespace().toLowerCase());
			Espace e= dao.executeNameQuery("Espace.findByNomespace", map);
	    		if(e == null) // n'existe pas
	    			return false;
	    		else //existe
	    			if(e.getId().equals(espace.getId()))// appartient au meme utilisateur
	    				return false;
	    			return true;
	    	
	    }
	
	public IEspaceDao getDao() {
		return dao;
	}
	/**
	 * @param dao the dao to set
	 */
	public void setDao(IEspaceDao dao) {
		this.dao = dao;
	}
	

}
