package security.boundary;

/**
 *
 * @author Sukma Wardana
 * @since 1.0
 */
public enum Algorithm {
    SHA256 ("SHA-256"),
    SHA512 ("SHA-512");

    private final String name;

    private Algorithm(String name) {
        this.name = name;
    }

    public String getAlgorithmName() {
        return this.name;
    }

}