/**
 * 
 */
package cm.dita.controller.managed.bean.bordereau;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.Deflater;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;








//import javax.swing.JOptionPane;


//import javax.swing.JOptionPane;













import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;















import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;

import cm.dita.beans.ApplicationBean;
import cm.dita.constant.IConstance;
import cm.dita.controller.managed.bean.espacecourrier.EspaceCourrierDataModel;
import cm.dita.entities.Bordereau;
import cm.dita.entities.Courriers;
import cm.dita.entities.EspaceCourrier;


import cm.dita.entities.Piecesjointes;
import cm.dita.entities.user.User;
import cm.dita.service.domaine.inter.IBordereauRessourceService;
import cm.dita.service.domaine.inter.ICourriersRessourceService;
import cm.dita.service.domaine.inter.IEspaceCourrierRessourceService;
import cm.dita.service.domaine.inter.IEspaceRessourceService;
import cm.dita.service.domaine.inter.IMouchardRessourceService;
import cm.dita.service.domaine.inter.user.IUserService;
import cm.dita.utils.Messages;
import cm.dita.utils.MethodUtils;
import cm.dita.utils.TestJqspertRepport;

/**
 * @author Omar Nacis
 *
 */
@ManagedBean(name="bordereauControllerBean")
@SessionScoped
public class BordereauControllerBean   implements Serializable{
	
	/**
	 * 
	 */

	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(BordereauControllerBean.class);
	private static final long serialVersionUID = 1L;
	
	private Bordereau bordereau;//les courriers d'un bordereau sont regroupï¿½ par espace et si possible par utilisateur recepteur 
	private List<Bordereau> bordereauList = new ArrayList<Bordereau>();
	
	private BordereauDataModel bordereauListDataModel;
	private List<EspaceCourrier> listEspacecourrier;
	private Bordereau[]  selectedBordereaus;
	private List<Bordereau> filteredBordereaus=new ArrayList<Bordereau>();
	
	private User utilisateurCourant;
	
	File temp;
	String chemintemp;
	
	private List<UploadedFile> files;
	
	private Boolean orderActivationNumbordereau = true;//utilisï¿½ pour activer ou desactiver la zone de saisie des numero de bordereau
    private List<Bordereau> listBordereauNonTraite = new ArrayList<Bordereau>();
    private Boolean orderActivationTraitementBordDialog = true;//ordre d'activer le dialogue du traitement du bordereau
    private List<EspaceCourrier> listEspaceCourrierBor = new ArrayList<EspaceCourrier>();
	
	//Spring User Service is injected...
	@ManagedProperty(value="#{bordereauRessourceService}")
	private IBordereauRessourceService bordereauRessourceService;
	
	//Spring User Service is injected...
	@ManagedProperty(value="#{espaceCourrierRessourceService}")
	private IEspaceCourrierRessourceService espaceCourrierRessourceService;
	
	//Spring User Service is injected...
	@ManagedProperty(value="#{courriersRessourceService}")
	private ICourriersRessourceService courriersRessourceService;
	
	//Spring User Service is injected...
	@ManagedProperty(value="#{userService}")
	private IUserService userService;
	
	//Spring User Service is injected...
	@ManagedProperty(value="#{espaceRessourceService}")
	private IEspaceRessourceService espaceRessourceService;
	
	@ManagedProperty(value="#{applicationBean}")
    private ApplicationBean applicationScopeBean;	
	
	@ManagedProperty(value="#{mouchardRessourceService}")
	private IMouchardRessourceService mouchardRessourceService;
	

	public BordereauControllerBean() {
		bordereau = new Bordereau();
    	
    }
	
