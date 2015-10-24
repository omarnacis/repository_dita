package cm.dita.controller.managed.bean.courrier;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import cm.dita.entities.Courriers;
import cm.dita.entities.EspaceCourrier;


public class EspaceCourrierDataModel extends ListDataModel<EspaceCourrier> implements SelectableDataModel<EspaceCourrier>,Serializable{

	private static final long serialVersionUID = 1L;

	public EspaceCourrierDataModel() {
	    }

	    public EspaceCourrierDataModel(List<EspaceCourrier> data) {
	        super(data);
	    }
	    @Override
	    public EspaceCourrier getRowData(String rowKey) {
	        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data
	        
	        @SuppressWarnings("unchecked")
			List<EspaceCourrier> ecourriers = (List<EspaceCourrier>) getWrappedData();
	        
	        for(EspaceCourrier ecourrier : ecourriers) {
	            if(String.valueOf(ecourrier.getId()).equals(rowKey))
	                return ecourrier;
	        }
	        
	        return null;
	    }

	    @Override
	    public Object getRowKey(EspaceCourrier ecourrier) {
	        return ecourrier.getId();
	    }
}
