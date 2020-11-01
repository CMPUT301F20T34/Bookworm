package com.example.bookworm;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DatabaseTest {

    // Mock classes for testing purposes.
    private User mockUser() {
        return new User("Mike", "hunter2", "mike@hotmail.com", "592-441-0235");
    }

    private Book mockBook() {
        return new Book("Harry Potter", "J.K Rowling", "Available", mockUser().getUsername());
    }

    private Request mockRequest() {
        return new Request(mockBook(), mockUser(), "Status");
    }

    /**
     * Tests the functionality for writing to and retrieving
     * a library from the database.
     */
    @Test
    public void testWriteLibrary(){
        User exampleUser = mockUser();
        Book exampleBook = mockBook();
        Request exampleReq = mockRequest();
        Library exampleLibrary = new Library();
        exampleLibrary.addUser(exampleUser);
        exampleLibrary.addBook(exampleBook);
        exampleLibrary.addRequest(exampleReq);

        Database.writeLibrary(exampleLibrary);

        Database.createListener();
        Database.writeLibrary(exampleLibrary);

        assertEquals("Harry Potter", exampleLibrary.getBooks().get(0).getTitle());

        Library testLibrary = new Library();
        Book testBook = new Book("LOTR", "J.R.R Tolkien", "Available");
        exampleLibrary.addBook(testBook);
        Database.createListener();
        Database.writeLibrary(exampleLibrary);

        assertEquals("LOTR", testLibrary.getBooks().get(1).getTitle());
    }

//    @Test
//    public void test
}