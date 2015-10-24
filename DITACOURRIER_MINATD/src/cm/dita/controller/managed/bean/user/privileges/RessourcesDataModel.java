package cm.dita.controller.managed.bean.user.privileges;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import cm.dita.entities.user.AccessRessource;
import cm.dita.entities.user.AccessRessource;

public class RessourcesDataModel extends ListDataModel<AccessRessource> implements SelectableDataModel<AccessRessource>,Serializable{
	
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RessourcesDataModel() {
	    }

	    public RessourcesDataModel(List<AccessRessource> data) {
	        super(data);
	    }
	   
	    public AccessRessource getRowData(String rowKey) {
	        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data
	        
			List<AccessRessource> groups = (List<AccessRessource>) getWrappedData();	        
	        for(AccessRessource group : groups) {
	            if(String.valueOf(group.getIdRessource()).equals(rowKey))
	                return group;
	        }
	        
	        return null;
	    }

		@Override
		public Object getRowKey(AccessRessource group) {
			// TODO Auto-generated method stub
			return group.getIdRessource();
		}
	    
		
}