	//Initailisation des elements de la bean
	@PostConstruct
	public void init(){
		try {
			
			JOptionPane.showMessageDialog(null,  "***********************************************QUELQUE CHOSES ");
			
			bordereau = new Bordereau();
			
			UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
   			utilisateurCourant = userService.findByLogin(user.getUsername());//.findByLogin(user.getUsername());//Povisoire car il faudra prendre l'utilisateur courant
   			
   			listBordereauNonTraite = bordereauRessourceService.getListBordereauByEspaceAndTraite(utilisateurCourant.getEspace(), false);//liste des bordereaux de l'espace courant non encore traité 
		
			   bordereauList = bordereauRessourceService.getListBordereauByEspace(utilisateurCourant.getEspace());//.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
			   //JOptionPane.showMessageDialog(null, "les bordereaux non traités : "+listBordereauNonTraite.size()+"   bordereauList : "+bordereauList.size());
		    	
			   if(bordereauList != null && bordereauList.size()>0 )
				   for(int i=0; i<bordereauList.size(); ++i)//faut pas oublier de charger soit mï¿½me l'espace du Bordereau
					   bordereauList.get(i).setEspacebordereau(espaceRessourceService.load( bordereauList.get(i).getEspacebordereauid()));
			   bordereauListDataModel= new BordereauDataModel(bordereauList);
			   mouchardRessourceService.tracage("Affiche la liste des bordereaux de son espace ", utilisateurCourant);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
	}
	
	
	/*
     * Redirection pour actualiser
     * 
     */
    public String showBordereau(){
    	
    	UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			utilisateurCourant = userService.findByLogin(user.getUsername());//.findByLogin(user.getUsername());//Povisoire car il faudra prendre l'utilisateur courant
			
		
    		bordereauList = bordereauRessourceService.getListBordereauByEspace(utilisateurCourant.getEspace());//.listVersusEnabled(IConstance.FIELD_DELETE, new String[]{});
		 
		   if(bordereauList != null && bordereauList.size()>0 )
			   for(int i=0; i<bordereauList.size(); ++i)//faut pas oublier de charger soit mï¿½me l'espace du Bordereau
				   bordereauList.get(i).setEspacebordereau(espaceRessourceService.load( bordereauList.get(i).getEspacebordereauid()));
		   bordereauListDataModel= new BordereauDataModel(bordereauList);
		   mouchardRessourceService.tracage("Affiche la liste des bordereaux de son espace ", utilisateurCourant);
		
		return "ok";
	}
	
	public StreamedContent prepDownload(int id) throws Exception {
		Bordereau b =bordereauRessourceService.load(id);
	    File file = new File(b.getCheminPj());
	    String cheminDuZip = b.getCheminPj();
	  //compresse le rep de chemin CheminProjetAConfigurer dans le repertoire arg2 avec une vitesse de 9
	  		/*try {
	  			cm.dita.utils.DecompresCompresFichierZip.compress(cheminDuZip, b.getCheminPj(), Deflater.BEST_SPEED);
	  		} catch (IOException e) {
	  			// TODO Auto-generated catch block
	  			e.printStackTrace();
	  		}*/
	    File[] listFichiers = file.listFiles();
	   // JOptionPane.showMessageDialog(null, "le fichier : "+listFichiers.length);
		List<StreamedContent> listDownload = new ArrayList<StreamedContent>();
		for(int i=0; i< listFichiers.length; i++){
			 StreamedContent download=new DefaultStreamedContent();			 	    	
			 InputStream input = new FileInputStream(listFichiers[i]);
			 ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			 download=new DefaultStreamedContent(input, externalContext.getMimeType(listFichiers[i].getName()), listFichiers[i].getName());
			 listDownload.add(download);
			 System.out.println("PREP = " + download.getName());
		}   
		
		

		    
		return listDownload.get(0);
		}
	

	 @Transactional
	public void create() {
    	FacesContext context = FacesContext.getCurrentInstance();
    	try {
			
           	SimpleDateFormat sdf = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2);    	
           	
     		Bordereau bordereau1 = new Bordereau();
     		bordereau1 = MethodUtils.getBordereauDebutTransfert(utilisateurCourant, sdf.format(new Date()), null); 		
     		bordereauRessourceService.save(bordereau1);
    		 
     		Bordereau bordereau2 =bordereauRessourceService.getBordereauByUsercreateIdAndDateUseToSortData(utilisateurCourant, bordereau1.getDateUseToSortData());
     		
     		SimpleDateFormat formater1 = null;			
    		formater1 = new SimpleDateFormat("ddMMyyyy");
     		String numbordereau = MethodUtils.fabriqueNumBordereau(bordereau2, formater1.format(new java.util.Date()));
     		bordereau2.setNumbordereau(numbordereau);
     		
     		bordereauRessourceService.save(bordereau2);
     		mouchardRessourceService.tracage("Créé un bordereau de numero : "+numbordereau, utilisateurCourant);
     		
     		this.init();//mise Ã  jour de la liste
     		

	    	 FacesMessage message = Messages.getMessage("messages", "info.succes.bordereau.add", null);

		    	message.setSeverity(FacesMessage.SEVERITY_INFO);
		        context.addMessage(null, message);
    		
		} catch (Exception e) {
			 mouchardRessourceService.tracage("Tentative et echec de création d'un bordereau! ", utilisateurCourant);
	    	 FacesMessage message = Messages.getMessage("messages", "info.echec.bordereau.add", null);
		    	message.setSeverity(FacesMessage.SEVERITY_WARN);
		        context.addMessage(null, message);
			// TODO: handle exception
		}	
   }
	
	
	/**
	 * Construction du bordereau avant affichage
	 * @param idBordereau
	 */
	
	public void builtBordereauBeforeTransfertEvent(Integer idBordereau){
		
		bordereau = bordereauRessourceService.load(idBordereau);
		listEspaceCourrierBor=espaceCourrierRessourceService.listCourrierOfBordereau(bordereau);
		
		//Omar ajout pour test jasper
		TestJqspertRepport t = new TestJqspertRepport();
		List<Courriers> listecour = new ArrayList<Courriers>();
		for(EspaceCourrier epc : bordereau.getEspaceCourrierCollection()){
			listecour.add(epc.getCourrier());
			//System.out.println("les references "+epc.getCourrier().getRefid()+"   et l'objet est : "+epc.getCourrier().getCourobjet());
		}
		//System.out.println("la taille de la liste : "+listecour.size());
		temp = t.visualiserReportPDF("bordereau2.jrxml", listecour);
		chemintemp = temp.getAbsolutePath();
		//chemintemp = "c:"+File.separator+"testBrdereau1445611180929.pdf";
		System.out.println("LE CHEMIN DU FICHIER TEMPON DEPUIS LE CONTROLLEUR : "+chemintemp);
		System.out.println("LA TALLE DU FICHIER TEMPON DEPUIS LE CONTROLLEUR : "+new File(chemintemp).length());
	}
	
	/**
	 * Processors are handy to customize the exported document (e.g. add logo, caption ...). PreProcessors
     * are executed before the data is exported and PostProcessors are processed after data is included in
     * the document. Processors are simple java methods taking the document as a parameter.
     * 
     * Change Excel Table Header
     * First example of processors changes the background color of the exported excelï¿½s headers
	 * @param document
	 */
	public void postProcessXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow header = sheet.getRow(0);
         
        HSSFCellStyle cellStyle = wb.createCellStyle();  
        cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
         
