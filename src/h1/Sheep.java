package h1;

public class Sheep {

    public enum Animal {sheep, goat}

    ;

    public static void main(String[] param) {
        // for debugging
    }

    public static void reorder(Animal[] animals) {
        int start = 0;
        int end = animals.length - 1;

        while (start < end) {

            // increment start if pointer is already on goat
            while (start < end && animals[start] == Animal.goat) {
                start++;
            }
            // decrement end if pointer is already on sheep
            while (start < end && animals[end] == Animal.sheep) {
                end--;
            }
            // swap goat and sheep, if sheep on left
            if (start < end) {
                Animal temp = animals[start];
                animals[start] = animals[end];
                animals[end] = temp;
            }

        }
    }
}

