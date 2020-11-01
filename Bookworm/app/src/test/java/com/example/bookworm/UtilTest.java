package com.example.bookworm;

import com.example.bookworm.util.Util;

import org.junit.Test;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Util.java
 */
public class UtilTest {
    /**
     * Tests for the correctness of getRandomEmail
     */
    @Test
    public void randomString() {
        int l = new Random().nextInt(500);
        String s = Util.getRandomString(l);
        assertEquals(s.length(), l);
    }

    /**
     * Tests for the correctness of getRandomEmail
     */
    @Test
    public void randomEmail() {
        int l = new Random().nextInt(500);
        String e = Util.getRandomEmail(l);

        // Ensure length is correct
        assertEquals(e.length(), l);

        // Ensure correct format of email
        Pattern p = Pattern.compile("\\w+@[a-z]+\\.com");
        Matcher m = p.matcher(e);
        assertTrue(m.find());
    }

    /**
     * Tests for the correctness of getRandomEmail
     */
    @Test
    public void randomPhoneNumber() {
        String num = Util.getRandomPhoneNumber();

        // Searches for a match
        Pattern p = Pattern.compile("(\\d{3}-\\d{4}-\\d{4})");
        Matcher m = p.matcher(num);
        assertTrue(m.find());

        // Entire string is a match, the number isn't somewhere inside the string
        assertEquals(m.start(), 0);
        assertEquals(m.end(), num.length());
    }

    @Test
    public void validateEmail() {
        // correct emails
        assertTrue(Util.validateEmail("psaunder@ualberta.ca"));
        assertTrue(Util.validateEmail("psaunder@hotmail.com"));
        assertTrue(Util.validateEmail("psaunder@gmail.com"));
        assertTrue(Util.validateEmail("_psaunder@ualberta.ca"));
        assertTrue(Util.validateEmail("1psaunder@ualberta.ca"));

        // Incorrect emails
        assertFalse(Util.validateEmail("psaunder@ualbertaca"));
        assertFalse(Util.validateEmail("psaunderualberta.ca"));
        assertFalse(Util.validateEmail("psaunder@.ca"));
        assertFalse(Util.validateEmail("@ualberta.ca"));
        assertFalse(Util.validateEmail("psaunder@ualbertaca"));

        // Miscallaneous values
        assertFalse(Util.validateEmail("Hello, world!"));
        assertFalse(Util.validateEmail(""));
        assertFalse(Util.validateEmail("@."));
        assertFalse(Util.validateEmail(" "));

    }

    /**
     * Tests for the correctness of validatePhoneNumber
     */
    @Test
    public void validatePhoneNumber() {
        // Correct phone numbers
        assertTrue(Util.validatePhoneNumber("7802467244"));
        assertTrue(Util.validatePhoneNumber("780 246 7244"));
        assertTrue(Util.validatePhoneNumber("780 246-7244"));
        assertTrue(Util.validatePhoneNumber("780-246 7244"));
        assertTrue(Util.validatePhoneNumber("(780) 246 7244"));
        assertTrue(Util.validatePhoneNumber("(780) 246-7244"));
        assertTrue(Util.validatePhoneNumber("+17802467244"));
        assertTrue(Util.validatePhoneNumber("+197802467244"));
        assertTrue(Util.validatePhoneNumber("+1 7802467244"));
        assertTrue(Util.validatePhoneNumber("+19 7802467244"));

        // Incorrect phone numbers
        assertFalse(Util.validatePhoneNumber("780246724"));
        assertFalse(Util.validatePhoneNumber("78024672444"));
        assertFalse(Util.validatePhoneNumber("7 80246724"));
        assertFalse(Util.validatePhoneNumber("7802 46724"));
        assertFalse(Util.validatePhoneNumber("78024672 4"));
        assertFalse(Util.validatePhoneNumber("9 780246724"));
        assertFalse(Util.validatePhoneNumber("-1 7802467244"));
        assertFalse(Util.validatePhoneNumber("11 7802467244 1"));

        // Miscellaneous
        assertFalse(Util.validatePhoneNumber("hello, world"));
        assertFalse(Util.validatePhoneNumber(""));
        assertFalse(Util.validatePhoneNumber("  "));
        assertFalse(Util.validatePhoneNumber("--"));
        assertFalse(Util.validatePhoneNumber("(fff) fff ffff"));
    }
}
