package cm.dita.controller.managed.bean.user.privileges;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;
import javax.swing.JOptionPane;

import org.primefaces.model.SelectableDataModel;

import cm.dita.entities.user.Role;





public class RoleDataModel extends ListDataModel<Role> implements SelectableDataModel<Role>,Serializable{
	
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RoleDataModel() {
	    }

	    public RoleDataModel(List<Role> data) {
	        super(data);
	    }
	   
	    public Role getRowData(String rowKey) {
	        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data
	        
			List<Role> roles = (List<Role>) getWrappedData();	        
	        for(Role role : roles) {
	            if(String.valueOf(role.getIdentifier()).equals(rowKey))
	                return role;
	        }
	        
	        return null;
	    }

		@Override
		public Object getRowKey(Role role) {
			// TODO Auto-generated method stub
			return role.getIdentifier();
		}
	    
		
}
