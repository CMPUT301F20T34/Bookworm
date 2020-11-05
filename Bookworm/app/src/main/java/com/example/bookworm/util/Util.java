package com.example.bookworm.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * Validates that a given email is formatted correctly
     * https://www.tutorialspoint.com/validate-email-address-in-java
     * Accessed July 28th, 2020
     * @param email the email to test
     * @return boolean
     */
    static public boolean validateEmail(String email) {
        Pattern p = Pattern.compile("(^[\\w-_\\.+]*[\\w-_\\.]\\@(?:[\\w]+\\.)+[\\w]+[\\w]$)");
        Matcher m = p.matcher(email);
        return (m.find() && m.start() == 0 && m.end() == email.length());
    }

    /**
     * Validates that a given phone number is formatted correctly
     * https://www.baeldung.com/java-regex-validate-phone-numbers
     * Accessed October 28th, 2020
     * @param num the phone number to test
     * @return boolean
     */
    static public boolean validatePhoneNumber(String num) {
        Pattern p = Pattern.compile("(^(?:\\+\\d{1,3}(?: )?)?(?:(?:\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$)");
        Matcher m = p.matcher(num);
        return (m.find() && m.start() == 0 && m.end() == num.length());
    }

    /**
     * Used to convert a string into a bitmap
     * @param encodedString the string to convert
     * @return The string converted into a bitmap
     */
    static public Bitmap stringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    static public String bitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    /* Following methods are from
     * https://en.proft.me/2017/08/2/how-work-bitmap-android/
     * Accessed October 31st, 2020
     */

    /**
     * Resizes a bitmap to a set width, keeping the aspect ratio
     * https://stackoverflow.com/questions/23005948/convert-string-to-bitmap
     * Accessed October 31st, 2020
     * @param b the bitmap to resize
     * @param width the target width of the resulting bitmap
     * @return the new, resized bitmap
     */
    public static Bitmap scaleToFitWidth(Bitmap b, int width) {
        float factor = width / (float) b.getWidth();
        return Bitmap.createScaledBitmap(b, width, (int) (b.getHeight() * factor), true);
    }

    /**
     * Resizes a bitmap to a set height, keeping the aspect ratio
     * @param b the bitmap to resize
     * @param height the target height of the resulting bitmap
     * @return the new, resized bitmap
     */
    public static Bitmap scaleToFitHeight(Bitmap b, int height) {
        float factor = height / (float) b.getHeight();
        return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factor), height, true);
    }

}
