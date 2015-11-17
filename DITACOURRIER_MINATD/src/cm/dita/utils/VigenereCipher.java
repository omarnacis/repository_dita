package cm.dita.utils;
/**
 * VigenereCipher.java
 * @author Aclan, Nelson D.
 * February 26, 2010
 *
 * Last Modified: August 27, 2010
 *
 * Assumptions:
 *    User only inputs characters (and spaces!) included in the given alphanumeric set.
 *    Numeric characters result in uppercase plain/cipher characters.
 *    Encryption/decryption key cannot have a space.
 *
 * Reference:
 *    http://en.wikipedia.org/wiki/Vigenère_cipher
 */

import java.util.Vector;
import javax.swing.JOptionPane;

public class VigenereCipher {
	/*private static final char[] alphnum = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 
		'H', 'I', 'J','K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
	'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '�', '�', '�', '�', '�', '�', '�', '�', '�', '�', '�', 
	'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 
	't', 'u', 'v', 'w', 'x', 'y', 'z', '�', '�', '�', '�', '�', '�', '�', '�', '�', '�', '0', 
	'1', '2', '3', '4', '5', '6', '7', '8', '9', ',',';', ':', '?', '.', '!', '\'', '<', '>',
	'$', '�', '%', '�','+','@', '-','='};*/
	private static final char[] alphnum = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 
		'H', 'I', 'J','K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
	'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 
	'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 
	't', 'u', 'v', 'w', 'x', 'y', 'z', 
	'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

   private static final int dim = alphnum.length;
   private char[][] vgnrSquare = new char[alphnum.length][alphnum.length];
   private Vector<Character> alphnumv = new Vector<Character>();

   public VigenereCipher() {
      init();

      /* copy contents of alphum array to alphnumv vector to exploit indexOf method */
      for(int i = 0; i < dim; i++) {
         alphnumv.add(alphnum[i]);
      }

      print();
   }

   /**
    * Initializes the contents of the Vigenere Square using the given alphanumeric set.
    */
   public void init() {
      /* Fill up the upper left triangle */
      for(int i = 0; i < dim; i++) {
         for(int j = i; j < dim; j++) {
            if(i == 0) {
               vgnrSquare[i][j] = alphnum[j];
            } else {
               vgnrSquare[i][j-i] = alphnum[j];
            }
         }
      }

      /* Fill up the remaining cells */
      for(int i = 1; i < dim; i++) {
         int k = 0; // always start from the first element of the set

         for(int j = dim-i; j < dim; j++) {
            vgnrSquare[i][j] = alphnum[k];
            k++;
         }
      }
   }

   /**
    * Encrypts the given plaintext using the specified key.
    * @param plaintext the text to be encrypted
    * @param key the encryption key
    */
   public String encrypt(String plaintext) {
	   String key="wvsms2012";
      if(keyHasSpace(key) || key.length() == 0) {
         //System.out.println("\nKey cannot contain a space!\n");
         return null;
      } else if(plaintext.length() == 0) {
        // System.out.println("\nNothing to encrypt!\n");
         return null;
      } else {
         char[] ciphertext = new char[plaintext.length()];
         key = generateKey(plaintext, key); // update key

         for(int i = 0; i < ciphertext.length; i++) {
            int row = alphnumv.indexOf(key.charAt(i));
            int col = alphnumv.indexOf(plaintext.charAt(i));
            //JOptionPane.showMessageDialog(null, plaintext.charAt(i)+" ligne="+row+" colonne="+col);
            if(plaintext.charAt(i) == ' ') {
               ciphertext[i] = ' ';
            } else {
            	if(col==-1)
            		ciphertext[i] = plaintext.charAt(i); // encryption
            	else
            	ciphertext[i] = vgnrSquare[row][col]; // encryption
              

               /* retain case */
               /*if(Character.isLowerCase(plaintext.charAt(i))) {
                  ciphertext[i] = Character.toLowerCase(ciphertext[i]);
               }*/
            }
         }

         //System.out.println("\nPlaintext:  " + plaintext);
         //System.out.println("Key:        " + key);
         return new String(ciphertext);
      }
   }

