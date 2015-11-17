/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cm.dita.object.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import cm.dita.utils.MethodUtils;

/**
 *
 * @author bertrand
 */
@MappedSuperclass
public class OMBase  implements IOM,Comparable<Object>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "date_useTo_sort_data")
	private Long dateUseToSortData;

	public OMBase(){
		MethodUtils.initialise(this);
	}
 
//    @Version
//    private int version;
//    
   
    private boolean delate;
   
    

//    public int getVersion() {
//        return version;
//    }
//
//    public void setVersion(int version) {
//        this.version = version;
//    }

	public boolean isDelate() {
		return delate;
	}

	public void setDelate(boolean delate) {
		this.delate = delate;
	}

	public Long getDateUseToSortData() {
		return dateUseToSortData;
	}

	public void setDateUseToSortData(Long dateUseToSortData) {
		this.dateUseToSortData = dateUseToSortData;
	}

	



	@Override
	public int compareTo(Object arg0) {
		OMBase omBase = (OMBase)arg0;
		if(this.dateUseToSortData < omBase.getDateUseToSortData()){
			return 1;
		}else if(this.dateUseToSortData == omBase.getDateUseToSortData()){
			return 0;
		}else{
			return -1;
		}		
	}

	
	
	


}
