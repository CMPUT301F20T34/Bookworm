package com.example.bookworm;

import java.util.ArrayList;

/**
 * An interface for a book user, an owner or a borrower.
 */
public interface BookUser {
    public void addBook(Book book);
    public ArrayList<Book> getBooks();
    public void deleteBook(Book book);
}
