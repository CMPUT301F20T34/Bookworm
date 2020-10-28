package com.example.bookworm;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DatabaseTest {

    @Test
    public void testLibrary(){
        Book exampleBook = new Book("Harry Potter", "J.K Rowling", "Available");
        Owner exampleOwner = new Owner("Mike", "hunter2", "mike@hotmail.com", "592-441-0235");
        Borrower exampleBorrower = new Borrower(exampleOwner);
        Request exampleReq = new Request(exampleBook, exampleBorrower, "Status");
        Library exampleLibrary = new Library();
        exampleLibrary.addOwner(exampleOwner);
        exampleLibrary.addBorrower(exampleBorrower);
        exampleLibrary.addBook(exampleBook);
        exampleLibrary.addRequest(exampleReq);

        Database.createListener(exampleLibrary);
        Database.writeLibrary(exampleLibrary);

        assertEquals("Harry Potter", exampleLibrary.getBooks().get(0).getTitle());

        Library testLibrary = new Library();
        Book testBook = new Book("LOTR", "J.R.R Tolkien", "Available");
        exampleLibrary.addBook(testBook);
        Database.createListener(testLibrary);
        Database.writeLibrary(exampleLibrary);

        assertEquals("LOTR", testLibrary.getBooks().get(1).getTitle());
    }

}
