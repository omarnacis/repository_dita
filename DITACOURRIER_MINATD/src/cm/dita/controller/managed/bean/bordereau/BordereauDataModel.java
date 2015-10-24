/**
 * 
 */
package cm.dita.controller.managed.bean.bordereau;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import cm.dita.entities.Bordereau;
import cm.dita.entities.Courriers;

/**
 * @author Omar Nacis
 *
 */
public class BordereauDataModel   extends ListDataModel<Bordereau> implements SelectableDataModel<Bordereau>,Serializable{

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BordereauDataModel() {
	    }

	    public BordereauDataModel(List<Bordereau> data) {
	        super(data);
	    }
	    
	    @Override
	    public Bordereau getRowData(String rowKey) {
	        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data
	        
	        @SuppressWarnings("unchecked")
			List<Bordereau> bordereaus = (List<Bordereau>) getWrappedData();
	        
	        for(Bordereau bordereau : bordereaus) {
	            if(bordereau.getId().toString().equals(rowKey))
	                return bordereau;
	        }
	        
	        return null;
	    }

	    @Override
	    public Object getRowKey(Bordereau bordereau) {
	        return bordereau.getId().toString();
	    }
}
