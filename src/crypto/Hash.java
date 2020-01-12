package crypto;

import crypto.Ripemd160;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Hash {

    private Hash() {};

    /* Cryptographic Hashing */

    // Apply two rounds of SHA256 to a string using UTF-8 standards
    public static byte[] doubleSHA(String input) throws NoSuchAlgorithmException {
        // Convert string to byte array
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        return sha256.digest(sha256.digest(bytes));
    }

    // Apply two rounds of SHA256 to a byte array
    public static byte[] doubleSHA(byte[] input) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        return sha256.digest(sha256.digest(input));
    }

    // Apply SHA256 then RIPEMD160 to a byte array
    public static byte[] RIPEMDSHA(byte[] input) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        return Ripemd160.getHash(sha256.digest(input));
    }

}
