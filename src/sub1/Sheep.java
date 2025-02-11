package sub1;

//An array contains sheep and goats in random order.
//Write a possibly fast method to rearrange the array,
//so that all goats are at the beginning and all sheep
//are at the end of the array.
//Consider the case when all animals are the same kind.
//If any sources are used, they must be cited (in form of comments
//at the beginning of the code).
//Do not use Java List types (lists are very slow for this task).


import java.util.Arrays;

public class Sheep {

    enum Animal {sheep, goat}

    ;

    public static void main(String[] param) {
        Animal[] animals = {Animal.sheep, Animal.goat, Animal.sheep, Animal.goat, Animal.sheep};
        reorder(animals);
        System.out.println(Arrays.toString(animals));
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

