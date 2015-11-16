/**
 * 
 */
package cm.dita.utils;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;

/**
 * @author MALINE
 *
 */
public class DateManipulation {
	
	 
	
 	public static long differenceEnJourEntre2Dates(String date1, String date2){
 		
 		final long MILISECOND_PER_DAY = 24 * 60 * 60 * 1000;
 		
 		String [] tab;
 		
 		int i;
 		
 		String date = date1.trim();
 		
 		if(date.contains(" ")){
 			
	 		tab = date.split(" ");
	 		i=0;
	 		
	 		//for(int i=0; i<tab)
	 		
	 		while(!tab[i].contains("-")){
	 			i +=1;
	 		}
	 		
	 		date = tab[i]; 
	 		
	 		i+=1;
	 		if(tab[i] != null && tab[i].contains(":")){
	 			
		 		date += "-"+tab[i].replace(":", "-");
		 		
	 		}
	 		
 		}
 		 		
 		String[] tab2 = date.split("-");
 		
 		Date dateBegin = null;
 		
 		if(tab2.length > 3){
 			
 	 		if(tab2[0].length() == 4){//la date commence par l'année
 	 			
 	            /** La date du départ*/
 	 			dateBegin = new GregorianCalendar(Integer.parseInt(tab2[0]),Integer.parseInt(tab2[1]),Integer.parseInt(tab2[2]),Integer.parseInt(tab2[3]),Integer.parseInt(tab2[4]),Integer.parseInt(tab2[5])).getTime( );

 	 			
 	 		}
 	 		else{//la date ne commence pas par l'année
 	 			
 	            /** La date du départ*/
 	 			dateBegin = new GregorianCalendar(Integer.parseInt(tab2[2]),Integer.parseInt(tab2[1]),Integer.parseInt(tab2[0]),Integer.parseInt(tab2[3]),Integer.parseInt(tab2[4]),Integer.parseInt(tab2[5])).getTime( );

 	 		}
 			
 		}
 		else{
 			
 	 		if(tab2[0].length() == 4){//la date commence par l'année
 	 			
 	            /** La date du départ*/
 	 			dateBegin = new GregorianCalendar(Integer.parseInt(tab2[0]),Integer.parseInt(tab2[1]),Integer.parseInt(tab2[2]),00,00,00).getTime( );

 	 			
 	 		}
 	 		else{//la date ne commence pas par l'année
 	 			
 	            /** La date du départ*/
 	 			dateBegin = new GregorianCalendar(Integer.parseInt(tab2[2]),Integer.parseInt(tab2[1]),Integer.parseInt(tab2[0]),00,00,00).getTime( );

 	 		}
 			
 		}
 		

 		
 		
 		
/**
* Pour la date de fin	 		
*/
 		date = date2.trim();
 		
 		if(date.contains(" ")){
 			
 	 		tab = date.split(" ");
 	 		i=0;
 	 		while(!tab[i].contains("-")){
 	 			i +=1;
 	 		}
 	 		
 	 		date = tab[i]; 
 	 		
 	 		i+=1;
	 		if(tab[i] != null && tab[i].contains(":")){
	 			
		 		date += "-"+tab[i].replace(":", "-");
		 		
	 		}
 	 		
 		}

 		tab2 = date.split("-");
 		Date dateEnd = null;
 		
 		if(tab2.length > 3){
 			
 			if(tab2[0].length() == 4){//la date commence par l'année
 	 			
 	            /** La date de fin*/
 	 			dateEnd = new GregorianCalendar(Integer.parseInt(tab2[0]),Integer.parseInt(tab2[1]),Integer.parseInt(tab2[2]),Integer.parseInt(tab2[3]),Integer.parseInt(tab2[4]),Integer.parseInt(tab2[5])).getTime( );

 	 			
 	 		}
 	 		else{//la date ne commence pas par l'année
 	 			
 	            /** La date de fin*/
 	 			dateEnd = new GregorianCalendar(Integer.parseInt(tab2[2]),Integer.parseInt(tab2[1]),Integer.parseInt(tab2[0]),Integer.parseInt(tab2[3]),Integer.parseInt(tab2[4]),Integer.parseInt(tab2[5])).getTime( );

 	 		}
 			
 		}
 		else{
 			
 			if(tab2[0].length() == 4){//la date commence par l'année
 	 			
 	            /** La date de fin*/
 	 			dateEnd = new GregorianCalendar(Integer.parseInt(tab2[0]),Integer.parseInt(tab2[1]),Integer.parseInt(tab2[2]),23,59,59).getTime( );

 	 			
 	 		}
 	 		else{//la date ne commence pas par l'année
 	 			
 	            /** La date de fin*/
 	 			dateEnd = new GregorianCalendar(Integer.parseInt(tab2[2]),Integer.parseInt(tab2[1]),Integer.parseInt(tab2[0]),23,59,59).getTime( );

 	 		}
 			
 		}
 		
 		
 		JOptionPane.showMessageDialog(null,  "DATE COURANTE : "+dateEnd.getTime( )+"  DERNIERE CONNECTION : "+dateBegin.getTime( ));


        // Calcul de différence
        long diff = dateEnd.getTime( ) - dateBegin.getTime( );

        System.out.println("Différence en nombre de jour entre : "+dateBegin+ " et " + dateEnd +
                " nest " + (diff / (MILISECOND_PER_DAY)) + " jours.");
 		//return Math.round(Math.abs(diff/MILISECOND_PER_DAY));
 		//return Math.round(diff/MILISECOND_PER_DAY);
        return Math.round(diff);//c'est mieux de prendre la différence de temps en miliseconde
 	}

}
