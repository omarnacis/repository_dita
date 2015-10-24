package cm.dita.utils;

import java.awt.Desktop;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;







import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;



import net.sf.jasperreports.view.JasperViewer;
import cm.dita.entities.Courriers;



public class TestJqspertRepport {
	
	private String realPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
	private static String partName=System.currentTimeMillis()+"";
	
	
	
	 /**
	 * @return the partName
	 */
	public String getPartName() {
		return partName;
	}

	/**
	 * @param partName the partName to set
	 */
	public void setPartName(String partName) {
		this.partName = partName;
	}

	/**
	 * @return the realPath
	 */
	public String getRealPath() {
		return realPath;
	}

	/**
	 * @param realPath the realPath to set
	 */
	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}

	public void testJqsper() {
         try {
                 //InputStream file = Main.class.getClassLoader().getResourceAsStream(realPath+"reporting"+File.separator+"Simple_rapport.jrxml");
                 System.out.println("1 CHEMIN : "+realPath+"reporting"+File.separator+"Simple_rapport.jrxml" );
                 //JasperDesign jasperDesign = JRXmlLoader.load(file);
                 JasperDesign jasperDesign = JRXmlLoader.load(realPath+"reporting"+File.separator+"Simple_rapport.jrxml");
                 System.out.println("2 CHEMIN : "+realPath );
                 JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                 System.out.println("3 CHEMIN : "+realPath );
                 Map<String, Object> parameters = new HashMap<String, Object>();
                 parameters.put("Titre", "UN TITRE");
                 JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters);
                 System.out.println("4 CHEMIN : "+realPath );
                 JasperExportManager.exportReportToPdfFile(jasperPrint, "c:"+File.separator+"Simple_report"+partName + ".pdf");
                 System.out.println("5 CHEMIN : "+realPath );
                
         } catch (Exception e) {
                 System.out.println("Impossible de créer le rapport: " + e.getMessage());
         }
 }
	
//public String visualiserReportPDF(String cheminfichierModel, List<Courriers>  dataSource) {//recuperer la source de données et compiler le fichier .jrxml
	public File visualiserReportPDF(String cheminfichierModel, List<Courriers>  dataSource) {//recuperer la source de données et compiler le fichier .jrxml
		
    	
    	try{
    		
    		 
    		
    		//JasperDesign  design = JRXmlLoader.load(realPath+"/reporting/bordereau2.jrxml");//pointer sur le fichier model qui sera compiler
    		JasperDesign  design = JRXmlLoader.load(realPath+"reporting"+File.separator+cheminfichierModel);//pointer sur le fichier model qui sera compiler
    		//JOptionPane.showMessageDialog(null, " load reussie: ");
    		JasperReport jr=JasperCompileManager.compileReport(design);//compilé et retourne la jr
    		//JOptionPane.showMessageDialog(null, "compilation ressussie: ");
    		
    		Map<String, Object> param = new HashMap<>();
    	
    		//JOptionPane.showMessageDialog(null, "debut chargement");
    		
    		//JasperPrint	jp=JasperFillManager.fillReport(jr, param,new JRBeanCollectionDataSource(listeRegions));//chargement des données
    		JasperPrint	jp=JasperFillManager.fillReport(jr, param,new JRBeanCollectionDataSource(dataSource));//chargement des données
    	
    		//JasperViewer.viewReport(jp,true);
    		JasperViewer Jv = new JasperViewer(jp, false); // pour l'aperçu avant impression, le false empèche l'arrêt de l'application
            Jv.setTitle("Aperçu");
            Jv.setVisible(true);
    		

    		
    		//JOptionPane.showMessageDialog(null, "fin chargement");
    		testJqsper();
    		
    		 return exporToPdfFile(jp, realPath, (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest(), 
    				
    				FacesContext.getCurrentInstance().getExternalContext(),
    				FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream(), "testBordereau", "reporting");
    		//JOptionPane.showMessageDialog(null, "Voulez Enregistrer l'indicateur de sigle 4: "); 
    		//FacesContext.getCurrentInstance().responseComplete();
    	
    		
    	}catch(Exception ex){
    		ex.printStackTrace();
    		System.out.println(ex.getMessage());
    		
    	}
    	
    	
    	return null;
}
    
        
    
       
       /**
   	 * envoie le flux d'un fichier vers le navigateur
   	 * @param request
   	 * @param response
   	 * @param file
   	 * @param out
   	 * @param typefile
   	 * @param content
   	 * @param nameFile
   	 */
   	public static void exportToBroweser(HttpServletRequest request,
   			ExternalContext ec, File file, OutputStream out,
   			String typefile, String content, String nameFile, String extension) {

   		try {

   			ec.responseReset();
   			BufferedInputStream from = null;
   			ec.setResponseContentLength((int) file.length());
   			int bufferSize = 8 * 1024;
   			from = new BufferedInputStream(new FileInputStream(file),
   					bufferSize * 2);
   			byte[] bufferFile = new byte[bufferSize];
   			ec.setResponseContentType(content);
   			ec.setResponseHeader("Content-Disposition", "attachment;filename=\""
   					+ nameFile+extension+ "\"");
   			for (int i = 0;; i++) {
   				int len = from.read(bufferFile);
   				if (len < 0)
   					break;
   			  	out.write(bufferFile, 0, len);
   			}
   			out.flush();
   			from.close();
   			/************************************************************/
   			
   			/*********************************************************/
   			out.close();
   			//getConnection().close();
   		} catch (Exception e) {
   			e.printStackTrace();
   			//NotificationMessagesHelper.saveError(request.getSession(),
   				//	"error.when.downloading." + typefile, null);
   		}

   	}
   			
   			
   			
   				/**
   	 * genÃ¨re un Ã©tat au format pdf
   	 * @param jasperPrint
   	 * @param realPath
   	 * @param request
   	 * @param response
   	 * @param out
   	 * @param nameFile
   	 * @param folderName  //repertoire dans lequel on genÃ¨re le fichier 
   	 */
   	//public static  void exporToPdfFile(JasperPrint jasperPrint, String realPath,
   	public static  File exporToPdfFile(JasperPrint jasperPrint, String realPath,
   			HttpServletRequest request, ExternalContext ec,
   			OutputStream out, String nameFile,String folderName) throws Exception {
   		   
   	 File temp = File.createTempFile("testBrdereau"+partName, ".pdf");
   		// - CrÃ©ation du rapport au format PDF
   		try {
   			JasperExportManager.exportReportToPdfFile(jasperPrint, realPath+folderName+File.separator+ nameFile+partName + ".pdf");
   			//JasperExportManager.exportReportToPdfFile(jasperPrint, "c:"+File.separator+"testBrdereau"+partName + ".pdf");
   			
   		   // Créer un fichier temporaire clip
   		   
   		    System.out.println("Chemin du fichier tempon AVANT : "+temp.length());
   			JasperExportManager.exportReportToPdfFile(jasperPrint, temp.getAbsolutePath());
   			System.out.println("Chemin du fichier tempon : "+temp.getAbsolutePath());
   			System.out.println("Chemin du fichier tempon : "+temp.length());
   		
   			
   			File file = new File(realPath+folderName+File.separator+ nameFile+partName + ".pdf");
   			exportToBroweser(request, ec, file, out, "pdffile", "application/pdf", nameFile, ".pdf");
   			
   			
   			 			
   			
   			System.out.println("Nom du fichier : "+file.getAbsolutePath());
    		
   			file.delete();
   		} catch (JRException e) {
   			e.printStackTrace();
   			// throw new Exception(ReportUtils.class.toString(), e.getMessage(), e);
   		}
        
   		return temp;
   	}


}
