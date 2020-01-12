package classes;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import crypto.*;
import crypto.Hash;

public class Key {

    /* --- Fields --- */

    private final EllipticCurve EC = new EllipticCurve();
    private boolean compressed;
    private HexNum privateKey;
    private HexNum publicKey;
    private String privateKeyWIFUncompressed;
    private String privateKeyWIFCompressed;
    private String publicAddressUncompressed;
    private String publicAddressCompressed;


    /* --- Constructors --- */

    // FOR REAL WORLD BITCOIN ADDRESSES USE THESE CONSTRUCTORS

    // Default constructor, generate a new, cryptographically secure, random Bitcoin key
    public Key() throws NoSuchAlgorithmException {
        generatePrivateKey();
        generateKey();
    }

    // Generate a Key from an existing raw Bitcoin private key
    // TODO Create a compressed and uncompressed key pair
    public Key(HexNum privateKey) throws NoSuchAlgorithmException {
        generatePrivateKey(privateKey);
        generateKey();
    }

    // Generate a Key from an existing Bitcoin WIF private key
    public Key(String privateKeyWIF) throws NoSuchAlgorithmException {
        generatePrivateKey(privateKeyWIF);
        generateKey();
    }

    /* --- Constructor Helpers --- */

    /* --- Key Generation --- */

    // Generates a cryptographically secure random private key
    private void generatePrivateKey() throws NoSuchAlgorithmException {
        byte[] seed;
        HexNum privKey;
        do {
            // Generate a cryptographically secure random 256 bit number
            seed = new SecureRandom().generateSeed(256);
            // Perform a double hash to obtain the private key
            privKey = new HexNum(Hash.doubleSHA(seed));
        }
        // Check privateKey is within allowed field; if not generate a new private key
        while (!(privKey.compareTo(HexNum.HEX_ONE) > 0 && privKey.compareTo(EC.field) < 0));
        this.compressed = false;
        this.privateKey = privKey;
    }

    // Use an existing private key in raw hexadecimal format
    private void generatePrivateKey(HexNum privKey) {
        // Check privateKey is appropriate length
        if (privKey.length() != 64) {
            throw new IllegalArgumentException("Invalid private key: Must be 64 characters");
        }
        // Check privateKey is within allowed field
        if (!(privKey.compareTo(HexNum.HEX_ONE) > 0 && privKey.compareTo(EC.field) < 0)) {
            throw new IllegalArgumentException("Invalid private key: Not within field");
        }
        this.privateKey = privKey;
    }

    // Use an existing private key in WIF format
    private void generatePrivateKey(String privKeyWIF) throws NoSuchAlgorithmException {
        this.privateKey = decodePrivKey(privKeyWIF);
    }

    // Obtain public key, WIF formats, and public addresses
    private void generateKey() throws NoSuchAlgorithmException {
        this.publicKey = EC.computePublicKey(this.privateKey);
        this.privateKeyWIFUncompressed = encodePrivU(this.privateKey);
        this.privateKeyWIFCompressed = encodePrivC(this.privateKey);
        this.publicAddressUncompressed = encodePubU(this.publicKey);
        this.publicAddressCompressed = encodePubC(this.publicKey);
    }

    /* --- Decoding --- */

    private HexNum decodePrivKey(String privWIF) throws NoSuchAlgorithmException {
        // Decode from base-58
        HexNum privKey = base58toHex(privWIF);
        // Verify checksum and separate
        HexNum checksum = privKey.substring(privKey.length() - 8, privKey.length());
        privKey = privKey.substring(0, privKey.length() - 8);
        if (!computeChecksum(privKey).equals(checksum)) {
            throw new IllegalArgumentException("Invalid private key: Checksum failed");
        }
        // Verify version number and remove
        if (!privKey.hexString().substring(0, 2).equals("80")) {
            throw new IllegalArgumentException("Invalid private key: Invalid version number");
        }
        privKey = privKey.substring(2, privKey.length());
        // Check for compression and remove
        if (privKey.length() == 66 && privKey.hexString().substring(64, 66).equals("01")) {
            this.compressed = true;
            privKey = privKey.substring(0, 64);
        }
        else {
            this.compressed = false;
        }
        // Check key is in valid key field
        if (!(privKey.compareTo(HexNum.HEX_ONE) > 0 && privKey.compareTo(EC.field) < 0)) {
            throw new IllegalArgumentException("Invalid private key: Not within field");
        }
        // Valid private key
        return privKey;
    }

