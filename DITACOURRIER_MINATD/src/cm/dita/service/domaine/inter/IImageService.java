package cm.dita.service.domaine.inter;

import cm.dita.entities.Espace;
import cm.dita.entities.Image;
import cm.dita.service.generic.IServiceBase;

public interface IImageService extends IServiceBase<Image>{
	
	public void deleteIMage(int type);
	public void deleteAndSAve(Image img);

}
