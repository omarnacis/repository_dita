package cm.dita.service.domaine.impl;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import cm.dita.service.generic.ServiceBaseImpl;
import cm.dita.dao.domaine.inter.ICourriersDao;
import cm.dita.dao.domaine.inter.IEspaceCourrierDao;
import cm.dita.dao.domaine.inter.IEspaceDao;
import cm.dita.dao.generic.IDaoBase;
import cm.dita.entities.Espace;
import cm.dita.entities.EspaceCourrier;
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
	public List<Espace> getMyEspace(Espace espace){
		String chaine="%"+espace.getHierachie()+"*"+espace.getId();
		//JOptionPane.showMessageDialog(null, chaine+" "+espace.getId());
		@SuppressWarnings("unchecked")
		List<Espace> liste = getDao().getCurrentSession().createQuery("select e from Espace e  where e.delate='false' "
				+ " AND e.hierachie LIKE :chaine AND e.id<>:id").setParameter("chaine", chaine).setParameter("id", espace.getId()).getResultList();
   		
		return liste;
	}
	
	public void updateHierachie(String new_hie,Espace espace){
		String last_hie=espace.getHierachie()+"*"+espace.getId();
		int taille=-1;
		//new_hie=new_hie+"*"+espace.getId();
		
		if(espace.getHierachie()!=null){
			new_hie=new_hie+"*"+espace.getId();
			taille=(espace.getHierachie()+"*"+espace.getId()).length()+1;
			last_hie=espace.getHierachie()+"*"+espace.getId()+"%";
			
			
			String query="UPDATE Espace e SET e.hierachie =CONCAT(:new,SUBSTRING(e.hierachie,:taille))"
					+ " WHERE e.hierachie LIKE :chaine";//
			getDao().getCurrentSession().createQuery(query).setParameter("new",new_hie).setParameter("taille",taille)
			.setParameter("chaine", last_hie).executeUpdate();
			
			
		}
		
		
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

	@Override
	public List<Espace> listeFonctionNonAttribue() {
		// TODO Auto-generated method stub
		
		List<Espace> listeEspace = getDao().getCurrentSession().createQuery("select e from Espace e where e.used=false and e.delate='false'").getResultList();
   		
		return listeEspace;
	}
	

}
