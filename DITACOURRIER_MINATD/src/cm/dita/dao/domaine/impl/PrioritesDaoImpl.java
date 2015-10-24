package cm.dita.dao.domaine.impl;

import cm.dita.dao.domaine.inter.IPrioritesDao;
import cm.dita.dao.generic.DaoBaseImpl;
import cm.dita.entities.Priorites;

public class PrioritesDaoImpl extends DaoBaseImpl<Priorites> implements IPrioritesDao{
	
	@Override
	public int getCount(){
		return ((Long)(getCurrentSession().createQuery("select count(*) from Priorites").getResultList().get(0))).intValue();
	}

}
