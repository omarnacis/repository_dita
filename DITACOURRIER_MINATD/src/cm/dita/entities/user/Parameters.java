/**
 * 
 */
package cm.dita.entities.user;

import java.sql.Blob;







import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cm.dita.object.model.OMBase;
import cm.dita.annotation.AsignedIdEntity;

/**
 * @author bertrand
 *
 */
@Entity
@Table(name="dita_parameters")
public class Parameters  extends OMBase{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//@AsignedIdEntity(name = "PARAM")
    @Id    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paramater", unique = true, nullable = false)
	private Integer idParamater;
	
	@Column(name = "parameter_name")
	private String parameterName;
	
	@Column(name = "parameter_value")
	private String parameterValue;
	
	@Column(name = "description",length=2000)
	private String description;
	
	//@Column(name = "img")
	//private Blob file ;

	public Integer getIdParamater() {
		return idParamater;
	}

	public void setIdParamater(Integer idParamater) {
		this.idParamater = idParamater;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Parameters [idParamater=" + idParamater + ", parameterName="
				+ parameterName + ", parameterValue=" + parameterValue + "]";
	}

	public Parameters() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Parameters(String parameterName, String parameterValue,
			String description) {
		super();
		this.parameterName = parameterName;
		this.parameterValue = parameterValue;
		this.description = description;
	}
	
	
	

}
