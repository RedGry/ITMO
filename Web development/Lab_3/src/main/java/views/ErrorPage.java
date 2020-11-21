package views;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import java.io.IOException;

public class ErrorPage {
    public static void goToErrorPage(String errorMessage) throws IOException {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.put("errorMessage", errorMessage);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/error.jsf");
    }
}