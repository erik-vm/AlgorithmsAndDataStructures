package h0;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Lab 1.
 *
 * @since 1.8
 */
public class Answer {

    public static void main(String[] param) throws InterruptedException {

        // TODO!!! Solutions to small problems
        //   that do not need an independent method!

        // conversion double -> String

        double doubleVal = 3.123;

        String strOfDouble = String.valueOf(doubleVal);

        // conversion String -> int

        String stringInt = "123";
        int intStr = 0;
        try {
            intStr = Integer.parseInt(stringInt);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
//
        System.out.println(intStr);

        // "hh:mm:ss"
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime currentTime = LocalDateTime.now();
        String currentTimeFormated = currentTime.format(format);

        System.out.println("Current time: " + currentTimeFormated);

        // cos 45 deg

        double cos = Math.cos(45.0);

        System.out.println("Cosine: " + cos);

        // table of square roots

        for (int i = 0; i <= 100; i += 5) {
            System.out.printf("argument: %d | square root: %.2f%n", i, Math.sqrt(i));
        }


        String firstString = "ABcd12";
        String result = reverseCase(firstString);
        System.out.println("\"" + firstString + "\" -> \"" + result + "\"");

        // reverse string

        String secondString = "1234ab";
        String reversedStr = new StringBuilder(secondString).reverse().toString();
        System.out.println("\"" + secondString + "\" -> \"" + reversedStr + "\"");


        String s = "How  many      words   here";
        int nw = countWords(s);
        System.out.println(s + "\t" + nw);

        // pause. COMMENT IT OUT BEFORE JUNIT-TESTING!

//        long start = System.currentTimeMillis();
//        Thread.sleep(3000);
//        long end = System.currentTimeMillis();
//        System.out.println("Time difference in milliseconds: " + (end - start));

        final int LSIZE = 100;
        ArrayList<Integer> randList = new ArrayList<Integer>(LSIZE);
        Random generaator = new Random();
        for (int i = 0; i < LSIZE; i++) {
            randList.add(generaator.nextInt(1000));
        }

        // minimal element

        int minElement = Collections.min(randList);
        System.out.println(minElement);

        // HashMap tasks:
        //    create
        //    print all keys
        //    remove a key
        //    print all pairs

        HashMap<String, String> subjects = new HashMap<>();
        subjects.put("ICD0001", "Algoritmid ja andmestruktuurid");
        subjects.put("ICD0013", "Tarkvaratehnika");
        subjects.put("ICY0031", "Ettevõtluse alused ja ärisuhtlus");
        subjects.put("ICA0019", "Arvutivõrkude alused");
        subjects.put("ICD0006", "JavaScript");

        for (String key : subjects.keySet()) {
            System.out.println(key);
        }

        subjects.remove("ICY0031");

        for (Map.Entry<String, String> map : subjects.entrySet()) {
            System.out.println("Key: " + map.getKey() + " | " + "Values: " + map.getValue());
        }


        System.out.println("Before reverse:  " + randList);
        reverseList(randList);
        System.out.println("After reverse: " + randList);

        System.out.println("Maximum: " + maximum(randList));
    }


    /**
     * Finding the maximal element.
     *
     * @param a Collection of Comparable elements
     * @return maximal element.
     * @throws NoSuchElementException if <code> a </code> is empty.
     */
    static public <T extends Object & Comparable<? super T>>
    T maximum(Collection<? extends T> a)
            throws NoSuchElementException {
        if (a.isEmpty()) {
            throw new EmptyStackException();
        }
        return Collections.max(a);
    }

    /**
     * Counting the number of words. Any number of any kind of
     * whitespace symbols between words is allowed.
     *
     * @param text text
     * @return number of words in the text
     */
    public static int countWords(String text) {
        return text.trim().replaceAll("\\s+", " ").split(" ").length;
    }

    /**
     * Case-reverse. Upper -> lower AND lower -> upper.
     *
     * @param s string
     * @return processed string
     */
    public static String reverseCase(String s) {
        StringBuilder result = new StringBuilder();

        for (char c : s.toCharArray()) {
            if (!Character.isAlphabetic(c)) {
                result.append(c);
            } else if (Character.isLowerCase(c)) {
                result.append(String.valueOf(c).toUpperCase());
            } else {
                result.append(String.valueOf(c).toLowerCase());
            }
        }
        return result.toString();
    }

    /**
     * List reverse. Do not create a new list.
     *
     * @param list list to reverse
     */
    public static <T> void reverseList(List<T> list)
            throws UnsupportedOperationException {
        int start = 0;
        int end = list.size() - 1;

        while (start < end) {
            Collections.swap(list, start, end);
            start++;
            end--;
        }
    }
}

