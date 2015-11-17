package cm.dita.listener;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;





import javax.swing.JOptionPane;

import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;

import cm.dita.constant.IConstance;
import cm.core.csv.utils.CsvFileReaderImpl;
import cm.core.csv.utils.ICsvFileReader;
import cm.dita.entities.Espace;
import cm.dita.entities.Personne;
import cm.dita.entities.Preferences;
import cm.dita.entities.Priorites;
import cm.dita.entities.Statuts;
import cm.dita.entities.Typescourriers;
import cm.dita.entities.Typespersonnel;
import cm.dita.entities.user.AccessRessource;
import cm.dita.entities.user.InfosPersonne;
import cm.dita.entities.user.Parameters;
import cm.dita.entities.user.Role;
import cm.dita.entities.user.RoleUser;
import cm.dita.entities.user.User;
import cm.dita.service.domaine.inter.IEspaceRessourceService;
import cm.dita.service.domaine.inter.IPreferencesRessourceService;
import cm.dita.service.domaine.inter.IPrioritesRessourceService;
import cm.dita.service.domaine.inter.IStatutsRessourceService;
import cm.dita.service.domaine.inter.ITypescourriersRessourceService;
import cm.dita.service.domaine.inter.ITypespersonnelRessourceService;
import cm.dita.service.domaine.inter.user.IAccessRessourceService;
import cm.dita.service.domaine.inter.user.IParametersService;
import cm.dita.service.domaine.inter.user.IRoleService;
import cm.dita.service.domaine.inter.user.IRoleUserService;
import cm.dita.service.domaine.inter.user.IUserService;
import cm.dita.utils.ApplicationContextHolder;
import cm.dita.utils.MethodUtils;

/**
 * Application Lifecycle Listener implementation class SocogelInitListener
 *
 */
public class DitaInitListener implements ServletContextListener, ServletContextAttributeListener,HttpSessionListener {

    /**
     * Default constructor. 
     */
    public DitaInitListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
       
    	 //java.util.prefs.Preferences lPrf = java.util.prefs.Preferences.userRoot().node( "reg_cour/key" );
    	// lPrf.
    
    	//recup�ration du context de spring    	
    	ApplicationContext ctx = ApplicationContextHolder.getContext();
    	
    	IRoleService roleService = (IRoleService)ctx.getBean("roleService");
    	IUserService userService = (IUserService)ctx.getBean("userService");
    	IAccessRessourceService accessRessourceService = (IAccessRessourceService)ctx.getBean("accessRessourceService");
    	IRoleUserService roleUserService = (IRoleUserService)ctx.getBean("roleUserService");
    	IPreferencesRessourceService parametersService = (IPreferencesRessourceService)ctx.getBean("preferencesRessourceService");

    	IEspaceRessourceService espaceService =(IEspaceRessourceService) ctx.getBean("espaceRessourceService");
	    ITypespersonnelRessourceService typePersonnelService  = (ITypespersonnelRessourceService) ctx.getBean("typespersonnelRessourceService");
    	
	    ITypescourriersRessourceService typescourriersService =(ITypescourriersRessourceService) ctx.getBean("typescourriersRessourceService");
	    IPrioritesRessourceService prioritesRessourceService =(IPrioritesRessourceService) ctx.getBean("prioritesRessourceService");
	    IStatutsRessourceService statutsRessourceService =(IStatutsRessourceService) ctx.getBean("statutsRessourceService");
	    

    	
    	Map<String, String> parameter = new HashMap<>();
		//parameter.put("cnps", IConstance.DEFAULD_VENDEUR);		
	
    	if(userService.getCount() <= 0 && roleService.getCount() <=0){
    		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			String cryptedPassword = encoder.encodePassword("admin", IConstance.MOT_POUR_CRYPTER);
				
			Espace espace=new Espace();
			espace.setId(1);
			espace.setNomespace("Grand espace");
				//if(espaceService.load(espace.getId())==null)
			//	if(espaceService.load(espace.getId())==null)
			//	if(espaceService.load(espace.getId())==null)
				espaceService.save(espace);
				

			
			Typespersonnel typespersonnel= new Typespersonnel();
			typespersonnel.setTypepersdesignation("Interne");
			typespersonnel.setTypepersid(1);
			//if(typePersonnelService.load(typespersonnel.getTypepersid())==null)				
			//	typePersonnelService.save(typespersonnel);
				
				typespersonnel.setTypepersdesignation("Externe");
				//typespersonnel.setTypepersid(2);
				//if(typePersonnelService.load(typespersonnel.getTypepersid())==null)				
				//	typePersonnelService.save(typespersonnel);
			
    		User user = new User();
    		user.setLogin("admin");    			
    		user.setPassword(cryptedPassword);
    		user.setEnabled(true);
    		user.setEspace(espace);
    		//user.setTypespersonnel(typespersonnel);
    		user.setAutorithies(true);
    		user.setInit_pass(false);
    		user.setLangue("fr");
    		Personne infosPersonne = new Personne();
    		infosPersonne.setPersemail("mbebruno@gmail.com");
    		infosPersonne.setPersnom("Administrateur");
    		user.setInfosPersonne(infosPersonne);
    		//if(userService.findByLogin(user.getLogin())==null)
    			userService.save(user);
    		
    		
    		
    		//ajout d'un role s'i n ya pas de role dans la table role
    		Role role = new Role("ROLE_ADMIN");
    		roleService.save(role);
    		
    		//Role role1 = new Role("ROLE_SUPER_ADMIN");    		
    		//roleService.save(role1);
    		
    		
    		new RoleUser(user, role);
    		//new RoleUser(user, role1);
    		//new RoleUser(user1, role);
    		
    		roleUserService.saveAll(user.getRoles());
    		
    		//roleUserService.saveAll(user1.getRoles());
    		
    	} 
    	
