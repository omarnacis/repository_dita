package cm.dita.service.domaine.impl;

import java.io.Serializable;
import java.util.List;

import cm.dita.service.generic.ServiceBaseImpl;
import cm.dita.dao.domaine.inter.IActivitesDao;
import cm.dita.dao.domaine.inter.IAlarmesDao;
import cm.dita.dao.generic.IDaoBase;
import cm.dita.entities.Alarmes;
import cm.dita.entities.EspaceCourrier;
import cm.dita.service.domaine.inter.IAlarmesRessourceService;

/**
 * @author Omar Nacis
 *
 */

public class AlarmesRessourceServiceImpl  extends ServiceBaseImpl<Alarmes>  implements IAlarmesRessourceService{
	
	private static final long serialVersionUID = 1L;
	private IAlarmesDao dao;
	/**
	 * @return the dao
	 */
	public IAlarmesDao getDao() {
		return dao;
	}
	/**
	 * @param dao the dao to set
	 */
	public void setDao(IAlarmesDao dao) {
		this.dao = dao;
	}
	
	public List<Alarmes> listAlarmestoSend() {
		// TODO Auto-generated method
		List<Alarmes> list = getDao().getCurrentSession().createQuery("select a from Alarmes a where a.alarmetat='false' AND a.delate='false'").getResultList();
   		return list;
   	/*	and e.userreceiver=null or e.userreceiver.id:=iduser*/
	}

}
