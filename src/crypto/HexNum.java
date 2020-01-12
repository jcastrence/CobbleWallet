package crypto;

import java.math.BigInteger;

/*
    Pseudo-immutable class used to represent 256-bit hexadecimal numbers on elliptic curves
    HexNum values can be represented in three ways (Objects / types):
    1. hexString - String representation of a hexadecimal number for creating public / private keys and human readability
    2. bytes - Byte array for use with cryptographic hash algorithms
    3. scalar - BigInteger to allow for easy mathematical operation between HexNum objects
*/
public final class HexNum implements Comparable<HexNum> {

    /* --- Fields --- */

    // All three instance fields should represent the same number at all times
    // Therefore, HexNum instance fields are immutable after instantiation
    private final String hexString;
    private final byte[] bytes;
    private final BigInteger scalar;

    // All mathematical operations between HexNum objects are modular
    // By default, HexNum objects are (mod 2^256 - 2^32 - 2^9 - 2^8 - 2^7 - 2^6 - 2^4 - 1) for use with secp256k1
    public static String SECP256K1_p = "115792089237316195423570985008687907853269984665640564039457584007908834671663";
    public static BigInteger modulus = new BigInteger(SECP256K1_p);

    // Constants
    public static final BigInteger BIG_ZERO = new BigInteger("0");
    public static final BigInteger BIG_ONE = new BigInteger("1");
    public static final BigInteger BIG_TWO = new BigInteger("2");
    public static final BigInteger BIG_THREE = new BigInteger("3");
    public static final BigInteger BIG_FOUR = new BigInteger("4");

    public static final HexNum HEX_ZERO = new HexNum("00");
    public static final HexNum HEX_ONE = new HexNum("01");
    public static final HexNum HEX_TWO = new HexNum("02");
    public static final HexNum HEX_THREE = new HexNum("03");
    public static final HexNum HEX_FOUR = new HexNum("04");

    /* --- Constructors --- */

    // Default: Use these 3 for keys and ecc points
    public HexNum(String hexString) {
        this.hexString = processString(hexString);
        this.bytes = hexToBytes(this.hexString);
        this.scalar = hexToScalar(this.hexString);
    }

    public HexNum(byte[] bytes) {
        this.bytes = bytes;
        this.hexString = bytesToHex(this.bytes);
        this.scalar = bytesToScalar(this.bytes);
    }

    public HexNum(BigInteger scalar) {
        this.scalar = new BigInteger(scalar.toString());
        this.hexString = scalarToHex(this.scalar);
        this.bytes = scalarToBytes(this.scalar);
    }


    /* --- Getters / Setters --- */

    public String hexString() {
        return hexString;
    }

    public BigInteger scalar() {
        return scalar;
    }

    public byte[] bytes() {
        return bytes;
    }

    // Changes the modulus of the HexNum object
    // IMPORTANT: Changing the modulus mid-calculation will render the entire calculation invalid
    // Unless working with a different elliptic curve, the modulus should never be changed for secp256k1 calculations
    public static void setModulus(BigInteger m) {
        modulus = m;
    }

    // Reset modulus back to secp256k1 standard
    public static void resetModulus() {
        modulus = new BigInteger(SECP256K1_p);
    }

    /* --- Checkers --- */

    // Check string is in correct hexadecimal format and add leading zero if necessary
    private static String processString(String string) {
        // Check string characters are within valid charset
        for (int i = 0; i < string.length(); i++) {
            if (!Character.isLetterOrDigit(string.charAt(i))) {
                throw new IllegalArgumentException(
                    "Illegal String: " + string + "\n" +
                    "String must represent a hexadecimal value; Invalid char @ index "
                    + Integer.toString(i) + ": " + string.charAt(i)
                );
            }
        }
        // Add leading zero if string is of odd length
        string = string.length() % 2 != 0 ? "0" + string : string;
        // Convert to uppercase
        return string.toUpperCase();
    }