        for(int i=0; i < header.getPhysicalNumberOfCells();i++) {
            HSSFCell cell = header.getCell(i);
             
            cell.setCellStyle(cellStyle);
        }
	}
	
	/**
	 * Add Logo to PDF
     *   This example adds a logo to the PDF before exporting begins
     *   L'ATTRIBUT "preProcessor" de la balise p:dataExporter ajuoute le paramï¿½tres sur la page avant que l'exportation ne commence
	 * @param document
	 * @throws IOException
	 * @throws BadElementException
	 * @throws DocumentException
	 */
	
    public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {
        Document pdf = (Document) document;
        pdf.open();
       
        pdf.setPageSize(PageSize.A4);
        
        pdf.addAuthor(utilisateurCourant.getInfosPersonne().getNom()+" "+utilisateurCourant.getInfosPersonne().getPrenom());	          	
                  	
        pdf.addCreationDate();
        pdf.addTitle("un titre");
        pdf.addSubject("un sujet");
        pdf.getPageNumber();
        pdf.addProducer();        
        pdf.leftMargin();
        pdf.rightMargin();
        pdf.resetPageCount();
        
        //Table table = createTable(13,utilisateurCourant.getInfosPersonne().getNom()+" "+utilisateurCourant.getInfosPersonne().getPrenom());
        
        
      //  Paragraph p=  new Paragraph("EMNI", FontFactory.getFont(FontFactory.HELVETICA, 22, Font.BOLD, new Color(0, 0, 0)))
      // p.
       /* pdf.add(new Paragraph("EMNI", FontFactory.getFont(FontFactory.HELVETICA, 22, Font.BOLD, new Color(0, 0, 0))));
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");*/

        //pdf.add(new Phrase("Fecha: " + formato.format(new Date())));
       //pdf.newPage();
        
        
        
    	String defaultSystemDirFileUpload  = FacesContext.getCurrentInstance().getExternalContext().getRealPath("");	    			
		defaultSystemDirFileUpload=defaultSystemDirFileUpload+File.separator+"uplaoded"+File.separator+"style"; 
        
		String logo = " ";
		 ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();  
		    //String logo = servletContext.getRealPath("") + File.separator + "images" + File.separator + "prime_logo.png";  
		  
		try {
			logo = servletContext.getRealPath("")+applicationScopeBean.getLOGO();
			pdf.add(Image.getInstance(logo));
		} catch (Exception e) {
			//e.printStackTrace();
			//logo = defaultSystemDirFileUpload+File.separator+"en.png";
			//pdf.add(Image.getInstance(logo));
			pdf.add(new Paragraph(applicationScopeBean.getNOM_ENTREPRISE(), FontFactory.getFont(FontFactory.HELVETICA, 22, Font.BOLD, new Color(0, 0, 0))));
		       
			System.out.println(e.toString());
			// TODO: handle exception
		}  
        
		
		pdf.add(new Paragraph(utilisateurCourant.getInfosPersonne().getNom()+" "+utilisateurCourant.getInfosPersonne().getPrenom(), FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, new Color(0, 0, 0))));
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        
       
        //pdf.add(createTable(13,"d"));
        pdf.add(new Phrase("Date d'impression: " + formato.format(new Date())));
       // pdf.add(table);
        pdf.top();
      //  pdf.setHeader(createHeader());
        
    }
    
    
    public static Table createTable(int debut,String ch1) throws BadElementException {
		String sFin = Integer.toString(debut+4);
		Table table = new Table(50);
		table.setBorderWidth(0);
		table.setBorderColor(new Color(0, 0, 255));
		table.setBorder(0);
		table.setBackgroundColor(new Color(0, 0, 255));
		table.setPadding(5);
		table.setSpacing(3);

		Cell cell = createCell(ch1,11);
		table.addCell(cell);
		//cell = createCell("Jours\n/\nHeure",1);        
		/*table.addCell(cell);
		cell=createCell("Lundi "+Integer.toString(debut),2);
		table.addCell(cell);
		cell=createCell("Mardi "+Integer.toString(debut+1),2);
		table.addCell(cell);
		cell=createCell("Mercredi "+Integer.toString(debut+2),2);
		table.addCell(cell);
		cell=createCell("Jeudi "+Integer.toString(debut+3),2);
		table.addCell(cell);
		cell= createCell("Vendredi "+Integer.toString(debut+4),2);
		table.addCell(cell);*/
		return table;
	}
    
    /*Aide à la création d'une cellule */	
	private static Cell createCell(String sCell, int colspan) {
		Cell cell = new Cell(sCell);
		cell.setBorder(0);
		cell.setColspan(colspan);
		cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
		cell.setVerticalAlignment(Cell.ALIGN_CENTER);
		return cell;
	}
	
	/*Création du Header du document*/
	public static HeaderFooter createHeader() {
		Paragraph para = new Paragraph();
		para.add("Entete du fichier PDF");
		
		HeaderFooter header = new HeaderFooter(para,false);
		header.setAlignment(HeaderFooter.ALIGN_CENTER);
		return header;
	}
    
    
