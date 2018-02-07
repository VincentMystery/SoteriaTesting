package security;

import security.boundary.AccountStore;
import security.boundary.TokenStore;
import security.entity.Account;

import static javax.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;
import static org.omnifaces.util.Servlets.getRemoteAddr;
import static security.entity.TokenType.REMEMBER_ME;

import java.util.Optional;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.CallerPrincipal;
import javax.security.enterprise.credential.RememberMeCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.RememberMeIdentityStore;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Sukma Wardana
 * @since 1.0
 */
@ApplicationScoped
public class SoteriaRememberMeIdentityStore implements RememberMeIdentityStore {

    @Inject
    HttpServletRequest request;

    @Inject
    AccountStore accountStore;

    @Inject
    TokenStore tokenStore;

    @Override
    public CredentialValidationResult validate(RememberMeCredential rmc) {
        Optional<Account> account = this.accountStore.getByLoginToken(rmc.getToken(), REMEMBER_ME);

        if (account.isPresent()) {
            return new CredentialValidationResult(new CallerPrincipal(account.get().getUsername()));
        } else {
            return INVALID_RESULT;
        }
    }

    @Override
    public String generateLoginToken(CallerPrincipal cp, Set<String> set) {
        //getRemoteAddr() was method that come with Omnifaces dependency
        return this.tokenStore.generate(cp.getName(), getRemoteAddr(request), getDescription(), REMEMBER_ME);
    }

    @Override
    public void removeLoginToken(String loginToken) {
        this.tokenStore.remove(loginToken);
    }

    private String getDescription() {
        return "Remember me session: " + this.request.getHeader("User-Agent");
    }
}