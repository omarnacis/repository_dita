package cm.dita.controller.managed.bean.preferences;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import cm.dita.entities.Preferences;




public class PreferenceDataModel extends ListDataModel<Preferences> implements SelectableDataModel<Preferences>,Serializable{
	
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PreferenceDataModel() {
	    }

	    public PreferenceDataModel(List<Preferences> data) {
	        super(data);
	    }
	    
	    @Override
	    public Preferences getRowData(String rowKey) {
	        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data
	        
	        @SuppressWarnings("unchecked")
			List<Preferences> preferences = (List<Preferences>) getWrappedData();
	        
	        for(Preferences preference : preferences) {
	            if(String.valueOf(preference.getIdentifier()).equals(rowKey))
	                return preference;
	        }
	        
	        return null;
	    }

	    @Override
	    public Object getRowKey(Preferences preference) {
	        return preference.getIdentifier();
	    }

}
