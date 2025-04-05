package h4;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Quaternions. Basic operations.
 * <p>
 * Quaternion klass esindab kvaternione,
 * mis on laiendus kompleksarvudele nelja
 * komponendiga (reaalosa ja kolm imaginaarset osa).
 */
public class Quaternion {

    // väga väike väärtus võrdluste jaoks, et vältida ujuvkomaarvude ümardamisvigu
    private static final double EPSILON = 1.0e-6;

    // kvaternioni komponendid, kus a on reaalosa ja b, c, d on imaginaarsed osad
    private final double a, b, c, d;

    /**
     * Constructor from four double values.
     *
     * @param a real part
     * @param b imaginary part i
     * @param c imaginary part j
     * @param d imaginary part k
     */
    public Quaternion(double a, double b, double c, double d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    /**
     * Real part of the quaternion.
     *
     * @return real part
     * <p>
     * // tagastab reaalosa a
     */
    public double getRpart() {
        return a;
    }

    /**
     * Imaginary part i of the quaternion.
     *
     * @return imaginary part i
     * <p>
     * tagastab imiginaarse osa b
     */
    public double getIpart() {
        return b;
    }

    /**
     * Imaginary part j of the quaternion.
     *
     * @return imaginary part j
     * <p>
     * tagastab imiginaarse osa c
     */
    public double getJpart() {
        return c;
    }

    /**
     * Imaginary part k of the quaternion.
     *
     * @return imaginary part k
     * <p>
     * tagastab imiginaarse osa d
     */
    public double getKpart() {
        return d;
    }

    /**
     * Conversion of the quaternion to the string.
     *
     * @return a string form of this quaternion:
     * "a+bi+cj+dk"
     * (without any brackets)
     * <p>
     * teisendab kvaternioni stringiks kujul "a+bi+cj+dk"
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(a);
        if (b >= 0) {
            sb.append("+");
        }
        sb.append(b).append("i");
        if (c >= 0) {
            sb.append("+");
        }
        sb.append(c).append("j");
        if (d >= 0) {
            sb.append("+");
        }
        sb.append(d).append("k");
        return sb.toString();
    }

    /**
     * Conversion from the string to the quaternion.
     * Reverse to <code>toString</code> method.
     *
     * @param s string of form produced by the <code>toString</code> method
     * @return a quaternion represented by string s
     * @throws IllegalArgumentException if string s does not represent
     *                                  a quaternion (defined by the <code>toString</code> method)
     *                                  <p>
     *                                  <p>
     *                                  teisendab stringi tagasi kvaternioniks, kasutades regulaaravaldisi
     */
    public static Quaternion valueOf(String s) {
        s = s.replaceAll("\\s", ""); // eemaldab tühikud
        Pattern pattern = Pattern.compile("([-+]?[\\d\\.eE]+)([-+]?[\\d\\.eE]+)i([-+]?[\\d\\.eE]+)j([-+]?[\\d\\.eE]+)k");
        Matcher matcher = pattern.matcher(s);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid quaternion format: " + s);
        }

        return new Quaternion(
                Double.parseDouble(matcher.group(1)),
                Double.parseDouble(matcher.group(2)),
                Double.parseDouble(matcher.group(3)),
                Double.parseDouble(matcher.group(4))
        );
    }

    /**
     * Clone of the quaternion.
     *
     * @return independent clone of <code>this</code>
     * <p>
     * teeb koopia kvaternionist
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return new Quaternion(a, b, c, d);
    }

    /**
     * Test whether the quaternion is zero.
     *
     * @return true, if the real part and all the imaginary parts are (close to) zero
     * <p>
     * <p>
     * kontrollib, kas väärtused on lähedal nullile
     */
    public boolean isZero() {
        return Math.abs(a) < EPSILON &&
                Math.abs(b) < EPSILON &&
                Math.abs(c) < EPSILON &&
                Math.abs(d) < EPSILON;
    }

    /**
     * Conjugate of the quaternion. Expressed by the formula
     * conjugate(a+bi+cj+dk) = a-bi-cj-dk
     *
     * @return conjugate of <code>this</code>
     * <p>
     * muudab imiginaarste osade märgid vastupidiseks
     */
    public Quaternion conjugate() {
        return new Quaternion(a, -b, -c, -d);
    }

    /**
     * Opposite of the quaternion. Expressed by the formula
     * opposite(a+bi+cj+dk) = -a-bi-cj-dk
     *
     * @return quaternion <code>-this</code>
     * <p>
     * muudab kõik märgid vastupidiseks
     */
    public Quaternion opposite() {
        return new Quaternion(-a, -b, -c, -d);
    }

    /**
     * Sum of quaternions. Expressed by the formula
     * (a1+b1i+c1j+d1k) + (a2+b2i+c2j+d2k) = (a1+a2) + (b1+b2)i + (c1+c2)j + (d1+d2)k
     *
     * @param q addend
     * @return quaternion <code>this+q</code>
     * <p>
     * liidab kaks kavternioni
     */
    public Quaternion plus(Quaternion q) {
        return new Quaternion(a + q.a, b + q.b, c + q.c, d + q.d);
    }

    /**
     * Product of quaternions. Expressed by the formula
     * (a1+b1i+c1j+d1k) * (a2+b2i+c2j+d2k) = (a1a2-b1b2-c1c2-d1d2) + (a1b2+b1a2+c1d2-d1c2)i +
     * (a1c2-b1d2+c1a2+d1b2)j + (a1d2+b1c2-c1b2+d1a2)k
     *
     * @param q factor
     * @return quaternion <code>this*q</code>
     * <p>
     * korrutab kaks kvaternioni kasutades kvaternioni korrutamisreeglit
     */
    public Quaternion times(Quaternion q) {
        return new Quaternion(a * q.a - b * q.b - c * q.c - d * q.d,
                a * q.b + b * q.a + c * q.d - d * q.c,
                a * q.c - b * q.d + c * q.a + d * q.b,
                a * q.d + b * q.c - c * q.b + d * q.a);
    }

