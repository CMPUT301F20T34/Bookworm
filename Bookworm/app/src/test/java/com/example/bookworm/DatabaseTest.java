package com.example.bookworm;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DatabaseTest {

    @Test
    public void testLibrary(){
        User exampleUser = new User("Mike", "hunter2", "mike@hotmail.com", "592-441-0235");
        Book exampleBook = new Book("Harry Potter", "J.K Rowling", "Available", exampleUser);
        Request exampleReq = new Request(exampleBook, exampleUser, "Status");
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

}
