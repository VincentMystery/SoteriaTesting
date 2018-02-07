package security.entity;


import static java.time.temporal.ChronoUnit.MONTHS;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

/**
 *
 * @author Sukma Wardana
 * @since 1.0.0
 */
@Entity
@Table(name = "token", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"token_hash"})
})
@NamedQueries({
        @NamedQuery(name = Token.REMOVE_TOKEN, query = "delete from Token t where t.tokenHash = :tokenHash"),
        @NamedQuery(name = Token.REMOVE_EXPIRED_TOKEN, query = "delete from Token t where t.expiration < CURRENT_TIMESTAMP")
})
public class Token implements Serializable {

    public static final String REMOVE_TOKEN = "Token.removeToken";
    public static final String REMOVE_EXPIRED_TOKEN = "Token.removeExpiredToken";

    @Id
    @GeneratedValue(generator = "token_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "token_id_seq", sequenceName = "token_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "token_hash")
    private String tokenHash;

    @Column(name = "token_type")
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @Column(name = "ip_address")
    @Size(min = 1, max = 45)
    private String ipAddress;

    private String description;

    private Instant created;

    private Instant expiration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    public Token() {
    }

    @PrePersist
    public void generateInformation() {
        this.created = Instant.now();
        if (this.expiration == null) {
            this.expiration = this.created.plus(1, MONTHS);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public void setTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getExpiration() {
        return expiration;
    }

    public void setExpiration(Instant expiration) {
        this.expiration = expiration;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    /* don't depend on natural identifier for equality checks, see: https://vladmihalcea.com/2017/03/29/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/#more-7143 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Token token = (Token) obj;
        return Objects.equals(id, token.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "Token{ id " + id + '}';
    }
}