   /**
    * Decrypts the given ciphertext using the specified key.
    * @param ciphertext the text to be decrypted
    * @param key the decryption key
    */
   public String decrypt(String ciphertext) {
	   String key="wvsms2012";
      if(keyHasSpace(key) || key.length() == 0) {
         //System.out.println("\nKey cannot contain a space!\n");
         return null;
      } else if(ciphertext.length() == 0) {
        // System.out.println("\nNothing to decrypt!\n");
         return null;
      } else {
         char[] plaintext = new char[ciphertext.length()];
         key = generateKey(ciphertext, key); // update key

         for(int i = 0; i < plaintext.length; i++) {
        	 
            int row = alphnumv.indexOf(key.charAt(i));
            int col = alphnumv.indexOf(ciphertext.charAt(i));
           
            if(ciphertext.charAt(i) == ' ') {
               plaintext[i] = ' ';
            } else {
            		if(col==-1)
            			 plaintext[i]=ciphertext.charAt(i);
            		else{
		               int x = (col - row) % dim; // decryption algebraic approach
		
		               if(x >= 0) {
		                  plaintext[i] = alphnumv.elementAt(x).toString().charAt(0);
		               } else {
		                  plaintext[i] = alphnumv.elementAt(dim+x).toString().charAt(0);
		               }
            		}

               /* retain case */
              /* if(Character.isLowerCase(ciphertext.charAt(i))) {
                  plaintext[i] = Character.toLowerCase(plaintext[i]);
               }*/
            }
         }

         /*for(int i=0;i<plaintext.length;i++){
        	 
        	 plaintext[i] = Character.toLowerCase(plaintext[i]);
         }*/
         
        // System.out.println("\nCiphertext: " + ciphertext);
        // System.out.println("Key:        " + key);
         
         return  new String(plaintext);
      }
   }

   /**
    * Generates the repeating key.
    * @param text the plaintext/ciphertext which serves as the basis for the key's length
    * @param key the initial, non-repeating key
    * @return newKey - the new (repeating) key
    */
   public String generateKey(String text, String key) {
      char[] newKey = new char[text.length()];
      int repeatFlag = 0;

      for(int i = 0; i < newKey.length; i++) {
         if(text.charAt(i) == ' ') {
            newKey[i] = ' ';
         } else {
            newKey[i] = key.charAt(repeatFlag);

            if(repeatFlag+1 == key.length()) {
               repeatFlag = 0;
            } else {
               repeatFlag++;
            }
         }
      }

      return new String(newKey);
   }

   /**
    * Checks whether the inputted key contains a space.
    * @param key the key to be scanned
    * @return answer - boolean value
    */
   public boolean keyHasSpace(String key) {
      boolean answer = false;

      for(int i = 0; i < key.length(); i++) {
         if(key.charAt(i) == ' ') {
            answer = true;
            break;
         }
      }

      return answer;
   }

   /**
    * Prints the Vigenere Square.
    */
   public void print() {
     // System.out.println();

      for(int i = 0; i < dim; i++) {
         for(int j = 0; j < dim; j++) {
           // System.out.print(vgnrSquare[i][j] + " ");

            if(j == dim-1) {
              // System.out.println();
            }
         }
      }
   }

   public static void main(String[] args) {
      String choice, input, key, output,output1;
      VigenereCipher vc = new VigenereCipher();

      try {
        /* choice = (JOptionPane.showInputDialog(null, "What do you want to do?", "Vigenere Cipher",
            JOptionPane.QUESTION_MESSAGE, null, new Object[] {"Encrypt", "Decrypt"}, "Encrypt")).toString();
         */
    	//  String mot="pass=toto";
       /*  input = vc.encrypt(mot);        
         output = vc.decrypt("GdMTVScV=x5wzx22B.ooreqw");         
         output1 = vc.decrypt("RTZG");*/
       //  JOptionPane.showMessageDialog(null,output );
     //   System.out.println(output);
         
        /* 1, 'CSOFT', 'https://www.csoft.co.uk/sendsms', 'GdMTVScV=x5wzx22B.ooreqw', 'B35=qsrzwvsl', 'Message', 'from', 'SendTo', 'ResponseFormat=0', '', 0, 3, 0
         * 
         * if(choice.equals("Encrypt")) {
            input = JOptionPane.showInputDialog("Enter plaintext").trim();
            //key = JOptionPane.showInputDialog("Enter key").trim();
            output = vc.encrypt(input);
         } else {
            input = JOptionPane.showInputDialog("Enter ciphertext").trim();
            key = JOptionPane.showInputDialog("Enter key").trim();
            output = vc.decrypt(input);
         }*/

      /*   if(output != null) {
            System.out.println(output);
         }*/
      } catch(NullPointerException npe) {
         System.out.println("\nOops! You seem to have pressed \"Cancel\" along the way...\n");
      } catch(ArrayIndexOutOfBoundsException aioobe) {
         System.out.println("\nOnly alphanumeric characters are allowed!\n");
      }

      System.exit(0);
   }
}