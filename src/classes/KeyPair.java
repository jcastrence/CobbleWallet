package classes;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import classes.HexNum;
import crypto.*;
import utils.Hash;

public class KeyPair {

    /* --- Fields --- */

    public String privateKey;
    public String publicKey;
    private final EllipticCurve EC = new EllipticCurve();


    /* --- Constructors --- */

    // Default constructor, cryptographically secure
    // FOR REAL WORLD BITCOIN ADDRESSES ONLY USE THIS CONSTRUCTOR
    public KeyPair() throws NoSuchAlgorithmException {
        this.privateKey = generatePrivateKey();
        this.publicKey = EC.computePublicKey(this.privateKey);
    }

    // DO NOT USE THIS CONSTRUCTOR FOR REAL WORLD BITCOIN ADDRESSES, FOR TESTING ONLY, HIGHLY INSECURE
    public KeyPair(String seed) throws NoSuchAlgorithmException {
        this.privateKey = generatePrivateKey(seed);
        this.publicKey = EC.computePublicKey(this.privateKey);
    }

    /* --- Key Generation --- */

    // Generates a cryptographically random private key
    private String generatePrivateKey() throws NoSuchAlgorithmException {
        byte[] seed;
        HexNum privateKeyHex;
        do {
            // Generate a cryptographically secure random 256 bit number
            seed = new SecureRandom().generateSeed(256);
            // Perform a double hash to obtain the private key
            privateKeyHex = new HexNum(Hash.doubleSHA(seed));
        }
        // Check privateKeyHex is within allowed field; if not generate a new private key
        while (!(privateKeyHex.compareTo(EllipticCurve.ONE) > 0 || privateKeyHex.compareTo(EC.field) < 0));
        return privateKeyHex.hexString();
    }

    /* HIGHLY INSECURE METHOD OF OBTAINING A PRIVATE KEY USED FOR TESTING PURPOSES ONLY */
    private String generatePrivateKey(String seed) throws NoSuchAlgorithmException {
        return new HexNum(Hash.doubleSHA(seed)).hexString();
    }
}
