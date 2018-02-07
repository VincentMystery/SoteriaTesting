package security;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.auth.message.AuthException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.AutoApplySession;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;
import javax.security.enterprise.authentication.mechanism.http.RememberMe;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.identitystore.IdentityStore;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sukma Wardana
 * @since 1.0
 */
@AutoApplySession // For "Is user already logged-in?"
@RememberMe(
        cookieMaxAgeSeconds = 60 * 60 * 24 * 14, // 14 days
        cookieSecureOnly = false, // Remove this when login is served over HTTPS.
        isRememberMeExpression = "#{self.isRememberMe(httpMessageContext)}"
)
@LoginToContinue(
        loginPage = "/login.xhtml", // specify your login url
        errorPage = "",
        useForwardToLogin = false
)
@ApplicationScoped
public class SoteriaFormAuthenticationMechanism implements HttpAuthenticationMechanism {

    @Inject
    IdentityStore identityStore;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest req, HttpServletResponse res, HttpMessageContext context) {

        Credential credential = context.getAuthParameters().getCredential();

        if (credential != null) {
            return context.notifyContainerAboutLogin(this.identityStore.validate(credential));
        } else {
            return context.doNothing();
        }
    }

    // this was called on @RememberMe annotations
    public Boolean isRememberMe(HttpMessageContext httpMessageContext) {
        return httpMessageContext.getAuthParameters().isRememberMe();
    }

    // Workaround for Weld bug; at least in Weld 2.3.2 default methods are not intercepted
    @Override
    public void cleanSubject(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) {
        HttpAuthenticationMechanism.super.cleanSubject(request, response, httpMessageContext);
    }
}