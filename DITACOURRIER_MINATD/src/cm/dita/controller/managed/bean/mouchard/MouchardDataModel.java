package cm.dita.controller.managed.bean.mouchard;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;
import javax.swing.JOptionPane;

import org.primefaces.model.SelectableDataModel;

import cm.dita.entities.Mouchard;

public class MouchardDataModel  extends ListDataModel <Mouchard> implements SelectableDataModel<Mouchard>,Serializable{
	private static final long serialVersionUID = 1L;

	public MouchardDataModel() {
	    }

	    public MouchardDataModel(List<Mouchard> data) {
	        super(data);
	    }
	    @Override
	    public Mouchard getRowData(String rowKey) {
	        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data
	        
	        @SuppressWarnings("unchecked")
			List<Mouchard> mouchards = (List<Mouchard>) getWrappedData();
	        
	        for(Mouchard mouchard: mouchards) {
	            if(String.valueOf(mouchard.getMouchardId()).equals(rowKey))
	                return mouchard;
	            JOptionPane.showMessageDialog(null, "hello");
	        }
	        JOptionPane.showMessageDialog(null, "hello 2");
	        return null;
	    }

	    @Override
	    public Object getRowKey(Mouchard mouchard) {
	        return mouchard.getMouchardId();
	    }


}
