package security.presentation;

import security.boundary.AccountStore;

import static org.omnifaces.util.Messages.addGlobalInfo;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

/**
 *
 * @author Sukma Wardana
 * @since 1.0
 */
@Model
public class Register {

    private String username;
    private String password;
    private String email;

    @Inject
    AccountStore accountStore;

    public void submit() {
        //TODO: validate and sanitize username and password
        this.accountStore.registerAccount(username, email, password);
        addGlobalInfo("register.message.success");
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}