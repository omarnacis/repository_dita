package cm.dita.quartz;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.springframework.context.ApplicationContext;

import cm.dita.entities.Alarmes;
import cm.dita.service.domaine.inter.IAlarmesRessourceService;
import cm.dita.service.domaine.inter.IPreferencesRessourceService;
import cm.dita.utils.ApplicationContextHolder;
import cm.dita.utils.MailMessage;
import cm.dita.utils.MailSender;
import cm.dita.utils.VigenereCipher;

public class MailCourrier {
	
	
	public void runAction() throws ParseException {
		//System.out.println("Notification sur les stock");
				
		ApplicationContext context = ApplicationContextHolder.getContext();
		 
		IAlarmesRessourceService alarmesRessourceService = (IAlarmesRessourceService) context.getBean("alarmesRessourceService");	
		IPreferencesRessourceService preferencesService =  (IPreferencesRessourceService) context.getBean("preferencesRessourceService");
		
		List<Alarmes> liste=alarmesRessourceService.listAlarmestoSend();
		
		//String smtp=preferencesService.getParameterByName(nom)
		String	SERVEUR_SMTP=preferencesService.getParameterByName("SERVEUR_SMTP").getPreferenceValue();
		String PORT_SERVEUR_SMTP=preferencesService.getParameterByName("PORT_SERVEUR_SMTP").getPreferenceValue();
		String ADRESSE_COMPTE_ENVOI_MAIL=preferencesService.getParameterByName("ADRESSE_COMPTE_ENVOI_MAIL").getPreferenceValue();
		String PWD_COMPTE_ENVOI_MAIL=preferencesService.getParameterByName("PWD_COMPTE_ENVOI_MAIL").getPreferenceValue();
		
		//String OBJET_MAIL=preferencesService.getParameterByName("OBJET_MAIL").getPreferenceValue();
		//String SCORPS_MAIL=preferencesService.getParameterByName("CORPS_MAIL").getPreferenceValue();
		System.out.println("********************************** DEBUT DEMOM");
		
		VigenereCipher vc = new VigenereCipher();
		
		final MailMessage msg = new MailMessage();
        final MailSender mail2 = new MailSender(SERVEUR_SMTP,Integer.parseInt(PORT_SERVEUR_SMTP),ADRESSE_COMPTE_ENVOI_MAIL, vc.decrypt(PWD_COMPTE_ENVOI_MAIL), true);
        
        for(int i=0;i<liste.size();i++){
        	
        	try {
				msg.setFrom(new InternetAddress(ADRESSE_COMPTE_ENVOI_MAIL));
				 msg.setTo(liste.get(i).getEmail());
		            msg.setSubject(liste.get(i).getObjet());
		            msg.setContent(liste.get(i).getCorps(), true);
		            mail2.sendMessage(msg);		            
		            liste.get(i).setAlarmetat(true);
		            
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				liste.get(i).setNbrEssai(liste.get(i).getNbrEssai()+1);
				if(liste.get(i).getNbrEssai()>6)
					liste.get(i).setAlarmetat(true);
			}finally{
				
					alarmesRessourceService.update(liste.get(i));
				
			}
        	
        //	System.out.println("********************************** FIN DEMOM");
           
        }
        // Message avec texte html + images incluses + pi�ces jointes
        
      //  msg.setAttachmentURL(new String[] { "c:\\fichier.csv", "c:\\fichier1.csv",
            	//"c:\\fichier2.csv", "c:\\numero.csv"});
        
		
		
			
	}

}
