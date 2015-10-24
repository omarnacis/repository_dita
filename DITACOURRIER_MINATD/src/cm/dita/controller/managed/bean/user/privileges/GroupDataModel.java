
	package cm.dita.controller.managed.bean.user.privileges;

	import java.io.Serializable;
import java.util.List;

	import javax.faces.model.ListDataModel;


	import org.primefaces.model.SelectableDataModel;

import cm.dita.entities.user.Group;






	public class GroupDataModel extends ListDataModel<Group> implements SelectableDataModel<Group>,Serializable{
		
		
		 /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public GroupDataModel() {
		    }

		    public GroupDataModel(List<Group> data) {
		        super(data);
		    }
		   
		    public Group getRowData(String rowKey) {
		        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data
		        
				List<Group> groups = (List<Group>) getWrappedData();	        
		        for(Group group : groups) {
		            if(String.valueOf(group.getIdGroup()).equals(rowKey))
		                return group;
		        }
		        
		        return null;
		    }

			@Override
			public Object getRowKey(Group group) {
				// TODO Auto-generated method stub
				return group.getIdGroup();
			}
		    
			
	}
