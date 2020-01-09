package classes;

import classes.HexNum;

import java.math.BigInteger;

public class EllipticCurve {

    /* --- Point Class --- */

    public class Point {

        public HexNum x, y;
        public boolean infinity;

        public final BigInteger INT_ZERO = new BigInteger("0");
        public final BigInteger INT_TWO = new BigInteger("2");

        public Point(HexNum x, HexNum y) {
            this.x = x;
            this.y = y;
            this.infinity = false;
        }

        public Point() {
            this.x = null;
            this.y = null;
            this.infinity = true;
        }

        public Point pointDouble() {
            // Check for point at infinity or undefined slope
            if (this.infinity || this.y.equals(ZERO)) {
                return new Point();
            }
            // Get slope
            HexNum slope = THREE.mul(this.x.pow(TWO)).div(TWO.mul(this.y));     // λ = (3 * (x ^ 2)) / (2 * y) (% p)
            // Get x coordinate
            HexNum Rx = slope.pow(TWO).sub(TWO.mul(this.x));                    // Rx = (λ ^ 2) - (2 * x) (% p)
            // Get y coordinate
            HexNum Ry = slope.mul(this.x.sub(Rx)).sub(this.y);                  // Ry = (λ * (x - Rx)) - y (% p)
            // Return the (x, y) coordinate pair as a point
            return new Point(Rx, Ry);
        }

        public Point pointAdd(Point Q) {
            // Check for point at infinity
            if (this.infinity) {
                return Q;
            }
            if (Q.infinity) {
                return this;
            }
            // Check for undefined slope
            if (this.x.equals(Q.x)) {
                return new Point();
            }
            // Check if same point
            if (this.equals(Q)) {
                return this.pointDouble();
            }
             //Get slope
            HexNum slope = this.y.sub(Q.y).div(this.x.sub(Q.x));                // λ = (y1 - y1) / (x1 - x1) (% p)
            // Get x coordinate
            HexNum Rx = slope.pow(TWO).sub(this.x).sub(Q.x);                    // Rx = (λ ^ 2) - x - Gx (% p)
            // Get y coordinate
            HexNum Ry = slope.mul(Q.x.sub(Rx)).sub(Q.y);                        // Ry = (λ * (x - Rx)) - y (% p)
            // Return the (x, y) coordinate pair as a point
            return new Point(Rx, Ry);
        }

        public Point pointMultiply(HexNum k) {
            // Reduce scalar if necessary
            k = k.mod();
            // Convert scalar to a binary string
            String bitString = "";
            BigInteger n = k.scalar();
            while (n.compareTo(INT_ZERO) > 0) {
                bitString = (n.mod(INT_TWO).compareTo(INT_ZERO) == 0 ? "0" : "1") + bitString;
                n = n.divide(INT_TWO);
            }
            // Establish a base point, 1 * R
            Point R = this;
            for (int i = 1; i < bitString.length(); i++) {
                // Double the base point
                R = R.pointDouble();
                // Only perform additions for values of 1
                if (bitString.charAt(i) == '1') {
                    R = pointAdd(R);
                }
            }
            return R;
        }


        /* --- Object Methods --- */

        public boolean equals(Point other) {
            return (this.infinity && other.infinity) || (this.x.equals(other.x) && this.y.equals(other.y));
        }

        @Override
        public String toString() {
            if (this.infinity) {
                return "Infinity";
            }
            return "Point{" + "x=" + x + ", y=" + y + '}';
        }
    }

    /* --- Fields --- */

    public HexNum a, b, p, field;
    public Point G;


    /* --- Constants --- */

    public static final HexNum ZERO = new HexNum("00");
    public static final HexNum ONE = new HexNum("01");
    public static final HexNum TWO = new HexNum("02");
    public static final HexNum THREE = new HexNum("03");

    // secp256k1 Parameters
    public static final String SECP256K1_a = "0";
    public static final String SECP256K1_b = "0";
    public static final String SECP256K1_p = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F";
    public static final String SECP256K1_Gx = "79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798";
    public static final String SECP256K1_Gy = "483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8";
    public static final String SECP256K1_FIELD = "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141";


    /* --- Constructors --- */

    public EllipticCurve(String a, String b, String p, String Gx, String Gy, String field) {
        this.a = new HexNum(a);
        this.b = new HexNum(b);
        this.p = new HexNum(p);
        this.G = new Point(new HexNum(Gx), new HexNum(Gy));
        this.field = new HexNum(SECP256K1_FIELD);
    }

    // Empty constructor creates the secp256k1 curve by default
    public EllipticCurve() {
        this.a = new HexNum(SECP256K1_a);
        this.b = new HexNum(SECP256K1_b);
        this.p = new HexNum(SECP256K1_p);
        this.G = new Point(new HexNum(SECP256K1_Gx), new HexNum(SECP256K1_Gy));
        this.field = new HexNum(SECP256K1_FIELD);
    }

    /* --- Generate Key Pair --- */

    public String computePublicKey(String privateKeyString) {
        HexNum privateKey = new HexNum(privateKeyString);
        Point publicPoint = G.pointMultiply(privateKey);
        return publicPoint.x.hexString() + publicPoint.y.hexString();
    }

    /* --- Testing --- */

//    public void doublePoint(String x, String y) {
//        HexNum.setModulus(this.p.scalar());
//        Point point = new Point(new HexNum(x), new HexNum(y));
//        System.out.println(point.pointDouble());
//    }
//
//    public void addPoints(String x1, String y1, String x2, String y2) {
//        HexNum.setModulus(this.p.scalar());
//        Point one = new Point(new HexNum(x1), new HexNum(y1));
//        Point two = new Point(new HexNum(x2), new HexNum(y2));
//        Point three = one.pointAdd(two);
//        System.out.println(three);
//    }
//
//    public void addInfinity(String x1, String y1) {
//        HexNum.setModulus(this.p.scalar());
//        Point one = new Point(new HexNum(x1), new HexNum(y1));
//        Point two = new Point();
//        Point three = two.pointAdd(one);
//        System.out.println(three);
//    }
//
//    public void multiply(String k, String x, String y) {
//        HexNum.setModulus(this.p.scalar());
//        Point one = new Point(new HexNum(x), new HexNum(y));
//        Point two = one.pointMultiply(new HexNum(k));
//        System.out.println(two);
//    }

}
