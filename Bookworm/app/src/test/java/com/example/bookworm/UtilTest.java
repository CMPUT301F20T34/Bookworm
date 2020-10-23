package com.example.bookworm;

import com.example.bookworm.util.Util;

import org.junit.Test;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UtilTest {
    @Test
    public void randomString() {
        int l = new Random().nextInt(500);
        String s = Util.getRandomString(l);
        assertEquals(s.length(), l);
    }

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
}
