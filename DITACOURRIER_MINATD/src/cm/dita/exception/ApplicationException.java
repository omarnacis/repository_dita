/**
 * 
 */
package cm.dita.exception;

/**
 * @author bertrand
 *
 */
public class ApplicationException  extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// code d'erreur
	 private int code;

	 public ApplicationException(int code) {
	 super();
	 this.code = code;
	 }

	 public ApplicationException(String message, int code) {
	 super(message);
	 this.code = code;
	 }

	 public ApplicationException(Throwable cause, int code) {
	 super(cause);
	 this.code = code;
	 }

	 public ApplicationException(String message, Throwable cause, int code) {
	 super(message, cause);
	this.code = code;
	}

	 // getter et setter
	 public int getCode() {
	 return code;
	 }

	 public void setCode(int code) {
	 this.code = code;
	 }

}
