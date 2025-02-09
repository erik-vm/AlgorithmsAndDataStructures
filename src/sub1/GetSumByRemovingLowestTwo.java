package sub1;

//Sportsmans score is calculated as sum of points from different attempts and two worst attempts are not counted
// (there are more than two attempts).
//Write a Java method to calculate the score if an array of points from all attempts is given.
//Do not change the array given as parameter.

public class GetSumByRemovingLowestTwo {

    public static void main(String[] args) {
        System.out.println(score(new int[]{4, 1, 2, 3, 0})); // 9
        // Your tests here
    }

    public static int score(int[] points) {

        int total = 0;
        int min1 = Integer.MAX_VALUE;
        int min2 = Integer.MAX_VALUE;

        for (int point : points) {
            total += point;
            if (point < min1) {
                min2 = min1;
                min1 = point;
            } else if (point < min2) {
                min2 = point;
            }
        }
        return total - min1 - min2;

    }

}
