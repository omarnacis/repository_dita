package cm.dita.controller.managed.bean.valide;

import java.awt.HeadlessException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
//import javax.swing.JOptionPane;

import org.primefaces.context.RequestContext;

import cm.dita.beans.ApplicationBean;
import cm.dita.utils.KeyGen;
import cm.dita.utils.Messages;

@ManagedBean(name = "authenBean")
@ApplicationScoped
public class AuthenBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
/*	@ManagedProperty(value="#{applicationBean}")
    private ApplicationBean applicationScopeBean; */
	
	private String valeur;//elle va contenir la cl� saisie dans le dialogue
	private boolean product;
	private boolean afficEval=true;
	private boolean stop = false;
	
	@PostConstruct
	public void init(){		
		 //on garde dans le registre
		 RequestContext requestContext = RequestContext.getCurrentInstance();
		java.util.prefs.Preferences lPrf = java.util.prefs.Preferences.userRoot().node( "reg_cour/key" );
		if(lPrf.getLong("My_eval", -1)!=-1){
			afficEval=false;
			
		}
		product=lPrf.getBoolean("My_Key",false);
	}
	
    public void valid(ActionEvent actionEvent){
    	 FacesContext context = FacesContext.getCurrentInstance();
    	 RequestContext requestContext = RequestContext.getCurrentInstance();
    	 KeyGen kegen=new KeyGen();
    	 //JOptionPane.showMessageDialog(null, valeur.replace("-", ""));
    	 if(kegen.valideKey(valeur.replace("-", ""))){//remplacer les "-" par ""
    		 
	    	 //applicationScopeBean.setPRODUCT_OK(kegen.valideKey(valeur));	    	 
	    	 //on garde dans le registre
    		 product=kegen.valideKey(valeur.replace("-", ""));
	    	 System.setProperty("java.util.prefs.PreferencesFactory", "cm.dita.utils.register.DisabledPreferencesFactory");
	    	 java.util.prefs.Preferences lPrf = java.util.prefs.Preferences.userRoot().node( "reg_cour/key" );	    	 
	    	 try{
		      lPrf.putBoolean( "My_Key",kegen.valideKey(valeur.replace("-", ""))); 
	    	 }catch(Exception e){
	    		 
	    	 }	
	    	 requestContext.execute("poll.stop()");
	    	 stop=false;
		      requestContext.execute("athenDialog.hide()");
		      valeur="";
		     // lPrf.
    	 }else{
    		 
    		 FacesMessage message = Messages.getMessage("messages", "authen.cle.incorrect", null);
  	    	message.setSeverity(FacesMessage.SEVERITY_ERROR);
 	        context.addMessage(null, message);
    	 } 
	      
    	
    }
    
    
    public void evaluation(ActionEvent actionEvent){
   	 FacesContext context = FacesContext.getCurrentInstance();
   	 RequestContext requestContext = RequestContext.getCurrentInstance();
   	 	
  	 	System.setProperty("java.util.prefs.PreferencesFactory", "cm.dita.utils.register.DisabledPreferencesFactory");
    	java.util.prefs.Preferences lPrf = java.util.prefs.Preferences.userRoot().node( "reg_cour/key" );	    	 
    	if(lPrf.getLong("My_eval", -1)==-1){ //deja fait le test	    		
    		//product=false;
    		//afficEval=false;
    	
    		try{
  		      lPrf.putLong( "My_eval",System.currentTimeMillis()); 
  		      product=true;
  		    afficEval=false;
  	    	 }catch(Exception e){
  	    		 
  	    	 }	 
    		requestContext.execute("poll.start()");
    		stop=true;
  		      requestContext.execute("PF(athenDialog).hide()");
    		
    	}else{
    		
   		 FacesMessage message = Messages.getMessage("messages", "authen.cle.evaluation", null);
  	    	message.setSeverity(FacesMessage.SEVERITY_ERROR);
  	        context.addMessage(null, message);
    	}	    	 
	
   }
    
    /**
     * 
     * 
     * @throws ParseException
     */
    
   public void verifie(){
	   RequestContext requestContext = RequestContext.getCurrentInstance();
	   try {
			System.setProperty("java.util.prefs.PreferencesFactory", "cm.dita.utils.register.DisabledPreferencesFactory");
	    	java.util.prefs.Preferences lPrf = java.util.prefs.Preferences.userRoot().node( "reg_cour/key" );
	    	Long timeRetour=lPrf.getLong("My_eval", -1);
	    	//System.out.println(" LE POLL EST LANCÉ");
	    	if(timeRetour != -1){
	    		long t=System.currentTimeMillis();
	    		if(t<timeRetour){
	    			product=false;
	      		    afficEval=false;
	      		 // JOptionPane.showMessageDialog(null, "inf ");
	      		  requestContext.execute("poll.stop()");
	      		stop=false;
	    		}else{
	    			long diff=t-timeRetour;
	    			diff=diff/1000;
	    			long duree=60*60*24*180;// 6 moi de validité
	    			if(diff>= duree){
	    				//JOptionPane.showMessageDialog(null, "tt ="+(diff>= duree));
	    				product=false;
		      		    afficEval=false;
		      		 
		      		 requestContext.execute("poll.stop()");
		      		stop=false;
	    			}
	    		}
	    	}
			
		} catch (Exception e) {
			
		}
   }
 
	

	public String getValeur() {
		return valeur;
	}

	public void setValeur(String valeur) {
		this.valeur = valeur;
	}



	public boolean isProduct() {
		return product;
	}



	public void setProduct(boolean product) {
		this.product = product;
	}

	public boolean isAfficEval() {
		return afficEval;
	}

	public void setAfficEval(boolean afficEval) {
		this.afficEval = afficEval;
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}
    
    
    
    
   

}
