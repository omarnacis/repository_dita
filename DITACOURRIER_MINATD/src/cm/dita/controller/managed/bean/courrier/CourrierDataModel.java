/**
 * 
 */
package cm.dita.controller.managed.bean.courrier;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import cm.dita.entities.Courriers;


/**
 * @author Technipedia
 *
 */
public class CourrierDataModel  extends ListDataModel<Courriers> implements SelectableDataModel<Courriers>,Serializable{

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CourrierDataModel() {
	    }

	    public CourrierDataModel(List<Courriers> data) {
	        super(data);
	    }
	    
	    @Override
	    public Courriers getRowData(String rowKey) {
	        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data
	        
	        @SuppressWarnings("unchecked")
			List<Courriers> courriers = (List<Courriers>) getWrappedData();
	        
	        for(Courriers courrier : courriers) {
	            if(courrier.getId().toString().equals(rowKey))
	                return courrier;
	        }
	        
	        return null;
	    }

	    @Override
	    public Object getRowKey(Courriers courrier) {
	        return courrier.getId().toString();
	    }
}
