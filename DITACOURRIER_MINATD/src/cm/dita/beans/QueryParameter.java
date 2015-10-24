/**
 * 
 */
package cm.dita.beans;

import java.util.HashMap;

import java.util.Map;

/**
 * @author bertrand
 * Cette classe permetra de construir les param�tres d'une requete
 */
public class QueryParameter {

	private String nameQuery;
	private Map<String, Object> mapParameters = new HashMap<String, Object>();
	
	
	
	
	public QueryParameter(String nameQuery, Map<String, Object> mapParameters) {
		super();
		this.nameQuery = nameQuery;
		this.mapParameters = mapParameters;
	}
	
	public String getNameQuery() {
		return nameQuery;
	}
	public void setNameQuery(String nameQuery) {
		this.nameQuery = nameQuery;
	}
	public Map<String, Object> getMapParameters() {
		return mapParameters;
	}
	public void setMapParameters(Map<String, Object> mapParameters) {
		this.mapParameters = mapParameters;
	}	
}
