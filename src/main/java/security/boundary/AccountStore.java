package security.boundary;

import helper.InvalidPasswordException;
import helper.InvalidUsernameException;
import security.entity.Account;
import security.entity.TokenType;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Optional;

/**
 *
 * @author Sukma Wardana
 * @since 1.0
 */
@Stateless
public class AccountStore {

    @PersistenceContext
    EntityManager em;

    @Inject
    @HashAlgorithm(algorithm = Algorithm.SHA256)
    HashGenerator tokenHash;

    @Inject
    @HashAlgorithm
    HashGenerator passwordHash;

    @Inject
    TokenStore tokenStore;

    public void registerAccount(final String username, final String email, final String password) {
        String securedPassword = this.passwordHash.getHashText(password);

        Account account = new Account(username, securedPassword, email);

        // Account should not activated by default.
        account.setActive(true);

        this.em.persist(account);
    }

    public Optional<Account> getByUsername(final String username) {
        try {
            return Optional.of(
                    this.em.createNamedQuery(Account.FIND_BY_USERNAME, Account.class)
                            .setParameter("username", username).getSingleResult()
            );
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public Optional<Account> getByEmail(final String email) {
        try {
            return Optional.of(
                    this.em.createNamedQuery(Account.FIND_BY_EMAIL, Account.class)
                            .setParameter("email", email).getSingleResult()
            );
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public Optional<Account> getByLoginToken(String loginToken, TokenType tokenType) {
        try {
            return Optional.of(
                    this.em.createNamedQuery(Account.FIND_BY_TOKEN, Account.class)
                            .setParameter("tokenHash", this.tokenHash.getHashText(loginToken))
                            .setParameter("tokenType", tokenType)
                            .getSingleResult()
            );
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    public Account getByUsernameAndPassword(String username, String password) {
        Account managedAccount = getByUsername(username).orElseThrow(InvalidUsernameException::new);

        String hashesPassword = this.passwordHash.getHashText(password);

        if (!hashesPassword.equals(managedAccount.getPassword())) {
            throw new InvalidPasswordException();
        }

        return managedAccount;
    }
}