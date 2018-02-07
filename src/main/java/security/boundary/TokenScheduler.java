package security.boundary;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Sukma Wardana
 * @since 1.0
 */
@Singleton
public class TokenScheduler {

    @PersistenceContext
    EntityManager em;

    @Inject
    TokenStore tokenStore;

    @Schedule(dayOfWeek = "*", hour = "*", minute = "0", second = "0", persistent = false)
    public void hourly() {
        this.tokenStore.removeExpired();
    }
}