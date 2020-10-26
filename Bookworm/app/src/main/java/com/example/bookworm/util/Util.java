package com.example.bookworm.util;

import java.util.Random;

public class Util {

    /**
     * Returns a random string consisting of letters and numbers
     *
     * @param length length of random string
     * @return String
     */
    static public String getRandomString(int length) {

        // Initialize variables
        StringBuilder sb = new StringBuilder(length);
        String lettersAndNumbers = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int randomInt = 0;

        // Iterate over the length of the new string
        for (int i = 0; i < length; i++) {

            // Random index from lettersAndNumbers
            randomInt = new Random().nextInt(lettersAndNumbers.length());

            // Append character at above index
            sb.append(lettersAndNumbers.charAt(randomInt));
        }

        return sb.toString();
    }

    /**
     * Returns a random email of length 'length'
     *
     * @param length length of random email
     * @return String
     */
    static public String getRandomEmail(int length) {
        String[] sites = {"gmail", "hotmail", "mac", "yahoo"};
        String site = "@" + sites[new Random().nextInt(sites.length)] + ".com";
        return Util.getRandomString(length - site.length()) + site;
    }

    /**
     * Returns a random phone number
     *
     * @return String
     */
    static public String getRandomPhoneNumber() {
        int randomInt = 0;
        int lengths[] = {3, 4, 4};
        String nums = "01234567890";
        String num = "";

        for (int l : lengths) {
            for (int i = 0; i < l; i++) {
                // Random index from lettersAndNumbers
                randomInt = new Random().nextInt(nums.length());

                // Append character at above index
                num += nums.charAt(randomInt);
            }
            num += "-";
        }

        return num.substring(0, num.length() - 1);
    }
}
