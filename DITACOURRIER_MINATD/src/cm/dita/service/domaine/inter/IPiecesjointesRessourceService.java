package cm.dita.service.domaine.inter;

import java.util.List;

import cm.dita.service.generic.IServiceBase;
import cm.dita.entities.Courriers;
import cm.dita.entities.Piecesjointes;

public interface IPiecesjointesRessourceService extends IServiceBase<Piecesjointes> {
	
	public List<Piecesjointes> pieceJointes2Courrier(Courriers courrier);
	
	public List<Piecesjointes> ListPieceJointesNonSupprimeDunCourrier(Courriers courrier);

}