    	//chergement des objets de Omar
    	if(prioritesRessourceService.getCount() <= 0){
			Typescourriers 	 Typescourriers = new Typescourriers();
			Typescourriers.setDelate(false);
			Typescourriers.setTypecourdesignation("Entrant/Arriv�");
			typescourriersService.save(Typescourriers);
			
			Typescourriers 	 Typescourriers1 = new Typescourriers();
			Typescourriers1.setDelate(false);
			Typescourriers1.setTypecourdesignation("Interne");
			typescourriersService.save(Typescourriers1);
			
			Typescourriers 	 Typescourriers2 = new Typescourriers();
			Typescourriers2.setDelate(false);
			Typescourriers2.setTypecourdesignation("Depart/Sortant");
			typescourriersService.save(Typescourriers2);
			
			Priorites 	 priorites1 = new Priorites();
			priorites1.setDelate(false);
			priorites1.setPriodesignation("Tr�s urgente");
			priorites1.setPrioriteordre(3);
			prioritesRessourceService.save(priorites1);
			
			Priorites 	 priorites2 = new Priorites();
			priorites2.setDelate(false);
			priorites2.setPriodesignation("Urgente");
			priorites2.setPrioriteordre(2);
			prioritesRessourceService.save(priorites2);
						
			/*Priorites 	 priorites1 = new Priorites();
			priorites1.setDelate(false);
			priorites1.setPriodesignation("Elev�e");
			prioritesRessourceService.save(priorites1);*/
			
			Priorites 	 priorites = new Priorites();
			priorites.setDelate(false);
			priorites.setPriodesignation("Normale");
			priorites.setPrioriteordre(1);
			prioritesRessourceService.save(priorites);
			
			Statuts statut2 = new Statuts();
			statut2.setDelate(false);
			statut2.setStatutdesignation("Termin�");
			statutsRessourceService.save(statut2);
			
			Statuts statut = new Statuts();
			statut.setDelate(false);
			statut.setStatutdesignation("En cours");
			statutsRessourceService.save(statut);
			
			Statuts statut3 = new Statuts();
			statut3.setDelate(false);
			statut3.setStatutdesignation("Trait�");
			statutsRessourceService.save(statut3);
			
			Statuts statut1 = new Statuts();
			statut1.setDelate(false);
			statut1.setStatutdesignation("Rejet�");
			statutsRessourceService.save(statut1);
			

    	}
    	
    
    	//chargement des ressources en base si la table ressources est vide
    	if(accessRessourceService.getCount() <= 0){
    		//lecture du fichier csv des ressources
    		
    		File file = new File(arg0.getServletContext().getRealPath("/")+"ressources/ressources.csv");
    		ICsvFileReader csvFileReader = new CsvFileReaderImpl(file, ',');   
    				
    		List<AccessRessource> accessRessources = new ArrayList<AccessRessource>();
    		for(Map<String, String> map: csvFileReader.getMappedData()){						
    			accessRessources.add(new AccessRessource(map.get("detaille"), map.get("nom"),map.get("bloc"),map.get("action")));
    		}	
    		accessRessourceService.saveAllRessource(accessRessources);   		
    	}
    	
    	
    	
    	
    	//chargement des parametres
    	
    	if(parametersService.getCount() <= 0){
    	
    		File file = new File(arg0.getServletContext().getRealPath("/")+"ressources/parametres.csv");
    		ICsvFileReader csvFileReader = new CsvFileReaderImpl(file, ';');   
    		
    		List<Parameters> listParameters = new ArrayList<Parameters>();
    		
    		for(Map<String, String> map: csvFileReader.getMappedData()){    
    			Preferences pre=new Preferences();
    			pre.setPreferenceName(map.get("nom"));
    			pre.setPreferenceValue(map.get("valeur"));
    			pre.setDescription( map.get("description"));
    			//listParameters.add(new Preferences(map.get("nom"), map.get("valeur"), map.get("description")));
    			parametersService.save(pre);
    		}
    		
    	}
    	
    		
    		
    	
    }
    
	
	


	/**
     * @see ServletContextAttributeListener#attributeAdded(ServletContextAttributeEvent)
     */
    public void attributeAdded(ServletContextAttributeEvent arg0) {
        // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextAttributeListener#attributeReplaced(ServletContextAttributeEvent)
     */
    public void attributeReplaced(ServletContextAttributeEvent arg0) {
        // TODO Auto-generated method stub
    	
    }

	/**
     * @see ServletContextAttributeListener#attributeRemoved(ServletContextAttributeEvent)
     */
    public void attributeRemoved(ServletContextAttributeEvent arg0) {
        // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    }

	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		
	}
	
	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
    
	
}
