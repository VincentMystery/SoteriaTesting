package security;

import helper.InvalidPasswordException;
import helper.InvalidUsernameException;
import security.boundary.AccountStore;
import security.entity.Account;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.CallerOnlyCredential;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import static javax.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;
import static javax.security.enterprise.identitystore.CredentialValidationResult.NOT_VALIDATED_RESULT;
import javax.security.enterprise.identitystore.IdentityStore;

/**
 *
 * @author Sukma Wardana
 * @since 1.0
 */
@ApplicationScoped
public class SoteriaIdentityStore implements IdentityStore {

    // call our EJB service to validate the account
    @Inject
    AccountStore accountStore;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        try {

            // check if the credential was UsernamePasswordCredential
            if (credential instanceof UsernamePasswordCredential) {
                String username = ((UsernamePasswordCredential) credential).getCaller();
                String password = ((UsernamePasswordCredential) credential).getPasswordAsString();

                return validate(this.accountStore.getByUsernameAndPassword(username, password));
            }

            // check if the credential was CallerOnlyCredential
            if (credential instanceof CallerOnlyCredential) {
                String username = ((CallerOnlyCredential) credential).getCaller();

                return validate(
                        this.accountStore.getByUsername(username)
                                .orElseThrow(InvalidUsernameException::new)
                );
            }

        } catch (InvalidUsernameException e) {
            return INVALID_RESULT;
        }
        return NOT_VALIDATED_RESULT;
    }

    // before return the CredentialValidationResult, check if the account is active or not
    private CredentialValidationResult validate(Account account) {
        if (!account.isActive()) {
            throw new InvalidPasswordException();
        }

        return new CredentialValidationResult(account.getUsername());
    }
}