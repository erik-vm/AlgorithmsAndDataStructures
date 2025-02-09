package sub1;

//A brick (cuboid) has dimensions a, b, c and a hole (rectangle) has dimensions x, y (all positive real numbers).
//Write a Java method "fits" to determine whether the brick passes the hole (true - passes, false - does not pass in any direction,
//                                                                            oblique positions are not allowed).

public class DoesBrickFit {

    public static boolean fits(double a, double b, double c,
                                double x, double y) {
        return (a <= x && b <= y) || (b <= x && a <= y) ||
                (a <= x && c <= y) || (c <= x && a <= y) ||
                (b <= x && c <= y) || (c <= x && b <= y);
    }

}
