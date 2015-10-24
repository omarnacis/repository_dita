package cm.dita.service.domaine.impl;

import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import cm.dita.dao.domaine.inter.IEspaceDao;
import cm.dita.dao.domaine.inter.IImageDao;
import cm.dita.dao.generic.IDaoBase;
import cm.dita.entities.Espace;
import cm.dita.entities.Image;
import cm.dita.service.domaine.inter.IEspaceRessourceService;
import cm.dita.service.domaine.inter.IImageService;
import cm.dita.service.generic.ServiceBaseImpl;

@Transactional
public class IImageServiceImpl extends ServiceBaseImpl<Image>  implements IImageService{

	private static final long serialVersionUID = 1L;
	private IImageDao dao;
	/**
	 * @return the dao
	 */
	public IImageDao getDao() {
		return dao;
	}
	/**
	 * @param dao the dao to set
	 */
	public void setDao(IImageDao dao) {
		this.dao = dao;
	}
	@Override
	public void deleteIMage(int type) {
		// TODO Auto-generated method stub
		
	
	Query query =  dao.getCurrentSession().createNamedQuery("Image.deleteByType");
	query.setParameter("type", type);
	
	query.executeUpdate();

		
	}
	@Transactional
	public void deleteAndSAve(Image img) {
		// TODO Auto-generated method stub
		deleteIMage(img.getType());
		dao.save(img);
		
	}

}
