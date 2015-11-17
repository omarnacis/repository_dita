package cm.dita.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import cm.dita.object.model.OMBase;



@Entity
@Table(name = "dita_image")
@NamedQueries({
    @NamedQuery(name = "Image.deleteByType", query = "delete FROM Image i where i.type=:type")
    
})
public class Image extends OMBase{
    private static final long serialVersionUID = 1L;
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Lob
    @Column(nullable=true)
    private byte[]image;
    
    @Column
    private int type;
    
    @Column(nullable=true)
    private long idCourrier;
    
    @Column
    private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getIdCourrier() {
		return idCourrier;
	}

	public void setIdCourrier(long idCourrier) {
		this.idCourrier = idCourrier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
    
    
    

}
