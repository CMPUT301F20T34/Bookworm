package com.example.bookworm.util;

import java.util.Random;

public class Util {

    /**
     * Returns a random string consisting of letters and numbers
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
}
