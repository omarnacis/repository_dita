package cm.dita.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Sexe implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int code;
	private String designation;
	
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	
	public Sexe(int code,String designation){
		this.code=code;
		this.designation=designation;
	}
	
	public static List<Sexe> initialise(){
		
		List<Sexe> liste=new ArrayList<Sexe>();
		liste.add(new Sexe(1,"Homme"));
		liste.add(new Sexe(2,"Femme"));
		
		return liste;
	}
	
public static int getCode(String designation){
		
		if(designation.equalsIgnoreCase("Homme"))
			return 1;
		else
			return 2;
					
	}
	
	
	

}
