package cm.dita.utils;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Main extends Object {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	//	import java.util.prefs.Preferences; 
		
		java.util.prefs.Preferences lPrf2 = java.util.prefs.Preferences.userRoot().node( "reg_cour/key" );
		try {
			lPrf2.removeNode();
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		      Preferences lPrf = Preferences.userRoot().node( "Test/My App" ); 
		      Long t=System.currentTimeMillis();
		  /*    Long t2=System.currentTimeMillis();
		      lPrf.putLong( "My Key", t ); 
		      System.out.println( lPrf.getLong("My Key", t2));
		      System.out.println(" y="+t);
		      System.out.println(lPrf.getLong("My Key", t2));
		      System.out.println(lPrf.absolutePath());*/
		
		  System.out.println( lPrf.getLong("mbe", t)==t);
		
	}

}
