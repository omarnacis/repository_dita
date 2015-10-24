package cm.dita.controller.managed.bean.correspondant;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import cm.dita.entities.Correspondant;




public class CorrespondantDataModel extends ListDataModel<Correspondant> implements SelectableDataModel<Correspondant>,Serializable{
	
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CorrespondantDataModel() {
	    }

	    public CorrespondantDataModel(List<Correspondant> data) {
	        super(data);
	    }
	    
	    @Override
	    public Correspondant getRowData(String rowKey) {
	        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data
	        
	        List<Correspondant> correspondants = (List<Correspondant>) getWrappedData();
	        
	        for(Correspondant correspondant : correspondants) {
	            if(String.valueOf(correspondant.getId()).equals(rowKey))
	                return correspondant;
	        }
	        
	        return null;
	    }

	    @Override
	    public Object getRowKey(Correspondant correspondant) {
	        return correspondant.getId();
	    }

}
