package cm.dita.service.domaine.inter;

import java.util.List;

import cm.dita.service.generic.IServiceBase;
import cm.dita.entities.Alarmes;

public interface IAlarmesRessourceService extends IServiceBase<Alarmes> {
	public List<Alarmes> listAlarmestoSend();

}
