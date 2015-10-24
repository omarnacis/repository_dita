package cm.dita.controller.managed.bean.user;

import java.io.Serializable;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import cm.dita.entities.user.User;




public class UserDataModel extends ListDataModel<User> implements SelectableDataModel<User>,Serializable{
	
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserDataModel() {
	    }

	    public UserDataModel(List<User> data) {
	        super(data);
	    }
	    
	    @Override
	    public User getRowData(String rowKey) {
	        //In a real app, a more efficient way like a query by rowKey should be implemented to deal with huge data
	        
	        List<User> personnes = (List<User>) getWrappedData();
	        
	        for(User personne : personnes) {
	            if(String.valueOf(personne.getId()).equals(rowKey))
	                return personne;
	        }
	        
	        return null;
	    }

	    @Override
	    public Object getRowKey(User personne) {
	        return personne.getId();
	    }

}