    /* --- Encoding --- */

    private String encodePrivU(HexNum privKey) throws NoSuchAlgorithmException {
        // Prefix version number for Bitcoin
        HexNum privKeyWIF = new HexNum("80" + privKey.hexString());
        // Compute checksum and append
        privKeyWIF = privKeyWIF.concat(computeChecksum(privKeyWIF));
        // Encode using base-58
        return hexToBase58(privKeyWIF);
    }

    private String encodePrivC(HexNum privKey) throws NoSuchAlgorithmException {
        // Prefix version number for Bitcoin and suffix version number to indicate compression
        HexNum privKeyWIF = new HexNum("80" + privKey.hexString() + "01");
        // Compute checksum and append
        privKeyWIF = privKeyWIF.concat(computeChecksum(privKeyWIF));
        // Encode using base-58
        return hexToBase58(privKeyWIF);
    }
    //    TODO
    private String encodePubU(HexNum pubKey) throws NoSuchAlgorithmException {
        // Prefix version number to indicate uncompression
        HexNum pubAddress = new HexNum("04" + pubKey.hexString());
        // Perform hashing; RIPEMD160(SHA256(x))
        pubAddress = publicHashing(pubAddress);
        // Prefix version number for Bitcoin
        pubAddress = new HexNum("00" + pubAddress);
        // Compute checksum and append
        pubAddress = pubAddress.concat(computeChecksum(pubAddress));
        // Encode using base-58
        return hexToBase58(pubAddress);
    }
    //    TODO
    private String encodePubC(HexNum pubKey) throws NoSuchAlgorithmException {
        // Isolate coordinates
        HexNum x = pubKey.substring(0, 64);
        HexNum y = pubKey.substring(64, 128);
        // Prefix version number to indicate compression
        // If y is: Even -> 02, Odd -> 03
        String prefix = HexNum.isEven(y) ? "02" : "03";
        HexNum pubAddress = new HexNum(prefix + x.hexString());
        // Perform hashing; RIPEMD160(SHA256(x))
        pubAddress = publicHashing(pubAddress);
        // Prefix version number for Bitcoin
        pubAddress = new HexNum("00" + pubAddress);
        // Compute checksum and append
        pubAddress = pubAddress.concat(computeChecksum(pubAddress));
        // Encode using base-58
        return hexToBase58(pubAddress);
    }

    private HexNum computeChecksum(HexNum hex) throws NoSuchAlgorithmException {
        return new HexNum(HexNum.bytesToHex(Hash.doubleSHA(hex.bytes())).substring(0, 8));
    }

    private HexNum publicHashing(HexNum hex) throws NoSuchAlgorithmException {
        return new HexNum(HexNum.bytesToHex(Hash.RIPEMDSHA(hex.bytes())));
    }

    private String hexToBase58(HexNum hex) {
        return Base58Check.rawBytesToBase58(hex.bytes());
    }

    private HexNum base58toHex(String base58String) {
        return new HexNum(Base58Check.base58ToRawBytes(base58String));
    }


    /* --- Getters / Setters --- */

    public HexNum getPrivateKey() {
        return this.privateKey;
    }

    public HexNum getPublicKey() {
        return this.publicKey;
    }

    public String getPrivateKeyWIFCompressed() {
        return this.privateKeyWIFCompressed;
    }

    public String getPrivateKeyWIFUncompressed() {
        return this.privateKeyWIFUncompressed;
    }

    public String getPublicAddressCompressed() {
        return this.publicAddressCompressed;
    }

    public String getPublicAddressUncompressed() {
        return this.publicAddressUncompressed;
    }

    public boolean isCompressed() {
        return this.compressed;
    }


    /* --- Object Methods --- */

    @Override
    public boolean equals(Object other) {
        if (other instanceof Key) {
            return this.privateKey.equals(((Key) other).privateKey);
        }
        return false;
    }

    public String toString() {
        String str = "Private Key: " + this.privateKey + "\n"
                + "Public Key: " + this.publicKey + "\n"
                + "Private Key WIF Uncompressed: " + this.privateKeyWIFUncompressed + "\n"
                + "Private Key WIF Compressed: " + this.privateKeyWIFCompressed + "\n"
                + "Public Address Uncompressed: " + this.publicAddressUncompressed + "\n"
                + "Public Address Compressed: " + this.publicAddressCompressed + "\n";
        return str;

    }

}
