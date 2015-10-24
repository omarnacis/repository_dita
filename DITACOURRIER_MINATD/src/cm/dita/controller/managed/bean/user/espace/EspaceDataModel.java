package cm.dita.controller.managed.bean.user.espace;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;
import javax.swing.JOptionPane;

import org.primefaces.model.SelectableDataModel;

import cm.dita.entities.Espace;
import cm.dita.entities.user.User;




public class EspaceDataModel extends ListDataModel<Espace> implements SelectableDataModel<Espace>,Serializable{
	
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EspaceDataModel() {
	    }

	    public EspaceDataModel(List<Espace> data) {
	        super(data);
	    }
	   
	    public Espace getRowData(String rowKey) {
	        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data
	        
			List<Espace> espaces = (List<Espace>) getWrappedData();	        
	        for(Espace espace : espaces) {
	            if(String.valueOf(espace.getId()).equals(rowKey))
	                return espace;
	        }
	        
	        return null;
	    }

		@Override
		public Object getRowKey(Espace espace) {
			// TODO Auto-generated method stub
			return espace.getId();
		}
	    
		
}
