package cm.dita.controller.managed.bean.user;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

@Override
public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
  super.onAuthenticationFailure(request, response, exception);
  
 // JOptionPane.showMessageDialog(null, exception.getMessage());
  request.setAttribute("foo", "65655566565");
  
  
 
  
  if(exception.getClass().isAssignableFrom(UsernameNotFoundException.class)) {
	//  JOptionPane.showMessageDialog(null, "titittti");
    //showMessage("BAD_CREDENTIAL");
	  request.setAttribute("foo", "Utilisateur inconnue");
  } else if (exception.getClass().isAssignableFrom(DisabledException.class)) {
	  request.setAttribute("foo", "Utilisateur desactive");
	  //throw new LockedException("ffsfsdfssd");
  }
}

}