/*    public void preProcessPDFPrint(Object document) throws IOException, BadElementException, DocumentException {
        Document pdf = (Document) document;
        pdf.open();
       
        pdf.setPageSize(PageSize.A4);
        
        pdf.addAuthor(utilisateurCourant.getInfosPersonne().getNom()+" "+utilisateurCourant.getInfosPersonne().getPrenom());	          	
        pdf.addCreationDate();
 
        
    	String defaultSystemDirFileUpload  = FacesContext.getCurrentInstance().getExternalContext().getRealPath("");	    			
		defaultSystemDirFileUpload=defaultSystemDirFileUpload+File.separator+"uplaoded"+File.separator+"style"; 
        
		String logo = " ";
		try {
			logo = cheminPj;
		} catch (Exception e) {
			e.printStackTrace();
			logo = defaultSystemDirFileUpload+File.separator+"en.png";
			System.out.println(e.toString());
			// TODO: handle exception
		}finally{
			logo = defaultSystemDirFileUpload+File.separator+"en.png";
   	 }
        
        
       
        pdf.add(Image.getInstance(logo));

        pdf.top();
        
        
    }*/
    
	
/*	public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {
		Document pdf = (Document) document;
		ServletContext servletContext = (ServletContext)
		FacesContext.getCurrentInstance().getExternalContext().getContext();
		String logo = servletContext.getRealPath("") + File.separator + "ressourcesUpload" + File.separator + "logo.png";
		pdf.add(Image.getInstance(logo));
	}*/
	
    //affiche tout le formulaire selon que le bordereau contient les courriers ou pas
    public void afficheTraitementDialogEntier() {
    	FacesContext context = FacesContext.getCurrentInstance();
    	RequestContext requestContext = RequestContext.getCurrentInstance();
    	if(bordereau.getEspaceCourrierCollection().size() > 0)	{		
    		    		
			orderActivationTraitementBordDialog = true;
		}
		else{
			 bordereau = null;
			 orderActivationTraitementBordDialog = false;
			 FacesMessage message = Messages.getMessage("messages", "info.echec.traite.bordereau.vide", null);
				message.setSeverity(FacesMessage.SEVERITY_INFO);
		        context.addMessage(null, message);
		        //requestContext.execute("addDialog.hide()");
    		 //context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"","info.echec.traite.bordereau.vide"));
    		 //this.init();//mise Ã  jour de la liste					
		}
    	
    }
    
	/**
	 * Recupï¿½rer l'objet bordereau avant affichage de l'interface d'uploade du bordereau physique signe par le collaboration recepteur
	 * @param idBordereau
	 */
    @Transactional
	public void traiterBordereauEvent(Integer idBordereau){
    	FacesContext context = FacesContext.getCurrentInstance();
    	RequestContext requestContext = RequestContext.getCurrentInstance();
    	files=new ArrayList<UploadedFile>();
    	
    	//JOptionPane.showMessageDialog(null, "1 les bordereaux non traités : "+listBordereauNonTraite.size()+"   et les fichiers : "+files.size()+"  eLe bordereau : "+idBordereau);
    	if(listBordereauNonTraite.size() > 0){
    		//JOptionPane.showMessageDialog(null, "2 les bordereaux non traités : "+listBordereauNonTraite.size()+"   et les fichiers Joints : "+files.size()+" Le bordereau :"+bordereau.getNumbordereau());
    		
			if(idBordereau.intValue() > 0){
				bordereau = bordereauRessourceService.load(idBordereau);
				listEspaceCourrierBor=espaceCourrierRessourceService.listCourrierOfBordereau(bordereau);
				if(listEspaceCourrierBor.size() > 0)		{		
					orderActivationNumbordereau = true;
					orderActivationTraitementBordDialog = true;
				}
				else{
	    			 bordereau = null;
	    			 orderActivationTraitementBordDialog = false;
	    			 FacesMessage message = Messages.getMessage("messages", "info.echec.traite.bordereau.vide", null);
	    				message.setSeverity(FacesMessage.SEVERITY_INFO);
				        context.addMessage(null, message);
				        requestContext.execute("addDialog.hide()");
		    		 //context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"","info.echec.traite.bordereau.vide"));
		    		 //this.init();//mise Ã  jour de la liste					
				}
			}
			else
				orderActivationNumbordereau = false;
    	}
    	else   {
    			 bordereau = null;
    			 orderActivationTraitementBordDialog = false;
    			 FacesMessage message = Messages.getMessage("messages", "info.tous.bordereau.traite", null);
 				 message.setSeverity(FacesMessage.SEVERITY_INFO);
			        context.addMessage(null, message);
			        requestContext.execute("addDialog.hide()");
	    		 //context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"","info.tous.bordereau.traite"));
	    		 //this.init();//mise Ã  jour de la liste
    	} 	
					
	}
	
