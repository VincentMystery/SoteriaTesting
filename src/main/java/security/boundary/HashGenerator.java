package security.boundary;

import javax.ejb.Stateless;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 *
 * @author Sukma Wardana
 * @since 1.0
 */
@Stateless
public class HashGenerator {

    public String algorithmName;

    public HashGenerator() {
        // EJB need this one.
    }

    public HashGenerator(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public String getHashText(String text) {

        try {
            MessageDigest messageDigest = MessageDigest.getInstance(this.algorithmName);
            byte[] hash = messageDigest.digest(text.getBytes(StandardCharsets.UTF_8));
            String encoded = Base64.getEncoder().encodeToString(hash);

            return encoded;
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
}
