package security.presentation;

import static javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters.withParams;
import static javax.security.enterprise.AuthenticationStatus.SUCCESS;
import static javax.security.enterprise.AuthenticationStatus.SEND_FAILURE;
import static org.omnifaces.util.Faces.getRequest;
import static org.omnifaces.util.Faces.getResponse;
import static org.omnifaces.util.Faces.redirect;
import static org.omnifaces.util.Faces.validationFailed;
import static org.omnifaces.util.Messages.addGlobalError;

import java.io.IOException;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;

import org.omnifaces.cdi.Param;

/**
 *
 * @author Sukma Wardana
 * @since 1.0
 */
@Model
public class Login {

    private String username;
    private String password;
    private boolean remember;

    @Inject
    @Param(name = "continue") // Defined in @LoginToContinue of SecurityFormAuthenticationMechanism
    private boolean loginToContinue;

    @Inject
    SecurityContext securityContext;

    public void submit() throws IOException {
        //TODO: validate and sanitize username and password
        // credential that want to be validate was UsernamePasswordCredential
        Credential credential = new UsernamePasswordCredential(username, new Password(password));

        // this will call our security configuration to authorize the user
        AuthenticationStatus status = this.securityContext.authenticate(
                getRequest(),
                getResponse(),
                withParams()
                    .credential(credential)
                    .newAuthentication(!loginToContinue)
                    .rememberMe(remember)
        );

        if (status.equals(SUCCESS)) {

            redirect("index.html");

        } else if (status.equals(SEND_FAILURE)) {

            addGlobalError("auth.message.error.failure");
            validationFailed();

        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }
}