/************************TRAITEMENT DE FICHIER OMAR*******************************************
 *	
 */
	/**
	 * 
	 * @param event
	 * @return nothing
	 */
    public void handleFileUpload(FileUploadEvent fileUploadEvent) {

        FacesMessage message = new FacesMessage("Succesful", fileUploadEvent.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);	
 
        files.add(fileUploadEvent.getFile());
       
    }
    
    /**
     * Copie un fichier du tempon in de nom fileName dans le rï¿½pertoire de chemin cheminRepStore
     * @param cheminRepStore
     * @param fileName
     * @param in
     * @author Technipedia
     */
	public void copyFile(String cheminRepStore, String fileName, InputStream in) {
		try {
			// write the inputStream to a FileOutputStream
			OutputStream out = new FileOutputStream(new File(cheminRepStore+fileName));
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			in.close();
			out.flush();
			out.close();
			System.out.println("New file created!");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	/**
	 * 
	 * @param fichier
	 * supprimer un fichier tempon sachant son pointeur
	 */
	public void dropTempFile(UploadedFile fichier){
		 try {
			    
			 	files.remove(fichier);
			 	
			  }
			  catch (Exception e) {
			   System.out.println(e.toString());
			  } 
	}
	
	
	/**
	 * 
	 * @param indexFichier
	 * supprimer un fichier tempon sachant son index
	 */
	public void dropTempFileByIndex(int indexFichier){
		 try {
			    
			 	files.remove(indexFichier);				 	

			  }
			  catch (Exception e) {
			   System.out.println(e.toString());
			  } 
	}	
	
/*********************FIN TRAITEMENT DE FICHIER OMAR***************************************/
	
		
    /**
     * Pour la selection dans l'autocompletion
     * @param event
     */
    public void onItemSelect(SelectEvent event) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Item Selected", event.getObject().toString()));
    }
    
	/**
	 * Traiter un bordereau revien ï¿½ uploader son fichier physique  
	 * @param fileUploadEvent
	 */
    @Transactional
	public void traiterBordereau(FileUploadEvent fileUploadEvent) {   
    	FacesContext context = FacesContext.getCurrentInstance();
    	RequestContext requestContext = RequestContext.getCurrentInstance();
    	//JOptionPane.showMessageDialog(null, "les bordereaux non traités : "+listBordereauNonTraite.size()+"   et les fichiers Joints : "+files.size()+" Le bordereau :"+bordereau.getNumbordereau());
		 
	 try{  
					 
			 //recupï¿½ration de la date courante java et fabrication de la date Sql 		 
			 SimpleDateFormat formater = null;			
			 java.util.Date aujourdhui = new java.util.Date();			
			 
			 //recupï¿½ration de la date courante java pourfabrication du numero d'enregistrement 		 
			 SimpleDateFormat formater1 = null;	
			 SimpleDateFormat formater2 = null;	
			 formater1 = new SimpleDateFormat("ddMMyyyy");	
			 formater2 = new SimpleDateFormat(IConstance.PARAMETER_DATE_FORMAT_2);	
			 String ddMMyyyyAujourdhui = formater1.format(aujourdhui);
	         String dateTimeSql = formater2.format(aujourdhui);
	         
			 //Enregitrement des pieces jointes
			 //HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();			 
		     
			 //chemin complet du repertoire du projet courant dï¿½ployï¿½
		     //String defaultSystemDirFileUpload  = request.getSession().getServletContext().getRealPath("/");
		     
			 //Separateur des dossiers du systï¿½me
	         //String separateurRepSyst =  File.separator;
	         
	         //Chemin fixï¿½ par moi du rï¿½petoire des fichier uploadï¿½
	        // String destinationDirFileUpload = defaultSystemDirFileUpload.concat("ressourcesUpload");	
	         
		     String defaultSystemDirFileUpload  = FacesContext.getCurrentInstance().getExternalContext().getRealPath("");	    			
			 defaultSystemDirFileUpload=defaultSystemDirFileUpload+File.separator+"uplaoded"+File.separator+"bordereaux";
		     
		     //Chemin complet du sous repertoire d'enregistrement des piï¿½ces jointes de ce courrier ï¿½ completer
			 //JOptionPane.showMessageDialog(null, "les bordereaux non traités : "+listBordereauNonTraite.size()+"   et les fichiers Joints : "+files.size()+" Le bordereau :"+bordereau.getNumbordereau());
			 String nomSousRepStorePJCourrierCourant = bordereau.getNumbordereau()+"PJ";// le nom  du sous-repertoire qui contient les piï¿½ces jointes du courrier, il est composï¿½ du mot courrrier suvi du numï¿½ro d'enregistrement du courrier et se termine par PJ
		     String cheminCompletSousRepStockagePJ = defaultSystemDirFileUpload+File.separator+nomSousRepStorePJCourrierCourant;//utilisation rovisoire doit fabriquer le chemin coomplet du reperrtoire de stockage et le remplacer

		     
		     //crï¿½er le sous repertoire des pieces jointes ï¿½ uploader pour le courrier
		     File pointeurSousRepPJ = new File(cheminCompletSousRepStockagePJ);
		     if (pointeurSousRepPJ.exists()) {
		            System.out.println("Le dossier existe déja: " + pointeurSousRepPJ.getAbsolutePath());
		        } else {
		            if (pointeurSousRepPJ.mkdir()) {
		                System.out.println("Ajout du dossier : " + pointeurSousRepPJ.getAbsolutePath());
		            } else {
		                System.out.println("Echec sur le dossier : " + pointeurSousRepPJ.getAbsolutePath());
		            }
		        }
		     cheminCompletSousRepStockagePJ += File.separator;
		     
		     
				if(listEspaceCourrierBor.size() <= 0)	{	 //le bordereau est vide
				
	    			 bordereau = null;
	    			 //orderActivationTraitementBordDialog = false;
	    			 FacesMessage message = Messages.getMessage("messages", "info.echec.traite.bordereau.vide", null);
	    				message.setSeverity(FacesMessage.SEVERITY_INFO);
				        context.addMessage(null, message);
				        requestContext.execute("addDialog.hide()");				
				}				
	 			else//le bordereau n'est pas vide
				if(files != null  &&  files.size()>0) {
					
				     //enregistre les fichiers joints dans la repertoire physique
			        for(UploadedFile uploadFile : files)
				    	 copyFile(cheminCompletSousRepStockagePJ, uploadFile.getFileName(), uploadFile.getInputstream());
			        
			        //Complete les attribut du bordereau
				     bordereau.setCheminPj(cheminCompletSousRepStockagePJ);//Il s'agit du chemin du sousrepertoire qui contiendra les fichiers composant le bordereau 
				     bordereau.setDatetraitement(dateTimeSql);
				     bordereau.setEstTraite(true);
				     bordereau.setUserupdateid(utilisateurCourant.getId());
				     
				     //vider la liste des fichiers joins
				     this.files = null;
					 
					 //modification du bordereau
				     bordereauRessourceService.save(bordereau);
				     mouchardRessourceService.tracage("Traitement du bordereau de numéro : "+bordereau.getNumbordereau(), utilisateurCourant);
				     
					 //reinitialise la vue en rechargeant la nouvelle liste
			    	 this.init();
			    	 
			    	 FacesMessage message = Messages.getMessage("messages", "info.success.traitement.bordereau", null);
				    	message.setSeverity(FacesMessage.SEVERITY_INFO);
				        context.addMessage(null, message);
					 
			    	// context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"","info.success.traitement.bordereau"));				
				}
				else
				{
					 mouchardRessourceService.tracage("Tentative et echec de traitement du bordereau de numero "+bordereau.getNumbordereau()+"( Sans précision de fichier joint) ", utilisateurCourant);
			    	 FacesMessage message = Messages.getMessage("messages", "info.echec.traitement.bordereau.pj.manquante", null);
				    	message.setSeverity(FacesMessage.SEVERITY_INFO);
				        context.addMessage(null, message);
				        
					     //vider la liste des fichiers joins
					     this.files = null;
				}
			 

	 }catch(Exception e){
		 e.printStackTrace();
		 mouchardRessourceService.tracage("Tentative et echec de traitement d'un bordereau! ", utilisateurCourant);
    	 FacesMessage message = Messages.getMessage("messages", "info.echec.traitement.bordereau", null);
	    	message.setSeverity(FacesMessage.SEVERITY_INFO);
	        context.addMessage(null, message);
		// context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"","info.echec.traitement.bordereau"));
	        
		     //vider la liste des fichiers joins
		     this.files = null;
	 }finally{
		 listEspaceCourrierBor=new ArrayList<EspaceCourrier>();
	 }
	 
 
  }
    
/*    public void traiterBordereau(FileUploadEvent fileUploadEvent) {   
    	FacesContext context = FacesContext.getCurrentInstance();
    	
		 
	 try{  
		
			
			 //recup�ration de l'utilisateur courant
		     UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			 User utilisateurCourant = userService.findByLogin(user.getUsername());//.findByLogin(user.getUsername());//Povisoire car il faudra prendre l'utilisateur courant 
			 
			 //recup�ration de la date courante java et fabrication de la date Sql 		 
			 SimpleDateFormat formater = null;			
			 java.util.Date aujourdhui = new java.util.Date();			
			 
			 //recup�ration de la date courante java pourfabrication du numero d'enregistrement 		 
			 SimpleDateFormat formater1 = null;	
			 SimpleDateFormat formater2 = null;	
			 formater1 = new SimpleDateFormat("ddMMyyyy");	
			 formater2 = new SimpleDateFormat(IConstance.PARAMETER_DATETIME_FORMAT_SQL);	
			 String ddMMyyyyAujourdhui = formater1.format(aujourdhui);
	         String dateTimeSql = formater2.format(aujourdhui);
	         
			 //Enregitrement des pieces jointes
			 HttpServletRequest request=(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();			 
		     
			 //chemin complet du repertoire du projet courant d�ploy�
		     String defaultSystemDirFileUpload  = request.getSession().getServletContext().getRealPath("/");
		     
			 //Separateur des dossiers du syst�me
	         String separateurRepSyst =  File.separator;
	         
	         //Chemin fix� par moi du r�petoire des fichier upload�
	         String destinationDirFileUpload = defaultSystemDirFileUpload.concat("ressourcesUpload");	
	         
		     String defaultSystemDirFileUpload  = FacesContext.getCurrentInstance().getExternalContext().getRealPath("");	    			
			 defaultSystemDirFileUpload=defaultSystemDirFileUpload+File.separator+"uplaoded"+File.separator+"bordereaux";
		     
		     //Chemin complet du sous repertoire d'enregistrement des pi�ces jointes de ce courrier � completer
			 //JOptionPane.showMessageDialog(null, "le bordereau : "+bordereau.getNumbordereau());
			 String nomSousRepStorePJCourrierCourant = bordereau.getNumbordereau()+"PJ";// le nom  du sous-repertoire qui contient les pi�ces jointes du courrier, il est compos� du mot courrrier suvi du num�ro d'enregistrement du courrier et se termine par PJ
		     String cheminCompletSousRepStockagePJ = defaultSystemDirFileUpload+File.separator+nomSousRepStorePJCourrierCourant;//utilisation rovisoire doit fabriquer le chemin coomplet du reperrtoire de stockage et le remplacer

		     
		     //cr�er le sous repertoire des pieces jointes � uploader pour le courrier
		     File pointeurSousRepPJ = new File(cheminCompletSousRepStockagePJ);
		     if (pointeurSousRepPJ.exists()) {
		            System.out.println("Le dossier existe dꫡ: " + pointeurSousRepPJ.getAbsolutePath());
		        } else {
		            if (pointeurSousRepPJ.mkdir()) {
		                System.out.println("Ajout du dossier : " + pointeurSousRepPJ.getAbsolutePath());
		            } else {
		                System.out.println("Echec sur le dossier : " + pointeurSousRepPJ.getAbsolutePath());
		            }
		        }
		     cheminCompletSousRepStockagePJ += File.separator;
		     

				if(files != null) {
					
				     //enregistre les fichiers joints dans la repertoire physique
			        for(UploadedFile uploadFile : files)
				    	 copyFile(cheminCompletSousRepStockagePJ, uploadFile.getFileName(), uploadFile.getInputstream());
			        
			        //Complete les attribut du bordereau
				     bordereau.setCheminPj(cheminCompletSousRepStockagePJ);//Il s'agit du chemin du sousrepertoire qui contiendra les fichiers composant le bordereau 
				     bordereau.setDatetraitement(dateTimeSql);
				     bordereau.setEstTraite(true);
				     bordereau.setUserupdateid(utilisateurCourant.getId());
				     
				     //vider la liste des fichiers joins
				     this.files = null;
					 
					 //modification du bordereau
				     bordereauRessourceService.save(bordereau);
					 
					 //reinitialise la vue en rechargeant la nouvelle liste
			    	 this.init();
			    	 
			    	 FacesMessage message = Messages.getMessage("messages", "info.success.traitement.bordereau", null);
				    	message.setSeverity(FacesMessage.SEVERITY_INFO);
				        context.addMessage(null, message);
					 
			    	// context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"","info.success.traitement.bordereau"));
				

				}
				else
				{
			    	 FacesMessage message = Messages.getMessage("messages", "info.echec.traitement.bordereau.pj.manquante", null);
				    	message.setSeverity(FacesMessage.SEVERITY_INFO);
				        context.addMessage(null, message);
					
				}
			 

	 }catch(Exception e){
		 e.printStackTrace();
		
    	 FacesMessage message = Messages.getMessage("messages", "info.echec.traitement.bordereau", null);
	    	message.setSeverity(FacesMessage.SEVERITY_INFO);
	        context.addMessage(null, message);
		// context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"","info.echec.traitement.bordereau"));
		 
	 }
 
  }*/
	
    public void editEvent(Integer id) {
        
        this.bordereau = bordereauRessourceService.load(id);       

    }
	
    public void delete(ActionEvent actionEvent) {
    	
   	 FacesContext context = FacesContext.getCurrentInstance();
   	 RequestContext requestContext = RequestContext.getCurrentInstance();
      try{
   	  
   	   if(selectedBordereaus.length>0)
	    	   for(int i=0;i<selectedBordereaus.length;i++){	
	    		  
	    		   bordereauRessourceService.deleteVersusDesabled(selectedBordereaus[i], IConstance.FIELD_DELETE);
	    		   mouchardRessourceService.tracage("Suppression du bordereau de numero : "+selectedBordereaus[i].getNumbordereau(), utilisateurCourant);
		    		
		    	}
   	   else   { 	 
		    		bordereauRessourceService.deleteVersusDesabled(bordereau, IConstance.FIELD_DELETE);
   	   				mouchardRessourceService.tracage("Suppression du bordereau de numero : "+bordereau.getNumbordereau(), utilisateurCourant);
   	   }
		    	init();//mise Ã  jour de la liste
		    	
		    	 FacesMessage message = Messages.getMessage("messages", "global.gestion.delete", null);
			    	message.setSeverity(FacesMessage.SEVERITY_INFO);
			        context.addMessage(null, message);				    	
		        requestContext.execute("deleteDialog.hide()");	
   	  
   	   
      }catch(Exception e){
   	   e.printStackTrace();
   	   FacesMessage message = Messages.getMessage("messages", "global.gestion.echec", null);
	    	message.setSeverity(FacesMessage.SEVERITY_WARN);
	        context.addMessage(null, message);
 	 }finally{
 		selectedBordereaus=null;
 		bordereau=null;
	   
 	 }

   }
	


	/**
	 * @return the bordereau
	 */
	public Bordereau getBordereau() {
		return bordereau;
	}

	/**
	 * @param bordereau the bordereau to set
	 */
	public void setBordereau(Bordereau bordereau) {
		this.bordereau = bordereau;
	}

	/**
	 * @return the bordereauList
	 */
	public List<Bordereau> getBordereauList() {
		return bordereauList;
	}

	/**
	 * @param bordereauList the bordereauList to set
	 */
	public void setBordereauList(List<Bordereau> bordereauList) {
		this.bordereauList = bordereauList;
	}

	/**
	 * @return the bordereauListDataModel
	 */
	public BordereauDataModel getBordereauListDataModel() {
		return bordereauListDataModel;
	}

	/**
	 * @param bordereauListDataModel the bordereauListDataModel to set
	 */
	public void setBordereauListDataModel(BordereauDataModel bordereauListDataModel) {
		this.bordereauListDataModel = bordereauListDataModel;
	}

	/**
	 * @return the listEspacecourrier
	 */
	public List<EspaceCourrier> getListEspacecourrier() {
		return listEspacecourrier;
	}

	/**
	 * @param listEspacecourrier the listEspacecourrier to set
	 */
	public void setListEspacecourrier(List<EspaceCourrier> listEspacecourrier) {
		this.listEspacecourrier = listEspacecourrier;
	}

	/**
	 * @return the selectedBordereaus
	 */
	public Bordereau[] getSelectedBordereaus() {
		return selectedBordereaus;
	}

	/**
	 * @param selectedBordereaus the selectedBordereaus to set
	 */
	public void setSelectedBordereaus(Bordereau[] selectedBordereaus) {
		this.selectedBordereaus = selectedBordereaus;
	}

	/**
	 * @return the filteredBordereaus
	 */
	public List<Bordereau> getFilteredBordereaus() {
		return filteredBordereaus;
	}

	/**
	 * @param filteredBordereaus the filteredBordereaus to set
	 */
	public void setFilteredBordereaus(List<Bordereau> filteredBordereaus) {
		this.filteredBordereaus = filteredBordereaus;
	}

	/**
	 * @return the bordereauRessourceService
	 */
	public IBordereauRessourceService getBordereauRessourceService() {
		return bordereauRessourceService;
	}

	/**
	 * @param bordereauRessourceService the bordereauRessourceService to set
	 */
	public void setBordereauRessourceService(
			IBordereauRessourceService bordereauRessourceService) {
		this.bordereauRessourceService = bordereauRessourceService;
	}

	/**
	 * @return the espaceCourrierRessourceService
	 */
	public IEspaceCourrierRessourceService getEspaceCourrierRessourceService() {
		return espaceCourrierRessourceService;
	}

	/**
	 * @param espaceCourrierRessourceService the espaceCourrierRessourceService to set
	 */
	public void setEspaceCourrierRessourceService(
			IEspaceCourrierRessourceService espaceCourrierRessourceService) {
		this.espaceCourrierRessourceService = espaceCourrierRessourceService;
	}

	/**
	 * @return the courriersRessourceService
	 */
	public ICourriersRessourceService getCourriersRessourceService() {
		return courriersRessourceService;
	}

	/**
	 * @param courriersRessourceService the courriersRessourceService to set
	 */
	public void setCourriersRessourceService(
			ICourriersRessourceService courriersRessourceService) {
		this.courriersRessourceService = courriersRessourceService;
	}

	/**
	 * @return the files
	 */
	public List<UploadedFile> getFiles() {
		return files;
	}

	/**
	 * @param files the files to set
	 */
	public void setFiles(List<UploadedFile> files) {
		this.files = files;
	}

	/**
	 * @return the userService
	 */
	public IUserService getUserService() {
		return userService;
	}

	/**
	 * @param userService the userService to set
	 */
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	/**
	 * @return the espaceRessourceService
	 */
	public IEspaceRessourceService getEspaceRessourceService() {
		return espaceRessourceService;
	}

	/**
	 * @param espaceRessourceService the espaceRessourceService to set
	 */
	public void setEspaceRessourceService(
			IEspaceRessourceService espaceRessourceService) {
		this.espaceRessourceService = espaceRessourceService;
	}

	/**
	 * @return the orderActivationNumbordereau
	 */
	public Boolean getOrderActivationNumbordereau() {
		return orderActivationNumbordereau;
	}

	/**
	 * @param orderActivationNumbordereau the orderActivationNumbordereau to set
	 */
	public void setOrderActivationNumbordereau(Boolean orderActivationNumbordereau) {
		this.orderActivationNumbordereau = orderActivationNumbordereau;
	}

	/**
	 * @return the listBordereauNonTraite
	 */
	public List<Bordereau> getListBordereauNonTraite() {
		return listBordereauNonTraite;
	}

	/**
	 * @param listBordereauNonTraite the listBordereauNonTraite to set
	 */
	public void setListBordereauNonTraite(List<Bordereau> listBordereauNonTraite) {
		this.listBordereauNonTraite = listBordereauNonTraite;
	}

	/**
	 * @return the applicationScopeBean
	 */
	public ApplicationBean getApplicationScopeBean() {
		return applicationScopeBean;
	}

	/**
	 * @param applicationScopeBean the applicationScopeBean to set
	 */
	public void setApplicationScopeBean(ApplicationBean applicationScopeBean) {
		this.applicationScopeBean = applicationScopeBean;
	}

	/**
	 * @return the utilisateurCourant
	 */
	public User getUtilisateurCourant() {
		return utilisateurCourant;
	}

	/**
	 * @param utilisateurCourant the utilisateurCourant to set
	 */
	public void setUtilisateurCourant(User utilisateurCourant) {
		this.utilisateurCourant = utilisateurCourant;
	}

	/**
	 * @return the mouchardRessourceService
	 */
	public IMouchardRessourceService getMouchardRessourceService() {
		return mouchardRessourceService;
	}

	/**
	 * @param mouchardRessourceService the mouchardRessourceService to set
	 */
	public void setMouchardRessourceService(
			IMouchardRessourceService mouchardRessourceService) {
		this.mouchardRessourceService = mouchardRessourceService;
	}

	/**
	 * @return the orderActivationTraitementBordDialog
	 */
	public Boolean getOrderActivationTraitementBordDialog() {
		return orderActivationTraitementBordDialog;
	}

	/**
	 * @param orderActivationTraitementBordDialog the orderActivationTraitementBordDialog to set
	 */
	public void setOrderActivationTraitementBordDialog(
			Boolean orderActivationTraitementBordDialog) {
		this.orderActivationTraitementBordDialog = orderActivationTraitementBordDialog;
	}

	public List<EspaceCourrier> getListEspaceCourrierBor() {
		return listEspaceCourrierBor;
	}

	public void setListEspaceCourrierBor(List<EspaceCourrier> listEspaceCourrierBor) {
		this.listEspaceCourrierBor = listEspaceCourrierBor;
	}

	/**
	 * @return the temp
	 */
	public File getTemp() {
		return temp;
	}

	/**
	 * @param temp the temp to set
	 */
	public void setTemp(File temp) {
		this.temp = temp;
	}

	/**
	 * @return the chemintemp
	 */
	public String getChemintemp() {
		return chemintemp;
	}

	/**
	 * @param chemintemp the chemintemp to set
	 */
	public void setChemintemp(String chemintemp) {
		this.chemintemp = chemintemp;
	}   
	
	
    
	
    
}

//aprï¿½s 