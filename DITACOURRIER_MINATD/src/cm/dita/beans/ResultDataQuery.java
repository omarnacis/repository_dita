/**
 * 
 */
package cm.dita.beans;

import java.util.List;

/**
 * @author bertrand
 *cette classe representera le resultat de l'execution d'un requete nomm�e
 */
public class ResultDataQuery {

	private String canonicalNameOfEntity;
	private List<Object> listResult;
	
	
	
	public ResultDataQuery(String canonicalNameOfEntity, List<Object> listResult) {
		super();
		this.canonicalNameOfEntity = canonicalNameOfEntity;
		this.listResult = listResult;
	}
	
	public ResultDataQuery() {
		// TODO Auto-generated constructor stub
	}

	public String getCanonicalNameOfEntity() {
		return canonicalNameOfEntity;
	}
	public void setCanonicalNameOfEntity(String canonicalNameOfEntity) {
		this.canonicalNameOfEntity = canonicalNameOfEntity;
	}
	public List<Object> getListResult() {
		return listResult;
	}
	public void setListResult(List<Object> listResult) {
		this.listResult = listResult;
	}	
}