    /**
     * Multiplication by a coefficient.
     *
     * @param r coefficient
     * @return quaternion <code>this*r</code>
     * <p>
     * kvanternioni korrutamine reaalarvuga
     */
    public Quaternion times(double r) {
        return new Quaternion(a * r, b * r, c * r, d * r);
    }

    /**
     * Inverse of the quaternion. Expressed by the formula
     * 1/(a+bi+cj+dk) = a/(a*a+b*b+c*c+d*d) +
     * ((-b)/(a*a+b*b+c*c+d*d))i + ((-c)/(a*a+b*b+c*c+d*d))j + ((-d)/(a*a+b*b+c*c+d*d))k
     *
     * @return quaternion <code>1/this</code>
     * <p>
     * <p>
     * leiab kavanternioni pöördväärtuse, kui kvanternioni norm pole null
     */

    public Quaternion inverse() {
        double squaredNorm = norm() * norm();
        if (squaredNorm < EPSILON) {
            throw new ArithmeticException("Error: cant invert a quaternion with zero norm: " + squaredNorm);
        }
        return conjugate().times(1 / squaredNorm);
    }

    /**
     * Difference of quaternions. Expressed as addition to the opposite.
     *
     * @param q subtrahend
     * @return quaternion <code>this-q</code>
     * <p>
     * lahutab ühe kavaternioni teisest
     */
    public Quaternion minus(Quaternion q) {
        return new Quaternion(a - q.a, b - q.b, c - q.c, d - q.d);
    }

    /**
     * Right quotient of quaternions. Expressed as multiplication to the inverse.
     *
     * @param q (right) divisor
     * @return quaternion <code>this*inverse(q)</code>
     * <p>
     * kvanternioni jagamine paremal
     */
    public Quaternion divideByRight(Quaternion q) {
        if (q.isZero()) {
            throw new ArithmeticException("Error: cant divide by zero quaternion: " + q + " (right division)");
        }
        return this.times(q.inverse());
    }

    /**
     * Left quotient of quaternions.
     *
     * @param q (left) divisor
     * @return quaternion <code>inverse(q)*this</code>
     * <p>
     * kvanternioni jagamine vasakul
     */
    public Quaternion divideByLeft(Quaternion q) {
        if (q.isZero()) {
            throw new ArithmeticException("Error: cant divide by zero quaternion: " + q + " (left division)");
        }
        return q.inverse().times(this);
    }

    /**
     * Equality test of quaternions. Difference of equal numbers
     * is (close to) zero.
     *
     * @param qo second quaternion
     * @return logical value of the expression <code>this.equals(qo)</code>
     * <p>
     * võrdleb kahte kvaternioni, arvestades EPSILIONI väärtust
     */
    @Override
    public boolean equals(Object qo) {
        if (this == qo) {
            return true;
        }
        if (!(qo instanceof Quaternion)) {
            return false;
        }
        Quaternion q = (Quaternion) qo;
        return Math.abs(a - q.a) < EPSILON &&
                Math.abs(b - q.b) < EPSILON &&
                Math.abs(c - q.c) < EPSILON &&
                Math.abs(d - q.d) < EPSILON;
    }

    /**
     * Dot product of quaternions. (p*conjugate(q) + q*conjugate(p))/2
     *
     * @param q factor
     * @return dot product of this and q
     * <p>
     * arvutab kvanternioni skalaarkorrutise
     */
    public Quaternion dotMult(Quaternion q) {
        return this.times(q.conjugate()).plus(q.times(this.conjugate())).times(0.5);
    }

    /**
     * Integer hashCode has to be the same for equal objects.
     *
     * @return hashcode
     * <p>
     * kvanternioni räsikoodi genereerimine
     */
    @Override
    public int hashCode() {
        long bits = Double.doubleToLongBits(a);
        bits = 31 * bits + Double.doubleToLongBits(b);
        bits = 31 * bits + Double.doubleToLongBits(c);
        bits = 31 * bits + Double.doubleToLongBits(d);
        return (int) (bits ^ (bits >>> 32));
    }

    /**
     * Norm of the quaternion. Expressed by the formula
     * norm(a+bi+cj+dk) = Math.sqrt(a*a+b*b+c*c+d*d)
     *
     * @return norm of <code>this</code> (norm is a real number)
     * <p>
     * arvutab kvanternioni normi
     */
    public double norm() {
        return Math.sqrt(a * a + b * b + c * c + d * d);
    }


    public Quaternion pow(int n) throws CloneNotSupportedException {
        if (n == 0) {
            return new Quaternion(1, 0, 0, 0);
        } else if (n == 1) {
            return (Quaternion) clone();
        } else if (n == -1) {
            if (this.isZero()) {
                throw new IllegalArgumentException("Cannot raise zero quaternion to power -1 (inverse is undefined).");
            }
            return inverse();
        } else if (n > 1) {
            return this.times(this.pow(n - 1));
        } else {
            Quaternion positivePow = this.pow(-n);
            if (positivePow.isZero()) {
                throw new IllegalArgumentException("Cannot raise zero quaternion to negative power (inverse is undefined).");
            }
            return positivePow.inverse();
        }
    }


}

