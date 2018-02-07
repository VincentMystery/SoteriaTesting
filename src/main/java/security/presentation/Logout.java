package security.presentation;

import java.io.IOException;

import static org.omnifaces.util.Faces.invalidateSession;
import static org.omnifaces.util.Faces.redirect;

import javax.enterprise.inject.Model;
import javax.servlet.ServletException;
import org.omnifaces.util.Faces;

/**
 *
 * @author Sukma Wardana
 * @version 1.0.0
 */
@Model
public class Logout {

    public void submit() throws ServletException, IOException {
        Faces.logout();
        invalidateSession();
        redirect("login.xhtml");
    }
}