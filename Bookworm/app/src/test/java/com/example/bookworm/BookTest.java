package com.example.bookworm;


import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test Class for Book.java
 */
public class BookTest {

    private Book book;

    /**
     * Create an an empty book object before each test
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        book = new Book();
    }


    /**
     * Check getter an setter for title attribute
     */
    @Test
    public void testTitleGetterAndSetter() {
        assertEquals(null, book.getTitle());
        book.setTitle("Harry Potter");
        assertEquals("Harry Potter", book.getTitle());
    }

    /**
     * Check getter an setter for author attribute
     */
    @Test
    public void testAuthorGetterAndSetter() {
        assertEquals(null, book.getAuthor());
        book.setAuthor("JK . Rowling");
        assertEquals("JK . Rowling", book.getAuthor());
    }

    /**
     * Check getter an setter for isbn attribute
     */
    @Test
    public void testIsbnGetterAndSetter() {
        assertEquals(null, book.getIsbn());
        book.setIsbn("1234567890");
        assertEquals("1234567890", book.getIsbn());
    }

    /**
     * Check getter an setter for description attribute
     */
    @Test
    public void testDescriptionGetterAndSetter() {
        assertEquals(null, book.getDescription());
        ArrayList<String> description = new ArrayList<String>();
        description.add("fantasy");
        description.add("magic");
        description.add("classic");
        book.setDescription(description);
        ArrayList<String> getDescription = book.getDescription();
        assertTrue(getDescription.contains("fantasy"));
        assertTrue(getDescription.contains("magic"));
        assertTrue(getDescription.contains("classic"));
        assertFalse(getDescription.contains(""));
    }

    /**
     * Check getter an setter for status attribute.
     * Check that status is one of {"avaiable", "requested", "accepted", "borrowed"}.
     */
    @Test
    public void testStatusGetterAndSetter() {
        assertEquals(null, book.getStatus());
        try {
            book.setStatus("available");
        } catch (Exception e) {
            fail("failed setting status as available");
        }
        assertEquals("available", book.getStatus());
        try {
            book.setStatus("requested");
        } catch (Exception e) {
            fail("failed setting status as requested");
        }
        assertEquals("requested", book.getStatus());
        try {
            book.setStatus("accepted");
        } catch (Exception e) {
            fail("failed setting status as accepted");
        }
        assertEquals("accepted", book.getStatus());
        try {
            book.setStatus("borrowed");
        } catch (Exception e) {
            fail("failed setting status as borrowed");
        }
        assertEquals("borrowed", book.getStatus());
        try {
            book.setStatus("invalid status");
            fail("did not throw an exception when setting an invalid status");
        } catch (Exception e) {

        }
    }

    /**
     * Check getter an setter for owner attribute
     */
    @Test
    public void testOwnerGetterAndSetter() {
        assertEquals(null, book.getOwner());
        book.setOwner("test owner");
        assertEquals("test owner", book.getOwner());
    }

    /**
     * Check getter an setter for ownerId attribute
     */
    @Test
    public void testOwnerIdGetterAndSetter() {
        assertEquals(null, book.getOwnerId());
        book.setOwnerId("test owner id");
        assertEquals("test owner id", book.getOwnerId());
    }

    /**
     * Check getter an setter for borrower attribute
     */
    @Test
    public void testBorrowerGetterAndSetter() {
        assertEquals(null, book.getBorrower());
        book.setBorrower("test borrower");
        assertEquals("test borrower", book.getBorrower());
    }

    /**
     * Check getter an setter for borrowerId attribute
     */
    @Test
    public void testBorrowerIdGetterAndSetter() {
        assertEquals(null, book.getBorrowerId());
        book.setBorrowerId("test borrower id");
        assertEquals("test borrower id", book.getBorrowerId());
    }

    /**
     * Check getter an setter for photoGraph attribute
     */
    @Test
    public void testPhotographGetterAndSetter() {
        assertEquals(null, book.getDrawablePhotograph());
        book.setPhotograph("serialized photograph");
        assertEquals("serialized photograph", book.getPhotograph());
    }
}