    /* --- String Functions --- */

    public HexNum substring(int begin, int end) {
        return new HexNum(this.hexString.substring(begin, end));
    }

    public HexNum concat(HexNum other) {
        return new HexNum(this.hexString + other.hexString());
    }

    public int length() {
        return this.hexString.length();
    }

    /* --- Math Functions --- */

    // All mathematical operations on HexNum objects are modular

    // Mod
    public HexNum mod() {
        return new HexNum(this.scalar.mod(modulus));
    }

    // Modular Addition
    public HexNum add(HexNum other) {
        return new HexNum(this.scalar.add(other.scalar).mod(modulus));
    }

    // Modular Multiplication
    public HexNum mul(HexNum factor) {
        return new HexNum(this.scalar.multiply(factor.scalar).mod(modulus));
    }

    // Modular Exponentiation
    public HexNum pow(HexNum exp) {
        return new HexNum(this.scalar.modPow(exp.scalar, modulus));
    }

    // Modular Negation
    public HexNum neg() {
        return new HexNum(this.scalar.negate().mod(modulus));
    }

    // Modular Inverse
    public HexNum inv() {
        return new HexNum(this.scalar.modInverse(modulus));
    }

    // Modular Subtraction
    public HexNum sub(HexNum other) {
        return new HexNum(this.scalar.subtract(other.scalar).mod(modulus));
    }

    // Modular Division
    public HexNum div(HexNum divisor) {
        return this.mul(divisor.inv());
    }

    // Modular Square Root
    // If p ≡ 3 (% 4):
    // √a (% p) ≡ a ^ ((p + 1) / 4) (% p)
    public HexNum[] sqrt() {
        if (!modulus.mod(BIG_FOUR).equals(BIG_THREE)) {
            throw new ArithmeticException("Square roots not supported for modulus: " + modulus.toString());
        }
        HexNum root1 =
                new HexNum(this.scalar.modPow(modulus.add(BIG_ONE).divide(BIG_FOUR), modulus));
        HexNum root2 = root1.neg();
        return root1.compareTo(root2) < 0 ? new HexNum[] {root1, root2} : new HexNum[] {root2, root1};
    }

    // Check for even / odd
    public static boolean isEven(HexNum num) {
        return num.scalar.mod(BIG_TWO).equals(BIG_ZERO);
    }


    /* --- Conversion Functions --- */

    /* Hex -> _ */

    // Convert a hexadecimal string to a byte array
    public static byte[] hexToBytes(String hexString) {
        byte[] bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2) {
            bytes[i / 2] = (byte) Integer.parseInt(hexString.substring(i, i + 2), 16);
        }
        return bytes;
    }

    // Convert a hexadecimal string to a big integer
    public static BigInteger hexToScalar(String hexString) {
        return new BigInteger(hexString, 16);
    }

    /* Byte Array -> _ */

    // Convert a byte array to a hex string
    public static String bytesToHex(byte[] bytes) {
        return processString(new BigInteger(1, bytes).toString(16).toUpperCase());
    }

    // Convert a byte array to a big integer
    public static BigInteger bytesToScalar(byte[] bytes) {
        return new BigInteger(1, bytes);
    }

    /* Big Integer -> _ */

    // Convert a big integer to a hex string
    public static String scalarToHex(BigInteger scalar) {
        return processString(scalar.toString(16).toUpperCase());
    }

    // Convert a big integer to a byte array
    public static byte[] scalarToBytes(BigInteger scalar) {
        return hexToBytes((scalarToHex(scalar)));
    }


    /* --- Comparable Interface --- */

    @Override
    public int compareTo(HexNum other) {
        return this.scalar.compareTo(other.scalar);
    }


    /* --- Object Methods --- */

    @Override
    public boolean equals(Object other) {
        if (other instanceof HexNum) {
            return this.scalar.equals(((HexNum) other).scalar);
        }
        return false;
    }

    @Override
    public String toString() {
        return hexString;
    }

}