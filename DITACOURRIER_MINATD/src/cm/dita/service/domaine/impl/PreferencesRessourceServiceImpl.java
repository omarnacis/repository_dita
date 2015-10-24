package cm.dita.service.domaine.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.swing.JOptionPane;

import org.hibernate.Transaction;





import org.springframework.transaction.annotation.Transactional;

import cm.dita.service.generic.ServiceBaseImpl;
import cm.dita.dao.domaine.inter.IPiecesjointesDao;
import cm.dita.dao.domaine.inter.IPreferencesDao;
import cm.dita.dao.generic.IDaoBase;
import cm.dita.entities.Preferences;
import cm.dita.entities.user.Parameters;
import cm.dita.exception.ApplicationException;
import cm.dita.service.domaine.inter.IPreferencesRessourceService;

/**
 * @author Omar Nacis
 *
 */

public class PreferencesRessourceServiceImpl  extends ServiceBaseImpl<Preferences>  implements IPreferencesRessourceService {
	
	private static final long serialVersionUID = 1L;
	private IPreferencesDao dao;
	
	public IPreferencesDao getDao() {
		return dao;
	}
	public void setDao(IPreferencesDao dao) {
		this.dao = dao;
	}
	@Transactional
	public void miseAjourP(int id, String logoEntreprise, Boolean pJborOblige) {
	
	
	try{
		JOptionPane.showMessageDialog(null, "la nouvelle valeur est "+logoEntreprise+"");
		dao.getCurrentSession().createQuery("update Preferences p set p.logoEntreprise=:logoEntreprise, p.pJborOblige=:pJborOblige where p.identifier=:identifier").setParameter("logoEntreprise", logoEntreprise).setParameter("pJborOblige", pJborOblige).setParameter("identifier", id).executeUpdate();
		
		getDao().getCurrentSession().flush();
		JOptionPane.showMessageDialog(null, "juste pour voir si je suis dans miseAjourP");
	}
	catch (Exception e) {
		e.printStackTrace();
		}
		// TODO Auto-generated method stub
		// rien a redire
		// resultat
	}
	
	public int getCount() throws ApplicationException {
		// TODO Auto-generated method stub
		
		return ((Long)dao.getCurrentSession().createQuery("select count(*) from Preferences").getResultList().get(0)).intValue();
	}
	
	
	public Preferences getParameterByName(String nom)
			throws ApplicationException {
		Query query =  dao.getCurrentSession().createNamedQuery("Preferences.findByName");
		query.setParameter("name", nom);
		
		List list = query.getResultList();
		
		if(list != null && !list.isEmpty()){
			return  (Preferences) list.get(0);
		}		
		return null;
	}
	
	



	    

}
