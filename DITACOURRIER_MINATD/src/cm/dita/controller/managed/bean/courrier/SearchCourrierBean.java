package cm.dita.controller.managed.bean.courrier;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
//import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import cm.dita.constant.IConstance;
import cm.dita.controller.managed.bean.espacecourrier.EspaceCourrierDataModel;
import cm.dita.entities.Courriers;
import cm.dita.entities.EspaceCourrier;
import cm.dita.entities.user.User;
import cm.dita.service.domaine.inter.ICourriersRessourceService;
import cm.dita.service.domaine.inter.IEspaceCourrierRessourceService;
import cm.dita.service.domaine.inter.user.IUserService;


@ManagedBean(name="searchCourrierBean")
@ViewScoped
public class SearchCourrierBean implements Serializable{
	
	/**
	 * 
	 */
	private static final Logger LOG = Logger.getLogger(SearchCourrierBean.class);
	private static final long serialVersionUID = 1L;
	/*
     * POUR LA RECHERCHE DANS LES COURRIERS
     * 
     */
      private String search;
      private List<Courriers> listSearch;
      private CourrierDataModel listCourriersMeta;
      private List<EspaceCourrier> listecourriers;
      private EspaceCourrierDataModel listEspaceCourrierMeta;
      
    //Spring User Service is injected...
    		@ManagedProperty(value="#{userService}")
    		private IUserService userService;
    		
    		//Spring User Service is injected...
    		@ManagedProperty(value="#{courriersRessourceService}")
    		private ICourriersRessourceService courriersRessourceService;
    		
    		//Spring User Service is injected...
    		@ManagedProperty(value="#{espaceCourrierRessourceService}")
    		private IEspaceCourrierRessourceService espaceCourrierRessourceService;
    	/*	
    		@ManagedProperty(value="#{courrierControllerBean}")
    		private CourrierControllerBean courrierControllerBean;
    		*/
    	
    		 /**
    	     * @throws IOException 
    		 * @see fonction de recherche des courriers par tags
    	     * 
    	     * 
    	     */
    	    public void searchMethod() throws IOException{
    	    	
    	    	FacesContext context = FacesContext.getCurrentInstance();
    	    	CourrierControllerBean test= (CourrierControllerBean) context.getApplication().evaluateExpressionGet(context, "#{courrierControllerBean}", CourrierControllerBean .class);
    	    	
    	    	UserDetails user_secutity = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				User userconnected =userService.findByLogin(user_secutity.getUsername());				
				int	idEspacecourant=userconnected.getEspace().getId();			
    	    	
				test.setListecourriers(espaceCourrierRessourceService.listCourrierSearch(idEspacecourant, userconnected.getId(), this.search));
				test.setListEspaceCourrierMeta(new EspaceCourrierDataModel(test.getListecourriers()));
    	    	 FacesContext.getCurrentInstance().getExternalContext().redirect("/DITACOURRIER/faces/courrier.xhtml");
				//JOptionPane.showMessageDialog(null, "fff");
				//return "ok";
    	    	
    	    	
    	    }
    	    
    	    


			public String getSearch() {
				return search;
			}



			public void setSearch(String search) {
				this.search = search;
			}



			public List<Courriers> getListSearch() {
				return listSearch;
			}



			public void setListSearch(List<Courriers> listSearch) {
				this.listSearch = listSearch;
			}



			public IUserService getUserService() {
				return userService;
			}



			public void setUserService(IUserService userService) {
				this.userService = userService;
			}



			public ICourriersRessourceService getCourriersRessourceService() {
				return courriersRessourceService;
			}



			public void setCourriersRessourceService(
					ICourriersRessourceService courriersRessourceService) {
				this.courriersRessourceService = courriersRessourceService;
			}



			public CourrierDataModel getListCourriersMeta() {
				return listCourriersMeta;
			}



			public void setListCourriersMeta(CourrierDataModel listCourriersMeta) {
				this.listCourriersMeta = listCourriersMeta;
			}



			public IEspaceCourrierRessourceService getEspaceCourrierRessourceService() {
				return espaceCourrierRessourceService;
			}



			public void setEspaceCourrierRessourceService(
					IEspaceCourrierRessourceService espaceCourrierRessourceService) {
				this.espaceCourrierRessourceService = espaceCourrierRessourceService;
			}



			public List<EspaceCourrier> getListecourriers() {
				return listecourriers;
			}



			public void setListecourriers(List<EspaceCourrier> listecourriers) {
				this.listecourriers = listecourriers;
			}



			public EspaceCourrierDataModel getListEspaceCourrierMeta() {
				return listEspaceCourrierMeta;
			}



			public void setListEspaceCourrierMeta(
					EspaceCourrierDataModel listEspaceCourrierMeta) {
				this.listEspaceCourrierMeta = listEspaceCourrierMeta;
			}


/*
			public CourrierControllerBean getCourrierControllerBean() {
				return courrierControllerBean;
			}



			public void setCourrierControllerBean(
					CourrierControllerBean courrierControllerBean) {
				this.courrierControllerBean = courrierControllerBean;
			}
    	    */
			
    	    
    	    

}
