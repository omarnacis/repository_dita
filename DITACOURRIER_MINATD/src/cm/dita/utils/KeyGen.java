package cm.dita.utils;

import java.util.Random;

public class KeyGen {
	
	private static String CARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static int TAILLE_CLE = 16;
	private static int MODULO = 997;
	private static int VOULU = 500;
 
	public static void main(String[] args) {
		new KeyGen().gen(1000);
		/*
		 * � :500
KX36XDBMGPA1ZL29 :500
AOIIDYY2O6EKA9ZY :500
CJNAB7KS5WM4PD0Z :500
2759L0ZW72AQQGQT :500
ZRWLREV5AJ4PBBLC :500
LDNKKN4IVAW3M9F5 :500
ZBD66EENWF1ABHHQ :500
ALOLVNOUP36KK0G3
		 */
		
		
		//String t="";
		//System.out.println(" correct :" + new KeyGen().hash("ZRWLREV5AJ4PBBLC"));
	}
 
	private void gen(int i) {
		// Clé de départ
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < TAILLE_CLE; j++) {
			//sb.append(CARS.charAt(random.nextInt(TAILLE_CLE)));
			sb.append(CARS.charAt(random.nextInt(CARS.length())));
		}
		// boucle de recherche
		do {
			int hash = hash(sb.toString());
			
			if(hash == VOULU) {
				//break;
				System.out.println(sb + " :" + hash);
			}
			sb.setCharAt(random.nextInt(TAILLE_CLE), CARS.charAt(random.nextInt(CARS.length())));
		} while(true);
	}
 
	/**
	 * Calcule le hash de la clé, avec modulo
	 * @param key
	 * @return
	 */
	private int hash(String key) {
		if(key == null || key.length() != TAILLE_CLE) {
			throw new IllegalArgumentException("Clé invalide: " + key);
		}
		key = key.toUpperCase();
		int n = 0;
		for (int i = 0; i < key.length(); i++) {
			char car = key.charAt(i);
			int index = CARS.indexOf(car);
			if(index == -1) {
				throw new IllegalArgumentException("Caractère de la clé invalide: " + car);
			}
			n += (i + 1) * index;
		}
		return n % MODULO;
	}
	
	/**
	 * Valide la clés
	 * @param key
	 * @return 
	 */
	
	public  boolean valideKey(String key){
		try{
		if(hash(key)== VOULU)
			return true;
		else
			return false;
		}catch(Exception e){
			return false;
		}
	}
	
	public  int code(String key){
		return hash(key);
	}

}
