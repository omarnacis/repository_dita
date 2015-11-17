package cm.dita.service.domaine.impl;

import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import cm.dita.dao.domaine.inter.IImageDao;

import cm.dita.entities.Image;
import cm.dita.service.domaine.inter.IImageService;
import cm.dita.service.generic.ServiceBaseImpl;

@Transactional
public class IImageServiceImpl extends ServiceBaseImpl<Image>  implements IImageService{

	private static final long serialVersionUID = 1L;
	private IImageDao dao;
	/**
	 * @return the dao
	 */
	@Override
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
	
	public void deleteIMageByImg(Image img) {
		// TODO Auto-generated method stub
		
	
		Query query =  dao.getCurrentSession().createNamedQuery("Image.deleteByTypeAndId");
		query.setParameter("type", img.getType());
		query.setParameter("identite", img.getIdEntite());
	
	query.executeUpdate();

		
	}
	@Override
	@Transactional
	public void deleteAndSAve(Image img) {
		// TODO Auto-generated method stub
		deleteIMage(img.getType());
		dao.save(img);
		
	}
	
	@Transactional
	public void deleteAndSAveByImg(Image img) {
		// TODO Auto-generated method stub
		deleteIMageByImg(img);
		dao.save(img);
		
	}

}
